package Fg_TaiKhoan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.app_chothue_ql_tro.MainActivity;
import com.example.app_chothue_ql_tro.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import Data.AppDatabase;
import Data.DAO.UserDao;
import Data.ENTITY.User;
import de.hdodenhof.circleimageview.CircleImageView;

public class TaiKhoanFg extends Fragment {
    private View view;
    private Uri imageUri;
    private LinearLayout llayout_sangdangtin,llayout_sangtimkiem;
    private AppDatabase db;
    private TextView txt_dangxuat, txt_username, txt_mataikhoan, txt_qlthongtin, txt_qlmatkhau;
    private CircleImageView img_avatar;
    private BottomNavigationView bottomNav;
    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_taikhoan, container,false);
        anhXaView();
        llayout_sangdangtin.setVisibility(View.VISIBLE);
        llayout_sangtimkiem.setVisibility(View.GONE);
        llayout_sangdangtin.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).showDangTinNav();
        });
        txt_dangxuat.setOnClickListener(v -> {logOut();});
        loadUser();
        txt_qlthongtin.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_navigation_taikhoan_timkiem_to_navigation_qlthongtin);
        bottomNav.setVisibility(View.GONE);});
        txt_qlmatkhau.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_navigation_taikhoan_timkiem_to_navigation_qlmatkhau);
            bottomNav.setVisibility(View.GONE);});

        return view;
    }

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    Glide.with(this).load(imageUri).into(img_avatar); // Hiển thị ảnh
                }
            });
    private void anhXaView() {
        bottomNav = requireActivity().findViewById(R.id.bottom_nav_timkiem);
        llayout_sangdangtin = view.findViewById(R.id.llayout_taikhoan_chuyensangdangtin);
        llayout_sangtimkiem = view.findViewById(R.id.llayout_taikhoan_chuyensangtimkiem);

        txt_dangxuat = view.findViewById(R.id.txt_timkiem_taikhoan_dangxuat);
        txt_mataikhoan = view.findViewById(R.id.txt_taikhoan_timkiem_mataikhoan);
        txt_username = view.findViewById(R.id.txt_taikhoan_timkiem_username);
        txt_qlthongtin = view.findViewById(R.id.txt_timkiem_taikhoan_thongtintaikhoan);
        txt_qlmatkhau = view.findViewById(R.id.txt_timkiem_taikhoan_matkhauvabaomat);

        img_avatar = view.findViewById(R.id.img_taikhoan_timkiem_avatar);
    }

    private void loadUser(){
        new Thread(() -> {
            UserDao userDao = AppDatabase.getInstance(requireContext()).userDao();
            User loggedUser = userDao.findLoggedUser();
            if (loggedUser != null) {
                requireActivity().runOnUiThread(() -> {
                    txt_username.setText(loggedUser.getHoVaTen());
                    txt_mataikhoan.setText(loggedUser.getMaTaiKhoan());
                    if (loggedUser.getAvatar() != null && !loggedUser.getAvatar().isEmpty()) {
                        imageUri = Uri.parse(loggedUser.getAvatar());
                        Glide.with(this)
                                .load(imageUri)
                                .error(R.drawable.ic_user)
                                .into(img_avatar);
                    }
                });
            } else {
                requireActivity().runOnUiThread(() -> {
                    Log.d("TaiKhoanFg", "No logged user found");
                    Snackbar.make(view, "Không tìm thấy người dùng đang đăng nhập!", Snackbar.LENGTH_SHORT).show();
                    Navigation.findNavController(view).navigate(R.id.navigation_dangnhap);
                });
            }
        }).start();
    }

    private void logOut(){
        new Thread(() -> {
            try {
                UserDao userDao = AppDatabase.getInstance(requireContext()).userDao();
                User loggedUser = userDao.findLoggedUser();
                if (loggedUser != null) {
                    loggedUser.setLogged(false);
                    userDao.updateUser(loggedUser);
                    requireActivity().runOnUiThread(() -> {
                        Snackbar.make(view, "Đăng xuất thành công!", Snackbar.LENGTH_SHORT).show();
                        Navigation.findNavController(view).navigate(R.id.navigation_timkiem);
                    });
                } else {
                    requireActivity().runOnUiThread(() -> {
                        Snackbar.make(view, "Không tìm thấy người dùng đang đăng nhập!", Snackbar.LENGTH_SHORT).show();
                        Navigation.findNavController(view).navigate(R.id.navigation_dangnhap);
                    });
                }
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    Snackbar.make(view, "Đăng xuất thất bại: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                });
            }
        }).start();

    }
}
