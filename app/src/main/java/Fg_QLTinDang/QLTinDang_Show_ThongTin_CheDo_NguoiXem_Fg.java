package Fg_QLTinDang;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app_chothue_ql_tro.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import Adapter.TinDang_Show_DanhSachAnh;
import Data.AppDatabase;
import Data.ENTITY.DanhSachAnh;
import Data.ENTITY.TinDang;
import de.hdodenhof.circleimageview.CircleImageView;

public class QLTinDang_Show_ThongTin_CheDo_NguoiXem_Fg extends Fragment {
    private View view;
    private BottomNavigationView bottomNav;
    private AppDatabase db;
    private RecyclerView recyclerViewAnh;
    private TextView txtTieuDe, txtGiaThue, txtDienTich, txtSoNguoiO, txtDiaChi, txtHovaTen, txtNgayDang;
    private Button btnXemChiTiet, btnThoat;
    private ImageButton imgbtnLuuTin;
    private CircleImageView avatar;
    private Uri uriAvatar;
    private int ma_TinDang;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_qltindang_show_chedo_nguoixem, container, false);
        anhXaView();

        // Lấy ma_TinDang từ Bundle
        if (getArguments() != null) {
            ma_TinDang = getArguments().getInt("ma_TinDang");
        }

        // Tải thông tin tin đăng và ảnh
        loadTinDangData();
        loadImages();
        btnThoat.setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();
        });
        return view;
    }

    @SuppressLint("WrongViewCast")
    private void anhXaView() {
        txtTieuDe = view.findViewById(R.id.txt_qltindang_show_tieude);
        txtGiaThue = view.findViewById(R.id.txt_recycleview_qltindang_giatien);
        txtDienTich = view.findViewById(R.id.txt_recycleview_qltindang_dientich);
        txtSoNguoiO = view.findViewById(R.id.txt_qltindang_show_songuoio);
        txtDiaChi = view.findViewById(R.id.txt_recycleview_qltindang_diachihienthi);
        txtHovaTen = view.findViewById(R.id.txt_qltindang_show_username);
        txtNgayDang = view.findViewById(R.id.txt_qltindang_show_ngaydang);
        btnXemChiTiet = view.findViewById(R.id.btn_qltindang_show_xemchitiet);
        btnThoat = view.findViewById(R.id.btn_qltindang_show_chedo_nguoixem_thoat);
        imgbtnLuuTin = view.findViewById(R.id.imgbtn_qltindang_show_luutin);
        avatar = view.findViewById(R.id.img_qltindang_show_avatar);
        recyclerViewAnh = view.findViewById(R.id.recycleview_qltindang_show_img);
        bottomNav = view.findViewById(R.id.bottom_nav_dangtin);
        db = AppDatabase.getInstance(requireContext());
    }

    private void loadTinDangData() {
        new Thread(() -> {
            TinDang tinDang = db.tinDangDao().getTinDangById(ma_TinDang); // Giả định có phương thức getTinDangById
            if (tinDang != null) {
                requireActivity().runOnUiThread(() -> {
                    txtTieuDe.setText(tinDang.getTieu_de());
                    txtGiaThue.setText(tinDang.getGiaThue());
                    txtDienTich.setText(tinDang.getDienTich());
                    txtSoNguoiO.setText(tinDang.getSo_Nguoi_O());
                    txtDiaChi.setText(tinDang.getDia_chi());
                    txtHovaTen.setText(tinDang.getHo_va_Ten());
                    txtNgayDang.setText(tinDang.getNgayDang() != null ? tinDang.getNgayDang() : "N/A");
                    if (tinDang.getAvatar() != null && !tinDang.getAvatar().isEmpty()) {
                        Glide.with(requireContext())
                                .load(tinDang.getAvatar())
                                .error(R.drawable.ic_user)
                                .into(avatar);
                    }
                });
            } else {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "Không tìm thấy tin đăng", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void loadImages() {
        new Thread(() -> {
            List<DanhSachAnh> images = db.danhSachAnhDao().getImagesByPostId(ma_TinDang);
            List<String> imageUrls = new ArrayList<>();
            for (DanhSachAnh image : images) {
                imageUrls.add(image.getImageUrl());
            }
            requireActivity().runOnUiThread(() -> {
                if (imageUrls.isEmpty()) {
                    Toast.makeText(requireContext(), "Không có ảnh nào để hiển thị", Toast.LENGTH_SHORT).show();
                }
                recyclerViewAnh.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
                TinDang_Show_DanhSachAnh adapter = new TinDang_Show_DanhSachAnh(imageUrls);
                recyclerViewAnh.setAdapter(adapter);
            });
        }).start();
    }
}