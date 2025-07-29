package Data.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import Data.ENTITY.DanhSachAnh;

@Dao
public interface DanhSachAnhDao {
    @Insert
    void insertDanhSachAnh(DanhSachAnh image);

    @Insert
    void insertDanhSachAnhs(List<DanhSachAnh> images);

    @Query("SELECT * FROM danh_sach_anh WHERE tin_dang_id = :tin_dang_id")
    List<DanhSachAnh> getDanhSachAnh_by_TinDangId(int tin_dang_id);

    @Query("DELETE FROM danh_sach_anh WHERE tin_dang_id = :tin_dang_id")
    void deleteDanhSachAnh_by_TinDangId(int tin_dang_id);

    @Query("SELECT * FROM danh_sach_anh WHERE tin_dang_id = :tin_dang_id") //lấy ảnh theo id tin đăng, hiển thị danh sách ảnh
    List<DanhSachAnh> getImagesByPostId(int tin_dang_id);
    @Query("DELETE FROM danh_sach_anh WHERE imageUrl = :imageUrl")
    void deleteByImageUrl(String imageUrl);
}