package com.example.duy.calculator;

/**
 * Created by Duy on 3/7/2016
 */
public interface ICalculator {
    void onResult(final String result);

    void onError(final String errorResourceId);

    void onDelete();

    void onClear();

    void onEqual();

}
