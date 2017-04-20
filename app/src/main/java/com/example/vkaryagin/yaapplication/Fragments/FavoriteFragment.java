package com.example.vkaryagin.yaapplication.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.vkaryagin.yaapplication.Database.HistoryTranslate;
import com.example.vkaryagin.yaapplication.Database.YaAppDBOpenHelper;
import com.example.vkaryagin.yaapplication.R;
import com.example.vkaryagin.yaapplication.Views.FavoriteListAdapter;
import com.example.vkaryagin.yaapplication.Views.HistoryListAdapter;

import java.util.ArrayList;

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
        ArrayList historyRecords = (ArrayList) favoriteTranslate.getAll();

        historyAdapter = new HistoryListAdapter(this.getContext(), historyRecords, getDbOpenHelper());
        favoriteAdapter = new FavoriteListAdapter(this.getContext(), historyRecords, getDbOpenHelper());

        favoriteListView = (ListView) rootView.findViewById(R.id.favoroteList);
        favoriteListView.setAdapter(favoriteAdapter);

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleHint) {
        super.setUserVisibleHint(isVisibleHint);
        Log.e("FavoriteFragment", "[setUserVisibleHint] set is to " + isVisibleHint);
        if (isVisibleHint) {
            favoriteAdapter.clear();
            HistoryTranslate favoriteTranslate = new HistoryTranslate(getDbOpenHelper());
            favoriteAdapter.addAll(favoriteTranslate.getFavorites());
            favoriteAdapter.notifyDataSetChanged();
        }
    }
}
