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

import com.example.duy.calculator.activities.abstract_class.AbstractAppCompatActivity;
import com.example.duy.calculator.R;
import com.example.duy.calculator.math_eval.Tokenizer;
import com.example.duy.calculator.activities.BasicCalculatorActivity;

import java.util.ArrayList;


/**
 * Created by DUy on 29-Nov-16.
 */

public class HistoryActivity extends AbstractAppCompatActivity implements HistoryAdapter.HistoryListener {
    private RecyclerView mRecyclerView;
    private FloatingActionButton btnClear;
    private HistoryAdapter mHistoryAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mRecyclerView = (RecyclerView) findViewById(R.id.historyRecycler);
        btnClear = (FloatingActionButton) findViewById(R.id.btn_delete_all);

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
        ArrayList<ResultEntry> histories = mHistoryAdapter.getItemHistories();
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
     * @param resultEntry
     */
    @Override
    public void onItemClickListener(View view, ResultEntry resultEntry) {
        //finish
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BasicCalculatorActivity.DATA, resultEntry);
        intent.putExtra(BasicCalculatorActivity.DATA, bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * history long click, copy text to clipboard
     *
     * @param view
     * @param resultEntry
     */
    @Override
    public void onItemLongClickListener(View view, ResultEntry resultEntry) {
        Toast.makeText(HistoryActivity.this,
                getString(R.string.copied) + " \n" +
                        resultEntry.getExpression(),
                Toast.LENGTH_SHORT).show();
    }
}
