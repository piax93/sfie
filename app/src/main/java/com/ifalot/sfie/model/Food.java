package com.ifalot.sfie.model;

import java.util.HashMap;
import java.util.Map;

public class Food {

    private static String INGR_SEPARATOR = ";";
    private static String INGR_EQUAL = ":";

    private int id;
    private String name;
    private HashMap<Ingredient, Float> quantities;

    public Food(int id, String name){
        this(id, name, null);
    }

    public Food(int id, String name, String ingredients){
        this.id = id;
        this.name = name;
        quantities = new HashMap<>();
        if(ingredients != null){
            String[] ingrs = ingredients.split(INGR_SEPARATOR);
            for(String i : ingrs){
                String[] tmp = i.split(INGR_EQUAL);
                quantities.put(new Ingredient(tmp[0]), Float.valueOf(tmp[1]));
            }
        }
    }

    public void addIngredient(String name, float quantity){
        Ingredient tmp = new Ingredient(name);
        quantities.put(tmp, quantity);
    }

    public String getIngredientsString(){
        StringBuffer sb = new StringBuffer();
        int i = 0;
        for(Map.Entry<Ingredient, Float> e : quantities.entrySet()){
            i++;
            sb.append(e.getKey().getName()).append(INGR_EQUAL).append(e.getValue());
            if(i != quantities.size()) sb.append(INGR_SEPARATOR);
        }
        return sb.toString();
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
