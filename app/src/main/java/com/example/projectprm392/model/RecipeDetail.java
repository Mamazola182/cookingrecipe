package com.example.projectprm392.model;

import java.io.Serializable;
import java.util.List;

public class RecipeDetail implements Serializable {
    private int recipeId;
    private String title;
    private String description;
    private String thumbnail;
    private int prepTime;
    private int cookTime;
    private int totalTime;
    private String difficulty;
    private String authorName;
    private String createdAt;

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    private int favoriteCount;
    private User author;
    private List<Ingredient> ingredients;
    private List<RecipeStep> steps;
    private boolean is_favorite;

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setSteps(List<RecipeStep> steps) {
        this.steps = steps;
    }

    public void setIs_favorite(boolean is_favorite) {
        this.is_favorite = is_favorite;
    }

    // Getters
    public int getRecipeId() { return recipeId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getThumbnail() { return thumbnail; }
    public int getPrepTime() { return prepTime; }
    public int getCookTime() { return cookTime; }
    public int getTotalTime(){ return totalTime; }
    public String getDifficulty() { return difficulty; }
    public User getAuthor() { return author; }
    public List<Ingredient> getIngredients() { return ingredients; }
    public List<RecipeStep> getSteps() { return steps; }
    public boolean isIs_favorite() { return is_favorite; }
}