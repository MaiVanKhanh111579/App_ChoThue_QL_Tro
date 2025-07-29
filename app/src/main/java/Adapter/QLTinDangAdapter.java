package Adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_chothue_ql_tro.R;

import java.util.ArrayList;
import java.util.List;

import Data.ENTITY.TinDang;

public class QLTinDangAdapter extends RecyclerView.Adapter<QLTinDangAdapter.TinDangViewHolder> {
    private List<TinDang> tinDangList;
    private List<TinDang> filteredList;

    public QLTinDangAdapter(List<TinDang> tinDangList) {
        this.tinDangList = tinDangList;
        this.filteredList = new ArrayList<>(tinDangList);
    }

    @NonNull
    @Override
    public TinDangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_qltindang, parent, false);
        return new TinDangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TinDangViewHolder holder, int position) {
        TinDang tinDang = filteredList.get(position);
        holder.txtMaTinDang.setText(String.valueOf(tinDang.getTin_dang_id()));
        holder.txtTieuDe.setText(tinDang.getTieu_de());
        holder.txtNgayDang.setText(tinDang.getNgayDang() != null ? tinDang.getNgayDang() : "N/A");

        // Xử lý CheckBox
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(tinDang.getSelected());
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            tinDang.setSelected(isChecked);
        });

        // Xử lý ImageButton
        holder.imgBtnArrowNext.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("ma_TinDang", tinDang.getTin_dang_id());
            Navigation.findNavController(v).navigate(R.id.action_navigation_qltindang_to_navigation_qltindang_show_thongtin_tindang, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return filteredList != null ? filteredList.size() : 0;
    }

    // Lấy danh sách tin đăng được chọn
    public List<TinDang> getSelectedTinDang() {
        List<TinDang> selectedList = new ArrayList<>();
        for (TinDang tinDang : tinDangList) {
            if (tinDang.getSelected()) {
                selectedList.add(tinDang);
            }
        }
        return selectedList;
    }

    // Cập nhật trạng thái CheckBox cho tất cả
    public void setAllChecked(boolean isChecked) {
        for (TinDang tinDang : tinDangList) {
            tinDang.setSelected(isChecked);
        }
        filteredList = new ArrayList<>(tinDangList);
        notifyDataSetChanged();
    }

    // Tìm kiếm
    public void filter(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(tinDangList);
        } else {
            query = query.toLowerCase();
            for (TinDang tinDang : tinDangList) {
                if (String.valueOf(tinDang.getTin_dang_id()).contains(query) ||
                        tinDang.getTieu_de().toLowerCase().contains(query) ||
                        (tinDang.getNgayDang() != null && tinDang.getNgayDang().toLowerCase().contains(query))) {
                    filteredList.add(tinDang);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class TinDangViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;
        private TextView txtMaTinDang, txtNgayDang, txtTieuDe;
        private ImageButton imgBtnArrowNext;

        public TinDangViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox_recycleview_qltindang_select);
            txtMaTinDang = itemView.findViewById(R.id.txt_recycleview_qltindang_maTinDang);
            txtNgayDang = itemView.findViewById(R.id.txt_recycleview_qltindang_hoVaTen);
            txtTieuDe = itemView.findViewById(R.id.txt_recycleview_qltindang_tieuDe);
            imgBtnArrowNext = itemView.findViewById(R.id.imgbtn_recycleview_qltindang_arrowNext);
        }
    }
}