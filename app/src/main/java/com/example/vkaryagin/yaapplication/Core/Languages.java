package com.example.vkaryagin.yaapplication.Core;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * This class is response object for Languages method from Yandex Translate API.
 * He contain all available languages and provides working with them. You need call
 * {@link #init(String)} for working with this object.
 */
public class Languages implements Initiable {

    private List<Language> langs;
    private HashMap<String, Integer> langNumbers;
    private YaResponseCodes.YaResponse response;

    public Languages() {
        langs = new ArrayList<>();
        langNumbers = new HashMap<>();
        response = new YaResponseCodes.YaResponse(YaResponseCodes.SUCCESS);
    }

    /**
     * {@inheritDoc}
     * <br>Success JSON Example
     *{
     *  "langs": {
     *    "ru": "русский",
     *    "en": "английский",
     *    "pl": "польский",
     *    ...
     *   }
     *}
     * <br>Denied JSON Example
     *{
     * "code":401,
     *  "message":"API key is invalid"
     *}
     */
    public void init (String jsonString) {
        try {
            JSONObject res = new JSONObject(jsonString);

            if (!res.isNull("code")) {
                int responseCode = res.getInt("code");
                response = new YaResponseCodes.YaResponse(responseCode);
                if (!YaResponseCodes.isSuccess(responseCode)) return;
            }

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

    /**
     * Allows add new language to languages list that contains that object
     *
     * @param language {@link Language} object that will be added
     */
    public void add (Language language) {
        this.langs.add(language);
        this.langNumbers.put(language.getLanguageCode(), langs.size() - 1);
    }

    /**
     * Allows get serial number of language in languages list by language code. It's need where you get {@link #getLanguages()}
     * languages and you want find his index.
     *
     * @param langCode language code. Example: "ru", "en" etc.
     * @return language index in languages list
     */
    public int getLanguageNumber(String langCode) {
        if (langs.isEmpty()) return -1;
        return langNumbers.get(langCode);
    }

    /**
     * Allows get all languages that was added
     *
     * @return {@link List} of languages
     */
    public List<Language> getLanguages() { return langs; }

    /**
     * It's check that list of languages is empty. It's like {@link List#isEmpty()}
     * @return boolean value
     */
    public boolean isEmpty() { return langs.isEmpty(); }

    @Override
    public YaResponseCodes.YaResponse getResponse() { return response; }

    /**
     * This class allows contain language code and language name. For example, language code is "ru" and
     * language name is "Russian".
     */
    public class Language {

        private String languageCode;
        private String languageName;

        /**
         * Language constructor. Takes 2 {@link String} parameters for initialize.
         * @param code is language code like "ru", "en", etc.
         * @param name is language name like "Russian", "English", etc.
         */
        public Language(String code, String name) {
            this.languageCode = code;
            this.languageName = name;
        }

        /**
         * Allows get language code.
         * @return {@link String} as language code for example like "ru", "en", etc.
         */
        public String getLanguageCode() { return languageCode; }

        /**
         * Allows get language name.
         * @return {@link String} as language name for example like "Russian", "English", etc.
         */
        public String getLanguageName() { return languageName; }

        @Override
        public String toString() {
            return languageName;
        }

    }

}
