package com.example.vkaryagin.yaapplication.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.vkaryagin.yaapplication.Core.Initiable;
import com.example.vkaryagin.yaapplication.Database.HistoryTranslate;
import com.example.vkaryagin.yaapplication.Database.Schema.HistoryTranslateEntry;
import com.example.vkaryagin.yaapplication.Database.YaAppDBOpenHelper;
import com.example.vkaryagin.yaapplication.R;
import com.example.vkaryagin.yaapplication.Views.FavoriteListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tripo on 3/19/2017.
 */

public class FavoriteFragment extends HistoryFragment {

    public static final String TAG = "favotite_fragment";

    private ListView favoriteListView;
    private FavoriteListAdapter favoriteAdapter;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FavoriteFragment newInstance(Bundle state, YaAppDBOpenHelper dbOpenHelper) {
        FavoriteFragment fragment = new FavoriteFragment();
        fragment.setArguments(state);
        fragment.setDBOpenHelper(dbOpenHelper);
        Log.i("Favorite Fragment", "Create new instance!");
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);
        HistoryTranslate favoriteTranslate = new HistoryTranslate(getDbOpenHelper());
        setRecords(favoriteTranslate.getFavorites());

        favoriteAdapter = new FavoriteListAdapter(this.getContext(), new ArrayList<>(records.values()),
                getDbOpenHelper());

        favoriteListView = (ListView) rootView.findViewById(R.id.favoroteList);
        favoriteListView.setAdapter(favoriteAdapter);

        return rootView;
    }
}
