package Fg_QLTinDang;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_chothue_ql_tro.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.util.List;

import Adapter.QLTinDangAdapter;
import Data.AppDatabase;
import Data.DAO.DanhSachAnhDao;
import Data.ENTITY.DanhSachAnh;
import Data.ENTITY.TinDang;

public class QLTinDang extends Fragment {
    private View view;
    private ImageButton imgbtn_add, imgbtn_filter, imgbtn_delete;
    private BottomNavigationView bottomNav;
    private AppDatabase db;
    private RecyclerView recyclerView;
    private QLTinDangAdapter adapter;
    private EditText edtSearch;
    private CheckBox checkBoxAll;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_qltindang, container, false);
        anhXaView();

        // Thêm tin đăng
        imgbtn_add.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_navigation_qltindang_to_navigation_dangtinmoi);
            bottomNav.setVisibility(View.GONE);
        });

        // Chọn tất cả
        checkBoxAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (adapter != null) {
                adapter.setAllChecked(isChecked);
            }
        });

        // Tìm kiếm
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (adapter != null) {
                    adapter.filter(s.toString());
                }
            }
        });

        // Xóa tin đăng
        imgbtn_delete.setOnClickListener(v -> {
            if (adapter != null) {
                List<TinDang> selectedTinDang = adapter.getSelectedTinDang();
                if (selectedTinDang.isEmpty()) {
                    Toast.makeText(requireContext(), "Vui lòng chọn ít nhất một tin để xóa", Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(() -> {
                    DanhSachAnhDao danhSachAnhDao = db.danhSachAnhDao();
                    for (TinDang tinDang : selectedTinDang) {
                        // Xóa ảnh liên quan
                        List<DanhSachAnh> images = danhSachAnhDao.getImagesByPostId(tinDang.getTin_dang_id());
                        for (DanhSachAnh image : images) {
                            File file = new File(image.getImageUrl());
                            if (file.exists()) {
                                file.delete();
                            }
                        }
                        danhSachAnhDao.deleteDanhSachAnh_by_TinDangId(tinDang.getTin_dang_id());
                        // Xóa tin đăng
                        db.tinDangDao().deleteTinDang(tinDang);
                    }
                    // Tải lại danh sách
                    List<TinDang> updatedList = db.tinDangDao().getAllTinDang();
                    requireActivity().runOnUiThread(() -> {
                        adapter = new QLTinDangAdapter(updatedList);
                        recyclerView.setAdapter(adapter);
                        checkBoxAll.setChecked(false);
                        Toast.makeText(requireContext(), "Xóa tin thành công", Toast.LENGTH_SHORT).show();
                    });
                }).start();
            }
        });

        loadTinDang();
        return view;
    }

    private void anhXaView() {
        bottomNav = requireActivity().findViewById(R.id.bottom_nav_dangtin);
        imgbtn_add = view.findViewById(R.id.imgbtn_qltindang_add);
        imgbtn_delete = view.findViewById(R.id.imgbtn_qltindang_delete);
        imgbtn_filter = view.findViewById(R.id.imgbtn_qltindang_filter);
        recyclerView = view.findViewById(R.id.recycleview_qltindang_show_tindang);
        edtSearch = view.findViewById(R.id.edt_qltindang_search);
        checkBoxAll = view.findViewById(R.id.checkbox_qltindang_all);
        db = AppDatabase.getInstance(requireContext());
    }

    private void loadTinDang() {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        new Thread(() -> {
            List<TinDang> tinDangList = db.tinDangDao().getAllTinDang();
            requireActivity().runOnUiThread(() -> {
                adapter = new QLTinDangAdapter(tinDangList);
                recyclerView.setAdapter(adapter);
            });
        }).start();
    }
}