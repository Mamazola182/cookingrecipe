package com.example.projectprm392;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectprm392.api.RetrofitClient;
import com.example.projectprm392.model.LoginRequest;
import com.example.projectprm392.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private Button btnLogin;
    private TextView tvSignUp;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        edtUsername = findViewById(R.id.etUsername);
        edtPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);

        // Kiểm tra nếu đã có token → chuyển thẳng sang Main
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String token = prefs.getString("token", null);
        if (token != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            return;
        }

        btnLogin.setOnClickListener(v -> loginUser());
        tvSignUp.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void loginUser() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest request = new LoginRequest(username, password);

        // ✅ Gọi API login (trả về token string)
        RetrofitClient.getInstance().getUserApiService().login(request)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            String token = response.body();

                            // Lưu token
                            SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
                            prefs.edit().putString("token", token).apply();
                            Log.d("LOGIN", "Token saved: " + token);

                            // Sau khi lưu token, gọi API /me để lấy user info
                            getCurrentUserAndProceed();
                        } else {
                            Toast.makeText(LoginActivity.this, "Sai tài khoản hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("LOGIN_FAIL", "Error: ", t);
                    }
                });
    }

    private void getCurrentUserAndProceed() {
        RetrofitClient.getInstance()
                .getUserApiService()
                .getUserMe()
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            User user = response.body();

                            SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
                            prefs.edit()
                                    .putInt("userId", user.getUserId())
                                    .putString("role", user.getRole())
                                    .apply();

                            Log.d("LOGIN", "User saved: id=" + user.getUserId() + ", role=" + user.getRole());
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Không lấy được thông tin người dùng!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("LOGIN_USER_FAIL", "Error: ", t);
                    }
                });
    }
}
