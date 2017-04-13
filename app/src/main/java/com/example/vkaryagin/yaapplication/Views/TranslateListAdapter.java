package com.example.vkaryagin.yaapplication.Views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.vkaryagin.yaapplication.Core.Translate;
import com.example.vkaryagin.yaapplication.R;

/**
 * Created by v.karyagin on 12.04.2017.
 */

public class TranslateListAdapter extends ArrayAdapter<Translate> {

    private final Context context;
    private Translate translate;

    public TranslateListAdapter(@NonNull Context context) {
        super(context, R.layout.translate_list_item);//, (ArrayList) value.getTranslatedTexts());
        this.context = context;
        translate = new Translate(null);
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.translate_list_item, parent, false);

        TextView translatedText = (TextView) itemView.findViewById(R.id.translatedItemText);
        translatedText.setText((pos + 1) + ". " + translate.getTranslatedTexts().get(pos));

        return itemView;
    }

    @Override
    public int getCount() { return translate.getTranslatedTexts().size(); }

    public String getTranslateText() { return translate.getTranslateText(); }
    public void setTranslate (Translate translate) { this.translate = translate; }
    public final Translate getTranslate() { return translate; }
}
