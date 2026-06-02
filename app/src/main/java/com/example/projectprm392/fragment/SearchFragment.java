package com.example.projectprm392.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.R;
import com.example.projectprm392.RecipeDetailActivity;
import com.example.projectprm392.adapter.RecipeAdapter;
import com.example.projectprm392.adapter.SearchResultAdapter;
import com.example.projectprm392.model.Recipe;
import com.example.projectprm392.viewmodel.RecipeViewModel;

import java.util.ArrayList;

// 1. Implement OnItemClickListener giống như HomeFragment
public class SearchFragment extends Fragment implements RecipeAdapter.OnItemClickListener {

    private static final String TAG = "SearchFragment";

    private RecipeViewModel viewModel;
    private EditText searchEditText;
    private Button searchButton;
    private RecyclerView recyclerView;
    private SearchResultAdapter adapter;
    private ProgressBar progressBar;
    private TextView noResultsTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 2. Inflate layout
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 3. Lấy ViewModel chung của Activity
        viewModel = new ViewModelProvider(requireActivity()).get(RecipeViewModel.class);

        // 4. Ánh xạ Views
        searchEditText = view.findViewById(R.id.searchEditText);
        searchButton = view.findViewById(R.id.searchButton);
        recyclerView = view.findViewById(R.id.searchResultsRecyclerView);
        progressBar = view.findViewById(R.id.searchProgressBar);
        noResultsTextView = view.findViewById(R.id.noResultsTextView);

        // 5. Setup RecyclerView
        setupRecyclerView();

        // 6. Setup nút tìm kiếm
        setupSearchListeners();

        // 7. Lắng nghe kết quả từ ViewModel
        observeViewModel();
    }

    private void setupRecyclerView() {
        // Dùng RecipeAdapter và 'this' (SearchFragment) làm listener
        adapter = new SearchResultAdapter(getContext(), new ArrayList<>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void setupSearchListeners() {
        searchButton.setOnClickListener(v -> performSearch());

        // Cho phép tìm kiếm khi nhấn nút "Enter" (Search) trên bàn phím
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });
    }

    private void performSearch() {
        String query = searchEditText.getText().toString().trim();
        if (query.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập từ khóa", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ẩn bàn phím
        hideKeyboard();

        // Ẩn kết quả cũ, hiển thị loading
        noResultsTextView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        // Gọi hàm searchRecipes từ ViewModel
        viewModel.searchRecipes(query);
    }

    private void observeViewModel() {
        // Lắng nghe danh sách recipes (kết quả tìm kiếm)
        viewModel.getRecipes().observe(getViewLifecycleOwner(), recipes -> {
            if (recipes != null) {
                adapter.updateData(recipes);

                // Hiển thị/ẩn thông báo "không có kết quả"
                if (recipes.isEmpty()) {
                    noResultsTextView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    noResultsTextView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });

        // Lắng nghe trạng thái loading
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        });

        // Lắng nghe lỗi
        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), "Lỗi: " + error, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error: " + error);
            }
        });
    }

    // 8. Xử lý click (Giống hệt HomeFragment)
    @Override
    public void onDetailClick(Recipe recipe) {
        Log.d(TAG, "Clicked on: " + recipe.getTitle());
        // Mở RecipeDetailActivity
        Intent intent = new Intent(getContext(), RecipeDetailActivity.class);
        intent.putExtra("RECIPE_ID", recipe.getRecipeId());
        startActivity(intent);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = getActivity().getCurrentFocus();
        if (view == null) {
            view = new View(getActivity());
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Tự động focus và mở bàn phím khi vào màn hình
        searchEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Ẩn bàn phím khi rời màn hình
        hideKeyboard();
    }
}