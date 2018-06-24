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
