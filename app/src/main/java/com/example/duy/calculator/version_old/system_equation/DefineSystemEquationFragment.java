package com.example.duy.calculator.version_old.system_equation;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
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

import com.example.duy.calculator.R;
import com.example.duy.calculator.item_math_type.SystemEquationItem;
import com.example.duy.calculator.math_eval.BigEvaluator;
import com.example.duy.calculator.math_eval.LogicEvaluator;
import com.example.duy.calculator.version_new_error.AbstractFragment;
import com.example.duy.calculator.view.ResizingEditText;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

import java.util.Random;
import java.util.regex.Pattern;

import io.github.kexanie.library.MathView;

import static android.view.View.GONE;

public class DefineSystemEquationFragment extends AbstractFragment implements View.OnClickListener {
    private static final String TAG = DefineSystemEquationFragment.class.getName();
    private static final String STARTED = DefineSystemEquationFragment.class.getSimpleName() + "started";
    char[] defaultVariable = "xyztuvabcdefgh".toCharArray();
    private RelativeLayout mContainer;
    private int numOfVariable = 1;
    private boolean isDebug = true;
    private Random random = new Random(231378L);
    private Spinner mSpinnerVariable;
    private ProgressBar mProgressBar;
    private MathView mMathView;
    private Button btnSolve;
    private Button btnClear;
    private EditText editVar;

    @Override
    protected View getView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.define_system_equation, container, false);
    }

    @Override
    protected void onChangeModeFraction() {

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

        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_selectable_list_item, s);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerVariable.setAdapter(mAdapter);
        mSpinnerVariable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int old = numOfVariable;
                numOfVariable = Integer.parseInt(mSpinnerVariable.getSelectedItem().toString());
                new CreateMatrixTask(old, old + 1, numOfVariable, numOfVariable + 1, mContainer).execute();
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
                clear(numOfVariable, numOfVariable + 1);
                break;
        }
    }

    private void clear(int row, int col) {
        @IdRes int index = 1;
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
        @IdRes int index = 1;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                EditText editText = (EditText) mContainer.findViewById(index);
                String s = editText.getText().toString();
                if (s.isEmpty()) s = "0";
                index++;
                res[i][j] = s;
            }
        }
        return res;
    }

    public void setMatrix(String[][] matrix, int row, int col) {
//        @IdRes int index = 1;
//        for (int i = 0; i < row; i++) {
//            for (int j = 0; j < col; j++) {
//                EditText editText = (EditText) mContainer.findViewById(index);
//                editText.setText(matrix[i][j]);
//                index++;
//            }
//        }
    }


    public void doEval() {
        String[][] arr = getMatrix(numOfVariable, numOfVariable + 1);
        String[] var = editVar.getText().toString().split(Pattern.quote(","));

        if (var.length != numOfVariable) {
            editVar.requestFocus();
            editVar.setError("Number variable is " + numOfVariable
                    + ". But current number variable is " + var.length);
            Toast.makeText(mContext, "Number variable is " + numOfVariable
                    + ". But current number variable is " + var.length, Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i < numOfVariable; i++) {
            var[i] = String.valueOf(defaultVariable[i]);
        }
        SystemEquationItem item = new
                SystemEquationItem(numOfVariable, numOfVariable + 1, arr, var);
        new TaskSolveSystemEquation().execute(item);
    }

    private void showHelp() {
        final SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
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
            @IdRes int index = 1;
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
            ResizingEditText editText = new ResizingEditText(mContext);
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
            for (int i = 0; i < numOfVariable; i++) {
                s += defaultVariable[i];
                if (i != numOfVariable - 1) s += ",";
            }
            editVar.setText(s);
        }
    }

    /**
     * clickSolveEquation system equation
     */
    private class TaskSolveSystemEquation
            extends AsyncTask<SystemEquationItem, Void, String> {
        @Override
        protected String doInBackground(SystemEquationItem... params) {
            String input = params[0].getInput();
            Log.d(TAG, "doInBackground: " + input);

            if (params[0].isError(BigEvaluator.getInstance(getActivity()))) {
                Log.e(TAG, "doInBackground: input error");
                return params[0].getError(BigEvaluator.getInstance(getActivity()), mContext);
            }

            final String[] res = {getString(R.string.error)};
            BigEvaluator.getInstance(getActivity()).solveSystemEquation(input,
                    new LogicEvaluator.EvaluateCallback() {
                        @Override
                        public void onEvaluate(String expr, String result, int errorResourceId) {
                            if (errorResourceId == LogicEvaluator.RESULT_OK)
                                res[0] = result;
                        }
                    });
            Log.d(TAG, "doInBackground: result = " + res[0]);
            return res[0];
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
            mMathView.setText("");
        }


        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            mProgressBar.setVisibility(GONE);
            mMathView.setText(aVoid);
        }
    }

}
