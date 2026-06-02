package com.example.projectprm392.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.projectprm392.model.Category;
import com.example.projectprm392.repository.CategoryRepository;

import java.util.List;

public class CategoryViewModel extends ViewModel {
    private CategoryRepository repository = new CategoryRepository();
    private MutableLiveData<List<Category>> categories = new MutableLiveData<>();
    private MutableLiveData<Category> category = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();
    public LiveData<List<Category>> getCategories() {
        return categories;
    }
    public void loadAllCategories() {
        isLoading.setValue(true);
        repository.getAllCategory(categories, error);
        isLoading.setValue(false);
    }
}
