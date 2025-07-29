package Fg_ThanhToan;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.app_chothue_ql_tro.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class KetQuaGiaoDich_DangTinFg extends Fragment {
    private View view;
    private Button btnVeTrangChu;
    private TextView txtKetQua, txtTien, txtGhiNhanTin, txtTrangThaiThanhToan, txtMaTin, txtThoiGianDang_BatDau, txtThoiGianDang_KetThuc;
    private ImageView imgKetQuaGiaoDich;
    private BottomNavigationView bottomNav;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_ketquagiaodich_dangtin, container, false);
        anhXaView();
        returnKetQua();
        return view;
    }

    @SuppressLint("CutPasteId")
    private void anhXaView() {
        txtGhiNhanTin = view.findViewById(R.id.txt_ketquagiaodich_dangtin_ghinhantin);
        txtTrangThaiThanhToan = view.findViewById(R.id.txt_ketquagiaodich_dangtin_trangthaithanhtoan);
        txtMaTin = view.findViewById(R.id.txt_ketquagiaodich_dangtin_matin);
        txtThoiGianDang_BatDau = view.findViewById(R.id.txt_ketquagiaodich_dangtin_thoigiandang_batdau);
        txtThoiGianDang_KetThuc = view.findViewById(R.id.txt_ketquagiaodich_dangtin_thoigiandang_ketthuc);
        txtTien = view.findViewById(R.id.txt_ketquagiaodich_dangtin_tien);
        txtKetQua = view.findViewById(R.id.txt_ketquagiaodich_dangtin_ketqua);
        imgKetQuaGiaoDich = view.findViewById(R.id.img_ketquagiaodich_dangtin);
        btnVeTrangChu = view.findViewById(R.id.btn_ketquagiaodich_dangtin_qltindang);
        bottomNav = requireActivity().findViewById(R.id.bottom_nav_dangtin);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void returnKetQua() {
        // Nhận dữ liệu từ Bundle
        Bundle args = getArguments();
        if (args != null) {
            int maTinDang = args.getInt("ma_TinDang", 0);
            String ngayBatDau = args.getString("ngayBatDau", "");
            String thoiGianKetThuc = args.getString("thoiGianKetThuc", "");
            String tongTien = args.getString("tongTien", "");
            boolean thanhToanThanhCong = args.getBoolean("thanhToanThanhCong", false);

            // Hiển thị thông tin
            txtMaTin.setText("MT" + String.format("%06d", maTinDang));
            txtThoiGianDang_BatDau.setText(ngayBatDau);
            txtThoiGianDang_KetThuc.setText(thoiGianKetThuc);
            txtTien.setText(tongTien);
            txtTrangThaiThanhToan.setText(thanhToanThanhCong ? "Đã thanh toán" : "Thanh toán thất bại");
            txtKetQua.setText(thanhToanThanhCong ? "Thanh toán thành công" : "Thanh toán thất bại");
            imgKetQuaGiaoDich.setImageResource(thanhToanThanhCong ? R.drawable.ic_check_green : R.drawable.ic_close_red);

            // Hiển thị thông báo ghi nhận tin
            txtGhiNhanTin.setText(thanhToanThanhCong ? "Tin của bạn đã được ghi nhận" : "Tin của bạn chưa được ghi nhận do thanh toán thất bại");
        } else {
            txtKetQua.setText("Thanh toán thất bại");
            imgKetQuaGiaoDich.setImageResource(R.drawable.ic_close_red);
            txtGhiNhanTin.setText("Thiếu thông tin giao dịch");
            txtTrangThaiThanhToan.setText("Thanh toán thất bại");
        }

        // Xử lý nút Quản lý tin đăng
        btnVeTrangChu.setOnClickListener(v -> {
            bottomNav.setVisibility(View.VISIBLE);
            Navigation.findNavController(v).navigate(R.id.navigation_qltindang);
        });

        // Hiển thị bottom navigation
        bottomNav.setVisibility(View.VISIBLE);
    }
}