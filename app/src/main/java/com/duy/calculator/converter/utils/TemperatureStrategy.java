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

package com.duy.calculator.converter.utils;

import android.content.Context;

import com.duy.calculator.R;

/**
 * Created by tranleduy on 27-May-16.
 */
public class TemperatureStrategy implements Strategy {
    private Context context;

    public TemperatureStrategy(Context context) {
        this.context = context;
    }

    public double Convert(String from, String to, double input) {


        if ((from.equals(context.getResources().getString(R.string.temperatureunitc)) && to.equals((context.getResources().getString(R.string.temperatureunitf))))) {
            return (input * 9 / 5) + 32;
        }

        if ((from.equals(context.getResources().getString(R.string.temperatureunitf)) && to.equals((context.getResources().getString(R.string.temperatureunitc))))) {
            return (input - 32) * 5 / 9;
        }

        if ((from.equals(context.getResources().getString(R.string.temperatureunitc)) && to.equals((context.getResources().getString(R.string.temperatureunitk))))) {
            return input + 273;
        }

        if ((from.equals(context.getResources().getString(R.string.temperatureunitk)) && to.equals((context.getResources().getString(R.string.temperatureunitc))))) {
            return input - 273;
        }

        if ((from.equals(context.getResources().getString(R.string.temperatureunitc))
                && to.equals((context.getResources().getString(R.string.temperatureunitn))))) {
            return input * 33 / 100;
        }
        if ((from.equals(context.getResources().getString(R.string.temperatureunitn)) &&
                to.equals((context.getResources().getString(R.string.temperatureunitc))))) {
            return input * 100 / 33;
        }

        if (from.equals(to)) {
            return input;
        }

        return 0.0;
    }

    @Override
    public String getUnitDefault() {

        return context.getResources().getString(R.string.temperatureunitc);
    }

}
