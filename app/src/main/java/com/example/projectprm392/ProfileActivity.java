package com.example.projectprm392;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projectprm392.adapter.ProfileAdapter;
import com.example.projectprm392.api.RetrofitClient;
import com.example.projectprm392.api.UserApiService;
import com.example.projectprm392.model.RecipeUser;
import com.example.projectprm392.model.User;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    private TextView profileFullname, profileEmail;
    private RecyclerView recyclerView;
    private UserApiService userApiService;
//    private ImageView avatarProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_user);
        // Ánh xạ View
//        avatarProfile = findViewById(R.id.avatarProfile);
        profileFullname = findViewById(R.id.profileFullname);
        profileEmail = findViewById(R.id.profileEmail);
//        recyclerView = findViewById(R.id.rcv_profile);
        ImageButton backBtn = findViewById(R.id.bt_trove);
        Button editUser = findViewById(R.id.editProfile);

        // Nút quay lại
        backBtn.setOnClickListener(v -> finish());
        // Khởi tạo Retrofit service
        userApiService = RetrofitClient.getInstance().getUserApiService();
        // Gọi API lấy user (tạm ID = 1)
        loadUserInfo(1);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(new ProfileAdapter(recipes));
        editUser.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, ChangeUserInformationActivity.class);
            startActivity(intent);
        });
    }
    private void loadUserInfo(int userId) {
        userApiService.getUserById(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
//                    avatarProfile.setImageResource(user.);
                    profileFullname.setText(user.getFullName());
                    profileEmail.setText(user.getEmail());

                } else {
                    Toast.makeText(ProfileActivity.this, "Không lấy được thông tin người dùng", Toast.LENGTH_SHORT).show();
                    Log.e("ProfileActivity", "Response error: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
                Log.e("ProfileActivity", "API Error: " + t.getMessage());
            }
        });
    }
}
