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

package com.duy.ncalc.systemequations;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.duy.calculator.R;
import com.duy.calculator.evaluator.Constants;
import com.duy.calculator.evaluator.EvaluateConfig;
import com.duy.calculator.evaluator.MathEvaluator;
import com.duy.calculator.evaluator.thread.ResultCallback;
import com.duy.calculator.symja.models.SystemEquationItem;
import com.duy.ncalc.view.ResizingEditText;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.gx.common.collect.Lists;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Pattern;

import io.github.kexanie.library.MathView;

import static android.view.View.GONE;

public class DefineSystemEquationFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = DefineSystemEquationFragment.class.getName();
    private static final String STARTED = DefineSystemEquationFragment.class.getSimpleName() + "started";
    char[] defaultVariable = "xyztuvabcdefgh".toCharArray();
    private RelativeLayout mContainer;
    private int numberVariable = 1;
    private boolean isDebug = true;
    private Random random = new Random(231378L);
    private Spinner mSpinnerVariable;
    private ProgressBar mProgressBar;
    private MathView mMathView;
    private Button btnSolve;
    private Button btnClear;
    private EditText editVar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.define_system_equation, container, false);
    }

    public View findViewById(int id) {
        return getView().findViewById(id);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContainer = (RelativeLayout) findViewById(R.id.container);
        mMathView = (MathView) findViewById(R.id.math_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        btnSolve = (Button) findViewById(R.id.btn_submit);
        btnSolve.setOnClickListener(this);
        btnClear = (Button) findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(this);

        editVar = (EditText) findViewById(R.id.edit_variable);
        initSpinOperator();
        findViewById(R.id.btn_help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHelp();
            }
        });
    }

    private void initSpinOperator() {
        mSpinnerVariable = (Spinner) findViewById(R.id.spinner);
        //limit 20 variable
        String[] s = new String[10];
        for (int i = 0; i < 10; i++) s[i] = String.valueOf(i + 2);

        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_selectable_list_item, s);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerVariable.setAdapter(mAdapter);
        mSpinnerVariable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int old = numberVariable;
                numberVariable = Integer.parseInt(mSpinnerVariable.getSelectedItem().toString());
                new CreateMatrixTask(old, old + 1, numberVariable, numberVariable + 1, mContainer).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_submit:
                doEval();
                break;
            case R.id.btn_clear:
                clear(numberVariable, numberVariable + 1);
                break;
        }
    }

    private void clear(int row, int col) {
        int index = 1;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                EditText editText = (EditText) mContainer.findViewById(index);
                if (editText != null) editText.setText(null);
                index++;
            }
        }
        mMathView.setText("");
    }


    public String[][] getMatrix(int row, int col) {
        String res[][] = new String[row][col];
        int index = 1;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                EditText editText = mContainer.findViewById(index);
                String s = editText.getText().toString();
                if (s.isEmpty()) s = "0";
                index++;
                res[i][j] = s;
            }
        }
        return res;
    }

    public void setMatrix(String[][] matrix, int row, int col) {
//       int index = 1;
//        for (int i = 0; i < row; i++) {
//            for (int j = 0; j < col; j++) {
//                EditText editText = (EditText) mContainer.findViewById(index);
//                editText.setText(matrix[i][j]);
//                index++;
//            }
//        }
    }


    public void doEval() {
        String[][] arr = getMatrix(numberVariable, numberVariable + 1);
        String[] var = editVar.getText().toString().split(Pattern.quote(","));

        if (var.length != numberVariable) {
            editVar.requestFocus();
            String msg = "Number variable is " + numberVariable
                    + ". But current number variable is " + var.length;
            editVar.setError(msg);
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i < numberVariable; i++) {
            var[i] = String.valueOf(defaultVariable[i]);
        }
        mProgressBar.setVisibility(View.VISIBLE);
        mMathView.setText("");
        SystemEquationItem item = new SystemEquationItem(numberVariable, numberVariable + 1, arr, var);
        TaskSolveSystemEquation task = new TaskSolveSystemEquation(new ResultCallback() {
            @Override
            public void onSuccess(ArrayList<String> result) {
                mProgressBar.setVisibility(GONE);
                StringBuilder res = new StringBuilder();
                for (String s : result) {
                    res.append(s).append("</br>").append(Constants.WEB_SEPARATOR);
                }
                mMathView.setText(res.toString());
            }

            @Override
            public void onError(Exception e) {
                mProgressBar.setVisibility(GONE);
                mMathView.setText(e.getMessage());
            }
        }, getContext());
        task.execute(item);
    }

    private void showHelp() {
        final SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        TapTarget target1 = TapTarget.forView(mSpinnerVariable,
                getString(R.string.select_num_var),
                getString(R.string.select_num_var_des))
                .drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark).targetRadius(70);

        TapTarget target2 = TapTarget.forView(mContainer,
                getString(R.string.enter_coefficient),
                getString(R.string.enter_coeff_desc))
                .drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark)
                .targetRadius(70);

        TapTarget target3 = TapTarget.forView(editVar, getString(R.string.enter_variable));
        target3.drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark);

        TapTarget target4 = TapTarget.forView(btnSolve, getString(R.string.solve_system_equation));
        target4.drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark);

        TapTargetSequence sequence = new TapTargetSequence(getActivity());
        sequence.targets(target1, target2, target3, target4);
        sequence.listener(new TapTargetSequence.Listener() {
            @Override
            public void onSequenceFinish() {
                editor.putBoolean(STARTED, true);
                editor.apply();
                doEval();
            }

            @Override
            public void onSequenceCanceled(TapTarget lastTarget) {
            }
        });
//        addParams("2x - y = 2");
//        addParams("3x + 2y = 0");
         sequence.start();
    }

    /**
     * create matrix view
     */
    private class CreateMatrixTask extends AsyncTask<Void, Integer, Void> {
        private int col;
        private int row;
        private ViewGroup mViewGroup;
        private String[][] currentMatrix;

        public CreateMatrixTask(int rowOld, int colOld, int rowNew, int colNew, ViewGroup viewGroup) {
            this.row = rowNew;
            this.col = colNew;
            this.mViewGroup = viewGroup;
//            currentMatrix = getMatrix(rowOld, rowNew);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mSpinnerVariable.setEnabled(false);
            btnSolve.setEnabled(false);
            btnClear.setEnabled(false);
            mViewGroup.post(new Runnable() {
                @Override
                public void run() {
                    mViewGroup.removeAllViews();
                }
            });
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d(TAG, "createLayoutMatrix: " + col + " " + row);
            int index = 1;
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    publishProgress(i, j, index);
                    index++;
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int i = values[0];
            int j = values[1];
            int index = values[2];
            ResizingEditText editText = new ResizingEditText(getContext());
            editText.setHint("[" + (i + 1) + "," + (j + 1) + "]");
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(130, RelativeLayout.LayoutParams.WRAP_CONTENT);
            editText.setSingleLine(true);
            editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            editText.setId(values[2]);
            if (isDebug) {
                editText.setText(String.valueOf(random.nextInt(200) - 100));
            }
            if (values[1] == 0)
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            else
                params.addRule(RelativeLayout.RIGHT_OF, index - 1);

            if (i == 0)
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            else
                params.addRule(RelativeLayout.BELOW, index - col);

            editText.setLayoutParams(params);
            mViewGroup.addView(editText);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            btnSolve.setEnabled(true);
            btnClear.setEnabled(true);
            mSpinnerVariable.setEnabled(true);
            String s = "";
            for (int i = 0; i < numberVariable; i++) {
                s += defaultVariable[i];
                if (i != numberVariable - 1) s += ",";
            }
            editVar.setText(s);
        }
    }

    /**
     * clickSolveEquation system equation
     */
    private class TaskSolveSystemEquation extends AsyncTask<SystemEquationItem, Void, ArrayList<String>> {
        private ResultCallback resultCallback;
        private Context context;
        private Exception exception = null;

        public TaskSolveSystemEquation(ResultCallback resultCallback, Context context) {
            this.resultCallback = resultCallback;
            this.context = context;
        }

        @Override
        protected ArrayList<String> doInBackground(SystemEquationItem... params) {
            String input = params[0].getInput();

            if (params[0].isError(MathEvaluator.getInstance())) {
                String msg = params[0].getError(MathEvaluator.getInstance(), context);
                exception = new RuntimeException(msg);
                return null;
            }

            EvaluateConfig config = EvaluateConfig.loadFromSetting(context);
            try {
                String fraction = MathEvaluator.getInstance()
                        .solveSystemEquation(input, config.setEvalMode(EvaluateConfig.FRACTION), context);
                String decimal = MathEvaluator.getInstance()
                        .solveSystemEquation(input, config.setEvalMode(EvaluateConfig.DECIMAL), context);
                return Lists.newArrayList(fraction, decimal);
            } catch (Exception e) {
                this.exception = e;
            }
            return null;
        }


        @Override
        protected void onPostExecute(ArrayList<String> result) {
            super.onPostExecute(result);
            if (exception != null) {
                resultCallback.onError(exception);
            } else {
                resultCallback.onSuccess(result);
            }
        }
    }

}
