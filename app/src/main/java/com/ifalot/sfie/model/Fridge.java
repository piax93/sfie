package com.ifalot.sfie.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import com.ifalot.sfie.util.Database;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Fridge {

    public static final String theEndTag = "theend";
    private HashMap<Ingredient, Float> supplies;
    private Date theEnd;

    Fridge(){
        theEnd = null;
        supplies = new HashMap<>();
    }

    public HashMap<Ingredient, Float> getSupplies(){
        return this.supplies;
    }

    public void setTheEnd(long end){
        theEnd = new Date(end);
    }

    public void consume(Meal meal){
        boolean neg = false;
        HashMap<Ingredient, Float> required = meal.getNeededIngredients();
        for(Map.Entry<Ingredient, Float> e : required.entrySet()) {
            Float base = 0f;
            if (supplies.containsKey(e.getKey())) base = supplies.get(e.getKey());
            base = base - e.getValue();
            if(base < 0f) neg = true;
            supplies.put(e.getKey(), base);
        }
        if(neg) {
            if(theEnd == null) theEnd = meal.getDate();
            else if(meal.getDate().getTime() < theEnd.getTime()) theEnd = meal.getDate();
        }
    }

    public void addSupply(Ingredient ingredient, float quantity){
        supplies.put(ingredient, quantity);
    }

    public Float getQuantity(Ingredient ingredient){
        return supplies.get(ingredient);
    }

    public void save(){
        String query = "INSERT OR REPLACE INTO fridge (name, quantity) VALUES(?,?)";
        SQLiteDatabase db = Database.getDB();
        try {
            db.beginTransaction();
            for(Map.Entry<Ingredient, Float> e : supplies.entrySet()){
                Object[] args = { e.getKey().getName(), e.getValue() };
                db.execSQL(query, args);
            }
            if(theEnd != null){
                Object[] args = { theEndTag, theEnd.getTime() };
                db.execSQL(query, args);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public static Fridge load(){
        Cursor c = null;
        Fridge f = new Fridge();
        try {
            String query = "SELECT * FROM fridge";
            c = Database.getDB().rawQuery(query, null);
            if(c.moveToFirst()){
                do {
                    String name = c.getString(0);
                    if(name.equals(theEndTag)) f.setTheEnd(Float.valueOf(c.getFloat(1)).longValue());
                    else f.addSupply(new Ingredient(name), c.getFloat(1));
                } while (c.moveToNext());
            }
        } catch (SQLiteException e){
            Log.d("Fridge", "Error loading the fridge");
        } finally {
            if(c != null) c.close();
        }
        return f;
    }

}
