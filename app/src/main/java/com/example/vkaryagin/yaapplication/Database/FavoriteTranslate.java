package com.example.vkaryagin.yaapplication.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.vkaryagin.yaapplication.Database.Schema.FavoriteTranslateEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tripo on 4/3/2017.
 */

public class FavoriteTranslate {

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + FavoriteTranslateEntry.TABLE_NAME +
            " ( "+ FavoriteTranslateEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
            FavoriteTranslateEntry.COLUMN_NAME_TRANSLATE_TEXT + " TEXT, " +
            FavoriteTranslateEntry.COLUMN_NAME_TRANSLATED_TEXT + " TEXT, " +
            FavoriteTranslateEntry.COLUMN_NAME_TRANSLATE_LANG + " TEXT, " +
            FavoriteTranslateEntry.COLUMN_NAME_TRANSLATED_LANG + " TEXT, " +
            FavoriteTranslateEntry.COLUMN_NAME_TRANSLATE_CODE + " TEXT, " +
            FavoriteTranslateEntry.COLUMN_NAME_TRANSLATED_CODE + " TEXT, " +
            FavoriteTranslateEntry.COLUMN_NAME_TRANSLATE_DATE + " TEXT DEFAULT (datetime('now', 'localtime')))";
    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FavoriteTranslateEntry.TABLE_NAME;

    public static final String[] QUERY_COLUMS = {
            FavoriteTranslateEntry._ID,
            FavoriteTranslateEntry.COLUMN_NAME_TRANSLATE_TEXT,
            FavoriteTranslateEntry.COLUMN_NAME_TRANSLATED_TEXT,
            FavoriteTranslateEntry.COLUMN_NAME_TRANSLATE_LANG,
            FavoriteTranslateEntry.COLUMN_NAME_TRANSLATED_LANG,
            FavoriteTranslateEntry.COLUMN_NAME_TRANSLATE_CODE,
            FavoriteTranslateEntry.COLUMN_NAME_TRANSLATED_CODE,
            FavoriteTranslateEntry.COLUMN_NAME_TRANSLATE_DATE
    };

    private final YaAppDBOpenHelper dbOpenHelper;

    public FavoriteTranslate(final Context context) {
        dbOpenHelper = new YaAppDBOpenHelper(context, null);
    }

    public List<FavoriteTranslateEntry> getAll() {
        ArrayList<FavoriteTranslateEntry> records = new ArrayList<>();
        Cursor cursor = this.query(QUERY_COLUMS, null, null, null, null,
                FavoriteTranslateEntry.COLUMN_NAME_TRANSLATE_DATE + " DESC");

        while (cursor.moveToNext()) {
            FavoriteTranslateEntry record = new FavoriteTranslateEntry();
            record.id = cursor.getLong(cursor.getColumnIndexOrThrow(FavoriteTranslateEntry._ID));
            record.translateText = cursor.getString(cursor.getColumnIndexOrThrow(
                    FavoriteTranslateEntry.COLUMN_NAME_TRANSLATE_TEXT));
            record.translatedText = cursor.getString(cursor.getColumnIndexOrThrow(
                    FavoriteTranslateEntry.COLUMN_NAME_TRANSLATED_TEXT));
            record.translateLang = cursor.getString(cursor.getColumnIndexOrThrow(
                    FavoriteTranslateEntry.COLUMN_NAME_TRANSLATE_LANG));
            record.translatedLang = cursor.getString(cursor.getColumnIndexOrThrow(
                    FavoriteTranslateEntry.COLUMN_NAME_TRANSLATED_LANG));
            record.translateCode = cursor.getString(cursor.getColumnIndexOrThrow(
                    FavoriteTranslateEntry.COLUMN_NAME_TRANSLATE_CODE));
            record.translatedCode = cursor.getString(cursor.getColumnIndexOrThrow(
                    FavoriteTranslateEntry.COLUMN_NAME_TRANSLATED_CODE));
            record.date = cursor.getString(cursor.getColumnIndexOrThrow(
                    FavoriteTranslateEntry.COLUMN_NAME_TRANSLATE_DATE));
            records.add(record);
        }
        cursor.close();

        return records;
    }


    public Cursor query(String[] columns, String selection, String[] selectionArgs, String groupBy,
                        String having, String orderBy ) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        return db.query(FavoriteTranslateEntry.TABLE_NAME,
                columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    public void insert(String translateText, String translatedText, String translateLang,
                       String translatedLang, String translateCode, String translatedCode) {
        ContentValues values = new ContentValues();
        values.put(FavoriteTranslateEntry.COLUMN_NAME_TRANSLATE_TEXT, translateText);
        values.put(FavoriteTranslateEntry.COLUMN_NAME_TRANSLATED_TEXT, translatedText);
        values.put(FavoriteTranslateEntry.COLUMN_NAME_TRANSLATE_LANG, translateLang);
        values.put(FavoriteTranslateEntry.COLUMN_NAME_TRANSLATED_LANG, translatedLang);
        values.put(FavoriteTranslateEntry.COLUMN_NAME_TRANSLATE_CODE, translateCode);
        values.put(FavoriteTranslateEntry.COLUMN_NAME_TRANSLATED_CODE, translatedCode);

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        long id = db.insert(FavoriteTranslateEntry.TABLE_NAME, null, values);
    }

}
