package Fg_QLTinDang;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app_chothue_ql_tro.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import Adapter.DangTinMoi_TienIchAdapter;
import Data.AppDatabase;
import Data.DAO.DanhSachAnhDao;
import Data.DAO.ThongTinNhaTroDao;
import Data.DAO.TienIchDao;
import Data.DAO.TinDangDao;
import Data.DAO.UserDao;
import Data.ENTITY.DanhSachAnh;
import Data.ENTITY.ThongTinNhaTro;
import Data.ENTITY.TienIch;
import Data.ENTITY.TinDang;
import Data.ENTITY.User;
import de.hdodenhof.circleimageview.CircleImageView;

public class DangTinMoiFg extends Fragment {
    private TextView txt_thoat, txt_title_DiaChi, txt_title_ThongTinChinh, txt_title_ThongTinKhac, txt_title_ThongTinLienHe,
            txt_title_TieuDevaMoTa, txtPhongTro, txtNhaTro, txtNoiThatDayDu, txtNoiThatCoBan, txtNoiThatKhong, txtDungChung, txtDungRieng, txtTienPhong;
    private ImageButton imgbtnSoNguoiOMinus, imgbtnSoNguoiOPlus, imgbtnCloseThemNoiThat, imgbtnCloseThongTinKhac,
            imgbtnArrowDownDiaChi, imgbtnArrowDownThongTinChinh, imgbtnArrowDownThongTinKhac, imgbtnArrowDownThongTinLienHe, imgbtnArrowDownTieuDeVaMoTa;
    private ConstraintLayout constraint_dangtinmoi_button_diachi, constraint_dangtinmoi_button_thongtinchinh,
            constraint_dangtinmoi_button_thongtinkhac, constraint_dangtinmoi_button_thongtinlienhe, constraint_dangtinmoi_button_tieude_va_mota;
    private LinearLayout llayoutShowDiaChi, llayoutShowThongTinChinh, llayoutShowThongTinKhac, llayoutShowThongTinLienHe, llayoutShowTieuDeVaMoTa;
    private EditText edtSoPhong, edtDiaChi, edtHienThi, edtDienTich, edtGiaThue, edtThemNoiThat, edtThongTinKhac,
            edtSoNguoiO, edtMucGiaDien, edtMucGiaNuoc, edtMucGiaInternet, edtHoVaTen, edtEmail, edtSdt, edtTieuDe, edtMoTa;
    private RecyclerView recycleview_TienIch;
    private Button btnTiepTuc;
    private BottomNavigationView bottomNav;
    private View view;
    private boolean isPhongTro = false;
    private boolean isNhaTro = false;
    private String ChonloaiTro="";
    private boolean isNoiThatDayDu = false;
    private boolean isNoiThatCoBan = false;
    private boolean isNoiThatKhong = false;
    private String ChonNoiThat="";
    private boolean isDungChung = false;
    private boolean isDungRieng = false;
    private String ChonLoaiPhong = "";
    private User logged;
    private RecyclerView recyclerView_Tienich;
    private DangTinMoi_TienIchAdapter adapter;
    private List<TienIch> itemList;
    private List<TienIch> selectedTienIchList = new ArrayList<>();
    private Uri avatar;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_dangtinmoi, container, false);
        anhXaView();
        bottomNav.setVisibility(View.GONE);
        taoTienIch();
        txt_thoat.setOnClickListener(v -> {
            bottomNav.setVisibility(View.VISIBLE);
            Navigation.findNavController(v).popBackStack();
        });
        showHideGroup();
        selectloaiTro();
        selectNoiThat();
        selectloaiPhong();
        imgbtnSoNguoiOMinus.setOnClickListener(v -> adjustSoNguoiO(-1, imgbtnSoNguoiOMinus, imgbtnSoNguoiOPlus));
        imgbtnSoNguoiOPlus.setOnClickListener(v -> adjustSoNguoiO(1, imgbtnSoNguoiOMinus, imgbtnSoNguoiOPlus));
        imgbtnCloseThemNoiThat.setOnClickListener(v -> edtThemNoiThat.setText(""));
        imgbtnCloseThongTinKhac.setOnClickListener(v -> edtThongTinKhac.setText(""));
        edtTienThue();

        btnTiepTuc.setOnClickListener(v -> {
            insert_TinDang_and_ThongTinNhaTro();
        });

        return view;
    }

    private void anhXaView() {
        bottomNav = requireActivity().findViewById(R.id.bottom_nav_dangtin);

        txt_thoat = view.findViewById(R.id.txt_dangtinmoi_thoat);
        constraint_dangtinmoi_button_diachi = view.findViewById(R.id.constraint_dangtinmoi_button_diachi);
        llayoutShowDiaChi = view.findViewById(R.id.llayout_dangtinmoi_show_diachi);
        imgbtnArrowDownDiaChi = view.findViewById(R.id.imgbtn_arrow_down_diachi);
        txt_title_DiaChi = view.findViewById(R.id.txt_dangtinmoi_title_diachi);
        edtSoPhong = view.findViewById(R.id.edt_dangtinmoi_sophong);
        edtDiaChi = view.findViewById(R.id.edt_dangtinmoi_diachi);
        edtHienThi = view.findViewById(R.id.edt_dangtinmoi_diachi_hienthi);

        constraint_dangtinmoi_button_thongtinchinh = view.findViewById(R.id.constraint_dangtinmoi_button_thongtinchinh);
        txtPhongTro = view.findViewById(R.id.txt_dangtinmoi_chon_phongtro);
        txtNhaTro = view.findViewById(R.id.txt_dangtinmoi_chon_nhatro);
        txt_title_ThongTinChinh = view.findViewById(R.id.txt_dangtinmoi_title_thongtinchinh);
        edtDienTich = view.findViewById(R.id.edt_dangtinmoi_dientich);
        edtGiaThue = view.findViewById(R.id.edt_dangtinmoi_giathue);
        imgbtnArrowDownThongTinChinh = view.findViewById(R.id.imgbtn_arrow_down_thongtinchinh);
        llayoutShowThongTinChinh = view.findViewById(R.id.llayout_dangtinmoi_show_thongtinchinh);

        constraint_dangtinmoi_button_thongtinkhac = view.findViewById(R.id.constraint_dangtinmoi_button_thongtinkhac);
        imgbtnArrowDownThongTinKhac = view.findViewById(R.id.imgbtn_arrow_down_thongtinkhac);
        imgbtnCloseThemNoiThat = view.findViewById(R.id.imgbtn_dangtinmoi_thongtinkhac_close_1);
        imgbtnCloseThongTinKhac = view.findViewById(R.id.imgbtn_dangtinmoi_thongtinkhac_close_2);
        imgbtnSoNguoiOMinus = view.findViewById(R.id.imgbtn_dangtinmoi_songuoio_minus);
        imgbtnSoNguoiOPlus = view.findViewById(R.id.imgbtn_dangtinmoi_songuoio_plus);
        txtNoiThatDayDu = view.findViewById(R.id.txt_dangtinmoi_noithat_daydu);
        txtNoiThatCoBan = view.findViewById(R.id.txt_dangtinmoi_noithat_coban);
        txtNoiThatKhong = view.findViewById(R.id.txt_dangtinmoi_noithat_khongnoithat);
        txtDungChung = view.findViewById(R.id.txt_dangtinmoi_noithat_dungchung);
        txtDungRieng = view.findViewById(R.id.txt_dangtinmoi_noithat_dungrieng);
        txt_title_ThongTinKhac = view.findViewById(R.id.txt_dangtinmoi_title_thongtinkhac);
        edtThemNoiThat = view.findViewById(R.id.edt_dangtinmoi_noithat_themnoithat);
        edtThongTinKhac = view.findViewById(R.id.edt_dangtinmoi_loaiphong_thongtinkhac);
        edtMucGiaDien = view.findViewById(R.id.edt_dangtinmoi_thongtinkhac_mucgiadien);
        edtMucGiaNuoc = view.findViewById(R.id.edt_dangtinmoi_thongtinkhac_mucgianuoc);
        edtMucGiaInternet = view.findViewById(R.id.edt_dangtinmoi_thongtinkhac_mucgiainternet);
        edtSoNguoiO = view.findViewById(R.id.edt_dangtinmoi_songuoio);
        llayoutShowThongTinKhac = view.findViewById(R.id.llayout_dangtinmoi_show_thongtinkhac);

        constraint_dangtinmoi_button_thongtinlienhe = view.findViewById(R.id.constraint_dangtinmoi_button_thongtinlienhe);
        txt_title_ThongTinLienHe = view.findViewById(R.id.txt_dangtinmoi_title_thongtinlienhe);
        edtHoVaTen = view.findViewById(R.id.edt_dangtinmoi_thongtinlienhe_hovaten);
        edtEmail = view.findViewById(R.id.edt_dangtinmoi_thongtinlienhe_email);
        edtSdt = view.findViewById(R.id.edt_dangtinmoi_thongtinlienhe_sdt);
        imgbtnArrowDownThongTinLienHe = view.findViewById(R.id.imgbtn_arrow_down_thongtinlienhe);
        llayoutShowThongTinLienHe = view.findViewById(R.id.llayout_dangtinmoi_show_thongtinlienhe);

        constraint_dangtinmoi_button_tieude_va_mota = view.findViewById(R.id.constraint_dangtinmoi_button_tieude_va_mota);
        txt_title_TieuDevaMoTa = view.findViewById(R.id.txt_dangtinmoi_title_tieude_va_mota);
        edtTieuDe = view.findViewById(R.id.edt_dangtinmoi_tieude_va_mota_tieude);
        edtMoTa = view.findViewById(R.id.edt_dangtinmoi_tieude_va_mota_mota);
        imgbtnArrowDownTieuDeVaMoTa = view.findViewById(R.id.imgbtn_arrow_down_tieude_va_mota);
        llayoutShowTieuDeVaMoTa = view.findViewById(R.id.llayout_dangtinmoi_show_tieude_va_mota);

        btnTiepTuc = view.findViewById(R.id.btn_dangtinmoi_tieptuc);
        recycleview_TienIch = view.findViewById(R.id.recycleview_dangtinmoi_tienich);
        txtTienPhong = view.findViewById(R.id.txt_dangtinmoi_show_tinhtien_phong);
        db = AppDatabase.getInstance(requireContext());
    }
    private void edtTienThue(){
        edtGiaThue.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @SuppressLint("SetTextI18n")
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(current)) {
                    // Ngăn vòng lặp vô hạn khi thay đổi text
                    edtGiaThue.removeTextChangedListener(this);

                    // Lấy giá trị gốc (loại bỏ dấu phẩy)
                    String cleanString = s.toString().replaceAll("[^0-9]", "");
                    if (!cleanString.isEmpty()) {
                        try {
                            // Chuyển thành số
                            long parsed = Long.parseLong(cleanString);
                            // Định dạng số với dấu phẩy
                            DecimalFormat formatter = new DecimalFormat("#,###");
                            String formatted = formatter.format(parsed);
                            current = formatted;

                            // Cập nhật EditText và TextView
                            edtGiaThue.setText(formatted);
                            edtGiaThue.setSelection(formatted.length()); // Đặt con trỏ trước " VNĐ"
                            txtTienPhong.setText("Khách thuê sẽ đóng " + formatted + " VNĐ/ tháng");
                            edtMucGiaInternet.setText(formatted);
                        } catch (NumberFormatException e) {
                            txtTienPhong.setText("Khách thuê sẽ đóng 0 VNĐ/ tháng");
                        }
                    } else {
                        txtTienPhong.setText("Khách thuê sẽ đóng 0 VNĐ/ tháng");
                    }

                    // Gắn lại TextWatcher
                    edtGiaThue.addTextChangedListener(this);
                }
            }
        });
    }
    private void showHideGroup() {
        txt_title_DiaChi.setOnClickListener(v -> toggleSection(llayoutShowDiaChi, txt_title_DiaChi, imgbtnArrowDownDiaChi));
        constraint_dangtinmoi_button_diachi.setOnClickListener(v -> toggleSection(llayoutShowDiaChi, txt_title_DiaChi, imgbtnArrowDownDiaChi));
        txt_title_ThongTinChinh.setOnClickListener(v -> toggleSection(llayoutShowThongTinChinh, txt_title_ThongTinChinh, imgbtnArrowDownThongTinChinh));
        constraint_dangtinmoi_button_thongtinchinh.setOnClickListener(v -> toggleSection(llayoutShowThongTinChinh, txt_title_ThongTinChinh, imgbtnArrowDownThongTinChinh));
        txt_title_ThongTinKhac.setOnClickListener(v -> toggleSection(llayoutShowThongTinKhac, txt_title_ThongTinKhac, imgbtnArrowDownThongTinKhac));
        constraint_dangtinmoi_button_thongtinkhac.setOnClickListener(v -> toggleSection(llayoutShowThongTinKhac, txt_title_ThongTinKhac, imgbtnArrowDownThongTinKhac));
        txt_title_ThongTinLienHe.setOnClickListener(v -> toggleSection(llayoutShowThongTinLienHe, txt_title_ThongTinLienHe, imgbtnArrowDownThongTinLienHe));
        constraint_dangtinmoi_button_thongtinlienhe.setOnClickListener(v -> toggleSection(llayoutShowThongTinLienHe, txt_title_ThongTinLienHe, imgbtnArrowDownThongTinLienHe));
        txt_title_TieuDevaMoTa.setOnClickListener(v -> toggleSection(llayoutShowTieuDeVaMoTa, txt_title_TieuDevaMoTa, imgbtnArrowDownTieuDeVaMoTa));
        constraint_dangtinmoi_button_tieude_va_mota.setOnClickListener(v -> toggleSection(llayoutShowTieuDeVaMoTa, txt_title_TieuDevaMoTa, imgbtnArrowDownTieuDeVaMoTa));
    }

    private void toggleSection(LinearLayout layout, TextView title, ImageButton arrow) {
        if (layout.getVisibility() == View.VISIBLE) {
            layout.setVisibility(View.GONE);
            title.setVisibility(View.VISIBLE);
            arrow.setRotation(180);
        } else {
            layout.setVisibility(View.VISIBLE);
            title.setVisibility(View.GONE);
            arrow.setRotation(0);
        }
    }
    private void selectloaiTro() {
        txtPhongTro.setOnClickListener(v -> {
            toggleSelection(txtPhongTro, R.style.Heading_3_Bold_black, isPhongTro, () -> {
                isPhongTro = true;
                isNhaTro = false;
                ChonloaiTro = "Phòng Trọ";
                updateTroSelection(txtPhongTro, txtNhaTro);
            });
        });
        txtNhaTro.setOnClickListener(v -> {
            toggleSelection(txtNhaTro, R.style.Heading_3_Bold_black, isNhaTro, () -> {
                isNhaTro = true;
                isPhongTro = false;
                ChonloaiTro = "Nhà Trọ";
                updateTroSelection(txtPhongTro, txtNhaTro);
            });
        });
    }

    private void selectNoiThat() {
        txtNoiThatDayDu.setOnClickListener(v -> {
            toggleSelection(txtNoiThatDayDu, R.style.Heading_3_Bold_black, isNoiThatDayDu, () -> {
                isNoiThatDayDu = true;
                isNoiThatCoBan = false;
                isNoiThatKhong = false;
                ChonNoiThat = "Đầy đủ nội thất";
                updateNoiThatSelection(txtNoiThatDayDu, txtNoiThatCoBan, txtNoiThatKhong);
            });
        });
        txtNoiThatCoBan.setOnClickListener(v -> {
            toggleSelection(txtNoiThatCoBan, R.style.Heading_3_Bold_black, isNoiThatCoBan, () -> {
                isNoiThatCoBan = true;
                isNoiThatDayDu = false;
                isNoiThatKhong = false;
                ChonNoiThat = "Nội thất cơ bản";
                updateNoiThatSelection(txtNoiThatDayDu, txtNoiThatCoBan, txtNoiThatKhong);
            });
        });
        txtNoiThatKhong.setOnClickListener(v -> {
            toggleSelection(txtNoiThatKhong, R.style.Heading_3_Bold_black, isNoiThatKhong, () -> {
                isNoiThatKhong = true;
                isNoiThatDayDu = false;
                isNoiThatCoBan = false;
                ChonNoiThat = "Không có nội thất";
                updateNoiThatSelection(txtNoiThatDayDu, txtNoiThatCoBan, txtNoiThatKhong);
            });
        });
    }

    private void selectloaiPhong() {
        txtDungChung.setOnClickListener(v -> {
            toggleSelection(txtDungChung, R.style.Heading_3_Bold_black, isDungChung, () -> {
                isDungChung = true;
                isDungRieng = false;
                ChonLoaiPhong = "Dùng chung";
                updatePhongSelection(txtDungChung, txtDungRieng);
            });
        });
        txtDungRieng.setOnClickListener(v -> {
            toggleSelection(txtDungRieng, R.style.Heading_3_Bold_black, isDungRieng, () -> {
                isDungRieng = true;
                isDungChung = false;
                ChonLoaiPhong = "Dùng riêng";
                updatePhongSelection(txtDungChung, txtDungRieng);
            });
        });
    }

    private void toggleSelection(TextView textView, int selectedStyle, boolean isSelected, Runnable updateFlag) {
        textView.setBackgroundResource(isSelected ? R.drawable.customer_background_noborder_radius_10dp : R.drawable.customer_button_redborder_radius_10dp);
        textView.setTextAppearance(requireContext(), isSelected ? R.style.Heading_3_Regular_black : selectedStyle);
        updateFlag.run();
    }

    private void updateTroSelection(TextView txtPhongTro, TextView txtNhaTro) {
        txtPhongTro.setBackgroundResource(isPhongTro ? R.drawable.customer_button_redborder_radius_10dp : R.drawable.customer_background_noborder_radius_10dp);
        txtPhongTro.setTextAppearance(requireContext(), isPhongTro ? R.style.Heading_3_Bold_black : R.style.Heading_3_Regular_black);
        txtNhaTro.setBackgroundResource(isNhaTro ? R.drawable.customer_button_redborder_radius_10dp : R.drawable.customer_background_noborder_radius_10dp);
        txtNhaTro.setTextAppearance(requireContext(), isNhaTro ? R.style.Heading_3_Bold_black : R.style.Heading_3_Regular_black);
    }

    private void updateNoiThatSelection(TextView txtDayDu, TextView txtCoBan, TextView txtKhong) {
        txtDayDu.setBackgroundResource(isNoiThatDayDu ? R.drawable.customer_button_redborder_radius_10dp : R.drawable.customer_background_noborder_radius_10dp);
        txtDayDu.setTextAppearance(requireContext(), isNoiThatDayDu ? R.style.Heading_3_Bold_black : R.style.Heading_3_Regular_black);
        txtCoBan.setBackgroundResource(isNoiThatCoBan ? R.drawable.customer_button_redborder_radius_10dp : R.drawable.customer_background_noborder_radius_10dp);
        txtCoBan.setTextAppearance(requireContext(), isNoiThatCoBan ? R.style.Heading_3_Bold_black : R.style.Heading_3_Regular_black);
        txtKhong.setBackgroundResource(isNoiThatKhong ? R.drawable.customer_button_redborder_radius_10dp : R.drawable.customer_background_noborder_radius_10dp);
        txtKhong.setTextAppearance(requireContext(), isNoiThatKhong ? R.style.Heading_3_Bold_black : R.style.Heading_3_Regular_black);
    }

    private void updatePhongSelection(TextView txtDungChung, TextView txtDungRieng) {
        txtDungChung.setBackgroundResource(isDungChung ? R.drawable.customer_button_redborder_radius_10dp : R.drawable.customer_background_noborder_radius_10dp);
        txtDungChung.setTextAppearance(requireContext(), isDungChung ? R.style.Heading_3_Bold_black : R.style.Heading_3_Regular_black);
        txtDungRieng.setBackgroundResource(isDungRieng ? R.drawable.customer_button_redborder_radius_10dp : R.drawable.customer_background_noborder_radius_10dp);
        txtDungRieng.setTextAppearance(requireContext(), isDungRieng ? R.style.Heading_3_Bold_black : R.style.Heading_3_Regular_black);
    }

    private void adjustSoNguoiO(int delta, ImageButton btnMinus, ImageButton btnPlus) {
        String soNguoiOStr = edtSoNguoiO.getText().toString().trim();
        int soNguoiO = soNguoiOStr.isEmpty() ? 0 : Integer.parseInt(soNguoiOStr);
        soNguoiO = Math.max(0, Math.min(10, soNguoiO + delta));
        edtSoNguoiO.setText(String.valueOf(soNguoiO));
        btnMinus.setEnabled(soNguoiO > 0);
        btnPlus.setEnabled(soNguoiO < 10);
    }

    private void insert_TinDang_and_ThongTinNhaTro() {
        String tieuDe = edtTieuDe.getText().toString().trim();
        String moTa = edtMoTa.getText().toString().trim();
        String diaChiHienThi = edtHienThi.getText().toString().trim();
        String dienTich = edtDienTich.getText().toString().trim();
        String giaThue = edtGiaThue.getText().toString().trim();
        String soNguoiO = edtSoNguoiO.getText().toString().trim();
        String ngayDang = getCurrentDate(); // Lấy ngày hiện tại
        // Kiểm tra thông tin đầu vào
        if (diaChiHienThi.isEmpty() || dienTich.isEmpty() || giaThue.isEmpty() || tieuDe.isEmpty() || moTa.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        String diaChi = edtDiaChi.getText().toString().trim();
        String soPhong = edtSoPhong.getText().toString().trim();
        String giaDien = edtMucGiaDien.getText().toString().trim();
        String giaNuoc = edtMucGiaNuoc.getText().toString().trim();
        String giaInternet = edtMucGiaInternet.getText().toString().trim();
        String loaiTro = "";
        String noiThat = "";
        String loaiPhong = "";
        if (ChonloaiTro.equals("Phòng Trọ")) {
            loaiTro = "Phòng Trọ";
        } else if (ChonloaiTro.equals("Nhà Trọ")) {
            loaiTro = "Nhà Trọ";
        } else {loaiTro = "";}

        if (ChonNoiThat.equals("Đầy đủ nội thất")) {
            noiThat = "Đầy đủ nội thất";
        } else if (ChonNoiThat.equals("Nội thất cơ bản")) {
            noiThat = "Nội thất cơ bản";
        } else if (ChonNoiThat.equals("Không có nội thất")) {
            noiThat = "Không có nội thất";
        } else {
            noiThat = "";
        }

        if (ChonLoaiPhong.equals("Dùng chung")){
            loaiPhong = "Dùng chung";
        } else if (ChonLoaiPhong.equals("Dùng riêng")) {
            loaiPhong = "Dùng riêng";
        } else {loaiPhong = "";}

//        try {
//            float dienTichValue = Float.parseFloat(dienTich);
//            float giaThueValue = Float.parseFloat(giaThue);
//            if (dienTichValue <= 0 || giaThueValue <= 0) {
//                Toast.makeText(requireContext(), "Diện tích và giá thuê phải lớn hơn 0", Toast.LENGTH_SHORT).show();
//                return;
//            }
//        } catch (NumberFormatException e) {
//            Toast.makeText(requireContext(), "Diện tích và giá thuê phải là số hợp lệ", Toast.LENGTH_SHORT).show();
//            return;
//        }

        String finalLoaiTro = loaiTro;
        String finalNoiThat = noiThat;
        String finalLoaiPhong = loaiPhong;

        String id_tien_ich = selectedTienIchList.stream()
                .map(TienIch::getMaTienIch)
                .collect(Collectors.joining(","));
        Log.d("TienIch", "Saving id_tien_ich: " + id_tien_ich);
        new Thread(() -> {
            UserDao userDao = db.userDao();
            User logged = userDao.findLoggedUser();
            if (logged == null) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show());
                return;
            }

            // Cập nhật giao diện
            requireActivity().runOnUiThread(() -> {
                edtHoVaTen.setText(logged.getHoVaTen());
                edtSdt.setText(logged.getSDTChinh());
                edtEmail.setText(logged.getEmail());
                if (logged.getAvatar() != null && !logged.getAvatar().isEmpty()) {
                    avatar = Uri.parse(logged.getAvatar());
                }
            });

            // Lưu tin đăng
            try {
                TinDangDao tinDangDao = db.tinDangDao();
                TinDang tinDang = new TinDang(tieuDe, moTa, diaChiHienThi, dienTich, giaThue, soNguoiO,
                        id_tien_ich, logged.getHoVaTen(), logged.getSDTChinh(), logged.getEmail(), logged.getAvatar(), ngayDang);
                long ma_TinDang = tinDangDao.insert(tinDang);
                int id_tindang = (int) ma_TinDang;
                ThongTinNhaTroDao thongTinNhaTroDao = db.thongTinNhaTroDao();
                ThongTinNhaTro thongTinNhaTro = new ThongTinNhaTro(soPhong, diaChi, finalLoaiTro, finalNoiThat, finalLoaiPhong, giaDien, giaNuoc, giaInternet, id_tindang);
                long ma_ThongTinNhaTro = thongTinNhaTroDao.insert(thongTinNhaTro);
                if (ma_TinDang != -1 & ma_ThongTinNhaTro != -1) {
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), "Lưu tin đăng thành công", Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putInt("ma_TinDang", (int) ma_TinDang);
                        Navigation.findNavController(view).navigate(R.id.action_navigation_dangtinmoi_to_navigation_dangtinmoi_2, bundle);
                    });
                } else {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(requireContext(), "Lỗi khi lưu tin đăng", Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "Lỗi khi lưu tin đăng: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    private void taoTienIch() {
        itemList = new ArrayList<>();
        new Thread(() -> {
            // Chèn dữ liệu nếu chưa tồn tại
            TienIchDao tienIchDao = db.tienIchDao();
            if (tienIchDao.getTienIchByMa("Ti01") == null) {
                tienIchDao.insertTienIch(new TienIch("Ti01", "Thang máy"));
                tienIchDao.insertTienIch(new TienIch("Ti02", "Bãi xe"));
                tienIchDao.insertTienIch(new TienIch("TI03", "Bảo vệ"));
                tienIchDao.insertTienIch(new TienIch("TI04", "24/24"));
                tienIchDao.insertTienIch(new TienIch("TI05", "Có Wifi"));
                tienIchDao.insertTienIch(new TienIch("TI06", "Có Camera an ninh"));
                tienIchDao.insertTienIch(new TienIch("TI07", "Được nuôi thú cưng"));
                tienIchDao.insertTienIch(new TienIch("TI08", "Có nơi sinh hoạt"));
                tienIchDao.insertTienIch(new TienIch("TI09", "Riêng với chủ"));
            }
        }).start();
        // Tạo danh sách hiển thị
        itemList.add(new TienIch("Ti01", "Thang máy"));
        itemList.add(new TienIch("Ti02", "Bãi xe"));
        itemList.add(new TienIch("TI03", "Bảo vệ"));
        itemList.add(new TienIch("TI04", "24/24"));
        itemList.add(new TienIch("TI05", "Có Wifi"));
        itemList.add(new TienIch("TI06", "Có Camera an ninh"));
        itemList.add(new TienIch("TI07", "Được nuôi thú cưng"));
        itemList.add(new TienIch("TI08", "Có nơi sinh hoạt"));
        itemList.add(new TienIch("TI09", "Riêng với chủ"));

        adapter = new DangTinMoi_TienIchAdapter(itemList, (tienIch, position) -> {
            if (tienIch.isSelected()) {
                selectedTienIchList.add(tienIch);
            } else {
                selectedTienIchList.remove(tienIch);
            }
            adapter.notifyItemChanged(position);
            Snackbar.make(view, (tienIch.isSelected() ? "Đã chọn: " : "Đã bỏ chọn: ") + tienIch.getTenTienIch(), Snackbar.LENGTH_SHORT).show();
        });
        recycleview_TienIch.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        recycleview_TienIch.setAdapter(adapter);
    }
}