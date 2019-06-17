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

package com.duy.ncalc.unitconverter.utils;

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
