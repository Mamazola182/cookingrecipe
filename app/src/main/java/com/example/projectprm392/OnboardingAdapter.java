package com.example.projectprm392;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder> {

    private final List<OnboardingItem> onboardingItems;

    public OnboardingAdapter(List<OnboardingItem> onboardingItems) {
        this.onboardingItems = onboardingItems;
    }

    @NonNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnboardingViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_onboarding, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
        holder.setData(onboardingItems.get(position));
    }

    @Override
    public int getItemCount() {
        return onboardingItems.size();
    }

    static class OnboardingViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageOnboarding;
        private final TextView textTitle;
        private final TextView textDescription;

        OnboardingViewHolder(@NonNull View itemView) {
            super(itemView);
            imageOnboarding = itemView.findViewById(R.id.imageOnboarding);
            textTitle = itemView.findViewById(R.id.tv_onboarding_title);
            textDescription = itemView.findViewById(R.id.tv_onboarding_description);
        }

        void setData(OnboardingItem item) {
            imageOnboarding.setImageResource(item.getImage());
            textTitle.setText(item.getTitle());
            textDescription.setText(item.getDescription());
        }
    }
}