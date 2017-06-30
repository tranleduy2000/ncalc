/*
 * Copyright (c) 2017 by Tran Le Duy
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

package com.duy.calc.casio.calculator.listener.setvalue;

import android.view.View;

import com.example.duy.calculator.R;
import com.duy.calc.casio.calculator.CalculatorContract;
import com.duy.calc.casio.calculator.listener.compute.ComputeListener;
import com.duy.calc.casio.evaluator.thread.BaseThread;
import com.duy.calc.casio.token.StringToken;
import com.duy.calc.casio.token.Token;
import com.duy.calc.casio.token.VariableToken;
import com.duy.calc.casio.token.factory.VariableFactory;
import com.google.common.collect.Lists;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by Duy on 28-Jun-17.
 */

public class SetValueHandler implements View.OnClickListener {
    private ComputeListener mParentListener;
    private ArrayList<Token> primaryExpression;

    private Iterator<VariableToken> mVariables;

    private SetValueListener mSetValueListener = null;
    private CalculatorContract.Presenter mPresenter;
    private CalculatorContract.Display mDisplay;
    private VariableToken next = null;

    private boolean isDisplayHint = false;

    public SetValueHandler(ComputeListener listener) {
        this.mParentListener = listener;
        this.mPresenter = listener.getPresenter();
        this.mDisplay = listener.getDisplay();
    }

    @Override
    public void onClick(View view) {
        if (isDisplayHint) {
            clearScreen();
            isDisplayHint = false;
        }
        switch (view.getId()) {
            case R.id.btn_calc:
            case R.id.btn_mode:
            case R.id.btn_setting:
            case R.id.btn_s_to_d:
            case R.id.btn_rcl: {
                mParentListener.getDisplay().showToast("Unsupported operation");
                break;
            }
            default:
                if (mSetValueListener != null) {
                    mSetValueListener.onClick(view);
                }
                break;
        }
    }

    private void clearScreen() {
        mDisplay.displayInput(new ArrayList<Token>());
        mDisplay.displayOutput(new ArrayList<Token>());
        mDisplay.reset();
    }

    void clickClear() {
        mParentListener.restoreDefaultDisplay();
    }

    void clickEqual() {
        BaseThread.ResultCallback resultCallback = new BaseThread.ResultCallback() {
            @Override
            public void onSuccess(ArrayList<Token> result) {
                setValue(next, result);
                nextVariable();
            }


            @Override
            public void onError(Exception e) {
                mPresenter.handleExceptions(e);
                mDisplay.hideProcessBar();
            }
        };
        if (!mSetValueListener.getExpression().isEmpty()) {
            mPresenter.doCalculate(mSetValueListener.getExpression(), resultCallback);
        } else {
            nextVariable();
        }
    }

    private void nextVariable() {
        mSetValueListener.clearExpr();
        if (!mVariables.hasNext()) {
            mParentListener.getPresenter().getKeyboard().setKeyboardListener(mParentListener);
            mParentListener.restoreDefaultDisplay();
            mParentListener.clickEquals();
        } else {
            displayHint();
        }
    }

    private void displayHint() {
        next = mVariables.next();
        mDisplay.reset();
        mDisplay.displayInput(Lists.<Token>newArrayList(new StringToken(next.getSymbol() + "=?")));
        mDisplay.displayOutput(next.getValue());
        isDisplayHint = true;
    }

    private void setValue(VariableToken var, ArrayList<Token> result) {
        switch (var.getType()) {
            case VariableToken.A:
                VariableFactory.A_Value = result;
                break;
            case VariableToken.B:
                VariableFactory.B_Value = result;
                break;
            case VariableToken.C:
                VariableFactory.C_Value = result;
                break;
            case VariableToken.D:
                VariableFactory.D_Value = result;
                break;
            case VariableToken.E:
                VariableFactory.E_Value = result;
                break;
            case VariableToken.F:
                VariableFactory.F_Value = result;
                break;
            case VariableToken.X:
                VariableFactory.X_Value = result;
                break;
            case VariableToken.Y:
                VariableFactory.Y_Value = result;
                break;
            case VariableToken.M:
                VariableFactory.Y_Value = result;
                break;
            default:
                throw new InvalidParameterException("Can not find var " + var);
        }
    }

    public void setValueFor(ArrayList<Token> expression) {
        this.primaryExpression = expression;
        initializer(expression);
    }

    private void initializer(ArrayList<Token> expression) {
        //filter variable, store its in a set
        HashSet<VariableToken> variables = new HashSet<>();
        for (Token token : expression)
            if (token instanceof VariableToken) variables.add((VariableToken) token);
        mVariables = variables.iterator();

        //has empty variable
        if (!mVariables.hasNext()) {
            mParentListener.restoreDefaultDisplay();
            mParentListener.clickEquals();
            return;
        }

        mSetValueListener = new SetValueListener(this);
        mSetValueListener.setDisplay(mParentListener.getDisplay());
        mSetValueListener.setExpression(new ArrayList<Token>());
        mSetValueListener.setPresenter(mParentListener.getPresenter());

        mParentListener.getDisplay().displayOutput(new ArrayList<Token>());
        mParentListener.getDisplay().reset();
        mParentListener.getPresenter().getKeyboard().setKeyboardListener(mSetValueListener);

        displayHint();
    }
}
