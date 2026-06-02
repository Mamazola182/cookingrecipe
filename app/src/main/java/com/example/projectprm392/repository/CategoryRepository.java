package com.example.projectprm392.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.projectprm392.api.CategoryApiService;
import com.example.projectprm392.api.RetrofitClient;
import com.example.projectprm392.model.Category;
import com.example.projectprm392.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRepository {
    private static final String TAG = "CategoryRepository";
    private CategoryApiService apiService;

    public CategoryRepository() {
        apiService = RetrofitClient.getInstance().getCategoryApiService();
    }
    public void getAllCategory(MutableLiveData<List<Category>> recipesLiveData,
                              MutableLiveData<String> errorLiveData) {

        Call<List<Category>> call = apiService.getAll();

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recipesLiveData.postValue(response.body());
                } else {
                    errorLiveData.postValue("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e(TAG, "Error: " + t.getMessage());
                errorLiveData.postValue(t.getMessage());
            }
        });
    }
}
