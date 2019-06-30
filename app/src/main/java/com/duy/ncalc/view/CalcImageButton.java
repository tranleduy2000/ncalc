package com.duy.ncalc.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.duy.calculator.R;

public class CalcImageButton extends AppCompatImageView {
    @Nullable
    private ColorStateList mColorStateList;

    public CalcImageButton(Context context) {
        super(context);
        setup(context, null);
    }

    public CalcImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context, attrs);
    }

    public CalcImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context, attrs);
    }


    private void setup(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CalcImageButton);
            mColorStateList = ta.getColorStateList(R.styleable.CalcImageButton_android_textColor);

            refreshDrawableState();
            ta.recycle();
        }
    }

    public void setTextColor(@ColorInt int color) {
        mColorStateList = ColorStateList.valueOf(color);
        refreshDrawableState();
    }

    @Override
    public void refreshDrawableState() {
        super.refreshDrawableState();
        if (mColorStateList != null) {
            int defColor = mColorStateList.getColorForState(getDrawableState(),
                    mColorStateList.getDefaultColor());
            clearColorFilter();
            setColorFilter(defColor, PorterDuff.Mode.SRC_IN);
        }
    }

}
