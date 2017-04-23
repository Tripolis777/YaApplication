package com.example.vkaryagin.yaapplication.Core;

import android.content.Context;

import com.example.vkaryagin.yaapplication.R;
import com.example.vkaryagin.yaapplication.Response;

/**
 * This class contains all available Yandex Translate API's response codes
 */
public class YaResponseCodes {
    /**
     * Code variants list
     * 200 - Операция выполнена успешно
     * 401 - Неправильный API-ключ
     * 402 - API-ключ заблокирован
     * 404 - Превышено суточное ограничение на объем переведенного текста
     * 413 - Превышен максимально допустимый размер текста
     * 422 - Текст не может быть переведен
     * 501 - Заданное направление перевода не поддерживается
     */

    public static final int SUCCESS = 200;
    public static final int INVALID_API_KEY = 401;
    public static final int BLOCKED_API_KEY = 402;
    public static final int EXCEEDED_DAY_LIMIT = 404;
    public static final int EXCEEDED_TEXT_LIMIT = 413;
    public static final int CANT_TRANSLATE = 422;
    public static final int INCORRECT_TRANSLATE_LANGUAGE = 501;

    /**
     * Checks response code for {@link YaResponse} object for {@link #SUCCESS}
     * @param object response object for Yandex Translate API
     * @return true if response code equal {@link #SUCCESS} else false
     */
    public static boolean isSuccess(YaResponse object) {
        return isSuccess(object.getCode());
    }

    /**
     * Checks Yandex Translation API's response code for {@link #SUCCESS}
     * @param responseCode Yandex Translate API response code
     * @return true if response code equal {@link #SUCCESS} else false
     */
    public static boolean isSuccess(int responseCode) {
        return responseCode == SUCCESS;
    }

    /**
     * Like {@link #getResponseDescription(int, Context)}, but take response code from {@link YaResponse} object.
     * @param object response object for Yandex Translation API
     * @param context {@link Context}
     * @return description for response code.
     */
    public static String getResponseDescription(YaResponse object, final Context context) {
        return getResponseDescription(object.getCode(), context);
    }

    /**
     * Allow get response code description
     * @param responseCode Yandex Translation API's response code
     * @param context {@link Context}
     * @return description for response code
     */
    public static String getResponseDescription(int responseCode, final Context context) {
        String stringName = String.format("ya_response_code_%d", responseCode);
        int stringId = context.getResources().getIdentifier(stringName, "string", context.getPackageName());

        if (stringId == 0) return "Response code not found";
        return context.getString(stringId);
    }

    /**
     * Allows get title that unities all response codes.
     * @param context
     * @return string of title
     */
    public static String getTitle(final Context context) { return context.getString(R.string.ya_response_code_title); }

    public static class YaResponse extends Response{
        public YaResponse(int responseCode) {
            code = responseCode;
        }

        @Override
        public String getMessage(final Context context) {
            return getResponseDescription(code, context);
        }

        @Override
        public int getCode() { return this.code; }

        public String toString(final Context context) {
            return String.format("Code %d: response message : %s", getCode(), getMessage(context));
        }
    }
}
