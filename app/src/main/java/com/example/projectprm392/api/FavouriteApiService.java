package com.example.projectprm392.api;

import com.example.projectprm392.model.Favorite;
import com.example.projectprm392.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FavouriteApiService {
    // Lấy danh sách tất cả favorites
    @GET("api/Favorites")
    Call<List<Favorite>> getAllFavorites();

    // Lấy favorites theo userId (nếu backend hỗ trợ)
    @GET("api/Favorites/user/{userId}")
    Call<List<Favorite>> getFavoritesByUser(@Path("userId") int userId);

    // Thêm favorite mới
    @POST("api/Favorites")
    Call<Favorite> addFavorite(@Body Favorite favorite);

    // Xóa favorite theo id
    @DELETE("api/Favorites/{id}")
    Call<Void> deleteFavorite(@Path("id") int id);
}
