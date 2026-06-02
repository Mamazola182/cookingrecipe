package com.example.projectprm392.model;

import java.util.List;

public class Category {
    private int categoryId;
    private String name;
    private String description;
    private String createdAt;
    private List<RecipeCategory> recipeCategories;

    // Constructors
    public Category() {}

    public Category(int categoryId, String name, String description, String createdAt) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public List<RecipeCategory> getRecipeCategories() { return recipeCategories; }
    public void setRecipeCategories(List<RecipeCategory> recipeCategories) {
        this.recipeCategories = recipeCategories;
    }
}
