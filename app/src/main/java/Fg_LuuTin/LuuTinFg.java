package Fg_LuuTin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_chothue_ql_tro.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.LuuTin_Show;
import Data.AppDatabase; // Import AppDatabase
import Data.ENTITY.TinDang; // Import TinDang

public class LuuTinFg extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private LuuTin_Show luuTinAdapter;
    private List<TinDang> listLuuTin;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_luutin, container, false);

        db = AppDatabase.getInstance(getContext()); // Khởi tạo database

        anhXaView();
        setupRecyclerView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Tải lại dữ liệu mỗi khi fragment được hiển thị
        loadSavedPosts();
    }

    private void anhXaView() {
        recyclerView = view.findViewById(R.id.recycleview_luutin_show);
    }

    private void setupRecyclerView() {
        listLuuTin = new ArrayList<>();
        luuTinAdapter = new LuuTin_Show(listLuuTin, db); // Truyền db vào adapter

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(luuTinAdapter);
    }

    private void loadSavedPosts() {
        new Thread(() -> {
            // Lấy danh sách tin đã lưu từ database
            List<TinDang> savedPosts = db.tinDangDao().getSavedTinDang();
            // Cập nhật UI trên Main Thread
            if(getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    luuTinAdapter.setData(savedPosts); // Dùng phương thức setData để cập nhật
                });
            }
        }).start();
    }
}