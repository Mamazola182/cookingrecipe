package com.example.projectprm392.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.R;
import com.example.projectprm392.RecipeDetailActivity;
import com.example.projectprm392.model.CategoryRecipeGroup;
import com.example.projectprm392.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class CategoryRecipeGroupAdapter extends RecyclerView.Adapter<CategoryRecipeGroupAdapter.GroupViewHolder> {

    private Context context;
    private List<CategoryRecipeGroup> groupList;

    public CategoryRecipeGroupAdapter(Context context) {
        this.context = context;
        this.groupList = new ArrayList<>();
    }

    public void setData(List<CategoryRecipeGroup> groupList) {
        this.groupList = groupList != null ? groupList : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_group, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        CategoryRecipeGroup group = groupList.get(position);
        holder.bind(group);
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    class GroupViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryTitle;
        private TextView recipeCount;
        private RecyclerView recipesRecyclerView;
        private RecipeAdapter recipeAdapter;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTitle = itemView.findViewById(R.id.categoryTitle);
            recipeCount = itemView.findViewById(R.id.recipeCount);
            recipesRecyclerView = itemView.findViewById(R.id.recipesRecyclerView);

            // Setup horizontal RecyclerView
            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    context, LinearLayoutManager.HORIZONTAL, false);
            recipesRecyclerView.setLayoutManager(layoutManager);

            // Create adapter with click listener
            recipeAdapter = new RecipeAdapter(context, new ArrayList<>(), recipe -> {
                // Navigate to recipe detail
                Intent intent = new Intent(context, RecipeDetailActivity.class);
                intent.putExtra("RECIPE_ID", recipe.getRecipeId());
                context.startActivity(intent);
            });
            recipesRecyclerView.setAdapter(recipeAdapter);
        }

        public void bind(CategoryRecipeGroup group) {
            categoryTitle.setText(group.getCategoryName());
            recipeCount.setText(group.getRecipeCount() + " công thức");

            // Update recipes directly (no need to cast from Object)
            recipeAdapter.updateData(group.getRecipes());
        }
    }
}