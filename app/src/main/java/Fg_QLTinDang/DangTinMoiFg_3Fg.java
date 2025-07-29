package Fg_QLTinDang;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_chothue_ql_tro.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import Data.AppDatabase;
import Data.DAO.DanhSachAnhDao;
import Data.DAO.ThongTinNhaTroDao;
import Data.DAO.TinDangDao;
import Data.ENTITY.DanhSachAnh;
import Data.ENTITY.ThanhToan;
import Data.ENTITY.ThongTinNhaTro;
import Data.ENTITY.TinDang;

public class DangTinMoiFg_3Fg extends Fragment {
    private RecyclerView recyclerView;
    private boolean isKimCuong = false;
    private boolean isVang = false;
    private boolean isBac = false;
    private boolean isChonNgay = true;
    private boolean isChonTuan = false;
    private boolean isThuong = false;
    private int maTinDang;
    private TextView txtTongTien, txtThanhToanLoaiTin, txtThanhToanDonGia, txtThanhToanSoNgay, txtThanhToanThoiGianKetThuc,
    txt_dangtinmoi_3_thoat;
    private Button btn_dangtinmoi_3_thanhtoan;
    private AppDatabase db;
    private View view;
    private EditText edtNgayBatDau;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Nullable
    @Override
    @SuppressLint({"MissingInflatedId", "LocalSuppress", "ResourceAsColor", "SetTextI18n"})
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_dangtinmoi_3, container, false);
        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_nav_dangtin);
        bottomNav.setVisibility(View.GONE);

        // Ánh xạ các view
        ScrollView scrollview_dangtinmoi_3 = view.findViewById(R.id.scrollview_dangtinmoi_3);
        LinearLayout llayout_dangtinmoi_3_thanhtoan = view.findViewById(R.id.llayout_dangtinmoi_3_thanhtoan);

        LinearLayout llayout_dangtinmoi_3_vip_kimcuong = view.findViewById(R.id.llayout_dangtinmoi_3_vip_kimcuong);
        LinearLayout llayout_dangtinmoi_3_vip_vang = view.findViewById(R.id.llayout_dangtinmoi_3_vip_vang);
        LinearLayout llayout_dangtinmoi_3_vip_bac = view.findViewById(R.id.llayout_dangtinmoi_3_vip_bac);
        LinearLayout llayout_dangtinmoi_3_thuong = view.findViewById(R.id.llayout_dangtinmoi_3_thuong);

        LinearLayout llayout_dangtinmoi_3_vip_kimcuong_show_bangia = view.findViewById(R.id.llayout_dangtinmoi_3_vip_kimcuong_show_bangia);
        LinearLayout llayout_dangtinmoi_3_vip_vang_show_bangia = view.findViewById(R.id.llayout_dangtinmoi_3_vip_vang_show_bangia);
        LinearLayout llayout_dangtinmoi_3_vip_bac_show_bangia = view.findViewById(R.id.llayout_dangtinmoi_3_vip_bac_show_bangia);
        llayout_dangtinmoi_3_vip_kimcuong_show_bangia.setVisibility(View.GONE);
        llayout_dangtinmoi_3_vip_vang_show_bangia.setVisibility(View.GONE);
        llayout_dangtinmoi_3_vip_bac_show_bangia.setVisibility(View.GONE);

        TextView txt_dangtinmoi_3_tratheo_ngay = view.findViewById(R.id.txt_dangtinmoi_3_tratheo_ngay);
        TextView txt_dangtinmoi_3_tratheo_tuan = view.findViewById(R.id.txt_dangtinmoi_3_tratheo_tuan);
        TextView txt_dangtinmoi_3_thanhtoan_phidangtin = view.findViewById(R.id.txt_dangtinmoi_3_thanhtoan_phidangtin);
        TextView txt_dangtinmoi_3_thanhtoan_khuyenmai = view.findViewById(R.id.txt_dangtinmoi_3_thanhtoan_khuyenmai);
        TextView txt_dangtinmoi_3_thanhtoan_tongtien = view.findViewById(R.id.txt_dangtinmoi_3_thanhtoan_tongtien);

        btn_dangtinmoi_3_thanhtoan = view.findViewById(R.id.btn_dangtinmoi_3_thanhtoan);
        Button btn_dangtinmoi_3_quaylai = view.findViewById(R.id.btn_dangtinmoi_3_quaylai);
        Button btn_dangtinmoi_3_tieptuc = view.findViewById(R.id.btn_dangtinmoi_3_tieptuc);

        txtTongTien = view.findViewById(R.id.txt_dangtinmoi_3_tongtien);
        txtThanhToanLoaiTin = view.findViewById(R.id.txt_dangtinmoi_3_thanhtoan_loaitin);
        txtThanhToanDonGia = view.findViewById(R.id.txt_dangtinmoi_3_thanhtoan_dongia);
        txtThanhToanSoNgay = view.findViewById(R.id.txt_dangtinmoi_3_thanhtoan_songaydang);
        txtThanhToanThoiGianKetThuc = view.findViewById(R.id.txt_dangtinmoi_3_thanhtoan_thoigianketthuc);
        edtNgayBatDau = view.findViewById(R.id.edt_dangtinmoi_3_ngaybatdau);

        // VIP KIM CUONG
        RadioButton radiobtn_dangtinmoi_3_vip_kimcuong_5_ngay = view.findViewById(R.id.radiobtn_dangtinmoi_3_vip_kimcuong_5_ngay);
        RadioButton radiobtn_dangtinmoi_3_vip_kimcuong_10_ngay = view.findViewById(R.id.radiobtn_dangtinmoi_3_vip_kimcuong_10_ngay);
        RadioButton radiobtn_dangtinmoi_3_vip_kimcuong_15_ngay = view.findViewById(R.id.radiobtn_dangtinmoi_3_vip_kimcuong_15_ngay);
        // VIP VANG
        RadioButton radiobtn_dangtinmoi_3_vip_vang_5_ngay = view.findViewById(R.id.radiobtn_dangtinmoi_3_vip_vang_5_ngay);
        RadioButton radiobtn_dangtinmoi_3_vip_vang_10_ngay = view.findViewById(R.id.radiobtn_dangtinmoi_3_vip_vang_10_ngay);
        RadioButton radiobtn_dangtinmoi_3_vip_vang_15_ngay = view.findViewById(R.id.radiobtn_dangtinmoi_3_vip_vang_15_ngay);
        // VIP BAC
        RadioButton radiobtn_dangtinmoi_3_vip_bac_5_ngay = view.findViewById(R.id.radiobtn_dangtinmoi_3_vip_bac_5_ngay);
        RadioButton radiobtn_dangtinmoi_3_vip_bac_10_ngay = view.findViewById(R.id.radiobtn_dangtinmoi_3_vip_bac_10_ngay);
        RadioButton radiobtn_dangtinmoi_3_vip_bac_15_ngay = view.findViewById(R.id.radiobtn_dangtinmoi_3_vip_bac_15_ngay);

        // Hàm tính tổng tiền và ngày kết thúc
        @SuppressLint("SetTextI18n") View.OnClickListener radioClickListener = v -> {
            RadioButton selectedRadio = (RadioButton) v;
            String xepHangTin = "";
            int soNgay = 0;
            double donGia = 0;

            // Xóa trạng thái checked của tất cả radio button
            radiobtn_dangtinmoi_3_vip_kimcuong_5_ngay.setChecked(false);
            radiobtn_dangtinmoi_3_vip_kimcuong_10_ngay.setChecked(false);
            radiobtn_dangtinmoi_3_vip_kimcuong_15_ngay.setChecked(false);
            radiobtn_dangtinmoi_3_vip_vang_5_ngay.setChecked(false);
            radiobtn_dangtinmoi_3_vip_vang_10_ngay.setChecked(false);
            radiobtn_dangtinmoi_3_vip_vang_15_ngay.setChecked(false);
            radiobtn_dangtinmoi_3_vip_bac_5_ngay.setChecked(false);
            radiobtn_dangtinmoi_3_vip_bac_10_ngay.setChecked(false);
            radiobtn_dangtinmoi_3_vip_bac_15_ngay.setChecked(false);
            selectedRadio.setChecked(true);

            // Xác định loại tin, số ngày và đơn giá
            if (selectedRadio == radiobtn_dangtinmoi_3_vip_kimcuong_5_ngay) {
                xepHangTin = "VIP Kim Cương";
                soNgay = 5;
                donGia = 50000;
            } else if (selectedRadio == radiobtn_dangtinmoi_3_vip_kimcuong_10_ngay) {
                xepHangTin = "VIP Kim Cương";
                soNgay = 10;
                donGia = 45000;
            } else if (selectedRadio == radiobtn_dangtinmoi_3_vip_kimcuong_15_ngay) {
                xepHangTin = "VIP Kim Cương";
                soNgay = 15;
                donGia = 40000;
            } else if (selectedRadio == radiobtn_dangtinmoi_3_vip_vang_5_ngay) {
                xepHangTin = "VIP Vàng";
                soNgay = 5;
                donGia = 40000;
            } else if (selectedRadio == radiobtn_dangtinmoi_3_vip_vang_10_ngay) {
                xepHangTin = "VIP Vàng";
                soNgay = 10;
                donGia = 35000;
            } else if (selectedRadio == radiobtn_dangtinmoi_3_vip_vang_15_ngay) {
                xepHangTin = "VIP Vàng";
                soNgay = 15;
                donGia = 30000;
            } else if (selectedRadio == radiobtn_dangtinmoi_3_vip_bac_5_ngay) {
                xepHangTin = "VIP Bạc";
                soNgay = 5;
                donGia = 20000;
            } else if (selectedRadio == radiobtn_dangtinmoi_3_vip_bac_10_ngay) {
                xepHangTin = "VIP Bạc";
                soNgay = 10;
                donGia = 15000;
            } else if (selectedRadio == radiobtn_dangtinmoi_3_vip_bac_15_ngay) {
                xepHangTin = "VIP Bạc";
                soNgay = 15;
                donGia = 10000;
            }

            // Tính tổng tiền
            double tongTien = donGia * soNgay;
            txtTongTien.setText(String.format(Locale.getDefault(), "%,.0f đ", tongTien));
            txt_dangtinmoi_3_thanhtoan_phidangtin.setText(String.format(Locale.getDefault(), "%,.0f đ", tongTien));
            txt_dangtinmoi_3_thanhtoan_tongtien.setText(String.format   (Locale.getDefault(), "%,.0f đ", tongTien));
            // Tính ngày kết thúc
            String ngayBatDauStr = edtNgayBatDau.getText().toString();
            if (!ngayBatDauStr.isEmpty()) {
                try {
                    Date ngayBatDau = dateFormat.parse(ngayBatDauStr);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(ngayBatDau);
                    calendar.add(Calendar.DAY_OF_MONTH, soNgay);
                    String ngayKetThuc = dateFormat.format(calendar.getTime());

                    // Cập nhật thông tin thanh toán
                    txtThanhToanLoaiTin.setText(xepHangTin);
                    txtThanhToanDonGia.setText(String.format(Locale.getDefault(), "%,.0f đ/ngày", donGia));
                    txtThanhToanSoNgay.setText(soNgay + " ngày");
                    txtThanhToanThoiGianKetThuc.setText(ngayKetThuc);
                } catch (ParseException e) {
                    Toast.makeText(requireContext(), "Ngày bắt đầu không hợp lệ", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "Vui lòng nhập ngày bắt đầu", Toast.LENGTH_SHORT).show();
            }
        };



        // Gắn sự kiện cho các radio button
        radiobtn_dangtinmoi_3_vip_kimcuong_5_ngay.setOnClickListener(radioClickListener);
        radiobtn_dangtinmoi_3_vip_kimcuong_10_ngay.setOnClickListener(radioClickListener);
        radiobtn_dangtinmoi_3_vip_kimcuong_15_ngay.setOnClickListener(radioClickListener);
        radiobtn_dangtinmoi_3_vip_vang_5_ngay.setOnClickListener(radioClickListener);
        radiobtn_dangtinmoi_3_vip_vang_10_ngay.setOnClickListener(radioClickListener);
        radiobtn_dangtinmoi_3_vip_vang_15_ngay.setOnClickListener(radioClickListener);
        radiobtn_dangtinmoi_3_vip_bac_5_ngay.setOnClickListener(radioClickListener);
        radiobtn_dangtinmoi_3_vip_bac_10_ngay.setOnClickListener(radioClickListener);
        radiobtn_dangtinmoi_3_vip_bac_15_ngay.setOnClickListener(radioClickListener);

        // Xử lý khi chọn "Tin thường"
        llayout_dangtinmoi_3_thuong.setOnClickListener(v -> {
            isThuong = true;
            llayout_dangtinmoi_3_thuong.setBackgroundResource(R.drawable.customer_button_redborder_radius_10dp);
            llayout_dangtinmoi_3_vip_vang.setBackgroundResource(R.drawable.customer_edittext_greyborder_radius_5dp);
            llayout_dangtinmoi_3_vip_bac.setBackgroundResource(R.drawable.customer_edittext_greyborder_radius_5dp);
            llayout_dangtinmoi_3_vip_kimcuong.setBackgroundResource(R.drawable.customer_edittext_greyborder_radius_5dp);

            llayout_dangtinmoi_3_vip_kimcuong_show_bangia.setVisibility(View.GONE);
            llayout_dangtinmoi_3_vip_vang_show_bangia.setVisibility(View.GONE);
            llayout_dangtinmoi_3_vip_bac_show_bangia.setVisibility(View.GONE);

            isKimCuong = false;
            isVang = false;
            isBac = false;

            // Cập nhật tổng tiền và thông tin thanh toán cho "Tin thường"
            double donGia = 2500;
            int soNgay = 1; // Mặc định 1 ngày cho "Tin thường"
            double tongTien = donGia * soNgay;
            txtTongTien.setText(String.format(Locale.getDefault(), "%,.0f đ", tongTien));
            txt_dangtinmoi_3_thanhtoan_phidangtin.setText(String.format(Locale.getDefault(), "%,.0f đ", tongTien));
            txt_dangtinmoi_3_thanhtoan_tongtien.setText(String.format   (Locale.getDefault(), "%,.0f đ", tongTien));
            String ngayBatDauStr = edtNgayBatDau.getText().toString();
            if (!ngayBatDauStr.isEmpty()) {
                try {
                    Date ngayBatDau = dateFormat.parse(ngayBatDauStr);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(ngayBatDau);
                    calendar.add(Calendar.DAY_OF_MONTH, soNgay);
                    String ngayKetThuc = dateFormat.format(calendar.getTime());

                    txtThanhToanLoaiTin.setText("Tin thường");
                    txtThanhToanDonGia.setText(String.format(Locale.getDefault(), "%,.0f đ/ngày", donGia));
                    txtThanhToanSoNgay.setText(soNgay + " ngày");
                    txtThanhToanThoiGianKetThuc.setText(ngayKetThuc);
                } catch (ParseException e) {
                    Toast.makeText(requireContext(), "Ngày bắt đầu không hợp lệ", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "Vui lòng nhập ngày bắt đầu", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện chọn trả theo ngày/tuần
        txt_dangtinmoi_3_tratheo_ngay.setOnClickListener(v -> {
            txt_dangtinmoi_3_tratheo_ngay.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.black));
            txt_dangtinmoi_3_tratheo_ngay.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            txt_dangtinmoi_3_tratheo_tuan.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.grey_background));
            txt_dangtinmoi_3_tratheo_tuan.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_text));

            isChonNgay = true;
            isChonTuan = false;
        });

        txt_dangtinmoi_3_tratheo_tuan.setOnClickListener(v -> {
            txt_dangtinmoi_3_tratheo_tuan.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.black));
            txt_dangtinmoi_3_tratheo_tuan.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            txt_dangtinmoi_3_tratheo_ngay.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.grey_background));
            txt_dangtinmoi_3_tratheo_ngay.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_text));

            isChonTuan = true;
            isChonNgay = false;
        });

        // Xử lý sự kiện chọn loại tin
        llayout_dangtinmoi_3_vip_kimcuong.setOnClickListener(v -> {
            isKimCuong = true;
            llayout_dangtinmoi_3_vip_kimcuong.setBackgroundResource(R.drawable.customer_button_redborder_radius_10dp);
            llayout_dangtinmoi_3_vip_vang.setBackgroundResource(R.drawable.customer_edittext_greyborder_radius_5dp);
            llayout_dangtinmoi_3_vip_bac.setBackgroundResource(R.drawable.customer_edittext_greyborder_radius_5dp);
            llayout_dangtinmoi_3_thuong.setBackgroundResource(R.drawable.customer_edittext_greyborder_radius_5dp);

            llayout_dangtinmoi_3_vip_kimcuong_show_bangia.setVisibility(View.VISIBLE);
            llayout_dangtinmoi_3_vip_vang_show_bangia.setVisibility(View.GONE);
            llayout_dangtinmoi_3_vip_bac_show_bangia.setVisibility(View.GONE);

            isVang = false;
            isBac = false;
            isThuong = false;
        });

        llayout_dangtinmoi_3_vip_vang.setOnClickListener(v -> {
            isVang = true;
            llayout_dangtinmoi_3_vip_vang.setBackgroundResource(R.drawable.customer_button_redborder_radius_10dp);
            llayout_dangtinmoi_3_vip_kimcuong.setBackgroundResource(R.drawable.customer_edittext_greyborder_radius_5dp);
            llayout_dangtinmoi_3_vip_bac.setBackgroundResource(R.drawable.customer_edittext_greyborder_radius_5dp);
            llayout_dangtinmoi_3_thuong.setBackgroundResource(R.drawable.customer_edittext_greyborder_radius_5dp);

            llayout_dangtinmoi_3_vip_vang_show_bangia.setVisibility(View.VISIBLE);
            llayout_dangtinmoi_3_vip_kimcuong_show_bangia.setVisibility(View.GONE);
            llayout_dangtinmoi_3_vip_bac_show_bangia.setVisibility(View.GONE);

            isKimCuong = false;
            isBac = false;
            isThuong = false;
        });

        llayout_dangtinmoi_3_vip_bac.setOnClickListener(v -> {
            isBac = true;
            llayout_dangtinmoi_3_vip_bac.setBackgroundResource(R.drawable.customer_button_redborder_radius_10dp);
            llayout_dangtinmoi_3_vip_vang.setBackgroundResource(R.drawable.customer_edittext_greyborder_radius_5dp);
            llayout_dangtinmoi_3_vip_kimcuong.setBackgroundResource(R.drawable.customer_edittext_greyborder_radius_5dp);
            llayout_dangtinmoi_3_thuong.setBackgroundResource(R.drawable.customer_edittext_greyborder_radius_5dp);

            llayout_dangtinmoi_3_vip_bac_show_bangia.setVisibility(View.VISIBLE);
            llayout_dangtinmoi_3_vip_vang_show_bangia.setVisibility(View.GONE);
            llayout_dangtinmoi_3_vip_kimcuong_show_bangia.setVisibility(View.GONE);

            isVang = false;
            isKimCuong = false;
            isThuong = false;
        });

        // Thoát
        txt_dangtinmoi_3_thoat = view.findViewById(R.id.txt_dangtinmoi_3_thoat);
        txt_dangtinmoi_3_thoat.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.navigation_qltindang);
            bottomNav.setVisibility(View.VISIBLE);
        });

        // Xử lý nút Tiếp tục
        btn_dangtinmoi_3_tieptuc.setOnClickListener(v -> {
            if (txtThanhToanLoaiTin.getText().toString().isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng chọn loại tin", Toast.LENGTH_SHORT).show();
                return;
            }
            scrollview_dangtinmoi_3.setVisibility(View.GONE);
            llayout_dangtinmoi_3_thanhtoan.setVisibility(View.VISIBLE);
            btn_dangtinmoi_3_tieptuc.setVisibility(View.GONE);
            btn_dangtinmoi_3_thanhtoan.setVisibility(View.VISIBLE);
        });

        // Xử lý nút Quay lại
        btn_dangtinmoi_3_quaylai.setOnClickListener(v -> {
            if (llayout_dangtinmoi_3_thanhtoan.getVisibility() == View.VISIBLE) {
                llayout_dangtinmoi_3_thanhtoan.setVisibility(View.GONE);
                btn_dangtinmoi_3_thanhtoan.setVisibility(View.GONE);
                btn_dangtinmoi_3_tieptuc.setVisibility(View.VISIBLE);
                scrollview_dangtinmoi_3.setVisibility(View.VISIBLE);
            } else {
                Navigation.findNavController(v).popBackStack();
            }
        });

        // Xử lý nút Thanh toán
        btn_dangtinmoi_3_thanhtoan.setOnClickListener(v -> {
            // Lưu thông tin thanh toán vào cơ sở dữ liệu
            String loaiTin = txtThanhToanLoaiTin.getText().toString();
            String donGia = txtThanhToanDonGia.getText().toString();
            String soNgay = txtThanhToanSoNgay.getText().toString();
            String thoiGianKetThuc = txtThanhToanThoiGianKetThuc.getText().toString();
            String tongTien = txtTongTien.getText().toString();

            if (loaiTin.isEmpty() || donGia.isEmpty() || soNgay.isEmpty() || thoiGianKetThuc.isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng chọn đầy đủ thông tin thanh toán", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                try {
                    ThanhToan thanhToan = new ThanhToan(0, loaiTin, maTinDang, "Thành công", thoiGianKetThuc, tongTien);
                    AppDatabase.getInstance(requireContext()).thanhToanDao().insertThanhToan(thanhToan);
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                        NavController navController = Navigation.findNavController(v);
                        Bundle bundle = new Bundle();
                        bundle.putInt("ma_TinDang", maTinDang);
                        bundle.putString("loaiTin", loaiTin);
                        bundle.putString("donGia", donGia);
                        bundle.putString("soNgay", soNgay);
                        bundle.putString("thoiGianKetThuc", thoiGianKetThuc);
                        bundle.putString("tongTien", tongTien);
                        navController.navigate(R.id.action_navigation_dangtinmoi_3_to_navigation_phuongthucthanhtoan_dangtin, bundle);
                    });
                } catch (Exception e) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(requireContext(), "Lỗi khi lưu thanh toán: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            }).start();
        });
        if (getArguments() != null) {
            maTinDang = getArguments().getInt("ma_TinDang");
        }
        // Kiểm tra ma_TinDang
        if (maTinDang == 0) {
            Toast.makeText(requireContext(), "Không tìm thấy mã tin đăng", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).popBackStack();
            return view;
        }
        edtNgayBatDau.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (!input.isEmpty()) {
                    try {
                        dateFormat.parse(input);
                    } catch (ParseException e) {
                        edtNgayBatDau.setError("Ngày không hợp lệ, vui lòng nhập theo định dạng 01/01/2025");
                    }
                }
            }
        });

        return view;
    }
    private void handleThanhToan() {
        btn_dangtinmoi_3_thanhtoan.setOnClickListener(v -> {
            Bundle args = getArguments();
            if (args == null) {
                Toast.makeText(requireContext(), "Thiếu thông tin tin đăng", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lấy dữ liệu từ Bundle
            String tieuDe = args.getString("tieuDe", "");
            String moTa = args.getString("moTa", "");
            String diaChiHienThi = args.getString("diaChiHienThi", "");
            String dienTich = args.getString("dienTich", "");
            String giaThue = args.getString("giaThue", "");
            String soNguoiO = args.getString("soNguoiO", "");
            String ngayDang = args.getString("ngayDang", "");
            String diaChi = args.getString("diaChi", "");
            String soPhong = args.getString("soPhong", "");
            String giaDien = args.getString("giaDien", "");
            String giaNuoc = args.getString("giaNuoc", "");
            String giaInternet = args.getString("giaInternet", "");
            String loaiTro = args.getString("loaiTro", "");
            String noiThat = args.getString("noiThat", "");
            String loaiPhong = args.getString("loaiPhong", "");
            String id_tien_ich = args.getString("id_tien_ich", "");
            String hoVaTen = args.getString("hoVaTen", "");
            String sdt = args.getString("sdt", "");
            String email = args.getString("email", "");
            String avatar = args.getString("avatar", "");
            ArrayList<String> imageUrls = args.getStringArrayList("imageUrls");
            String loaiTin = txtThanhToanLoaiTin.getText().toString();
            String donGia = txtThanhToanDonGia.getText().toString();
            String soNgay = txtThanhToanSoNgay.getText().toString();
            String thoiGianKetThuc = txtThanhToanThoiGianKetThuc.getText().toString();
            String tongTien = txtTongTien.getText().toString();

            // Kiểm tra dữ liệu
            if (tieuDe.isEmpty() || moTa.isEmpty() || diaChiHienThi.isEmpty() || dienTich.isEmpty() ||
                    giaThue.isEmpty() || soNguoiO.isEmpty() || loaiTro.isEmpty() || imageUrls == null ||
                    loaiTin.isEmpty() || donGia.isEmpty() || soNgay.isEmpty() || thoiGianKetThuc.isEmpty() || tongTien.isEmpty()) {
                Toast.makeText(requireContext(), "Thiếu thông tin để lưu tin đăng", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lưu dữ liệu
            new Thread(() -> {
                try {
                    TinDangDao tinDangDao = db.tinDangDao();
                    TinDang tinDang = new TinDang(tieuDe, moTa, diaChiHienThi, dienTich, giaThue, soNguoiO,
                            id_tien_ich, hoVaTen, sdt, email, avatar, ngayDang);
                    long ma_TinDang = tinDangDao.insert(tinDang);
                    int id_tindang = (int) ma_TinDang;

                    ThongTinNhaTroDao thongTinNhaTroDao = db.thongTinNhaTroDao();
                    ThongTinNhaTro thongTinNhaTro = new ThongTinNhaTro(soPhong, diaChi, loaiTro, noiThat, loaiPhong, giaDien, giaNuoc, giaInternet, id_tindang);
                    long ma_ThongTinNhaTro = thongTinNhaTroDao.insert(thongTinNhaTro);

                    DanhSachAnhDao danhSachAnhDao = db.danhSachAnhDao();
                    for (String imagePath : imageUrls) {
                        DanhSachAnh image = new DanhSachAnh(id_tindang, imagePath);
                        danhSachAnhDao.insertDanhSachAnh(image);
                    }

                    ThanhToan thanhToan = new ThanhToan(0, loaiTin, id_tindang, "Thành công", thoiGianKetThuc, tongTien);
                    AppDatabase.getInstance(requireContext()).thanhToanDao().insertThanhToan(thanhToan);

                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), "Lưu tin đăng và thanh toán thành công", Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putInt("ma_TinDang", id_tindang);
                        bundle.putString("loaiTin", loaiTin);
                        bundle.putString("donGia", donGia);
                        bundle.putString("soNgay", soNgay);
                        bundle.putString("thoiGianKetThuc", thoiGianKetThuc);
                        bundle.putString("tongTien", tongTien);
                        Navigation.findNavController(v).navigate(R.id.action_navigation_dangtinmoi_3_to_navigation_phuongthucthanhtoan_dangtin, bundle);
                    });
                } catch (Exception e) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(requireContext(), "Lỗi khi lưu tin đăng: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            }).start();
        });
    }

    private void handleThoat() {
        txt_dangtinmoi_3_thoat.setOnClickListener(v -> {
            Bundle args = getArguments();
            if (args == null) {
                Toast.makeText(requireContext(), "Thiếu thông tin tin đăng", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(v).popBackStack(R.id.navigation_qltindang, false);
                return;
            }

            // Lấy dữ liệu từ Bundle
            String tieuDe = args.getString("tieuDe", "");
            String moTa = args.getString("moTa", "");
            String diaChiHienThi = args.getString("diaChiHienThi", "");
            String dienTich = args.getString("dienTich", "");
            String giaThue = args.getString("giaThue", "");
            String soNguoiO = args.getString("soNguoiO", "");
            String ngayDang = args.getString("ngayDang", "");
            String diaChi = args.getString("diaChi", "");
            String soPhong = args.getString("soPhong", "");
            String giaDien = args.getString("giaDien", "");
            String giaNuoc = args.getString("giaNuoc", "");
            String giaInternet = args.getString("giaInternet", "");
            String loaiTro = args.getString("loaiTro", "");
            String noiThat = args.getString("noiThat", "");
            String loaiPhong = args.getString("loaiPhong", "");
            String id_tien_ich = args.getString("id_tien_ich", "");
            String hoVaTen = args.getString("hoVaTen", "");
            String sdt = args.getString("sdt", "");
            String email = args.getString("email", "");
            String avatar = args.getString("avatar", "");
            ArrayList<String> imageUrls = args.getStringArrayList("imageUrls");

            // Lưu dữ liệu
            new Thread(() -> {
                try {
                    TinDangDao tinDangDao = db.tinDangDao();
                    TinDang tinDang = new TinDang(tieuDe, moTa, diaChiHienThi, dienTich, giaThue, soNguoiO,
                            id_tien_ich, hoVaTen, sdt, email, avatar, ngayDang);
                    long ma_TinDang = tinDangDao.insert(tinDang);
                    int id_tindang = (int) ma_TinDang;

                    ThongTinNhaTroDao thongTinNhaTroDao = db.thongTinNhaTroDao();
                    ThongTinNhaTro thongTinNhaTro = new ThongTinNhaTro(soPhong, diaChi, loaiTro, noiThat, loaiPhong, giaDien, giaNuoc, giaInternet, id_tindang);
                    long ma_ThongTinNhaTro = thongTinNhaTroDao.insert(thongTinNhaTro);

                    if (imageUrls != null) {
                        DanhSachAnhDao danhSachAnhDao = db.danhSachAnhDao();
                        for (String imagePath : imageUrls) {
                            DanhSachAnh image = new DanhSachAnh(id_tindang, imagePath);
                            danhSachAnhDao.insertDanhSachAnh(image);
                        }
                    }

                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), "Lưu tin đăng thành công", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(v).popBackStack(R.id.navigation_qltindang, false);
                    });
                } catch (Exception e) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(requireContext(), "Lỗi khi lưu tin đăng: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            }).start();
        });
    }
}