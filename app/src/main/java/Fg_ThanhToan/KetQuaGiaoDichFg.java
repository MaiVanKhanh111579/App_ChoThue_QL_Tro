package Fg_ThanhToan;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.app_chothue_ql_tro.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

public class KetQuaGiaoDichFg extends Fragment {
    private View view;
    private Button btnVeTrangChu;
    private TextView txtKetQua, txtTien, txtGhiNhanTin, txtLoaiThanhToan;
    private ImageView imgKetQuaGiaoDich;
    private BottomNavigationView bottomNav;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_ketquagiaodich, container,false);
        anhXaView();

        return view;
    }

    @SuppressLint("CutPasteId")
    private void anhXaView() {
        txtKetQua = view.findViewById(R.id.txt_ketquagiaodich_ketqua);
        txtTien = view.findViewById(R.id.txt_ketquagiaodich_tien);
        txtLoaiThanhToan = view.findViewById(R.id.txt_ketquagiaodich_loaiThanhToan);

        imgKetQuaGiaoDich = view.findViewById(R.id.img_ketquagiaodich);

        btnVeTrangChu = view.findViewById(R.id.btn_ketquagiaodich_trangchu);
    }
}
