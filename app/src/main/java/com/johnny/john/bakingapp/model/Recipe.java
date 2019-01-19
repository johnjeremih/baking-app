package com.johnny.john.bakingapp.model;


import com.johnny.john.bakingapp.model.Ingredients;
import com.johnny.john.bakingapp.model.Steps;

import java.io.Serializable;
import java.util.List;

public class Recipe implements Serializable{

    private int id;
    private String name;
    private String servings;
    private List<Ingredients> ingredient;
    private List<Steps> steps;



    public Recipe (int id, String name, String servings, List<Ingredients> ingredients,  List<Steps> steps ) {

    this.id = id;
    this.name = name;
    this.servings = servings;
    this.ingredient= ingredients;
    this.steps=steps;
    }


    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public String getServings() {
        return servings;
    }

    /////////////////////////////////////////////////////////////////////
    //ALT+Insert to create getters and setter

    public List<Ingredients> getIngredient() {
        return ingredient;
    }

    /////////////////////////////////////////////////////////////////////


    public List<Steps> getSteps() {
        return steps;
    }

}
