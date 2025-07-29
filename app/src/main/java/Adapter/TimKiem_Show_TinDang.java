package Adapter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app_chothue_ql_tro.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Data.AppDatabase;
import Data.DAO.DanhSachAnhDao;
import Data.ENTITY.DanhSachAnh;
import Data.ENTITY.TinDang;
import de.hdodenhof.circleimageview.CircleImageView;

public class TimKiem_Show_TinDang extends RecyclerView.Adapter<TimKiem_Show_TinDang.TimKiem_Show_TinDang_ViewHolder> {
    private List<TinDang> tinDangList;
    private List<TinDang> filteredList;
    private AppDatabase db;

    public TimKiem_Show_TinDang(List<TinDang> tinDangList, AppDatabase db) {
        this.tinDangList = tinDangList;
        this.filteredList = new ArrayList<>(tinDangList);
        this.db = db;
    }

    @NonNull
    @Override
    public TimKiem_Show_TinDang_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_timkiem_show_tindang, parent, false);
        return new TimKiem_Show_TinDang_ViewHolder(view);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull TimKiem_Show_TinDang_ViewHolder holder, int position) {
        TinDang tinDang = filteredList.get(position);


        holder.txtTieuDe.setText(tinDang.getTieu_de());
        holder.txtGiaTien.setText(tinDang.getGiaThue() + " VNĐ/tháng");
        holder.txtDienTich.setText(tinDang.getDienTich() + " m²");
        holder.txtSoNguoiO.setText(tinDang.getSo_Nguoi_O());
        holder.txtDiaChi.setText(tinDang.getDia_chi());
        holder.txtUsername.setText(tinDang.getHo_va_Ten());
        holder.txtNgayDang.setText(tinDang.getNgayDang() != null ? tinDang.getNgayDang() : "N/A");


        // Tải avatar
        String avatarPath = tinDang.getAvatar();
        if (avatarPath != null && new File(avatarPath).exists()) {
            Glide.with(holder.itemView.getContext())
                    .load(new File(avatarPath))
                    .error(R.drawable.ic_user)
                    .into(holder.imgAvatar);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.ic_user)
                    .into(holder.imgAvatar);
        }

        // Tải danh sách ảnh
        List<String> imageUrls = new ArrayList<>();
        new Thread(() -> {
            DanhSachAnhDao danhSachAnhDao = db.danhSachAnhDao();
            List<DanhSachAnh> images = danhSachAnhDao.getImagesByPostId(tinDang.getTin_dang_id());
            for (DanhSachAnh image : images) {
                imageUrls.add(image.getImageUrl());
            }
            holder.itemView.post(() -> {
                TinDang_Show_DanhSachAnh imageAdapter = new TinDang_Show_DanhSachAnh(imageUrls);
                holder.recyclerViewImages.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
                holder.recyclerViewImages.setAdapter(imageAdapter);
            });
        }).start();

        // Xử lý nút Xem chi tiết
        holder.btnXemChiTiet.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("ma_TinDang", tinDang.getTin_dang_id());
            Navigation.findNavController(v).navigate(R.id.action_navigation_timkiem_to_navigation_chitiet_tindang, bundle);
        });

        // Xử lý nút Lưu tin
        holder.imgbtnLuuTin.setOnClickListener(v -> {
            boolean isSaved = !tinDang.getLuuTin();
            tinDang.setLuuTin(isSaved);
            new Thread(() -> {
                db.tinDangDao().update(tinDang);
                holder.itemView.post(() -> {
                    holder.imgbtnLuuTin.setImageResource(isSaved ? R.drawable.ic_heart_red : R.drawable.ic_heart);
                });
            }).start();
        });
        holder.imgbtnLuuTin.setImageResource(tinDang.getLuuTin() ? R.drawable.ic_heart_red : R.drawable.ic_heart);
    }

    @Override
    public int getItemCount() {
        return filteredList != null ? filteredList.size() : 0;
    }

    // Tìm kiếm
    @SuppressLint("NotifyDataSetChanged")
    public void filter(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(tinDangList);
        } else {
            query = query.trim().toLowerCase(); // Chuyển query thành chữ thường
            for (TinDang tinDang : tinDangList) {
                // Tìm kiếm theo tiêu đề
                if (tinDang.getTieu_de() != null && tinDang.getTieu_de().toLowerCase().contains(query)) {
                    filteredList.add(tinDang);
                    continue;
                }
                // Tìm kiếm theo địa chỉ
                if (tinDang.getDia_chi() != null && tinDang.getDia_chi().toLowerCase().contains(query)) {
                    filteredList.add(tinDang);
                    continue;
                }
                // Tìm kiếm theo số người ở
                if (tinDang.getSo_Nguoi_O() != null && tinDang.getSo_Nguoi_O().toLowerCase().contains(query)) {
                    filteredList.add(tinDang);
                    continue;
                }
                // Tìm kiếm theo giá tiền và diện tích (nếu query là số)
                try {
                    float queryNumber = Float.parseFloat(query.replaceAll("[^0-9.]", ""));
                    if (!tinDang.getGiaThue().isEmpty()) {
                        float gia = Float.parseFloat(tinDang.getGiaThue());
                        if (gia >= queryNumber * 0.8 && gia <= queryNumber * 1.2) { // ±20% khoảng
                            filteredList.add(tinDang);
                            continue;
                        }
                    }
                    // Diện tích
                    if (tinDang.getDienTich() != null) {
                        String dienTich = tinDang.getDienTich().replaceAll("[^0-9.]", "");
                        if (!dienTich.isEmpty() && Float.parseFloat(dienTich) == queryNumber) {
                            filteredList.add(tinDang);
                        }
                    }
                } catch (NumberFormatException e) {
                    // Bỏ qua nếu query không phải số
                }
            }
        }
        Log.d("TimKiem", "Query: " + query + ", Filtered items: " + filteredList.size());
        notifyDataSetChanged();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void sort(String sortType) {
        Collections.sort(filteredList, new Comparator<TinDang>() {
            @Override
            public int compare(TinDang t1, TinDang t2) {
                switch (sortType) {
                    case "price_asc":
                        float price1 = parseFloat(t1.getGiaThue());
                        float price2 = parseFloat(t2.getGiaThue());
                        return Float.compare(price1, price2);
                    case "price_desc":
                        price1 = parseFloat(t1.getGiaThue());
                        price2 = parseFloat(t2.getGiaThue());
                        return Float.compare(price2, price1);
                    case "date_newest":
                        Date date1 = parseDate(t1.getNgayDang());
                        Date date2 = parseDate(t2.getNgayDang());
                        return date2 != null && date1 != null ? date2.compareTo(date1) : 0;
                    case "date_oldest":
                        date1 = parseDate(t1.getNgayDang());
                        date2 = parseDate(t2.getNgayDang());
                        return date1 != null && date2 != null ? date1.compareTo(date2) : 0;
                    case "area_asc":
                        float area1 = parseFloat(t1.getDienTich());
                        float area2 = parseFloat(t2.getDienTich());
                        return Float.compare(area1, area2);
                    case "area_desc":
                        area1 = parseFloat(t1.getDienTich());
                        area2 = parseFloat(t2.getDienTich());
                        return Float.compare(area2, area1);
                    default:
                        return 0;
                }
            }

            private float parseFloat(String value) {
                try {
                    return value != null ? Float.parseFloat(value.replaceAll("[^0-9.]", "")) : 0f;
                } catch (NumberFormatException e) {
                    return 0f;
                }
            }

            private Date parseDate(String dateStr) {
                try {
                    return dateStr != null ? new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateStr) : null;
                } catch (ParseException e) {
                    return null;
                }
            }
        });
        notifyDataSetChanged();
        Log.d("TimKiem", "Sorted by: " + sortType + ", Items: " + filteredList.size());
    }


    public class TimKiem_Show_TinDang_ViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView recyclerViewImages;
        private TextView txtTieuDe, txtGiaTien, txtDienTich, txtSoNguoiO, txtDiaChi, txtUsername, txtNgayDang;
        private CircleImageView imgAvatar;
        private Button btnXemChiTiet;
        private ImageButton imgbtnLuuTin;
        public TimKiem_Show_TinDang_ViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerViewImages = itemView.findViewById(R.id.recycleview_recycleview_timkiem_show_img);
            txtTieuDe = itemView.findViewById(R.id.txt_recycleview_timkiem_show_tieude);
            txtGiaTien = itemView.findViewById(R.id.txt_recycleview_timkiem_giatien);
            txtDienTich = itemView.findViewById(R.id.txt_recycleview_timkiem_dientich);
            txtSoNguoiO = itemView.findViewById(R.id.txt_recycleview_timkiem_show_songuoio);
            txtDiaChi = itemView.findViewById(R.id.txt_recycleview_timkiem_diachihienthi);
            txtUsername = itemView.findViewById(R.id.txt_recycleview_timkiem_show_username);
            txtNgayDang = itemView.findViewById(R.id.txt_recycleview_timkiem_show_ngaydang);
            imgAvatar = itemView.findViewById(R.id.img_recycleview_timkiem_show_avatar);
            btnXemChiTiet = itemView.findViewById(R.id.btn_recycleview_timkiem_show_xemchitiet);
            imgbtnLuuTin = itemView.findViewById(R.id.imgbtn_recycleview_timkiem_show_luutin);
        }
    }
}