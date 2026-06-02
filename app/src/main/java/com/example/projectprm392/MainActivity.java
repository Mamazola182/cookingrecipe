package com.example.projectprm392;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.projectprm392.fragment.ChatbotFragment;
import com.example.projectprm392.fragment.HomeFragment;
import com.example.projectprm392.fragment.ProfileFragment;
import com.example.projectprm392.fragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        // Load HomeFragment mặc định
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment(),true);
        }

        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                loadFragment(new HomeFragment(), true);
                return true;
            } else if (itemId == R.id.nav_tv) {
                loadFragment(new SearchFragment(), false);
                return true;
            } else if (itemId == R.id.nav_docs) {
                loadFragment(new ChatbotFragment(), false);
                return true;
            } else if (itemId == R.id.nav_video) {
                loadFragment(new FavouriteFragment(), false);
                return true;
            } else if (itemId == R.id.nav_live) {
                loadFragment(new ProfileFragment(), false);
                return true;
            }

            if (selectedFragment != null) {
                return true;
            }
            return false;
        });
    }

    public void navigateToSearch() {
        loadFragment(new SearchFragment(), false);

        if (bottomNavigation != null) {
            bottomNavigation.setSelectedItemId(R.id.nav_tv);
        }
    }
    private void loadFragment(Fragment fragment, boolean isInitial) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.fragmentContainer, fragment);

        if (!isInitial) {
            transaction.addToBackStack(null); // Cho phép nhấn back để quay lại Home
        }

        transaction.commit();
    }
}