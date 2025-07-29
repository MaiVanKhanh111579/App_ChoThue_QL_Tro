package com.example.app_chothue_ql_tro;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import Data.AppDatabase;
import Data.DAO.UserDao;
import Data.ENTITY.User;


public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomDangTin, bottomTimKiem;

    private NavHostFragment navHostDangTin, navHostTimKiem;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomDangTin = findViewById(R.id.bottom_nav_dangtin);
        bottomTimKiem = findViewById(R.id.bottom_nav_timkiem);

        navHostDangTin = NavHostFragment.create(R.navigation.nav_graph_dangtin);
        navHostTimKiem = NavHostFragment.create(R.navigation.nav_graph_timkiem);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container_main, navHostDangTin, "DANGTIN");
        transaction.add(R.id.container_main, navHostTimKiem, "TIMKIEM");
        transaction.hide(navHostTimKiem);
        transaction.commitNow();

        setupBottomNavigation();

        showTimKiemNav();

        new Thread(() -> {
            try {
                UserDao userDao = AppDatabase.getInstance(this).userDao();
                User admin = userDao.findByMaTaiKhoan("admin");
                if (admin == null) {
                    admin = new User(
                            "admin",           // maTaiKhoan
                            "Admin",           // hoVaTen
                            "",                // ngaySinh
                            "",                // CCCD
                            "",                // maSoThue
                            "0000000000",      // SDTChinh
                            "admin@example.com", // email
                            "",                // avatar
                            "",                // diaChi
                            "admin123",        // passWord
                            false,              // isLogged
                            "Admin",           // loaiUser
                            "Đang hoạt động"   // trangThai
                    );
                    userDao.insertUser(admin);
                    Log.d("MainActivity", "Created admin user with loaiUser=Admin");
                }
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Log.e("MainActivity", "Error creating/updating admin: " + e.getMessage());
                });
            }
        }).start();
    }

    private void setupBottomNavigation() {
        bottomDangTin.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (bottomDangTin.getVisibility() != View.VISIBLE) {
                showDangTinNav();
            }
            NavController navController = navHostDangTin.getNavController();
            if (id == R.id.navigation_qltindang) {
                navController.popBackStack(navController.getGraph().getStartDestinationId(), false);
            } else if (id == R.id.navigation_khachhang) {
                navController.navigate(R.id.navigation_khachhang);
            } else if (id == R.id.navigation_taikhoan_dangtin) {
                navController.navigate(R.id.navigation_taikhoan_dangtin);
            }
            return true;
        });

        bottomTimKiem.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (bottomTimKiem.getVisibility() != View.VISIBLE) {
                showTimKiemNav();
            }
            NavController navController = navHostTimKiem.getNavController();
            if (id == R.id.navigation_timkiem) {
                navController.popBackStack(navController.getGraph().getStartDestinationId(), false);
            } else if (id == R.id.navigation_luutin) {
                navController.navigate(R.id.navigation_luutin);
            } else if (id == R.id.navigation_dango) {
                navController.navigate(R.id.navigation_dango);
            } else if (id == R.id.navigation_taikhoan_timkiem) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_taikhoan_timkiem) {
                    handleAccountNavigation();
                    return false;
                }
                return NavigationUI.onNavDestinationSelected(item, navController);
            }
            return true;
        });
    }

    public void showDangTinNav() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.show(navHostDangTin);
        transaction.hide(navHostTimKiem);
        transaction.commitNow();

        bottomDangTin.setVisibility(View.VISIBLE);
        bottomTimKiem.setVisibility(View.GONE);
        bottomDangTin.setSelectedItemId(R.id.navigation_qltindang);
    }

    public void showTimKiemNav() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.show(navHostTimKiem);
        transaction.hide(navHostDangTin);
        transaction.commitNow();

        bottomDangTin.setVisibility(View.GONE);
        bottomTimKiem.setVisibility(View.VISIBLE);
        bottomTimKiem.setSelectedItemId(R.id.navigation_timkiem);
    }
    private void handleAccountNavigation() {
        navController = navHostTimKiem.getNavController();
        new Thread(() -> {
            User loggedInUser = AppDatabase.getInstance(this).userDao().findLoggedUser();
            runOnUiThread(() -> {
                if (loggedInUser != null) {
                    navController.navigate(R.id.navigation_taikhoan_timkiem);
                } else {
                    navController.navigate(R.id.navigation_dangnhap);
                    bottomTimKiem.setVisibility(View.GONE);
                }
            });
        }).start();
    }


}