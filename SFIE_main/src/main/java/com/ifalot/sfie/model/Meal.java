package com.ifalot.sfie.model;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import com.ifalot.sfie.util.CIHashMap;
import com.ifalot.sfie.util.Database;

import java.text.SimpleDateFormat;
import java.util.*;

public class Meal {

    public final static String[] meal_type = {"Lunch", "Dinner"};

    private int id;
    private long date;
    private String type;
    private LinkedList<Food> foods;

    public Meal(long date, String type) {
        this.id = -1;
        this.date = date + (type.equals(meal_type[1]) ? 1 : 0);
        this.type = type;
        this.foods = new LinkedList<>();
    }

    public Meal(Date date, String name) {
        this(date.getTime(), name);
    }

    public void addFood(Food food) {
        foods.add(food);
    }

    public List<Food> getFoods() {
        return foods;
    }

    public Food getFood(String name) {
        for (Food f : foods) {
            if (f.getName().equals(name)) return f;
        }
        return null;
    }

    public int getFoodCount() {
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

    public String getFoodDetailsToString() {
        StringBuilder sb = new StringBuilder();
        for (Food f : foods) {
            int i = 0;
            sb.append("- ").append(f.getName()).append(" (");
            Set<String> ingrs = f.getIngredients();
            for (String ing : ingrs) {
                i++;
                sb.append(ing);
                if (i != ingrs.size()) sb.append(", ");
            }
            sb.append(")\n");
        }
        return sb.toString();
    }

    public void deleteFromDatabase() {
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

    public CIHashMap<Float> getNeededIngredients() {
        CIHashMap<Float> res = new CIHashMap<>();
        for (Food f : foods) {
            for (Map.Entry<String, Float> e : f.getQuantities().entrySet()) {
                if (res.containsKey(e.getKey())) {
                    res.put(e.getKey(), e.getValue() + res.get(e.getKey()));
                } else {
                    res.put(e.getKey(), e.getValue());
                }
            }
        }
        return res;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return new Date(date);
    }

    public long getDateLong() {
        return date;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return type + " of " + new SimpleDateFormat("EEE d MMM y", Locale.US).format(new Date(date));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Meal && id == ((Meal) obj).getId();
    }

}
