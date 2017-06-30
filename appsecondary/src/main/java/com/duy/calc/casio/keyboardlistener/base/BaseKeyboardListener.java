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

package com.duy.calc.casio.keyboardlistener.base;

import android.view.View;

import com.duy.calc.casio.evaluator.base.BaseEvaluator;
import com.duy.calc.casio.evaluator.base.BaseModule;
import com.duy.calc.casio.keyboardlistener.BasicListener;
import com.duy.calc.casio.mutilbutton.MultiButton;
import com.duy.calc.casio.token.Token;
import com.duy.calc.casio.token.factory.DigitFactory;

import java.util.ArrayList;
import java.util.List;

public class BaseKeyboardListener extends BasicListener implements OnBaseChangeListener {

    private int currentBase = Base.DECIMAL;
    private BaseEvaluator baseEvaluator;

    /**
     * constructor
     */
    public BaseKeyboardListener() {
        //create new list expression for display
        this.expression = new ArrayList<>();
        baseEvaluator = new BaseEvaluator();
        BaseModule baseModule = new BaseModule(baseEvaluator);
    }

    @Override
    public void clickDecFracMode() {
        //don't required
    }

    @Override
    public void clickSemicolon() {
        //don't required
    }

    @Override
    public void clickShift() {
        //don't required
    }

    @Override
    public void clickEquals() {
        // TODO: 28-Jun-17
    }

    @Override
    public void clickHyp() {
        //click C
        int realCursorIndex = mDisplay.getRealCursorIndex();
        expression.add(realCursorIndex, DigitFactory.makeC());
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();

    }

    @Override
    public void clickIntegrate() {
        //don't required
    }

    @Override
    public void clickPrimeFactor() {
        //don't required
    }

    @Override
    public boolean onClick(View v) {
        if (v instanceof MultiButton) {
            boolean handled = ((MultiButton) v).onClick();
            if (handled) {
                return true;
            }
            mPresenter.getKeyboard().updateMultiButtons(buttonMode);
        } else {
            if (!super.onClick(v)) {
                mDisplay.showToast("Can not handle in BASE mode");
            }
            return false;
        }
        return false;
    }

    @Override
    public void clickHistory() {

    }

    @Override
    public void clickDecimal() {
        //don't required
    }

    @Override
    public void clickNegative() {
        int realCursorIndex = mDisplay.getRealCursorIndex();
        expression.add(realCursorIndex, DigitFactory.makeA());
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();

    }

    @Override
    public void clickPowerOfTen() {   //don't required
    }

    @Override
    public void clickLn() {
        //click oct
        changeBase(currentBase, Base.OCTAL, expression);
    }

    @Override
    public void clickLog_10() {
        //click BIN
        changeBase(currentBase, Base.OCTAL, expression);
    }

    @Override
    public void clickPower() {
        // click hex
        changeBase(currentBase, Base.HEX, expression);
    }

    @Override
    public void clickSurd() {
        //don't required
    }

    @Override
    public void clickSquare() {
        // click decimal
        changeBase(currentBase, Base.DECIMAL, expression);
    }

    @Override
    public void clickCube() {
        //don't required
    }

    @Override
    public void clickSqrt() {
        //don't required
    }

    @Override
    public void clickCbrt() {
        //don't required
    }

    @Override
    public void clickFraction() {   //don't required
    }

    @Override
    public void clickReciprocal() {   //don't required
    }

    @Override
    public void clickCombination() {   //don't required
    }

    @Override
    public void clickPermutation() {   //don't required
    }

    @Override
    public void click_e() {   //don't required
    }

    @Override
    public void clickPi() {   //don't required
    }

    @Override
    public void clickSin() {
        //click D
        int realCursorIndex = mDisplay.getRealCursorIndex();
        expression.add(realCursorIndex, DigitFactory.makeD());
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();

    }

    @Override
    public void clickASin() {   //don't required
    }

    @Override
    public void clickCos() {
        //click E
        int realCursorIndex = mDisplay.getRealCursorIndex();
        expression.add(realCursorIndex, DigitFactory.makeE());
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();

    }

    @Override
    public void clickACos() {   //don't required
    }

    @Override
    public void clickTan() {
        //click F
        int realCursorIndex = mDisplay.getRealCursorIndex();
        expression.add(realCursorIndex, DigitFactory.makeF());
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();

    }

    @Override
    public void clickATan() {   //don't required
    }

    @Override
    public void clickSinh() {   //don't required
    }

    @Override
    public void clickASinh() {   //don't required
    }

    @Override
    public void clickCosh() {   //don't required
    }

    @Override
    public void clickACosh() {   //don't required
    }

    @Override
    public void clickTanh() {   //don't required
    }

    @Override
    public void clickATanh() {   //don't required
    }

    @Override
    public void clickConst() {   //don't required
    }


    @Override
    public void clickMPlus() {
        // TODO: 28-Jun-17
    }

    @Override
    public void clickMMinus() {
        // TODO: 28-Jun-17
    }

    @Override
    public void clickPercent() {   //don't required
    }

    @Override
    public void clickCommaSign() {   //don't required
    }

    @Override
    public void clickAbs() {   //don't required
    }

    @Override
    public void clickDegree() {
        //click B
        int realCursorIndex = mDisplay.getRealCursorIndex();
        expression.add(realCursorIndex, DigitFactory.makeB());
        mDisplay.setCursorIndex(mDisplay.getCursorIndex() + 1);
        updateInput();
    }

    @Override
    public void clickBin() {
    }

    @Override
    public void clickOctal() {
    }

    @Override
    public void clickPowerOfE() {
        //OCT
        changeBase(currentBase, Base.OCTAL, expression);
    }


    @Override
    public void clickFactorial() {   //don't required
    }

    @Override
    public void clickENG() {   //don't required
    }

    @Override
    public void clickImplicit() {   //don't required
    }

    @Override
    public void clickComplex() {   //don't required
    }

    @Override
    public void clickBase() {
        // TODO: 28-Jun-17  OPERATOR
    }

    @Override
    public void clickMatrix() {   //don't required
    }

    @Override
    public void clickVector() {   //don't required
    }

    @Override
    public void clickConv() {   //don't required
    }

    @Override
    public void clickClr() {   //don't required
    }

    @Override
    public void clickRandomReal() {   //don't required
    }

    @Override
    public void clickRandomInt() {   //don't required
    }

    @Override
    public void clickLCM() {   //don't required
    }

    @Override
    public void clickGCD() {   //don't required
    }

    @Override
    public void clickInt() {   //don't required
    }

    @Override
    public void clickFloor() {   //don't required
    }

    @Override
    public void clickDRG() {   //don't required
    }

    @Override
    public void clickPreAns() {   //don't required
    }

    @Override
    public void clickMulPowerOfTen() {   //don't required
    }

    @Override
    public void clickRedundancy() {   //don't required
    }

    @Override
    public void clickLogN() {   //don't required
    }

    @Override
    public void clickPol() {   //don't required
    }

    @Override
    public void clickRec() {   //don't required
    }

    @Override
    public void clickEqualSymbol() {   //don't required
    }

    @Override
    public void clickQuotient() {   //don't required
    }

    @Override
    public void clickCalc() {   //don't required
    }

    @Override
    public void clickFindRoots() {   //don't required
    }

    @Override
    public void clickDerivative() {   //don't required
    }

    @Override
    public List<Token> changeBase(int old, int newBase, List<Token> exprOldBase) {
        return exprOldBase;
    }
}