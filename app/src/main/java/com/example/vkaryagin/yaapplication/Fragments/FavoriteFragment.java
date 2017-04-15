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
 * Created by tripo on 3/19/2017.
 */

public class FavoriteFragment extends BaseFragment {

    public static final String TAG = "favotite_fragment";
    public static final String COMMUNICATE_FAVORITE_KEY = "add_favorite";

    /**
     * Saved instance state arguments
     */
    private static final String ARG_FAVORITE_LIST = "favorite_list";

    private ListView favoriteListView;
    private FavoriteListAdapter favoriteAdapter;
    private ArrayList<HistoryTranslateEntry> favorites;

    public FavoriteFragment() {
        favorites = new ArrayList<>();
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


//    public static void sendTranslateRecord(FavoriteTranslateEntry rec) {
//        Bundle msg = new Bundle();
//        msg.putString(FavoriteFragment.COMMUNICATE_TRANSLATE_TEXT, translateText);
//        msg.putString(FavoriteFragment.COMMUNICATE_TRANSLATED_TEXT, translatedText);
//        msg.putString(FavoriteFragment.COMMUNICATE_TRANSLATE_LANG, translateLang.getLanguageName());
//        msg.putString(FavoriteFragment.COMMUNICATE_TRANSLATED_LANG, translatedLang.getLanguageName());
//
//        Bundle data = new Bundle();
//        data.putBundle(FavoriteFragment.COMMUNICATE_FAVORITE_KEY, msg);
//        FragmentsCommutator.getInstance().addData(FavoriteFragment.TAG, data);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);
        HistoryTranslate favoriteTranslate = new HistoryTranslate(getDbOpenHelper());
        favorites = (ArrayList) favoriteTranslate.getFavorites();

        favoriteAdapter = new FavoriteListAdapter(this.getContext(), favorites);

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
