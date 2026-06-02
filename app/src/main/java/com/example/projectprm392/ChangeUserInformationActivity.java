package com.example.projectprm392;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.example.projectprm392.api.RetrofitClient;
import com.example.projectprm392.api.UserApiService;
import com.example.projectprm392.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeUserInformationActivity extends AppCompatActivity {

    private EditText etFullName, etEmail;
    private Button btnUpdate;
    private ImageButton btnBack;
    private UserApiService userApiService;
    private int userId = 1; // ⚠️ Tạm thời fix cứng, sau ta lấy từ login/SharedPreferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user); // ⚠️ Tên XML công tử đang dùng nhé

        // Ánh xạ view
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnBack = findViewById(R.id.btnBack);
        userApiService = RetrofitClient.getInstance().getUserApiService();
        btnBack.setOnClickListener(v -> finish());
        btnUpdate.setOnClickListener(v -> updateUserInfo());
    }
    private void updateUserInfo() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        if (fullName.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }
        User updatedUser = new User();
        updatedUser.setFullName(fullName);
        updatedUser.setEmail(email);
        Call<User> call = userApiService.updateUser(userId, updatedUser);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ChangeUserInformationActivity.this,
                            "Updated successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Quay lại màn trước
                } else {
                    Toast.makeText(ChangeUserInformationActivity.this,
                            "Update failed! Code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ChangeUserInformationActivity.this,
                        "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
