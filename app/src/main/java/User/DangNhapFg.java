package User;

import static android.text.InputType.TYPE_CLASS_TEXT;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.app_chothue_ql_tro.MainActivity;
import com.example.app_chothue_ql_tro.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import Data.AppDatabase;
import Data.DAO.UserDao;
import Data.ENTITY.User;

public class DangNhapFg extends Fragment {
    private View view;

    private TextView txt_thoat, txt_canhbao_matkhau, txt_quenmatkhau, txt_dangky;
    private ImageButton imgbtn_showhide_eye;
    private EditText edt_sdt, edt_nhapmatkhau;
    private MaterialButton mbutton_dangnhap;
    private BottomNavigationView bottomNav;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_dangnhap, container,false);
        anhXaView();
        setupEyeButton();
        mbutton_dangnhap.setOnClickListener(v -> { DangNhap();});
        txt_dangky.setOnClickListener(v -> {Navigation.findNavController(v).navigate(R.id.navigation_dangky);
            bottomNav.setVisibility(View.GONE);});
        txt_thoat.setOnClickListener(v -> {Navigation.findNavController(v).navigate(R.id.navigation_timkiem);
            bottomNav.setVisibility(View.VISIBLE);});
        return view;
    }

    private void anhXaView()
    {
        bottomNav = requireActivity().findViewById(R.id.bottom_nav_timkiem);

        //TextView
        txt_thoat = view.findViewById(R.id.txt_dangnhap_thoat);
        txt_canhbao_matkhau = view.findViewById(R.id.txt_dangnhap_canhbao_matkhau);
        txt_quenmatkhau = view.findViewById(R.id.txt_dangnhap_quenmatkhau);
        txt_dangky = view.findViewById(R.id.txt_dangnhap_dangky);

        //MaterialButton
        mbutton_dangnhap = view.findViewById(R.id.mbutton_dangnhap_btn_dangnhap);

        //EditText
        edt_sdt = view.findViewById(R.id.edt_dangnhap_sdt);
        edt_nhapmatkhau = view.findViewById(R.id.edt_dangnhap_nhapmatkhau);

        //IMAGE BUTTOn
        imgbtn_showhide_eye = view.findViewById(R.id.imgbtn_dangnhap_showhide_eye);
    }

    @SuppressLint("SetTextI18n")
    private void DangNhap() {
        String sdt = edt_sdt.getText().toString().trim();
        String password = edt_nhapmatkhau.getText().toString().trim();

        // Kiểm tra dữ liệu

        if (password.isEmpty()) {
            txt_canhbao_matkhau.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            try {
                UserDao userDao = AppDatabase.getInstance(requireContext()).userDao();
                User user = userDao.findBySDT(sdt);

                // Check Email
                if (user == null) {
                    requireActivity().runOnUiThread(() -> {
                        txt_canhbao_matkhau.setVisibility(View.VISIBLE);
                        txt_canhbao_matkhau.setText("Số điện thoại sai hoặc không tồn tại");
                    });
                    return;
                } else {
                    requireActivity().runOnUiThread(() -> {
                        txt_canhbao_matkhau.setVisibility(View.GONE);
                    });
                }

                if (!user.getPassWord().equals(password)) {
                    requireActivity().runOnUiThread(() -> {
                        txt_canhbao_matkhau.setVisibility(View.VISIBLE);
                        txt_canhbao_matkhau.setText("Mật khẩu không đúng!");
                    });
                    return;
                }


                if (user.getTrangThai().equals("Đã khóa")) {
                    requireActivity().runOnUiThread(() -> {
                        Snackbar.make(view, "Tài khoản đã bị khóa!", Snackbar.LENGTH_SHORT).show();
                    });
                    return;
                }

                // Reset isLogged của tất cả user và cập nhật user hiện tại
                userDao.resetLoggedUsers();
                user.setLogged(true);
                if (user.getMaTaiKhoan().equals("admin")) {
                    user.setLoaiUser("Admin"); // Đảm bảo admin có loaiUser = "Admin"
                }
                userDao.updateUser(user);

                requireActivity().runOnUiThread(() -> {
                    Snackbar.make(requireView(), "Đăng nhập thành công!", Snackbar.LENGTH_SHORT).show();
                    String loaiUser = user.getLoaiUser();
                     //Điều hướng và cập nhật BottomNavigationView dựa trên loaiUser
                    if ("Admin".equals(loaiUser)) {
                        ((MainActivity) requireActivity()).showDangTinNav();
                    } else if ("Đăng tin".equals(loaiUser)) {
                        ((MainActivity) requireActivity()).showDangTinNav();
                    } else {
                        ((MainActivity) requireActivity()).showTimKiemNav();
                    }
                    Log.d("DangNhapFg", "Logged in, navigated for loaiUser=" + loaiUser);
                });
            } catch (Exception e) {
                Log.e("DangNhap", "Error: ", e);
                requireActivity().runOnUiThread(() -> {
                    Snackbar.make(requireView(), "Đăng nhập thất bại: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
    private void setupEyeButton() {
        if (edt_nhapmatkhau.getInputType()== TYPE_CLASS_TEXT){
            //Nếu 2 mật khẩu kiểu text thì ẩn
            edt_nhapmatkhau.setInputType(TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imgbtn_showhide_eye.setImageResource(R.drawable.ic_hide_eye);
        }
        imgbtn_showhide_eye.setOnClickListener(v -> {
            if(edt_nhapmatkhau.getInputType()== TYPE_CLASS_TEXT)
            {
                edt_nhapmatkhau.setInputType(TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                imgbtn_showhide_eye.setImageResource(R.drawable.ic_hide_eye);
            } else {
                edt_nhapmatkhau.setInputType(TYPE_CLASS_TEXT);
                imgbtn_showhide_eye.setImageResource(R.drawable.ic_show_eye);
            }
        });
    }

}

