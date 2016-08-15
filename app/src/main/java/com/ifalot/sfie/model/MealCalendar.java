package com.ifalot.sfie.model;

import android.database.Cursor;
import com.ifalot.sfie.util.Database;

import java.util.Date;
import java.util.HashMap;

public class MealCalendar {

    private HashMap<Integer, Meal> meals;

    public MealCalendar(){
        meals = new HashMap<>();
    }

    public void addMeal(Meal meal){
        Integer id = meals.size();
        meal.setId(id);
        meals.put(id, meal);
    }

    public static MealCalendar getMeals(Date today){
        MealCalendar tmp = new MealCalendar();
        String query = "SELECT * FROM calendar WHERE date >= ?";
        String[] selectionArgs = {String.valueOf(today.getTime())};
        Cursor cursor = Database.getDB().rawQuery(query, selectionArgs);
        return null;
    }

}
