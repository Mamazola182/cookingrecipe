package com.example.projectprm392.model;

public class RecipeCategory {
    private int recipeCategoryId;
    private int recipeId;
    private int categoryId;
    private String createdAt;
    private Category category;
    private Recipe recipe;

    // Constructors
    public RecipeCategory() {}

    // Getters and Setters
    public int getRecipeCategoryId() { return recipeCategoryId; }
    public void setRecipeCategoryId(int recipeCategoryId) {
        this.recipeCategoryId = recipeCategoryId;
    }

    public int getRecipeId() { return recipeId; }
    public void setRecipeId(int recipeId) { this.recipeId = recipeId; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public Recipe getRecipe() { return recipe; }
    public void setRecipe(Recipe recipe) { this.recipe = recipe; }
}
