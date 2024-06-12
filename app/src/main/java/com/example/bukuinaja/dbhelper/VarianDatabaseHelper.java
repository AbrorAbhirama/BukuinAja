package com.example.bukuinaja.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class VarianDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DB_Name = "BukuinAja.db";
    private static final int DB_Version = 1;

    private static final String Table_Name = "Varian";
    private static final String Collum_ID = "idVarian";
    private static final String Collum_name = "name";
    private static final String COllum_jual = "jual";
    private static final String Collum_beli = "beli";
    private static final String Collum_Produk = "idProduk";
    private static final String Foreign_Table = "produk";
    public VarianDatabaseHelper(@Nullable Context context) {
        super(context, DB_Name, null, DB_Version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String querry =
                "CREATE TABLE " + Table_Name +
                        "(" + Collum_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        Collum_name + " TEXT, " +
                        COllum_jual + " INTEGER, " +
                        Collum_beli + " INTEGER, "+
                        Collum_Produk + " INTEGER REFERENCES produk(" + Collum_Produk + ") ON DELETE CASCADE);";
        db.execSQL(querry );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Table_Name);
        onCreate(db);
    }
    public Cursor readDB(){
        String querry = "SELECT * FROM " + Table_Name;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(querry, null);
        }
        return  cursor;
    }
    public void addData(String name, int jual, int beli, int produk){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(Collum_name, name);
        cv.put(COllum_jual, jual);
        cv.put(Collum_beli, beli);
        cv.put(Collum_Produk, produk);
        long result = db.insert(Table_Name, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Success to add", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateData(String row_id, String name, String jual, String beli, String produk){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Collum_name, name);
        cv.put(COllum_jual, jual);
        cv.put(Collum_beli, beli);
        cv.put(Collum_Produk, produk);

        long result = db.update(Table_Name, cv, "rowid=?", new String[]{row_id});
        if (result == -1){
            Toast.makeText(context, "Failed Update", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Success update", Toast.LENGTH_SHORT).show();
        }
    }
    public void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(Table_Name, "rowid=?", new String[]{row_id});
        if (result == -1){
            Toast.makeText(context, "Failed delete", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Success delete", Toast.LENGTH_SHORT).show();
        }
    }
}
