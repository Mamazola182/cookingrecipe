package com.example.projectprm392.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projectprm392.R;
import com.example.projectprm392.model.Ingredient;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private final List<Ingredient> ingredients;

    public IngredientAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.tvIngredientName.setText("• " + ingredient.getName());
        holder.tvIngredientAmount.setText(ingredient.getQuantity());
    }

    @Override
    public int getItemCount() {
        return ingredients != null ? ingredients.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvIngredientName, tvIngredientAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIngredientName = itemView.findViewById(R.id.tvIngredientName);
            tvIngredientAmount = itemView.findViewById(R.id.tvIngredientAmount);
        }
    }
}
