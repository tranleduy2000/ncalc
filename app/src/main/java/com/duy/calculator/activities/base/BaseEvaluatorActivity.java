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

package com.duy.calculator.activities.base;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.duy.calculator.R;
import com.duy.calculator.evaluator.EvaluateConfig;
import com.duy.calculator.evaluator.exceptions.ExpressionChecker;
import com.duy.calculator.evaluator.exceptions.ParsingException;
import com.duy.calculator.evaluator.thread.BaseThread;
import com.duy.calculator.evaluator.thread.CalculateThread;
import com.duy.calculator.evaluator.thread.Command;
import com.duy.calculator.history.ResultEntry;
import com.duy.calculator.symja.activities.ResultAdapter;
import com.duy.ncalc.document.FunctionSuggestionAdapter;
import com.duy.ncalc.document.MarkdownDocumentActivity;
import com.duy.ncalc.document.model.FunctionDocumentItem;
import com.duy.ncalc.view.ResizingEditText;

import org.matheclipse.parser.client.SyntaxError;
import org.matheclipse.parser.client.math.MathException;

import java.util.ArrayList;


/**
 * Abstract for eval equation, trig to exp,...
 * <p>
 * Created by Duy on 19/7/2016
 */
public abstract class BaseEvaluatorActivity extends NavDrawerActivity
        implements View.OnClickListener, FunctionSuggestionAdapter.OnSuggestionClickListener {
    protected String TAG = BaseEvaluatorActivity.class.getName();

    /*Limit, integrate, derivative*/
    protected EditText mEditLowerBound, mEditUpperBound;
    protected LinearLayout mLayoutLimit;
    protected TextInputLayout mHint1, mHint2;
    protected AppCompatSpinner mSpinner;

    protected ResizingEditText mInputFormula;
    protected ResizingEditText mInputFormula2;

    protected SharedPreferences mPreferences;
    protected ContentLoadingProgressBar mProgress;
    protected Button mBtnClear, mBtnEvaluate;

    protected RecyclerView mResultView;
    private ResultAdapter mResultAdapter;
    private final View.OnKeyListener mFormulaOnKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_NUMPAD_ENTER:
                case KeyEvent.KEYCODE_ENTER:
                    if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
                        clickEvaluate();
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluator);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        initView();
    }

    /**
     * restore input
     */
    @Override
    protected void onResume() {
        super.onResume();
        String input = mCalculatorSetting.getString("input_" + getClass().getSimpleName());
        mInputFormula.setText(input);
    }

    /**
     * save input
     */
    @Override
    protected void onPause() {
        super.onPause();
        mCalculatorSetting.put("input_" + getClass().getSimpleName(),
                mInputFormula.getText().toString());
    }

    private void initView() {
        mBtnEvaluate = findViewById(R.id.btn_solve);
        mInputFormula = findViewById(R.id.edit_input);
        mInputFormula.setOnSuggestionClickListener(this);

        mProgress = findViewById(R.id.progress_bar);
        mSpinner = findViewById(R.id.spinner);
        mBtnClear = findViewById(R.id.btn_clear);
        mInputFormula2 = findViewById(R.id.edit_input_2);
        mHint1 = findViewById(R.id.hint_1);
        mHint2 = findViewById(R.id.hint_2);

        mBtnClear.setOnClickListener(this);
        mBtnEvaluate.setOnClickListener(this);
        mProgress.hide();
        findViewById(R.id.fab_help).setOnClickListener(this);
        mEditLowerBound = findViewById(R.id.edit_lower);
        mEditUpperBound = findViewById(R.id.edit_upper);
        mLayoutLimit = findViewById(R.id.layout_limit);
        mLayoutLimit.setVisibility(View.GONE);
        mInputFormula.setOnKeyListener(mFormulaOnKeyListener);

        mResultView = findViewById(R.id.rc_result);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(false);
        mResultView.setHasFixedSize(true);
        mResultView.setLayoutManager(linearLayoutManager);
        mResultAdapter = new ResultAdapter(this);
        mResultView.setAdapter(mResultAdapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_clear:
                clickClear();
                break;
            case R.id.btn_solve:
                clickEvaluate();
                break;
            case R.id.fab_help:
                clickHelp();
                break;
        }
    }


    public void clickClear() {
        mInputFormula.setText("");
        mEditLowerBound.setText("");
        mEditUpperBound.setText("");
        mInputFormula2.setText("");
    }

    /**
     * Evaluate expression
     */
    @CallSuper
    public void clickEvaluate() {
        //if input empty, do not evaluate
        if (mInputFormula.getText().toString().isEmpty()) {
            mInputFormula.requestFocus();
            mInputFormula.setError(getString(R.string.enter_expression));
            return;
        }

        try {
            ExpressionChecker.checkExpression(mInputFormula.getCleanText());
        } catch (Exception e) {
            hideKeyboard();
            handleExceptions(mInputFormula, e);
            return;
        }

        String expr = getExpression();
        if (expr == null) {
            Toast.makeText(this, "Invalid Input", Toast.LENGTH_SHORT).show();
            return;
        }
        Command<ArrayList<String>, String> command = getCommand();

        mProgress.show();
        mBtnEvaluate.setEnabled(false);
        mBtnClear.setEnabled(false);
        hideKeyboard();
        mResultAdapter.clear();

        EvaluateConfig config = EvaluateConfig.loadFromSetting(this);
        CalculateThread calculateThread = new CalculateThread(config, new BaseThread.ResultCallback() {
            @Override
            public void onSuccess(ArrayList<String> result) {
                Log.d(TAG, "onSuccess() called with: result = [" + result + "]");

                hideKeyboard();
                mProgress.hide();
                mBtnEvaluate.setEnabled(true);
                mBtnClear.setEnabled(true);

                for (String entry : result) {
                    mResultAdapter.addItem(new ResultEntry("", entry));
                }

                if (mResultAdapter.getItemCount() > 0) {
                    mResultView.scrollToPosition(0);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.d(TAG, "onError() called with: e = [" + e + "]");

                handleExceptions(mInputFormula, e);
                mProgress.hide();
                mBtnEvaluate.setEnabled(true);
                mBtnClear.setEnabled(true);
            }
        });
        calculateThread.execute(command, expr);
    }

    protected void handleExceptions(EditText editText, Exception e) {
        if (e instanceof SyntaxError) {
            int start = Math.min(editText.length(), ((SyntaxError) e).getColumnIndex() - 1);
            int end = Math.min(editText.length(), ((SyntaxError) e).getColumnIndex());
            editText.setSelection(start, end);
            mResultAdapter.clear();
            mResultAdapter.addItem(new ResultEntry("SYNTAX ERROR", e.getMessage()));
        } else if (e instanceof MathException) {
            mResultAdapter.clear();
            mResultAdapter.addItem(new ResultEntry("MATH ERROR", e.getMessage()));
        } else if (e instanceof ParsingException) {
            int start = Math.min(editText.length(), ((ParsingException) e).getIndex());
            int end = Math.min(editText.length(), ((ParsingException) e).getIndex() + 1);
            editText.setSelection(start, end);
            mResultAdapter.clear();
            mResultAdapter.addItem(new ResultEntry("SYNTAX ERROR", e.getMessage()));
        } else {
            mResultAdapter.clear();
            mResultAdapter.addItem(new ResultEntry("Unknown error", e.getMessage()));
        }
        editText.setError("Error!");
    }

    protected String getExpression() {
        return mInputFormula.getCleanText();
    }

    /**
     * show target helper
     */
    public abstract void clickHelp();

    @Override
    public void clickOpenDocument(FunctionDocumentItem functionDocumentItem) {
        MarkdownDocumentActivity.open(this, functionDocumentItem);
    }


    @Nullable
    public abstract Command<ArrayList<String>, String> getCommand();

}
