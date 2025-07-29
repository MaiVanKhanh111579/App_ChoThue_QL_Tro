package Fg_ThanhToan;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.app_chothue_ql_tro.R;

public class PhuongThucThanhToanFg extends Fragment {
    private View view;
    private ImageButton imgtbtnBackStack;
    private Button btnQuayLai, btnTiepTuc;
    private TextView txtToolbar;
    private RadioButton rdnganhang, rdbangvidientu;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_phuongthucthanhtoan, container,false);
        anhXaView();
        toolBar();
        btnTiepTuc.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_navigation_phuongthucthanhtoan_to_navigation_ketquagiaodich);
        });
        btnQuayLai.setOnClickListener(v -> {
            Navigation.findNavController(v).popBackStack();
        });
        return view;
    }

    private void anhXaView() {
        txtToolbar = view.findViewById(R.id.txt_layout_toolbar_have_backstack_tittle);
        imgtbtnBackStack = view.findViewById(R.id.imgbtn_layout_toolbar_have_backstack_BackStack);

        btnQuayLai = view.findViewById(R.id.btn_phuongthucthanhtoan_quaylai);
        btnTiepTuc = view.findViewById(R.id.btn_phuongthucthanhtoan_tieptuc);
    }
    @SuppressLint("SetTextI18n")
    private void toolBar(){
        txtToolbar.setText("Phương thức thanh toán");
        imgtbtnBackStack.setVisibility(View.GONE);
    }
}
