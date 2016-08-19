package com.ifalot.sfie.model;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import com.ifalot.sfie.util.Database;
import org.parceler.Parcel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

@Parcel
public class Meal {

    int id;
    long date;
    String type;
    LinkedList<Food> foods;

    public Meal(){}

    public Meal(long date, String type){
        this.id = -1;
        this.date = date;
        this.type = type;
        this.foods = new LinkedList<>();
    }

    public Meal(Date date, String name){
        this(date.getTime(), name);
    }

    public void addFood(Food food){
        foods.add(food);
    }

    public Food getFood(String name){
        for(Food f : foods){
            if(f.getName().equals(name)) return f;
        }
        return null;
    }

    public int getFoodCount(){
        return foods.size();
    }

    public void insertIntoDatabase() throws SQLiteException {
        SQLiteDatabase db = Database.getDB();
        String query = "INSERT INTO meal (calendarid, foodid) VALUES(?, ?)";
        for(Food f : foods){
            Integer[] args = { id, f.getId() };
            db.execSQL(query, args);
        }
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public Date getDate(){
        return new Date(date);
    }

    public String getType(){
        return type;
    }

    @Override
    public String toString() {
        return type + " of " + new SimpleDateFormat("d MMM y").format(new Date(date));
    }
}
