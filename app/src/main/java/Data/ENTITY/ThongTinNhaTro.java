package Data.ENTITY;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity (tableName = "thongtin_nhatro",
        foreignKeys = @ForeignKey(entity = TinDang.class,
                parentColumns = "tin_dang_id",
                childColumns = "tin_dang_id",
                onDelete = ForeignKey.CASCADE))
public class ThongTinNhaTro {
    @PrimaryKey(autoGenerate = true)
    private int thongtin_nhatro_id;

    private String soPhong;
    private String diaChi;
    private String loaiTro; //Phòng trọ - Nhà trọ
    private String noiThat; //Đầy đủ / Cơ bản / Không nội thất
    private String loaiPhong; //Dùng chung - Dùng riêng
    private String giaDien;
    private String giaNuoc;
    private String giaInternet;
    private int tin_dang_id;

    public ThongTinNhaTro(String soPhong, String diaChi, String loaiTro, String noiThat, String loaiPhong, String giaDien, String giaNuoc, String giaInternet, int tin_dang_id) {
        this.soPhong = soPhong;
        this.diaChi = diaChi;
        this.loaiTro = loaiTro;
        this.noiThat = noiThat;
        this.loaiPhong = loaiPhong;
        this.giaDien = giaDien;
        this.giaNuoc = giaNuoc;
        this.giaInternet = giaInternet;
        this.tin_dang_id = tin_dang_id;
    }

    public int getTin_dang_id() {
        return tin_dang_id;
    }

    public void setTin_dang_id(int tin_dang_id) {
        this.tin_dang_id = tin_dang_id;
    }

    public int getThongtin_nhatro_id() {
        return thongtin_nhatro_id;
    }

    public void setThongtin_nhatro_id(int thongtin_nhatro_id) {
        this.thongtin_nhatro_id = thongtin_nhatro_id;
    }

    public String getSoPhong() {
        return soPhong;
    }

    public void setSoPhong(String soPhong) {
        this.soPhong = soPhong;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getLoaiTro() {
        return loaiTro;
    }

    public void setLoaiTro(String loaiTro) {
        this.loaiTro = loaiTro;
    }

    public String getNoiThat() {
        return noiThat;
    }

    public void setNoiThat(String noiThat) {
        this.noiThat = noiThat;
    }

    public String getLoaiPhong() {
        return loaiPhong;
    }

    public void setLoaiPhong(String loaiPhong) {
        this.loaiPhong = loaiPhong;
    }

    public String getGiaDien() {
        return giaDien;
    }

    public void setGiaDien(String giaDien) {
        this.giaDien = giaDien;
    }

    public String getGiaNuoc() {
        return giaNuoc;
    }

    public void setGiaNuoc(String giaNuoc) {
        this.giaNuoc = giaNuoc;
    }

    public String getGiaInternet() {
        return giaInternet;
    }

    public void setGiaInternet(String giaInternet) {
        this.giaInternet = giaInternet;
    }
}
