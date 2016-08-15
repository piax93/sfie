package com.ifalot.sfie.model;

import java.util.HashMap;

public class Food {

    private int id;
    private String name;
    private HashMap<Ingredient, Float> quantities;

    public Food(String name){
        this.name = name;
        quantities = new HashMap<>();
    }

    public void addIngredient(String name, float quantity){
        Ingredient tmp = new Ingredient(name);
        quantities.put(tmp, quantity);
    }

    public float getQuantity(String ingredient){
        return quantities.get(new Ingredient(ingredient));
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public int getId(){
        return id;
    }

}
