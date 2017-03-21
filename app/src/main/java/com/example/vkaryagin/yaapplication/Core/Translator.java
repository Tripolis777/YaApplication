package com.example.vkaryagin.yaapplication.Core;

import android.content.Context;
import android.os.AsyncTask;

import com.example.vkaryagin.yaapplication.R;

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

//TODO: Подумать о необходимости этого класса и почему он singleton
//TODO: обязательные и опциональные переменные запроса нужно как-то вынести если это возможно
public class Translator {

    private static String defaultLanguage;
    private Context context;

    public Translator(Context context) {
        this.context = context;
        defaultLanguage = "EN";
    }

    public List<String> getLanguages() {
        try {
            URL url = new URL(this.getLanguagesLink());
            GetLanguagesTask getLanguagesTask = new GetLanguagesTask();
            return getLanguagesTask.doInBackground(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
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

    //TODO: нужно дописать функцию
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
