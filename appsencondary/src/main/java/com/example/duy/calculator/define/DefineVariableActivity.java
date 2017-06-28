package com.example.duy.calculator.define;

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

import com.example.duy.calculator.AbstractAppCompatActivity;
import com.example.duy.calculator.R;
import com.example.duy.calculator.math_eval.BigEvaluator;
import com.example.duy.calculator.version_old.activities.BasicCalculatorActivity;

import java.util.ArrayList;


/**
 * <<<<<<< HEAD
 * Definetion value of list variable
 * =======
 * Definetion getValue() of list variable
 * >>>>>>> refs/remotes/origin/master
 * <p>
 * Created by DUy on 04-Dec-16.
 */

public class DefineVariableActivity extends AbstractAppCompatActivity {
    String TAG = DefineVariableActivity.class.getSimpleName();
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
            ArrayList<String> variables = BigEvaluator.newInstance(this).getListVariables(expr);
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
            if (BigEvaluator.newInstance(this).isNumber(e.getValue()) && !e.getName().isEmpty()) {
                BigEvaluator.newInstance(this).define(e.getName(), e.getValue());
//                database.addVariable(e.getName(), e.getValue());
            } else {
                // Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
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
