package com.example.projectprm392.model;

import java.io.Serializable;

public class RecipeStep implements Serializable {
    private int stepId;
    private int recipeId;
    private int stepNumber;
    private String content;
    private String imageUrl;
    private String videoUrl;
    private Integer duration;  // Thời gian của bước này (phút)
    private Recipe recipe;

    // Constructors
    public RecipeStep() {}

    // Getters and Setters
    public int getStepId() { return stepId; }
    public void setStepId(int stepId) { this.stepId = stepId; }

    public int getRecipeId() { return recipeId; }
    public void setRecipeId(int recipeId) { this.recipeId = recipeId; }

    public int getStepNumber() { return stepNumber; }
    public void setStepNumber(int stepNumber) { this.stepNumber = stepNumber; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }

    public Recipe getRecipe() { return recipe; }
    public void setRecipe(Recipe recipe) { this.recipe = recipe; }
}
