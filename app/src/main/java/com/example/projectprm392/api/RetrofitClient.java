package com.example.projectprm392.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.projectprm392.MyApp;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://cookingrecipe-production.up.railway.app/";
    private static RetrofitClient instance;
    private final Retrofit retrofit;

    private final RecipeApiService apiRecipeService;
    private final CategoryApiService apiCategoryService;
    private final UserApiService apiUserService;
    private final ChatbotApiService apiChatbotService;
    private final FavouriteApiService favouriteApiService;
    private final IngredientApiService ingredientApiService;


    private RetrofitClient() {
        // Logging interceptor
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Auth interceptor — tự đọc token từ SharedPreferences toàn cục
        Interceptor authInterceptor = chain -> {
            Context context = MyApp.getAppContext();
            SharedPreferences prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
            String token = prefs.getString("token", null);

            Request.Builder builder = chain.request().newBuilder();
            if (token != null) {
                builder.addHeader("Authorization", "Bearer " + token);
                Log.d("AUTH_INTERCEPTOR", "Gắn token: " + token);
            } else {
                Log.d("AUTH_INTERCEPTOR", "Không có token, bỏ qua Authorization");
            }

            return chain.proceed(builder.build());
        };

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiRecipeService = retrofit.create(RecipeApiService.class);
        apiCategoryService = retrofit.create(CategoryApiService.class);
        apiUserService = retrofit.create(UserApiService.class);
        apiChatbotService = retrofit.create(ChatbotApiService.class);
        favouriteApiService = retrofit.create(FavouriteApiService.class);
        ingredientApiService = retrofit.create(IngredientApiService.class);

    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public RecipeApiService getRecipeApiService() { return apiRecipeService; }
    public CategoryApiService getCategoryApiService() { return apiCategoryService; }
    public UserApiService getUserApiService() { return apiUserService; }
    public ChatbotApiService getChatbotApiService() { return apiChatbotService; }
    public FavouriteApiService getFavoriteApiService() { return favouriteApiService; }

    public IngredientApiService getIngredientApiService() { return ingredientApiService; }

}
