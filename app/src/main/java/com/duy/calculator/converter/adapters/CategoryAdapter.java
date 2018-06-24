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

package com.duy.calculator.converter.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.duy.calculator.R;

import java.util.ArrayList;

/**
 * Custom adapter for activity unit converter parent
 * Created by tranleduy on 27-May-16.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private String arrTitle[];
    private ArrayList<Integer> arrImg = new ArrayList<>();
    private Context context;
    private OnItemClickListener mListener;

    public CategoryAdapter(String[] arrTitle, ArrayList<Integer> arrImg, Context context) {
        this.arrTitle = arrTitle;
        this.arrImg = arrImg;
        this.context = context;
    }

    public CategoryAdapter(ArrayList<Integer> arrImg, Context context) {
        this.arrImg = arrImg;
        this.context = context;
        this.arrTitle = context.getResources().getStringArray(R.array.unit);
    }

    public CategoryAdapter(Context context) {
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_categoty_unit_converter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.textView.setText(arrTitle[position]);
        if (position < arrImg.size()) {
            holder.imageView.setImageResource(arrImg.get(position));
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.onItemClick(position, arrTitle[position]);
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mListener != null) mListener.onItemLongClick();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrTitle.length;
    }

    public void setListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(int pos, String text);

        public void onItemLongClick();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_title);
            imageView = (ImageView) itemView.findViewById(R.id.item_img);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }
}