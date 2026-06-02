package com.example.projectprm392.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projectprm392.R;
import com.example.projectprm392.api.UserApiService;
import com.example.projectprm392.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditProfileFragment extends Fragment {

    private EditText etFullname, etEmail;
    private Button btnSave;
    private UserApiService userApi;

    private int currentUserId = 1; // ⚠️ Tạm thời gán cứng userId (sau này thay bằng user login thực tế)

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        etFullname = view.findViewById(R.id.et_edit_fullname);
        etEmail = view.findViewById(R.id.et_edit_email);
        btnSave = view.findViewById(R.id.btn_save_edit);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://cookingrecipe-production.up.railway.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userApi = retrofit.create(UserApiService.class);

        loadUserData();

        btnSave.setOnClickListener(v -> updateUser());

        return view;
    }

    private void loadUserData() {
        userApi.getUserById(currentUserId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User u = response.body();
                    etFullname.setText(u.getFullName());
                    etEmail.setText(u.getEmail());
                } else {
                    Toast.makeText(getContext(), "Không tải được dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUser() {
        String name = etFullname.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(getContext(), "Không được bỏ trống", Toast.LENGTH_SHORT).show();
            return;
        }

        User updatedUser = new User();
        updatedUser.setFullName(name);
        updatedUser.setEmail(email);

        userApi.updateUser(currentUserId, updatedUser).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
