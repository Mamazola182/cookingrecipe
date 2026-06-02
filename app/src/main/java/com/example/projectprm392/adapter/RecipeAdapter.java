package com.example.projectprm392.adapter;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.projectprm392.R;
import com.example.projectprm392.RecipeManagementActivity;
import com.example.projectprm392.api.RetrofitClient;
import com.example.projectprm392.model.Favorite;
import com.example.projectprm392.model.Recipe;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Recipe> recipeList;
    private Context context;
    public interface OnFavoriteClickListener {
        void onFavoriteClick(Recipe recipe, boolean isCurrentlyFavorite);
    }

    private OnFavoriteClickListener favoriteClickListener;

    public void setOnFavoriteClickListener(OnFavoriteClickListener listener) {
        this.favoriteClickListener = listener;
    }

    // 1. Định nghĩa Interface cho click listener
    public interface OnItemClickListener {
        void onDetailClick(Recipe recipe);
    }

    private final OnItemClickListener listener;

    // 2. Cập nhật constructor để nhận listener
    public RecipeAdapter(Context context, List<Recipe> recipeList, OnItemClickListener listener) {
        // Lưu lại context
        this.context = context;
        this.recipeList = recipeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout của item đã tạo ở Bước 1
        View itemView;
        if(viewType == 1){
            itemView = LayoutInflater.from(context).inflate(R.layout.item_recipe_card, parent, false);
            return new RecipeViewHolderVer2(itemView);
        }
        else{
            itemView = LayoutInflater.from(context).inflate(R.layout.list_item_recipe, parent, false);
            return new RecipeViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Recipe currentRecipe = recipeList.get(position);

        // Gán dữ liệu văn bản
        if(holder instanceof RecipeViewHolder){
            RecipeViewHolder holder1 = (RecipeViewHolder)holder;
            holder1.itemFavoriteButton.setOnClickListener(v -> {
                // Lấy userId từ SharedPreferences
                SharedPreferences prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                int userId = prefs.getInt("userId", -1);

                if (userId == -1) {
                    Toast.makeText(context, "Vui lòng đăng nhập để thêm yêu thích", Toast.LENGTH_SHORT).show();
                    return;
                }
                Favorite favorite = new Favorite();
                favorite.setUserId(userId);
                favorite.setRecipeId(currentRecipe.getRecipeId());
                // Gọi API thêm món yêu thích
                RetrofitClient.getInstance().getFavoriteApiService()
                        .addFavorite(favorite)
                        .enqueue(new Callback<Favorite>() {
                            @Override
                            public void onResponse(Call<Favorite> call, Response<Favorite> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(context, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                                    // Đổi icon sang trái tim đầy
                                    holder1.itemFavoriteButton.setImageResource(R.drawable.ic_favorite_border);

                                    // Tăng số lượt thích hiển thị
                                    int count = currentRecipe.getFavoriteCount() + 1;
                                    currentRecipe.setFavoriteCount(count);
                                    holder1.favCount.setText("Lượt thích: " + count);
                                } else {
                                    Toast.makeText(context, "Thêm yêu thích thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }


                            @Override
                            public void onFailure(Call<Favorite> call, Throwable t) {
                                Toast.makeText(context, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            });
            holder1.title.setText(currentRecipe.getTitle());
            holder1.author.setText("Tác giả: " + currentRecipe.getAuthorName());
            holder1.favCount.setText("Lượt thích: " + currentRecipe.getFavoriteCount());

            // 1. Tải ảnh bằng Glide
            Glide.with(context)
                    .load(currentRecipe.getThumbnail()) // Lấy link ảnh
//                .placeholder(R.drawable.placeholder_image)
//                .error(R.drawable.error_image) // (Tùy chọn) Ảnh khi lỗi
                    .into(holder1.image); // Gán vào ImageView

            // 2. Gán độ khó và đặt màu
            String difficulty = currentRecipe.getDifficulty();
            if (difficulty != null) {
                holder1.difficulty.setText(difficulty);
                setDifficultyColor(holder1.cardView, holder1.difficulty, difficulty);
            }

            // 3. Gán sự kiện click
            holder1.detailButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDetailClick(currentRecipe);
                }
            });
        }
        else {
            RecipeViewHolderVer2 holder2 = (RecipeViewHolderVer2) holder;
            holder2.tvRecipeName.setText(currentRecipe.getTitle());
            holder2.tvCookTime.setText(currentRecipe.getTotalTime()+" phút");
            Glide.with(context)
                    .load(currentRecipe.getThumbnail()) // Lấy link ảnh
//                .placeholder(R.drawable.placeholder_image)
//                .error(R.drawable.error_image) // (Tùy chọn) Ảnh khi lỗi
                    .into(holder2.ivRecipeImage); // Gán vào ImageView
            holder2.difficulty.setText(currentRecipe.getDifficulty());
            holder2.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDetailClick(currentRecipe);
                }
            });
            holder2.btnDelete.setOnClickListener(v -> {
                new AlertDialog.Builder(context)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc muốn xóa '" + currentRecipe.getTitle() + "' không?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            if (context instanceof RecipeManagementActivity) {
                                ((RecipeManagementActivity) context).deleteRecipe(currentRecipe.getRecipeId());
                            }
                            Toast.makeText(context, "Đang xóa: " + currentRecipe.getTitle(), Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            });
        }
    }
    private void setDifficultyColor(CardView card, TextView textView, String difficulty) {
        int colorResId;          // để lưu màu từ colors.xml
        String difficultyText;

        switch (difficulty.toLowerCase(Locale.ROOT)) {
            case "easy":
                difficultyText = "EASY";
                colorResId = R.color.difficulty_easy;
                break;
            case "medium":
                difficultyText = "MEDIUM";
                colorResId = R.color.difficulty_medium;
                break;
            case "hard":
                difficultyText = "HARD";
                colorResId = R.color.difficulty_hard;
                break;
            default:
                difficultyText = difficulty;
                colorResId = R.color.black;
                break;
        }

        // Set text và màu cho TextView
        textView.setText(difficultyText);
        textView.setTextColor(ContextCompat.getColor(textView.getContext(), colorResId));
    }

    @Override
    public int getItemCount() {
        return recipeList != null ? recipeList.size() : 0;
    }

    // Class ViewHolder để giữ các View của item
    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        final CardView cardView; // Thêm CardView
        final ImageView image; // Thêm ImageView
        final TextView title;
        final TextView author;
        final TextView favCount;
        final TextView difficulty; // Thêm TextView độ khó
        final Button detailButton;
        public ImageButton itemFavoriteButton;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.item_card);
            image = itemView.findViewById(R.id.item_image);
            title = itemView.findViewById(R.id.item_title);
            author = itemView.findViewById(R.id.item_author);
            favCount = itemView.findViewById(R.id.item_fav_count);
            difficulty = itemView.findViewById(R.id.item_difficulty);
            detailButton = itemView.findViewById(R.id.item_detail_button);
            itemFavoriteButton= itemView.findViewById(R.id.item_favorite_button);
        }
    }

    static class RecipeViewHolderVer2 extends RecyclerView.ViewHolder {
        final ImageView ivRecipeImage;
        final TextView tvRecipeName;
        final TextView tvCookTime;
        final TextView difficulty;
        final Button btnDelete;

        public RecipeViewHolderVer2(@NonNull View itemView) {
            super(itemView);
            ivRecipeImage = itemView.findViewById(R.id.ivRecipeImage);
            tvRecipeName = itemView.findViewById(R.id.tvRecipeName);
            tvCookTime = itemView.findViewById(R.id.tvCookTime);
            difficulty = itemView.findViewById(R.id.tvDifficulty);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    // Phương thức để cập nhật dữ liệu cho adapter
    public void updateData(List<Recipe> newRecipes) {
        this.recipeList.clear();
        this.recipeList.addAll(newRecipes);
        notifyDataSetChanged();
    }
    @Override
    public int getItemViewType(int position) {
        Recipe recipe = recipeList.get(position);
        if (recipe.isManage()) {
            return 1;
        }
        return 0;
    }
}