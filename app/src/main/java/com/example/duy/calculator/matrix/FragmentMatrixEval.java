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

package com.example.duy.calculator.matrix;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.duy.calculator.R;
import com.example.duy.calculator.item_math_type.StepItem;
import com.example.duy.calculator.math_eval.BigEvaluator;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by Duy on 18-May-17.
 */
public class FragmentMatrixEval extends AppCompatDialogFragment {
    public static final String TAG = "FragmentMatrixEval";
    private static final java.lang.String EXPRESSION_KEY = "expression_key";
    private MatrixResultAdapter resultAdapter;
    private RecyclerView resultView;
    private ProgressBar progressBar;

    /**
     * create new dialog calculate
     *
     * @param expression - input matrix
     */
    public static FragmentMatrixEval newInstance(String expression) {
        Bundle args = new Bundle();
        args.putString(EXPRESSION_KEY, expression);

        FragmentMatrixEval fragment = new FragmentMatrixEval();
        fragment.setArguments(args);
        return fragment;
    }


    /**
     * set full height for dialog
     */
    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_matrix_result, container, false);
    }

    public void doCalculate(String expression) {
        Log.d(TAG, "doCalculate() called with: expression = [" + expression + "]");
        if (isAdded()) {
            new MatrixEvaluateTask().execute(expression);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().setTitle(R.string.result);

        resultView = (RecyclerView) view.findViewById(R.id.recycle_view);
        resultView.setHasFixedSize(false);
        resultView.setLayoutManager(new LinearLayoutManager(getContext()));
        resultAdapter = new MatrixResultAdapter(getContext());
        resultView.setAdapter(resultAdapter);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        doCalculate(getArguments().getString(EXPRESSION_KEY));
    }

    /**
     * Created by Duy on 18-May-17.
     */

    public class MatrixEvaluateTask extends AsyncTask<String, Void, ArrayList<StepItem>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<StepItem> doInBackground(String... params) {
            Log.d(TAG, "doInBackground() called with: params = [" + Arrays.toString(params) + "]");
            BigEvaluator evaluator = BigEvaluator.newInstance(getContext());
            return evaluator.getStep(params[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<StepItem> strings) {
            super.onPostExecute(strings);
            progressBar.setVisibility(View.GONE);
            resultAdapter.setResults(strings);
            resultAdapter.notifyDataSetChanged();
        }

    }
}
