package com.ifalot.sfie.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Parcel;
import android.os.Parcelable;
import com.ifalot.sfie.util.CIHashMap;
import com.ifalot.sfie.util.Database;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class Food implements Parcelable, Comparable {

    public static final char INGR_SEPARATOR = ';';
    public static final char INGR_EQUAL = ':';

    private int id;
    private String name;
    private CIHashMap<Float> quantities;

    public Food(int id, String name){
        this(id, name, null);
    }

    public Food(int id, String name, String ingredients){
        this.id = id;
        this.name = name;
        quantities = new CIHashMap<>();
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

    public CIHashMap<Float> getQuantities() {
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
        String query = "SELECT * FROM food ORDER BY name";
        Cursor c = db.rawQuery(query, null);
        ArrayList<Food> res = new ArrayList<>();
        if(c.moveToFirst()){
            do res.add(new Food(c.getInt(0), c.getString(1), c.getString(2)));
            while (c.moveToNext());
        }
        c.close();
        return res;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeSerializable(this.quantities);
    }

    protected Food(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.quantities = (CIHashMap<Float>) in.readSerializable();
    }

    public static final Parcelable.Creator<Food> CREATOR = new Parcelable.Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel source) {
            return new Food(source);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    @Override
    public int compareTo(Object o) {
        return name.compareTo(((Food)o).name);
    }

}
