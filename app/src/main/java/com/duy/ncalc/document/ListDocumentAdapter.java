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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duy.calculator.R;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by Duy on 19-May-17.
 */
public class ListDocumentAdapter extends RecyclerView.Adapter<ListDocumentAdapter.ViewHolder> {
    private static final String DOT_MD = ".md";

    private Context context;
    private ArrayList<String> fileNames = new ArrayList<>();
    private ArrayList<String> originalData = new ArrayList<>();
    private LayoutInflater inflater;
    private String assetPath;
    private OnDocumentClickListener onDocumentClickListener;

    ListDocumentAdapter(Context context, String assetPath) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.assetPath = assetPath;
        loadData();
    }

    private void loadData() {
        try {
            String[] functions = context.getAssets().list(assetPath);
            for (String function : functions) {
                if (function.endsWith(".md")) {
                    fileNames.add(function);
                }
            }
            originalData.addAll(fileNames);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_document, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String fileName = fileNames.get(position);
        if (fileName.toLowerCase().endsWith(DOT_MD)) {
            String nameWithoutExt = fileName.substring(0, fileName.length() - 3);
            nameWithoutExt = nameWithoutExt.replace("-", " ");
            String name = StringHelper.capitalize(nameWithoutExt);

            holder.txtName.setText(name);
            holder.txtName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onDocumentClickListener != null) {
                        onDocumentClickListener.onDocumentClick(
                                assetPath + "/" + fileName);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return fileNames.size();
    }

    public void query(String query) {
        fileNames.clear();
        notifyDataSetChanged();
        for (String s : originalData) {
            if (s.toLowerCase().replace(" ", "").contains(query.toLowerCase())) {
                fileNames.add(s);
            }
        }
        notifyDataSetChanged();
    }

    public void setOnDocumentClickListener(OnDocumentClickListener onDocumentClickListener) {
        this.onDocumentClickListener = onDocumentClickListener;
    }

    public interface OnDocumentClickListener {
        void onDocumentClick(String path);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
        }
    }
}
