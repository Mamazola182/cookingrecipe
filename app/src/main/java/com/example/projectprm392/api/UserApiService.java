package com.example.projectprm392.api;

import com.example.projectprm392.model.LoginRequest;
import com.example.projectprm392.model.RegisterRequest;
import com.example.projectprm392.model.User;
import retrofit2.Call;
import java.util.List;
import retrofit2.http.*;
public interface UserApiService {
    @GET("api/User/me")
    Call<User> getUserMe();
    @GET("api/User/GetAll")
    Call<List<User>> getAllUser();
    @GET("api/User/{id}")
    Call<User> getUserById(@Path("id") int id);
    @PUT("api/User/{id}")
    Call<User> updateUser(@Path("id") int id, @Body User user);
    @DELETE("api/User/{id}")
    Call<Void> deleteUser(@Path("id") int id);

    @POST("api/User/register")
    Call<String> register(@Body RegisterRequest request);

    @POST("api/User/login")
    Call<String> login(@Body LoginRequest request);
    @POST("api/User/{id}/change-password")
    Call<User> changePassword(@Path("id") int id, @Body User user);

}
