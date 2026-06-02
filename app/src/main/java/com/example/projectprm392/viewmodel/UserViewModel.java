package com.example.projectprm392.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.projectprm392.model.LoginRequest;
import com.example.projectprm392.repository.UserRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends ViewModel {
    private final UserRepository repository = new UserRepository();
    public MutableLiveData<String> token = new MutableLiveData<>();
    public MutableLiveData<String> error = new MutableLiveData<>();

    public void login(String username, String password) {
        LoginRequest request = new LoginRequest(username, password);

        repository.login(request).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    token.postValue(response.body());
                } else {
                    error.postValue("Login failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                error.postValue(t.getMessage());
            }
        });
    }
}
