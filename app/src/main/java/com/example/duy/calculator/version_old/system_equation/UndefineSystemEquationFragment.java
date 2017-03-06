package com.example.duy.calculator.version_old.system_equation;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
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

import com.example.duy.calculator.CalcApplication;
import com.example.duy.calculator.R;
import com.example.duy.calculator.math_eval.BigEvaluator;
import com.example.duy.calculator.math_eval.Constants;
import com.example.duy.calculator.math_eval.LogicEvaluator;
import com.example.duy.calculator.utils.ConfigApp;
import com.example.duy.calculator.version_new_error.AbstractFragment;
import com.example.duy.calculator.view.ResizingEditText;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

import java.util.ArrayList;

import io.github.kexanie.library.MathView;

public class UndefineSystemEquationFragment extends AbstractFragment {
    private static final String STARTED = "UndefineSystemEquationFragment";
    private static final String TAG = UndefineSystemEquationFragment.class.getSimpleName();
    private ProgressBar progressBar;
    private SharedPreferences preferences;
    private int mCountView = 0;
    private LinearLayout mContainer;
    private MathView mMathView;
    private Context context;
    private View.OnClickListener btnSolveClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mContainer.getChildCount() == 0) {
                Toast.makeText(context, R.string.not_input_equation, Toast.LENGTH_SHORT).show();
                return;
            }
            new TaskSolveSystemEquations((CalcApplication) getActivity().getApplicationContext()).execute();
            if (mContainer.getChildCount() > 0) {
                try {
                    hideKeyboard(mContainer.getChildAt(0));
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
        }
    };
    private EditText editParams;
    private View.OnClickListener clearClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            mContainer.removeAllViews();
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        editParams = (EditText) findViewById(R.id.edit_params);

        findViewById(R.id.btn_add).setOnClickListener(btnAddClick);
        findViewById(R.id.btn_solve).setOnClickListener(btnSolveClick);
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
//            showHelp();
        }

        addParams("2x - y = 2");
        addParams("3x + 2y = 0");
    }


    private void showHelp() {
        final SharedPreferences.Editor editor = preferences.edit();

        View btnAdd = findViewById(R.id.btn_add);
        View btnSolve = findViewById(R.id.btn_solve);
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
                new TaskSolveSystemEquations((CalcApplication) getActivity().getApplicationContext()).execute();
            }

            @Override
            public void onSequenceCanceled(TapTarget lastTarget) {
                new TaskSolveSystemEquations((CalcApplication) getActivity().getApplicationContext()).execute();
            }
        });
//        addParams("2x - y = 2");
//        addParams("3x + 2y = 0");
        sequence.start();
    }


    public class TaskSolveSystemEquations extends AsyncTask<Void, Void, String> {
        private ArrayList<String> arrayList = new ArrayList<>();
        private StringBuilder equation = new StringBuilder();
        private boolean isOk = true;

        public TaskSolveSystemEquations(CalcApplication context) {
            for (int i = 0; i < mContainer.getChildCount(); i++) {
                ResizingEditText editText = (ResizingEditText) mContainer.getChildAt(i);
                String exp = editText.getCleanText();
                exp = mTokenizer.getNormalExpression(exp);
                exp = replaceEqualSymbol(exp);
                if (!exp.isEmpty()) arrayList.add(exp);
            }

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
        }

        /**
         * replace equal symbol to symja
         *
         * @param s
         * @return
         */
        private String replaceEqualSymbol(String s) {
            if (!s.contains("=")) s = s + "==0";
            if (!s.contains("==")) s = s.replace("=", "==");
            while (s.contains("===")) s = s.replace("===", "==");
            return s;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            if (!isOk) return "";
            String input = equation.toString();
            Log.d(TAG, input);
            if (BigEvaluator.getInstance(getActivity()).isSyntaxError(input)) return getString(R.string.error);
            final String[] res = {""};

            /**
             * evaluate with mResult as fraction
             */
            BigEvaluator.getInstance(getActivity()).setFraction(true);
            BigEvaluator.getInstance(getActivity()).evaluateWithResultAsTex(input, new LogicEvaluator.EvaluateCallback() {
                @Override
                public void onEvaluate(String expr, String result, int errorResourceId) {
                    if (errorResourceId == LogicEvaluator.RESULT_OK) res[0] = result;
                }
            });

            /**
             * evaluate with mResult as numeric
             */
            BigEvaluator.getInstance(getActivity()).setFraction(false);
            BigEvaluator.getInstance(getActivity()).evaluateWithResultAsTex(input, new LogicEvaluator.EvaluateCallback() {
                @Override
                public void onEvaluate(String expr, String result, int errorResourceId) {
                    if (errorResourceId == LogicEvaluator.RESULT_OK) {
                        res[0] += Constants.WEB_SEPARATOR;
                        res[0] += result;
                    }
                }
            });
            return res[0];
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            Log.d(TAG, "onPostExecute: " + aVoid);
            mMathView.setText(aVoid.replace("\\to", "="));
        }
    }

}
