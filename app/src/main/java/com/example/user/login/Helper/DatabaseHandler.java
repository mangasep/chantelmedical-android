package com.example.user.login.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.user.login.model.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bagicode on 28/09/16.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "token";

    private static final String tb_token = "token";

    private static final String field_token = "token";
    private static final String field_device_id = "device_id";

    private static final String CREATE_TABLE_TOKEN = "CREATE TABLE " + tb_token + "("
            + field_token + " TEXT,"
            + field_device_id + " TEXT )";

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TOKEN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void CreateToken (Token mdNotif) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(field_token, mdNotif.getToken());
        values.put(field_token, mdNotif.getDevice_id());
        db.insert(tb_token, null, values);
        db.close();
    }

    public List<Token> ReadMahasiswa() {
        List<Token> judulModelList = new ArrayList<Token>();
        String selectQuery = "SELECT  * FROM " + tb_token;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Token mdKontak = new Token();
                mdKontak.setToken (cursor.getString(0));
                mdKontak.setDevice_id(cursor.getString(1));
                judulModelList.add(mdKontak);
            } while (cursor.moveToNext());
        }
        db.close();
        return judulModelList;
    }

    public int UpdateMahasiswa (Token mdNotif) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(field_token, mdNotif.getToken());
        values.put(field_device_id, mdNotif.getDevice_id());

        return db.update(tb_token, values, field_token + " = ?",
                new String[] { String.valueOf(mdNotif.getToken())});
    }

    public void DeleteMahasiswa (Token mdNotif) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tb_token, field_token+ " = ?",
                new String[]{String.valueOf(mdNotif.getToken())});
        db.close();
    }
}
