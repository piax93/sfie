package com.ifalot.sfie.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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

}
