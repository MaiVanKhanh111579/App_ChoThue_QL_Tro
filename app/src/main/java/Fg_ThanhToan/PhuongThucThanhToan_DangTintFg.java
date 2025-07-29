package Fg_ThanhToan;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.app_chothue_ql_tro.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PhuongThucThanhToan_DangTintFg extends Fragment {
    private View view;
    private ImageButton imgtbtnBackStack;
    private Button btnQuayLai, btnTiepTuc;
    private TextView txtToolbar, txtCountdown;
    private ImageView imgQrCode;
    private RadioButton rdnganhang;
    private String loaiTin, donGia, soNgay, thoiGianKetThuc, tongTien, ngayBatDau;
    private int maTinDang;
    private CountDownTimer countDownTimer;
    private boolean thanhToanThanhCong = true; // Trạng thái thanh toán

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_phuongthucthanhtoan, container, false);
        anhXaView();
        toolBar();

        // Nhận dữ liệu từ Bundle
        if (getArguments() != null) {
            maTinDang = getArguments().getInt("ma_TinDang", 0);
            loaiTin = getArguments().getString("loaiTin", "");
            donGia = getArguments().getString("donGia", "");
            soNgay = getArguments().getString("soNgay", "");
            thoiGianKetThuc = getArguments().getString("thoiGianKetThuc", "");
            tongTien = getArguments().getString("tongTien", "");
        }

        // Tính ngày bắt đầu dựa trên thời gian kết thúc và số ngày
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date ketThuc = dateFormat.parse(thoiGianKetThuc);
            int soNgayInt = Integer.parseInt(soNgay.replace(" ngày", ""));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(ketThuc);
            calendar.add(Calendar.DAY_OF_MONTH, -soNgayInt);
            ngayBatDau = dateFormat.format(calendar.getTime());
        } catch (Exception e) {
            ngayBatDau = "";
        }

        // Kiểm tra dữ liệu
        if (maTinDang == 0 || loaiTin.isEmpty() || donGia.isEmpty() || soNgay.isEmpty() || thoiGianKetThuc.isEmpty() || tongTien.isEmpty() || ngayBatDau.isEmpty()) {
            Toast.makeText(requireContext(), "Thiếu thông tin thanh toán", Toast.LENGTH_SHORT).show();
            Navigation.findNavController(view).popBackStack();
            return view;
        }

        // Tạo mã QR ngay khi vào màn hình
        @SuppressLint("DefaultLocale") String qrData = String.format(
                "Mã tin đăng: %d\nLoại tin: %s\nĐơn giá: %s\nSố ngày: %s\nThời gian bắt đầu: %s\nThời gian kết thúc: %s\nTổng tiền: %s\nPhương thức: Ngân hàng",
                maTinDang, loaiTin, donGia, soNgay, ngayBatDau, thoiGianKetThuc, tongTien
        );
        Bitmap qrBitmap = generateQRCode(qrData);
        if (qrBitmap != null) {
            imgQrCode.setImageBitmap(qrBitmap);
            imgQrCode.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(requireContext(), "Không thể tạo mã QR", Toast.LENGTH_SHORT).show();
            thanhToanThanhCong = false;
            navigateToResult();
            return view;
        }

        // Khởi động bộ đếm ngược 5 phút (300,000ms)
        startCountdownTimer();

        // Xử lý nút Tiếp tục
        btnTiepTuc.setOnClickListener(v -> {
            if (!rdnganhang.isChecked()) {
                Toast.makeText(requireContext(), "Vui lòng chọn phương thức thanh toán qua ngân hàng", Toast.LENGTH_SHORT).show();
                return;
            }
            navigateToResult();
        });

        // Xử lý nút Quay lại
        btnQuayLai.setOnClickListener(v -> {
            if (countDownTimer != null) {
                countDownTimer.cancel(); // Hủy bộ đếm khi quay lại
            }
            Navigation.findNavController(v).popBackStack();
        });

        return view;
    }

    private void anhXaView() {
        txtToolbar = view.findViewById(R.id.txt_layout_toolbar_have_backstack_tittle);
        imgtbtnBackStack = view.findViewById(R.id.imgbtn_layout_toolbar_have_backstack_BackStack);
        imgQrCode = view.findViewById(R.id.img_qr_code);
        btnQuayLai = view.findViewById(R.id.btn_phuongthucthanhtoan_quaylai);
        btnTiepTuc = view.findViewById(R.id.btn_phuongthucthanhtoan_tieptuc);
        rdnganhang = view.findViewById(R.id.radio_phuongthucthanhtoan_nganhang);
        txtCountdown = view.findViewById(R.id.txt_countdown);
    }

    @SuppressLint("SetTextI18n")
    private void toolBar() {
        txtToolbar.setText("Phương thức thanh toán");
        imgtbtnBackStack.setVisibility(View.GONE);
    }

    private Bitmap generateQRCode(String data) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 200, 200);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void startCountdownTimer() {
        txtCountdown.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(300000, 1000) { // 5 phút = 300,000ms
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                long minutes = seconds / 60;
                seconds = seconds % 60;
                txtCountdown.setText("Thời gian còn lại: " + String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                imgQrCode.setVisibility(View.GONE);
                txtCountdown.setText("Mã QR đã hết hạn");
                Toast.makeText(requireContext(), "Mã QR đã hết hạn. Vui lòng quay lại và tạo lại.", Toast.LENGTH_LONG).show();
                btnTiepTuc.setEnabled(false); // Vô hiệu hóa nút Tiếp tục
                thanhToanThanhCong = false; // Đặt trạng thái thất bại
            }
        }.start();
    }

    private void navigateToResult() {
        if (countDownTimer != null) {
            countDownTimer.cancel(); // Hủy bộ đếm khi điều hướng
        }
        NavController navController = Navigation.findNavController(view);
        Bundle bundle = new Bundle();
        bundle.putInt("ma_TinDang", maTinDang);
        bundle.putString("loaiTin", loaiTin);
        bundle.putString("ngayBatDau", ngayBatDau);
        bundle.putString("thoiGianKetThuc", thoiGianKetThuc);
        bundle.putString("tongTien", tongTien);
        bundle.putBoolean("thanhToanThanhCong", thanhToanThanhCong);
        navController.navigate(R.id.action_navigation_phuongthucthanhtoan_dangtin_to_navigation_ketquagiaodich_dangtin, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel(); // Hủy bộ đếm khi rời khỏi fragment
        }
    }
}