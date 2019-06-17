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
public class AreaStrategy implements Strategy {
    private Context context;

    public AreaStrategy(Context context) {
        this.context = context;
    }

    public double Convert(String from, String to, double input) {


        if ((from.equals(context.getResources().getString(R.string.areaunitsqmiles)) && to.equals((context.getResources().getString(R.string.areaunitsqkm))))) {
            //if((from.equals("square miles")) && (to.equals("square km"))){
            double ret = 1.60934 * 1.60934 * input;
            return ret;
        }
        if ((from.equals(context.getResources().getString(R.string.areaunitsqkm)) && to.equals((context.getResources().getString(R.string.areaunitsqmiles))))) {
            //if((from.equals("square km")) && (to.equals("square miles"))){
            double ret = 0.62137 * 0.62137 * input;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.areaunitsqmiles)) && to.equals(context.getResources().getString(R.string.areaunitsqm)))) {
            //if((from.equals("square miles")) && (to.equals("square m"))){
            double ret = 1609.34 * 1609.34 * input;
            return ret;
        }
        if ((from.equals(context.getResources().getString(R.string.areaunitsqm)) && to.equals(context.getResources().getString(R.string.areaunitsqmiles)))) {
            //if((from.equals("square m")) && (to.equals("square miles"))){
            double ret = input / (1609.34 * 1609.34);
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.areaunitsqmiles)) && to.equals(context.getResources().getString(R.string.areaunitsqcm)))) {
            //if((from.equals("square miles")) && (to.equals("square cm"))){
            double ret = input * 160934 * 160934;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.areaunitsqcm)) && to.equals(context.getResources().getString(R.string.areaunitsqmiles)))) {
            //if((from.equals("square cm")) && (to.equals("square miles"))){
            double ret = input / (160934 * 160934);
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.areaunitsqmiles)) && to.equals(context.getResources().getString(R.string.areaunitsqmm)))) {
            //if((from.equals("square miles")) && (to.equals("square mm"))){
            double ret = input * 1609340 * input;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.areaunitsqmm)) && to.equals(context.getResources().getString(R.string.areaunitsqmiles)))) {
            //if((from.equals("square mm")) && (to.equals("square miles"))){
            double ret = input / (1609340 * 1609340);
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.areaunitsqmiles)) && to.equals(context.getResources().getString(R.string.areaunitsqyard)))) {
            //if((from.equals("square miles")) && (to.equals("square yards"))){
            double ret = input * 1760 * 1760;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.areaunitsqyard)) && to.equals(context.getResources().getString(R.string.areaunitsqmiles)))) {
            //if((from.equals("square yards")) && (to.equals("square miles"))){
            double ret = input / (1760 * 1760);
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.areaunitsqkm)) && to.equals(context.getResources().getString(R.string.areaunitsqm)))) {
            //if((from.equals("square km")) && (to.equals("square m"))){
            double ret = input * 1000 * 1000;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.areaunitsqm)) && to.equals(context.getResources().getString(R.string.areaunitsqkm)))) {
            //if((from.equals("square m")) && (to.equals("square km"))){
            double ret = input / (1000 * 1000);
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.areaunitsqkm)) && to.equals(context.getResources().getString(R.string.areaunitsqcm)))) {
            //if((from.equals("square km")) && (to.equals("square cm"))){
            double ret = input * 100000 * 100000;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.areaunitsqcm)) && to.equals(context.getResources().getString(R.string.areaunitsqkm)))) {
            //if((from.equals("square cm")) && (to.equals("square km"))){
            double ret = input / (100000 * 100000);
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.areaunitsqkm)) && to.equals(context.getResources().getString(R.string.areaunitsqmm)))) {
            //if((from.equals("square km")) && (to.equals("square mm"))){
            double ret = input * 1000000 * 1000000;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.areaunitsqmm)) && to.equals(context.getResources().getString(R.string.areaunitsqkm)))) {
            //if((from.equals("square mm")) && (to.equals("square km"))){
            double ret = input / (1000000 * 1000000);
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.areaunitsqkm)) && to.equals(context.getResources().getString(R.string.areaunitsqyard)))) {
            //if((from.equals("square km")) && (to.equals("square yards"))){
            double ret = 1093.6133 * 1093.6133 * input;
            return ret;
        }
        if ((from.equals(context.getResources().getString(R.string.areaunitsqyard)) && to.equals(context.getResources().getString(R.string.areaunitsqkm)))) {
            //if((from.equals("square yards")) && (to.equals("square km"))){
            double ret = input / (1093.6133 * 1093.6133);
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.areaunitsqm)) && to.equals(context.getResources().getString(R.string.areaunitsqcm)))) {
            //if((from.equals("square m")) && (to.equals("square cm"))){
            double ret = input * 100 * 100;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.areaunitsqcm)) && to.equals(context.getResources().getString(R.string.areaunitsqm)))) {
            //if((from.equals("square cm")) && (to.equals("square m"))){
            double ret = input / (100 * 100);
            return ret;
        }


        if ((from.equals(context.getResources().getString(R.string.areaunitsqm)) && to.equals(context.getResources().getString(R.string.areaunitsqmm)))) {
            //if((from.equals("square m")) && (to.equals("square mm"))){
            double ret = input * 1000 * 1000;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.areaunitsqmm)) && to.equals(context.getResources().getString(R.string.areaunitsqm)))) {
            //if((from.equals("square mm")) && (to.equals("square m"))){
            double ret = input / (1000 * 1000);
            return ret;
        }


        if ((from.equals(context.getResources().getString(R.string.areaunitsqm)) && to.equals(context.getResources().getString(R.string.areaunitsqyard)))) {
            //if((from.equals("square m")) && (to.equals("square yards"))){
            double ret = 1.09361 * 1.09361 * input;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.areaunitsqyard)) && to.equals(context.getResources().getString(R.string.areaunitsqm)))) {
            //if((from.equals("square yards")) && (to.equals("square m"))){
            double ret = input / (1.09361 * 1.09361);
            return ret;
        }


        if ((from.equals(context.getResources().getString(R.string.areaunitsqcm)) && to.equals(context.getResources().getString(R.string.areaunitsqmm)))) {
            //if((from.equals("square cm")) && (to.equals("square mm"))){
            double ret = (input * 10 * 10);
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.areaunitsqmm)) && to.equals(context.getResources().getString(R.string.areaunitsqcm)))) {
            //if((from.equals("square mm")) && (to.equals("square cm"))){
            double ret = input / (10 * 10);
            return ret;
        }


        if ((from.equals(context.getResources().getString(R.string.areaunitsqcm)) && to.equals(context.getResources().getString(R.string.areaunitsqyard)))) {
            //if((from.equals("square cm")) && (to.equals("square yards"))){
            double ret = 0.01094 * 0.01094 * input;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.areaunitsqyard)) && to.equals(context.getResources().getString(R.string.areaunitsqcm)))) {
            //if((from.equals("square yards")) && (to.equals("square cm"))){
            double ret = input / (0.01094 * 0.01094);
            return ret;
        }


        if ((from.equals(context.getResources().getString(R.string.areaunitsqmm)) && to.equals(context.getResources().getString(R.string.areaunitsqyard)))) {
            //if((from.equals("square mm")) && (to.equals("square yards"))){
            double ret = 0.001094 * 0.001094 * input;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.areaunitsqyard)) && to.equals(context.getResources().getString(R.string.areaunitsqmm)))) {
            //if((from.equals("square yards")) && (to.equals("square mm"))){
            double ret = input / (0.001094 * 0.001094);
            return ret;
        }

        if (from.equals(to)) {
            return input;
        }
        return 0.0;
    }

    @Override
    public String getUnitDefault() {
        return context.getResources().getString(R.string.areaunitsqm);
    }
}
