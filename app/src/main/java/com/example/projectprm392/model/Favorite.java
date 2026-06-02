package com.example.projectprm392.model;

public class Favorite {
    private int favoriteId;
    private int userId;
    private int recipeId;
    private String createdAt;
    private String recipeName;  // THÊM DÒNG NÀY
    private String userName;
    private Recipe recipe;
    private User user;

    // Constructors
    public Favorite() {}

    // Getters and Setters
    public int getFavoriteId() { return favoriteId; }
    public void setFavoriteId(int favoriteId) { this.favoriteId = favoriteId; }
    public String getRecipeName() { return recipeName; }
    public void setRecipeName(String recipeName) { this.recipeName = recipeName; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getRecipeId() { return recipeId; }
    public void setRecipeId(int recipeId) { this.recipeId = recipeId; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public Recipe getRecipe() { return recipe; }
    public void setRecipe(Recipe recipe) { this.recipe = recipe; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
