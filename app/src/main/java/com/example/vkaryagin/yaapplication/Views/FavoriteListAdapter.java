package com.example.vkaryagin.yaapplication.Views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.example.vkaryagin.yaapplication.Database.Schema.HistoryTranslateEntry;
import com.example.vkaryagin.yaapplication.Database.YaAppDBOpenHelper;

import java.util.ArrayList;
/**
 * ListView Adapter for favorite history records. It's like {@link HistoryListAdapter}, but override
 * favorite button behavior.
 * @see HistoryListAdapter
 * @see android.widget.ArrayAdapter
 */
public class FavoriteListAdapter extends HistoryListAdapter {
    public FavoriteListAdapter(@NonNull Context context, ArrayList<HistoryTranslateEntry> values, YaAppDBOpenHelper dbOpenHelper) {
        super(context, values, dbOpenHelper);
    }

    @Override
    protected View.OnClickListener getOnFavoriteButtonClickListener(final int pos, final HistoryTranslateEntry entry) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavoriteListAdapter.this.removeRecord(pos);
                FavoriteListAdapter.this.setRecordFavorite(entry, false);
                FavoriteListAdapter.this.notifyDataSetChanged();
            }
        };
    }
}
