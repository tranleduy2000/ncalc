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

package com.duy.calc.casio.calculator.listener.compute;

import android.support.annotation.Nullable;
import android.view.View;

import com.example.duy.calculator.R;
import com.duy.calc.casio.calculator.listener.DisplayMode;
import com.duy.calc.casio.calculator.listener.setvalue.SetValueHandler;
import com.duy.calc.casio.evaluator.EvaluateConfig;
import com.duy.calc.casio.evaluator.MathEvaluator;
import com.duy.calc.casio.evaluator.thread.BaseThread;
import com.duy.calc.casio.keyboardlistener.BasicListener;
import com.duy.calc.casio.mutilbutton.ButtonMode;
import com.duy.calc.casio.mutilbutton.MultiButton;
import com.duy.calc.casio.token.Token;
import com.duy.calc.casio.token.factory.MatrixFactory;
import com.duy.calc.casio.token.factory.VariableFactory;
import com.google.common.collect.Lists;

import java.util.ArrayList;

public class ComputeListener extends BasicListener {

    private static final String FILENAME = "history_advanced";
    private static final String TAG = "AdvancedListener";


    static {
        filename = "history_advanced";
    }


    private int mCursorIndex = 0;
    private int mRealCursorIndex = 0;


    private HyperbolicHandler hyperbolicHandler;
    private SelectModeHandler selectModeHandler;
    @Nullable
    private SetValueHandler mSetValueHandler;

    public ComputeListener() {
        hyperbolicHandler = new HyperbolicHandler(this);
        selectModeHandler = new SelectModeHandler(this);
    }

    /**
     * When a Button has been clicked, calls the appropriate method.
     *
     * @param v The Button that has been clicked
     */
    @Override
    public boolean onClick(View v) {
        switch (mDisplayMode) {
            case HYPERBOLIC:
                hyperbolicHandler.onClick(v);
                break;
            case SELECT_MODE:
                selectModeHandler.onClick(v);
                break;
            case SET_VALUE:
                if (mSetValueHandler != null) {
                    mSetValueHandler.onClick(v);
                }
                break;
            default: {
                switch (v.getId()) {
                    case R.id.btn_equals:
                        clickEquals();
                        return false;
                    case R.id.alpha_button:
                        clickAlpha();
                        return false;
                    case R.id.shift_button:
                        clickShift();
                        return false;
                    case R.id.btn_frac:
                        clickFraction();
                        return false;
                    case R.id.btn_mode:
                        if (clickMode()) return false;
                        break;
                    default:
                        if (v instanceof MultiButton) {
                            boolean handled = ((MultiButton) v).onClick();
                            if (!handled) {
                                resetButtonState();
                                restoreDefaultDisplay();
                            }
                        } else {
                            super.onClick(v);
                        }
                }
                break;
            }
        }
        return false;
    }

    private boolean clickMode() {
        if (mDisplayMode == DisplayMode.NORMAL) {
            saveDisplay();
            Token token = MatrixFactory.createMatrixMode();
            mDisplay.setShowCursor(false);
            mDisplay.displayInput(Lists.newArrayList(token));
        } else {
            restoreDefaultDisplay();
            resetButtonState();
        }
        return false;
    }

    public void restoreDefaultDisplay() {
        if (!(mDisplayMode == DisplayMode.NORMAL)) {
            mDisplayMode = DisplayMode.NORMAL;
            mDisplay.displayInput(expression);
            mDisplay.setCursorIndex(mCursorIndex);
            mDisplay.setRealCursorIndex(mRealCursorIndex);
            mDisplay.setShowCursor(true);
        }
    }

    private void resetButtonState() {
        if (!(buttonMode == ButtonMode.NORMAL)) {
            buttonMode = ButtonMode.NORMAL;
            mPresenter.getKeyboard().updateMultiButtons(buttonMode);
        }
        recall = false;
        memory = false;
    }

    @Override
    public void clickCalc() {
        mDisplayMode = DisplayMode.SET_VALUE;
        saveDisplay();
        mSetValueHandler = new SetValueHandler(this);
        mSetValueHandler.setValueFor(expression);
    }

    /**
     * When the user presses the hyp button. Switches the state of the boolean variable hyperbolic
     */
    public void clickHyp() {
        if (mDisplayMode == DisplayMode.HYPERBOLIC) {
            restoreDefaultDisplay();
        } else {
            mDisplayMode = DisplayMode.HYPERBOLIC;
            saveDisplay();
            Token matrixHyp = MatrixFactory.createMatrixHyp();
            ArrayList<Token> tokens = new ArrayList<>();
            tokens.add(matrixHyp);
            mDisplay.setShowCursor(false);
            mDisplay.displayInput(tokens);
        }
    }

    private void saveDisplay() {
        this.mCursorIndex = mDisplay.getCursorIndex();
        this.mRealCursorIndex = mDisplay.getRealCursorIndex();
    }

    /**
     * When the user presses the equals Button.
     */
    public void clickEquals() {
        mPresenter.doCalculate(expression, new BaseThread.ResultCallback() {
            @Override
            public void onSuccess(ArrayList<Token> result) {
                mDisplay.displayOutput(result);
                mDisplay.scrollDown();
                mDisplay.hideProcessBar();

                VariableFactory.setAnsValue(result);
            }

            @Override
            public void onError(Exception e) {
                handleExceptions(e);
                mDisplay.hideProcessBar();
            }
        });
    }

    public void updateOutput() {
        mDisplay.displayOutput(new ArrayList<Token>()); //Clears output
        mDisplay.displayInput(expression);
    }

    public void clickDecFracMode() {
        EvaluateConfig config = mPresenter.getEvaluateConfig();
        config.setEvalMode(config.getEvaluateMode() == EvaluateConfig.FRACTION ?
                EvaluateConfig.DECIMAL : EvaluateConfig.FRACTION);
        clickEquals();
    }

    public void clickIntegrate() {
    }

    public void clickPrimeFactor() {
        try {
            ArrayList<Token> tokens = MathEvaluator.primeFactor(this.expression);
            mDisplay.displayOutput(tokens);
        } catch (Exception e) { //User did a mistake
            e.printStackTrace();
            handleExceptions(e);
        }
        mDisplay.scrollDown();
    }

    public void clickSemicolon() {
        // TODO: 26-Jun-17
    }

}