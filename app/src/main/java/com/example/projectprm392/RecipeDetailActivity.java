package com.example.projectprm392;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projectprm392.adapter.IngredientAdapter;
import com.example.projectprm392.adapter.StepAdapter;
import com.example.projectprm392.api.RecipeApiService;
import com.example.projectprm392.api.RetrofitClient;
import com.example.projectprm392.model.Ingredient;
import com.example.projectprm392.model.RecipeDetail;
import com.example.projectprm392.model.RecipeStep;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView tvRecipeTitle, tvRating, tvTime, tvDifficulty, tvRecipeDescription;
    private RecyclerView rvIngredients, rvInstructions;
    private ImageView ivRecipeImage;
    private MaterialButton btnFavorite, btnShare;
    private RecipeDetail currentRecipe;
    private RecipeApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail);

        int recipeId = getIntent().getIntExtra("RECIPE_ID", -1);

        tvRecipeTitle = findViewById(R.id.tvRecipeTitle);
        tvRecipeDescription = findViewById(R.id.tvRecipeDescription);
        tvRating = findViewById(R.id.tvRating);
        tvTime = findViewById(R.id.tvTime);
        tvDifficulty = findViewById(R.id.tvDifficulty);
        rvIngredients = findViewById(R.id.rvIngredients);
        rvInstructions = findViewById(R.id.rvInstructions);
        ivRecipeImage = findViewById(R.id.ivRecipeImage);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnShare = findViewById(R.id.btnShare);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        rvIngredients.setLayoutManager(new LinearLayoutManager(this));
        rvInstructions.setLayoutManager(new LinearLayoutManager(this));

        apiService = RetrofitClient.getInstance().getRecipeApiService();

        if (recipeId != -1) {
            loadRecipeDetail(recipeId);
        } else {
            Toast.makeText(this, "Không có ID công thức", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadRecipeDetail(int id) {
        apiService.getRecipeDetail(id).enqueue(new Callback<RecipeDetail>() {
            @Override
            public void onResponse(Call<RecipeDetail> call, Response<RecipeDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RecipeDetail recipe = response.body();
                    currentRecipe = recipe;
                    tvRecipeTitle.setText(recipe.getTitle());
                    tvRecipeDescription.setText(recipe.getDescription());
                    tvTime.setText(String.format("%d phút", recipe.getTotalTime()));
                    String diff = "";
                    if(recipe.getDifficulty().equalsIgnoreCase("hard")){
                        diff = "Khó";
                    } else if (recipe.getDifficulty().equalsIgnoreCase("medium")) {
                        diff = "Trung bình";
                    }
                    else{
                        diff = "Dễ";
                    }
                    tvDifficulty.setText(diff);
                    Glide.with(RecipeDetailActivity.this)
                            .load(recipe.getThumbnail())
                            .into(ivRecipeImage);

                    List<Ingredient> ingredients = recipe.getIngredients();
                    rvIngredients.setAdapter(new IngredientAdapter(ingredients));
                    List<RecipeStep> steps = recipe.getSteps();
                    rvInstructions.setAdapter(new StepAdapter(steps));

                } else {
                    Toast.makeText(RecipeDetailActivity.this, "Không tải được dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecipeDetail> call, Throwable t) {
                Log.e("API_ERROR", t.getMessage(), t);
                Toast.makeText(RecipeDetailActivity.this, "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void startCooking(View view){
        if (currentRecipe != null) {
            Intent intent = new Intent(this, CookingModeActivity.class);
            intent.putExtra("recipe_detail", currentRecipe);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Chưa có dữ liệu công thức", Toast.LENGTH_SHORT).show();
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

}
