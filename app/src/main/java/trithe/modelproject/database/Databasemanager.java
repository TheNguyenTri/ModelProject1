package trithe.modelproject.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Databasemanager extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "dbtest1";
    public static final int VERSION = 1;

    public Databasemanager(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserDAO.SQL_USER);
        db.execSQL(SachDAO.SQL_SACH);
        db.execSQL(TheloaiDAO.SQL_THE_LOAI);
        db.execSQL(HoaDonDAO.SQL_HOA_DON);
        db.execSQL(HoaDonChiTietDAO.SQL_HOA_DON_CHI_TIET);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists " + UserDAO.TABLE_NAME);
        db.execSQL("Drop table if exists " + SachDAO.TABLE_NAME);
        db.execSQL("Drop table if exists " + TheloaiDAO.TABLE_NAME);
        db.execSQL("Drop table if exists " + HoaDonDAO.TABLE_NAME);
        db.execSQL("Drop table if exists " + HoaDonChiTietDAO.TABLE_NAME);
        onCreate(db);
    }
}
