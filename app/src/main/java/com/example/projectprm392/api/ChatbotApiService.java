package com.example.projectprm392.api;

import com.example.projectprm392.model.ChatRequest;
import com.example.projectprm392.model.ChatResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ChatbotApiService {

    @POST("api/Chatbot/advice")
    Call<ChatResponse> getAdvice(@Body ChatRequest request);
}