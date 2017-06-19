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

package com.duy.calculator.item_math_type;

public class ItemResult {
    public int mResultState;
    public String mExpression, mResult;
    private AExprInput mInput;
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

    public ItemResult(AExprInput mInput, String mExpression, String mResult, int mResultState) {
        this.mInput = mInput;
        this.mExpression = mExpression;
        this.mResult = mResult;
        this.mResultState = mResultState;
    }

    @Override
    public String toString() {
        return this.mExpression + " = " + mResult + " | " + mResultState;
    }

    public AExprInput getInput() {
        return mInput;
    }

    public void setInput(AExprInput mInput) {
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