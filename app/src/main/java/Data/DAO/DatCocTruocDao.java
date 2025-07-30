package Data.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import Data.ENTITY.DatCocTruoc;

@Dao
public interface DatCocTruocDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DatCocTruoc datCocTruoc);
    @Query("SELECT * FROM datcoctruoc WHERE maDatCocTruoc = :ma")
    DatCocTruoc findById(String ma);
}