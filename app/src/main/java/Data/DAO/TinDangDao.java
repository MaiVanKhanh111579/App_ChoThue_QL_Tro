package Data.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import Data.ENTITY.TinDang;

@Dao
public interface TinDangDao {
    @Insert
    long insert (TinDang tinDang);
    @Query("SELECT * FROM tin_dang")
    List<TinDang> getAllTinDang();

    @Query("SELECT * FROM tin_dang WHERE tin_dang_id = :tin_dang_id")
    TinDang getTinDangById(int tin_dang_id);

    @Update
    void update (TinDang tinDang);

    @Delete
    void deleteTinDang(TinDang tinDang);
}
