package com.ifalot.sfie.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import com.ifalot.sfie.util.Database;
import com.ifalot.sfie.util.FridgeListAdapter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Fridge {

    private static final String theEndTag = "theend";
    private static Fridge instance = null;

    private HashMap<String, Float> supplies;
    private FridgeListAdapter viewAdapter;
    private Date theEnd;
    private boolean modified;

    Fridge(){
        theEnd = null;
        modified = false;
        supplies = new HashMap<>();
    }

    public HashMap<String, Float> getSupplies(){
        return this.supplies;
    }

    public void setTheEnd(long end){
        if(end == -1) theEnd = null;
        else theEnd = new Date(end);
    }

    public Date getTheEnd(){
        return theEnd;
    }

    public void consume(Meal meal){
        modified = true;
        boolean neg = false;
        for(Map.Entry<String, Float> e : meal.getNeededIngredients().entrySet()) {
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
        viewAdapter.update();
    }

    public void vomit(Meal meal){
        modified = true;
        for(Map.Entry<String, Float> e : meal.getNeededIngredients().entrySet()){
            Float base = 0f;
            if (supplies.containsKey(e.getKey())) base = supplies.get(e.getKey());
            base = base + e.getValue();
            supplies.put(e.getKey(), base);
        }
        viewAdapter.update();
    }

    public void addSupply(String ingredient, float quantity){
        modified = true;
        if(supplies.containsKey(ingredient)) quantity += supplies.get(ingredient);
        supplies.put(ingredient, quantity);
    }

    public Float getQuantity(String ingredient){
        return supplies.get(ingredient);
    }

    public void setViewUpdateListener(FridgeListAdapter fridgeListAdapter){
        this.viewAdapter = fridgeListAdapter;
    }

    public void save(){
        if(!modified) return;
        String query = "INSERT OR REPLACE INTO fridge (name, quantity) VALUES(?,?)";
        SQLiteDatabase db = Database.getDB();
        try {
            db.beginTransaction();
            for(Map.Entry<String, Float> e : supplies.entrySet()){
                if(e.getKey().equals(theEndTag)) continue;
                Object[] args = { e.getKey(), e.getValue() };
                db.execSQL(query, args);
            }
            if(theEnd != null) {
                Object[] args = { theEndTag, theEnd.getTime() };
                db.execSQL(query, args);
            } else {
                Object[] args = { theEndTag, -1 };
                db.execSQL(query, args);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private static Fridge load(){
        Cursor c = null;
        Fridge f = new Fridge();
        try {
            String query = "SELECT * FROM fridge";
            c = Database.getDB().rawQuery(query, null);
            if(c.moveToFirst()){
                do {
                    String name = c.getString(0);
                    if(name.equals(theEndTag)) f.setTheEnd(Float.valueOf(c.getFloat(1)).longValue());
                    else f.addSupply(name, c.getFloat(1));
                } while (c.moveToNext());
            }
        } catch (SQLiteException e){
            Log.d("Fridge", "Error loading the fridge");
        } finally {
            if(c != null) c.close();
        }
        return f;
    }

    public static Fridge getInstance(){
        if(instance == null) instance = load();
        return instance;
    }

}