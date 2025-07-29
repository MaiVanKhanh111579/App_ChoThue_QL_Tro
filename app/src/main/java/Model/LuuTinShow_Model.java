package Model;

public class LuuTinShow_Model {
    private String tieuDe;
    private String giaTien;
    private String dienTich;
    private String diaChi;
    private String ngayDang;
    private int hinhAnh;
    private boolean isHeart = false;

    public LuuTinShow_Model(String tieuDe, String giaTien, String dienTich, String diaChi, String ngayDang, int hinhAnh) {
        this.tieuDe = tieuDe;
        this.giaTien = giaTien;
        this.dienTich = dienTich;
        this.diaChi = diaChi;
        this.ngayDang = ngayDang;
        this.hinhAnh = hinhAnh;
    }

    public boolean isHeart() {
        return isHeart;
    }

    public void setHeart(boolean heart) {
        isHeart = heart;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(String giaTien) {
        this.giaTien = giaTien;
    }

    public String getDienTich() {
        return dienTich;
    }

    public void setDienTich(String dienTich) {
        this.dienTich = dienTich;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getNgayDang() {
        return ngayDang;
    }

    public void setNgayDang(String ngayDang) {
        this.ngayDang = ngayDang;
    }

    public int getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(int hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
}
