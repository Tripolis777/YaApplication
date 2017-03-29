package com.example.vkaryagin.yaapplication.Core;

import android.os.AsyncTask;
import android.telecom.Call;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by v.karyagin on 29.03.2017.
 */

public class YaTranslateTask<T extends Initiable> extends AsyncTask<String, Integer, YaTranslateTask.ResponseObject<T>> {

    private final Callable<T> mCallback;
    private final T mObject;

    public YaTranslateTask(final T object, final Callable<T> callback) {
        super();
        this.mObject = object;
        this.mCallback = callback;
    }

    @Override
    protected ResponseObject<T> doInBackground(String[] strings) {
        for (int i = 0; i < strings.length; i++) {
            String link = strings[i];

            try {
                URL url = new URL(link);
                URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                url = uri.toURL();
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("POST");

                Log.println(Log.INFO, "CONNECTION", String.format("Response code: %d", connection.getResponseCode()));

                if(connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                    return new ResponseObject(connection.getResponseCode(), connection.getResponseMessage());


                StringBuilder stringBuilder = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line;
                while ((line = br.readLine()) != null){
                    stringBuilder.append(line);
                }
                br.close();

                String response = stringBuilder.toString();
                mObject.init(response);

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
        return new ResponseObject(mObject);
    }

    @Override
    protected void onPostExecute(ResponseObject<T> result) {

        if (result.isDone())
            mCallback.done(result.getObject());
        else
            mCallback.error(result.getResponse());
    }

    public static class Response {
        public int responseCode;
        public String responseMessage;

        public Response(int responseCode, String responseMessage) {
            this.responseCode = responseCode;
            this.responseMessage = responseMessage;
        }
    }

    protected static class ResponseObject<T> {
        private T object;
        private Response res;


        public ResponseObject(T object) {
            this.object = object;
            this.res = new Response(HttpURLConnection.HTTP_OK, "OK");
        }

        public ResponseObject(int responseCode, String responseMessage) {
            this.res = new Response(responseCode, responseMessage);
        }

        public boolean isDone() { return res.responseCode == HttpURLConnection.HTTP_OK; }
        public T getObject() { return this.object; }
        public Response getResponse() { return this.res; }
    }
}
