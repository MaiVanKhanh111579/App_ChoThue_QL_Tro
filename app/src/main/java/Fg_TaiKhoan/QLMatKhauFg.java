package Fg_TaiKhoan;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.app_chothue_ql_tro.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import Data.AppDatabase;
import Data.DAO.UserDao;
import Data.ENTITY.User;

public class QLMatKhauFg extends Fragment {
    private ImageButton img_BackStack;
    private TextView txt_toolbar, txt_canhbao_matkhauHienTai, txt_canhbao_matkhauMoi;
    private EditText edt_matkhau_HienTai, edt_matkhau_Moi, edt_matkhau_NhapLai, edt_matkhau_KhoaTaiKhoan;
    private MaterialCheckBox checkBox;
    private Button btn_luu, btn_khoataikhoan, btn_xoataikhoan;
    private View view;
    private BottomNavigationView bottomNav;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_taikhoan_qlmatkhau, container,false);
        anhXaView();
        bottomNav.setVisibility(View.GONE);
        setUPToolbar();
        btn_luu.setOnClickListener(v -> {updateUser();});


        return view;
    }

    private void anhXaView ()
    {
        bottomNav = requireActivity().findViewById(R.id.bottom_nav_timkiem);

        //Text View
        txt_toolbar = view.findViewById(R.id.txt_layout_toolbar_have_backstack_tittle);
        txt_canhbao_matkhauHienTai = view.findViewById(R.id.txt_qltaikhoan_canhbao_matkhauHienTai);
        txt_canhbao_matkhauMoi = view.findViewById(R.id.txt_qltaikhoan_canhbao_matkhauMoi);

        //Image Button
        img_BackStack = view.findViewById(R.id.imgbtn_layout_toolbar_have_backstack_BackStack);

        //Edit Text
        edt_matkhau_HienTai = view.findViewById(R.id.edt_taikhoan_baomat_matkhauhientai);
        edt_matkhau_Moi = view.findViewById(R.id.edt_taikhoan_baomat_matkhaumoi);
        edt_matkhau_NhapLai =view.findViewById(R.id.edt_taikhoan_baomat_nhaplaimatkhau);
        edt_matkhau_KhoaTaiKhoan = view.findViewById(R.id.edt_qlthongtintk_khoaTaiKhoan);

        //Button
        btn_khoataikhoan = view.findViewById(R.id.btn_taikhoan_baomat_khoataikhoan);
        btn_xoataikhoan = view.findViewById(R.id.btn_taikhoan_baomat_xoataikhoan);
        btn_luu = view.findViewById(R.id.btn_taikhoan_baomat_luu);

        //Check Box
        checkBox = view.findViewById(R.id.checkbox_taikhoan_baomat_xoataikhoan);
    }

    @SuppressLint("SetTextI18n")
    private void updateUser()
    {
        String edt_hienTai = edt_matkhau_HienTai.getText().toString().trim();
        String edt_Moi = edt_matkhau_Moi.getText().toString().trim();
        String edt_nhapLai = edt_matkhau_NhapLai.getText().toString().trim();

        if (edt_hienTai.isEmpty() || edt_Moi.isEmpty() || edt_nhapLai.isEmpty()) {
            txt_canhbao_matkhauMoi.setText("Vui lòng điền đầy đủ!");
            txt_canhbao_matkhauMoi.setVisibility(View.VISIBLE);
        } else {
            txt_canhbao_matkhauMoi.setText("");
            txt_canhbao_matkhauMoi.setVisibility(View.GONE);
        }

        if (!edt_Moi.equals(edt_nhapLai))
        {
            txt_canhbao_matkhauMoi.setText("Mật khẩu không khớp");
            txt_canhbao_matkhauMoi.setVisibility(View.VISIBLE);
        }
        new Thread(() -> {
            try {
                UserDao userDao = AppDatabase.getInstance(requireContext()).userDao();
                User loggedUser = userDao.findLoggedUser();
                if (!loggedUser.getPassWord().equals(edt_hienTai)) {
                    requireActivity().runOnUiThread(() -> {
                        txt_canhbao_matkhauHienTai.setVisibility(View.VISIBLE);
                        txt_canhbao_matkhauHienTai.setText("Mật khẩu không đúng!");
                    });
                    return;
                }
                if (loggedUser != null) {
                    loggedUser.setPassWord(edt_matkhau_Moi.getText().toString().trim());
                    userDao.updateUser(loggedUser);
                    requireActivity().runOnUiThread(() -> {
                        edt_matkhau_HienTai.setText("");
                        edt_matkhau_NhapLai.setText("");
                        edt_matkhau_Moi.setText("");
                        Snackbar.make(view, "Cập nhật mật khẩu thành công!", Snackbar.LENGTH_SHORT).show();
                    });

                }
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    Snackbar.make(requireView(), "Cập nhật thất bại", BaseTransientBottomBar.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
    @SuppressLint("SetTextI18n")
    private void setUPToolbar ()
    {
        txt_toolbar.setText("Quản lý mật khẩu");
        img_BackStack.setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();
            bottomNav.setVisibility(View.VISIBLE);
        });
    }
}