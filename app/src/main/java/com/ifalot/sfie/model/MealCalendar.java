package com.ifalot.sfie.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import com.ifalot.sfie.util.Database;

import java.util.*;

public class MealCalendar {

    private TreeMap<Integer, Meal> meals;
    private int topid;

    private MealCalendar(){
        topid = -1;
        meals = new TreeMap<>();
    }

    public void addMeal(Meal meal){
        int tmpId = meal.getId();
        if (tmpId >= topid) topid = tmpId + 1;
        if (tmpId == -1) {
            meal.setId(topid);
            String query = "INSERT INTO calendar VALUES(?, ?, ?)";
            SQLiteDatabase db = Database.getDB();
            Object[] args = { topid, meal.getType(), meal.getDate().getTime() };
            db.execSQL(query, args);
            topid++;
            meal.insertIntoDatabase();
        }
        meals.put(meal.getId(), meal);
    }

    public ArrayList<Meal> getMeals(){
        return new ArrayList<>(meals.values());
    }

    public static MealCalendar getMealCalendar(Date today){
        MealCalendar tmp = new MealCalendar();
        SQLiteDatabase db = Database.getDB();
        Cursor cursor = null, mealCursor = null;
        String query = "SELECT id, name, date FROM calendar WHERE date >= ? ORDER BY date";
        String subquery = "SELECT id, name, ingredients FROM meal, food WHERE calendarid = ?";
        String[] selectionArgs = {String.valueOf(today.getTime())};
        try {
            cursor = db.rawQuery(query, selectionArgs);
            if (cursor.moveToFirst()) {
                do {
                    int mealId = cursor.getInt(0);
                    String mealName = cursor.getString(1);
                    long mealDate = cursor.getLong(2);
                    String[] subArgs = {String.valueOf(mealId)};
                    Meal meal = new Meal(new Date(mealDate), mealName);
                    meal.setId(mealId);
                    mealCursor = db.rawQuery(subquery, subArgs);
                    if(mealCursor.moveToFirst()){
                        do {
                            int foodid = mealCursor.getInt(0);
                            String foodName = mealCursor.getString(1);
                            String ingrs = mealCursor.getString(2);
                            meal.addFood(new Food(foodid, foodName, ingrs));
                        } while (mealCursor.moveToNext());
                    }
                    tmp.addMeal(meal);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException e) {
            Log.d("MealCalendar", "Failed to read database");
        } finally {
            if(cursor != null) cursor.close();
            if(mealCursor != null) mealCursor.close();
        }
        return tmp;
    }

}
