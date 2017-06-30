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

package com.duy.calc.casio.evaluator;

import android.content.Context;

import com.duy.calc.casio.settings.CalculatorSetting;

/**
 * Created by Duy on 26-Jun-17.
 */

public class EvaluateConfig implements Cloneable {
    //trig mode
    public static final int DEGREE = 0;
    public static final int RADIAN = 1;
    public static final int GRADIAN = 2;

    //calculate mode
    public static final int DECIMAL = 0;
    public static final int FRACTION = 1;
    private int trigMode = RADIAN;
    private int evalMode = DECIMAL;
    private int roundTo = 10;

    public static EvaluateConfig loadFromSetting(Context c) {
        return CalculatorSetting.createEvaluateConfig(c);
    }

    public int getTrigMode() {
        return trigMode;
    }

    public void setTrigMode(int trigMode) {
        this.trigMode = trigMode;
    }

    public int getEvaluateMode() {
        return evalMode;
    }

    public void setEvalMode(int evalMode) {
        this.evalMode = evalMode;
    }

    public int getRoundTo() {
        return roundTo;
    }

    public void setRoundTo(int roundTo) {
        this.roundTo = roundTo;
    }

}
