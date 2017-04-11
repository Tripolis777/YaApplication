package com.example.vkaryagin.yaapplication.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.vkaryagin.yaapplication.Database.Schema.FavoriteTranslateEntry;
import com.example.vkaryagin.yaapplication.R;

import java.util.Collection;
import java.util.List;

/**
 * Created by v.karyagin on 07.04.2017.
 */

public class FavoriteListAdapter extends ArrayAdapter<FavoriteTranslateEntry> {
    private final Context context;
    private final List<FavoriteTranslateEntry> values;

    public FavoriteListAdapter(Context context, List<FavoriteTranslateEntry> values) {
        super(context, R.layout.favorite_list_item, values);
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

        FavoriteTranslateEntry value = values.get(pos);
        translateText.setText(value.translateText);
        translatedText.setText(value.translatedText);
        translateLang.setText(value.translateLang);
        translatedLang.setText(value.translatedLang);

        return itemView;
    }


    @Override
    public void addAll(Collection collection) {
        values.addAll(collection);
        super.addAll(collection);
    }
}
