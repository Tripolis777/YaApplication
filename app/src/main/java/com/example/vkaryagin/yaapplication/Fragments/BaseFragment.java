package com.example.vkaryagin.yaapplication.Fragments;

import com.example.vkaryagin.yaapplication.Database.YaAppDBOpenHelper;

/**
 * Основной абстрактный класс для фрагментов. Содержит в себе основной интерфейс работы с DBOpenHelper'ом
 */
public abstract class BaseFragment extends android.support.v4.app.Fragment {
    private YaAppDBOpenHelper dbOpenHelper;

    public void setDBOpenHelper(final YaAppDBOpenHelper dbOpenHelper) {
        this.dbOpenHelper = dbOpenHelper;
    }

    protected YaAppDBOpenHelper getDbOpenHelper() {
        if (dbOpenHelper == null) {
            dbOpenHelper = new YaAppDBOpenHelper(this.getContext(), null);
        }
        return dbOpenHelper;
    }

}
