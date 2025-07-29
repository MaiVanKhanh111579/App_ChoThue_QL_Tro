package Data.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import Data.ENTITY.ThanhToan;

@Dao
public interface ThanhToanDao {
    @Insert
    long insertThanhToan(ThanhToan thanhToan);

    @Query("SELECT * FROM thanh_toan WHERE ma_tindang = :maTinDang")
    ThanhToan getThanhToanByTinDangId(int maTinDang);
}
