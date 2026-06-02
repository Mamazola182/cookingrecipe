package com.example.projectprm392.api;

import com.example.projectprm392.model.Ingredient;
import com.example.projectprm392.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IngredientApiService {
    @GET("api/Ingredient")
    Call<List<Ingredient>> getAll();
}
