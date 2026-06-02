package com.example.projectprm392.api;

import com.example.projectprm392.model.Recipe;
import com.example.projectprm392.model.RecipeDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;
public interface RecipeApiService {
    @GET("api/Recipe")
    Call<List<Recipe>> getAllRecipes();
    @GET("api/Recipe/recommend")
    Call<List<Recipe>> getAllRecipesRecommend();
    @GET("Recipe/{id}")
    Call<Recipe> getRecipeById(@Path("id") int id);
    @GET("api/Recipe/{id}")
    Call<RecipeDetail> getRecipeDetail(@Path("id") int id);
    @POST("Recipe")
    Call<Recipe> createRecipe(@Body Recipe recipe);

    @PUT("Recipe/{id}")
    Call<Recipe> updateRecipe(@Path("id") int id, @Body Recipe recipe);

    @DELETE("api/Recipe/{id}")
    Call<Void> deleteRecipe(@Path("id") int id);

    @GET("api/Recipe/search")
    Call<List<Recipe>> searchRecipes(@Query("keyword") String query);
    @GET("/api/Recipe/author/{authorId}")
    Call<List<Recipe>> getRecipeByAuthor(@Path("authorId") int id);

    @GET("api/Recipe/category/{categoryId}")
    Call<List<Recipe>> getRecipeByCategoryId(@Path("categoryId")int categoryId);

}
