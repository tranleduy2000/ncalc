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

package com.example.duy.calculator.converter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.duy.calculator.R;

import java.util.ArrayList;

/**
 * custom adapter for rc view
 */
public class UnitAdapter extends RecyclerView.Adapter<UnitAdapter.Holder> {
    final Context context;
    private ArrayList<ItemUnitConverter> mList = new ArrayList<>();

    public UnitAdapter(Context context, ArrayList<ItemUnitConverter> mList) {
        this.context = context;
        this.mList = mList;
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<ItemUnitConverter> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_result_unit, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        ItemUnitConverter itemUnitConverter = mList.get(position);
        holder.txtUnit.setText(itemUnitConverter.getTitle());
        holder.txtResult.setText(String.valueOf(itemUnitConverter.getRes()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        TextView txtUnit, txtResult;

        public Holder(View itemView) {
            super(itemView);
            txtUnit = (TextView) itemView.findViewById(R.id.txtTitle);
            txtResult = (TextView) itemView.findViewById(R.id.txtResult);
        }
    }
}