package com.example.duy.calculator.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.duy.calculator.R;
import com.example.duy.calculator.history.HistoryEntry;
import com.example.duy.calculator.utils.ClipboardManager;

import java.util.ArrayList;

import io.github.kexanie.library.MathView;

/**
 * adapter for recycle view mResult
 */
public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {
    final static String TAG = "ResultAdapter";
    private Activity mActivity;
    private ArrayList<HistoryEntry> mResults = new ArrayList<>();
    private OnItemListener listener = null;


    public ResultAdapter(Activity activity) {
        mActivity = activity;

    }

    public ArrayList<HistoryEntry> getResults() {
        return mResults;
    }

    public void setResults(ArrayList<HistoryEntry> mResults) {
        this.mResults = mResults;
    }

    public void addItem(HistoryEntry item) {
        mResults.add(0, item);
        notifyItemInserted(0);
    }

    public boolean removeItem(int position) {
        if (position > mResults.size() - 1) {
            return false;
        }
        mResults.remove(position);
        notifyItemRemoved(position);
        return true;
    }

    public HistoryEntry getResult(int position) {
        if (position > mResults.size() - 1) {
            return new HistoryEntry("0", "0");
        }
        return mResults.get(position);
    }

    public void clear() {
        int size = mResults.size();
        if (mResults.size() == 1) {
            mResults.clear();
            notifyItemRemoved(0);
        } else {
            mResults.clear();
            notifyItemRangeRemoved(0, size);
        }
    }

    @Override
    public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.evaluated_entry, parent, false);
        return new ResultViewHolder(view);
    }

    public void setListener(OnItemListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(ResultViewHolder holder, final int position) {
        final HistoryEntry item = mResults.get(position);
        Log.d(TAG, "onBindViewHolder: " + item.getMath() + " = " + item.getResult());
        holder.txtMath.setText(item.getMath());
        holder.txtResult.setText(item.getResult());
        holder.imgCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager.setClipboard(mActivity, item.getResult());
            }
        });
        holder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, item.getMath() + " = " + item.getResult());
                intent.setType("text/plain");
                mActivity.startActivity(intent);
            }
        });
        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onEditExpr(item);
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    private void updateEmptyView() {

    }

    public interface OnItemListener {
        void onItemClickListener(View view, HistoryEntry historyEntry);

        void onItemLongClickListener(View view, HistoryEntry historyEntry);

        void onEditExpr(HistoryEntry item);

    }

    static class ResultViewHolder extends RecyclerView.ViewHolder {
        MathView txtMath;
        MathView txtResult;
        CardView cardView;
        ImageView imgShare;
        ImageView imgCopy;
        View imgDelete;
        View imgEdit;

        public ResultViewHolder(View itemView) {
            super(itemView);
            txtMath = (MathView) itemView.findViewById(R.id.txt_input);
            txtResult = (MathView) itemView.findViewById(R.id.txt_result);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            imgShare = (ImageView) itemView.findViewById(R.id.img_share_item);

            imgCopy = (ImageView) itemView.findViewById(R.id.img_copy_item);
            imgDelete = itemView.findViewById(R.id.img_delete_item);
            imgEdit = itemView.findViewById(R.id.img_edit);

        }
    }
}
