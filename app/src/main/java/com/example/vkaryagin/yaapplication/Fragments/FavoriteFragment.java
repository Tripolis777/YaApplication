package com.example.vkaryagin.yaapplication.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.vkaryagin.yaapplication.Database.FavoriteTranslate;
import com.example.vkaryagin.yaapplication.Database.Schema.FavoriteTranslateEntry;
import com.example.vkaryagin.yaapplication.R;
import com.example.vkaryagin.yaapplication.Views.FavoriteListAdapter;

import java.util.ArrayList;

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


    private ListView favoriteList;
    private FavoriteListAdapter favoriteAdapter;
    private ArrayList<FavoriteTranslateEntry> favorites;

    public FavoriteFragment() {
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

        FavoriteTranslate favoriteTranslate = new FavoriteTranslate(this.getContext());
        favorites = (ArrayList) favoriteTranslate.getAll();
        favoriteAdapter = new FavoriteListAdapter(this.getContext(), favorites);

        favoriteList = (ListView) rootView.findViewById(R.id.favoroteList);
        favoriteList.setAdapter(favoriteAdapter);

        return rootView;
    }

//    public void addFavorite(String translateText, String translatedText, String translateLang,
//                                String translatedLang) {
//        favorites.add(0, new FavoriteTranslateEntry(-1, translateText, translatedText, translateLang,
//                translatedLang, new Date().toString()));
//    }

}
