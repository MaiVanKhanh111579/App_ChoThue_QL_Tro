package Data.ENTITY;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "users_infor")
public class User {

    @PrimaryKey
    @NonNull
    private String maTaiKhoan ;
    private String hoVaTen;
    private String ngaySinh;
    private String CCCD;
    private String maSoThue;
    private String SDTChinh;
    private String email;
    private String avatar;
    private String diaChi;
    private String passWord;
    private boolean isLogged = false;
    private String loaiUser;
    private String trangThai;

    public User(){}

    public User(@NonNull String maTaiKhoan, String hoVaTen, String ngaySinh, String CCCD, String maSoThue, String SDTChinh, String email, String avatar, String diaChi, String passWord, boolean isLogged, String loaiUser, String trangThai) {
        this.maTaiKhoan = maTaiKhoan;
        this.hoVaTen = hoVaTen;
        this.ngaySinh = ngaySinh;
        this.CCCD = CCCD;
        this.maSoThue = maSoThue;
        this.SDTChinh = SDTChinh;
        this.email = email;
        this.avatar = avatar;
        this.diaChi = diaChi;
        this.passWord = passWord;
        this.isLogged = isLogged;
        this.loaiUser = loaiUser;
        this.trangThai = trangThai;
    }

    public String getLoaiUser() {
        return loaiUser;
    }

    public void setLoaiUser(String loaiUser) {
        this.loaiUser = loaiUser;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getMaTaiKhoan() {
        return maTaiKhoan;
    }

    public void setMaTaiKhoan(String maTaiKhoan) {
        this.maTaiKhoan = maTaiKhoan;
    }

    public String getHoVaTen() {
        return hoVaTen;
    }

    public void setHoVaTen(String hoVaTen) {
        this.hoVaTen = hoVaTen;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getCCCD() {
        return CCCD;
    }

    public void setCCCD(String CCCD) {
        this.CCCD = CCCD;
    }

    public String getMaSoThue() {
        return maSoThue;
    }

    public void setMaSoThue(String maSoThue) {
        this.maSoThue = maSoThue;
    }

    public String getSDTChinh() {
        return SDTChinh;
    }

    public void setSDTChinh(String SDTChinh) {
        this.SDTChinh = SDTChinh;
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

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
    }
}