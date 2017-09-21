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

package com.duy.calculator.matrix;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duy.calculator.R;
import com.duy.calculator.evaluator.EvaluateConfig;
import com.duy.calculator.evaluator.MathEvaluator;

import io.github.kexanie.library.MathView;


/**
 * Created by Duy on 18-May-17.
 */
public class FragmentMatrixResult extends AppCompatDialogFragment {
    public static final String TAG = "FragmentMatrixEval";
    private static final java.lang.String EXPRESSION_KEY = "expression_key";
    private MathView mathView;

    public static FragmentMatrixResult newInstance(String expression) {
        Bundle args = new Bundle();
        args.putString(EXPRESSION_KEY, expression);

        FragmentMatrixResult fragment = new FragmentMatrixResult();
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
        if (isAdded()) {
            new MatrixEvaluateTask().execute(expression);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle(R.string.result);
        mathView = view.findViewById(R.id.math_view);
        doCalculate(getArguments().getString(EXPRESSION_KEY));
    }

    /**
     * Created by Duy on 18-May-17.
     */

    public class MatrixEvaluateTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                return MathEvaluator.newInstance().evaluateWithResultAsTex(params[0],
                        EvaluateConfig.loadFromSetting(getContext()));
            } catch (Exception e) {
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String strings) {
            super.onPostExecute(strings);
            mathView.setText(strings);
        }
    }
}
