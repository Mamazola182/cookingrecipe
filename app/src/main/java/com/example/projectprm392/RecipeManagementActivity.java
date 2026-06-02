package com.example.projectprm392;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectprm392.adapter.RecipeAdapter;
import com.example.projectprm392.api.RecipeApiService;
import com.example.projectprm392.api.RetrofitClient;
import com.example.projectprm392.model.Recipe;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeManagementActivity extends AppCompatActivity {

    private RecyclerView rvRecipes;
    private LinearLayout layoutEmptyState;
    private ExtendedFloatingActionButton fabAddRecipe;
    private RecipeApiService apiService;
    private List<Recipe> recipeList;
    private TextView tvTotalRecipes, tvFavorites;
    private EditText etSearch;
    private RecipeAdapter adapter;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_management);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Views
        rvRecipes = findViewById(R.id.rvRecipes);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        fabAddRecipe = findViewById(R.id.fabAddRecipe);
        tvTotalRecipes = findViewById(R.id.tvTotalRecipes);
        tvFavorites = findViewById(R.id.tvFavorites);
        etSearch = findViewById(R.id.etSearch); // thêm dòng này sau khi setContentView

        // RecyclerView
        rvRecipes.setLayoutManager(new LinearLayoutManager(this));

        apiService = RetrofitClient.getInstance().getRecipeApiService();
        //Lay theo userId dang dang nhap
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        userId = prefs.getInt("userId", 0);
        loadAllRecipe(userId);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (query.isEmpty()) {
                    loadAllRecipe(userId); // search trống → load toàn bộ
                } else {
                    searchRecipe(query);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // FAB thêm công thức
        fabAddRecipe.setOnClickListener(v ->{
            Intent intent = new Intent(RecipeManagementActivity.this, CreateRecipeActivity.class);
            startActivity(intent);
                }
        );
    }

    private void searchRecipe(String query) {
        apiService.searchRecipes(query).enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recipeList = response.body();
                    for (Recipe r : recipeList) {
                        r.setManage(true);
                    }
                    adapter = new RecipeAdapter(
                            RecipeManagementActivity.this,
                            recipeList,
                            recipe -> {
                                Toast.makeText(
                                        RecipeManagementActivity.this,
                                        "Clicked: " + recipe.getTitle(),
                                        Toast.LENGTH_SHORT
                                ).show();
                                Intent intent = new Intent(RecipeManagementActivity.this, RecipeDetailActivity.class);
                                intent.putExtra("RECIPE_ID", recipe.getRecipeId());
                                startActivity(intent);
                            }
                    );
                    rvRecipes.setAdapter(adapter);

                    tvTotalRecipes.setText(String.valueOf(recipeList.size()));

                    int countFav = 0;
                    for (Recipe r : recipeList) {
                        countFav += r.getFavoriteCount();
                    }
                    tvFavorites.setText(String.valueOf(countFav));

                    updateEmptyState();
                } else {
                    Toast.makeText(RecipeManagementActivity.this, "Không tải được dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e("API_ERROR", t.getMessage(), t);
                Toast.makeText(RecipeManagementActivity.this, "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAllRecipe(int id) {
        apiService.getRecipeByAuthor(id).enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recipeList = response.body();
                    for (Recipe r : recipeList) {
                        r.setManage(true);
                    }
                    adapter = new RecipeAdapter(
                            RecipeManagementActivity.this,
                            recipeList,
                            recipe -> {
                                Toast.makeText(
                                        RecipeManagementActivity.this,
                                        "Clicked: " + recipe.getTitle(),
                                        Toast.LENGTH_SHORT
                                ).show();
                                Intent intent = new Intent(RecipeManagementActivity.this, RecipeDetailActivity.class);
                                intent.putExtra("RECIPE_ID", recipe.getRecipeId());
                                startActivity(intent);
                            }
                    );
                    rvRecipes.setAdapter(adapter);

                    tvTotalRecipes.setText(String.valueOf(recipeList.size()));

                    int countFav = 0;
                    for (Recipe r : recipeList) {
                        countFav += r.getFavoriteCount();
                    }
                    tvFavorites.setText(String.valueOf(countFav));

                    updateEmptyState();
                } else {
                    Toast.makeText(RecipeManagementActivity.this, "Không tải được dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e("API_ERROR", t.getMessage(), t);
                Toast.makeText(RecipeManagementActivity.this, "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateEmptyState() {
        if (recipeList == null || recipeList.isEmpty()) {
            layoutEmptyState.setVisibility(View.VISIBLE);
            rvRecipes.setVisibility(View.GONE);
        } else {
            layoutEmptyState.setVisibility(View.GONE);
            rvRecipes.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void deleteRecipe(int recipeId) {
        apiService.deleteRecipe(recipeId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RecipeManagementActivity.this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                    loadAllRecipe(userId);
                } else {
                    Toast.makeText(RecipeManagementActivity.this, "Không thể xóa công thức.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RecipeManagementActivity.this, "Lỗi kết nối khi xóa.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
