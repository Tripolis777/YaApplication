package com.example.vkaryagin.yaapplication.Core;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.vkaryagin.yaapplication.Core.Tasks.GetDetectLanguageTask;
import com.example.vkaryagin.yaapplication.Core.Tasks.GetLanguagesTask;
import com.example.vkaryagin.yaapplication.Core.Tasks.GetTranslateTask;
import com.example.vkaryagin.yaapplication.R;

import java.util.HashMap;

/**
 * Created by tripo on 3/27/2017.
 */

//TODO: Подумать о необходимости этого класса и почему он singleton
//TODO: обязательные и опциональные переменные запроса нужно как-то вынести если это возможно

public class YaTranslateManager {

    private static YaTranslateManager instance;
    private Languages languages;
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
            callback.callback(languages);
            return;
        }

        GetLanguagesTask getLanguagesTask = new GetLanguagesTask(new Callable<Languages>() {
            @Override
            public void callback(Languages value) {
                languages = value;
                callback.callback(value);
            }
        });
        getLanguagesTask.execute(YandexHttpApi.getLanguagesLink(context));
    }

    public void executeTranslate(Translate.Params params, final Context context,
                                 final Callable<Translate> callback) {
        //Some cache code

        GetTranslateTask getTranslateTask = new GetTranslateTask(callback);
        getTranslateTask.execute(YandexHttpApi.getTranslateLink(context, params));
    }

    public void executeDetect(String text, final Context context,
                              final Callable<DetectLanguage> callback) {

        GetDetectLanguageTask getDetectLanguageTask = new GetDetectLanguageTask(callback);
        getDetectLanguageTask.execute(YandexHttpApi.getDetectLink(context, text));
    }

    public void resetLanguages() {
        this.languages = new Languages();
    }

    public Languages getLanguages() { return this.languages; }

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
