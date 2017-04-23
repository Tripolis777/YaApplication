package com.example.vkaryagin.yaapplication;

import android.content.Context;

/**
 * Абстрактный класс для обработки ответов. Содердит код ответа и сообщение, если токовое имеется.
 */
public abstract class Response {
    protected int code;
    protected String message;
    public abstract String getMessage(final Context context);
    public abstract int getCode();
    public abstract String toString(Context context);
}
