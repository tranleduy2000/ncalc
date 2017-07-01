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

package com.duy.calculator;

import android.support.annotation.NonNull;

/**
 * Created by Duy on 30-Jun-17.
 */

public class CalculatorPresenter implements CalculatorContract.Presenter {

    private CalculatorContract.Display display;
    private CalculatorContract.ResourceAccess resourceAccess;

    public CalculatorPresenter(@NonNull CalculatorContract.Display display,
                               @NonNull CalculatorContract.ResourceAccess resourceAccess) {
        this.display = display;
        this.resourceAccess = resourceAccess;
    }

    @Override
    public void updateSettings() {

    }

    @Override
    public CalculatorContract.Display getDisplay() {
        return null;
    }

    @Override
    public void openSetting() {

    }

    @Override
    public void handleExceptions(Exception e) {

    }

    @Override
    public void openHistory() {

    }
}
