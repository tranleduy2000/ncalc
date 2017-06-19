/*
 * Copyright 2017 Tran Le Duy
 *
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

package com.duy.calculator.graph;

import android.content.Context;
import android.util.AttributeSet;


public class FastGraphView extends com.duy.calculator.graph.Graph2DView {


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