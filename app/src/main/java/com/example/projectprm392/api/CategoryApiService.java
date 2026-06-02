package com.example.projectprm392.api;

import com.example.projectprm392.model.Category;
import com.example.projectprm392.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CategoryApiService {
    @GET("api/Category")
    Call<List<Category>> getAll();
    @GET("api/Category/{id}")
    Call<Category> getCategoryById(@Path("id") int id);
}
