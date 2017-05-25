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
import com.example.vkaryagin.yaapplication.Adapters.HistoryListAdapter;

import java.util.ArrayList;

/**
 * Фрагмент для записей истории переводов.
 */

public class HistoryFragment extends BaseFragment {
    protected HistoryListAdapter historyAdapter;

    private ArrayList<HistoryTranslateEntry> historyRecords;
    private ListView recordsListView;

    public static HistoryFragment newInstance(Bundle state, YaAppDBOpenHelper dbOpenHelper) {
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

        historyAdapter = new HistoryListAdapter(this.getContext(), historyRecords, getDbOpenHelper());

        recordsListView = (ListView) rootView.findViewById(R.id.favoroteList);
        recordsListView.setAdapter(historyAdapter);

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleHint) {
        super.setUserVisibleHint(isVisibleHint);
        if (isVisibleHint && historyAdapter != null) {
            historyAdapter.clear();
            HistoryTranslate favoriteTranslate = new HistoryTranslate(getDbOpenHelper());
            historyAdapter.addAll(favoriteTranslate.getAll());
            historyAdapter.notifyDataSetChanged();
        }
    }
}
