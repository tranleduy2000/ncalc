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

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.duy.calculator.R;

import java.util.Random;

import static com.duy.calculator.calc.BasicCalculatorActivity.TAG;

/**
 * Created by Duy on 18-May-17.
 */

public class FragmentMatrixInput extends Fragment implements View.OnClickListener {
    private static final int MAX_ROW = 100;
    private static final int MAX_COL = 100;
    private RelativeLayout mContainerA, mContainerB;
    private int currentRowA = 3;
    private int currentColumnA = 3;
    private int currentRowB = 3;
    private int currentColumnB = 3;
    private boolean isDebug = false;
    private Random random = new Random(231378L);
    private Spinner spinOperator;

    private View root;
    @Nullable
    private OnMatrixEvalListener listener;

    public static FragmentMatrixInput newInstance() {

        Bundle args = new Bundle();

        FragmentMatrixInput fragment = new FragmentMatrixInput();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnMatrixEvalListener) getActivity();
        } catch (Exception e) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_matrix_input, container, false);
        return root;
    }

    private View findViewById(int id) {
        return root.findViewById(id);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnCreA = (Button) findViewById(R.id.btn_create_a);
        btnCreA.setOnClickListener(this);

        Button btnCreB = (Button) findViewById(R.id.btn_create_b);
        btnCreB.setOnClickListener(this);

        mContainerA = (RelativeLayout) findViewById(R.id.rl_matrixA);
        mContainerB = (RelativeLayout) findViewById(R.id.rl_matrixB);


        initSpinOperator();
        Button btnSubmit = (Button) findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);

        createLayoutMatrix(3, 3, mContainerA);
        createLayoutMatrix(3, 3, mContainerB);
    }

    /**
     * show dialog with title and messenger
     *
     * @param title - title
     * @param msg   - messenger
     */
    protected void showDialog(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title).setMessage(msg);
        builder.setNegativeButton(this.getString(R.string.close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
    }

    private void createLayoutMatrix(int col, int row, ViewGroup viewGroup) {
        viewGroup.removeAllViews();
        @IdRes int index = 1;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                AppCompatEditText editText = new AppCompatEditText(getContext());
                editText.setHint("[" + i + "," + j + "]");
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                editText.setSingleLine(true);
                editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                editText.setId(index);
                if (isDebug) {
                    editText.setText(String.valueOf(random.nextInt(200) - 100));
                }
                if (j == 0) {
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                } else {
                    params.addRule(RelativeLayout.RIGHT_OF, index - 1);
                }

                if (i == 0) {
                    params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
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
        int col = currentColumnA;
        int row = currentRowA;
        @IdRes int index = 1;
        res.append("{");
        for (int i = 0; i < row; i++) {
            res.append("{");
            for (int j = 0; j < col; j++) {
                EditText editText = mContainerA.findViewById(index);
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
        int col = (currentColumnB);
        int row = (currentRowB);
        @IdRes int index = 1;
        for (int i = 0; i < row; i++) {
            res.append("{");
            for (int j = 0; j < col; j++) {
                EditText editText = mContainerB.findViewById(index);
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

    @SuppressWarnings("ConstantConditions")
    private void showDialogCreateMatrix(final boolean isMatrixA) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.fragment_matrix_dimens);
        builder.setMessage(R.string.msg_delete_matrix);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final EditText editRow = (EditText) dialog.findViewById(R.id.edit_row);
        final EditText editCol = (EditText) dialog.findViewById(R.id.edit_colums);
        Button btnCreate = (Button) dialog.findViewById(R.id.btn_create);
        Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (isMatrixA) {
                        currentRowA = Integer.parseInt(editRow.getText().toString());
                        currentColumnA = Integer.parseInt(editCol.getText().toString());
                        createLayoutMatrix(currentColumnA, currentRowA, mContainerA);
                    } else {
                        currentRowB = Integer.parseInt(editRow.getText().toString());
                        currentColumnB = Integer.parseInt(editCol.getText().toString());
                        createLayoutMatrix(currentColumnB, currentRowB, mContainerB);
                    }
                } catch (NumberFormatException e) {
                    showDialog(getResources().getString(R.string.error),
                            getResources().getString(R.string.error_input_num) + " " + e.getMessage());
                } catch (Exception e) {
                    showDialog(null, e.getMessage());
                }
                dialog.cancel();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
    }

    private void initSpinOperator() {
        spinOperator = (Spinner) findViewById(R.id.spin_matrix_op);
        String op[] = getResources().getStringArray(R.array.matrix_operator);
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, op);
        mAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinOperator.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_create_a:
                showDialogCreateMatrix(true);
                break;
            case R.id.btn_create_b:
                showDialogCreateMatrix(false);
                break;
            case R.id.btn_submit:
                prepareAndCalculate();
                break;
        }
    }

    /**
     * 1. evaluate two matrix with the "opt" operator
     * 2. parse result and show to layout
     */
    public void doEval(MatrixOpt opt) {
        switch (opt) {
            case ADD:
            case SUB:
            case MUL:
                try {
                    String expr;
                    String matrixA = getMatrixA();
                    String matrixB = getMatrixB();
                    if (opt == MatrixOpt.ADD) {
                        expr = matrixA + " + " + matrixB;
                    } else if (opt == MatrixOpt.MUL) {
                        expr = matrixA + " * " + matrixB;
                    } else {
                        expr = matrixA + " - " + matrixB;
                    }
                    if (listener != null) {
                        listener.doCalculate(expr);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showDialog(this.getString(R.string.error), e.getMessage());
                }
                break;
            case INVERSE_A:
                try {
                    String expr = getMatrixA();
                    expr = "Inverse(" + expr + ")";
                    if (listener != null) {
                        listener.doCalculate(expr);
                    }
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
                    if (listener != null) {
                        listener.doCalculate(expr);
                    }
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
                    if (listener != null) {
                        listener.doCalculate(expr);
                    }
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
                    if (listener != null) {
                        listener.doCalculate(expr);
                    }
                } catch (NumberFormatException e) {
                    showDialog(getResources().getString(R.string.error), getResources().getString(R.string.error_input_num) + " " + e.getMessage());
                } catch (Exception e) {
                    showDialog(null, e.getMessage());
                }
                break;
        }

    }

    private void prepareAndCalculate() {
        String s = spinOperator.getSelectedItem().toString();
        Log.w(TAG, s);
        if (s.equals(getString(R.string.matrix_op_add))) {
            doEval(MatrixOpt.ADD);
        }
        if (s.equals(getString(R.string.matrix_op_sub))) {
            doEval(MatrixOpt.SUB);
        }
        if (s.equals(getString(R.string.matrix_op_mul))) {
            doEval(MatrixOpt.MUL);
        }
        if (s.equals(getString(R.string.matrix_op_inverse_a))) {
            doEval(MatrixOpt.INVERSE_A);
        }
        if (s.equals(getString(R.string.matrix_op_inverse_b))) {
            doEval(MatrixOpt.INVERSE_B);
        }
        if (s.equals(getString(R.string.matrix_op_tranpose_a))) {
            doEval(MatrixOpt.TRANSOPE_A);
        }
        if (s.equals(getString(R.string.matrix_op_tranpose_b))) {
            doEval(MatrixOpt.TRANSOPE_B);
        }
    }

    public interface OnMatrixEvalListener {
        void doCalculate(String matrix1, String matrix2, MatrixOpt opt);

        void doCalculate(String expression);
    }
}
