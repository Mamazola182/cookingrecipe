package com.example.projectprm392.adapter.createrecipe;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projectprm392.R; // Đảm bảo bạn đã import R
import com.example.projectprm392.model.Ingredient;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class IngredientRecipeAdapter extends RecyclerView.Adapter<IngredientRecipeAdapter.IngredientViewHolder> {

    // Danh sách nguyên liệu người dùng chọn cho công thức
    private List<Ingredient> recipeIngredientList;
    // Danh sách tất cả nguyên liệu từ API để gợi ý
    private List<Ingredient> masterIngredientList;
    private Context context;
    private final OnRemoveClickListener removeClickListener;
    private HashMap<String, Ingredient> masterIngredientMap;
    public interface OnRemoveClickListener {
        void onRemoveClick(int position);
    }

    public IngredientRecipeAdapter(List<Ingredient> recipeIngredientList,
                                   List<Ingredient> masterIngredientList,
                                   HashMap<String, Ingredient> masterIngredientMap, // Thêm Map
                                   Context context,
                                   OnRemoveClickListener removeClickListener) {
        this.recipeIngredientList = recipeIngredientList;
        this.masterIngredientList = masterIngredientList;
        this.masterIngredientMap = masterIngredientMap; // Gán Map
        this.context = context;
        this.removeClickListener = removeClickListener;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_create_recipe_ingredient, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        // Lấy nguyên liệu hiện tại của công thức
        Ingredient currentIngredient = recipeIngredientList.get(position);

        // --- Cấu hình AutoCompleteTextView ---

        // Lấy danh sách tên nguyên liệu từ master list
        List<String> ingredientNames = new ArrayList<>();
        if (masterIngredientList != null) {
            for (Ingredient ing : masterIngredientList) {
                ingredientNames.add(ing.getName());
            }
        }

        // Tạo Adapter cho AutoCompleteTextView
        ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<>(
                context,
                android.R.layout.simple_dropdown_item_1line,
                ingredientNames
        );
        holder.actvName.setAdapter(autoCompleteAdapter);

        // --- Gán dữ liệu và Listener ---

        // Gán dữ liệu (tạm thời vô hiệu hóa listener để tránh vòng lặp vô hạn)
        holder.removeTextWatchers();
        holder.actvName.setText(currentIngredient.getName());
        holder.etQuantity.setText(currentIngredient.getQuantity());
        holder.addTextWatchers();

        // Listener khi người dùng *chọn* một item từ dropdown
        holder.actvName.setOnItemClickListener((parent, view, pos, id) -> {
            String selectedName = (String) parent.getItemAtPosition(pos);
            currentIngredient.setName(selectedName);
            // Bạn cũng có thể tìm ID của nguyên liệu nếu cần
            // findIngredientIdByName(selectedName);
        });
    }

    @Override
    public int getItemCount() {
        return recipeIngredientList.size();
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {
        AutoCompleteTextView actvName;
        TextInputEditText etQuantity;
        ImageButton btnRemove;

        private TextWatcher nameWatcher;
        private TextWatcher quantityWatcher;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            actvName = itemView.findViewById(R.id.actv_ingredient_name);
            etQuantity = itemView.findViewById(R.id.et_ingredient_quantity);
            btnRemove = itemView.findViewById(R.id.btn_remove_ingredient);

            btnRemove.setOnClickListener(v -> {
                if (removeClickListener != null) {
                    removeClickListener.onRemoveClick(getAdapterPosition());
                }
            });
        }

        public void addTextWatchers() {
            // Listener khi người dùng *gõ* vào trường tên
            nameWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    String currentName = s.toString();
                    Ingredient currentRecipeIngredient = recipeIngredientList.get(getAdapterPosition());

                    // Luôn cập nhật tên
                    currentRecipeIngredient.setName(currentName);

                    // Kiểm tra xem tên này có trong Map không
                    if (masterIngredientMap.containsKey(currentName)) {
                        // Tên hợp lệ, lấy ID từ Map
                        Ingredient masterIngredient = masterIngredientMap.get(currentName);
                        currentRecipeIngredient.setIngredientId(masterIngredient.getIngredientId());
                    } else {
                        currentRecipeIngredient.setIngredientId(0);
                    }
                }
            };

            // quantityWatcher giữ nguyên
            quantityWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    recipeIngredientList.get(getAdapterPosition()).setQuantity(s.toString());
                }
                @Override
                public void afterTextChanged(Editable s) {}
            };

            actvName.addTextChangedListener(nameWatcher);
            etQuantity.addTextChangedListener(quantityWatcher);
        }

        public void removeTextWatchers() {
            actvName.removeTextChangedListener(nameWatcher);
            etQuantity.removeTextChangedListener(quantityWatcher);
        }
    }
}