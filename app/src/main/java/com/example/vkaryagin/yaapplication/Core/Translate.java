package com.example.vkaryagin.yaapplication.Core;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by tripo on 3/27/2017.
 */

public class Translate {

    private List<String> text;
    private int responseCode;
    private String language;

    public Translate() {
        text = new ArrayList<>();
        responseCode = -1;
    }

    public void init(String jsonString) {

        /** Request Example
         "code": 200,
         "lang": "en-ru",
         "text": [
            "Здравствуй, Мир!"
         ]
        */
        try {
            JSONObject res = new JSONObject(jsonString);
            responseCode = res.getInt("code");

            if (responseCode != 200) return; // TODO: нужно обработать ситуацию + добавить коды + константы

            JSONArray translateVariants = res.getJSONArray("text");
            for(int i = 0; i < translateVariants.length(); i++)
                text.add(translateVariants.getString(i));

            language = res.getString("lang");
        } catch(JSONException e) {
            Log.println(Log.ERROR, "JSON", "Translate, json cant parse.");
            e.printStackTrace();
        }
    }

    public List<String> getTranslatedText() { return text; }

    public boolean isEmpty() { return text.isEmpty(); }

    public boolean checkResponseCode() {   // TODO: future checkResponseCode(Context context) for alert messages
        return responseCode != 200;
    }

    public static class Params {
        private String text;
        private String language;
        private String format;
        private String options;

        public Params(String text, String languageIn, String languageOut) {
            this._setText(text);
            this._setLanguage(languageIn, languageOut);
            this.format = new String();
            this.options = new String();
        }

        private void _setText(String text) {
//            try {
//                this.text = URLEncoder.encode(text, "UTF-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
            this.text = text;
        }

        private void _setLanguage(String langIn, String langOut) {
            this.language = langIn + "-" + langOut;
        }

        public Params setText(String text) {
            this._setText(text);
            return this;
        }

        public Params setLanguage(String languageIn, String languageOut) {
            this._setLanguage(languageIn, languageOut);
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
    }
}
