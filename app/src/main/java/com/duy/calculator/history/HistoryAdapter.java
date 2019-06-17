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

package com.duy.calculator.history;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.duy.ncalc.utils.DLog;
import com.duy.calculator.R;
import com.duy.calculator.symja.tokenizer.ExpressionTokenizer;
import com.duy.ncalc.utils.ClipboardManager;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private ArrayList<ResultEntry> itemHistories = new ArrayList<>();
    private Activity context;
    private HistoryListener listener = null;
    private DatabaseHelper database;
    private ExpressionTokenizer tokenizer;

    public HistoryAdapter(Activity context, ExpressionTokenizer tokenizer) {
        this.context = context;
        database = new DatabaseHelper(context);
        this.itemHistories = database.getAllItemHistory();
        this.tokenizer = tokenizer;
        for (ResultEntry entry : itemHistories) {
            DLog.i("HistoryEntry : " + entry.toString());
        }
    }

    public ArrayList<ResultEntry> getItemHistories() {
        return itemHistories;
    }

    public void setItemHistories(ArrayList<ResultEntry> itemHistories) {
        this.itemHistories = itemHistories;
    }

    public HistoryListener getListener() {
        return listener;
    }

    public void setListener(HistoryListener listener) {
        this.listener = listener;
    }

    public void addHistory(ResultEntry resultEntry) {
        itemHistories.add(resultEntry);
        database.removeItemHistory(resultEntry.getTime());
        notifyItemInserted(itemHistories.size() - 1);
    }

    public void removeHistory(ResultEntry resultEntry, int position) {
        itemHistories.remove(resultEntry);
        database.removeItemHistory(resultEntry.getTime());
        notifyItemRemoved(position);
    }

    public void removeHistory(int position) {
        itemHistories.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_entry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ResultEntry resultEntry = itemHistories.get(position);
        holder.txtMath.setText(tokenizer.getLocalizedExpression(resultEntry.getExpression()));
        holder.txtMath.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listener != null) listener.onItemLongClickListener(v, resultEntry);
                return false;
            }
        });
        holder.txtMath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onItemClickListener(v, resultEntry);

            }
        });

        holder.txtResult.setText(resultEntry.getResult());
        holder.txtResult.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listener != null) listener.onItemLongClickListener(v, resultEntry);
                return false;
            }
        });
        holder.txtResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onItemClickListener(v, resultEntry);

            }
        });

        holder.imgCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager.setClipboard(context, resultEntry.getExpression());
            }
        });
        holder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, resultEntry.getExpression() + " = " + resultEntry.getResult());
                intent.setType("text/plain");
                context.startActivity(intent);
            }
        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(position);
            }
        });

    }

    public boolean removeItem(int position) {
        if (position > itemHistories.size() - 1) {
            return false;
        }
        itemHistories.remove(position);
        notifyItemRemoved(position);
        return true;
    }

    @Override
    public int getItemCount() {
        return itemHistories.size();
    }

    public ResultEntry getItem(int index) {
        return itemHistories.get(index);
    }

    public void clear() {
        itemHistories.clear();
        notifyItemRangeRemoved(0, getItemCount());
    }


    public interface HistoryListener {
        void onItemClickListener(View view, ResultEntry resultEntry);

        void onItemLongClickListener(View view, ResultEntry resultEntry);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMath;
        TextView txtResult;

        CardView cardView;
        ImageView imgShare;
        ImageView imgCopy;
        View imgDelete;
        View imgEdit;

        public ViewHolder(View itemView) {
            super(itemView);
            txtMath = (TextView) itemView.findViewById(R.id.txt_input);
            txtResult = (TextView) itemView.findViewById(R.id.txt_result);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            imgShare = (ImageView) itemView.findViewById(R.id.img_share_item);

            imgCopy = (ImageView) itemView.findViewById(R.id.img_copy_item);
            imgDelete = itemView.findViewById(R.id.img_delete_item);
            imgEdit = itemView.findViewById(R.id.img_edit);

        }
    }
}
