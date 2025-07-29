package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app_chothue_ql_tro.R;

import java.io.File;
import java.util.List;

public class TinDang_DanhSachAnh extends RecyclerView.Adapter<TinDang_DanhSachAnh.ImageViewHolder> {
    private List<String> imageUrls;
    private OnImageRemoveListener removeListener;

    public interface OnImageRemoveListener {
        void onImageRemove(int position);
    }

    public TinDang_DanhSachAnh(List<String> imageUrls, OnImageRemoveListener listener) {
        this.imageUrls = imageUrls;
        this.removeListener = listener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_danh_sach_anh, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String imagePath = imageUrls.get(position);
        Glide.with(holder.itemView.getContext())
                .load(new File(imagePath))
                .error(R.drawable.ic_user)
                .into(holder.imageView);

        holder.removeButton.setOnClickListener(v -> {
            if (removeListener != null) {
                removeListener.onImageRemove(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageUrls != null ? imageUrls.size() : 0;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private ImageButton removeButton;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_recycleview_danh_sach_anh);
            removeButton = itemView.findViewById(R.id.imgbtn_recycleview_danh_sach_anh);
        }
    }
}