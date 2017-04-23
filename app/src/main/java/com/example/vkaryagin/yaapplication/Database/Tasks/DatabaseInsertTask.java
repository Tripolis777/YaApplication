package com.example.vkaryagin.yaapplication.Database.Tasks;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import com.example.vkaryagin.yaapplication.Database.Schema.Record;

/**
 * Класс реализующий ассинхронное добавление записи в базу данных
 */
public class DatabaseInsertTask extends AsyncTask<Record, Integer, Boolean> {

    private SQLiteDatabase sqLiteDatabase;
    private final SQLiteOpenHelper dbHelper;

    public DatabaseInsertTask(SQLiteOpenHelper dbHelper) {
        super();
        this.dbHelper = dbHelper;
    }

    @Override
    protected Boolean doInBackground(final Record records[]) {
        sqLiteDatabase = dbHelper.getWritableDatabase();

        for (Record record : records) {
            long id = sqLiteDatabase.insert(record.getTableName(), record.getColumnNameNullable(),
                    record.getContentValues());
            record.setId(id);
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
    }

}
