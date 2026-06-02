package com.example.projectprm392;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.adapter.FavoriteAdapter;
import com.example.projectprm392.api.RetrofitClient;
import com.example.projectprm392.model.Favorite;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouriteFragment extends Fragment {

    private RecyclerView favoriteRecyclerView;
    private ProgressBar progressBar;
    private TextView emptyStateText;
    private List<Favorite> favoriteList = new ArrayList<>();
    private FavoriteAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        favoriteRecyclerView = view.findViewById(R.id.favoriteRecyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        emptyStateText = view.findViewById(R.id.emptyState);

        adapter = new FavoriteAdapter(requireContext(), favoriteList, new FavoriteAdapter.OnItemClickListener() {
            @Override
            public void onDetailClick(int recipeId) {
                Intent intent = new Intent(requireContext(), RecipeDetailActivity.class);
                intent.putExtra("RECIPE_ID", recipeId);
                startActivity(intent);
            }

            @Override
            public void onFavoriteClick(Favorite favorite, int position) {
                RetrofitClient.getInstance().getFavoriteApiService()
                        .deleteFavorite(favorite.getFavoriteId())
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    favoriteList.remove(position);
                                    adapter.notifyItemRemoved(position);
                                    adapter.notifyItemRangeChanged(position, favoriteList.size());
                                    Toast.makeText(requireContext(), "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show();
                                    emptyStateText.setVisibility(favoriteList.isEmpty() ? View.VISIBLE : View.GONE);
                                } else {
                                    Toast.makeText(requireContext(), "Lỗi xóa", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(requireContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        favoriteRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        favoriteRecyclerView.setAdapter(adapter);

        // Lấy userId từ SharedPreferences
        SharedPreferences prefs = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        int userId = prefs.getInt("userId", -1);
        if (userId == -1) {
            emptyStateText.setVisibility(View.VISIBLE);
            return;
        }

        loadFavorites(userId);
    }

    private void loadFavorites(int userId) {
        progressBar.setVisibility(View.VISIBLE);
        RetrofitClient.getInstance().getFavoriteApiService()
                .getFavoritesByUser(userId)
                .enqueue(new Callback<List<Favorite>>() {
                    @Override
                    public void onResponse(Call<List<Favorite>> call, Response<List<Favorite>> response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {
                            favoriteList.clear();
                            favoriteList.addAll(response.body());
                            adapter.notifyDataSetChanged();
                            emptyStateText.setVisibility(favoriteList.isEmpty() ? View.VISIBLE : View.GONE);
                        } else {
                            emptyStateText.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Favorite>> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        emptyStateText.setText("Lỗi kết nối: " + t.getMessage());
                        emptyStateText.setVisibility(View.VISIBLE);
                    }
                });
    }
}