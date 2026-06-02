package com.example.projectprm392.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.R;
import com.example.projectprm392.RecipeDetailActivity;
import com.example.projectprm392.api.RetrofitClient;
import com.example.projectprm392.model.Favorite;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavViewHolder> {

    private List<Favorite> favorites;
    private Context context;
    private OnItemClickListener listener;

    // Callback interface để Fragment xử lý
    public interface OnItemClickListener {
        void onDetailClick(int recipeId);
        void onFavoriteClick(Favorite favorite, int position);
    }

    public FavoriteAdapter(Context context, List<Favorite> favorites, OnItemClickListener listener) {
        this.context = context;
        this.favorites = favorites;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.list_item_recipe, parent, false);
        return new FavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavViewHolder holder, int position) {
        Favorite fav = favorites.get(position);

        holder.recipeName.setText(fav.getRecipeName() != null ? fav.getRecipeName() : "Không rõ");
        holder.itemAuthor.setText(fav.getUserName() != null ? fav.getUserName() : "Không rõ");

        if (fav.getRecipe() != null) {
            holder.itemFavCount.setText(String.valueOf(fav.getRecipe().getFavoriteCount()));
            holder.itemDifficulty.setText(fav.getRecipe().getDifficulty());
        }

        holder.favoriteButton.setImageResource(R.drawable.ic_heart_filled);

        holder.favoriteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFavoriteClick(fav, position);
            }
        });

        holder.detailButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDetailClick(fav.getRecipeId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public static class FavViewHolder extends RecyclerView.ViewHolder {
        TextView recipeName, itemAuthor, itemFavCount, itemDifficulty;
        ImageView recipeImage;
        ImageButton favoriteButton;
        Button detailButton;

        public FavViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.item_title);
            recipeImage = itemView.findViewById(R.id.item_image);
            favoriteButton = itemView.findViewById(R.id.item_favorite_button);
            detailButton = itemView.findViewById(R.id.item_detail_button);
            itemAuthor = itemView.findViewById(R.id.item_author);
            itemFavCount = itemView.findViewById(R.id.item_fav_count);
            itemDifficulty = itemView.findViewById(R.id.item_difficulty);
        }
    }
}