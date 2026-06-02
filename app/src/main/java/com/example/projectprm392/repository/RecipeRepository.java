package com.example.projectprm392.repository;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;

import com.example.projectprm392.api.RecipeApiService;
import com.example.projectprm392.api.RetrofitClient;
import com.example.projectprm392.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeRepository {
    private static final String TAG = "RecipeRepository";
    private RecipeApiService apiService;

    public RecipeRepository() {
        apiService = RetrofitClient.getInstance().getRecipeApiService();
    }

    public void getAllRecipes(MutableLiveData<List<Recipe>> recipesLiveData,
                              MutableLiveData<String> errorLiveData) {

        Call<List<Recipe>> call = apiService.getAllRecipes();

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recipesLiveData.postValue(response.body());
                } else {
                    errorLiveData.postValue("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage());
                errorLiveData.postValue(t.getMessage());
            }
        });
    }
    public void getAllRecipesRecommend(MutableLiveData<List<Recipe>> recipesLiveData,
                              MutableLiveData<String> errorLiveData) {

        Call<List<Recipe>> call = apiService.getAllRecipesRecommend();

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recipesLiveData.postValue(response.body());
                } else {
                    errorLiveData.postValue("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage());
                errorLiveData.postValue(t.getMessage());
            }
        });
    }

    public void getRecipeById(int id,
                              MutableLiveData<Recipe> recipeLiveData,
                              MutableLiveData<String> errorLiveData) {
        Call<Recipe> call = apiService.getRecipeById(id);

        call.enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recipeLiveData.postValue(response.body());
                } else {
                    errorLiveData.postValue("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage());
                errorLiveData.postValue(t.getMessage());
            }
        });
    }

    public void createRecipe(Recipe recipe,
                             MutableLiveData<Recipe> recipeLiveData,
                             MutableLiveData<String> errorLiveData) {
        Call<Recipe> call = apiService.createRecipe(recipe);

        call.enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recipeLiveData.postValue(response.body());
                } else {
                    errorLiveData.postValue("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage());
                errorLiveData.postValue(t.getMessage());
            }
        });
    }

    public void searchRecipes(String query,
                              MutableLiveData<List<Recipe>> recipesLiveData,
                              MutableLiveData<String> errorLiveData) {


        Call<List<Recipe>> call = apiService.searchRecipes(query); // ✅ Đúng kiểu

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recipesLiveData.postValue(response.body()); // ✅ parse trực tiếp List<Recipe>
                } else {
                    errorLiveData.postValue("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage());
                errorLiveData.postValue(t.getMessage());
            }
        });
    }


    public void deleteRecipe(int id,
                             MutableLiveData<Boolean> successLiveData,
                             MutableLiveData<String> errorLiveData) {
        Call<Void> call = apiService.deleteRecipe(id);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    successLiveData.postValue(true);
                } else {
                    errorLiveData.postValue("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage());
                errorLiveData.postValue(t.getMessage());
            }
        });
    }

    public void getRecipeByCatgoryId(int categoryId,
                              MutableLiveData<List<Recipe>> recipeLiveData,
                              MutableLiveData<String> errorLiveData) {
        Call<List<Recipe>> call = apiService.getRecipeByCategoryId(categoryId);

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recipeLiveData.postValue(response.body());
                } else {
                    errorLiveData.postValue("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage());
                errorLiveData.postValue(t.getMessage());
            }
        });
    }
}
