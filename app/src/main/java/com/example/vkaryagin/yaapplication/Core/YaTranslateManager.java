package com.example.vkaryagin.yaapplication.Core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.vkaryagin.yaapplication.ApplicationUtils;
import com.example.vkaryagin.yaapplication.Callable;
import com.example.vkaryagin.yaapplication.R;
import com.example.vkaryagin.yaapplication.Response;

/**
 * Этот singleton класс предоставляет интерфейс работы с Yandex Translation API. В случае отсутствия интернет
 * соединения будет показан {@link android.support.v7.app.AlertDialog}. Работа с результатом запроса
 * производится с помощью вызова callback функции через {@link Callable} интерфейс.
 */
public class YaTranslateManager {

    private static YaTranslateManager instance;
    private final Languages languages;

    private YaTranslateManager() {
        languages = new Languages();
    }

    /**
     * Метод для получение объекта класса
     * @return объект класса {@link YaTranslateManager}
     */
    public static YaTranslateManager getInstance() {
        if (instance == null)
            instance = new YaTranslateManager();
        return instance;
    }

    /**
     * Асинхронный вызов метода getLangs в Yandex Translation API, который получет доступные языки.
     * В случае отсутствия интернет соединения будет показан {@link android.support.v7.app.AlertDialog}.
     * @param context context
     * @param callback объект интерфейса {@link Callable}. В случае успеха будет вызвана функция
     *                 {@link Callable#done(Object)}, иначе {@link Callable#error(Response)}
     */
    public void executeLanguages(final Context context, final Callable<Languages> callback) {
        if (!languages.isEmpty()) {
            callback.done(languages);
            return;
        }

        if (!this.checkNetwork(context)) {
            ApplicationUtils.throwAlertDialog(context, R.string.network_not_connection_title,
                    R.string.network_not_connection_message);
            return;
        }

        YaTranslateTask<Languages> getLanguagesTask = new YaTranslateTask<>(languages, callback);
        getLanguagesTask.execute(YandexHttpApi.getLanguagesLink(context));
    }

    /**
     * Ассинхронный вызов метода translate в Yandex Translation API, вызываемый с полученными параметрами.
     * В случае отсутствия интернет соединения будет показан {@link android.support.v7.app.AlertDialog}.
     *
     * @param params параметры запроса
     * @param context context
     * @param callback объект интерфейса {@link Callable}. В случае успеха будет вызвана функция
     *                 {@link Callable#done(Object)}, иначе {@link Callable#error(Response)}
     */
    public void executeTranslate(Translate.Params params, final Context context,
                                 final Callable<Translate> callback) {
        if (!this.checkNetwork(context)) {
            ApplicationUtils.throwAlertDialog(context, R.string.network_not_connection_title,
                    R.string.network_not_connection_message);
            return;
        }

        YaTranslateTask<Translate> getTranslateTask = new YaTranslateTask<>(new Translate(params), callback);
        getTranslateTask.execute(YandexHttpApi.getTranslateLink(context, params));
    }

    /**
     * Ассинхронный вызов метода detect в Yandex Translate API, который определяет язык текста.
     * В случае отсутствия интернет соединения будет показан {@link android.support.v7.app.AlertDialog}.
     * @param text переводимый текст
     * @param context context
     * @param callback объект интерфейса {@link Callable}. В случае успеха будет вызвана функция
     *                 {@link Callable#done(Object)}, иначе {@link Callable#error(Response)}
     */
    public void executeDetect(String text, final Context context,
                              final Callable<DetectLanguage> callback) {

        if (!this.checkNetwork(context))  {
            ApplicationUtils.throwAlertDialog(context, R.string.network_not_connection_title,
                    R.string.network_not_connection_message);
            return;
        }

        YaTranslateTask<DetectLanguage> getDetectLanguageTask = new YaTranslateTask<>(new DetectLanguage(), callback);
        getDetectLanguageTask.execute(YandexHttpApi.getDetectLink(context, text));
    }

    /**
     * Возвращает объект {@link Languages}, который содержит в себе доступные языки. Если {@link #executeLanguages(Context, Callable)}
     * не был вызван, список языков будет пуст.
     * @return {@link Languages} объект для работы с языками
     */
    public Languages getLanguages() { return this.languages; }

    private boolean checkNetwork(Context context) {
        boolean connected = false;

        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null)
            connected = networkInfo.isConnected();

        if (!connected)
           Toast.makeText(context, "Not connected to network", Toast.LENGTH_SHORT);

        return connected;
    }

    private static class YandexHttpApi {

        @NonNull
        public static String getLanguagesLink(final Context context) {
            StringBuilder sb = getLinkBuilder(context);

            sb.append(context.getString(R.string.yt_get_langs,
                    context.getString(R.string.yt_api_key),
                    context.getResources().getConfiguration().locale.getLanguage()));

            return sb.toString();
        }

        @NonNull
        public static String getTranslateLink(final Context context, Translate.Params params) {
            StringBuilder sb = getLinkBuilder(context);

            sb.append(context.getString(R.string.yt_translate,
                    context.getString(R.string.yt_api_key),
                    params.getText(), params.getLanguage()
                    //params.getFormat(), params.getOptions()
            ));

            return sb.toString();
        }

        @NonNull
        public static String getDetectLink(final Context context, String text) {
            StringBuilder sb = getLinkBuilder(context);

            sb.append(context.getString(R.string.yt_detect_lang,
                    context.getString(R.string.yt_api_key),
                    text
            ));

            return sb.toString();
        }

        private static StringBuilder getLinkBuilder(final Context context) {
            StringBuilder sb = new StringBuilder();

            sb.append(context.getString(R.string.yt_api_main_link)).append("/")
                    .append(context.getString(R.string.yt_version)).append("/")
                    .append(context.getString(R.string.yt_response_json)).append("/");

            return sb;
        }
    }
}
