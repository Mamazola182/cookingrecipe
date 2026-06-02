package com.example.projectprm392.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.projectprm392.AllRecipesActivity;
import com.example.projectprm392.MainActivity;
import com.example.projectprm392.R;
import com.example.projectprm392.RecipeDetailActivity;
import com.example.projectprm392.adapter.BannerAdapter;
import com.example.projectprm392.adapter.CategoryAdapter;
import com.example.projectprm392.adapter.RecipeAdapter;
import com.example.projectprm392.model.Category;
import com.example.projectprm392.model.Recipe;
import com.example.projectprm392.viewmodel.CategoryViewModel;
import com.example.projectprm392.viewmodel.RecipeViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment implements RecipeAdapter.OnItemClickListener {
    private static final String TAG = "HomeFragment";

    // ViewModels
    private RecipeViewModel recipeViewModel;
    private CategoryViewModel categoryViewModel;

    // Views
    private ProgressBar progressBar;
    private RecyclerView recyclerView1, recyclerView2, recyclerView3;
    private TextView seeAllTrending, seeAllRecommended;
    // Adapters
    private RecipeAdapter adapter2, adapter3;
    private CategoryAdapter adapter1;

    // Banner components
    private Handler autoScrollHandler;
    private ViewPager2 bannerViewPager;
    private BannerAdapter bannerAdapter;
    private TabLayout tabLayout;
    private int currentPage = 0;
    private List<Integer> bannerImages = Arrays.asList(
            R.drawable.bg1,
            R.drawable.bg5,
            R.drawable.bg3,
            R.drawable.bg4
    );

    private CardView searchCard;

    private Runnable autoScrollRunnable = new Runnable() {
        @Override
        public void run() {
            if (bannerViewPager != null) {
                bannerViewPager.setCurrentItem(currentPage + 1, true);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initViewModels();
        setupBanner();
        setupRecyclerViews();
        setupClickListeners();
        observeViewModel();

        // Load data
        categoryViewModel.loadAllCategories();
        recipeViewModel.loadRecommendRecipes();
        recipeViewModel.loadAllRecipes();
    }

    private void initViews(View view) {
        seeAllTrending = view.findViewById(R.id.seeAllTrending);
        seeAllRecommended = view.findViewById(R.id.seeAllRecommended);
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView1 = view.findViewById(R.id.recyclerView1);
        recyclerView2 = view.findViewById(R.id.recyclerView2);
        recyclerView3 = view.findViewById(R.id.recyclerView3);
        bannerViewPager = view.findViewById(R.id.bannerViewPager);
        tabLayout = view.findViewById(R.id.tabLayout);

        searchCard = view.findViewById(R.id.searchCard);
        autoScrollHandler = new Handler(Looper.getMainLooper());
    }

    private void initViewModels() {
        recipeViewModel = new ViewModelProvider(requireActivity()).get(RecipeViewModel.class);
        categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);
    }

    private void setupRecyclerViews() {
        Intent intent = new Intent(getContext(), AllRecipesActivity.class);
        View.OnClickListener goToAll = v -> startActivity(intent);

        seeAllTrending.setOnClickListener(goToAll);
        seeAllRecommended.setOnClickListener(goToAll);
        // Category RecyclerView
        adapter1 = new CategoryAdapter((category, position) -> {
            Log.d(TAG, "Category clicked: " + category.getName());
            Toast.makeText(getContext(), "Selected: " + category.getName(), Toast.LENGTH_SHORT).show();
            recipeViewModel.loadRecipeByCatgoryId(category.getCategoryId());
        });
        LinearLayoutManager layout1 = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(layout1);
        recyclerView1.setAdapter(adapter1);

        // Recommended Recipes RecyclerView
        adapter2 = new RecipeAdapter(getContext(), new ArrayList<>(), this);
        LinearLayoutManager layout2 = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layout2);
        recyclerView2.setAdapter(adapter2);

        // New Recipes RecyclerView
        adapter3 = new RecipeAdapter(getContext(), new ArrayList<>(), this);
        LinearLayoutManager layout3 = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView3.setLayoutManager(layout3);
        recyclerView3.setAdapter(adapter3);
    }

    private void setupClickListeners() {
        searchCard.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToSearch();
            }
        });
    }

    private void setupBanner() {
        bannerAdapter = new BannerAdapter(bannerImages);
        bannerViewPager.setAdapter(bannerAdapter);

        tabLayout.removeAllTabs();
        for (int i = 0; i < bannerImages.size(); i++) {
            tabLayout.addTab(tabLayout.newTab());
        }
        tabLayout.setClickable(true);

        int startPosition = Integer.MAX_VALUE / 2;
        startPosition = startPosition - (startPosition % bannerImages.size());
        bannerViewPager.setCurrentItem(startPosition, false);
        currentPage = startPosition;

        bannerViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPage = position;
                int realPosition = position % bannerImages.size();
                if (tabLayout.getTabCount() > 0) {
                    tabLayout.selectTab(tabLayout.getTabAt(realPosition));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                switch (state) {
                    case ViewPager2.SCROLL_STATE_DRAGGING:
                        stopAutoScroll();
                        break;
                    case ViewPager2.SCROLL_STATE_IDLE:
                        startAutoScroll();
                        break;
                }
            }
        });
    }

    private void startAutoScroll() {
        stopAutoScroll();
        autoScrollHandler.postDelayed(autoScrollRunnable, 3000);
    }

    private void stopAutoScroll() {
        autoScrollHandler.removeCallbacks(autoScrollRunnable);
    }

    private void observeViewModel() {
        categoryViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            if (categories != null && !categories.isEmpty()) {
                Log.d(TAG, "Received " + categories.size() + " categories");
                adapter1.setCategories(categories);
                adapter1.setSelectedPosition(0);
            }
        });

        recipeViewModel.getRecommendRecipes().observe(getViewLifecycleOwner(), recommendList -> {
            if (recommendList != null && !recommendList.isEmpty()) {
                Log.d(TAG, "Received " + recommendList.size() + " recommended recipes");
                adapter2.updateData(recommendList);
            }
        });

        recipeViewModel.getRecipes().observe(getViewLifecycleOwner(), recipes -> {
            if (recipes != null && !recipes.isEmpty()) {
                Log.d(TAG, "Received " + recipes.size() + " recipes");
                adapter3.updateData(recipes);
            }
        });

        recipeViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });

        recipeViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error: " + error);
            }
        });
    }

    @Override
    public void onDetailClick(Recipe recipe) {
        Log.d(TAG, "Clicked on: " + recipe.getTitle());
        Toast.makeText(getContext(), "Clicked: " + recipe.getTitle(), Toast.LENGTH_SHORT).show();
        // Navigate to detail fragment
        Intent intent = new Intent(getContext(), RecipeDetailActivity.class);
        intent.putExtra("RECIPE_ID", recipe.getRecipeId());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        startAutoScroll();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopAutoScroll();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopAutoScroll();
    }
}