package Data.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import Data.ENTITY.TienIch;

@Dao
public interface TienIchDao {
    @Insert
    long insertTienIch(TienIch tienIch);

    @Query("SELECT * FROM tienIch WHERE maTienIch = :maTienIch")
    TienIch getTienIchByMa(String maTienIch);
}