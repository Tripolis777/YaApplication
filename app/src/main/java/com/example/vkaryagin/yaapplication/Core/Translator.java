package com.example.vkaryagin.yaapplication.Core;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by tripo on 3/19/2017.
 */

public class Translator {

    private static Translator instance;
    private static String defaultLanguage;

    private static final String GET_LANGUAGES_LINK = "https://translate.yandex.net/api/v1.5/tr.json/getLangs?";
    private static final String KEY = "trnsl.1.1.20170319T155623Z.19b34397c41f30d0.d868b6b5bbd2026a45f6ae0889f8a601f82eb776";

    private Translator() {
        defaultLanguage = "EN";
    }

    public static Translator getInstance() {
        if (instance == null) {
            instance = new Translator();
        }
        return instance;
    }

    public List<String> getLanguages() {
        StringBuilder sb = new StringBuilder();
        sb.append(GET_LANGUAGES_LINK).append("ui=").append("en").append("&key=").append(KEY);
        try {
            URL url = new URL(sb.toString());
            GetLanguagesTask getLanguagesTask = new GetLanguagesTask();
            return getLanguagesTask.doInBackground(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getLanguagesLink() {
        StringBuilder sb = new StringBuilder();
        sb.append(GET_LANGUAGES_LINK).append("ui=").append("ru").append("&key=").append(KEY);
        return sb.toString();
    }

    public void setLanguages (Spinner spinner) {
        List<String> langs = getLanguages();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(spinner.getContext(), android.R.layout.simple_spinner_item, langs);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }

    public static String[] translate(String text) {
        return translate(text, defaultLanguage);
    }

    public static String[] translate(String text, String lang) {
        return null;
    }

    private class GetLanguagesTask extends AsyncTask<URL, Integer, List<String>> {

        @Override
        protected List<String> doInBackground(URL... urls) {
            List<String> result = new ArrayList<>();
            for (int i = 0; i < urls.length; i++) {
                URL url = urls[i];
                StringBuilder stringBuilder = new StringBuilder();

                try {
                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    String line;
                    while ((line = br.readLine()) != null){
                        stringBuilder.append(line);
                    }
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    String response = stringBuilder.toString();
                    JSONObject res = new JSONObject(response);
                    JSONObject langs = res.getJSONObject("langs");

                    for (Iterator<String> it = langs.keys(); it.hasNext(); )
                        result.add(it.next());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return result;
        }
    }
}
