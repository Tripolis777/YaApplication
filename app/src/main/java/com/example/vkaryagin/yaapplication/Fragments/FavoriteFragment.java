package com.example.vkaryagin.yaapplication.Fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.vkaryagin.yaapplication.Database.Schema.FavoriteTranslate;
import com.example.vkaryagin.yaapplication.Database.YaAppDBOpenHelper;
import com.example.vkaryagin.yaapplication.R;

/**
 * Created by tripo on 3/19/2017.
 */

//TODO: Пока сказать нечего, надо хоть что-то написать

public class FavoriteFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String[] QUERY_COLUMS = {
            FavoriteTranslate.FavoriteTranslateEntry._ID,
            FavoriteTranslate.FavoriteTranslateEntry.COLUMN_NAME_TRANSLATE_TEXT,
            FavoriteTranslate.FavoriteTranslateEntry.COLUMN_NAME_TRANSLATED_TEXT,
            FavoriteTranslate.FavoriteTranslateEntry.COLUMN_NAME_TRANSLATE_LANG,
            FavoriteTranslate.FavoriteTranslateEntry.COLUMN_NAME_TRANSLATED_LANG
    };


    private ListView favoriteList;
    private ArrayAdapter<String> favoriteAdapter;
    private final YaAppDBOpenHelper dbHelper;

    public FavoriteFragment() {
        dbHelper = new YaAppDBOpenHelper(this.getContext(), null);
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FavoriteFragment newInstance(int sectionNumber) {
        FavoriteFragment fragment = new FavoriteFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);

        favoriteList = (ListView) rootView.findViewById(R.id.favoroteList);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(FavoriteTranslate.FavoriteTranslateEntry.TABLE_NAME,
                QUERY_COLUMS, null, null, null, null, null);
        

        return rootView;
    }

    private void initFavoriteAdapter() {
        favoriteAdapter  = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1);

    }

}
