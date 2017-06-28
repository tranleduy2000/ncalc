package com.example.duy.calculator.dev;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.duy.calculator.R;
import com.example.duy.calculator.math_eval.BigEvaluator;

/**
 * Created by DUy on 29-Dec-16.
 */

public class CommandActivity extends AppCompatActivity {
    private BigEvaluator evaluator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.command);
    }
}
