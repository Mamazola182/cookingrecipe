package com.example.projectprm392.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projectprm392.LoginActivity;
import com.example.projectprm392.R;
import com.example.projectprm392.RecipeManagementActivity;
import com.example.projectprm392.api.RetrofitClient;
import com.example.projectprm392.api.UserApiService;
import com.example.projectprm392.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private TextView txtName, txtEmail;
    private ImageView imgAvatar;
    private View layoutRecipeManage, layoutIngredientManage;
    private UserApiService userApi;
    private int currentUserId = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // 🔹 Ánh xạ view
        txtName = view.findViewById(R.id.profile_name1);
        txtEmail = view.findViewById(R.id.profile_email1);
        imgAvatar = view.findViewById(R.id.profile_avatar1);
        layoutRecipeManage = view.findViewById(R.id.layout_recipe_manage);
        layoutIngredientManage = view.findViewById(R.id.layout_ingredient_manage);

        // 🔹 Ẩn menu quản lý trước khi biết role
        if (layoutRecipeManage != null) layoutRecipeManage.setVisibility(View.GONE);
        if (layoutIngredientManage != null) layoutIngredientManage.setVisibility(View.GONE);

        // ✅ Lấy instance Retrofit có AuthInterceptor
        userApi = RetrofitClient.getInstance().getUserApiService();

        // 🔹 Kiểm tra token
        SharedPreferences prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);
        if (token == null) {
            Toast.makeText(getContext(), "Vui lòng đăng nhập trước khi xem hồ sơ", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(), LoginActivity.class));
            requireActivity().finish();
            return view;
        }

        loadUserInfo();

        View deleteAccount = view.findViewById(R.id.layout_delete_account);
        if (deleteAccount != null) {
            deleteAccount.setOnClickListener(v -> showDeleteConfirmation());
        }

        View logoutLayout = view.findViewById(R.id.layout_logout);
        if (logoutLayout != null) {
            logoutLayout.setOnClickListener(v -> logoutAndRedirect());
        }

        return view;
    }

    private void loadUserInfo() {
        userApi.getUserMe().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    currentUserId = user.getUserId();

                    txtName.setText(user.getFullName());
                    txtEmail.setText(user.getEmail());
                    // Nếu có avatar: Picasso.get().load(user.getAvatarUrl()).into(imgAvatar);

                    String role = user.getRole();
                    if ("chef".equalsIgnoreCase(role)) {
                        layoutRecipeManage.setVisibility(View.VISIBLE);
                        layoutIngredientManage.setVisibility(View.VISIBLE);

                        layoutRecipeManage.setOnClickListener(v -> {
                            Intent intent = new Intent(getContext(), RecipeManagementActivity.class);
                            startActivity(intent);
                        });
//                        layoutIngredientManage.setOnClickListener(v -> {
//                            Intent intent = new Intent(getContext(), RecipeManagementActivity.class);
//                            startActivity(intent);
//                        });
                    }

                } else if (response.code() == 401) {
                    Toast.makeText(getContext(), "Phiên đăng nhập đã hết hạn, vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
                    logoutAndRedirect();
                } else {
                    Toast.makeText(getContext(), "Không tải được thông tin người dùng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDeleteConfirmation() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xóa tài khoản")
                .setMessage("Bạn có chắc muốn xóa tài khoản này không? Hành động này không thể hoàn tác.")
                .setPositiveButton("Xóa", (dialog, which) -> deleteAccount())
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteAccount() {
        if (currentUserId == -1) {
            Toast.makeText(getContext(), "Không xác định được người dùng hiện tại", Toast.LENGTH_SHORT).show();
            return;
        }

        userApi.deleteUser(currentUserId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Đã xóa tài khoản thành công", Toast.LENGTH_SHORT).show();
                    logoutAndRedirect();
                } else if (response.code() == 401) {
                    Toast.makeText(getContext(), "Phiên đăng nhập đã hết hạn, vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
                    logoutAndRedirect();
                } else {
                    Toast.makeText(getContext(), "Xóa tài khoản thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void RecipeManagement() {

    }
    private void logoutAndRedirect() {
        SharedPreferences prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        prefs.edit().remove("token").apply();
        startActivity(new Intent(getContext(), LoginActivity.class));
        requireActivity().finish();
    }
}
