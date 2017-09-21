/*
 * Copyright 2017 Tran Le Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
