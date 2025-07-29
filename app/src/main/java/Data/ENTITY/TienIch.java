package Data.ENTITY;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import Data.DAO.TienIchDao;

@Entity (tableName = "tienIch")
public class TienIch {
    @PrimaryKey
    @NonNull
    private String maTienIch;
    private String tenTienIch;
    private boolean isSelected = false;

    public TienIch(){}

    public TienIch(@NonNull String maTienIch, String tenTienIch) {
        this.maTienIch = maTienIch;
        this.tenTienIch = tenTienIch;
    }
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    @NonNull
    public String getMaTienIch() {
        return maTienIch;
    }

    public void setMaTienIch(@NonNull String maTienIch) {
        this.maTienIch = maTienIch;
    }

    public String getTenTienIch() {
        return tenTienIch;
    }

    public void setTenTienIch(String tenTienIch) {
        this.tenTienIch = tenTienIch;
    }
}
