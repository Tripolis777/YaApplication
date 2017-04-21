package com.example.vkaryagin.yaapplication.Core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.vkaryagin.yaapplication.ApplicationUtils;
import com.example.vkaryagin.yaapplication.Callable;
import com.example.vkaryagin.yaapplication.R;

/**
 * Created by tripo on 3/27/2017.
 */

public class YaTranslateManager {

    private static YaTranslateManager instance;
    private final Languages languages;
   // private HashMap<String, Translate> lastRequests;   // Future cache last 10 request maybe

    private YaTranslateManager() {
        languages = new Languages();
    }

    public static YaTranslateManager getInstance() {
        if (instance == null)
            instance = new YaTranslateManager();
        return instance;
    }

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

    public void executeTranslate(Translate.Params params, final Context context,
                                 final Callable<Translate> callback) {
        //Some cache code
        if (!this.checkNetwork(context)) {
            ApplicationUtils.throwAlertDialog(context, R.string.network_not_connection_title,
                    R.string.network_not_connection_message);
            return;
        }

        YaTranslateTask<Translate> getTranslateTask = new YaTranslateTask<>(new Translate(params), callback);
        getTranslateTask.execute(YandexHttpApi.getTranslateLink(context, params));
    }

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
