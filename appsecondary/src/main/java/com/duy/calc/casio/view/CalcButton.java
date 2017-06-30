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

package com.duy.calc.casio.view;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.duy.calc.casio.mutilbutton.MultiButtonDescriptor;

/**
 * Created by Duy on 23-Jun-17.
 */

public class CalcButton extends AppCompatButton {
    private MultiButtonDescriptor descriptor;

    public CalcButton(Context context) {
        super(context);
    }

    public CalcButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CalcButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDescriptor(MultiButtonDescriptor descriptor) {
        this.descriptor = descriptor;
    }
}
