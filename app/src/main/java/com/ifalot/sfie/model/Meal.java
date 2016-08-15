package com.ifalot.sfie.model;

import java.util.Date;
import java.util.LinkedList;

public class Meal {

    private int id;
    private Date date;
    private LinkedList<Food> foods;

    public Meal(Date date){
        this.date = date;
        this.foods = new LinkedList<>();
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
        return date;
    }

}
