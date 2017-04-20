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
import com.example.vkaryagin.yaapplication.Views.HistoryListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by v.karyagin on 17.04.2017.
 */

public class HistoryFragment extends BaseFragment {

    protected static final String COMMUNICATE_RECORD_KEY = "record";
    public static final String TAG = "history_fragment";

    private ArrayList<HistoryTranslateEntry> historyRecords;
    private HistoryListAdapter historyAdapter;
    private ListView recordsListView;

    protected HashMap<Long, HistoryTranslateEntry> records;

    public HistoryFragment() {
        super();
        records = new HashMap<>();
    }

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
            HistoryTranslateEntry newRecord = (HistoryTranslateEntry) msg.getSerializable(
                    COMMUNICATE_RECORD_KEY);
            Log.d("FavoriteFragment", "checkMassageQueue favoriteRecord is " + (
                    newRecord == null ? "null" : "init") + "!");
            if (newRecord == null) continue;
            if (records.get(newRecord.id) != null) continue;

            newRecs.add(newRecord);
            records.put(newRecord.id, newRecord);
        }

        if (recordsListView != null) {
            HistoryListAdapter adapter = (HistoryListAdapter) recordsListView.getAdapter();
            Log.d("FavoriteFragment", "[checkMessageQueue] add to list view " + newRecs.size() + " new items!");
            adapter.addToFirstAll(newRecs);
            adapter.notifyDataSetChanged();
        }
    }

    protected void setRecords(List<HistoryTranslateEntry> records) {
        for (HistoryTranslateEntry rec : records) {
            this.records.put(rec.id, rec);
        }
    }

    public static void addHistoryRecord(HistoryTranslateEntry record) {
        Bundle data = new Bundle();
        data.putSerializable(COMMUNICATE_RECORD_KEY, record);
        FragmentsCommutator.getInstance().addData(TAG, data);
    }


}
