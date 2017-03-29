package com.example.vkaryagin.yaapplication.Core.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.vkaryagin.yaapplication.Core.Callable;
import com.example.vkaryagin.yaapplication.Core.Languages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by v.karyagin on 3/21/17.
 */

public class GetLanguagesTask extends  AsyncTask<String, Integer, Languages> {

    private Callable<Languages> object;

    public GetLanguagesTask(Callable<Languages> object) {
        super();
        this.object = object;
    }

    @Override
    protected Languages doInBackground(String... strings) {
        Languages result = new Languages();
        for (int i = 0; i < strings.length; i++) {
            String link = strings[i];

            try {
                URL url = new URL(link);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

                Log.println(Log.INFO, "CONNECTION", String.format("Response code: %d", connection.getResponseCode()));

                if(connection.getResponseCode() != HttpURLConnection.HTTP_OK) return result;

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
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(Languages result) {
        object.callback(result);
    }


//    public static class ResponseObject<T> {
//        private T object;
//        private int responseCode;
//        private String responseMessage;
//
//
//        public ResponseObject(T object) {
//            this.object = object;
//            responseCode = HttpURLConnection.HTTP_OK;
//        }
//
//        public ResponseObject(int responseCode, String responseMessage) {
//            this.responseCode = responseCode;
//            this.responseMessage = responseMessage;
//        }
//
//        public boolean isDone() { return responseCode == HttpURLConnection.HTTP_OK; }
//        public T getObject() { return this.object; }
//        public int getResponseCode() { return this.responseCode; }
//        public String getResponseMessage() { return this.responseMessage; }
//    }
}