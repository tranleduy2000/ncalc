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
