package Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_chothue_ql_tro.R;

import java.util.List;

import Data.ENTITY.TienIch;
import Interface.OnTienIchClickListener;

public class DangTinMoi_TienIchAdapter extends RecyclerView.Adapter<DangTinMoi_TienIchAdapter.DangTinMoi_ViewHolder> {
    private List<TienIch> mlist;
    private OnTienIchClickListener listener;

    public DangTinMoi_TienIchAdapter(List<TienIch> mlist, OnTienIchClickListener listener) {
        this.mlist = mlist;
        this.listener = listener;
    }
    public DangTinMoi_TienIchAdapter(List<TienIch> mlist) {
        this.mlist = mlist;
    }

    @NonNull
    @Override
    public DangTinMoi_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_dangtinmoi_tienich, parent, false);
        return new DangTinMoi_ViewHolder(view);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull DangTinMoi_ViewHolder holder, int position) {
        TienIch tienIch = mlist.get(position);
        if (tienIch == null){
            return;
        }
        holder.txtName.setText(tienIch.getTenTienIch());

        if (tienIch.isSelected()) {
            holder.itemView.setBackgroundResource(R.drawable.customer_button_redborder_radius_10dp); // Background khi chọn
            holder.txtName.setTextAppearance(R.style.Paragraph_Bold_black);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.customer_background_noborder_radius_10dp); // Background mặc định
            holder.txtName.setTextAppearance(R.style.Paragraph_Regular_black);
        }

    }

    @Override
    public int getItemCount() {
        if (mlist != null){
            return mlist.size();
        }
        return 0;
    }

    public class DangTinMoi_ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private ImageView icon;
        public DangTinMoi_ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_recycleview_dangtinmoi_tienich_item);
            icon = itemView.findViewById(R.id.img_recycleview_dangtinmoi_tienich_item);
//            itemView.setOnClickListener(v -> {
//                if (listener != null) {
//                    int position = getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION) {
//                        listener.onTienIchClick(mlist.get(position), position);
//                    }
//                }
//            });
//            itemView.setOnClickListener(v -> {
//                int position = getAdapterPosition();
//                if (position != RecyclerView.NO_POSITION && listener != null) {
//                    TienIch tienIch = mlist.get(position);
//                    tienIch.setSelected(true); // Giữ trạng thái chọn
//                    listener.onTienIchClick(tienIch, position);
//                    notifyItemChanged(position); // Cập nhật giao diện item
//                }
//            });
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    TienIch tienIch = mlist.get(position);
                    if (!tienIch.isSelected()) { // Chỉ chọn nếu chưa được chọn
                        tienIch.setSelected(true);
                        notifyItemChanged(position); // Cập nhật giao diện
                        listener.onTienIchClick(tienIch, position);
                    } else {
                        tienIch.setSelected(false);
                        notifyItemChanged(position); // Cập nhật giao diện

                    }                }
            });
        }
    }
}
