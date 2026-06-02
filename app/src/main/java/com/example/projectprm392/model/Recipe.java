package com.example.projectprm392.model;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.util.List;
public class Recipe {
    private int recipeId;
    private String title;
    private String description;
    private String instructions;
    private Integer prepTime;
    private Integer cookTime;
    private String difficulty;
    private int authorId;
    private String createdAt;
    private User author;
    private List<Favorite> favorites;
    private List<RecipeCategory> recipeCategories;
    private List<RecipeIngredient> recipeIngredients;
    private List<RecipeStep> recipeSteps;
    private boolean isManage;

    public boolean isManage() {
        return isManage;
    }

    public void setManage(boolean manage) {
        isManage = manage;
    }

    // lẤY TẤT CẢ Recipe tổng quan
    private String authorName;

    private String thumbnail;
    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    private int favoriteCount;
    private Integer totalTime;

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    // Constructors
    public Recipe() {}

    // Getters and Setters
    public int getRecipeId() { return recipeId; }
    public void setRecipeId(int recipeId) { this.recipeId = recipeId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public Integer getPrepTime() { return prepTime; }
    public void setPrepTime(Integer prepTime) { this.prepTime = prepTime; }

    public Integer getCookTime() { return cookTime; }
    public void setCookTime(Integer cookTime) { this.cookTime = cookTime; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public int getAuthorId() { return authorId; }
    public void setAuthorId(int authorId) { this.authorId = authorId; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }

    public List<Favorite> getFavorites() { return favorites; }
    public void setFavorites(List<Favorite> favorites) { this.favorites = favorites; }

    public List<RecipeCategory> getRecipeCategories() { return recipeCategories; }
    public void setRecipeCategories(List<RecipeCategory> recipeCategories) {
        this.recipeCategories = recipeCategories;
    }

    public List<RecipeIngredient> getRecipeIngredients() { return recipeIngredients; }
    public void setRecipeIngredients(List<RecipeIngredient> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public List<RecipeStep> getRecipeSteps() { return recipeSteps; }
    public void setRecipeSteps(List<RecipeStep> recipeSteps) {
        this.recipeSteps = recipeSteps;
    }
}