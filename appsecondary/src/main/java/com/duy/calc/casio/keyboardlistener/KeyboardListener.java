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

package com.duy.calc.casio.keyboardlistener;

import android.view.View;

import com.duy.calc.casio.calculator.CalculatorContract;
import com.duy.calc.casio.token.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Duy on 26-Jun-17.
 */

public interface KeyboardListener {

    boolean onClick(View view);

    void clickExit();

    void clickOne();

    void clickTwo();

    void clickThree();

    void clickFour();

    void clickFive();

    void clickSix();

    void clickSeven();

    void clickEight();

    void clickNine();

    void clickZero();

    void clickDecimal();

    void clickMultiply();

    void clickDivide();

    void clickAdd();

    void clickSubtract();

    void clickSqrt();

    void clickClear();

    void clickBack();

    void clickA();

    void clickAbs();

    void clickACos();

    void clickACosh();

    void clickAlpha();

    void clickAns();

    void clickASin();

    void clickASinh();

    void clickATan();

    void clickATanh();

    void clickB();

    void clickBase();

    void clickBin();

    void clickC();

    void clickCalc();

    void clickCbrt();

    void clickCloseParentheses();

    void clickClr();

    void clickCombination();

    void clickCommaSign();

    void clickComplex();

    void clickConst();

    void clickConv();

    void clickCos();

    void clickCosh();

    void clickCube();

    void clickD();

    void clickDecFracMode();

    void clickDegree();

    void clickDerivative();

    List<Token> getExpression();

    void setExpression(ArrayList<Token> tokens);

    void clickImplicit();

    void clickENG();

    void clickRandomInt();

    void clickRandomReal();

    void clickRcl();

    void clickRec();

    void clickReciprocal();

    void clickRedundancy();

    void clickSemicolon();

    void clickShift();

    void clickSin();

    void clickSinh();

    void clickSquare();

    void clickStore();

    void clickSurd();

    void clickTan();

    void clickTanh();

    void clickVector();

    void clickX();

    void clickY();

    void clickDRG();

    void clickE();

    void click_e();

    void clickEquals();

    void clickEqualSymbol();

    void clickF();

    void clickFactorial();

    void clickFindRoots();

    void clickFraction();

    void clickGCD();

    void clickHistory();

    void clickHyp();

    void clickInt();

    void clickIntegrate();

    void clickFloor();

    void clickLCM();

    void clickLn();

    void clickLog_10();

    void clickLogN();

    void clickM();

    void clickMatrix();

    void clickMem();

    void clickMMinus();

    void clickMPlus();

    void clickMulPowerOfTen();

    void clickOctal();

    void clickOpenParentheses();

    void clickPercent();

    void clickPermutation();

    void clickPi();

    void clickPol();

    void clickPower();

    void clickPowerOfE();

    void clickPowerOfTen();

    void clickPreAns();

    void clickPrimeFactor();

    void clickQuotient();

    void clickNegative();

    void setPresenter(CalculatorContract.Presenter mPresenter);

}
