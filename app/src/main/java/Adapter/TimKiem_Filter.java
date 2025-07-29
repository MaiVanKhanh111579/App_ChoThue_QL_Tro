package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_chothue_ql_tro.R;

import java.util.List;

public class TimKiem_Filter extends RecyclerView.Adapter<TimKiem_Filter.TimKiem_Filter_ViewHolder> {
    private List<Model.TimKiem_Filter> mFilter;

    public TimKiem_Filter(List<Model.TimKiem_Filter> mFilter) {
        this.mFilter = mFilter;
    }

    @NonNull
    @Override
    public TimKiem_Filter_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_timkiem_fillter, parent,false);
        return new TimKiem_Filter_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimKiem_Filter_ViewHolder holder, int position) {
        Model.TimKiem_Filter filter_model = mFilter.get(position);

        if (filter_model ==null)
        {
            return;
        }
        holder.txt_filter_button.setText(filter_model.getName());
        holder.imgbtn_filter_button.setEnabled(true);
    }

    @Override
    public int getItemCount() {
        if (mFilter!=null)
        {
            return mFilter.size();
        }
        return 0;
    }

    public class TimKiem_Filter_ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_filter_button;
        private ImageButton imgbtn_filter_button;
        public TimKiem_Filter_ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_filter_button = itemView.findViewById(R.id.txt_recycleview_filter_button);
            imgbtn_filter_button= itemView.findViewById(R.id.imgbtn_recycleview_filter_button);
        }
    }
}