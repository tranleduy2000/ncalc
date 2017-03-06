package com.example.duy.calculator.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;


public class RevealView extends View {

    final Paint mPaint = new Paint();
    Path mRevealPath;
    float mCenterX;
    float mCenterY;
    float mRadius;
    View mTarget;

    public RevealView(Context context) {
        this(context, null);
    }

    public RevealView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RevealView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mRevealPath = new Path();
    }


    public void setRevealColor(int color) {
        mPaint.setColor(color);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (mTarget == null) {
            canvas.drawColor(mPaint.getColor());
        } else {
            final int state = canvas.save();

            mRevealPath.reset();
            mRevealPath.addCircle(mCenterX, mCenterY, mRadius, Path.Direction.CW);

            canvas.drawPath(mRevealPath, mPaint);

            canvas.restoreToCount(state);
        }
    }
}
