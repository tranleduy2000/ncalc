package com.example.duy.calculator.history;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.duy.calculator.AbstractAppCompatActivity;
import com.example.duy.calculator.R;
import com.example.duy.calculator.math_eval.Tokenizer;
import com.example.duy.calculator.version_old.activities.BasicCalculatorActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * <<<<<<< HEAD
 * History activity
 * <p>
 * =======
 * >>>>>>> refs/remotes/origin/master
 * Created by DUy on 29-Nov-16.
 */

public class HistoryActivity extends AbstractAppCompatActivity implements HistoryAdapter.HistoryListener {
    @BindView(R.id.historyRecycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.btn_delete_all)
    FloatingActionButton btnClear;
    private HistoryAdapter mHistoryAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Tokenizer tokenizer = new Tokenizer(this);

        mHistoryAdapter = new HistoryAdapter(this, tokenizer);
        mHistoryAdapter.setListener(this);
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                mHistoryAdapter.removeHistory(viewHolder.getAdapterPosition());
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mHistoryAdapter);
        mRecyclerView.scrollToPosition(mHistoryAdapter.getItemCount() - 1);
        helper.attachToRecyclerView(mRecyclerView);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHistoryDatabase.clearHistory();
                mHistoryAdapter.clear();
                mHistoryAdapter.notifyDataSetChanged();
            }
        });

        /**
         * custom event fab show and hide when scroll recycle view
         */
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    btnClear.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && btnClear.isShown()) {
                    btnClear.hide();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }


    @Override
    protected void onPause() {
        doSave();
        super.onPause();
    }

    private void doSave() {
        ArrayList<HistoryEntry> histories = mHistoryAdapter.getItemHistories();
        mHistoryDatabase.clearHistory();
        mHistoryDatabase.saveHistory(histories);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            doSave();
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * set bundle data and finish activity
     *
     * @param view
     * @param historyEntry
     */
    @Override
    public void onItemClickListener(View view, HistoryEntry historyEntry) {
        //finish
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BasicCalculatorActivity.DATA, historyEntry);
        intent.putExtra(BasicCalculatorActivity.DATA, bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * history long click, copy text to clipboard
     *
     * @param view
     * @param historyEntry
     */
    @Override
    public void onItemLongClickListener(View view, HistoryEntry historyEntry) {
        Toast.makeText(HistoryActivity.this,
                getString(R.string.copied) + " \n" +
                        historyEntry.getMath(),
                Toast.LENGTH_SHORT).show();
    }
}
