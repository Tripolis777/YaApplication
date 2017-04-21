package com.example.vkaryagin.yaapplication.Core;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tripo on 3/27/2017.
 */

public class Translate implements Initiable, Serializable {

    private List<String> translatedTexts;
    private String language;
    private final Params requestParams;
    private String translatedJSONString;
    private YaResponseCodes.YaResponse response;

    public Translate(Params requestParams) {
        this.requestParams = requestParams;
        translatedTexts = new ArrayList<>();
    }

    /** Request Example
     "code": 200,
     "lang": "en-ru",
     "text": [
     "Здравствуй, Мир!"
     ]
     */
    public void init(String jsonString) {
        try {
            JSONObject res = new JSONObject(jsonString);
            int responseCode = res.getInt("code");
            response = new YaResponseCodes.YaResponse(responseCode);
            if (!YaResponseCodes.isSuccess(response)) return;

            JSONArray translateVariants = res.getJSONArray("text");
            translatedJSONString = translateVariants.toString();
            for (int i = 0; i < translateVariants.length(); i++)
                translatedTexts.add(translateVariants.getString(i));

            language = res.getString("lang");
        } catch(JSONException e) {
            Log.println(Log.ERROR, "JSON", "Translate, json cant parse.");
            e.printStackTrace();
        }
    }

    public List<String> getTranslatedTexts() { return translatedTexts; }
    public String getTranslatedJSONString() { return translatedJSONString; }
    public String getTranslateText() { return requestParams.getText(); }
    public String getFirstTranslatedText() { return translatedTexts.get(0); }
    public Languages.Language getTranslateLanguage() { return requestParams.getTranslateLang(); }
    public Languages.Language getTranslatedLanguage() { return requestParams.getTranslatedLang(); }

    public boolean isEmpty() { return translatedTexts.isEmpty(); }

    @Override
    public YaResponseCodes.YaResponse getResponse() { return response; }

    public static class Params {
        private String text;
        private String language;
        private String format;
        private String options;

        private Languages.Language translateLang;
        private Languages.Language translatedLang;

        public Params(String text, Languages.Language translateLang, Languages.Language translatedLang) {
            this.text = text;
            this._setLanguage(translateLang, translatedLang);
            this.format = new String();
            this.options = new String();
        }

        private void _setLanguage(Languages.Language translateLang, Languages.Language translatedLang) {
            this.language = translateLang.getLanguageCode() + "-" + translatedLang.getLanguageCode();
            this.translateLang = translateLang;
            this.translatedLang = translatedLang;
        }

        public Params setText(String text) {
            this.text = text;
            return this;
        }

        public Params setLanguage(Languages.Language translateLang, Languages.Language translatedLang) {
            this._setLanguage(translateLang, translatedLang);
            return this;
        }

        public Params setFormat(String format) {
            this.format = format;
            return this;
        }

        public Params setOptions(String options) {
            this.options = options;
            return this;
        }

        public String getText() { return this.text; }
        public String getLanguage() { return this.language; }
        public String getFormat() { return this.format; }
        public String getOptions() { return this.options; }
        public Languages.Language getTranslateLang() { return this.translateLang; }
        public Languages.Language getTranslatedLang() { return this.translatedLang; }
    }
}
