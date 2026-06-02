package com.example.projectprm392;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectprm392.api.RetrofitClient;
import com.example.projectprm392.model.RegisterRequest;
import com.example.projectprm392.model.User;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText  edtFullname, edtPassword;
    private Button btnRegister;
    private TextView tvSignIn;
    private TextInputEditText edtUsername, edtEmail;
    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        edtUsername = findViewById(R.id.etUsername);
         edtEmail = findViewById(R.id.etEmail);
        edtFullname = findViewById(R.id.etFullName);
        edtPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvSignIn = findViewById(R.id.tvSignIn);

        btnRegister.setOnClickListener(v -> registerUser());
        tvSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void registerUser() {
        String username = edtUsername.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String fullname = edtFullname.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || fullname.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterRequest user = new RegisterRequest(username, email, password,fullname);

        RetrofitClient.getInstance().getUserApiService().register(user)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(RegisterActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
