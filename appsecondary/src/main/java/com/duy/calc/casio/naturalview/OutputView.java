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

package com.duy.calc.casio.naturalview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

/**
 * Created by Duy on 24-Jun-17.
 */

public class OutputView extends DisplayView {

    public OutputView(Context context, AttributeSet attr) {
        super(context, attr);
        cursor.setVisible(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float maxY = drawExpression(new Canvas());
        int maxX = -1;
        for (float aFloat : drawX) if (aFloat > maxX) maxX = (int) aFloat;
        int height = expression.size() == 0 ? (int) mTextHeight : (int) (maxY + mTextHeight);
        this.setMeasuredDimension(maxX, height);
    }
}
