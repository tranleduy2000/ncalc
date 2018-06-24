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

package com.duy.calculator.converter.utils;

import android.content.Context;

import com.duy.calculator.R;

/**
 * Created by tranleduy on 27-May-16.
 */
public class TimeStratery implements Strategy {
    private final Context context;

    private final int TIME_UNIT = 60;

    public TimeStratery(Context context) {
        this.context = context;
    }
    @Override
    public String getUnitDefault() {
        return context.getResources().getString(R.string.timeunitsecond);
    }
    @Override
    public double Convert(String from, String to, double input) {
        if ((from.equals(context.getResources().getString(R.string.timeunitsecond))) &&
                (to.equals(context.getResources().getString(R.string.timeunitsecond)))) {
            return input;
        }


        if ((from.equals(context.getResources().getString(R.string.timeunitsecond))) &&
                (to.equals(context.getResources().getString(R.string.timeunitminutes)))) {
            double ret = input / TIME_UNIT;
            return ret;
        }


        if ((from.equals(context.getResources().getString(R.string.timeunitsecond))) &&
                (to.equals(context.getResources().getString(R.string.timeunithours)))) {
            double ret = input / TIME_UNIT / TIME_UNIT;
            return ret;
        }


        if ((from.equals(context.getResources().getString(R.string.timeunitsecond))) &&
                (to.equals(context.getResources().getString(R.string.timeunitdays)))) {
            double ret = input / TIME_UNIT / TIME_UNIT / 24;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.timeunitsecond))) &&
                (to.equals(context.getResources().getString(R.string.timeunitweeks)))) {
            double ret = input / TIME_UNIT / TIME_UNIT / 24 / 7;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.timeunitsecond))) &&
                (to.equals(context.getResources().getString(R.string.timeunitmiliseconds)))) {
            double ret = input * 1000;
            return ret;
        }


        if ((from.equals(context.getResources().getString(R.string.timeunitminutes))) &&
                (to.equals(context.getResources().getString(R.string.timeunitsecond)))) {
            double ret = input * TIME_UNIT;
            return ret;
        }


        if ((from.equals(context.getResources().getString(R.string.timeunithours))) &&
                (to.equals(context.getResources().getString(R.string.timeunitsecond)))) {
            double ret = input * TIME_UNIT * TIME_UNIT;
            return ret;
        }


        if ((from.equals(context.getResources().getString(R.string.timeunitdays))) &&
                (to.equals(context.getResources().getString(R.string.timeunitsecond)))) {
            double ret = input * TIME_UNIT * TIME_UNIT * 24;
            return ret;
        }


        if ((from.equals(context.getResources().getString(R.string.timeunitweeks))) &&
                (to.equals(context.getResources().getString(R.string.timeunitsecond)))) {
            double ret = input * TIME_UNIT * TIME_UNIT * 24 * 7;
            return ret;
        }


        if ((from.equals(context.getResources().getString(R.string.timeunitmiliseconds))) &&
                (to.equals(context.getResources().getString(R.string.timeunitsecond)))) {
            double ret = input / 1000;
            return ret;
        }

        return 0;
    }
}
