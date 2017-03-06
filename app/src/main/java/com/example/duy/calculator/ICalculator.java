package com.example.duy.calculator;

/**
 * Created by Duy on 3/7/2016
 */
public interface ICalculator {
    public void onResult(final String result);

    public void onError(final String errorResourceId);

    public void onDelete();

    public void onClear();

    public void onEqual();
}
