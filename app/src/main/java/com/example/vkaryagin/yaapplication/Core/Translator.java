package com.example.vkaryagin.yaapplication.Core;

import android.content.Context;
import android.os.AsyncTask;

import com.example.vkaryagin.yaapplication.Core.Tasks.GetLanguagesTask;
import com.example.vkaryagin.yaapplication.Fragments.TranslateFragment;
import com.example.vkaryagin.yaapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by tripo on 3/19/2017.
 */

//TODO: Подумать о необходимости этого класса и почему он singleton
//TODO: обязательные и опциональные переменные запроса нужно как-то вынести если это возможно
public class Translator {

    private static String defaultLanguage;
    private Context context;
    private ArrayList<Language> langs;
    private HashMap<String, Integer> langNumbers;

    public Translator(Context context) {
        this.context = context;
        langs = new ArrayList<>();
        langNumbers = new HashMap<>();
        defaultLanguage = "EN";
    }

    public void setLanguages(final Callable<ArrayList<Language>> callback) {
        if (!langs.isEmpty()) {
            callback.callback(langs);
            return;
        }

        GetLanguagesTask getLanguagesTask = new GetLanguagesTask(new Callable<ArrayList<Language>>() {
                @Override
                public void callback(ArrayList<Language> value) {
                    langs = value;
                    Translator.this.initializeLangNumbers();

                    callback.callback(value);
                }
            });
        getLanguagesTask.execute(this.getLanguagesLink());
    }

    public ArrayList<Language> getLanguages() {
        return langs;
    }

    public String getLanguagesLink() {
        StringBuilder sb = new StringBuilder();

        sb.append(context.getString(R.string.yt_api_main_link)).append("/")
            .append(context.getString(R.string.yt_version)).append("/")
            .append(context.getString(R.string.yt_response_json)).append("/")
            .append(context.getString(R.string.yt_get_langs,
                    context.getResources().getConfiguration().locale.getLanguage(),
                    context.getString(R.string.yt_api_key)));

        return sb.toString();
    }

    public int getLanguageNumber(String langCode) {
        if (langs.size() != langCode.length()) {
            this.initializeLangNumbers();
        }
        return langNumbers.get(langCode);
    }

    private void initializeLangNumbers(){
        langNumbers.clear();
        for (int i = 0; i < langs.size(); i++)
             langNumbers.put(langs.get(i).getLanguageCode(), i);
    }

    //TODO: нужно дописать функцию
    public static String[] translate(String text) {
        return translate(text, defaultLanguage);
    }
    public static String[] translate(String text, String lang) {
        return null;
    }

}
