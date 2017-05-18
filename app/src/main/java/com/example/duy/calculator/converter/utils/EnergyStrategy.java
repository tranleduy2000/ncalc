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
public class EnergyStrategy implements Strategy {
    private final Context context;

    public EnergyStrategy(Context context) {
        this.context = context;
    }

    @Override
    public String getUnitDefault() {
        return context.getResources().getString(R.string.energyunitcalories);
    }

    public double Convert(String from, String to, double input) {
        // TODO Auto-generated method stub

        if ((from.equals(context.getResources().getString(R.string.energyunitcalories)) && to.equals(context.getResources().getString(R.string.energyunitkilocalories)))) {
            //if((from.equals("calories")) &&(to.equals("kilocalories"))){
            double ret = input / 1000;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.energyunitkilocalories)) && to.equals(context.getResources().getString(R.string.energyunitcalories)))) {
            //if((from.equals("kilocalories")) && (to.equals("calories"))){
            double ret = input * 1000;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.energyunitcalories)) && to.equals(context.getResources().getString(R.string.energyunitjoules)))) {
            //if((from.equals("calories")) && (to.equals("joules"))){
            double ret = input * 4.1868;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.energyunitjoules)) && to.equals(context.getResources().getString(R.string.energyunitcalories)))) {
            //if((from.equals("joules")) && (to.equals("calories"))){
            double ret = input * 0.23885;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.energyunitkilocalories)) && to.equals(context.getResources().getString(R.string.energyunitjoules)))) {
            //if((from.equals("kilocalories")) && (to.equals("joules"))){
            double ret = input * 4186.8;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.energyunitjoules)) && to.equals(context.getResources().getString(R.string.energyunitkilocalories)))) {
            //if((from.equals("joules")) && (to.equals("kilocalories"))){
            double ret = input / 4186.8;
            return ret;
        }
        if (from.equals(to)) {
            return input;
        }
        return 0.0;
    }
}
