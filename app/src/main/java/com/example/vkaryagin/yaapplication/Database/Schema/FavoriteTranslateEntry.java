package com.example.vkaryagin.yaapplication.Database.Schema;

import android.provider.BaseColumns;

/**
 * Created by v.karyagin on 07.04.2017.
 */

public class FavoriteTranslateEntry implements BaseColumns {
    public static final String TABLE_NAME = "favorites";

    public static final String COLUMN_NAME_TRANSLATE_TEXT = "translate_text";
    public static final String COLUMN_NAME_TRANSLATED_TEXT = "translated_text";
    public static final String COLUMN_NAME_TRANSLATE_LANG = "translate_lang";
    public static final String COLUMN_NAME_TRANSLATED_LANG = "translated_lang";
    public static final String COLUMN_NAME_TRANSLATE_CODE = "translate_code";
    public static final String COLUMN_NAME_TRANSLATED_CODE = "translated_code";
    public static final String COLUMN_NAME_TRANSLATE_DATE = "date";

    public long id;
    public String translateText;
    public String translatedText;
    public String translateLang;
    public String translatedLang;
    public String translateCode;
    public String translatedCode;
    public String date;
}