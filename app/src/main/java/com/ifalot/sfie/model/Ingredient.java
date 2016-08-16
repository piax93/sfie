package com.ifalot.sfie.model;

public class Ingredient {

    private String name;

    public Ingredient(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

}
