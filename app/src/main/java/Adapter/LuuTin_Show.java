package Adapter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app_chothue_ql_tro.R;

import java.io.File;
import java.util.List;

import Data.AppDatabase;
import Data.ENTITY.DanhSachAnh;
import Data.ENTITY.TinDang;

// Thay đổi Model thành TinDang
public class LuuTin_Show extends RecyclerView.Adapter<LuuTin_Show.LuuTin_Show_ViewHolder> {
    private List<TinDang> mlist;
    private AppDatabase db;

    public LuuTin_Show(List<TinDang> mlist, AppDatabase db) {
        this.mlist = mlist;
        this.db = db;
    }

    // Phương thức để cập nhật dữ liệu khi cần
    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<TinDang> newList) {
        this.mlist = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LuuTin_Show_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_luutin_item, parent, false);
        return new LuuTin_Show_ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LuuTin_Show_ViewHolder holder, int position) {
        TinDang item = mlist.get(position);
        if (item == null) {
            return;
        }

        // Gán dữ liệu từ đối tượng TinDang
        holder.txtTieuDe.setText(item.getTieu_de());
        holder.txtGiaTien.setText(item.getGiaThue() + " VNĐ");
        holder.txtDienTich.setText(item.getDienTich() + " m²");
        holder.txtDiaChi.setText(item.getDia_chi());
        holder.txtNgayDang.setText(item.getNgayDang());

        // Tải ảnh đầu tiên của tin đăng (lấy từ bảng DanhSachAnh)
        new Thread(() -> {
            List<DanhSachAnh> images = db.danhSachAnhDao().getImagesByPostId(item.getTin_dang_id());
            if (images != null && !images.isEmpty()) {
                // 3. Lấy URL (String) từ đối tượng ảnh đầu tiên
                String firstImageUrl = images.get(0).getImageUrl();

                // 4. Tải ảnh lên UI bằng Glide
                holder.itemView.post(() -> {
                    Glide.with(holder.itemView.getContext())
                            .load(new File(firstImageUrl))
                            .error(R.drawable.customer_background_noborder_radius_10dp) // Ảnh khi có lỗi
                            .into(holder.imgView);
                });
            } else {
                // Xử lý khi không có ảnh nào
                holder.itemView.post(() -> holder.imgView.setImageResource(R.drawable.customer_background_noborder_radius_10dp));
            }
        }).start();


        holder.imgbtnLuuTin.setImageResource(R.drawable.ic_heart_red);

        holder.imgbtnLuuTin.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                // Lấy tin đăng cần bỏ lưu
                TinDang tinToUnsave = mlist.get(currentPosition);
                tinToUnsave.setLuuTin(false); // Cập nhật trạng thái

                // Cập nhật vào database trên một thread khác
                new Thread(() -> {
                    db.tinDangDao().update(tinToUnsave);
                    // Quay lại Main thread để cập nhật UI
                    holder.itemView.post(() -> {
                        // Xóa item khỏi danh sách của adapter
                        mlist.remove(currentPosition);
                        // Thông báo cho adapter để cập nhật RecyclerView
                        notifyItemRemoved(currentPosition);
                    });
                }).start();
            }
        });

        holder.layout.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("ma_TinDang", item.getTin_dang_id());
            Navigation.findNavController(v).navigate(R.id.action_navigation_luutin_to_navigation_chitiet_tindang, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return mlist != null ? mlist.size() : 0;
    }

    public class LuuTin_Show_ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTieuDe, txtGiaTien, txtDienTich, txtDiaChi, txtNgayDang;
        private ImageView imgView;
        private ImageButton imgbtnLuuTin;
        private LinearLayout layout;

        public LuuTin_Show_ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTieuDe = itemView.findViewById(R.id.txt_recycleview_luutin_tieude);
            txtGiaTien = itemView.findViewById(R.id.txt_recycleview_luutin_giaTien);
            txtDienTich = itemView.findViewById(R.id.txt_recycleview_luutin_dienTich);
            txtDiaChi = itemView.findViewById(R.id.txt_recycleview_luutin_diaChi);
            imgView = itemView.findViewById(R.id.img_recycleview_luutin_hinhanh);
            txtNgayDang = itemView.findViewById(R.id.txt_recycleview_luutin_ngaydang);
            imgbtnLuuTin = itemView.findViewById(R.id.imgbtn_recycleview_luutin_luutin);
            layout = itemView.findViewById(R.id.recycleview_luutin_item);
        }
    }
}