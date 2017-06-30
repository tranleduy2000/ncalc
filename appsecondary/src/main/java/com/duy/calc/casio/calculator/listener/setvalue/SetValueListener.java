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

package com.duy.calc.casio.calculator.listener.setvalue;

import android.view.View;

import com.duy.calc.casio.calculator.listener.compute.ComputeListener;

/**
 * Created by Duy on 28-Jun-17.
 */

public class SetValueListener extends ComputeListener {
    private SetValueHandler mCallback;

    public SetValueListener(SetValueHandler callback) {
        this.mCallback = callback;
    }

    @Override
    public boolean onClick(View v) {
        return super.onClick(v);
    }

    @Override
    public void clickEquals() {
        mCallback.clickEqual();
    }

    @Override
    public void clickFindRoots() {

    }

    @Override
    public void clickCalc() {
    }

    @Override
    public void clickRcl() {
    }


    @Override
    public void clickStore() {

    }

    @Override
    public void clickDecFracMode() {
    }

    @Override
    public void clickClear() {
        mCallback.clickClear();
    }

    public void clearExpr() {
        this.expression.clear();
    }
}
