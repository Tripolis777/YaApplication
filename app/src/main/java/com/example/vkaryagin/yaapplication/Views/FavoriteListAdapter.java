package com.example.vkaryagin.yaapplication.Views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import java.util.Collection;
import java.util.List;

/**
 * Created by v.karyagin on 07.04.2017.
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
                values.remove(pos);
                setFavoriteRecord(entry, false);
                FavoriteListAdapter.this.notifyDataSetChanged();
            }
        };
    }
}
