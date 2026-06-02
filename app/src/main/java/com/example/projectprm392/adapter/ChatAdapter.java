package com.example.projectprm392.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.R;
import com.example.projectprm392.model.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_USER = 1;
    private static final int VIEW_TYPE_BOT = 2;

    private List<ChatMessage> messages;
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    public ChatAdapter(List<ChatMessage> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = messages.get(position);
        return message.getRole().equals("user") ? VIEW_TYPE_USER : VIEW_TYPE_BOT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_USER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_user, parent, false);
            return new UserMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_bot, parent, false);
            return new BotMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);

        if (holder instanceof UserMessageViewHolder) {
            ((UserMessageViewHolder) holder).bind(message);
        } else if (holder instanceof BotMessageViewHolder) {
            ((BotMessageViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void updateMessages(List<ChatMessage> newMessages) {
        this.messages = newMessages;
        notifyDataSetChanged();
    }

    // User Message ViewHolder
    class UserMessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView messageText;
        private final TextView timeText;

        UserMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
            timeText = itemView.findViewById(R.id.timeText);
        }

        void bind(ChatMessage message) {
            messageText.setText(message.getContent());
            timeText.setText(timeFormat.format(message.getTimestamp()));
        }
    }

    // Bot Message ViewHolder
    class BotMessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView messageText;
        private final TextView timeText;

        BotMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageText);
            timeText = itemView.findViewById(R.id.timeText);
        }

        void bind(ChatMessage message) {
            messageText.setText(message.getContent());
            timeText.setText(timeFormat.format(message.getTimestamp()));
        }
    }
}