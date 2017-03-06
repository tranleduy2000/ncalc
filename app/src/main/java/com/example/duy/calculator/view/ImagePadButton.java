package com.example.duy.calculator.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Duy on 3/7/2016
 */
public class ImagePadButton extends ImageView {
    Context activity;


    public ImagePadButton(Context context) {
        super(context);
        init(context);
    }

    public ImagePadButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ImagePadButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        if (!isInEditMode()) {
            activity = context;

        }
    }
}