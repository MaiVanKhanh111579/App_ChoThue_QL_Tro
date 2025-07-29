package Data.ENTITY;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tin_dang")
public class TinDang {
    @PrimaryKey(autoGenerate = true)
    private int tin_dang_id;
    private String tieu_de;
    private String mo_ta;
    private String dia_chi;
    private String dienTich;
    private String giaThue;
    private String so_Nguoi_O;
    private String id_tien_ich; //mã tiện ich
    private String ho_va_Ten;
    private String sdt;
    private String email;
    private String avatar;
    private String ngayDang;
    private Boolean isLuuTin = false;
    private Boolean isSelected = false;

    public TinDang(String tieu_de, String mo_ta, String dia_chi, String dienTich, String giaThue, String so_Nguoi_O, String id_tien_ich, String ho_va_Ten, String sdt, String email, String avatar, String ngayDang) {
        this.tieu_de = tieu_de;
        this.mo_ta = mo_ta;
        this.dia_chi = dia_chi;
        this.dienTich = dienTich;
        this.giaThue = giaThue;
        this.so_Nguoi_O = so_Nguoi_O;
        this.id_tien_ich = id_tien_ich;
        this.ho_va_Ten = ho_va_Ten;
        this.sdt = sdt;
        this.email = email;
        this.avatar = avatar;
        this.ngayDang = ngayDang;
    }

    public Boolean getLuuTin() {
        return isLuuTin;
    }

    public void setLuuTin(Boolean luuTin) {
        isLuuTin = luuTin;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public String getNgayDang() { return ngayDang; }
    public void setNgayDang(String ngayDang) { this.ngayDang = ngayDang; }
    public String getGiaThue() {
        return giaThue;
    }

    public void setGiaThue(String giaThue) {
        this.giaThue = giaThue;
    }

    public String getDienTich() {
        return dienTich;
    }

    public void setDienTich(String dienTich) {
        this.dienTich = dienTich;
    }

    public String getSo_Nguoi_O() {
        return so_Nguoi_O;
    }

    public void setSo_Nguoi_O(String so_Nguoi_O) {
        this.so_Nguoi_O = so_Nguoi_O;
    }

    public int getTin_dang_id() {
        return tin_dang_id;
    }

    public void setTin_dang_id(int tin_dang_id) {
        this.tin_dang_id = tin_dang_id;
    }

    public String getTieu_de() {
        return tieu_de;
    }

    public void setTieu_de(String tieu_de) {
        this.tieu_de = tieu_de;
    }

    public String getMo_ta() {
        return mo_ta;
    }

    public void setMo_ta(String mo_ta) {
        this.mo_ta = mo_ta;
    }

    public String getDia_chi() {
        return dia_chi;
    }

    public void setDia_chi(String dia_chi) {
        this.dia_chi = dia_chi;
    }

    public String getId_tien_ich() {
        return id_tien_ich;
    }

    public void setId_tien_ich(String id_tien_ich) {
        this.id_tien_ich = id_tien_ich;
    }

    public String getHo_va_Ten() {
        return ho_va_Ten;
    }

    public void setHo_va_Ten(String ho_va_Ten) {
        this.ho_va_Ten = ho_va_Ten;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
