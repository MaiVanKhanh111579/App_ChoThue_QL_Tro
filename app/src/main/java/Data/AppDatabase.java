package Data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import Data.DAO.DanhSachAnhDao;
import Data.DAO.ThanhToanDao;
import Data.DAO.ThongTinNhaTroDao;
import Data.DAO.TienIchDao;
import Data.DAO.TinDangDao;
import Data.DAO.UserDao;
import Data.ENTITY.DanhSachAnh;
import Data.ENTITY.ThanhToan;
import Data.ENTITY.ThongTinNhaTro;
import Data.ENTITY.TienIch;
import Data.ENTITY.TinDang;
import Data.ENTITY.User;


@Database(entities = {User.class, TienIch.class, TinDang.class, DanhSachAnh.class, ThongTinNhaTro.class, ThanhToan.class}, version = 6, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "qlNhaTro.db";
    public static AppDatabase Instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (Instance == null) {
            Instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                    .addMigrations(MIGRATION_1_2,MIGRATION_2_3, MIGRATION_3_4,MIGRATION_4_5,MIGRATION_5_6)
                    .build();
        }
        return Instance;
    }
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE tin_dang ADD COLUMN ngayDang TEXT");
            database.execSQL("ALTER TABLE tin_dang ADD COLUMN isSelected INTEGER DEFAULT 0");
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS thongtin_nhatro (" +
                    "thongtin_nhatro_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "soPhong TEXT, " +
                    "diaChi TEXT, " +
                    "loaiTro TEXT, " +
                    "noiThat TEXT, " +
                    "loaiPhong TEXT, " +
                    "giaDien TEXT, " +
                    "giaNuoc TEXT, " +
                    "giaInternet TEXT, " +
                    "tin_dang_id INTEGER NOT NULL, " +
                    "FOREIGN KEY(tin_dang_id) REFERENCES tin_dang(tin_dang_id) ON DELETE CASCADE)");
        }
    };

    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Tạo bảng tạm thời với cấu trúc đúng
            database.execSQL("CREATE TABLE thongtin_nhatro_new (" +
                    "thongtin_nhatro_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "soPhong TEXT, " +
                    "diaChi TEXT, " +
                    "loaiTro TEXT, " +
                    "noiThat TEXT, " +
                    "loaiPhong TEXT, " +
                    "giaDien TEXT, " +
                    "giaNuoc TEXT, " +
                    "giaInternet TEXT, " +
                    "tin_dang_id INTEGER NOT NULL, " +
                    "FOREIGN KEY(tin_dang_id) REFERENCES tin_dang(tin_dang_id) ON DELETE CASCADE)");
            // Sao chép dữ liệu từ bảng cũ (nếu có)
            database.execSQL("INSERT INTO thongtin_nhatro_new (thongtin_nhatro_id, soPhong, diaChi, loaiTro, noiThat, loaiPhong, giaDien, giaNuoc, giaInternet) " +
                    "SELECT thongtin_nhatro_id, soPhong, diaChi, loaiTro, noiThat, loaiPhong, giaDien, giaNuoc, giaInternet FROM thongtin_nhatro");
            // Xóa bảng cũ
            database.execSQL("DROP TABLE thongtin_nhatro");
            // Đổi tên bảng tạm thành bảng chính
            database.execSQL("ALTER TABLE thongtin_nhatro_new RENAME TO thongtin_nhatro");
        }
    };
    static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS thanh_toan (" +
                    "ma_thanhtoan INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "loaiThanhToan TEXT, " +
                    "trangThai TEXT, " +
                    "thoiGianThanhToan TEXT, " +
                    "soTienThanhToan TEXT, " +
                    "ma_tindang Interger not null)");
        }
    };
    static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE tin_dang ADD COLUMN isLuuTin INTEGER DEFAULT 0");
        }
    };

    public abstract UserDao userDao();
    public abstract TinDangDao tinDangDao();
    public abstract DanhSachAnhDao danhSachAnhDao();
    public abstract TienIchDao tienIchDao();
    public abstract ThongTinNhaTroDao thongTinNhaTroDao();
    public abstract ThanhToanDao thanhToanDao();
}