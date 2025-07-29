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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.app_chothue_ql_tro.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import Data.AppDatabase;

public class DatCocTruocFg extends Fragment {
    private View view;
    private ImageButton imgtbtnBackStack;
    private Button btnQuayLai, btnTiepTuc;
    private TextView txtToolbar, txtDiaChi, txtTienThue, txtTienCoc, txtNgayDatCoc, txtHoTen, txtSDT;
    private EditText edtNgayLienHe;
    private BottomNavigationView bottomNav_DangTin, bottomNav_TimKiem;
    private AppDatabase db;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_datcoctruoc, container,false);
        anhXaView();
        setToolbar();
        btnQuayLai.setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();
        });
        btnTiepTuc.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_navigation_datcoctruoc_to_navigation_phuongthucthanhtoan);
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
    @SuppressLint("SetTextI18n")
    private void setToolbar(){
        imgtbtnBackStack.setVisibility(View.GONE);
        txtToolbar.setText("Đặt cọc trước");
    }
}
