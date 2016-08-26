package com.ifalot.sfie.model;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import com.ifalot.sfie.util.Database;
import org.parceler.Parcel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
        try {
            String query = "INSERT INTO meal (calendarid, foodid) VALUES(?, ?)";
            db.beginTransaction();
            for (Food f : foods) {
                Integer[] args = {id, f.getId()};
                db.execSQL(query, args);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public String getFoodDetails(){
        StringBuilder sb = new StringBuilder();
        for(Food f : foods){
            int i = 0;
            sb.append("- ").append(f.getName()).append(" -> ");
            Set<Ingredient> ingrs = f.getIngredients();
            for(Ingredient ing : ingrs) {
                i++;
                sb.append(ing.getName());
                if(i != ingrs.size()) sb.append(", ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public void deleteFromDatabase(){
        SQLiteDatabase db = Database.getDB();
        try {
            Object[] args = {id};
            String mquery = "DELETE FROM meal WHERE calendarid = ?";
            String cquery = "DELETE FROM calendar WHERE id = ?";
            db.beginTransaction();
            db.execSQL(mquery, args);
            db.execSQL(cquery, args);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public HashMap<Ingredient, Float> getNeededIngredients(){
        HashMap<Ingredient, Float> res = new HashMap<>();
        for(Food f : foods){
            for(Map.Entry<Ingredient, Float> e : f.getQuantities().entrySet()){
                if(res.containsKey(e.getKey())){
                    res.put(e.getKey(), e.getValue() + res.get(e.getKey()));
                } else {
                    res.put(e.getKey(), e.getValue());
                }
            }
        }
        return res;
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

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Meal) return id == ((Meal)obj).getId();
        return false;
    }

}
