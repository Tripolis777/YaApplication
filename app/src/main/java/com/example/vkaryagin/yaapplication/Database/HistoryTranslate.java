package com.example.vkaryagin.yaapplication.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.vkaryagin.yaapplication.Core.Translate;
import com.example.vkaryagin.yaapplication.Database.Schema.HistoryTranslateEntry;
import com.example.vkaryagin.yaapplication.Database.Tasks.DatabaseInsertTask;
import com.example.vkaryagin.yaapplication.Database.Tasks.DatabaseUpdateTask;
import com.example.vkaryagin.yaapplication.Database.Tasks.UpdateRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс работы с таблицей history
 */
public class HistoryTranslate {

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + HistoryTranslateEntry.TABLE_NAME +
                    " ( "+ HistoryTranslateEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    HistoryTranslateEntry.COLUMN_NAME_TRANSLATE_TEXT + " TEXT, " +
                    HistoryTranslateEntry.COLUMN_NAME_TRANSLATED_TEXT + " TEXT, " +
                    HistoryTranslateEntry.COLUMN_NAME_TRANSLATE_LANG + " TEXT, " +
                    HistoryTranslateEntry.COLUMN_NAME_TRANSLATED_LANG + " TEXT, " +
                    HistoryTranslateEntry.COLUMN_NAME_TRANSLATE_CODE + " TEXT, " +
                    HistoryTranslateEntry.COLUMN_NAME_TRANSLATED_CODE + " TEXT, " +
                    HistoryTranslateEntry.COLUMN_NAME_TRANSLATE_DATE + " TEXT DEFAULT (datetime('now', 'localtime')), " +
                    HistoryTranslateEntry.COLUMN_NAME_IS_FAVORITE + " INTEGER DEFAULT(0))";
    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + HistoryTranslateEntry.TABLE_NAME;

    public static final String[] QUERY_COLUMNS = {
            HistoryTranslateEntry._ID,
            HistoryTranslateEntry.COLUMN_NAME_TRANSLATE_TEXT,
            HistoryTranslateEntry.COLUMN_NAME_TRANSLATED_TEXT,
            HistoryTranslateEntry.COLUMN_NAME_TRANSLATE_LANG,
            HistoryTranslateEntry.COLUMN_NAME_TRANSLATED_LANG,
            HistoryTranslateEntry.COLUMN_NAME_TRANSLATE_CODE,
            HistoryTranslateEntry.COLUMN_NAME_TRANSLATED_CODE,
            HistoryTranslateEntry.COLUMN_NAME_TRANSLATE_DATE,
            HistoryTranslateEntry.COLUMN_NAME_IS_FAVORITE
    };

    private final YaAppDBOpenHelper dbOpenHelper;

    public HistoryTranslate(final YaAppDBOpenHelper dbOpenHelper) {
        this.dbOpenHelper = dbOpenHelper;
    }

    /**
     * Позволяет получить все записи из таблицы и сортирует их по дате добавления от ранних к поздним
     * @return список записей
     */
    public List<HistoryTranslateEntry> getAll() {
        Cursor cursor = this.query(QUERY_COLUMNS, null, null, null, null,
                HistoryTranslateEntry.COLUMN_NAME_TRANSLATE_DATE + " DESC");
        ArrayList<HistoryTranslateEntry> records = (ArrayList<HistoryTranslateEntry>) parseSelectRequest(cursor);
        cursor.close();

        return records;
    }

    /**
     * Позволяет получить все записи из таблицы, у которых поле favorite is true
     * @return список записей
     */
    public List<HistoryTranslateEntry> getFavorites() {
        Cursor cursor = this.query(
                QUERY_COLUMNS,
                HistoryTranslateEntry.COLUMN_NAME_IS_FAVORITE + " = ?",
                new String[] { "1" },
                null,
                null,
                HistoryTranslateEntry.COLUMN_NAME_TRANSLATE_DATE + " DESC");
        ArrayList<HistoryTranslateEntry> records = (ArrayList<HistoryTranslateEntry>) parseSelectRequest(cursor);
        cursor.close();

        return records;
    }

    private List<HistoryTranslateEntry> parseSelectRequest(Cursor cursor) {
        ArrayList<HistoryTranslateEntry> records = new ArrayList<>();
        while (cursor.moveToNext()) {
            HistoryTranslateEntry record = new HistoryTranslateEntry();
            record.id = cursor.getLong(cursor.getColumnIndexOrThrow(HistoryTranslateEntry._ID));
            record.translateText = cursor.getString(cursor.getColumnIndexOrThrow(
                    HistoryTranslateEntry.COLUMN_NAME_TRANSLATE_TEXT));
            record.translatedText = cursor.getString(cursor.getColumnIndexOrThrow(
                    HistoryTranslateEntry.COLUMN_NAME_TRANSLATED_TEXT));
            record.translateLang = cursor.getString(cursor.getColumnIndexOrThrow(
                    HistoryTranslateEntry.COLUMN_NAME_TRANSLATE_LANG));
            record.translatedLang = cursor.getString(cursor.getColumnIndexOrThrow(
                    HistoryTranslateEntry.COLUMN_NAME_TRANSLATED_LANG));
            record.translateCode = cursor.getString(cursor.getColumnIndexOrThrow(
                    HistoryTranslateEntry.COLUMN_NAME_TRANSLATE_CODE));
            record.translatedCode = cursor.getString(cursor.getColumnIndexOrThrow(
                    HistoryTranslateEntry.COLUMN_NAME_TRANSLATED_CODE));
            record.date = cursor.getString(cursor.getColumnIndexOrThrow(
                    HistoryTranslateEntry.COLUMN_NAME_TRANSLATE_DATE));
            record.favorite = cursor.getInt(cursor.getColumnIndexOrThrow(
                    HistoryTranslateEntry.COLUMN_NAME_IS_FAVORITE)) != 0;
            records.add(record);
        }
        return records;
    }

    /**
     * Как {@link SQLiteDatabase#query(String, String[], String, String[], String, String, String)}
     * только сам передает имя таблицы
     * @see SQLiteDatabase#query(String, String[], String, String[], String, String, String)
     */
    public Cursor query(String[] columns, String selection, String[] selectionArgs, String groupBy,
                        String having, String orderBy ) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        return db.query(HistoryTranslateEntry.TABLE_NAME,
                columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    /**
     * Создает новую запись в базе данных.
     * @param translate объект перевода из которого создается {@link HistoryTranslateEntry}
     * @param favorite является ли запись избранной, опциональый параметр.
     * @return объект записи таблицы history {@link HistoryTranslateEntry}
     */
    public HistoryTranslateEntry create(Translate translate, @Nullable Boolean favorite) {
        HistoryTranslateEntry record = new HistoryTranslateEntry();
        record.setTranslate(translate);
        if (favorite != null)
            record.favorite = favorite;

        DatabaseInsertTask insertTask = new DatabaseInsertTask(dbOpenHelper);
        insertTask.execute(record);
        return record;
    }

    /**
     * Устанавливает записи таблицы history поле favorite. Делает запрос в базу данных на обновление
     * @param record запись в таблице history
     * @param favorite значение поля favorite
     */
    public void setFavorite(HistoryTranslateEntry record, boolean favorite) {
        ContentValues values = new ContentValues();
        values.put(HistoryTranslateEntry.COLUMN_NAME_IS_FAVORITE, favorite);
        this.update(record, values);
    }

    /**
     * Делает запрос на обновление записи в таблице history.
     * @param record запись в таблице history
     * @param values поля, которые будут обновлены
     * @see ContentValues
     * @see SQLiteDatabase#update(String, ContentValues, String, String[])
     *
     */
    public void update(HistoryTranslateEntry record, ContentValues values)  {
        UpdateRequest request = new UpdateRequest(
                HistoryTranslateEntry.TABLE_NAME,
                values,
                HistoryTranslateEntry._ID  + " = ?",
                new String[] {Long.toString(record.id)} );

        Log.i("HistoryTranslate", "[UPDATE] update rec with id: " + record.id);

        DatabaseUpdateTask updateTask = new DatabaseUpdateTask(dbOpenHelper);
        updateTask.execute(request);
    }

    public static void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
