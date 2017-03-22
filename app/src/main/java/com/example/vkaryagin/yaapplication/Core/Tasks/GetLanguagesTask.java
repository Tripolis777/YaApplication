package com.example.vkaryagin.yaapplication.Core.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.vkaryagin.yaapplication.Core.Callable;
import com.example.vkaryagin.yaapplication.Core.Language;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by v.karyagin on 3/21/17.
 */

public class GetLanguagesTask extends  AsyncTask<String, Integer, ArrayList<Language>> {

    private Callable<ArrayList<Language>> object;

    public GetLanguagesTask(Callable<ArrayList<Language>> object) {
        super();
        this.object = object;
    }

    @Override
    protected ArrayList<Language> doInBackground(String... strings) {
        ArrayList<Language> result = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            String link = strings[i];

            try {
                URL url = new URL(link);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

                Log.println(Log.INFO, "CONNECTION", String.format("Response code: %d", connection.getResponseCode()));

                if(connection.getResponseCode() != 200) return result;

                StringBuilder stringBuilder = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line;
                while ((line = br.readLine()) != null){
                    stringBuilder.append(line);
                }
                br.close();

                String response = stringBuilder.toString();
                JSONObject res = new JSONObject(response);
                JSONObject langs = res.getJSONObject("langs");    // TODO: Is "langs" magic?

                for (Iterator<String> it = langs.keys(); it.hasNext(); ) {
                    String langCode = it.next();
                    String lang = langs.getString(langCode);
                    Log.println(Log.DEBUG, this.getClass().getName(), String.format("Lang Code: %s, Lang Desc: %s", langCode, lang));

                    result.add(new Language(langCode, lang));
                }

            } catch (MalformedURLException e) {
                Log.println(Log.ERROR, "URL", e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                Log.println(Log.ERROR, "IO", e.getMessage());
                e.printStackTrace();
            } catch (JSONException e) {
                Log.println(Log.ERROR, "JSON", e.getMessage());
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<Language> result) {
        object.callback(result);
    }
}