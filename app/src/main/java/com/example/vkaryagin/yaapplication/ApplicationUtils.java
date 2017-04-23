package com.example.vkaryagin.yaapplication;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Всякие функции, вызывать которые хочется везде, а копипастить не хочется.
 */
public final class ApplicationUtils {

    public static void throwAlertDialog(final Context context, int title, int message) {
        throwAlertDialog(context, context.getString(title), context.getString(message));
    }

    public static void throwAlertDialog(final Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setTitle(title);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
