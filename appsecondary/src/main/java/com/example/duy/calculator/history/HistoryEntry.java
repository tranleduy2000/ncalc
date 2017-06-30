package com.example.duy.calculator.history;

import java.io.Serializable;
import java.util.Date;

/**
 * Item for adapter history
 * <p/>
 * Created by Duy on 3/7/2016
 */
public class HistoryEntry implements Serializable {
    public static final int TYPE_SCIENCE = 0;
    public static final int TYPE_LOGIC = 1;
    public static final int TYPE_COMPLEX = 2;
    public static final int TYPE_GRAPH = 3;
    public static final int TYPE_SOLVE_ROOT = 4;
    public static final int TYPE_MAXTRIX = 5;
    public static final int TYPE_LINEAR_SYSTEM = 3;

    public static final long serialVersionUID = 4L;
    private int type = 0;
    private String math;
    private String result;
    private String inputTex = "";
    private String resultTex = "";
    private int color = 0;
    private long time; //id

    public HistoryEntry(String math, String res) {
        this(math, res, 0, new Date().getTime());
    }

    public HistoryEntry(String math, String result, long time) {
        this(math, result, 0, time);
    }

    public HistoryEntry(String math, String result, int color, long time) {
        this(math, result, color, time, TYPE_SCIENCE);
    }

    public HistoryEntry(String math, String result, int color, long time, int type) {
        this.math = math;
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

    public String getMath() {
        return math;
    }

    public void setMath(String math) {
        this.math = math;
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
        return this.math + " = " + this.result;
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
