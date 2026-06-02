package com.example.projectprm392.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.R;
import com.example.projectprm392.model.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categories = new ArrayList<>();
    private OnCategoryClickListener listener;
    private int selectedPosition = -1;

    public interface OnCategoryClickListener {
        void onCategoryClick(Category category, int position);
    }

    public CategoryAdapter(OnCategoryClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.bind(category, position == selectedPosition);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories != null ? categories : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setSelectedPosition(int position) {
        int previousSelected = selectedPosition;
        selectedPosition = position;

        if (previousSelected != -1) {
            notifyItemChanged(previousSelected);
        }
        if (selectedPosition != -1) {
            notifyItemChanged(selectedPosition);
        }
    }

    public void addCategory(Category category) {
        categories.add(category);
        notifyItemInserted(categories.size() - 1);
    }

    public void removeCategory(int position) {
        if (position >= 0 && position < categories.size()) {
            categories.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        categories.clear();
        selectedPosition = -1;
        notifyDataSetChanged();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        private TextView categoryNameText;
        private CardView cardView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ImageView channelImageView = itemView.findViewById(R.id.channelImageView);
            cardView = (CardView) itemView;

            // Tạo TextView để hiển thị tên category
            if (channelImageView != null) {
                ViewGroup parent = (ViewGroup) channelImageView.getParent();

                // Ẩn ImageView nếu muốn chỉ hiển thị text
                channelImageView.setVisibility(View.GONE);

                // Thêm TextView
                categoryNameText = new TextView(itemView.getContext());
                categoryNameText.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                ));
                categoryNameText.setGravity(android.view.Gravity.CENTER);
                categoryNameText.setTextColor(Color.WHITE);
                categoryNameText.setTextSize(12);
                categoryNameText.setPadding(8, 8, 8, 8);

                parent.addView(categoryNameText);
            }

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    setSelectedPosition(position);
                    listener.onCategoryClick(categories.get(position), position);
                }
            });
        }

        public void bind(Category category, boolean isSelected) {
            if (categoryNameText != null) {
                categoryNameText.setText(category.getName());

                // Thay đổi màu nền khi được chọn
                if (isSelected) {
                    cardView.setCardBackgroundColor(Color.parseColor("#FF6B6B"));
                } else {
                    cardView.setCardBackgroundColor(Color.parseColor("#2a2a2a"));
                }
            }

            // Hoặc nếu muốn hiển thị icon theo category
//            if (channelImageView != null && channelImageView.getVisibility() == View.VISIBLE) {
//                int iconResId = getCategoryIcon(category.getName());
//                channelImageView.setImageResource(iconResId);
//            }
        }

//        private int getCategoryIcon(String categoryName) {
//            // Map tên category với icon tương ứng
//            if (categoryName == null) return R.drawable.ic_category_default;
//
//            String name = categoryName.toLowerCase();
//            if (name.contains("breakfast")) {
//                return R.drawable.ic_breakfast;
//            } else if (name.contains("lunch")) {
//                return R.drawable.ic_lunch;
//            } else if (name.contains("dinner")) {
//                return R.drawable.ic_dinner;
//            } else if (name.contains("dessert")) {
//                return R.drawable.ic_dessert;
//            } else if (name.contains("snack")) {
//                return R.drawable.ic_snack;
//            } else if (name.contains("drink")) {
//                return R.drawable.ic_drink;
//            }
//
//            return R.drawable.ic_category_default;
//        }
    }
}