package com.example.projectprm392.model;

import java.io.Serializable;
import java.util.List;

public class Ingredient implements Serializable {
    private int ingredientId;
    private String name;
    private String type;
    private String createdAt;
    private String quantity;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    private List<RecipeIngredient> recipeIngredients;

    // Constructors
    public Ingredient() {}

    public Ingredient(int ingredientId, String name, String type, String createdAt, String quantity) {
        this.ingredientId = ingredientId;
        this.name = name;
        this.type = type;
        this.createdAt = createdAt;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getIngredientId() { return ingredientId; }
    public void setIngredientId(int ingredientId) { this.ingredientId = ingredientId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public List<RecipeIngredient> getRecipeIngredients() { return recipeIngredients; }
    public void setRecipeIngredients(List<RecipeIngredient> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }
}
