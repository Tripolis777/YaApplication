package com.example.vkaryagin.yaapplication.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.vkaryagin.yaapplication.Core.Callable;
import com.example.vkaryagin.yaapplication.Core.YaTranslateTask;
import com.example.vkaryagin.yaapplication.Database.FavoriteTranslate;
import com.example.vkaryagin.yaapplication.Database.Schema.FavoriteTranslateEntry;
import com.example.vkaryagin.yaapplication.R;
import com.example.vkaryagin.yaapplication.Views.FavoriteListAdapter;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by tripo on 3/19/2017.
 */

public class FavoriteFragment extends BaseFragment {

    public static final String TAG = "favotite_fragment";
    public static final String COMMUNICATE_FAVORITE_KEY = "add_favorite";

    public static final String COMMUNICATE_TRANSLATE_TEXT = "translate_text";
    public static final String COMMUNICATE_TRANSLATED_TEXT = "translated_text";
    public static final String COMMUNICATE_TRANSLATE_LANG = "translate_lang";
    public static final String COMMUNICATE_TRANSLATED_LANG = "translated_lang";

    /**
     * Saved instance state arguments
     */
    private static final String ARG_FAVORITE_LIST = "favorite_list";

    private ListView favoriteListView;
    private FavoriteListAdapter favoriteAdapter;
    private ArrayList<FavoriteTranslateEntry> favorites;

    public FavoriteFragment() {
        favorites = new ArrayList<>();
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FavoriteFragment newInstance(Bundle state) {
        FavoriteFragment fragment = new FavoriteFragment();
        fragment.setArguments(state);
        Log.e("Favorite Fragment", "Create new instance!");
        fragment.checkMessageQueue();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);
        Log.e("FavotiteFragment", "onCreateView");
        //if (savedInstanceState == null) {
            FavoriteTranslate favoriteTranslate = new FavoriteTranslate(this.getContext());
            favorites = (ArrayList) favoriteTranslate.getAll();
        //} else {
        //    favorites = (ArrayList) savedInstanceState.getSerializable(ARG_FAVORITE_LIST);
        //}

        favoriteAdapter = new FavoriteListAdapter(this.getContext(), favorites);

        favoriteListView = (ListView) rootView.findViewById(R.id.favoroteList);
        favoriteListView.setAdapter(favoriteAdapter);

        this.checkMessageQueue();
        return rootView;
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//    //    outState.putSerializable(ARG_FAVORITE_LIST, favorites);
//    }


    //TODO: надо попробовать передать всё одним объектом Translate чтобы вытянуть языки и создать полноценный объект FavoriteTranslateEntry
    @Override
    public void checkMessageQueue() {
        FragmentsCommutator fragmentsCommutator = FragmentsCommutator.getInstance();
        Stack<Bundle> data = fragmentsCommutator.getData(TAG);
        if (data == null || data.isEmpty()) return;

        for(Bundle msg : data) {
            Bundle favoriteRecord = msg.getBundle(COMMUNICATE_FAVORITE_KEY);
            if (favoriteRecord == null) continue;

            FavoriteTranslateEntry rec = new FavoriteTranslateEntry();
            rec.translateText = favoriteRecord.getString(COMMUNICATE_TRANSLATE_TEXT);
            rec.translatedText = favoriteRecord.getString(COMMUNICATE_TRANSLATED_TEXT);
            rec.translateLang = favoriteRecord.getString(COMMUNICATE_TRANSLATE_LANG);
            rec.translatedLang = favoriteRecord.getString(COMMUNICATE_TRANSLATED_LANG);

            favorites.add(0, rec);
        }
    }
}
