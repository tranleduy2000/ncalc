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

package com.example.duy.calculator.document;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.duy.calculator.R;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;

import us.feras.mdv.MarkdownView;

/**
 * Created by Duy on 19-May-17.
 */

class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.ViewHolder> {
    private static final String DOC_PATH = "file:///android_asset/functions/";
    private Context context;
    private LinkedList<String> name = new LinkedList<>();

    DocumentAdapter(Context context) {
        this.context = context;
        loadData();
    }

    private void loadData() {
        try {
            String[] functions = context.getAssets().list("functions");
            Collections.addAll(name, functions);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private LayoutInflater inflater;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.list_item_document, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.markdownView.loadMarkdownFile(DOC_PATH + name.get(position));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        MarkdownView markdownView;

        public ViewHolder(View itemView) {
            super(itemView);
            markdownView = (MarkdownView) itemView.findViewById(R.id.markdown_view);
        }
    }
}
