package com.example.bukuinaja.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class TransaksiDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DB_Name = "BukuinAja.db";
    private static final int DB_Version = 1;

    private static final String Table_Name = "transaksi";
    private static final String Collum_ID = "idTransaksi";
    private static final String Collum_jumlah = "jumlah";
    private static final String COllum_total = "total";
    private static final String Collum_tanggal = "tanggal";
    private static final String Collum_varian = "idVarian";
    private static final String Foreign_Table = "varian";
    public TransaksiDatabaseHelper(@Nullable Context context) {
        super(context, DB_Name, null, DB_Version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String querry =
                "CREATE TABLE " + Table_Name +
                        "(" + Collum_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        Collum_jumlah + " TEXT, " +
                        COllum_total + " INTEGER, " +
                        Collum_tanggal + " DATE, "+
                        Collum_varian + " INTEGER, " +
                        "FOREIGN KEY(" + Collum_varian +
                        ") REFERENCES " + Foreign_Table +
                        "(" + Collum_varian + "));";
        db.execSQL(querry );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Table_Name);
        onCreate(db);
    }
    Cursor readDB(){
        String querry = "SELECT * FROM " + Table_Name;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(querry, null);
        }
        return  cursor;
    }
    void addData(String jumlah, String total, int varian){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        Date cd = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");
        String cdt = df.format(cd);

        cv.put(Collum_jumlah, jumlah);
        cv.put(COllum_total, total);
        cv.put(Collum_tanggal, cdt);
        cv.put(Collum_varian, varian);
        long result = db.insert(Table_Name, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Success to add", Toast.LENGTH_SHORT).show();
        }
    }
}
