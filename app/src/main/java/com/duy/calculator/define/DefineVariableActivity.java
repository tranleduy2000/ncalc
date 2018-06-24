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

package com.duy.calculator.define;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.duy.calculator.activities.base.AbstractAppCompatActivity;
import com.duy.calculator.R;
import com.duy.calculator.evaluator.MathEvaluator;
import com.duy.calculator.tokenizer.Tokenizer;
import com.duy.calculator.calc.BasicCalculatorActivity;

import java.util.ArrayList;


/**
 * Definetion getValue() of list variable
 * <p>
 * Created by Duy on 04-Dec-16.
 */

public class DefineVariableActivity extends AbstractAppCompatActivity {
    String TAG = DefineVariableActivity.class.getSimpleName();
    private MathEvaluator mEvaluator;
    private Tokenizer mTokenizer;
    private VariableAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.define_variable_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rc_var);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DefineVariableActivity.this);
        linearLayoutManager.setStackFromEnd(false);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new VariableAdapter(DefineVariableActivity.this);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.removeVar(viewHolder.getAdapterPosition());
            }
        });
        helper.attachToRecyclerView(recyclerView);

        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSave();
                finish();
            }
        });
        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.add();
            }
        });
        ArrayList<String> vars = getIntentData();
        for (String s : vars) {
            adapter.addVar(new VariableEntry(s, ""));
        }
    }

    private ArrayList<String> getIntentData() {
        Intent intent = getIntent();
        try {
            String expr = intent.getStringExtra(BasicCalculatorActivity.DATA);
            Log.d(TAG, "getIntentData: " + expr);
            ArrayList<String> variables = mEvaluator.getListVariables(expr);
            for (String s : variables) {
                Log.d(TAG, "getIntentData: " + s);
            }
            return variables;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return new ArrayList<String>();
    }

    @Override
    public void onBackPressed() {
        doSave();
        super.onBackPressed();
    }

    //save variable
    private void doSave() {
        ArrayList<VariableEntry> entries = adapter.getEntries();
        for (VariableEntry e : entries) {
            Log.d(TAG, "doSave: " + e.getName() + e.getValue());
            if (mEvaluator.isNumber(e.getValue()) && !e.getName().isEmpty()) {
                mEvaluator.define(e.getName(), e.getValue());
            } else {
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }


}
