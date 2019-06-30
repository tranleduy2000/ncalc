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

package com.duy.ncalc.document;

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
import com.duy.ncalc.document.model.FunctionDocumentItem;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Duy on 23-May-17.
 */

public class FunctionSuggestionAdapter extends ArrayAdapter<FunctionDocumentItem> {
    private LayoutInflater inflater;
    private List<FunctionDocumentItem> displayItems;
    private ArrayList<FunctionDocumentItem> filterItems = new ArrayList<>();
    private ArrayList<FunctionDocumentItem> originalItems;
    @Nullable
    private OnSuggestionClickListener onSuggestionListener;

    private Filter filter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            FunctionDocumentItem functionDocumentItem = (FunctionDocumentItem) resultValue;
            return super.convertResultToString(functionDocumentItem.getName());
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filterItems.clear();
            FilterResults filterResults = new FilterResults();
            if (constraint != null) {
                for (FunctionDocumentItem item : originalItems) {
                    if (item.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        filterItems.add(item);
                    }
                }
                filterResults.count = filterItems.size();
                filterResults.values = filterItems;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            ArrayList<FunctionDocumentItem> filteredList = (ArrayList<FunctionDocumentItem>) results.values;
            clear();
            if (filteredList != null) {
                addAll(filteredList);
            }
            notifyDataSetChanged();
        }
    };

    public FunctionSuggestionAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<FunctionDocumentItem> displayItems) {
        super(context, resource, displayItems);
        this.displayItems = displayItems;
        this.originalItems = new ArrayList<>(displayItems);
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_suggest, parent, false);
        }
        final FunctionDocumentItem functionDocumentItem = displayItems.get(position);

        TextView txtName = convertView.findViewById(R.id.txt_name);
        txtName.setText(functionDocumentItem.getName());

        TextView txtDescription = convertView.findViewById(R.id.txt_description);
        if (functionDocumentItem.getDescription() == null || functionDocumentItem.getDescription().isEmpty()) {
            txtDescription.setVisibility(View.GONE);
        } else  {
            txtDescription.setVisibility(View.VISIBLE);
        }
        txtDescription.setText(functionDocumentItem.getDescription());

        View btnInfo = convertView.findViewById(R.id.img_info);
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSuggestionListener != null) {
                    onSuggestionListener.clickOpenDocument(functionDocumentItem);
                }
            }
        });
        return convertView;
    }

    @Override
    public int getCount() {
        return displayItems.size();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }


    public void setOnSuggestionClickListener(@Nullable OnSuggestionClickListener onSuggestionListener) {
        this.onSuggestionListener = onSuggestionListener;
    }

    public interface OnSuggestionClickListener {
        void clickOpenDocument(FunctionDocumentItem functionDocumentItem);
    }
}
