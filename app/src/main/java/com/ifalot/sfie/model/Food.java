package com.ifalot.sfie.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import com.ifalot.sfie.util.Database;
import org.parceler.Parcel;

import java.util.*;

@Parcel
public class Food {

    public static final char INGR_SEPARATOR = ';';
    public static final char INGR_EQUAL = ':';

    int id;
    String name;
    HashMap<String, Float> quantities;

    public Food(){}

    public Food(int id, String name){
        this(id, name, null);
    }

    public Food(int id, String name, String ingredients){
        this.id = id;
        this.name = name;
        quantities = new HashMap<>();
        if(ingredients != null){
            String[] ingrs = ingredients.split(String.valueOf(INGR_SEPARATOR));
            for(String i : ingrs){
                String[] tmp = i.split(String.valueOf(INGR_EQUAL));
                quantities.put(tmp[0], Float.valueOf(tmp[1]));
            }
        }
    }

    public void addIngredient(String name, float quantity){
        quantities.put(name, quantity);
    }

    public String getIngredientsString(){
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for(Map.Entry<String, Float> e : quantities.entrySet()){
            i++;
            sb.append(e.getKey()).append(INGR_EQUAL).append(e.getValue());
            if(i != quantities.size()) sb.append(INGR_SEPARATOR);
        }
        return sb.toString();
    }

    public String getIngredientsInNiceListing(){
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for(Map.Entry<String, Float> e : quantities.entrySet()){
            i++;
            Object value;
            if(Math.abs(Math.round(e.getValue()) - e.getValue()) < 0.0001) value = e.getValue().intValue();
            else value = e.getValue();
            sb.append("- ").append(e.getKey()).append(":  ").append(value);
            if(i != quantities.size()) sb.append('\n');
        }
        return sb.toString();
    }

    public Set<String> getIngredients(){
        return quantities.keySet();
    }

    public HashMap<String, Float> getQuantities() {
        return quantities;
    }

    public void insertIntoDatabase() throws SQLiteException {
        SQLiteDatabase db = Database.getDB();
        String query = "INSERT INTO food (id, name, ingredients) VALUES(?, ?, ?)";
        Object[] args = { id, name, getIngredientsString() };
        db.execSQL(query, args);
    }

    public float getQuantity(String ingredient){
        return quantities.get(ingredient);
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    @Override
    public String toString() {
        if(quantities.size() == 0) return name;
        String res = name + " (";
        for (String ing : quantities.keySet()) res += ing + ",";
        return res.substring(0, res.length()-1)+")";
    }

    public int getId(){
        return id;
    }

    public static ArrayList<Food> getFoodsInDB(){
        SQLiteDatabase db = Database.getDB();
        String query = "SELECT * FROM food";
        Cursor c = db.rawQuery(query, null);
        ArrayList<Food> res = new ArrayList<>();
        if(c.moveToFirst()){
            do res.add(new Food(c.getInt(0), c.getString(1), c.getString(2)));
            while (c.moveToNext());
        }
        c.close();
        return res;
    }

}
