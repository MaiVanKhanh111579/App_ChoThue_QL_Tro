package Adapter;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.app_chothue_ql_tro.R;

import java.io.File;
import java.util.List;

public class TinDang_Show_DanhSachAnh extends RecyclerView.Adapter<TinDang_Show_DanhSachAnh.TinDang_Show_DanhSachAnh_ViewHolder> {
    private List<String> imageUrls;

    public TinDang_Show_DanhSachAnh(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public TinDang_Show_DanhSachAnh_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_qltindang_show_danhsachanh, parent, false);
        return new TinDang_Show_DanhSachAnh_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TinDang_Show_DanhSachAnh_ViewHolder holder, int position) {
        String imagePath = imageUrls.get(position);
        Log.d("TinDang_Show_DanhSachAnh", "Attempting to load image: " + imagePath);
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            Glide.with(holder.itemView.getContext())
                    .load(imageFile)
                    .error(R.drawable.ic_user)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.e("TinDang_Show_DanhSachAnh", "Glide load failed for: " + imagePath + ", error: " + (e != null ? e.getMessage() : "null"));
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            Log.d("TinDang_Show_DanhSachAnh", "Glide load success for: " + imagePath);
                            return false;
                        }
                    })
                    .into(holder.imageView);
        } else {
            Log.e("TinDang_Show_DanhSachAnh", "Image file does not exist: " + imagePath);
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.ic_user)
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return imageUrls != null ? imageUrls.size() : 0;
    }

    public class TinDang_Show_DanhSachAnh_ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public TinDang_Show_DanhSachAnh_ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_recycleview_qltindang_show_danhSachAnh);
        }
    }
}