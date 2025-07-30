package Data.ENTITY;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "datcoctruoc")
public class DatCocTruoc {
    @PrimaryKey
    @NonNull
    private String maDatCocTruoc;
    private String maKhachHang;
    private String maTinDang;
    private String tienDatCoc;
    private String ngayDatCoc;
    private String ngayLienHe;
    private String trangThai; //Đã xem nhà / Chưa xem nhà


    public String getMaDatCocTruoc() {
        return maDatCocTruoc;
    }

    public void setMaDatCocTruoc(String maDatCocTruoc) {
        this.maDatCocTruoc = maDatCocTruoc;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }


    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public String getMaTinDang() {
        return maTinDang;
    }

    public void setMaTinDang(String maTinDang) {
        this.maTinDang = maTinDang;
    }

    public String getTienDatCoc() {
        return tienDatCoc;
    }

    public void setTienDatCoc(String tienDatCoc) {
        this.tienDatCoc = tienDatCoc;
    }

    public String getNgayDatCoc() {
        return ngayDatCoc;
    }

    public void setNgayDatCoc(String ngayDatCoc) {
        this.ngayDatCoc = ngayDatCoc;
    }

    public String getNgayLienHe() {
        return ngayLienHe;
    }

    public void setNgayLienHe(String ngayLienHe) {
        this.ngayLienHe = ngayLienHe;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
