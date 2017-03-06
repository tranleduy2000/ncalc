package com.example.duy.calculator.version_old.converter.utils;

/**
 * Created by tranleduy on 27-May-16.
 */
public interface Strategy {
    public String getUnitDefault();
    public double Convert(String from, String to, double input);
}
