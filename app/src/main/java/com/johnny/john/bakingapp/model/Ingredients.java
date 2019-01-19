package com.johnny.john.bakingapp.model;

import java.io.Serializable;

public class Ingredients implements Serializable {

    private String quantity;
    private String measure;
    private String ingredient;

    public Ingredients(String quantity, String measure, String ingredient){
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient= ingredient;
    }


    public String getQuantity() {
        return quantity;
    }
    public String getMeasure() {
        return measure;
    }
    public String getIngredient() {
        return ingredient;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

}
