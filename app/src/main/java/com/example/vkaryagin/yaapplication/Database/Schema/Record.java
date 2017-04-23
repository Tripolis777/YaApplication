package com.example.vkaryagin.yaapplication.Database.Schema;

import android.content.ContentValues;
import android.provider.BaseColumns;

import java.io.Serializable;

/**
 * Интерфейс для работы с записями таблицы
 */
public interface Record extends BaseColumns, Serializable {
    /**
     * Позволяет получить запись таблицы в виде {@link ContentValues}
     * @return запись таблицы
     */
    ContentValues getContentValues();

    /**
     * Позволяет получить имя таблицы
     * @return имя таблицы
     */
    String getTableName();

    /**
     * Позволяет получить имена полей, которые могут принимать null значение. Опциональный параметр.
     * @return by default null
     */
    String getColumnNameNullable();

    /**
     * Позволяет установить id записи из талицы. Warning! При неправильном использовании, неверно выставленный
     * id записи таблицы может изменить ваши данные.
     * @param id в таблице базы данных
     */
    void setId(long id);
}
