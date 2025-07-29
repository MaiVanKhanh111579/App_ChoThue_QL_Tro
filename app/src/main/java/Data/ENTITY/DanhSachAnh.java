package Data.ENTITY;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "danh_sach_anh",
        foreignKeys = @ForeignKey(entity = TinDang.class,
                parentColumns = "tin_dang_id",
                childColumns = "tin_dang_id",
                onDelete = ForeignKey.CASCADE))
public class DanhSachAnh {
    @PrimaryKey(autoGenerate = true)
    private int imageId;

    private int tin_dang_id; // Khóa ngoại liên kết với Post
    private String imageUrl; // Đường dẫn ảnh (URL hoặc local path)

    // Constructor
    public DanhSachAnh(int tin_dang_id, String imageUrl) {
        this.tin_dang_id = tin_dang_id;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getTin_dang_id() {
        return tin_dang_id;
    }

    public void setTin_dang_id(int tin_dang_id) {
        this.tin_dang_id = tin_dang_id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}