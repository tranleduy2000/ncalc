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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.duy.calculator.R;

import java.util.Random;

import static com.example.duy.calculator.activities.BasicCalculatorActivity.TAG;

/**
 * Created by Duy on 18-May-17.
 */

public class FragmentMatrixInput extends Fragment implements View.OnClickListener {
    private static final int MAX_ROW = 100;
    private static final int MAX_COL = 100;
    private EditText editRowA, editColA, editRowB, editColB;
    private RelativeLayout mContainerA, mContainerB;
    private int currentRowA = 0;
    private int currentColumnA = 0;
    private int currentRowB = 0;
    private int currentColumnB = 0;
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

        editRowA = (EditText) findViewById(R.id.edit_row_a);
        editColA = (EditText) findViewById(R.id.edit_colums_a);
        Button btnCreA = (Button) findViewById(R.id.button_create_mA);
        btnCreA.setOnClickListener(this);

        editRowB = (EditText) findViewById(R.id.edit_row_b);
        editColB = (EditText) findViewById(R.id.edit_colums_b);
        Button btnCreB = (Button) findViewById(R.id.button_create_mB);
        btnCreB.setOnClickListener(this);

        mContainerA = (RelativeLayout) findViewById(R.id.rl_matrixA);
        mContainerB = (RelativeLayout) findViewById(R.id.rl_matrixB);


        initSpinOperator();
        Button btnSubmit = (Button) findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);

        editColA.setText("3");
        editColB.setText("3");
        editRowA.setText("3");
        editRowB.setText("3");

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

    private void showDialogClearMatrixB() {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
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
    }

    private void createLayoutMatrix(int col, int row, ViewGroup viewGroup) {
        viewGroup.removeAllViews();
        @IdRes int index = 1;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                AppCompatEditText editText = new AppCompatEditText(getContext());
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

    private void showDialogClearMatrixA() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
    }

    private void initSpinOperator() {
        spinOperator = (Spinner) findViewById(R.id.spin_matrix_op);
        String op[] = getResources().getStringArray(R.array.matrix_operator);
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, op);
        mAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);


        //add handler for spinner select, if user change operator,
        // it will be pre calculate matrix
        spinOperator.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                prepareAndCalculate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinOperator.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.button_create_mA:
                showDialogClearMatrixA();
                break;
            case R.id.button_create_mB:
                showDialogClearMatrixB();
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
