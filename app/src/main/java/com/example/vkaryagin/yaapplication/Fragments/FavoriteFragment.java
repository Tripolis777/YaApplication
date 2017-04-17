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

public class FavoriteFragment extends BaseFragment {

    public static final String TAG = "favotite_fragment";
    public static final String COMMUNICATE_FAVORITE_KEY = "add_favorite";
    HashMap<Long, HistoryTranslateEntry> favorites;

    /**
     * Saved instance state arguments
     */
    private static final String ARG_FAVORITE_LIST = "favorite_list";

    private ListView favoriteListView;
    private FavoriteListAdapter favoriteAdapter;
    //private ArrayList<HistoryTranslateEntry> favorites;

    public FavoriteFragment() {
        favorites = new HashMap<>();
    }

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

    public static void addFavorite(HistoryTranslateEntry record) {
        Bundle data = new Bundle();
        data.putSerializable(COMMUNICATE_FAVORITE_KEY, record);
        FragmentsCommutator.getInstance().addData(TAG, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);
        HistoryTranslate favoriteTranslate = new HistoryTranslate(getDbOpenHelper());
        setFavorites(favoriteTranslate.getFavorites());

        favoriteAdapter = new FavoriteListAdapter(this.getContext(), new ArrayList<>(favorites.values()),
                getDbOpenHelper());

        favoriteListView = (ListView) rootView.findViewById(R.id.favoroteList);
        favoriteListView.setAdapter(favoriteAdapter);

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleHint) {
        super.setUserVisibleHint(isVisibleHint);
        Log.e("FavoriteFragment", "[setUserVisibleHint] set is to " + isVisibleHint);
        if (isVisibleHint)
            checkMessageQueue();
    }

    @Override
    public void checkMessageQueue() {
        Log.d("FavoriteFragment", "[checkMessageQueue] START");
        FragmentsCommutator fragmentsCommutator = FragmentsCommutator.getInstance();
        ArrayList<Bundle> data = (ArrayList) fragmentsCommutator.getData(TAG);
        if (data == null || data.isEmpty()) return;

        Log.i("FavoriteFragment", "[checkMessageQueue] data size: " + data.size());

        ArrayList<HistoryTranslateEntry> newRecs = new ArrayList<>();
        for(Bundle msg : data) {
            HistoryTranslateEntry favoriteRecord = (HistoryTranslateEntry) msg.getSerializable(COMMUNICATE_FAVORITE_KEY);
            Log.d("FavoriteFragment", "checkMassageQueue favoriteRecord is " + (
                    favoriteRecord == null ? "null" : "init") + "!");
            if (favoriteRecord == null) continue;
            if (favorites.get(favoriteRecord.id) != null) continue;

            newRecs.add(favoriteRecord);
            favorites.put(favoriteRecord.id, favoriteRecord);
        }

        if (favoriteListView != null) {
            FavoriteListAdapter adapter = (FavoriteListAdapter) favoriteListView.getAdapter();
            Log.d("FavoriteFragment", "[checkMessageQueue] add to list view " + newRecs.size() + " new items!");
            adapter.addToFirstAll(newRecs);
            adapter.notifyDataSetChanged();
        }
    }

    private void setFavorites(List<HistoryTranslateEntry> records) {
        for (HistoryTranslateEntry rec : records) {
            favorites.put(rec.id, rec);
        }
    }
}
