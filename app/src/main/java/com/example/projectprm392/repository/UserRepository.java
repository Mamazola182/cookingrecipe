package com.example.projectprm392.repository;

import com.example.projectprm392.api.RetrofitClient;
import com.example.projectprm392.api.UserApiService;
import com.example.projectprm392.model.LoginRequest;
import com.example.projectprm392.model.RegisterRequest;

import retrofit2.Call;

public class UserRepository {
    private final UserApiService apiService;

    public UserRepository() {
        apiService = RetrofitClient.getInstance().getUserApiService();
    }

    public Call<String> register(RegisterRequest request) {
        return apiService.register(request);
    }

    public Call<String> login(LoginRequest request) {
        return apiService.login(request);
    }
}
