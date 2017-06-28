package com.example.duy.calculator.version_old.activities;

public class EvalResult {
    int resultId;
    String expr, result;

    public EvalResult(int resultId, String expr, String res) {
        this.resultId = resultId;
        this.expr = expr;
        this.result = res;
    }
}