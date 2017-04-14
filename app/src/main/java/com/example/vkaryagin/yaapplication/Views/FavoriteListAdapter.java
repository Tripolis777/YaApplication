package com.example.vkaryagin.yaapplication.Views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.vkaryagin.yaapplication.Database.Schema.HistoryTranslateEntry;
import com.example.vkaryagin.yaapplication.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by v.karyagin on 07.04.2017.
 */

public class FavoriteListAdapter extends ArrayAdapter<HistoryTranslateEntry> {
    private final Context context;
    private final List<HistoryTranslateEntry> values;

    public FavoriteListAdapter(@NonNull Context context, ArrayList<HistoryTranslateEntry> values) {
        super(context, R.layout.favorite_list_item);
        this.context = context;
        this.values  = values;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.favorite_list_item, parent, false);

        TextView translateText = (TextView) itemView.findViewById(R.id.translateText);
        TextView translatedText = (TextView) itemView.findViewById(R.id.translatedText);
        TextView translateLang = (TextView) itemView.findViewById(R.id.translateLang);
        TextView translatedLang = (TextView) itemView.findViewById(R.id.translatedLang);

        HistoryTranslateEntry value = values.get(pos);
        translateText.setText(value.translateText);
        translatedText.setText(value.getTranslateFirst());
        translateLang.setText(value.translateLang);
        translatedLang.setText(value.translatedLang);

        return itemView;
    }


    @Override
    public void addAll(@Nullable Collection collection) {
        values.addAll(collection);
        super.addAll(collection);
    }

    public void addToFirstAll(@NonNull Collection collection) {
        values.addAll(0, collection);
    }
    public void removeItem(int index) { values.remove(index); }
}
