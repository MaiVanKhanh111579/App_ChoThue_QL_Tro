package Data.ENTITY;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "thanh_toan")
public class ThanhToan {
    @PrimaryKey (autoGenerate = true)
    private int ma_thanhtoan;
    private String loaiThanhToan;
    private int ma_tindang;
    private String trangThai; //Thanh cong / Khong thanh cong
    private String thoiGianThanhToan;
    private String soTienThanhToan;

    public ThanhToan(int ma_thanhtoan, String loaiThanhToan, int ma_tindang, String trangThai, String thoiGianThanhToan, String soTienThanhToan) {
        this.ma_thanhtoan = ma_thanhtoan;
        this.loaiThanhToan = loaiThanhToan;
        this.ma_tindang = ma_tindang;
        this.trangThai = trangThai;
        this.thoiGianThanhToan = thoiGianThanhToan;
        this.soTienThanhToan = soTienThanhToan;
    }

    public int getMa_thanhtoan() {
        return ma_thanhtoan;
    }

    public void setMa_thanhtoan(int ma_thanhtoan) {
        this.ma_thanhtoan = ma_thanhtoan;
    }

    public String getLoaiThanhToan() {
        return loaiThanhToan;
    }

    public void setLoaiThanhToan(String loaiThanhToan) {
        this.loaiThanhToan = loaiThanhToan;
    }

    public int getMa_tindang() {
        return ma_tindang;
    }

    public void setMa_tindang(int ma_tindang) {
        this.ma_tindang = ma_tindang;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getThoiGianThanhToan() {
        return thoiGianThanhToan;
    }

    public void setThoiGianThanhToan(String thoiGianThanhToan) {
        this.thoiGianThanhToan = thoiGianThanhToan;
    }

    public String getSoTienThanhToan() {
        return soTienThanhToan;
    }

    public void setSoTienThanhToan(String soTienThanhToan) {
        this.soTienThanhToan = soTienThanhToan;
    }
}
