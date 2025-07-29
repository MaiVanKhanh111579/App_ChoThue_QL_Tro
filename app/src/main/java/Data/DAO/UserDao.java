package Data.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import Data.ENTITY.User;


@Dao
public interface UserDao {

    @Insert
    void insertUser(User user);

    @Query("SELECT * FROM users_infor")
    List<User> getListUser();

    @Query("SELECT * FROM users_infor WHERE loaiUser IN ('Đăng tin', 'Tìm kiếm')")
    List<User> getNonAdminUsers();

    @Query("SELECT * FROM users_infor WHERE  SDTChinh = :sdt")
    User findBySDT(String sdt);

    @Query("SELECT * FROM users_infor WHERE maTaiKhoan = :maTaiKhoan")
    User findByMaTaiKhoan(String maTaiKhoan);

    @Query("SELECT * FROM users_infor WHERE isLogged = 1 LIMIT 1")
    User findLoggedUser();

    @Query("UPDATE users_infor SET isLogged = 0")
    void resetLoggedUsers();

    @Update
    void updateUser(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM users_infor WHERE (maTaiKhoan LIKE :keyword OR hoVaTen LIKE :keyword OR email LIKE :keyword) AND loaiUser IN ('Đăng tin', 'Tìm kiếm')")
    List<User> searchUsers(String keyword);
}