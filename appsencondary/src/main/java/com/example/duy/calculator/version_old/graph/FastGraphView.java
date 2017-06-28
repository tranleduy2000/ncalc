package com.example.duy.calculator.version_old.graph;

import android.content.Context;
import android.util.AttributeSet;


public class FastGraphView extends Graph2DView {


    public FastGraphView(Context context) {
        super(context);
        init();
    }

    public FastGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FastGraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        setWindow(-3, -3, 3, 3, 1, 1);
    }
}