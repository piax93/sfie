package com.ifalot.sfie.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import com.ifalot.sfie.util.CIHashMap;
import com.ifalot.sfie.util.Database;
import com.ifalot.sfie.util.FridgeListAdapter;

import java.util.Date;
import java.util.Map;

public class Fridge {

    private static Fridge instance = null;

    private CIHashMap<Float> supplies;
    private CIHashMap<Long> theends;
    private FridgeListAdapter viewAdapter;
    private long theEnd;
    private boolean modified;

    private Fridge() {
        theEnd = -1;
        modified = false;
        supplies = new CIHashMap<>();
        theends = new CIHashMap<>();
    }

    public CIHashMap<Float> getSupplies() {
        return this.supplies;
    }

    public void updateTheEnd() {
        theEnd = Long.MAX_VALUE;
        for (Long e : theends.values()) {
            if (e > 0 && e < theEnd) theEnd = e;
        }
        if (theEnd == Long.MAX_VALUE) theEnd = -1;
    }

    public Date getTheEnd() {
        return theEnd > 0 ? new Date(theEnd) : null;
    }

    public void consume(Meal meal) {
        modified = true;
        boolean neg = false;
        for (Map.Entry<String, Float> e : meal.getNeededIngredients().entrySet()) {
            Float base = 0f;
            if (supplies.containsKey(e.getKey())) base = supplies.get(e.getKey());
            base = base - e.getValue();
            if (base < 0f) {
                neg = true;
                if (!theends.containsKey(e.getKey()) || theends.get(e.getKey()) > meal.getDateLong())
                    theends.put(e.getKey(), meal.getDateLong());
            }
            supplies.put(e.getKey(), base);
        }
        if (neg) {
            if (theEnd == -1) theEnd = meal.getDateLong();
            else if (meal.getDateLong() < theEnd) theEnd = meal.getDateLong();
        }
        viewAdapter.update();
    }

    public void vomit(Meal meal) {
        modified = true;
        for (Map.Entry<String, Float> e : meal.getNeededIngredients().entrySet()) {
            addSupply(e.getKey(), e.getValue());
        }
        viewAdapter.update();
    }

    public void addSupply(String ingredient, float quantity) {
        modified = true;
        if (supplies.containsKey(ingredient)) quantity += supplies.get(ingredient);
        supplies.put(ingredient, quantity);
        if (quantity > 0f) theends.put(ingredient, -1L);
    }

    public Float getQuantity(String ingredient) {
        return supplies.get(ingredient);
    }

    public void setViewUpdateListener(FridgeListAdapter fridgeListAdapter) {
        this.viewAdapter = fridgeListAdapter;
    }

    public void save() {
        if (!modified) return;
        String query = "INSERT OR REPLACE INTO fridge (name, quantity, theend) VALUES(?,?,?)";
        SQLiteDatabase db = Database.getDB();
        try {
            db.beginTransaction();
            for (Map.Entry<String, Float> e : supplies.entrySet()) {
                Object[] args = {e.getKey(), e.getValue(),
                        theends.containsKey(e.getKey()) ? theends.get(e.getKey()) : -1};
                db.execSQL(query, args);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private static Fridge load() {
        Cursor c = null;
        Fridge f = new Fridge();
        try {
            String query = "SELECT * FROM fridge ORDER BY quantity ASC";
            c = Database.getDB().rawQuery(query, null);
            if (c.moveToFirst()) {
                f.theEnd = Long.MAX_VALUE;
                do {
                    String name = c.getString(0);
                    f.supplies.put(name, c.getFloat(1));
                    long ingrend = c.getLong(2);
                    f.theends.put(name, ingrend);
                    if (ingrend > 0 && ingrend < f.theEnd) f.theEnd = ingrend;
                } while (c.moveToNext());
                if (f.theEnd == Long.MAX_VALUE) f.theEnd = -1;
            }
        } catch (SQLiteException e) {
            Log.d("Fridge", "Error loading the fridge");
        } finally {
            if (c != null) c.close();
        }
        return f;
    }

    public static Fridge getInstance() {
        if (instance == null) instance = load();
        return instance;
    }

}
