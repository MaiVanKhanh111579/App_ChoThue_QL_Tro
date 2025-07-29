package User;

import static android.text.InputType.TYPE_CLASS_TEXT;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.app_chothue_ql_tro.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.Random;

import Data.AppDatabase;
import Data.DAO.UserDao;
import Data.ENTITY.User;

public class DangKyFg extends Fragment {
    private View view;
    private EditText edt_nhapsdt, edt_nhapMatkhau, edt_nhaplaimatkhau;
    private TextView txt_dangnhap, txt_toolbar, txt_canhbaoMatKhau;
    private ImageButton imgbtn_toolbar, imgbtn_showHide_pass;
    private Button btnDanKy;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_dangky, container,false);
        anhXaView();
        toolBar();
        txt_dangnhap.setOnClickListener(v -> {Navigation.findNavController(v).popBackStack();});
        setupEyeButton();
        btnDanKy.setOnClickListener(v -> {dangKy();});

        return view;
    }

    private void anhXaView() {

        btnDanKy = view.findViewById(R.id.btn_dangky_dangky);

        edt_nhapsdt = view.findViewById(R.id.edt_dangky_thongtindangnhap_sdt);
        edt_nhapMatkhau = view.findViewById(R.id.edt_dangky_thongtindangnhap_matkhau);
        edt_nhaplaimatkhau = view.findViewById(R.id.edt_dangky_thongtindangnhap_nhaplaimatkhau);

        txt_dangnhap = view.findViewById(R.id.txt_dangky_dangnhap);
        txt_toolbar = view.findViewById(R.id.txt_layout_toolbar_have_backstack_tittle);
        txt_canhbaoMatKhau = view.findViewById(R.id.txt_thongtindangnhap_canhbao);

        imgbtn_toolbar = view.findViewById(R.id.imgbtn_layout_toolbar_have_backstack_BackStack);
        imgbtn_showHide_pass = view.findViewById(R.id.imgbtn_eye_hide_show_pass);

    }
    @SuppressLint("SetTextI18n")
    private void toolBar(){
        imgbtn_toolbar.setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();
        });
        txt_toolbar.setText("Đăng ký");
    }

    @SuppressLint("SetTextI18n")
    private void dangKy(){
        String sdt = edt_nhapsdt.getText().toString();
        String matkhau = edt_nhapMatkhau.getText().toString();
        String nhaplaimatkhau = edt_nhaplaimatkhau.getText().toString();

        if (sdt.isEmpty() || matkhau.isEmpty() || nhaplaimatkhau.isEmpty()) {
            txt_canhbaoMatKhau.setVisibility(View.VISIBLE);
            txt_canhbaoMatKhau.setText("Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        if (!matkhau.equals(nhaplaimatkhau)) {
            txt_canhbaoMatKhau.setText("Mật khẩu không khớp. Vui lòng nhập lại!");
            txt_canhbaoMatKhau.setVisibility(View.VISIBLE);
            return;
        }
        new Thread(() -> {
            try {
                UserDao userDao = AppDatabase.getInstance(requireContext()).userDao();
                if (userDao.findBySDT(sdt)!=null){
                    requireActivity().runOnUiThread(() -> {
                        Snackbar.make(requireView(),"Email đã tồn tại", BaseTransientBottomBar.LENGTH_SHORT).show();
                    });
                    return;
                }
                String maTaiKhoan;
                String hovaTen = "";
                String ngaySinh = "";
                String cCCD = "";
                String diaChi = "";
                String maSoThue = "";
                String email = "";
                do {
                    maTaiKhoan = generateMaTaiKhoan();
                } while (userDao.findByMaTaiKhoan(maTaiKhoan) != null); // Kiểm tra trùng

                User user = new User( maTaiKhoan,  hovaTen,  ngaySinh,  cCCD,  maSoThue,  sdt,  email,  "",  diaChi,  matkhau,
                        false,"Tìm kiếm", "Đang hoạt động");
                userDao.insertUser(user);
                requireActivity().runOnUiThread(() -> {
                    Snackbar.make(requireView(),"Đăng ký thành công ", BaseTransientBottomBar.LENGTH_SHORT).show();
                    Navigation.findNavController(view).navigate(R.id.navigation_dangnhap);
                });
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> {
                    Snackbar.make(requireView(),"Đăng ký thất bại", BaseTransientBottomBar.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
    private void setupEyeButton() {
        if (edt_nhapMatkhau.getInputType()== TYPE_CLASS_TEXT & edt_nhaplaimatkhau.getInputType()== TYPE_CLASS_TEXT){
            //Nếu 2 mật khẩu kiểu text thì ẩn
            edt_nhapMatkhau.setInputType(TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            edt_nhaplaimatkhau.setInputType(TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imgbtn_showHide_pass.setImageResource(R.drawable.ic_hide_eye);
        }
        imgbtn_showHide_pass.setOnClickListener(v -> {
            if(edt_nhapMatkhau.getInputType()== TYPE_CLASS_TEXT & edt_nhaplaimatkhau.getInputType()== TYPE_CLASS_TEXT)
            {
                edt_nhapMatkhau.setInputType(TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                edt_nhaplaimatkhau.setInputType(TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                imgbtn_showHide_pass.setImageResource(R.drawable.ic_hide_eye);
            } else {
                edt_nhapMatkhau.setInputType(TYPE_CLASS_TEXT);
                edt_nhaplaimatkhau.setInputType(TYPE_CLASS_TEXT);
                imgbtn_showHide_pass.setImageResource(R.drawable.ic_show_eye);
            }
        });
    }
    private String generateMaTaiKhoan() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            sb.append(random.nextInt(10)); // Thêm số ngẫu nhiên từ 0-9
        }
        return "TK" + sb.toString(); // Ví dụ: TK483920174
    }
}

