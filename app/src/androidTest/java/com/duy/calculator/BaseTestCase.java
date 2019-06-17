package com.duy.calculator;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.duy.ncalc.calculator.BasicCalculatorActivity;

import org.junit.Rule;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public abstract class BaseTestCase {
    @Rule
    public ActivityTestRule<BasicCalculatorActivity> rule
            = new ActivityTestRule<>(BasicCalculatorActivity.class);
}
