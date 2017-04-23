package com.example.vkaryagin.yaapplication.Database.Tasks;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

/**
 * Класс реализующий ассинхронное обновленние данных записи в базе данных
 */
public class DatabaseUpdateTask extends AsyncTask<UpdateRequest, Integer, Integer> {
    private final SQLiteOpenHelper dbHelper;

    public DatabaseUpdateTask(SQLiteOpenHelper dbHelper) {
        super();
        this.dbHelper = dbHelper;
    }

    @Override
    protected Integer doInBackground(UpdateRequest... requests) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int counter  = 0;
        for (UpdateRequest request : requests) {
            counter += db.update(request.tableName, request.values, request.selection, request.selectionArgs);
        }
        return counter;
    }
}
