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

package com.duy.calc.casio.calculator.listener;

import android.support.v4.app.Fragment;

import com.duy.calc.casio.calculator.CalculatorActivity;

/**
 * Created by Duy on 26-Jun-17.
 */

public abstract class AbstractFragment extends Fragment {
    protected CalculatorActivity activity;

    public abstract int getRootId();

    @Override
    public void onResume() {
        super.onResume();
        activity = (CalculatorActivity) getActivity();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
