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

package com.duy.calculator.evaluator;

import android.content.Context;

import com.duy.ncalc.settings.CalculatorSetting;


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
    private int[] variableToKeep = new int[]{};

    private EvaluateConfig(EvaluateConfig other) {
        setEvalMode(other.getEvaluateMode());
        setRoundTo(other.getRoundTo());
        setTrigMode(other.getTrigMode());
    }

    private EvaluateConfig() {
    }

    public static EvaluateConfig loadFromSetting(Context c) {
        return CalculatorSetting.createEvaluateConfig(c);
    }

    public static EvaluateConfig newInstanceFrom(EvaluateConfig other) {
        return new EvaluateConfig(other);
    }

    public static EvaluateConfig newInstance() {
        return new EvaluateConfig();
    }


    public int getTrigMode() {
        return trigMode;
    }

    public EvaluateConfig setTrigMode(int trigMode) {
        this.trigMode = trigMode;
        return this;
    }

    public int getEvaluateMode() {
        return evalMode;
    }

    public EvaluateConfig setEvalMode(int evalMode) {
        this.evalMode = evalMode;
        return this;
    }

    public int getRoundTo() {
        return roundTo;
    }

    public EvaluateConfig setRoundTo(int roundTo) {
        this.roundTo = roundTo;
        return this;
    }

    public boolean hasKeepVariable(int type) {
        for (int i : variableToKeep) if (i == type) return true;
        return false;
    }

    public EvaluateConfig keepVars(int... type) {
        this.variableToKeep = type;
        return this;
    }
}
