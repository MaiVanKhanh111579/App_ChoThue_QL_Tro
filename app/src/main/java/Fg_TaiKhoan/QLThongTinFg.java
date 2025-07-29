package Fg_TaiKhoan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresExtension;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.app_chothue_ql_tro.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;

import Data.AppDatabase;
import Data.DAO.UserDao;
import Data.ENTITY.User;
import de.hdodenhof.circleimageview.CircleImageView;

public class QLThongTinFg extends Fragment {
    private EditText edt_email, edt_hoten, edt_ngaysinh, edt_diachi, edt_cccd, edt_masothue, edt_sdtchinh;
    private TextView txt_toolbar, txt_taianh;
    private ImageButton img_BackStack;
    private Button btn_luu;
    private CircleImageView img_avatar;
    private BottomNavigationView bottomNav;
    private View view;
    private Uri imageUri;
    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_taikhoan_qlthongtin, container,false);
        anhXaView();

        bottomNav.setVisibility(View.GONE);
        setUPToolbar();
        loadUsers();
        btn_luu.setOnClickListener(v -> updateUsers());
        txt_taianh.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
            imagePickerLauncher.launch(intent); // Mở Photo Picker
        });
        return view;
    }
    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    Glide.with(this).load(imageUri).into(img_avatar); // Hiển thị ảnh
                    updateUsers(); // Cập nhật avatar vào cơ sở dữ liệu
                }
            });

    private void anhXaView ()
    {

        bottomNav = requireActivity().findViewById(R.id.bottom_nav_timkiem);

        //EDITTEXT
        edt_email = view.findViewById(R.id.edt_qlthongtintk_email);
        edt_cccd = view.findViewById(R.id.edt_qlthongtintk_cccd);
        edt_hoten = view.findViewById(R.id.edt_qlthongtintk_hoten);
        edt_ngaysinh = view.findViewById(R.id.edt_qlthongtintk_ngaysinh);
        edt_diachi = view.findViewById(R.id.edt_qlthongtintk_diachi);
        edt_masothue = view.findViewById(R.id.edt_qlthongtintk_masothue);
        edt_sdtchinh = view.findViewById(R.id.edt_qlthongtintk_sdtchinh);

        //TEXT VIEW
        txt_toolbar = view.findViewById(R.id.txt_layout_toolbar_have_backstack_tittle);
        txt_taianh = view.findViewById(R.id.txt_dangtin_taikhoan_qlthongtin_button_taianh);

        //IMAGE BUTTON
        img_BackStack = view.findViewById(R.id.imgbtn_layout_toolbar_have_backstack_BackStack);

        //IMAGE VIEW
        img_avatar = view.findViewById(R.id.img_qlthongtintk_avatar);

        //BUTTON
        btn_luu = view.findViewById(R.id.btn_qlthongtintk_luu);

    }

    @SuppressLint("SetTextI18n")
    private void loadUsers() {
        new Thread(() -> {
            try {
                UserDao userDao = AppDatabase.getInstance(requireContext()).userDao();
                User loggedUser = userDao.findLoggedUser();
                if (loggedUser != null) {
                    requireActivity().runOnUiThread(() -> {
                        edt_hoten.setText(loggedUser.getHoVaTen());
                        edt_ngaysinh.setText(loggedUser.getNgaySinh());
                        edt_cccd.setText(loggedUser.getCCCD());
                        edt_masothue.setText(loggedUser.getMaSoThue());
                        edt_sdtchinh.setText(loggedUser.getSDTChinh());
                        edt_email.setText(loggedUser.getEmail());
                        edt_diachi.setText(loggedUser.getDiaChi());
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
                        Snackbar.make(view, "Không tìm thấy người dùng!", Snackbar.LENGTH_SHORT).show();
                        Navigation.findNavController(view).navigate(R.id.navigation_dangnhap);
                    });
                }
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    Snackbar.make(view, "Lỗi tải thông tin: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
    private void updateUsers() {
        new Thread(() -> {
            try {
                UserDao userDao = AppDatabase.getInstance(requireContext()).userDao();
                User loggedUser = userDao.findLoggedUser();
                if (loggedUser != null) {
                    loggedUser.setHoVaTen(edt_hoten.getText().toString().trim());
                    loggedUser.setNgaySinh(edt_ngaysinh.getText().toString().trim());
                    loggedUser.setCCCD(edt_cccd.getText().toString().trim());
                    loggedUser.setMaSoThue(edt_masothue.getText().toString().trim());
                    loggedUser.setSDTChinh(edt_sdtchinh.getText().toString().trim());
                    loggedUser.setEmail(edt_email.getText().toString().trim());
                    loggedUser.setDiaChi(edt_diachi.getText().toString().trim());
                    loggedUser.setAvatar(imageUri != null ? imageUri.toString() : ""); // Lưu đường dẫn ảnh
                    if (loggedUser.getLoaiUser() == null){
                        loggedUser.setLoaiUser("Tìm kiếm");
                    }
                    loggedUser.setTrangThai("Đang hoạt động");
                    userDao.updateUser(loggedUser);
                    requireActivity().runOnUiThread(() -> {
                        Snackbar.make(view, "Cập nhật thông tin thành công!", Snackbar.LENGTH_SHORT).show();
                        Navigation.findNavController(view).popBackStack();
                        bottomNav.setVisibility(View.VISIBLE);
                    });
                } else {
                    requireActivity().runOnUiThread(() -> {
                        Snackbar.make(view, "Không tìm thấy người dùng!", Snackbar.LENGTH_SHORT).show();
                        Navigation.findNavController(view).navigate(R.id.navigation_dangnhap);
                    });
                }
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    Snackbar.make(view, "Cập nhật thất bại: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    @SuppressLint("SetTextI18n")
    private void setUPToolbar ()
    {
        bottomNav.setVisibility(View.GONE);
        txt_toolbar.setText("Quản lý thông tin");
        img_BackStack.setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();
            bottomNav.setVisibility(View.VISIBLE);
        });
    }
}