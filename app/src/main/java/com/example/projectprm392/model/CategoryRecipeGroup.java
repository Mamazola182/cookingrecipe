package com.example.projectprm392.model;

import java.util.List;

public class CategoryRecipeGroup {
    private String categoryName;
    private List<Recipe> recipes;

    public CategoryRecipeGroup(String categoryName, List<Recipe> recipes) {
        this.categoryName = categoryName;
        this.recipes = recipes;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public int getRecipeCount() {
        return recipes != null ? recipes.size() : 0;
    }
}