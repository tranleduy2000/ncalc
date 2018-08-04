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

package com.duy.calculator.activities.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.duy.calculator.symja.tokenizer.ExpressionTokenizer;

/**
 * abstract app
 * <p/>
 * Created by Duy on 19/7/2016
 */
public abstract class AbstractCalculatorActivity extends NavDrawerActivity implements ICalculator {
    public ExpressionTokenizer mTokenizer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTokenizer = new ExpressionTokenizer();
    }


    /**
     * insert text to display - not clear screen
     * <p/>
     * use only for  calculator (base, science, complex)
     *
     * @param s - text
     */
    public abstract void insertText(String s);

    /**
     * insert operator to display
     * not clear display
     *
     * @param s - operator
     */
    public abstract void insertOperator(String s);

    /**
     * get text input display
     * <p/>
     * use only for  calculator (base, science, complex)
     *
     * @return - string text input
     */
    public abstract String getTextClean();


    /**
     * clear screen
     * <p/>
     * set text display - use only for  calculator (base, science, complex)
     * <p/>
     * set text for input edit text (eval)
     * <p/>
     * set text for matrix (matrix cal)
     * <p/>
     * set text for linear eval
     *
     * @param text - string input
     */
    public abstract void setTextDisplay(String text);


}
