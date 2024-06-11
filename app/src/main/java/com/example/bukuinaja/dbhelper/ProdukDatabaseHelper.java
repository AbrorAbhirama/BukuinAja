package com.example.bukuinaja.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class ProdukDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DB_Name = "BukuinAja.db";
    private static final int DB_Version = 1;

    private static final String Table_Name = "produk";
    private static final String Collum_ID = "idProduk";
    private static final String Collum_name = "name";
    private static final String COllum_image = "image";
    private static final String Collum_favorite = "favorite";

    ProdukDatabaseHelper(@Nullable Context context) {
        super(context, DB_Name, null, DB_Version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String querry =
                "CREATE TABLE " + Table_Name +
                        "(" + Collum_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        Collum_name + " TEXT, " +
                        COllum_image + " BLOB, " +
                        Collum_favorite + " INTEGER);";
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
    void addData(String name, byte[]img, int fav){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(Collum_name, name);
        cv.put(COllum_image, img);
        cv.put(Collum_favorite, fav);
        long result = db.insert(Table_Name, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Success to add", Toast.LENGTH_SHORT).show();
        }
    }

    void updateData(String row_id, String name, byte[]img, int fav){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Collum_name, name);
        cv.put(COllum_image, img);
        cv.put(Collum_favorite, fav);

        long result = db.update(Table_Name, cv, "rowid=?", new String[]{row_id});
        if (result == -1){
            Toast.makeText(context, "Failed Update", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Success update", Toast.LENGTH_SHORT).show();
        }
    }
    void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(Table_Name, "rowid=?", new String[]{row_id});
        if (result == -1){
            Toast.makeText(context, "Failed delete", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Success delete", Toast.LENGTH_SHORT).show();
        }
    }
    public Bitmap getImage(String collum_Image_ID){
        SQLiteDatabase db = this.getWritableDatabase();
        Bitmap bt = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + Table_Name + " WHERE " + Collum_ID +" = " + collum_Image_ID, null);
        if (cursor.moveToNext()){
            byte[] img = cursor.getBlob(1);
            bt = BitmapFactory.decodeByteArray(img, 0, img.length);
        }
        return bt;
    }
}
