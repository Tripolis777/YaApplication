package com.example.vkaryagin.yaapplication.Database.Schema;

import android.provider.BaseColumns;

import com.example.vkaryagin.yaapplication.Fragments.FavoriteFragment;

/**
 * Created by tripo on 4/3/2017.
 */

public class FavoriteTranslate {

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXIST " + FavoriteTranslateEntry.TABLE_NAME +
            " ( "+ FavoriteTranslateEntry._ID +"INTEGER PRIMARY KEY, " +
            FavoriteTranslateEntry.COLUMN_NAME_TRANSLATE_TEXT + " TEXT," +
            FavoriteTranslateEntry.COLUMN_NAME_TRANSLATED_TEXT + " TEXT," +
            FavoriteTranslateEntry.COLUMN_NAME_TRANSLATE_LANG + "TEXT, " +
            FavoriteTranslateEntry.COLUMN_NAME_TRANSLATED_LANG + "TEXT)";
    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXIST " + FavoriteTranslateEntry.TABLE_NAME;


    public FavoriteTranslate() {};

    public static class FavoriteTranslateEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorites";

        public static final String COLUMN_NAME_TRANSLATE_TEXT = "translate_text";
        public static final String COLUMN_NAME_TRANSLATED_TEXT = "translated_text";
        public static final String COLUMN_NAME_TRANSLATE_LANG = "translate_lang";
        public static final String COLUMN_NAME_TRANSLATED_LANG = "translated_lang";
    }
}
