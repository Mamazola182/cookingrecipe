package com.example.projectprm392.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.R;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {

    private List<Integer> images;

    public BannerAdapter(List<Integer> images) {
        this.images = images;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_banner, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        holder.imageView.setImageResource(images.get(position % images.size()));

    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE; // Để scroll vô hạn
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        BannerViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.bannerImage);

        }
    }
}