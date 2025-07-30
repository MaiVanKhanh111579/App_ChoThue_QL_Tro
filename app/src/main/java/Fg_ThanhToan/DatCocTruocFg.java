package Fg_ThanhToan;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import Data.AppDatabase;
import Data.ENTITY.DatCocTruoc;

public class DatCocTruocFg extends Fragment {
    private View view;
    private ImageButton imgtbtnBackStack;
    private Button btnQuayLai, btnTiepTuc;
    private TextView txtToolbar, txtDiaChi, txtTienThue, txtTienCoc, txtNgayDatCoc, txtHoTen, txtSDT;
    private EditText edtNgayLienHe;
    private BottomNavigationView bottomNav_DangTin, bottomNav_TimKiem;
    private AppDatabase db;
    private String diaChi, tienThue, tienCoc, hoTen, sdt;
    private int maTinDang;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_datcoctruoc, container,false);
        anhXaView();
        setToolbar();
        getAndDisplayData();
        btnQuayLai.setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();
        });
        btnTiepTuc.setOnClickListener(v -> {
            saveDatCoc();
        });


        return view;
    }

    private void anhXaView() {
        imgtbtnBackStack = view.findViewById(R.id.imgbtn_layout_toolbar_have_backstack_BackStack);

        btnQuayLai = view.findViewById(R.id.btn_datcoctruoc_quaylai);
        btnTiepTuc = view.findViewById(R.id.btn_datcoctruoc_tieptuc);

        txtToolbar = view.findViewById(R.id.txt_layout_toolbar_have_backstack_tittle);
        txtDiaChi = view.findViewById(R.id.txt_datcoctruoc_diachi);
        txtTienThue = view.findViewById(R.id.txt_datcoctruoc_tienthue);
        txtTienCoc = view.findViewById(R.id.txt_datcoctruoc_sotiendatcoc);
        txtNgayDatCoc = view.findViewById(R.id.txt_datcoctruoc_ngaydatcoc);
        txtHoTen = view.findViewById(R.id.txt_datcoctruoc_hovaten);
        txtSDT = view.findViewById(R.id.txt_datcoctruoc_sdt);

        edtNgayLienHe = view.findViewById(R.id.edt_datcoctruoc_ngaylienhe);
        bottomNav_TimKiem = requireActivity().findViewById(R.id.bottom_nav_timkiem);
        db = AppDatabase.getInstance(getContext());
    }
    private void getAndDisplayData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            maTinDang = bundle.getInt("ma_TinDang");
            diaChi = bundle.getString("diaChi");
            tienThue = bundle.getString("tienThue");
            tienCoc = bundle.getString("tienCoc");
            hoTen = bundle.getString("hoTen");
            sdt = bundle.getString("sdt");

            txtDiaChi.setText(diaChi);
            txtTienThue.setText(tienThue);
            txtTienCoc.setText(tienCoc);
            txtHoTen.setText(hoTen);
            txtSDT.setText(sdt);
        }

        // Lấy và hiển thị ngày đặt cọc hiện tại
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        txtNgayDatCoc.setText(currentDate);
    }

    private void saveDatCoc() {
        String ngayLienHe = edtNgayLienHe.getText().toString().trim();
        if (ngayLienHe.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập ngày bạn muốn liên hệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lưu vào database trên một thread khác
        new Thread(() -> {
            // Tạo đối tượng DatCocTruoc
            DatCocTruoc datCoc = new DatCocTruoc();
            String newId;
            do {
                newId = generateMaDatCoc();
            } while (db.datCocTruocDao().findById(newId) != null);
            datCoc.setMaDatCocTruoc(newId);

            datCoc.setMaDatCocTruoc(generateMaDatCoc());
            datCoc.setMaTinDang(String.valueOf(maTinDang));
            // Lấy mã khách hàng thực tế sau khi có chức năng đăng nhập
            datCoc.setMaKhachHang("KH001");
            datCoc.setTienDatCoc(tienCoc);
            datCoc.setNgayDatCoc(txtNgayDatCoc.getText().toString());
            datCoc.setNgayLienHe(ngayLienHe);
            datCoc.setTrangThai("Chưa xem nhà"); // Trạng thái mặc định
            db.datCocTruocDao().insert(datCoc);
            // Sau khi lưu, quay lại Main Thread để điều hướng và thông báo
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Tạo yêu cầu đặt cọc thành công!", Toast.LENGTH_SHORT).show();
                    // Điều hướng tới màn hình phương thức thanh toán
                    Navigation.findNavController(view).navigate(R.id.action_navigation_datcoctruoc_to_navigation_phuongthucthanhtoan);
                });
            }
        }).start();
    }
    @SuppressLint("SetTextI18n")
    private void setToolbar(){
        imgtbtnBackStack.setVisibility(View.GONE);
        txtToolbar.setText("Đặt cọc trước");
    }
    private String generateMaDatCoc() {
        Random random = new Random();
        int number = 100000 + random.nextInt(900000);
        return "DC" + number;
    }
}
