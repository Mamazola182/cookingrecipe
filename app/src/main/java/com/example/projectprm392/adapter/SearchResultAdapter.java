package com.example.projectprm392.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.projectprm392.R;
import com.example.projectprm392.fragment.SearchFragment;
import com.example.projectprm392.model.Recipe;
import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private List<Recipe> recipeList;
    private Context context;
    private final SearchFragment listener; // Dùng interface từ RecipeAdapter

    // Dùng chung interface OnItemClickListener từ RecipeAdapter
    public interface OnItemClickListener {
        void onDetailClick(Recipe recipe);
    }

    public SearchResultAdapter(Context context, List<Recipe> recipeList, SearchFragment listener) {
        this.context = context;
        this.recipeList = recipeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Dùng layout item mới
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);

        holder.title.setText(recipe.getTitle());
        holder.author.setText(recipe.getAuthorName());
        holder.difficulty.setText(recipe.getDifficulty());
        holder.totalTime.setText(recipe.getTotalTime().toString()+"phút"); // <-- THAY BẰNG DỮ LIỆU THẬT

        Glide.with(context)
                .load(recipe.getThumbnail())
                .into(holder.thumbnail);

        // Gán sự kiện click cho cả item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDetailClick(recipe);
            }
        });
//        holder.detailButton.setOnClickListener(v -> {
//            if (listener != null) {
//                listener.onDetailClick(currentRecipe);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return recipeList != null ? recipeList.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView thumbnail;
        final TextView title;
        final TextView author;
        final TextView difficulty;
        final TextView totalTime; // ▼▼▼ BIẾN MỚI ▼▼▼

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.item_thumbnail);
            title = itemView.findViewById(R.id.item_title);
            author = itemView.findViewById(R.id.item_author);
            difficulty = itemView.findViewById(R.id.item_difficulty);
            totalTime = itemView.findViewById(R.id.item_total_time); // ▼▼▼ ÁNH XẠ MỚI ▼▼▼
        }
    }

    public void updateData(List<Recipe> newRecipes) {
        this.recipeList.clear();
        this.recipeList.addAll(newRecipes);
        notifyDataSetChanged();
    }
}