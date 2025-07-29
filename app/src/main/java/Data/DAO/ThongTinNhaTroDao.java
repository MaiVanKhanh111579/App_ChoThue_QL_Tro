package Data.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import Data.ENTITY.DanhSachAnh;
import Data.ENTITY.ThongTinNhaTro;

@Dao
public interface ThongTinNhaTroDao {
    @Insert
    long insert(ThongTinNhaTro thongTinNhaTro);
    @Query("DELETE FROM thongtin_nhatro WHERE tin_dang_id = :tin_dang_id")
    void deleteThongTinNhaTro_By_TinDangId(int tin_dang_id);

    @Query("SELECT * FROM thongtin_nhatro WHERE tin_dang_id = :tin_dang_id") //lấy ảnh theo id tin đăng, hiển thị danh sách ảnh
    ThongTinNhaTro getThongTinNhaTro_by_TinDangId(int tin_dang_id);

    @Update
    void update(ThongTinNhaTro thongTinNhaTro);
}
