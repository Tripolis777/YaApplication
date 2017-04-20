package com.example.vkaryagin.yaapplication.Views;

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.example.vkaryagin.yaapplication.Fragments.FavoriteFragment;
import com.example.vkaryagin.yaapplication.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by tripo on 4/17/2017.
 */

public class HistoryListAdapter extends ArrayAdapter<HistoryTranslateEntry> {
    protected final Context context;
    protected final List<HistoryTranslateEntry> values;
    protected final YaAppDBOpenHelper dbOpenHelper;

    public HistoryListAdapter(@NonNull Context context, ArrayList<HistoryTranslateEntry> values, YaAppDBOpenHelper dbOpenHelper) {
        super(context, R.layout.favorite_list_item, values);
        this.context = context;
        this.values  = values;
        this.dbOpenHelper = dbOpenHelper;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.favorite_list_item, parent, false);

        TextView translateText = (TextView) itemView.findViewById(R.id.translateText);
        TextView translatedText = (TextView) itemView.findViewById(R.id.translatedText);
        TextView translateLang = (TextView) itemView.findViewById(R.id.translateLang);
        TextView translatedLang = (TextView) itemView.findViewById(R.id.translatedLang);

        ToggleButton favoriteButton = (ToggleButton) itemView.findViewById(R.id.favoriteItemButton);

        HistoryTranslateEntry value = values.get(pos);
        translateText.setText(value.translateText);
        translatedText.setText(value.getTranslateFirst());
        translateLang.setText(value.translateLang);
        translatedLang.setText(value.translatedLang);

        favoriteButton.setChecked(value.favorite);
        favoriteButton.setOnClickListener(getOnFavoriteButtonClickListener(pos, value));

        return itemView;
    }

    @Override
    public void addAll(@Nullable Collection collection) {
        values.addAll(collection);
        super.addAll(collection);
    }

    protected View.OnClickListener getOnFavoriteButtonClickListener(final int pos, final HistoryTranslateEntry entry) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isFavorite = ((ToggleButton) view).isChecked();
                Log.i("OnFavoriteButtonClick", "isFavorite : " + isFavorite + " record_id: " + entry.id);
                setFavoriteRecord(entry, isFavorite);
                if (isFavorite)
                    FavoriteFragment.addHistoryRecord(entry);
            }
        };
    }

    public void addToFirstAll(@NonNull Collection collection) {
        values.addAll(0, collection);
    }
    public void removeItem(int index) { values.remove(index); }

    protected void setFavoriteRecord(HistoryTranslateEntry record, boolean isFavorite) {
        HistoryTranslate hs = new HistoryTranslate(dbOpenHelper);
        ContentValues cv = new ContentValues();
        cv.put(HistoryTranslateEntry.COLUMN_NAME_IS_FAVORITE, isFavorite);
        hs.update(record, cv);
    }
}
