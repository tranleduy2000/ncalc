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

package com.duy.calculator.history;

import java.io.Serializable;
import java.util.Date;

/**
 * Item for adapter history
 * <p/>
 * Created by Duy on 3/7/2016
 */
public class ResultEntry implements Serializable {
    public static final int TYPE_SCIENCE = 0;
    public static final int TYPE_LOGIC = 1;
    public static final int TYPE_COMPLEX = 2;
    public static final int TYPE_GRAPH = 3;
    public static final int TYPE_SOLVE_ROOT = 4;
    public static final int TYPE_MAXTRIX = 5;
    public static final int TYPE_LINEAR_SYSTEM = 3;

    public static final long serialVersionUID = 4L;
    private int type = 0;
    private String expression = "";
    private String result = "";
    private String inputTex = "";
    private String resultTex = "";
    private int color = 0;
    private long time = 0; //id

    public ResultEntry(String expression, String res) {
        this(expression, res, 0, new Date().getTime());
    }

    public ResultEntry(String expression, String result, long time) {
        this(expression, result, 0, time);
    }

    public ResultEntry(String expression, String result, int color, long time) {
        this(expression, result, color, time, TYPE_SCIENCE);
    }

    public ResultEntry(String expression, String result, int color, long time, int type) {
        this.expression = expression;
        this.result = result;
        this.color = color;
        this.time = time;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return this.expression + " = " + this.result;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getInputTex() {
        return inputTex;
    }

    public void setInputTex(String inputTex) {
        this.inputTex = inputTex;
    }

    public String getResultTex() {
        return resultTex;
    }

    public void setResultTex(String resultTex) {
        this.resultTex = resultTex;
    }
}
