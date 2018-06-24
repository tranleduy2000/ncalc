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

package com.duy.calculator.equations;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.duy.calculator.AbstractFragment;
import com.duy.calculator.R;
import com.duy.calculator.evaluator.Constants;
import com.duy.calculator.evaluator.EvaluateConfig;
import com.duy.calculator.evaluator.MathEvaluator;
import com.duy.calculator.evaluator.thread.BaseThread;
import com.duy.calculator.utils.ConfigApp;
import com.duy.calculator.view.ResizingEditText;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.common.collect.Lists;

import java.util.ArrayList;

import io.github.kexanie.library.MathView;

import static android.view.View.GONE;

public class UndefineSystemEquationFragment extends AbstractFragment implements View.OnClickListener {
    private static final String STARTED = "UndefineSystemEquationFragment";
    private static final String TAG = UndefineSystemEquationFragment.class.getSimpleName();
    private ProgressBar mProgressBar;
    private SharedPreferences preferences;
    private int mCountView = 0;
    private LinearLayout mContainer;
    private MathView mMathView;
    private Context context;

    private EditText editParams;
    private View.OnClickListener clearClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!(mCountView > 0)) {
                return;
            }
            mContainer.removeViewAt(mCountView - 1);
            mMathView.setText("$$Calculator N+$$");
            mCountView--;
            notifyParams();
        }
    };
    private View.OnClickListener btnAddClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            addParams();
        }
    };
    private View mContainerView;

    private void addParams() {
        addParams(null);
    }

    private void addParams(String text) {
        final ResizingEditText editText = new ResizingEditText(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(params);
        editText.setHint(getString(R.string.input_equation));
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    ResizingEditText v = (ResizingEditText) view;
                    String s = v.getText().toString();
                    if (!s.contains("=")) {
                        s = s + "=0";
                        v.setText(s);
                    }
                } else {
                    EditText v = (EditText) view;
                    v.selectAll();
                }
            }
        });
        mCountView++;
        editText.setId(mCountView);
        mContainer.addView(editText);
        editText.requestFocus();
        editText.setText(text);

        final TranslateAnimation animation = new TranslateAnimation(editText.getWidth(), 0f, 0f, 0f);
        editText.post(new Runnable() {
            @Override
            public void run() {
                editText.startAnimation(animation);
            }
        });
        notifyParams();
    }

    private void hideKeyboard(View et) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    private void notifyParams() {
        switch (mCountView) {
            case 1:
                editParams.setText("x");
                break;
            case 2:
                editParams.setText("x, y");
                break;
            case 3:
                editParams.setText("x, y, z");
                break;
            default:
                Toast.makeText(context, "Nhập các ẩn của hệ phương trình", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    protected View getView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.undefine_system_equation, container, false);
    }

    @Override
    protected void onChangeModeFraction() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContainer = (LinearLayout) findViewById(R.id.container);
        mMathView = (MathView) findViewById(R.id.math_view);
        //disable horizontal scroll when use vertical scroll.
        mMathView.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ListView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ListView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle HorizontalScrollView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar2);
        editParams = (EditText) findViewById(R.id.edit_params);

        findViewById(R.id.btn_add).setOnClickListener(btnAddClick);
        findViewById(R.id.btn_solve).setOnClickListener(this);
        findViewById(R.id.btn_clear).setOnClickListener(clearClick);

        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isStarted = preferences.getBoolean(STARTED, false);

        findViewById(R.id.btn_help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHelp();
            }
        });
        if ((!isStarted) || ConfigApp.DEBUG) {
//            clickHelp();
        }

        addParams("2x - y = 2");
        addParams("3x + 2y = 0");
    }

    private void showHelp() {
        final SharedPreferences.Editor editor = preferences.edit();

        View btnAdd = findViewById(R.id.btn_add);
        final View btnSolve = findViewById(R.id.btn_solve);
        View btnClear = findViewById(R.id.btn_clear);

        //if is not start
        TapTarget target = TapTarget.forView(btnAdd, getString(R.string.add_equation));
        target.drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark);

        TapTarget target2 = TapTarget.forView(btnClear, getString(R.string.remove_equation));
        target2.drawShadow(true)
                .cancelable(true)
                .targetCircleColor(R.color.colorAccent)
                .transparentTarget(true)
                .outerCircleColor(R.color.colorPrimary)
                .dimColor(R.color.colorPrimaryDark);

        TapTarget target3 = TapTarget.forView(editParams, getString(R.string.enter_params));
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
        sequence.targets(target, target2, target3, target4);
        sequence.listener(new TapTargetSequence.Listener() {
            @Override
            public void onSequenceFinish() {
                editor.putBoolean(STARTED, true);
                editor.apply();
                onClick(btnSolve);
            }

            @Override
            public void onSequenceCanceled(TapTarget lastTarget) {
                onClick(btnSolve);
            }
        });
//        addParams("2x - y = 2");
//        addParams("3x + 2y = 0");
        sequence.start();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_solve) {
            if (mContainer.getChildCount() == 0) {
                Toast.makeText(context, R.string.not_input_equation, Toast.LENGTH_SHORT).show();
                return;
            }
            String expression = createExpression();
            TaskSolveSystemEquation task = new TaskSolveSystemEquation(new BaseThread.ResultCallback() {
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
            task.execute(expression);


            if (mContainer.getChildCount() > 0) {
                try {
                    hideKeyboard(mContainer.getChildAt(0));
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
        }
    }


    private String createExpression() {
        ArrayList<String> arrayList = new ArrayList<>();

        for (int i = 0; i < mContainer.getChildCount(); i++) {
            ResizingEditText editText = (ResizingEditText) mContainer.getChildAt(i);
            String exp = editText.getCleanText();
            exp = mTokenizer.getNormalExpression(exp);
            exp = replaceEqualSymbol(exp);
            if (!exp.isEmpty()) arrayList.add(exp);
        }

        StringBuilder equation = new StringBuilder();
        equation.append("Solve({");
        for (int i = 0; i < arrayList.size(); i++) {
            String s = arrayList.get(i);
            s = replaceEqualSymbol(s);
            if (i != arrayList.size() - 1) {
                equation.append(s);
                equation.append(",");
            } else {
                equation.append(s);
            }
        }
        equation.append("}");
        equation.append(",");
        equation.append("{").append(editParams.getText().toString()).append("}");
        equation.append(")");
        return equation.toString();
    }

    private String replaceEqualSymbol(String s) {
        if (!s.contains("=")) s = s + "==0";
        if (!s.contains("==")) s = s.replace("=", "==");
        while (s.contains("===")) s = s.replace("===", "==");
        return s;
    }

    /**
     * clickSolveEquation system equation
     */
    private class TaskSolveSystemEquation extends AsyncTask<String, Void, ArrayList<String>> {
        private BaseThread.ResultCallback resultCallback;
        private Context context;
        private Exception exception = null;

        public TaskSolveSystemEquation(BaseThread.ResultCallback resultCallback, Context context) {
            this.resultCallback = resultCallback;
            this.context = context;
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {

            String input = params[0];

            if (MathEvaluator.getInstance().isSyntaxError(input)) {
                exception = MathEvaluator.getError(input);
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
