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

package com.duy.ncalc.graph;

import android.content.Context;
import android.util.AttributeSet;


public class FastGraphView extends com.duy.ncalc.graph.Graph2DView {


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