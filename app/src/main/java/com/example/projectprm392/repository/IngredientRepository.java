package com.example.projectprm392.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.projectprm392.api.IngredientApiService;
import com.example.projectprm392.api.RetrofitClient;
import com.example.projectprm392.model.Ingredient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IngredientRepository {
    private static final String TAG = "IngredientRepository";
    private IngredientApiService apiService;

    public IngredientRepository() {
        apiService = RetrofitClient.getInstance().getIngredientApiService();
    }
    public void getAllIngredient(MutableLiveData<List<Ingredient>> recipesLiveData,
                               MutableLiveData<String> errorLiveData) {

        Call<List<Ingredient>> call = apiService.getAll();

        call.enqueue(new Callback<List<Ingredient>>() {
            @Override
            public void onResponse(Call<List<Ingredient>> call, Response<List<Ingredient>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recipesLiveData.postValue(response.body());
                } else {
                    errorLiveData.postValue("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Ingredient>> call, Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage());
                errorLiveData.postValue(t.getMessage());
            }
        });
    }
}
