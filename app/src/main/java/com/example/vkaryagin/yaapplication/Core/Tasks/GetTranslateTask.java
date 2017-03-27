package com.example.vkaryagin.yaapplication.Core.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.vkaryagin.yaapplication.Core.Callable;
import com.example.vkaryagin.yaapplication.Core.Languages;
import com.example.vkaryagin.yaapplication.Core.Translate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by tripo on 3/27/2017.
 */

public class GetTranslateTask extends AsyncTask<String, Integer, Translate> {

    private Callable<Translate> object;

    public GetTranslateTask(Callable<Translate> object) {
        super();
        this.object = object;
    }

    @Override
    protected Translate doInBackground(String... strings) {
        Translate result = new Translate();
        for (int i = 0; i < strings.length; i++) {
            String link = strings[i];

            try {
                URL url = new URL(link);
                URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                url = uri.toURL();
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("POST");

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
                result.init(response);

            } catch (MalformedURLException e) {
                Log.println(Log.ERROR, "URL", e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                Log.println(Log.ERROR, "IO", e.getMessage());
                e.printStackTrace();
            } catch (URISyntaxException e) {
                Log.println(Log.ERROR, "URI", e.getMessage());
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(Translate result) {
        object.callback(result);
    }

}
