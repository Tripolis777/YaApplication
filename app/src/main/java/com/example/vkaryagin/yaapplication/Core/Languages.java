package com.example.vkaryagin.yaapplication.Core;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by tripo on 3/27/2017.
 */

public class Languages implements Initiable {

    /**
     * Список code
     * 401 - Неправильный API-ключ
     * 402 - API-ключ заблокирован
     */

    private List<Language> langs;
    private HashMap<String, Integer> langNumbers;

    public Languages() {
        langs = new ArrayList<>();
        langNumbers = new HashMap<>();
    }

    public void init (String jsonString) {
        try {
            JSONObject res = new JSONObject(jsonString);
            JSONObject langs = res.getJSONObject("langs");

            for (Iterator<String> it = langs.keys(); it.hasNext(); ) {
                String langCode = it.next();
                String lang = langs.getString(langCode);
                Log.println(Log.DEBUG, this.getClass().getName(), String.format("Lang Code: %s, Lang Desc: %s", langCode, lang));

                this.add(new Language(langCode, lang));
            }
        } catch (JSONException e) {
            Log.println(Log.ERROR, "JSON", "Languages, json cant parse.");
            e.printStackTrace();
        }
    }

    public void add (Language language) {
        this.langs.add(language);
        this.langNumbers.put(language.getLanguageCode(), langs.size() - 1);
    }

    public int getLanguageNumber(String langCode) {
        if (langs.isEmpty()) return -1;
        return langNumbers.get(langCode);
    }

    public List<Language> getLanguages() { return langs; }

    public boolean isEmpty() { return langs.isEmpty(); }

    public class Language {

        private String languageCode;
        private String languageName;

        public Language(String code, String name) {
            this.languageCode = code;
            this.languageName = name;
        }

        public String getLanguageCode() { return languageCode; }
        public String getLanguageName() { return languageName; }

        @Override
        public String toString() {
            return languageName;
        }

    }

}
