package com.example.projectprm392;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;

public class OnboardingFragment extends Fragment {

    private OnboardingAdapter onboardingAdapter;
    private LinearLayout layoutIndicators;
    private MaterialButton buttonNext;
    private ViewPager2 onboardingViewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_onboarding, container, false);

        layoutIndicators = view.findViewById(R.id.layout_onboarding_indicators);
        buttonNext = view.findViewById(R.id.btn_onboarding_next);
        onboardingViewPager = view.findViewById(R.id.vp_onboarding_pages);
        MaterialButton buttonSkip = view.findViewById(R.id.btn_onboarding_skip);

        setupOnboardingItems();
        onboardingViewPager.setAdapter(onboardingAdapter);
        setupIndicators();
        setCurrentIndicator(0);

        onboardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
            }
        });

        buttonNext.setOnClickListener(v -> {
            if (onboardingViewPager.getCurrentItem() + 1 < onboardingAdapter.getItemCount()) {
                onboardingViewPager.setCurrentItem(onboardingViewPager.getCurrentItem() + 1);
            } else {
                goToMainScreen();
            }
        });

        buttonSkip.setOnClickListener(v -> goToMainScreen());

        return view;
    }

    private void setupOnboardingItems() {
        List<OnboardingItem> items = new ArrayList<>();

        items.add(new OnboardingItem(R.drawable.onboarding1,
                "Tìm kiếm hàng ngàn công thức",
                "Dễ dàng tìm kiếm món ăn yêu thích của bạn theo tên, nguyên liệu hoặc danh mục."));
        items.add(new OnboardingItem(R.drawable.onboarding2,
                "Hướng dẫn chi tiết từng bước",
                "Mỗi công thức đều có hướng dẫn rõ ràng, giúp bạn nấu ăn như đầu bếp thực thụ."));
        items.add(new OnboardingItem(R.drawable.onboarding3,
                "Lưu công thức yêu thích",
                "Lưu lại món ăn bạn yêu thích để nấu lại bất cứ khi nào."));
        onboardingAdapter = new OnboardingAdapter(items);
    }

    private void setupIndicators() {
        ImageView[] indicators = new ImageView[onboardingAdapter.getItemCount()];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(8, 0, 8, 0);

        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getContext());
            indicators[i].setImageResource(R.drawable.indicator_inactive);
            indicators[i].setLayoutParams(params);
            layoutIndicators.addView(indicators[i]);
        }
    }

    private void setCurrentIndicator(int index) {
        int childCount = layoutIndicators.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) layoutIndicators.getChildAt(i);
            if (i == index) {
                imageView.setImageResource(R.drawable.indicator_active);
            } else {
                imageView.setImageResource(R.drawable.indicator_inactive);
            }
        }

        if (index == onboardingAdapter.getItemCount() - 1) {
            buttonNext.setText("Bắt đầu");
        } else {
            buttonNext.setText("Tiếp theo");
        }
    }

    private void goToMainScreen() {

    }
}