package com.example.projectprm392.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.R;
import com.example.projectprm392.model.RecipeStep;

import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {

    private final List<RecipeStep> steps;

    public StepAdapter(List<RecipeStep> steps) {
        this.steps = steps;
    }

    @NonNull
    @Override
    public StepAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe_step, parent, false);
        return new StepAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepAdapter.ViewHolder holder, int position) {
        RecipeStep step = steps.get(position);
        holder.tvStepNumber.setText(String.format("Bước %d", step.getStepNumber()));
        holder.tvStepContent.setText("• " + step.getContent());
        holder.tvStepDuration.setText("Thời gian: " + step.getDuration() + "phút");
    }

    @Override
    public int getItemCount() {
        return steps != null ? steps.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStepNumber, tvStepContent, tvStepDuration;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStepNumber = itemView.findViewById(R.id.tvStepNumber);
            tvStepContent = itemView.findViewById(R.id.tvStepContent);
            tvStepDuration = itemView.findViewById(R.id.tvStepDuration);
        }
    }
}
