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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duy.calculator.R;
import com.duy.ncalc.document.model.FunctionDocumentItem;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Duy on 19-May-17.
 */
public class MarkdownListDocumentAdapter extends RecyclerView.Adapter<MarkdownListDocumentAdapter.ViewHolder>
        implements FastScrollRecyclerView.SectionedAdapter {

    private ArrayList<FunctionDocumentItem> filteredItems;
    private ArrayList<FunctionDocumentItem> originalItems;
    private LayoutInflater inflater;
    private OnDocumentClickListener onDocumentClickListener;
    @Nullable
    private String query;

    MarkdownListDocumentAdapter(Context context, ArrayList<FunctionDocumentItem> documentItems) {
        this.inflater = LayoutInflater.from(context);
        this.originalItems = documentItems;
        this.filteredItems = new ArrayList<>(documentItems);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_programming_document, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final FunctionDocumentItem item = filteredItems.get(position);
        holder.txtName.setText(highlightQuery(item.getName()));
        if (item.getDescription() == null) {
            holder.txtDescription.setVisibility(View.GONE);
        } else {
            holder.txtDescription.setText(highlightQuery(item.getDescription()));
            holder.txtDescription.setVisibility(View.VISIBLE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDocumentClickListener != null) {
                    onDocumentClickListener.onDocumentClick(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredItems.size();
    }

    private CharSequence highlightQuery(String text) {
        if (query == null || query.isEmpty()) {
            return text;
        }
        SpannableString spannableString = new SpannableString(text);
        Pattern pattern = Pattern.compile(Pattern.quote(query), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(spannableString);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            spannableString.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }

    public void query(@NonNull String query) {
        query = query.toLowerCase();
        this.query = query;

        filteredItems.clear();
        for (FunctionDocumentItem item : originalItems) {
            if (item.getName().toLowerCase().contains(query)) {
                filteredItems.add(item);
            }
        }
        notifyDataSetChanged();
    }

    public void setOnDocumentClickListener(OnDocumentClickListener onDocumentClickListener) {
        this.onDocumentClickListener = onDocumentClickListener;
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        return filteredItems.get(position).getName().substring(0, 1);
    }

    public interface OnDocumentClickListener {

        void onDocumentClick(FunctionDocumentItem item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        TextView txtDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            txtDescription = itemView.findViewById(R.id.txt_description);
        }
    }
}
