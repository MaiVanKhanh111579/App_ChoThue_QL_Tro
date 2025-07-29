package Fg_LuuTin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.app_chothue_ql_tro.R;

public class LuuTinFg extends Fragment {
    private View view;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_luutin, container,false);
        anhXaView();
        
        return view;
    }

    private void anhXaView() {
    }
}
