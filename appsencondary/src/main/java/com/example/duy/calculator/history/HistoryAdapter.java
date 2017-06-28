package com.example.duy.calculator.history;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.duy.calculator.DLog;
import com.example.duy.calculator.R;
import com.example.duy.calculator.data.Database;
import com.example.duy.calculator.math_eval.Tokenizer;
import com.example.duy.calculator.utils.ClipboardManager;
import com.example.duy.calculator.utils.MyClipboard;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter history for recycle view
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private ArrayList<HistoryEntry> itemHistories = new ArrayList<>();
    private Activity context;
    private HistoryListener listener = null;
    private Database database;
    private Tokenizer tokenizer;

    public HistoryAdapter(Activity context, Tokenizer tokenizer) {
        this.context = context;
        database = new Database(context);
        this.itemHistories = database.getAllItemHistory();
        this.tokenizer = tokenizer;
        for (HistoryEntry entry : itemHistories) {
            DLog.i("HistoryEntry : " + entry.toString());
        }
    }

    public ArrayList<HistoryEntry> getItemHistories() {
        return itemHistories;
    }

    public void setItemHistories(ArrayList<HistoryEntry> itemHistories) {
        this.itemHistories = itemHistories;
    }

    public HistoryListener getListener() {
        return listener;
    }

    public void setListener(HistoryListener listener) {
        this.listener = listener;
    }

    public void addHistory(HistoryEntry historyEntry) {
        itemHistories.add(historyEntry);
        database.removeItemHistory(historyEntry.getTime());
        notifyItemInserted(itemHistories.size() - 1);
    }

    public void removeHistory(HistoryEntry historyEntry, int position) {
        itemHistories.remove(historyEntry);
        database.removeItemHistory(historyEntry.getTime());
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final HistoryEntry historyEntry = itemHistories.get(position);
        holder.txtMath.setText(tokenizer.getLocalizedExpression(historyEntry.getMath()));
        holder.txtMath.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listener != null) listener.onItemLongClickListener(v, historyEntry);
                return false;
            }
        });
        holder.txtMath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onItemClickListener(v, historyEntry);

            }
        });

        holder.txtResult.setText(historyEntry.getResult());
        holder.txtResult.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listener != null) listener.onItemLongClickListener(v, historyEntry);
                return false;
            }
        });
        holder.txtResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onItemClickListener(v, historyEntry);

            }
        });

        holder.imgCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager.setClipboard(context, historyEntry.getMath());
            }
        });
        holder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, historyEntry.getMath() + " = " + historyEntry.getResult());
                intent.setType("text/plain");
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemHistories.size();
    }

    public HistoryEntry getItem(int index) {
        return itemHistories.get(index);
    }

    public void clear() {
        itemHistories.clear();
        notifyItemRangeRemoved(0, getItemCount());
    }


    public interface HistoryListener {
        void onItemClickListener(View view, HistoryEntry historyEntry);

        void onItemLongClickListener(View view, HistoryEntry historyEntry);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_math)
        TextView txtMath;
        @BindView(R.id.txt_result)
        TextView txtResult;
        @BindView(R.id.card_view)
        CardView cardView;
//        @BindView(R.id.tex_result)
//        MathView texResult;

        @BindView(R.id.img_share_item)
        ImageView imgShare;
        @BindView(R.id.img_copy_item)
        ImageView imgCopy;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
