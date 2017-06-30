package com.example.duy.calculator.item_math_type;

public class ItemResult {
    public int mResultState;
    public String mExpression, mResult;
    private IExprInput mInput;
    private int mType;
    private String texFraction, texNumeric;

    public ItemResult(int mResultState, String mExpression, String res) {
        this.mResultState = mResultState;
        this.mExpression = mExpression;
        this.mResult = res;
    }

    public ItemResult(String mExpression, String res, int mResultState) {
        this.mResultState = mResultState;
        this.mExpression = mExpression;
        this.mResult = res;
    }

    public ItemResult(IExprInput mInput, String mExpression, String mResult, int mResultState) {
        this.mInput = mInput;
        this.mExpression = mExpression;
        this.mResult = mResult;
        this.mResultState = mResultState;
    }

    @Override
    public String toString() {
        return this.mExpression + " = " + mResult + " | " + mResultState;
    }

    public IExprInput getInput() {
        return mInput;
    }

    public void setInput(IExprInput mInput) {
        this.mInput = mInput;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        this.mType = type;
    }

    public String getTexFraction() {
        return texFraction;
    }

    public void setTexFraction(String texFraction) {
        this.texFraction = texFraction;
    }

    public String getTexNumeric() {
        return texNumeric;
    }

    public void setTexNumeric(String texNumeric) {
        this.texNumeric = texNumeric;
    }
}