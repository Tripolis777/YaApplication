package com.example.vkaryagin.yaapplication.Database.Tasks;

import android.content.ContentValues;

/**
 * Created by v.karyagin on 14.04.2017.
 */

public class UpdateRequest {
    public String selection;
    public String[] selectionArgs;
    public ContentValues values;
    public String tableName;

    public UpdateRequest(String tableName, ContentValues values, String selection, String[] selectionArgs) {
        this.tableName = tableName;
        this.values = values;
        this.selection = selection;
        this.selectionArgs = selectionArgs;
    }

}
