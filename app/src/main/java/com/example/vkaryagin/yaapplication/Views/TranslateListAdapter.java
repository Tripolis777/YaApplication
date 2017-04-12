package com.example.vkaryagin.yaapplication.Views;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.vkaryagin.yaapplication.Core.Translate;
import com.example.vkaryagin.yaapplication.R;

import java.util.ArrayList;

/**
 * Created by v.karyagin on 12.04.2017.
 */

public class TranslateListAdapter extends ArrayAdapter<Translate> {

    private final Context context;
    private final Translate translate;

    public TranslateListAdapter(@NonNull Context context, Translate value) {
        super(context, R.layout.translate_list_item);//, (ArrayList) value.getTranslatedTexts());
        this.translate = value;
        this.context = context;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.favorite_list_item, parent, false);

        TextView translatedText = (TextView) itemView.findViewById(R.id.translatedItemText);

        translatedText.setText(pos + ". " + translate.getTranslatedTexts().get(pos));
        return itemView;
    }

    @Override
    public int getCount() { return translate.getTranslatedTexts().size(); }

    public String getTranslateText() { return translate.getTranslateText(); }
}
