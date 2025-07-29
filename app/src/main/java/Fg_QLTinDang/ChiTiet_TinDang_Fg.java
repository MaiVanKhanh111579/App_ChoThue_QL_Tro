package Fg_QLTinDang;

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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import Adapter.TinDang_Show_DanhSachAnh;
import Data.AppDatabase;
import Data.ENTITY.DanhSachAnh;
import Data.ENTITY.ThongTinNhaTro;
import Data.ENTITY.TinDang;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChiTiet_TinDang_Fg extends Fragment {
    private View view;
    private BottomNavigationView bottomNav;
    private AppDatabase db;
    private RecyclerView recyclerviewDanhSachAnh, recyclerviewTienIch;
    private ImageButton imgbtnBackStack, imgbtnCall;
    private Button btnDatCocTruoc;
    private TextView txtTieude, txtDiachi, txtTienThue, txtTienCoc, txtHoVaTen, txtSDT, txtMota,
    txtTienDien, txtTienNuoc, txtTienInternet, txtTinhTrang, txtDienTich, txtSoNguoiO;
    private int ma_TinDang;
    private CircleImageView avatar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_chitiet_tindang, container,false);
        anhXaView();
        bottomNav.setVisibility(View.GONE);
        loadThongTint();
        loadImages();
        imgbtnBackStack.setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();
            bottomNav.setVisibility(View.VISIBLE);
        });
        btnDatCocTruoc.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_navigation_chitiet_tindang_to_navigation_datcoctruoc);
            bottomNav.setVisibility(View.GONE);
        });
        return view;
    }

    private void anhXaView() {
        recyclerviewDanhSachAnh = view.findViewById(R.id.recycleview_chitiet_tindang_danhsachanh);
        recyclerviewTienIch = view.findViewById(R.id.recycleview_chitiet_tindang_tienich);
        imgbtnBackStack = view.findViewById(R.id.imgbtn_chitiet_tindang);
        btnDatCocTruoc = view.findViewById(R.id.btn_chitiet_tindang_datcoctruoc);
        imgbtnCall = view.findViewById(R.id.imgbtn_chitiet_tindang_call);
        avatar = view.findViewById(R.id.img_chitiet_tindang_avatar);
        txtTieude = view.findViewById(R.id.txt_chitiet_tindang_tieude);
        txtDiachi = view.findViewById(R.id.txt_chitiet_tindang_diachi);
        txtTienThue = view.findViewById(R.id.txt_chitiet_tindang_tienthue);
        txtTienCoc = view.findViewById(R.id.txt_chitiet_tindang_tiencoc);
        txtHoVaTen = view.findViewById(R.id.txt_chitiet_tindang_hovaTen);
        txtSDT = view.findViewById(R.id.txt_chitiet_tindang_sdt);
        txtMota = view.findViewById(R.id.txt_chitiet_tindang_mota);
        txtTienDien = view.findViewById(R.id.txt_chitiet_tindang_tiendien);
        txtTienNuoc = view.findViewById(R.id.txt_chitiet_tindang_tiennuoc);
        txtTienInternet = view.findViewById(R.id.txt_chitiet_tindang_tieninternet);
        txtTinhTrang = view.findViewById(R.id.txt_chitiet_tindang_tinhtrang);
        txtDienTich = view.findViewById(R.id.txt_chitiet_tindang_dientich);
        txtSoNguoiO = view.findViewById(R.id.txt_chitiet_tindang_songuoio);
        bottomNav = requireActivity().findViewById(R.id.bottom_nav_timkiem);
        db = AppDatabase.getInstance(requireContext());
    }
    private void loadThongTint(){
        if (getArguments() != null) {
            ma_TinDang = getArguments().getInt("ma_TinDang");
        }
        new Thread(() -> {
            TinDang tinDang = db.tinDangDao().getTinDangById(ma_TinDang);
            ThongTinNhaTro thongTinNhaTro = db.thongTinNhaTroDao().getThongTinNhaTro_by_TinDangId(ma_TinDang);
            if (tinDang != null) {
                requireActivity().runOnUiThread(() -> {
                    txtTieude.setText(tinDang.getTieu_de());
                    txtTienThue.setText(tinDang.getGiaThue());
                    txtTienCoc.setText(tinDang.getGiaThue());
                    txtDienTich.setText(tinDang.getDienTich());
                    txtSoNguoiO.setText(tinDang.getSo_Nguoi_O());
                    txtDiachi.setText(tinDang.getDia_chi());
                    txtHoVaTen.setText(tinDang.getHo_va_Ten());
                    txtSDT.setText(tinDang.getSdt());
                    txtMota.setText(tinDang.getMo_ta());
                    txtTienDien.setText(thongTinNhaTro.getGiaDien());
                    txtTienNuoc.setText(thongTinNhaTro.getGiaNuoc());
                    txtTienInternet.setText(thongTinNhaTro.getGiaInternet());

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
                recyclerviewDanhSachAnh.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
                TinDang_Show_DanhSachAnh adapter = new TinDang_Show_DanhSachAnh(imageUrls);
                recyclerviewDanhSachAnh.setAdapter(adapter);
            });
        }).start();
    }
    private void loadTienIch(){

    }
}
