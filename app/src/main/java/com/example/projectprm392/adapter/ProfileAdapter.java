package com.example.projectprm392.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;

import com.example.projectprm392.R;
import com.example.projectprm392.model.RecipeUser;

import java.util.List;
public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ChapterHolder> {
    List<RecipeUser> list;
    public ProfileAdapter(List<RecipeUser> list) {this.list = list;}
    @NonNull
    @Override
    public ChapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_user_saved, parent, false);
        return new ChapterHolder(v);
    }
    @Override
            public int getItemCount()   {return list.size();}
    @Override
            public void onBindViewHolder(@NonNull ChapterHolder holder, int position) {
            holder.tv_title.setText(list.get(position).getTitle());
            holder.imv_ava.setImageResource(list.get(position).getAva_chap());
    }
    class ChapterHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        ImageView imv_ava;
        public ChapterHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            imv_ava =itemView.findViewById(R.id.imv_ava);
         }
    }
}
