package com.ifalot.sfie.model;

import org.parceler.Parcel;

@Parcel
public class Ingredient {

    String name;

    public Ingredient(){}

    public Ingredient(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

}
