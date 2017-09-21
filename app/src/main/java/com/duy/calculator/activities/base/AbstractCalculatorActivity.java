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

package com.duy.calculator.activities.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.duy.calculator.tokenizer.Tokenizer;

/**
 * abstract app
 * <p/>
 * Created by Duy on 19/7/2016
 */
public abstract class AbstractCalculatorActivity extends AbstractNavDrawerActionBarActivity implements ICalculator {
    public Tokenizer mTokenizer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTokenizer = new Tokenizer();
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
