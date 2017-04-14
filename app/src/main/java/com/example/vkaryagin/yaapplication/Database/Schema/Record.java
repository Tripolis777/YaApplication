package com.example.vkaryagin.yaapplication.Database.Schema;

import android.content.ContentValues;
import android.provider.BaseColumns;

import java.io.Serializable;

/**
 * Created by v.karyagin on 14.04.2017.
 */

public interface Record extends BaseColumns, Serializable {
    ContentValues getContentValues();
    String getTableName();
    String getColumnNameNullable();
    void setId(long id);
}
