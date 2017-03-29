package com.example.vkaryagin.yaapplication.Core;

import android.content.Context;

/**
 * Created by v.karyagin on 29.03.2017.
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

    public static String getCodeDescription(int code, Context context) {
        String stringName = String.format("ya_response_code_%d", code);
        int stringId = context.getResources().getIdentifier(stringName, "string", context.getPackageName());

        if (stringId == 0) return "Response code not found";
        return context.getString(stringId);
    }
}
