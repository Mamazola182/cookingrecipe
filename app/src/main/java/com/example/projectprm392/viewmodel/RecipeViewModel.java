package com.example.projectprm392.viewmodel;

import androidx.lifecycle.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.projectprm392.model.Recipe;
import com.example.projectprm392.repository.RecipeRepository;

import java.util.List;
public class RecipeViewModel extends ViewModel {
    private RecipeRepository repository;
    private MutableLiveData<List<Recipe>> recipes;
    private MutableLiveData<List<Recipe>> recommendRecipes;
    private MutableLiveData<Recipe> recipe;
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<String> error;

    public RecipeViewModel() {
        repository = new RecipeRepository();
        recipes = new MutableLiveData<>();
        recommendRecipes = new MutableLiveData<>();
        recipe = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
        error = new MutableLiveData<>();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }
    public LiveData<List<Recipe>> getRecommendRecipes() {
        return recommendRecipes;
    }
    public LiveData<Recipe> getRecipe() {
        return recipe;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void loadAllRecipes() {
        isLoading.setValue(true);
        repository.getAllRecipes(recipes, error);
        isLoading.setValue(false);
    }

    public void loadRecommendRecipes() {
        isLoading.setValue(true);
        repository.getAllRecipesRecommend(recommendRecipes, error);
        isLoading.setValue(false);
    }


    public void loadRecipeById(int id) {
        isLoading.setValue(true);
        repository.getRecipeById(id, recipe, error);
        isLoading.setValue(false);
    }

    public void createRecipe(Recipe newRecipe) {
        isLoading.setValue(true);
        repository.createRecipe(newRecipe, recipe, error);
        isLoading.setValue(false);
    }

    public void searchRecipes(String query) {
        isLoading.setValue(true);
        repository.searchRecipes(query, recipes, error);
        isLoading.setValue(false);
    }

    public void deleteRecipe(int id) {
        isLoading.setValue(true);
        MutableLiveData<Boolean> successLiveData = new MutableLiveData<>();
        repository.deleteRecipe(id, successLiveData, error);
        isLoading.setValue(false);
    }

    public void loadRecipeByCatgoryId(int categoryId) {
        isLoading.setValue(true);
        repository.getRecipeByCatgoryId(categoryId,recipes, error);
        isLoading.setValue(false);
    }
    public LiveData<List<Recipe>> loadRecipeByCategoryIdLive(int categoryId) {
        MutableLiveData<List<Recipe>> data = new MutableLiveData<>();
        isLoading.setValue(true);
        repository.getRecipeByCatgoryId(categoryId, data, error);
        isLoading.setValue(false);
        return data;
    }
}
