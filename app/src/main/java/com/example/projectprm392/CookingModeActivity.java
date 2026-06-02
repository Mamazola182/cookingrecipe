package com.example.projectprm392;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.projectprm392.model.RecipeDetail;
import com.example.projectprm392.model.RecipeStep;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class CookingModeActivity extends AppCompatActivity {

    private TextView tvRecipeName, tvStepTitle, tvStepDescription, tvStepProgress, tvTimer;
    private ProgressBar progressBar;
    private Button btnNext, btnPrevious, btnStartTimer, btnResetTimer;
    private ImageView ivStepImage;

    private int currentStep = 0;
    private CountDownTimer countDownTimer;
    private boolean timerRunning = false;
    private long timeLeftInMillis = 0;

    private List<RecipeStep> steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cooking_mode);

        RecipeDetail recipe = (RecipeDetail) getIntent().getSerializableExtra("recipe_detail");
        if (recipe == null) {
            Toast.makeText(this, "Không có dữ liệu công thức!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Gán View
        tvRecipeName = findViewById(R.id.tvRecipeName);
        tvStepTitle = findViewById(R.id.tvStepTitle);
        tvStepDescription = findViewById(R.id.tvStepDescription);
        tvStepProgress = findViewById(R.id.tvStepProgress);
        tvTimer = findViewById(R.id.tvTimer);
        progressBar = findViewById(R.id.progressBar);
        btnNext = findViewById(R.id.btnNext);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnStartTimer = findViewById(R.id.btnStartTimer);
        btnResetTimer = findViewById(R.id.btnResetTimer);
        ivStepImage = findViewById(R.id.ivStepImage);

        tvRecipeName.setText(recipe.getTitle());
        steps = recipe.getSteps();

        if (steps == null || steps.isEmpty()) {
            Toast.makeText(this, "Công thức này chưa có bước nấu!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        updateStepUI();

        btnNext.setOnClickListener(v -> {
            if (currentStep < steps.size() - 1) {
                currentStep++;
                updateStepUI();
            } else {
                Toast.makeText(this, "Đã là bước cuối cùng!", Toast.LENGTH_SHORT).show();
            }
        });

        btnPrevious.setOnClickListener(v -> {
            if (currentStep > 0) {
                currentStep--;
                updateStepUI();
            } else {
                Toast.makeText(this, "Đang ở bước đầu tiên!", Toast.LENGTH_SHORT).show();
            }
        });

        btnStartTimer.setOnClickListener(v -> startOrPauseTimer());
        btnResetTimer.setOnClickListener(v -> resetTimer());
        FloatingActionButton fabExit = findViewById(R.id.fabExit);
        fabExit.setOnClickListener(v -> {
            // Dừng timer nếu đang chạy
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            // Kết thúc activity để quay lại màn hình trước
            finish();
        });

    }

    private void updateStepUI() {
        RecipeStep step = steps.get(currentStep);
        tvStepTitle.setText("Bước " + (currentStep + 1));
        tvStepDescription.setText(step.getContent());
        tvStepProgress.setText("Bước " + (currentStep + 1) + " / " + steps.size());
        progressBar.setProgress((int) (((currentStep + 1) / (float) steps.size()) * 100));

        // Hiển thị ảnh bằng Glide
        if (step.getImageUrl() != null && !step.getImageUrl().isEmpty()) {
            Glide.with(this)
                    .load(step.getImageUrl())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(ivStepImage);
        } else {
            ivStepImage.setImageResource(R.drawable.ic_launcher_foreground);
        }

        timeLeftInMillis = (step.getDuration() != null && step.getDuration() > 0
                ? step.getDuration()
                : 1) * 60 * 1000L;
        updateTimerText();
        resetTimer();
    }

    private void startOrPauseTimer() {
        if (timerRunning) {
            pauseTimer();
        } else {
            startTimer();
        }
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                btnStartTimer.setText("Bắt đầu");
                Toast.makeText(CookingModeActivity.this, "Hoàn thành bước này!", Toast.LENGTH_SHORT).show();
                if(currentStep < steps.size() - 1){
                    currentStep++;
                }
                updateStepUI();
            }
        }.start();

        timerRunning = true;
        btnStartTimer.setText("Tạm dừng");
    }

    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        timerRunning = false;
        btnStartTimer.setText("Tiếp tục");
    }

    private void resetTimer() {
        if (countDownTimer != null) countDownTimer.cancel();
        RecipeStep step = steps.get(currentStep);
        timeLeftInMillis = (step.getDuration() != null && step.getDuration() > 0
                ? step.getDuration()
                : 1) * 60 * 1000L;
        updateTimerText();
        btnStartTimer.setText("Bắt đầu");
        timerRunning = false;
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        tvTimer.setText(timeFormatted);
    }
}
