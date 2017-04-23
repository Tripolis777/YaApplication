package com.example.vkaryagin.yaapplication.Core;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.vkaryagin.yaapplication.Callable;
import com.example.vkaryagin.yaapplication.Response;

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
 * Generic класс для вызова методов Yandex Translate API.
 */
public class YaTranslateTask<T extends Initiable> extends AsyncTask<String, Integer, YaTranslateTask.ResponseObject<T>> {

    private final Callable<T> mCallback;
    private final T mObject;

    /**
     * Конструктор принимает объект, который будет инициализирован {@link Initiable} из полученного
     * ответа от сервера Yandex Translate и в случае успеха вызывает {@link Callable#done(Object)}. Если
     * произошла ошибка на уровне запроса, или Yandex Translate сервис ответил кодом, не равным
     * {@link YaResponseCodes#SUCCESS} , будет вызван {@link Callable#error(com.example.vkaryagin.yaapplication.Response)}
     * @param object объект интерфейса {@link Initiable}, у которого будет выхзвана {@link Initiable#init(String)}
     *               с JSON строкой ответа сервера
     * @param callback объект интерфейса {@link Callable}, {@link Callable#done(Object)} или {@link Callable#error(com.example.vkaryagin.yaapplication.Response)}
     *                 будут вызваны после инициализации объекта
     */
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

    /**
     * Класс, отвечающий за обработку ответа от сервера на уровне HTTP.
     */
    public static class Response extends com.example.vkaryagin.yaapplication.Response {

        public Response(int responseCode, String responseMessage) {
            this.code = responseCode;
            this.message = responseMessage;
        }

        @Override
        public String getMessage(final Context context) {
            return message;
        }

        @Override
        public int getCode() { return code; }

        @Override
        public String toString() {
            return String.format("Code: %d, Message: %s", code, message);
        }
        public String toString(Context context) { return toString(); }
    }

    /**
     * Класс оболочка, позволяющий передать инициализированный объект с его response объектом.
     * Позволяет проверить, прошла ли инициализация удачна.
     * @param <T>
     */
    protected static class ResponseObject<T extends Initiable> {
        private T object;
        private Response res;

        /**
         * Конструктор, когда объект удалось инициализировать
         * @param object объект
         */
        public ResponseObject(final T object) {
            this.object = object;
            this.res = new Response(HttpURLConnection.HTTP_OK, "OK");
        }

        /**
         * Конструктор, когда не удалось инициализировать объект.
         * @param responseCode код ответа
         * @param responseMessage сообщение
         */
        public ResponseObject(int responseCode, String responseMessage) {
            this.res = new Response(responseCode, responseMessage);
        }

        /**
         * Проверяет, прошла ли инийиализация объекта. Если HTTP запрос имеет code не равный
         * {@link HttpURLConnection#HTTP_OK} или объект пытались инициализировать с кодом отличным от
         * {@link YaResponseCodes#SUCCESS} выдаст false.
         * @return Если объект инициализирован, вернёт "true", иначе "false"
         */
        public boolean isDone() {
            return object != null &&
                    res.getCode() == HttpURLConnection.HTTP_OK &&
                    YaResponseCodes.isSuccess(object.getResponse());
        }


        /**
         * Позволяет получить инициализированный объект. Чтобы проверить, что объект был инициализирован
         * вызовите {@link #isDone()}
         * @return инициальзированный объект
         */
        public T getObject() { return this.object; }

        /**
         * Позволяет получить объект response. Если объект не дошёл до стадии инициализации, вернет {@link Response},
         * иначе вернёт {@link com.example.vkaryagin.yaapplication.Core.YaResponseCodes.YaResponse}
         * @return возвращает объект {@link com.example.vkaryagin.yaapplication.Response}
         */
        public com.example.vkaryagin.yaapplication.Response getResponse() {
            if (object == null)
                return res;
            return object.getResponse();
        }
    }
}
