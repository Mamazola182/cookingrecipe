package com.example.projectprm392.adapter.createrecipe;


import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.R;
import com.example.projectprm392.model.RecipeStep;
import java.util.List;

public class StepRecipeAdapter extends RecyclerView.Adapter<StepRecipeAdapter.ViewHolder> {

    private List<RecipeStep> steps;
    private OnStepRemoveListener removeListener;
    private OnStepImageClickListener imageClickListener;

    public interface OnStepRemoveListener {
        void onRemove(int position);
    }

    public interface OnStepImageClickListener {
        void onImageClick(int position);
    }

    public StepRecipeAdapter(List<RecipeStep> steps, OnStepRemoveListener removeListener,
                       OnStepImageClickListener imageClickListener) {
        this.steps = steps;
        this.removeListener = removeListener;
        this.imageClickListener = imageClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_create_recipe_step, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecipeStep step = steps.get(position);
        holder.bind(step, position);
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public List<RecipeStep> getSteps() {
        return steps;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvStepNumber;
        EditText etContent, etDuration;
        ImageButton btnRemove;
        ImageView ivStepImage;
        View uploadPlaceholder, imageContainer;

        ViewHolder(View itemView) {
            super(itemView);
            tvStepNumber = itemView.findViewById(R.id.tv_step_number);
            etContent = itemView.findViewById(R.id.et_step_content);
            etDuration = itemView.findViewById(R.id.et_step_duration);
            btnRemove = itemView.findViewById(R.id.btn_remove_step);
            ivStepImage = itemView.findViewById(R.id.iv_step_image);
            uploadPlaceholder = itemView.findViewById(R.id.upload_placeholder);
            imageContainer = itemView.findViewById(R.id.image_container);
        }

        void bind(RecipeStep step, int position) {
            // Remove previous listeners
            etContent.removeTextChangedListener((TextWatcher) etContent.getTag());
            etDuration.removeTextChangedListener((TextWatcher) etDuration.getTag());

            // Set step number
            tvStepNumber.setText("Bước " + step.getStepNumber());

            // Set current values
            etContent.setText(step.getContent());
            if (step.getDuration() != null && step.getDuration() > 0) {
                etDuration.setText(String.valueOf(step.getDuration()));
            } else {
                etDuration.setText("");
            }

            // Handle step image
            if (step.getImageUrl() != null && !step.getImageUrl().isEmpty()) {
                try {
                    Uri imageUri = Uri.parse(step.getImageUrl());
                    ivStepImage.setImageURI(imageUri);
                    ivStepImage.setVisibility(View.VISIBLE);
                    uploadPlaceholder.setVisibility(View.GONE);
                } catch (Exception e) {
                    ivStepImage.setVisibility(View.GONE);
                    uploadPlaceholder.setVisibility(View.VISIBLE);
                }
            } else {
                ivStepImage.setVisibility(View.GONE);
                uploadPlaceholder.setVisibility(View.VISIBLE);
            }

            // Content TextWatcher
            TextWatcher contentWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    step.setContent(s.toString());
                }
            };
            etContent.addTextChangedListener(contentWatcher);
            etContent.setTag(contentWatcher);

            // Duration TextWatcher
            TextWatcher durationWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        if (!s.toString().isEmpty()) {
                            step.setDuration(Integer.parseInt(s.toString()));
                        } else {
                            step.setDuration(null);
                        }
                    } catch (NumberFormatException e) {
                        step.setDuration(null);
                    }
                }
            };
            etDuration.addTextChangedListener(durationWatcher);
            etDuration.setTag(durationWatcher);

            // Remove button
            btnRemove.setOnClickListener(v -> {
                if (removeListener != null) {
                    removeListener.onRemove(getAdapterPosition());
                }
            });

            // Image upload click
            imageContainer.setOnClickListener(v -> {
                if (imageClickListener != null) {
                    imageClickListener.onImageClick(getAdapterPosition());
                }
            });
        }
    }
}