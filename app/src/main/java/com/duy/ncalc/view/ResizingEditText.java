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

package com.duy.ncalc.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.duy.calculator.R;
import com.duy.ncalc.utils.TextUtil;


/**
 * ResizingEditText will attempt to resize the text to as large as possible (with optional
 * max and min values). You can also use getVariableTextSize() to see what font size will
 * be used.
 */
public class ResizingEditText extends BaseEditText {
    private final Paint mTempPaint = new TextPaint();
    private float mMaximumTextSize;
    private float mMinimumTextSize;
    private float mStepTextSize;
    // Try and use as large a text as possible, if the width allows it
    private int mWidthConstraint = -1;
    private int mHeightConstraint = -1;
    private OnTextSizeChangeListener mOnTextSizeChangeListener;

    public ResizingEditText(Context context) {
        super(context);
        setUp(context, null);
    }

    public ResizingEditText(Context context, AttributeSet attr) {
        super(context, attr);
        setUp(context, attr);
    }

    private void setUp(Context context, AttributeSet attrs) {
        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(
                    attrs, R.styleable.ResizingEditText, 0, 0);
            mMaximumTextSize = a.getDimension(R.styleable.ResizingEditText_maxTextSize, getTextSize());
            mMinimumTextSize = a.getDimension(R.styleable.ResizingEditText_minTextSize, getTextSize());
            mStepTextSize = a.getDimension(R.styleable.ResizingEditText_stepTextSize, (mMaximumTextSize - mMinimumTextSize) / 3);
            a.recycle();

            setTextSize(TypedValue.COMPLEX_UNIT_PX, mMaximumTextSize);
            setMinimumHeight((int) (mMaximumTextSize * 1.2) + getPaddingBottom() + getPaddingTop());
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        invalidateTextSize();
    }

    protected void invalidateTextSize() {
        float oldTextSize = getTextSize();
        float newTextSize = getVariableTextSize(getText().toString());
        if (oldTextSize != newTextSize) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
        }
    }

    @Override
    public void setTextSize(int unit, float size) {
        final float oldTextSize = getTextSize();
        super.setTextSize(unit, size);
        if (mOnTextSizeChangeListener != null && getTextSize() != oldTextSize) {
            mOnTextSizeChangeListener.onTextSizeChanged(this, oldTextSize);
        }
    }

    public void setOnTextSizeChangeListener(OnTextSizeChangeListener listener) {
        mOnTextSizeChangeListener = listener;
    }

    public float getVariableTextSize(String text) {
        if (mWidthConstraint < 0 || mMaximumTextSize <= mMinimumTextSize) {
            // Not measured, bail early.
            return getTextSize();
        }

        // Count exponents, which aren't measured properly.
        int exponents = TextUtil.countOccurrences(text, '^');

        // Step through increasing text sizes until the text would no longer fit.
        float lastFitTextSize = mMinimumTextSize;
        while (lastFitTextSize < mMaximumTextSize) {
            final float nextSize = Math.min(lastFitTextSize + mStepTextSize, mMaximumTextSize);
            mTempPaint.setTextSize(nextSize);
            if (mTempPaint.measureText(text) > mWidthConstraint) {
                break;
            } else if (nextSize + nextSize * exponents / 2 > mHeightConstraint) {
                break;
            } else {
                lastFitTextSize = nextSize;
            }
        }

        return lastFitTextSize;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidthConstraint =
                MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        mHeightConstraint =
                MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        setTextSize(TypedValue.COMPLEX_UNIT_PX, getVariableTextSize(getText().toString()));
    }

    public String getCleanText() {
        return this.getText().toString();
    }

    public void insert(String delta) {
        String currentText = getText().toString();
        int selectionHandle = getSelectionStart();
        String textBeforeInsertionHandle = currentText.substring(0, selectionHandle);
        String textAfterInsertionHandle = currentText.substring(selectionHandle, currentText.length());
        setText(textBeforeInsertionHandle + delta + textAfterInsertionHandle);
        setSelection(selectionHandle + delta.length());
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        invalidateTextSize();
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
    }

    public interface OnTextSizeChangeListener {

        void onTextSizeChanged(TextView textView, float oldSize);
    }

}
