package com.example.projectprm392.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.R;
import com.example.projectprm392.adapter.ChatAdapter;
import com.example.projectprm392.adapter.QuickReplyAdapter;
import com.example.projectprm392.model.ChatMessage;
import com.example.projectprm392.viewmodel.ChatbotViewModel;

import java.util.ArrayList;

public class ChatbotFragment extends Fragment {
    private static final String TAG = "ChatbotFragment";

    // Views
    private RecyclerView chatRecyclerView;
    private RecyclerView quickRepliesRecyclerView;
    private EditText messageInput;
    private ImageButton sendButton;
    private ProgressBar progressBar;

    // Adapters
    private ChatAdapter chatAdapter;
    private QuickReplyAdapter quickReplyAdapter;

    // ViewModel
    private ChatbotViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chatbot, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initViewModel();
        setupRecyclerViews();
        setupClickListeners();
        observeViewModel();

        // Load initial greeting
        viewModel.sendWelcomeMessage();
    }

    private void initViews(View view) {
        chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        quickRepliesRecyclerView = view.findViewById(R.id.quickRepliesRecyclerView);
        messageInput = view.findViewById(R.id.messageInput);
        sendButton = view.findViewById(R.id.sendButton);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(ChatbotViewModel.class);
    }

    private void setupRecyclerViews() {
        // Chat messages RecyclerView
        chatAdapter = new ChatAdapter(new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(layoutManager);
        chatRecyclerView.setAdapter(chatAdapter);

        // Quick replies RecyclerView
        quickReplyAdapter = new QuickReplyAdapter(new ArrayList<>(), quickReply -> {
            // Handle quick reply click
            viewModel.sendMessage(quickReply.getLabel());
            messageInput.setText("");
        });
        LinearLayoutManager quickReplyLayout = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        quickRepliesRecyclerView.setLayoutManager(quickReplyLayout);
        quickRepliesRecyclerView.setAdapter(quickReplyAdapter);
    }

    private void setupClickListeners() {
        sendButton.setOnClickListener(v -> sendMessage());

        messageInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEND) {
                sendMessage();
                return true;
            }
            return false;
        });

        messageInput.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean hasText = s.toString().trim().length() > 0;
                sendButton.setEnabled(hasText);
                sendButton.setAlpha(hasText ? 1.0f : 0.5f);
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }

    private void sendMessage() {
        String message = messageInput.getText().toString().trim();
        if (!message.isEmpty()) {
            viewModel.sendMessage(message);
            messageInput.setText("");

            // Hide keyboard
            android.view.inputmethod.InputMethodManager imm =
                    (android.view.inputmethod.InputMethodManager) requireActivity()
                            .getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(messageInput.getWindowToken(), 0);
            }
        }
    }

    private void observeViewModel() {
        // Observe chat messages
        viewModel.getChatMessages().observe(getViewLifecycleOwner(), messages -> {
            if (messages != null) {
                chatAdapter.updateMessages(messages);
                chatRecyclerView.scrollToPosition(messages.size() - 1);
            }
        });

        // Observe quick replies
        viewModel.getQuickReplies().observe(getViewLifecycleOwner(), quickReplies -> {
            if (quickReplies != null && !quickReplies.isEmpty()) {
                quickReplyAdapter.updateQuickReplies(quickReplies);
                quickRepliesRecyclerView.setVisibility(View.VISIBLE);
            } else {
                quickRepliesRecyclerView.setVisibility(View.GONE);
            }
        });

        // Observe loading state
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                sendButton.setEnabled(!isLoading);
            }
        });

        // Observe errors
        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });

        // Observe suggested recipes
        viewModel.getSuggestedRecipeIds().observe(getViewLifecycleOwner(), recipeIds -> {
            if (recipeIds != null && !recipeIds.isEmpty()) {
                // You can navigate to recipe list or show recipes here
                // For example: showRecipesSuggestions(recipeIds);
            }
        });
    }
}