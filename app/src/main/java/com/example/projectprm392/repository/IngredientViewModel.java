package com.example.projectprm392.viewmodel; // Bạn có thể đặt tên package là viewmodel

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.projectprm392.model.Ingredient;
import com.example.projectprm392.repository.IngredientRepository;
import java.util.List;

public class IngredientViewModel extends ViewModel {

    private IngredientRepository repository;
    private MutableLiveData<List<Ingredient>> ingredientsLiveData;
    private MutableLiveData<String> errorLiveData;

    public IngredientViewModel() {
        repository = new IngredientRepository();
        ingredientsLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
    }

    // Getter để Activity có thể "observe" (quan sát)
    public MutableLiveData<List<Ingredient>> getIngredientsLiveData() {
        return ingredientsLiveData;
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    // Phương thức để Activity gọi và bắt đầu tải dữ liệu
    public void fetchAllIngredients() {
        repository.getAllIngredient(ingredientsLiveData, errorLiveData);
    }
}