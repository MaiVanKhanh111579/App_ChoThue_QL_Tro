package Fg_KhachHang;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_chothue_ql_tro.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import Data.AppDatabase;

public class KhachHang_ThongBaoFg extends Fragment {
    private View view;
    private Button btnThoat;
    private RecyclerView recyclerView;
    private BottomNavigationView bottomNav;
    private AppDatabase db;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_khachhang_thongbao, container,false);
        anhXaView();
        btnThoat.setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack(); 
            bottomNav.setVisibility(View.VISIBLE);
        });
        return view;
    }

    private void anhXaView() {
        btnThoat = view.findViewById(R.id.btn_khachhang_thongbao_thoat);
        recyclerView = view.findViewById(R.id.recycleview_khachhang_thongbao);
        bottomNav = requireActivity().findViewById(R.id.bottom_nav_dangtin);
        db  = AppDatabase.getInstance(requireContext());

    }
}
