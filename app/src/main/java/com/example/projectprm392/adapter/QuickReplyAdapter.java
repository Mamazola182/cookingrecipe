package com.example.projectprm392.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.R;
import com.example.projectprm392.model.QuickReply;

import java.util.List;

public class QuickReplyAdapter extends RecyclerView.Adapter<QuickReplyAdapter.ViewHolder> {
    private List<QuickReply> quickReplies;
    private final OnQuickReplyClickListener listener;

    public interface OnQuickReplyClickListener {
        void onQuickReplyClick(QuickReply quickReply);
    }

    public QuickReplyAdapter(List<QuickReply> quickReplies, OnQuickReplyClickListener listener) {
        this.quickReplies = quickReplies;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_quick_reply, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuickReply quickReply = quickReplies.get(position);
        holder.bind(quickReply, listener);
    }

    @Override
    public int getItemCount() {
        return quickReplies.size();
    }

    public void updateQuickReplies(List<QuickReply> newQuickReplies) {
        this.quickReplies = newQuickReplies;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView quickReplyText;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            quickReplyText = itemView.findViewById(R.id.quickReplyText);
        }

        void bind(QuickReply quickReply, OnQuickReplyClickListener listener) {
            quickReplyText.setText(quickReply.getLabel());
            itemView.setOnClickListener(v -> listener.onQuickReplyClick(quickReply));
        }
    }
}