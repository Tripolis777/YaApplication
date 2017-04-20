package com.example.vkaryagin.yaapplication.Fragments;

import com.example.vkaryagin.yaapplication.Database.YaAppDBOpenHelper;

/**
 * Created by v.karyagin on 11.04.2017.
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
