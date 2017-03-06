package com.example.duy.calculator.version_old.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.duy.calculator.R;
import com.example.duy.calculator.math_eval.BigEvaluator;
import com.example.duy.calculator.math_eval.LogicEvaluator;
import com.example.duy.calculator.version_old.activities.abstract_class.AbstractNavDrawerActionBarActivity;

import java.util.Random;

import io.github.kexanie.library.MathView;

public class MatrixCalculatorActivity extends AbstractNavDrawerActionBarActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, LogicEvaluator.EvaluateCallback {
    private static final int MAX_ROW = 100;
    private static final int MAX_COL = 100;
    private static final String TAG = MatrixCalculatorActivity.class.getName();
    private EditText editRowA, editColA, editRowB, editColB;
    private Button btnCreA, btnCreB, btnAdd, btnMinus, btnMul;
    private Button btnSubmit;
    private RelativeLayout mContainerA, mContainerB;
    private String matrixA, matrixB;
    private MathView mResult;
    private int currentRowA = 0;
    private int currentColumnA = 0;
    private int currentRowB = 0;
    private int currentColumnB = 0;
    private boolean isDebug = false;
    private Random random = new Random(231378L);
    private Spinner spinOperator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_matrix_calculator);
        editRowA = (EditText) findViewById(R.id.edit_row_a);
        editColA = (EditText) findViewById(R.id.edit_colums_a);
        btnCreA = (Button) findViewById(R.id.button_create_mA);
        btnCreA.setOnClickListener(this);

        editRowB = (EditText) findViewById(R.id.edit_row_b);
        editColB = (EditText) findViewById(R.id.edit_colums_b);
        btnCreB = (Button) findViewById(R.id.button_create_mB);
        btnCreB.setOnClickListener(this);

        mContainerA = (RelativeLayout) findViewById(R.id.rl_matrixA);
        mContainerB = (RelativeLayout) findViewById(R.id.rl_matrixB);

        mResult = (MathView) findViewById(R.id.rl_matrixC);
        mResult.setText("");
        initSpinOperator();
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);

        editColA.setText("3");
        editColB.setText("3");
        editRowA.setText("3");
        editRowB.setText("3");
        createLayoutMatrix(3, 3, mContainerA);
        createLayoutMatrix(3, 3, mContainerB);

    }

    private void initSpinOperator() {
        spinOperator = (Spinner) findViewById(R.id.spin_matrix_op);
        String op[] = getResources().getStringArray(R.array.matrix_operator);
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, op);
        mAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinOperator.setAdapter(mAdapter);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.button_create_mA:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.msg_delete_matrix);
                builder.setNegativeButton(this.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            currentRowA = Integer.parseInt(editRowA.getText().toString());
                            currentColumnA = Integer.parseInt(editColA.getText().toString());
                            createLayoutMatrix(currentColumnA, currentRowA, mContainerA);
                        } catch (NumberFormatException e) {
                            showDialog(getResources().getString(R.string.error), getResources().getString(R.string.error_input_num) + " " + e.getMessage());
                        } catch (Exception e) {
                            showDialog(null, e.getMessage());
                        }
                    }
                });
                builder.setPositiveButton(this.getString(R.string.close), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.create().show();
                break;
            case R.id.button_create_mB:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setMessage(R.string.msg_delete_matrix);
                builder2.setNegativeButton(this.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            currentRowB = Integer.parseInt(editRowB.getText().toString());
                            currentColumnB = Integer.parseInt(editColB.getText().toString());
                            createLayoutMatrix(currentColumnB, currentRowB, mContainerB);
                        } catch (NumberFormatException e) {
                            showDialog(getResources().getString(R.string.error),
                                    getResources().getString(R.string.error_input_num) + " " + e.getMessage());
                        } catch (Exception e) {
                            showDialog(null, e.getMessage());
                        }
                    }
                });
                builder2.setPositiveButton(this.getString(R.string.close), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder2.create().show();
                break;
            case R.id.btn_submit:
                String s = spinOperator.getSelectedItem().toString();
                Log.w(TAG, s);
                if (s.equals(getString(R.string.matrix_op_add))) {
                    doEval(MatrixOpt.ADD);
                    break;
                }
                if (s.equals(getString(R.string.matrix_op_sub))) {
                    doEval(MatrixOpt.SUB);
                    break;
                }
                if (s.equals(getString(R.string.matrix_op_mul))) {
                    doEval(MatrixOpt.MUL);
                    break;
                }
                if (s.equals(getString(R.string.matrix_op_inverse_a))) {
                    doEval(MatrixOpt.INVERSE_A);
                    break;
                }
                if (s.equals(getString(R.string.matrix_op_inverse_b))) {
                    doEval(MatrixOpt.INVERSE_B);
                    break;
                }
                if (s.equals(getString(R.string.matrix_op_tranpose_a))) {
                    doEval(MatrixOpt.TRANSOPE_A);
                    break;
                }
                if (s.equals(getString(R.string.matrix_op_tranpose_b))) {
                    doEval(MatrixOpt.TRANSOPE_B);
                    break;
                }
                break;
        }
    }

    private void createLayoutMatrix(int col, int row, ViewGroup viewGroup) {
        viewGroup.removeAllViews();
        @IdRes int index = 1;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                AppCompatEditText editText = new AppCompatEditText(this);
                editText.setHint("[" + i + "," + j + "]");
                RelativeLayout.LayoutParams params =
                        new RelativeLayout.LayoutParams(
                                100,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                        );
                editText.setSingleLine(true);
                editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                editText.setId(index);
                if (isDebug) {
                    editText.setText(String.valueOf(random.nextInt(200) - 100));
                }
                if (j == 0) {
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
                            RelativeLayout.TRUE);
                } else {
                    params.addRule(RelativeLayout.RIGHT_OF, index - 1);
                }

                if (i == 0) {
                    params.addRule(RelativeLayout.ALIGN_PARENT_TOP,
                            RelativeLayout.TRUE);
                } else {
                    params.addRule(RelativeLayout.BELOW, index - col);
                }
                editText.setLayoutParams(params);
                viewGroup.addView(editText);
                index++;
            }
        }

    }

    public String getMatrixA() {
        StringBuilder res = new StringBuilder();
        int col = Integer.parseInt(editColA.getText().toString());
        int row = Integer.parseInt(editRowA.getText().toString());
        @IdRes int index = 1;
        res.append("{");
        for (int i = 0; i < row; i++) {
            res.append("{");
            for (int j = 0; j < col; j++) {
                EditText editText = (EditText) mContainerA.findViewById(index);
                String s = editText.getText().toString();
                if (s.isEmpty()) s = "0";
                index++;
                if (j == col - 1) res.append(s);
                else res.append(s).append(",");
            }
            if (!(i == row - 1))
                res.append("}").append(",");
            else res.append("}");
        }
        res.append("}");
        Log.d(TAG, "getMatrixA: " + res.toString());
        return res.toString();
    }

    public String getMatrixB() {
        StringBuilder res = new StringBuilder();
        res.append("{");
        int col = Integer.parseInt(editColB.getText().toString());
        int row = Integer.parseInt(editRowB.getText().toString());
        @IdRes int index = 1;
        for (int i = 0; i < row; i++) {
            res.append("{");
            for (int j = 0; j < col; j++) {
                EditText editText = (EditText) mContainerB.findViewById(index);
                String s = editText.getText().toString();
                if (s.isEmpty()) s = "0";
                index++;
                if (j == col - 1) res.append(s);
                else res.append(s).append(",");
            }
            if (!(i == row - 1))
                res.append("}").append(",");
            else res.append("}");
        }
        res.append("}");

        Log.d(TAG, "getMatrixB: " + res.toString());
        return res.toString();
    }

    public void doEval(MatrixOpt opt) {
        switch (opt) {
            case ADD:
            case SUB:
            case MUL:
                try {
                    String expr;
                    matrixA = getMatrixA();
                    matrixB = getMatrixB();
                    if (opt == MatrixOpt.ADD) {
                        expr = matrixA + " + " + matrixB;
                    } else if (opt == MatrixOpt.MUL) {
                        expr = matrixA + " * " + matrixB;
                    } else {
                        expr = matrixA + " - " + matrixB;
                    }
                    Log.d(TAG, "doEval: " + expr);
                    BigEvaluator.getInstance(getApplicationContext()).evaluateWithResultAsTex(expr, this);
                } catch (Exception e) {
                    e.printStackTrace();
                    showDialog(this.getString(R.string.error), e.getMessage());
                }
                break;
            case INVERSE_A:
                try {
                    String expr = getMatrixA();
                    expr = "Inverse(" + expr + ")";
                    Log.d(TAG, "doEval: " + expr);
                    BigEvaluator.getInstance(getApplicationContext()).evaluateWithResultAsTex(expr, this);
                } catch (NumberFormatException e) {
                    showDialog(getResources().getString(R.string.error), getResources().getString(R.string.error_input_num) + " " + e.getMessage());
                } catch (Exception e) {
                    showDialog(null, e.getMessage());
                }
                break;
            case INVERSE_B:
                try {
                    String expr = getMatrixB();
                    expr = "Inverse(" + expr + ")";
                    Log.d(TAG, "doEval: " + expr);
                    BigEvaluator.getInstance(getApplicationContext()).evaluateWithResultAsTex(expr, this);
                } catch (NumberFormatException e) {
                    showDialog(getResources().getString(R.string.error), getResources().getString(R.string.error_input_num) + " " + e.getMessage());
                } catch (Exception e) {
                    showDialog(null, e.getMessage());
                }
                break;
            case TRANSOPE_A:
                try {
                    String expr = getMatrixA();
                    expr = "Transpose(" + expr + ")";
                    Log.d(TAG, "doEval: " + expr);
                    BigEvaluator.getInstance(getApplicationContext()).evaluateWithResultAsTex(expr, this);
                } catch (NumberFormatException e) {
                    showDialog(getResources().getString(R.string.error), getResources().getString(R.string.error_input_num) + " " + e.getMessage());
                } catch (Exception e) {
                    showDialog(null, e.getMessage());
                }
                break;
            case TRANSOPE_B:
                try {
                    String expr = getMatrixB();
                    expr = "Transpose(" + expr + ")";
                    BigEvaluator.getInstance(getApplicationContext()).evaluateWithResultAsTex(expr, this);
                    Log.d(TAG, "doEval: " + expr);
                } catch (NumberFormatException e) {
                    showDialog(getResources().getString(R.string.error), getResources().getString(R.string.error_input_num) + " " + e.getMessage());
                } catch (Exception e) {
                    showDialog(null, e.getMessage());
                }
                break;
        }

    }

    private void setText(String tex) {
        mResult.setText(tex);
    }

    @Override
    public void onEvaluate(String expr, String result, int resultId) {
        Log.d(TAG, "onEvaluate: " + expr + " = " + result);
        if (resultId == LogicEvaluator.RESULT_ERROR) {
            setText(getString(R.string.error));
        } else if (resultId == LogicEvaluator.RESULT_OK) {
            setText(result);
        } else if (resultId == LogicEvaluator.INPUT_EMPTY) {
            setText("$$NCALC+$$");
        }
    }


    protected void onChangeModeFraction() {

    }

    public enum MatrixOpt {
        ADD,
        SUB,
        MUL,
        INVERSE_A,
        INVERSE_B,
        TRANSOPE_A,
        TRANSOPE_B
    }
}
