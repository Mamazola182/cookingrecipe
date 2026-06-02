package com.example.projectprm392.model;

public class RecipeIngredient {
    private int recipeIngredientId;
    private int recipeId;
    private int ingredientId;
    private String quantity;  // vd: 200g, 2 muỗng canh
    private Ingredient ingredient;
    private Recipe recipe;

    // Constructors
    public RecipeIngredient() {}

    // Getters and Setters
    public int getRecipeIngredientId() { return recipeIngredientId; }
    public void setRecipeIngredientId(int recipeIngredientId) {
        this.recipeIngredientId = recipeIngredientId;
    }

    public int getRecipeId() { return recipeId; }
    public void setRecipeId(int recipeId) { this.recipeId = recipeId; }

    public int getIngredientId() { return ingredientId; }
    public void setIngredientId(int ingredientId) { this.ingredientId = ingredientId; }

    public String getQuantity() { return quantity; }
    public void setQuantity(String quantity) { this.quantity = quantity; }

    public Ingredient getIngredient() { return ingredient; }
    public void setIngredient(Ingredient ingredient) { this.ingredient = ingredient; }

    public Recipe getRecipe() { return recipe; }
    public void setRecipe(Recipe recipe) { this.recipe = recipe; }
}
