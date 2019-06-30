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

package com.duy.calculator.symja.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.duy.calculator.R;
import com.duy.calculator.history.ResultEntry;
import com.duy.ncalc.userinterface.ThemeManager;
import com.duy.ncalc.utils.ClipboardManager;

import java.util.ArrayList;

import io.github.kexanie.library.MathView;

/**
 * adapter for recycle view mResult
 */
public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {
    private final static String TAG = "ResultAdapter";
    @NonNull
    private final LayoutInflater inflater;
    private Activity activity;
    private ArrayList<ResultEntry> resultEntries = new ArrayList<>();
    @Nullable
    private OnItemListener listener = null;

    public ResultAdapter(Activity activity) {
        this.activity = activity;
        this.inflater = LayoutInflater.from(activity);
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_result, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ResultViewHolder holder, int position) {
        final ResultEntry item = resultEntries.get(position);
        Log.d(TAG, "onBindViewHolder: " + item.getExpression() + " = " + item.getResult());

        holder.txtMath.setDarkTextColor(ThemeManager.isLightTheme(activity));
        holder.txtResult.setDarkTextColor(ThemeManager.isLightTheme(activity));

        holder.txtMath.setText(item.getExpression());
        holder.txtResult.setText(item.getResult());

        holder.imgCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager.setClipboard(activity, item.getResult());
            }
        });
        holder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, item.getExpression() + " = " + item.getResult());
                intent.setType("text/plain");
                activity.startActivity(intent);
            }
        });
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onEditClick(item);
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return resultEntries.size();
    }

    public void setListener(@Nullable OnItemListener listener) {
        this.listener = listener;
    }

    public ArrayList<ResultEntry> getResults() {
        return resultEntries;
    }

    public void setResults(ArrayList<ResultEntry> mResults) {
        this.resultEntries = mResults;
    }

    public void addItem(ResultEntry item) {
        resultEntries.add(0, item);
        notifyItemInserted(0);
    }

    private void removeItem(int position) {
        if (position > resultEntries.size() - 1) {
            return;
        }
        resultEntries.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        int size = resultEntries.size();
        if (resultEntries.size() == 1) {
            resultEntries.clear();
            notifyItemRemoved(0);
        } else {
            resultEntries.clear();
            notifyItemRangeRemoved(0, size);
        }
    }

    public interface OnItemListener {

        void onEditClick(ResultEntry item);

    }

    static class ResultViewHolder extends RecyclerView.ViewHolder {
        MathView txtMath;
        MathView txtResult;
        CardView cardView;
        ImageView imgShare;
        ImageView imgCopy;
        View imgDelete;
        View imgEdit;

        ResultViewHolder(View itemView) {
            super(itemView);
            txtMath = itemView.findViewById(R.id.txt_input);
            txtResult = itemView.findViewById(R.id.txt_result);
            cardView = itemView.findViewById(R.id.card_view);
            imgShare = itemView.findViewById(R.id.img_share_item);

            imgCopy = itemView.findViewById(R.id.img_copy_item);
            imgDelete = itemView.findViewById(R.id.img_delete_item);
            imgEdit = itemView.findViewById(R.id.img_edit);

        }
    }
}