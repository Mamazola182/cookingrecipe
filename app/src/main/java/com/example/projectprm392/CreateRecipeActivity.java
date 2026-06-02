package com.example.projectprm392;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectprm392.adapter.createrecipe.IngredientRecipeAdapter;
import com.example.projectprm392.adapter.createrecipe.StepRecipeAdapter;
import com.example.projectprm392.viewmodel.IngredientViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.example.projectprm392.model.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreateRecipeActivity extends AppCompatActivity {

    private static final int PICK_THUMBNAIL_IMAGE = 1;
    private static final int PICK_STEP_IMAGE = 2;

    private TextInputEditText etTitle, etDescription;
    private TextInputEditText etPrepTime, etCookTime;
    private Spinner spinnerDifficulty;
    private RecyclerView rvIngredients, rvSteps;
    private Button btnAddIngredient, btnAddStep, btnSave;
    private RelativeLayout thumbnailContainer;
    private ImageView ivThumbnail;
    private View uploadPlaceholder;
    private HashMap<String, Ingredient> masterIngredientMap;
    private IngredientRecipeAdapter ingredientAdapter;
    private StepRecipeAdapter stepAdapter;
    private List<Ingredient> ingredientList;
    private List<Ingredient> masterIngredientList;
    private List<RecipeStep> stepList;

    private Uri thumbnailUri;
    private int currentStepImagePosition = -1;
    private IngredientViewModel ingredientViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);
        ingredientViewModel = new ViewModelProvider(this).get(IngredientViewModel.class);
        masterIngredientList = new ArrayList<>();
        masterIngredientMap = new HashMap<>();
        initViews();
        setupSpinner();
        setupRecyclerViews();

        setupListeners();
        observeViewModel();
        ingredientViewModel.fetchAllIngredients();
    }

    private void initViews() {
        // TextInputs
        etTitle = findViewById(R.id.et_title);
        etDescription = findViewById(R.id.et_description);
        etPrepTime = findViewById(R.id.et_prep_time);
        etCookTime = findViewById(R.id.et_cook_time);

        // Spinner
        spinnerDifficulty = findViewById(R.id.spinner_difficulty);

        // RecyclerViews
        rvIngredients = findViewById(R.id.rv_ingredients);
        rvSteps = findViewById(R.id.rv_steps);

        // Buttons
        btnAddIngredient = findViewById(R.id.btn_add_ingredient);
        btnAddStep = findViewById(R.id.btn_add_step);
        btnSave = findViewById(R.id.btn_save);

        // Thumbnail
        thumbnailContainer = findViewById(R.id.thumbnail_container);
        ivThumbnail = findViewById(R.id.iv_thumbnail);
        uploadPlaceholder = findViewById(R.id.upload_placeholder);

        // Back button
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }

    private void setupSpinner() {
        String[] difficulties = {"Easy", "Medium", "Hard"};
        String[] displayNames = {"Dễ", "Trung bình", "Khó"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                displayNames
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adapter);
        spinnerDifficulty.setSelection(1); // Medium by default
    }

    private void setupRecyclerViews() {
        ingredientList = new ArrayList<>();

        // !! QUAN TRỌNG: Truyền masterIngredientMap vào adapter
        ingredientAdapter = new IngredientRecipeAdapter(
                ingredientList,
                masterIngredientList,
                masterIngredientMap, // <--- THÊM BIẾN NÀY
                this,
                this::removeIngredient
        );

        rvIngredients.setLayoutManager(new LinearLayoutManager(this));
        rvIngredients.setAdapter(ingredientAdapter);
        rvIngredients.setNestedScrollingEnabled(false);

        addEmptyIngredientRow();
        // Steps RecyclerView
        stepList = new ArrayList<>();
        RecipeStep emptyStep = new RecipeStep();
        emptyStep.setStepNumber(1);
        emptyStep.setContent("");
        stepList.add(emptyStep);

        stepAdapter = new StepRecipeAdapter(stepList, this::removeStep, this::onStepImageClick);
        rvSteps.setLayoutManager(new LinearLayoutManager(this));
        rvSteps.setAdapter(stepAdapter);
        rvSteps.setNestedScrollingEnabled(false);
    }
    private void addEmptyIngredientRow() {
        Ingredient emptyIngredient = new Ingredient();
        emptyIngredient.setName("");
        emptyIngredient.setQuantity("");
        ingredientList.add(emptyIngredient);
        // Thông báo cho adapter một item MỚI đã được thêm
        ingredientAdapter.notifyItemInserted(ingredientList.size() - 1);
    }
    private void observeViewModel() {
        // Lắng nghe danh sách nguyên liệu
        ingredientViewModel.getIngredientsLiveData().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(List<Ingredient> ingredients) {
                if (ingredients != null && !ingredients.isEmpty()) {
                    // CẬP NHẬT MASTER LIST VÀ MAP
                    masterIngredientList.clear();
                    masterIngredientMap.clear(); // Xóa map cũ

                    masterIngredientList.addAll(ingredients);

                    // Đổ dữ liệu vào map để tra cứu
                    for (Ingredient ing : ingredients) {
                        masterIngredientMap.put(ing.getName(), ing);
                    }

                    ingredientAdapter.notifyDataSetChanged();
                }
            }
        });

        // Lắng nghe lỗi
        ingredientViewModel.getErrorLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if (error != null) {
                    Toast.makeText(CreateRecipeActivity.this, "Lỗi tải nguyên liệu: " + error, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void setupListeners() {
        btnAddIngredient.setOnClickListener(v -> {
            addEmptyIngredientRow(); // Chỉ cần gọi hàm này
            rvIngredients.smoothScrollToPosition(ingredientList.size() - 1);
        });

        btnAddStep.setOnClickListener(v -> {
            int stepNumber = stepList.size() + 1;
            RecipeStep newStep = new RecipeStep();
            newStep.setStepNumber(stepNumber);
            newStep.setContent("");
            stepList.add(newStep);
            stepAdapter.notifyItemInserted(stepList.size() - 1);
            rvSteps.smoothScrollToPosition(stepList.size() - 1);
        });

        btnSave.setOnClickListener(v -> saveRecipe());

        thumbnailContainer.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_THUMBNAIL_IMAGE);
        });
    }

    private void onStepImageClick(int position) {
        currentStepImagePosition = position;
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_STEP_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();

            if (requestCode == PICK_THUMBNAIL_IMAGE) {
                thumbnailUri = selectedImageUri;
                ivThumbnail.setImageURI(selectedImageUri);
                ivThumbnail.setVisibility(View.VISIBLE);
                uploadPlaceholder.setVisibility(View.GONE);
            } else if (requestCode == PICK_STEP_IMAGE && currentStepImagePosition >= 0) {
                stepList.get(currentStepImagePosition).setImageUrl(selectedImageUri.toString());
                stepAdapter.notifyItemChanged(currentStepImagePosition);
                currentStepImagePosition = -1;
            }
        }
    }

    private void removeIngredient(int position) {
        if (ingredientList.size() > 1) {
            ingredientList.remove(position);
            ingredientAdapter.notifyItemRemoved(position);
        } else {
            Toast.makeText(this, "Phải có ít nhất 1 nguyên liệu", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeStep(int position) {
        if (stepList.size() > 1) {
            stepList.remove(position);
            for (int i = 0; i < stepList.size(); i++) {
                stepList.get(i).setStepNumber(i + 1);
            }
            stepAdapter.notifyItemRemoved(position);
            stepAdapter.notifyItemRangeChanged(position, stepList.size());
        } else {
            Toast.makeText(this, "Phải có ít nhất 1 bước", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveRecipe() {
        String title = etTitle.getText().toString().trim();
        if (title.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên công thức", Toast.LENGTH_SHORT).show();
            etTitle.requestFocus();
            return;
        }

        String description = etDescription.getText().toString().trim();
        if (description.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mô tả", Toast.LENGTH_SHORT).show();
            etDescription.requestFocus();
            return;
        }

        int prepTime = 0;
        int cookTime = 0;

        try {
            String prepTimeStr = etPrepTime.getText().toString().trim();
            String cookTimeStr = etCookTime.getText().toString().trim();

            if (prepTimeStr.isEmpty() || cookTimeStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thời gian", Toast.LENGTH_SHORT).show();
                return;
            }

            prepTime = Integer.parseInt(prepTimeStr);
            cookTime = Integer.parseInt(cookTimeStr);

            if (prepTime <= 0 || cookTime <= 0) {
                Toast.makeText(this, "Thời gian phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Thời gian phải là số hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate thumbnail
        if (thumbnailUri == null) {
            Toast.makeText(this, "Vui lòng chọn ảnh đại diện", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate ingredients
        List<Ingredient> validIngredients = new ArrayList<>();
        for (Ingredient ing : ingredientList) {
            String name = (ing.getName() != null) ? ing.getName().trim() : "";
            String quantity = (ing.getQuantity() != null) ? ing.getQuantity().trim() : "";

            // Giả sử ID là kiểu int, 0 là không hợp lệ
            // Nếu ID là kiểu Integer, hãy kiểm tra ing.getId() != null
            if (!name.isEmpty() && !quantity.isEmpty() && ing.getIngredientId() != 0) {
                validIngredients.add(ing);
            }
        }

        if (validIngredients.isEmpty()) {
            Toast.makeText(this, "Vui lòng thêm ít nhất 1 nguyên liệu hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (validIngredients.size() < ingredientList.size()) {
            Toast.makeText(this, "Một số nguyên liệu không hợp lệ. Vui lòng chọn từ danh sách.", Toast.LENGTH_LONG).show();
            return;
        }
        // Validate steps
        List<RecipeStep> validSteps = new ArrayList<>();
        for (RecipeStep step : stepList) {
            if (step.getContent() != null && !step.getContent().trim().isEmpty()) {
                validSteps.add(step);
            }
        }

        if (validSteps.isEmpty()) {
            Toast.makeText(this, "Vui lòng thêm nội dung cho ít nhất 1 bước", Toast.LENGTH_SHORT).show();
            return;
        }

        // Build Recipe object
        RecipeDetail recipe = buildRecipe(title, description, prepTime, cookTime, validIngredients, validSteps);

        // TODO: Call API to save recipe
        // Example:
        // RecipeApiService.createRecipe(recipe, new Callback() {
        //     @Override
        //     public void onSuccess(Response response) {
        //         Toast.makeText(CreateRecipeActivity.this, "Lưu thành công!", Toast.LENGTH_SHORT).show();
        //         finish();
        //     }
        //
        //     @Override
        //     public void onError(String error) {
        //         Toast.makeText(CreateRecipeActivity.this, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
        //     }
        // });

        Toast.makeText(this, "Đang lưu công thức...", Toast.LENGTH_SHORT).show();

        // For demo - print recipe info
        printRecipeInfo(recipe);
    }

    private RecipeDetail buildRecipe(String title, String description, int prepTime,
                                     int cookTime, List<Ingredient> ingredients,
                                     List<RecipeStep> steps) {
        RecipeDetail recipe = new RecipeDetail();

        // Basic info (you'll need to add setters to RecipeDetail)
         recipe.setTitle(title);
         recipe.setDescription(description);
         recipe.setPrepTime(prepTime);
         recipe.setCookTime(cookTime);
         recipe.setTotalTime(prepTime + cookTime);

        // Difficulty
        String[] difficultyValues = {"Easy", "Medium", "Hard"};
        String difficulty = difficultyValues[spinnerDifficulty.getSelectedItemPosition()];
         recipe.setDifficulty(difficulty);

//         Thumbnail
         recipe.setThumbnail(thumbnailUri.toString());

//         Ingredients
         recipe.setIngredients(ingredients);

//         Steps
         recipe.setSteps(steps);

        return recipe;
    }

    private void printRecipeInfo(RecipeDetail recipe) {
        System.out.println("=== RECIPE INFO ===");
        System.out.println("Title: " + recipe.getTitle());
        System.out.println("Description: " + recipe.getDescription());
        System.out.println("Prep Time: " + recipe.getPrepTime() + " phút");
        System.out.println("Cook Time: " + recipe.getCookTime() + " phút");
        System.out.println("Total Time: " + recipe.getTotalTime() + " phút");
        System.out.println("Difficulty: " + recipe.getDifficulty());
        System.out.println("Thumbnail: " + recipe.getThumbnail());

        System.out.println("\n=== INGREDIENTS ===");
        for (Ingredient ing : recipe.getIngredients()) {
            System.out.println("- " + ing.getName() + ": " + ing.getQuantity());
        }

        System.out.println("\n=== STEPS ===");
        for (RecipeStep step : recipe.getSteps()) {
            System.out.println("Bước " + step.getStepNumber() + ": " + step.getContent());
            if (step.getDuration() != null) {
                System.out.println("  Thời gian: " + step.getDuration() + " phút");
            }
            if (step.getImageUrl() != null) {
                System.out.println("  Ảnh: " + step.getImageUrl());
            }
        }
    }
}