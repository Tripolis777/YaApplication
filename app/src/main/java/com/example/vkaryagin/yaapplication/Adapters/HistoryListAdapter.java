package com.example.vkaryagin.yaapplication.Adapters;

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.vkaryagin.yaapplication.Database.HistoryTranslate;
import com.example.vkaryagin.yaapplication.Database.Schema.HistoryTranslateEntry;
import com.example.vkaryagin.yaapplication.Database.YaAppDBOpenHelper;
import com.example.vkaryagin.yaapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides an interface for displaying and working with the list {@link HistoryTranslateEntry}. Inherit by {@link ArrayAdapter}.
 * @see ArrayAdapter
 */
public class HistoryListAdapter extends ArrayAdapter<HistoryTranslateEntry> {
    private final Context context;
    private final List<HistoryTranslateEntry> values;
    private final YaAppDBOpenHelper dbOpenHelper;

    static class ViewHolder {
        TextView translateText;
        TextView translatedText;
        TextView translateLang;
        TextView translatedLang;

        ToggleButton favoriteButton;
    }

    public HistoryListAdapter(@NonNull Context context, ArrayList<HistoryTranslateEntry> values, YaAppDBOpenHelper dbOpenHelper) {
        super(context, R.layout.favorite_list_item, values);
        this.context = context;
        this.values  = values;
        this.dbOpenHelper = dbOpenHelper;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent){
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.favorite_list_item, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.translateText = (TextView) convertView.findViewById(R.id.translateText);
            viewHolder.translatedText = (TextView) convertView.findViewById(R.id.translatedText);
            viewHolder.translateLang = (TextView) convertView.findViewById(R.id.translateLang);
            viewHolder.translatedLang = (TextView) convertView.findViewById(R.id.translatedLang);
            viewHolder.favoriteButton = (ToggleButton) convertView.findViewById(R.id.favoriteItemButton);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        HistoryTranslateEntry value = values.get(pos);
        viewHolder.translateText.setText(value.translateText);
        viewHolder.translatedText.setText(value.getTranslateFirst());
        viewHolder.translateLang.setText(value.translateLang);
        viewHolder.translatedLang.setText(value.translatedLang);

        viewHolder.favoriteButton.setChecked(value.favorite);
        viewHolder.favoriteButton.setOnClickListener(getOnFavoriteButtonClickListener(pos, value));

        return convertView;
    }

    protected View.OnClickListener getOnFavoriteButtonClickListener(final int pos, final HistoryTranslateEntry entry) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isFavorite = ((ToggleButton) view).isChecked();
                Log.i("OnFavoriteButtonClick", "isFavorite : " + isFavorite + " record_id: " + entry.id);
                setRecordFavorite(entry, isFavorite);
            }
        };
    }

    public void add(@NonNull HistoryTranslateEntry entry) {
        values.add(entry);
    }

    public void removeRecord(int index) {
        values.remove(index);
    }

    protected void setRecordFavorite(HistoryTranslateEntry record, boolean isFavorite) {
        HistoryTranslate hs = new HistoryTranslate(dbOpenHelper);
        ContentValues cv = new ContentValues();
        cv.put(HistoryTranslateEntry.COLUMN_NAME_IS_FAVORITE, isFavorite);
        hs.update(record, cv);
    }
}
