/*
 * Copyright (C) 2018 Duy Tran Le
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.duy.calculator.view.editor;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.duy.calculator.R;

import java.util.ArrayList;

/**
 * Created by Duy on 23-May-17.
 */

public class SuggestAdapter extends ArrayAdapter<String> {
    @NonNull
    private final Context context;
    private LayoutInflater inflater;
    private ArrayList<String> items;
    private ArrayList<String> suggestions = new ArrayList<>();
    private ArrayList<String> clone;
    @Nullable
    private OnSuggestionListener onSuggestionListener;

    private Filter mFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return super.convertResultToString(resultValue);
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            suggestions.clear();
            FilterResults filterResults = new FilterResults();
            if (constraint != null) {
                for (String item : clone) {
                    if (item.toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(item);
                    }
                }
                filterResults.count = suggestions.size();
                filterResults.values = suggestions;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<String> filteredList = (ArrayList<String>) results.values;
            clear();
            if (filteredList != null) {
                addAll(filteredList);
            }
            notifyDataSetChanged();
        }
    };

    public SuggestAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<String> items) {
        super(context, resource, items);
        this.context = context;
        this.items = items;
        this.clone = (ArrayList<String>) items.clone();
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_suggest, parent, false);
        }
        TextView txtName = convertView.findViewById(R.id.txt_name);
        txtName.setText(items.get(position));
        View btnInfo = convertView.findViewById(R.id.img_info);
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSuggestionListener != null) {
                    onSuggestionListener.onShowInfo(items.get(position));
                }
            }
        });
        return convertView;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return mFilter;
    }


    public void setOnSuggestionListener(OnSuggestionListener onSuggestionListener) {
        this.onSuggestionListener = onSuggestionListener;
    }

    public interface OnSuggestionListener {
        void onShowInfo(String key);
    }
}
