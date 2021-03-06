package com.example.vkaryagin.yaapplication.Database.Schema;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.vkaryagin.yaapplication.Core.Languages;
import com.example.vkaryagin.yaapplication.Core.Translate;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс оболочка для таблицы записи таблицы history. Содержит в себе имена всех полей и позволяет
 * инициализировать объект таблицы из объектf {@link Translate}
 */
public class HistoryTranslateEntry implements Record {
    public static final String TABLE_NAME = "history";

    public static final String COLUMN_NAME_TRANSLATE_TEXT = "translate_text";
    public static final String COLUMN_NAME_TRANSLATED_TEXT = "translated_text";
    public static final String COLUMN_NAME_TRANSLATE_LANG = "translate_lang";
    public static final String COLUMN_NAME_TRANSLATED_LANG = "translated_lang";
    public static final String COLUMN_NAME_TRANSLATE_CODE = "translate_code";
    public static final String COLUMN_NAME_TRANSLATED_CODE = "translated_code";
    public static final String COLUMN_NAME_TRANSLATE_DATE = "date";
    public static final String COLUMN_NAME_IS_FAVORITE = "favorite";

    public long id;
    public String translateText;
    public String translatedText;
    public String translateLang;
    public String translatedLang;
    public String translateCode;
    public String translatedCode;
    public String date;
    public Boolean favorite;

    private List<String> translatedTexts;

    /**
     * Позволяет установить язык перевода для объекта таблицы history
     * @param language язык перевода
     */
    public void setTranslatedLanguage(@NonNull Languages.Language language) {
        this.translatedLang = language.getLanguageName();
        this.translatedCode = language.getLanguageCode();
    }


    /**
     * Позволяет установить язык переводимого текста
     * @param language язык переводимого текста
     */
    public void setTranslateLanguage(@NonNull Languages.Language language) {
        this.translateLang = language.getLanguageName();
        this.translateCode = language.getLanguageCode();
    }

    /**
     * Позволяет преобразовать объект {@link Translate} в объект записы таблицы history
     * @param translate инициализированный объект запроса translate в Yandex Translation API
     */
    public void setTranslate(@NonNull Translate translate) {
        this.translateText = translate.getTranslateText();
        this.translatedText = translate.getTranslatedJSONString();
        this.translatedTexts = translate.getTranslatedTexts();
        setTranslateLanguage(translate.getTranslateLanguage());
        setTranslatedLanguage(translate.getTranslatedLanguage());
    }

    /**
     * Позволяет получить список переводов установленного текста. Преобразует JSON Array строку, которая
     * хранится в записи history в {@link List}
     * @return список переводов текста
     */
    public List<String> getTranslatedTexts() {
        Log.i("HistoryTranslateEntry", "[getTranslatedTexts] text: " + translatedText);

        if (translatedTexts == null) {
            if (translatedText == null || translatedText.isEmpty()) return new ArrayList<>();
            try {
                this.translatedTexts = new ArrayList<>();
                JSONArray list = new JSONArray(translatedText);
                Log.d("HistoryTranslateEntry", "[getTranslatedTexts] list: " + list.length());
                for (int i = 0; i < list.length(); i++) {
                    translatedTexts.add(list.getString(i));
                    Log.d("HistoryTranslateEntry", "[getTranslatedTexts] add item: " + list.getString(i));
                }
            } catch (JSONException e) {
                Log.e("HistoryTranslateEntry", "[getTranslatedTexts] Cants parse json array translated list! " +
                        "ERROR:" + e.getMessage());
                e.printStackTrace();
            }
        }

        Log.i("HistoryTranslateEntry", "[getTranslatedTexts] translatedTexts size: "
                + translatedTexts.size());

        return this.translatedTexts;
    }

    /**
     * Позволяет получить первый по списку перевод из списка возможных
     * @return строка перевода
     */
    public String getTranslateFirst() {
        Log.i("HistortTranslateEntry", "getTranslateFirst");
        if (!this.getTranslatedTexts().isEmpty())
            return this.getTranslatedTexts().get(0);
        return null;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME_TRANSLATE_TEXT, translateText);
        values.put(COLUMN_NAME_TRANSLATED_TEXT, translatedText);
        values.put(COLUMN_NAME_TRANSLATE_LANG, translateLang);
        values.put(COLUMN_NAME_TRANSLATED_LANG, translatedLang);
        values.put(COLUMN_NAME_TRANSLATE_CODE, translateCode);
        values.put(COLUMN_NAME_TRANSLATED_CODE, translatedCode);
        //values.put(COLUMN_NAME_TRANSLATE_DATE, date);
        values.put(COLUMN_NAME_IS_FAVORITE, favorite);

        return values;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getColumnNameNullable() { return null; }

    @Override
    public void setId(long id) {
        this.id = id;
    }

}
