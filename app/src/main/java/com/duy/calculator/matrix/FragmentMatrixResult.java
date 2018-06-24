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
                return MathEvaluator.getInstance().evaluateWithResultAsTex(params[0],
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
