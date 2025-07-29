package Fg_TimKiem;

import android.annotation.SuppressLint;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_chothue_ql_tro.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import Adapter.TimKiem_Filter;
import Adapter.TimKiem_Show_TinDang;
import Data.AppDatabase;
import Data.ENTITY.TinDang;

public class TimKiemFg extends Fragment {
    private View view;
    private EditText edtSearch;
    private RecyclerView recycleviewFilter, recyclerViewShowTinDang;
    private TextView txtSoLuongPhongTro;
    private Button btnFilter;
    private TimKiem_Filter timKiemFilterAdapter;
    private List<Model.TimKiem_Filter> mFilter;
    private BottomNavigationView bottomNav;
    private AppDatabase db;
    private List<TinDang> tinDangList;
    private TimKiem_Show_TinDang timKiemShowTinDangAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_timkiem, container,false);
        anhXaView();
        loadAdapterFilter();
        loadAdapterShowTinDang();
        // Tìm kiếm
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (timKiemShowTinDangAdapter != null) {
                    timKiemShowTinDangAdapter.filter(s.toString());
                }
            }
        });
        btnFilter.setOnClickListener(v -> {showSortBottomSheet();});
        return view;
    }

    private void anhXaView() {
        edtSearch = view.findViewById(R.id.edt_timkiem_search);
        recycleviewFilter = view.findViewById(R.id.recycleview_timkiem_filter);
        txtSoLuongPhongTro = view.findViewById(R.id.txt_timkiem_tongphongtro);
        btnFilter = view.findViewById(R.id.btn_timkiem_sapxep);
        bottomNav = recycleviewFilter.findViewById(R.id.bottom_nav_dangtin);
        recyclerViewShowTinDang = view.findViewById(R.id.recycleview_timkiem_show);
        db = AppDatabase.getInstance(getContext());
    }

    private void loadAdapterFilter(){
        mFilter = new ArrayList<>(); //Holer phải tồn tại thì Adapter mới hiển thị được
        Model.TimKiem_Filter filter_search_modle1 = new Model.TimKiem_Filter(1,"Loại phòng", true);
        Model.TimKiem_Filter filter_search_modle2 = new Model.TimKiem_Filter(2,"Khoảng giá", true);
        Model.TimKiem_Filter filter_search_modle3 = new Model.TimKiem_Filter(3,"Diện tích", true);
        Model.TimKiem_Filter filter_search_modle4 = new Model.TimKiem_Filter(4,"Khu vực", true);
        mFilter.add(filter_search_modle1);
        mFilter.add(filter_search_modle2);
        mFilter.add(filter_search_modle3);
        mFilter.add(filter_search_modle4);
        timKiemFilterAdapter = new TimKiem_Filter(mFilter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recycleviewFilter.setLayoutManager(linearLayoutManager);
        recycleviewFilter.setAdapter(timKiemFilterAdapter);
        //RecycleView Filter Search//
    }
    @SuppressLint("DefaultLocale")
    private void loadAdapterShowTinDang() {
        recyclerViewShowTinDang.setLayoutManager(new LinearLayoutManager(requireContext()));
        new Thread(() -> {
            tinDangList = db.tinDangDao().getAllTinDang();
            requireActivity().runOnUiThread(() -> {
                timKiemShowTinDangAdapter = new TimKiem_Show_TinDang(tinDangList, db);
                recyclerViewShowTinDang.setAdapter(timKiemShowTinDangAdapter);
                txtSoLuongPhongTro.setText(String.format("Tổng số phòng trọ: %d", tinDangList.size()));
            });
        }).start();
    }
    private void showSortBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        View bottomSheetView = LayoutInflater.from(requireContext()).inflate(R.layout.bottomsheet_timkiem_filter, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        // Điều chỉnh chiều cao BottomSheet
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        int peekHeight = (int) (600 * requireContext().getResources().getDisplayMetrics().density);
        behavior.setPeekHeight(peekHeight);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED); // Mở rộng hoàn toàn khi hiển thị

        // Ánh xạ các TextView trong BottomSheet
        TextView sortPriceAsc = bottomSheetView.findViewById(R.id.sort_price_asc);
        TextView sortPriceDesc = bottomSheetView.findViewById(R.id.sort_price_desc);
        TextView sortDateNewest = bottomSheetView.findViewById(R.id.sort_date_newest);
        TextView sortDateOldest = bottomSheetView.findViewById(R.id.sort_date_oldest);
        TextView sortAreaAsc = bottomSheetView.findViewById(R.id.sort_area_asc);
        TextView sortAreaDesc = bottomSheetView.findViewById(R.id.sort_area_desc);

        // Xử lý sự kiện nhấn các tùy chọn sắp xếp
        sortPriceAsc.setOnClickListener(v -> {
            timKiemShowTinDangAdapter.sort("price_asc");
            bottomSheetDialog.dismiss();
        });
        sortPriceDesc.setOnClickListener(v -> {
            timKiemShowTinDangAdapter.sort("price_desc");
            bottomSheetDialog.dismiss();
        });
        sortDateNewest.setOnClickListener(v -> {
            timKiemShowTinDangAdapter.sort("date_newest");
            bottomSheetDialog.dismiss();
        });
        sortDateOldest.setOnClickListener(v -> {
            timKiemShowTinDangAdapter.sort("date_oldest");
            bottomSheetDialog.dismiss();
        });
        sortAreaAsc.setOnClickListener(v -> {
            timKiemShowTinDangAdapter.sort("area_asc");
            bottomSheetDialog.dismiss();
        });
        sortAreaDesc.setOnClickListener(v -> {
            timKiemShowTinDangAdapter.sort("area_desc");
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }
}
