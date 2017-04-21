package com.example.vkaryagin.yaapplication;

import android.content.Context;

/**
 * Created by v.karyagin on 14.04.2017.
 */

public abstract class Response {
    protected int code;
    protected String message;
    public abstract String getMessage(final Context context);
    public abstract int getCode();
    public abstract String toString(Context context);
}
