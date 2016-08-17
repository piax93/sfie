package com.ifalot.sfie.model;

import org.parceler.Parcel;

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

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public Date getDate(){
        return new Date(date);
    }

}
