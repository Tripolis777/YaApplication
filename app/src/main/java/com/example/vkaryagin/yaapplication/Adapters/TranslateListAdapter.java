package com.example.vkaryagin.yaapplication.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.vkaryagin.yaapplication.Database.Schema.HistoryTranslateEntry;
import com.example.vkaryagin.yaapplication.R;

/**
 * List adapter to display takes variants of translated text. Working with {@link HistoryTranslateEntry}
 * @see ArrayAdapter
 */
public class TranslateListAdapter extends ArrayAdapter<HistoryTranslateEntry> {

    private final Context context;
    private HistoryTranslateEntry value;

    static class ViewHolder {
        TextView translatedText;
    }

    public TranslateListAdapter(@NonNull Context context) {
        super(context, R.layout.translate_list_item);
        this.context = context;
        this.value = new HistoryTranslateEntry();
    }

    public void setTranslateEntry(HistoryTranslateEntry entry) { this.value = entry; }
    public final HistoryTranslateEntry getTranslateEntry() { return this.value; }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent){
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.translate_list_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.translatedText = (TextView) convertView.findViewById(R.id.translatedItemText);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.translatedText.setText((pos + 1) + ". " + value.getTranslatedTexts().get(pos));

        return convertView;
    }

    @Override
    public int getCount() { return value.getTranslatedTexts().size(); }
}
