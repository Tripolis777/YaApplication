package com.example.vkaryagin.yaapplication.Database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

/**
 * Created by tripo on 4/3/2017.
 */

public class DatabaseInsertTask extends AsyncTask<ContentValues, Integer, Boolean> {

    private String tableName;
    private String columnNameNullable;
    private SQLiteDatabase sqLiteDatabase;

    public DatabaseInsertTask(YaAppDBOpenHelper dbHelper, String tableName, String columnNameNullable) {
        super();
        this.tableName = tableName;
        this.columnNameNullable = columnNameNullable;
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    @Override
    protected Boolean doInBackground(ContentValues[] rows) {
        for (int i = 0; i < rows.length; i++) {
            sqLiteDatabase.insert(tableName, columnNameNullable, rows[i]);
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
    }

}
