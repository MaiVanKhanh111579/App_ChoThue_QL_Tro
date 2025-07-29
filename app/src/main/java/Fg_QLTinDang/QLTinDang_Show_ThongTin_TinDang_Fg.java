package Fg_QLTinDang;

import android.annotation.SuppressLint;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_chothue_ql_tro.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import Adapter.DangTinMoi_TienIchAdapter;
import Data.AppDatabase;
import Data.DAO.ThongTinNhaTroDao;
import Data.DAO.TienIchDao;
import Data.DAO.TinDangDao;
import Data.ENTITY.ThongTinNhaTro;
import Data.ENTITY.TienIch;
import Data.ENTITY.TinDang;

public class QLTinDang_Show_ThongTin_TinDang_Fg extends Fragment {
    private View view;
    private BottomNavigationView bottomNav;
    private AppDatabase db;
    private ImageButton imgbtnCheDoNguoiXem, imgbtnBackStack;
    private Button btnLuuThongTin, btnThongTinNhaTro, btnThongTinTinDang;
    private TextView txtToolBar, txtChinhSuaAnh;
    private EditText edtMaTinDang, edtTieuDe, edtMoTa, edtDiaChi, edtDienTich, edtGiaThue, edtSoNguoiO, edtHoVaTen, edtSdt, edtEmail,
    edtMaThongTinNhaTro, edtDiaChiChinh, edtLoaiTro, edtNoiThat, edtLoaiPhong, edtGiaDien, edtGiaNuoc, edtGiaInternet;
    private RecyclerView recyclerViewTienIch;
    private LinearLayout llayoutThongTinTinDang, llayoutThongTinNhaTro;
    private int ma_TinDang;
    private boolean isSelectedbtnThongTinTinDang = false;
    private boolean  isSelectedbtnThongTinNhaTro = false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_qltindang_show_thongtin_tindang, container,false);
        anhXaView();
        bottomNav.setVisibility(View.GONE);
        Toolbar();
        loadThongTinTinDang_ThongTinNhaTro();
        edtTienThue();
        selectloaiTro();
        loadTienIch();
        btnLuuThongTin.setOnClickListener(v -> {updateTinDang_ThongTinNhaTro();});
        imgbtnCheDoNguoiXem.setOnClickListener(v -> {
            new Thread(()->{
                TinDangDao tinDangDao = db.tinDangDao();
                TinDang tinDang = tinDangDao.getTinDangById(ma_TinDang);
                requireActivity().runOnUiThread(()->{
                    Bundle bundle = new Bundle();
                    bundle.putInt("ma_TinDang", tinDang.getTin_dang_id());
                    Navigation.findNavController(v).navigate(R.id.action_navigation_qltindang_show_thongtin_tindang_to_navigation_qltindang_show_chedo_nguoixem, bundle);
                    bottomNav.setVisibility(View.GONE);
                });
            }).start();
        });
        txtChinhSuaAnh.setOnClickListener(v -> {
            new Thread(()->{
                TinDangDao tinDangDao = db.tinDangDao();
                TinDang tinDang = tinDangDao.getTinDangById(ma_TinDang);
                requireActivity().runOnUiThread(()->{
                    Bundle bundle1 = new Bundle();
                    bundle1.putInt("ma_TinDang", tinDang.getTin_dang_id());
                    Navigation.findNavController(v).navigate(R.id.action_navigation_qltindang_show_thongtin_tindang_to_navigation_qltindang_chinhsua_danhsachanh, bundle1);
                    bottomNav.setVisibility(View.GONE);
                });
            }).start();

        });
        return view;
    }

    private void anhXaView() {
        txtToolBar = view.findViewById(R.id.txt_layout_toolbar_have_backstack_tittle);
        txtChinhSuaAnh = view.findViewById(R.id.txt_qltindang_show_thongtin_tindang_chinhsua_anh);

        imgbtnCheDoNguoiXem = view.findViewById(R.id.imgbtn_qltindang_show_thongtin_tindang_chedo_nguoixem);
        imgbtnBackStack = view.findViewById(R.id.imgbtn_layout_toolbar_have_backstack_BackStack);

        edtMaTinDang = view.findViewById(R.id.edt_qltindang_show_thongtin_tindang_matindang);
        edtTieuDe = view.findViewById(R.id.edt_qltindang_show_thongtin_tindang_tieude);
        edtMoTa = view.findViewById(R.id.edt_qltindang_show_thongtin_tindang_mota);
        edtDiaChi = view.findViewById(R.id.edt_qltindang_show_thongtin_tindang_diachi);
        edtDienTich = view.findViewById(R.id.edt_qltindang_show_thongtin_tindang_dientich);
        edtGiaThue = view.findViewById(R.id.edt_qltindang_show_thongtin_tindang_tienthue);
        edtSoNguoiO = view.findViewById(R.id.edt_qltindang_show_thongtin_tindang_songuoio);
        edtHoVaTen = view.findViewById(R.id.edt_qltindang_show_thongtin_tindang_hovaten);
        edtSdt = view.findViewById(R.id.edt_qltindang_show_thongtin_tindang_sdt);
        edtEmail = view.findViewById(R.id.edt_qltindang_show_thongtin_tindang_email);
        edtMaThongTinNhaTro = view.findViewById(R.id.edt_qltindang_show_thongtin_tindang_mathongtin);
        edtDiaChiChinh = view.findViewById(R.id.edt_qltindang_show_thongtin_tindang_diachiChinh);
        edtLoaiTro = view.findViewById(R.id.edt_qltindang_show_thongtin_tindang_loaitro);
        edtNoiThat = view.findViewById(R.id.edt_qltindang_show_thongtin_tindang_noithat);
        edtLoaiPhong = view.findViewById(R.id.edt_qltindang_show_thongtin_tindang_loaiphong);
        edtGiaDien = view.findViewById(R.id.edt_qltindang_show_thongtin_tindang_giadien);
        edtGiaNuoc = view.findViewById(R.id.edt_qltindang_show_thongtin_tindang_gianuoc);
        edtGiaInternet = view.findViewById(R.id.edt_qltindang_show_thongtin_tindang_giaInternet);

        recyclerViewTienIch = view.findViewById(R.id.recycleview_qltindang_show_thongtin_tindang_tienich);

        btnLuuThongTin = view.findViewById(R.id.btn_qltindang_show_thongtin_tindang_luuthongtin);
        btnThongTinTinDang = view.findViewById(R.id.btn_qltindang_show_thongtin_tindang_thongtin_tin_dang);
        btnThongTinNhaTro = view.findViewById(R.id.btn_qltindang_show_thongtin_tindang_thongtin_thongtin_nhatro);

        llayoutThongTinTinDang = view.findViewById(R.id.llayout_qltindang_show_thongtin_tindang_thongtin_tindang);
        llayoutThongTinNhaTro = view.findViewById(R.id.llayout_qltindang_show_thongtin_tindang_thongtin_nhatro);

        bottomNav = requireActivity().findViewById(R.id.bottom_nav_dangtin);
        db = AppDatabase.getInstance(getContext());
    }
    @SuppressLint("ResourceAsColor")
    private void selectloaiTro() {
        btnThongTinNhaTro.setOnClickListener(v -> {
            btnThongTinNhaTro.setBackgroundResource(R.drawable.customer_red_button_radius_30dp);
            btnThongTinNhaTro.setTextColor(getResources().getColor(R.color.white, null));
            btnThongTinTinDang.setTextColor(getResources().getColor(R.color.grey_text, null));
            btnThongTinTinDang.setBackgroundResource(R.drawable.customer_background_noborder_radius_30dp);
            llayoutThongTinNhaTro.setVisibility(View.VISIBLE);
            llayoutThongTinTinDang.setVisibility(View.GONE);
        });

        btnThongTinTinDang.setOnClickListener(v -> {
            btnThongTinTinDang.setBackgroundResource(R.drawable.customer_red_button_radius_30dp);
            btnThongTinTinDang.setTextColor(getResources().getColor(R.color.white, null));
            btnThongTinNhaTro.setTextColor(getResources().getColor(R.color.grey_text, null));
            btnThongTinNhaTro.setBackgroundResource(R.drawable.customer_background_noborder_radius_30dp);
            llayoutThongTinTinDang.setVisibility(View.VISIBLE);
            llayoutThongTinNhaTro.setVisibility(View.GONE);
        });
    }

    @SuppressLint("SetTextI18n")
    private void Toolbar() {
    imgbtnBackStack.setOnClickListener(v -> {
        Navigation.findNavController(v).popBackStack();
        bottomNav.setVisibility(View.VISIBLE);
    });
    txtToolBar.setText("Thông tin tin đăng");
    }
    private void adjustSoNguoiO(int delta, ImageButton btnMinus, ImageButton btnPlus) {
        String soNguoiOStr = edtSoNguoiO.getText().toString().trim();
        int soNguoiO = soNguoiOStr.isEmpty() ? 0 : Integer.parseInt(soNguoiOStr);
        soNguoiO = Math.max(0, Math.min(10, soNguoiO + delta));
        edtSoNguoiO.setText(String.valueOf(soNguoiO));
        btnMinus.setEnabled(soNguoiO > 0);
        btnPlus.setEnabled(soNguoiO < 10);
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
                        // Chuyển thành số
                        long parsed = Long.parseLong(cleanString);
                        // Định dạng số với dấu phẩy
                        DecimalFormat formatter = new DecimalFormat("#,###");
                        String formatted = formatter.format(parsed);
                        current = formatted;

                        // Cập nhật EditText và TextView
                        edtGiaThue.setText(formatted);
                        edtGiaThue.setSelection(formatted.length()); // Đặt con trỏ trước " VNĐ"
                    }

                    // Gắn lại TextWatcher
                    edtGiaThue.addTextChangedListener(this);
                }
            }
        });
    }
    private void loadThongTinTinDang_ThongTinNhaTro() {
        if (getArguments() != null) {
            ma_TinDang = getArguments().getInt("ma_TinDang");
        }
        if (ma_TinDang == 0) {
            Navigation.findNavController(view).popBackStack();
            Toast.makeText(requireContext(), "Không tìm thấy tin đăng", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(()->{
            TinDangDao tinDangDao = db.tinDangDao();
            TinDang tinDang = tinDangDao.getTinDangById(ma_TinDang);
            ThongTinNhaTroDao thongTinNhaTroDao = db.thongTinNhaTroDao();
            ThongTinNhaTro thongTinNhaTro = thongTinNhaTroDao.getThongTinNhaTro_by_TinDangId(ma_TinDang);
            requireActivity().runOnUiThread(()->{
                edtMaTinDang.setText(String.valueOf(tinDang.getTin_dang_id()));
                edtTieuDe.setText(tinDang.getTieu_de());
                edtMoTa.setText(tinDang.getMo_ta());
                edtDiaChi.setText(tinDang.getDia_chi());
                edtDienTich.setText((tinDang.getDienTich()));
                edtGiaThue.setText((tinDang.getGiaThue()));
                edtSoNguoiO.setText((tinDang.getSo_Nguoi_O()));
                edtHoVaTen.setText(tinDang.getHo_va_Ten());
                edtSdt.setText(tinDang.getSdt());
                edtEmail.setText(tinDang.getEmail());

                edtMaThongTinNhaTro.setText(String.valueOf(thongTinNhaTro.getThongtin_nhatro_id()));
                edtDiaChiChinh.setText(thongTinNhaTro.getDiaChi());
                edtLoaiTro.setText(thongTinNhaTro.getLoaiTro());
                edtNoiThat.setText(thongTinNhaTro.getNoiThat());
                edtLoaiPhong.setText((thongTinNhaTro.getLoaiPhong()));
                edtGiaDien.setText((thongTinNhaTro.getGiaDien()));
                edtGiaNuoc.setText((thongTinNhaTro.getGiaNuoc()));
                edtGiaInternet.setText(thongTinNhaTro.getGiaInternet());
            });
        }).start();
    }
    private void updateTinDang_ThongTinNhaTro(){
        new Thread(()->{
            TinDangDao tinDangDao = db.tinDangDao();
            TinDang tinDang = tinDangDao.getTinDangById(ma_TinDang);
            ThongTinNhaTroDao thongTinNhaTroDao = db.thongTinNhaTroDao();
            ThongTinNhaTro thongTinNhaTro = thongTinNhaTroDao.getThongTinNhaTro_by_TinDangId(ma_TinDang);
            if (tinDang !=null){
                tinDang.setTieu_de(edtTieuDe.getText().toString().trim());
                tinDang.setMo_ta(edtMoTa.getText().toString().trim());
                tinDang.setDia_chi(edtDiaChi.getText().toString().trim());
                tinDang.setDienTich((edtDienTich.getText().toString().trim()));
                tinDang.setGiaThue((edtGiaThue.getText().toString().trim()));
                tinDang.setSo_Nguoi_O((edtSoNguoiO.getText().toString().trim()));
                tinDang.setHo_va_Ten(edtHoVaTen.getText().toString().trim());
                tinDang.setSdt(edtSdt.getText().toString().trim());
                tinDang.setEmail(edtEmail.getText().toString().trim());
                tinDangDao.update(tinDang);

                thongTinNhaTro.setDiaChi(edtDiaChiChinh.getText().toString().trim());
//                thongTinNhaTro.setLoaiTro(edtLoaiTro.getText().toString().trim());
//                thongTinNhaTro.setNoiThat(edtNoiThat.getText().toString().trim());
//                thongTinNhaTro.setLoaiPhong(edtLoaiPhong.getText().toString().trim());
                thongTinNhaTro.setGiaDien(edtGiaDien.getText().toString().trim());
                thongTinNhaTro.setGiaNuoc(edtGiaNuoc.getText().toString().trim());
                thongTinNhaTro.setGiaInternet(edtGiaInternet.getText().toString().trim());
                thongTinNhaTroDao.update(thongTinNhaTro);
            }

        }).start();
    }
    private void loadTienIch() {
        new Thread(() -> {
            TinDangDao tinDangDao = db.tinDangDao();
            TienIchDao tienIchDao = db.tienIchDao();
            TinDang tinDang = tinDangDao.getTinDangById(ma_TinDang);
            List<TienIch> tienIchList = new ArrayList<>();
            if (tinDang != null && tinDang.getId_tien_ich() != null && !tinDang.getId_tien_ich().isEmpty()) {
                String[] maTienIchArray = tinDang.getId_tien_ich().split(",");
                for (String maTienIch : maTienIchArray) {
                    String trimmedMa = maTienIch.trim();
                    TienIch tienIch = tienIchDao.getTienIchByMa(trimmedMa);
                    Log.d("TienIch", "Querying maTienIch: " + trimmedMa + ", Result: " + (tienIch != null ? tienIch.getTenTienIch() : "null"));
                    if (tienIch != null) {
                        tienIch.setSelected(true);
                        tienIchList.add(tienIch);
                    }
                }
            }
            requireActivity().runOnUiThread(() -> {
                DangTinMoi_TienIchAdapter adapter = new DangTinMoi_TienIchAdapter(tienIchList);
                recyclerViewTienIch.setLayoutManager(new GridLayoutManager(requireContext(), 3));
                recyclerViewTienIch.setAdapter(adapter);
                Log.d("TienIch", "Loaded " + tienIchList.size() + " items, Id_tien_ich: " + (tinDang != null ? tinDang.getId_tien_ich() : "null"));
            });
        }).start();
    }
}

