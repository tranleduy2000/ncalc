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

package com.duy.calculator.symja.models;

public class ItemResult {
    public String mExpression, mResult;
    private ExprInput mInput;
    private int mType;
    private String texFraction, texNumeric;

    public ItemResult(int mResultState, String mExpression, String res) {
        this.mExpression = mExpression;
        this.mResult = res;
    }

    public ItemResult(String mExpression, String res) {
        this.mExpression = mExpression;
        this.mResult = res;
    }

    public ItemResult(ExprInput mInput, String mExpression, String mResult) {
        this.mInput = mInput;
        this.mExpression = mExpression;
        this.mResult = mResult;
    }

    @Override
    public String toString() {
        return this.mExpression + " = " + mResult ;
    }

    public ExprInput getInput() {
        return mInput;
    }

    public void setInput(ExprInput mInput) {
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