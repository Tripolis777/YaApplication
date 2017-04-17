package com.example.vkaryagin.yaapplication.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.vkaryagin.yaapplication.Database.HistoryTranslate;
import com.example.vkaryagin.yaapplication.Database.Schema.HistoryTranslateEntry;
import com.example.vkaryagin.yaapplication.Database.YaAppDBOpenHelper;
import com.example.vkaryagin.yaapplication.R;
import com.example.vkaryagin.yaapplication.Views.FavoriteListAdapter;

import java.util.ArrayList;

/**
 * Created by v.karyagin on 17.04.2017.
 */

public class HistoryFragment extends BaseFragment {

    private ArrayList<HistoryTranslateEntry> historyRecords;
    private FavoriteListAdapter historyAdapter;
    private ListView historyListView;

    public static HistoryFragment getInstance(Bundle state, YaAppDBOpenHelper dbOpenHelper) {
        HistoryFragment fragment = new HistoryFragment();
        fragment.setArguments(state);
        fragment.setDBOpenHelper(dbOpenHelper);
        Log.i("History Fragment", "Create new instance!");
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);
        HistoryTranslate favoriteTranslate = new HistoryTranslate(getDbOpenHelper());
        historyRecords = (ArrayList) favoriteTranslate.getAll();

        historyAdapter = new FavoriteListAdapter(this.getContext(), historyRecords);

        historyListView = (ListView) rootView.findViewById(R.id.favoroteList);
        historyListView.setAdapter(historyAdapter);

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

            newRecs.add(favoriteRecord);
        }

        if (favoriteListView != null) {
            FavoriteListAdapter adapter = (FavoriteListAdapter) favoriteListView.getAdapter();
            Log.d("FavoriteFragment", "[checkMessageQueue] add to list view " + newRecs.size() + " new items!");
            adapter.addToFirstAll(newRecs);
            adapter.notifyDataSetChanged();
        } else {
            favorites.addAll(0, newRecs);
        }
    }
}
