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

package com.duy.calculator.document;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duy.calculator.R;
import com.mukesh.MarkdownView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


/**
 * Created by Duy on 19-May-17.
 */
public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.ViewHolder> {
    private static final String DOC_PATH = "doc/functions/";
    private static final String TAG = "DocumentAdapter";
    private Context context;
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> originalData = new ArrayList<>();
    private LayoutInflater inflater;

    public DocumentAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        loadData();
    }

    private void loadData() {
        Log.d(TAG, "loadData() called");

        try {
            String[] functions = context.getAssets().list("doc/functions");
            Collections.addAll(names, functions);
            originalData.addAll(names);
            Log.d(TAG, "loadData: " + Arrays.toString(functions));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.list_item_document, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder() called with: holder = [" + holder + "], position = [" + position + "]");
        holder.markdownView.loadMarkdownFromAssets(DOC_PATH + names.get(position));
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public void query(String query) {
        names.clear();
        notifyDataSetChanged();
        for (String s : originalData) {
            if (s.toLowerCase().contains(query.toLowerCase())) {
                names.add(s);
                notifyItemInserted(names.size() - 1);
            }
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        MarkdownView markdownView;

        public ViewHolder(View itemView) {
            super(itemView);
            markdownView = (MarkdownView) itemView.findViewById(R.id.markdown_view);
        }
    }
}
