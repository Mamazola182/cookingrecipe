package com.example.projectprm392;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.adapter.CategoryRecipeGroupAdapter;
import com.example.projectprm392.model.Category;
import com.example.projectprm392.model.CategoryRecipeGroup;
import com.example.projectprm392.model.Recipe;
import com.example.projectprm392.viewmodel.CategoryViewModel;
import com.example.projectprm392.viewmodel.RecipeViewModel;

import java.util.ArrayList;
import java.util.List;

public class AllRecipesActivity extends AppCompatActivity {
    private static final String TAG = "AllRecipesActivity";

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private CategoryRecipeGroupAdapter adapter;

    private RecipeViewModel recipeViewModel;
    private CategoryViewModel categoryViewModel;

    // Dữ liệu nhóm theo category
    private final List<CategoryRecipeGroup> groupedData = new ArrayList<>();
    private int loadedCount = 0; // Đếm số category đã load xong

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_recipes);

        initViews();
        setupToolbar();
        initViewModels();
        setupRecyclerView();
        observeCategories();
        loadData();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerViewAllRecipes);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Tất cả công thức");
        }
    }

    private void initViewModels() {
        recipeViewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
    }

    private void setupRecyclerView() {
        adapter = new CategoryRecipeGroupAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void observeCategories() {
        // Khi categories được load
        categoryViewModel.getCategories().observe(this, categories -> {
            if (categories != null && !categories.isEmpty()) {
                Log.d(TAG, "Có " + categories.size() + " category.");
                groupedData.clear();
                loadedCount = 0;
                progressBar.setVisibility(View.VISIBLE);

                // Lặp từng category để lấy recipe
                for (Category category : categories) {
                    int categoryId = category.getCategoryId();

                    recipeViewModel.loadRecipeByCategoryIdLive(categoryId).observe(this, recipes -> {
                        loadedCount++;
                        if (recipes != null && !recipes.isEmpty()) {
                            groupedData.add(new CategoryRecipeGroup(category.getName(), recipes));
                            Log.d(TAG, "Đã load " + recipes.size() + " recipes cho " + category.getName());
                        }

                        // Khi load xong hết tất cả category thì cập nhật adapter
                        if (loadedCount == categories.size()) {
                            progressBar.setVisibility(View.GONE);
                            if (groupedData.isEmpty()) {
                                Toast.makeText(this, "Không có công thức nào", Toast.LENGTH_SHORT).show();
                            } else {
                                adapter.setData(new ArrayList<>(groupedData));
                            }
                        }
                    });
                }

            } else {
                Toast.makeText(this, "Không có danh mục nào", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadData() {
        categoryViewModel.loadAllCategories();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
