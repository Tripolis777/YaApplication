package com.example.vkaryagin.yaapplication;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by tripo on 4/20/2017.
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