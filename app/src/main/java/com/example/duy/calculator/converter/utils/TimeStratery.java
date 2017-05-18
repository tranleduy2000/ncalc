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

package com.example.duy.calculator.converter.utils;

import android.content.Context;

import com.example.duy.calculator.R;

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
