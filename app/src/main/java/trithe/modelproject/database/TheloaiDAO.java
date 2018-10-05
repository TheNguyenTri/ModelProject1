package trithe.modelproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

import trithe.modelproject.model.Theloai;

public class TheloaiDAO {
    private SQLiteDatabase db;
    private Databasemanager databasemanager;
    public static final String TABLE_NAME = "TheLoai";
    public static final String SQL_THE_LOAI = "CREATE TABLE TheLoai (matheloai text primary key, tentheloai text, mota text, vitri int);";
    public static final String TAG = "TheLoaiDAO";

    public TheloaiDAO(Context context) {
        databasemanager = new Databasemanager(context);
        db = databasemanager.getWritableDatabase();
    }

    //insert
    public int inserTheLoai(Theloai theLoai) {
        ContentValues values = new ContentValues();
        values.put("matheloai", theLoai.getMaTheLoai());
        values.put("tentheloai", theLoai.getTenTheLoai());
        values.put("mota", theLoai.getMoTa());
        values.put("vitri", theLoai.getViTri());
        try {
            if (db.insert(TABLE_NAME, null, values) == -1) {
                return -1;
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
        return 1;
    }

    //getAllTheLoai
    public List<Theloai> getAllTheLoai() {
        List<Theloai> dsTheLoai = new ArrayList<>();
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
        c.moveToFirst();
        while (c.isAfterLast() == false) {
            Theloai ee = new Theloai();
            ee.setMaTheLoai(c.getString(0));
            ee.setTenTheLoai(c.getString(1));
            ee.setMoTa(c.getString(2));
            ee.setViTri(c.getInt(3));
            dsTheLoai.add(ee);
            Log.d("//=====", ee.toString());
            c.moveToNext();
        }
        c.close();
        return dsTheLoai;
    }

    //delete
    public int deleteTheLoaiByID(String matheloai) {
        int result = db.delete(TABLE_NAME, "matheloai=?", new String[]{matheloai});
        if (result == 0)
            return -1;
        return 1;
    }

    public int updateTheLoai(Theloai theLoai) {
        ContentValues values = new ContentValues();
        values.put("matheloai", theLoai.getMaTheLoai());
        values.put("tentheloai", theLoai.getTenTheLoai());
        values.put("mota", theLoai.getMoTa());
        values.put("vitri", theLoai.getViTri());
        int result = db.update(TABLE_NAME, values, "matheloai=?", new
                String[]{theLoai.getMaTheLoai()});
        if (result == 0) {
            return -1;
        }
        return 1;
    }


    public int updateTheLoai(String edMaTheloai, String s, String s1, String s2) {
        ContentValues values = new ContentValues();
        values.put("tentheloai", s);
        values.put("vitri", s1);
        values.put("mota", s2);
        int result = db.update(TABLE_NAME, values, "matheloai=?", new String[]{edMaTheloai});
        if (result == 0) {
            return -1;
        }
        return 1;
    }
}
