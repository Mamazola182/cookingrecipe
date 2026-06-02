package com.example.projectprm392.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.projectprm392.api.RetrofitClient;
import com.example.projectprm392.api.ChatbotApiService;
import com.example.projectprm392.model.ChatMessage;
import com.example.projectprm392.model.ChatRequest;
import com.example.projectprm392.model.ChatResponse;
import com.example.projectprm392.model.QuickReply;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatbotViewModel extends ViewModel {
    private static final String TAG = "ChatbotViewModel";

    private final MutableLiveData<List<ChatMessage>> chatMessages = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<List<QuickReply>> quickReplies = new MutableLiveData<>();
    private final MutableLiveData<List<Integer>> suggestedRecipeIds = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();

    private final ChatbotApiService apiService;
    private final List<ChatMessage> conversationHistory = new ArrayList<>();
    String isoTimestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            .format(new Date());
    public ChatbotViewModel() {
        this.apiService = RetrofitClient.getInstance().getChatbotApiService();
    }

    public LiveData<List<ChatMessage>> getChatMessages() {
        return chatMessages;
    }

    public LiveData<List<QuickReply>> getQuickReplies() {
        return quickReplies;
    }

    public LiveData<List<Integer>> getSuggestedRecipeIds() {
        return suggestedRecipeIds;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void sendWelcomeMessage() {
        ChatMessage welcomeMessage = new ChatMessage();
        welcomeMessage.setRole("assistant");
        welcomeMessage.setContent("Xin chào! Tôi là trợ lý nấu ăn của bạn. Tôi có thể giúp bạn:\n" +
                "• Tìm công thức nấu ăn\n" +
                "• Gợi ý món ăn theo nguyên liệu\n" +
                "• Tư vấn dinh dưỡng\n" +
                "• Hướng dẫn nấu ăn\n\n" +
                "Bạn muốn tìm món gì hôm nay?");

        addMessageToChat(welcomeMessage);

        // Add some quick replies
        List<QuickReply> initialQuickReplies = new ArrayList<>();
        initialQuickReplies.add(new QuickReply("Gợi ý món ăn", "suggest"));
        initialQuickReplies.add(new QuickReply("Món Việt Nam", "vietnamese"));
        initialQuickReplies.add(new QuickReply("Món chay", "vegetarian"));
        initialQuickReplies.add(new QuickReply("Món nhanh", "quick"));
        quickReplies.setValue(initialQuickReplies);
    }
    private String getCurrentIsoTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                .format(new Date());
    }

    public void sendMessage(String message) {
        if (message == null || message.trim().isEmpty()) {
            return;
        }

        // Add user message to chat
        ChatMessage userMessage = new ChatMessage();
        userMessage.setRole("user");
        userMessage.setContent(message);
        userMessage.setTimestamp(new Date());
        addMessageToChat(userMessage);

        // Add to conversation history
        conversationHistory.add(userMessage);

        // Show loading
        isLoading.setValue(true);

        // Create request
        ChatRequest request = new ChatRequest();
        request.setMessage(message);
        request.setConversationHistory(new ArrayList<>(conversationHistory));

        // Call API
        apiService.getAdvice(request).enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                isLoading.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    ChatResponse chatResponse = response.body();

                    // Add bot response to chat
                    ChatMessage botMessage = new ChatMessage();
                    botMessage.setRole("assistant");
                    botMessage.setContent(chatResponse.getReply());
                    addMessageToChat(botMessage);

                    // Add to conversation history
                    conversationHistory.add(botMessage);

                    // Update quick replies if available
                    if (chatResponse.getQuickReplies() != null && !chatResponse.getQuickReplies().isEmpty()) {
                        quickReplies.setValue(chatResponse.getQuickReplies());
                    } else {
                        // Default quick replies based on intent
                        quickReplies.setValue(getDefaultQuickReplies(chatResponse.getIntent()));
                    }

                    // Update suggested recipe IDs
                    if (chatResponse.getSuggestedRecipeIds() != null && !chatResponse.getSuggestedRecipeIds().isEmpty()) {
                        suggestedRecipeIds.setValue(chatResponse.getSuggestedRecipeIds());
                    }

                    Log.d(TAG, "Intent: " + chatResponse.getIntent());
                    Log.d(TAG, "Mentioned Recipes: " + chatResponse.getMentionedRecipes());
                } else {
                    error.setValue("Không thể kết nối với server. Vui lòng thử lại.");
                    Log.e(TAG, "Response error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                isLoading.setValue(false);
                error.setValue("Lỗi kết nối: " + t.getMessage());
                Log.e(TAG, "API call failed", t);

                // Add error message to chat
                ChatMessage errorMessage = new ChatMessage();
                errorMessage.setRole("assistant");
                errorMessage.setContent("Xin lỗi, tôi gặp sự cố kết nối. Vui lòng thử lại sau.");
                addMessageToChat(errorMessage);
            }
        });
    }

    private void addMessageToChat(ChatMessage message) {
        List<ChatMessage> currentMessages = chatMessages.getValue();
        if (currentMessages != null) {
            currentMessages.add(message);
            chatMessages.setValue(new ArrayList<>(currentMessages));
        }
    }

    private List<QuickReply> getDefaultQuickReplies(String intent) {
        List<QuickReply> replies = new ArrayList<>();

        switch (intent) {
            case "Suggestion":
                replies.add(new QuickReply("Món khác", "suggest_more"));
                replies.add(new QuickReply("Chi tiết món này", "details"));
                replies.add(new QuickReply("Nguyên liệu cần gì?", "ingredients"));
                break;

            case "Nutrition":
                replies.add(new QuickReply("Món ít calo", "low_calorie"));
                replies.add(new QuickReply("Món giàu protein", "high_protein"));
                replies.add(new QuickReply("Món chay", "vegetarian"));
                break;

            case "Cooking":
                replies.add(new QuickReply("Món dễ nấu", "easy"));
                replies.add(new QuickReply("Món nhanh", "quick"));
                replies.add(new QuickReply("Video hướng dẫn", "video"));
                break;

            default:
                replies.add(new QuickReply("Gợi ý món", "suggest"));
                replies.add(new QuickReply("Tư vấn dinh dưỡng", "nutrition"));
                replies.add(new QuickReply("Hướng dẫn nấu", "cooking"));
                break;
        }

        return replies;
    }

    public void clearChat() {
        chatMessages.setValue(new ArrayList<>());
        conversationHistory.clear();
        quickReplies.setValue(new ArrayList<>());
        sendWelcomeMessage();
    }
}