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
public class PowerStrategy implements Strategy {
    private Context context;

    @Override
    public String getUnitDefault() {
        return context.getResources().getString(R.string.powerunithorsepower);
    }

    public PowerStrategy(Context context) {
        this.context = context;
    }

    public double Convert(String from, String to, double input) {


        if ((from.equals(context.getResources().getString(R.string.powerunitwatts))
                && to.equals(context.getResources().getString(R.string.powerunithorsepower)))) {
            //if((from.equals("watts")) && (to.equals("horsepower"))){
            double ret = 0.00134 * input;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.powerunithorsepower))
                && to.equals(context.getResources().getString(R.string.powerunitwatts)))) {
            //if((from.equals("horsepower")) && (to.equals("watts"))){
            double ret = 745.7 * input;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.powerunitwatts))
                && to.equals(context.getResources().getString(R.string.powerunitkilowatts)))) {
            //if((from.equals("watts")) && (to.equals("kilowatts"))){
            double ret = input / 1000;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.powerunitkilowatts))

                && to.equals(context.getResources().getString(R.string.powerunitwatts)))) {
            //if((from.equals("kilowatts")) &&(to.equals("watts"))){
            double ret = input * 1000;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.powerunitkilowatts))
                && to.equals(context.getResources().getString(R.string.powerunithorsepower)))) {
            //if((from.equals("kilowatts")) && (to.equals("horsepower"))){
            double ret = input * 1.34102;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.powerunithorsepower))
                && to.equals(context.getResources().getString(R.string.powerunitkilowatts)))) {
            //if((from.equals("horsepower")) &&(to.equals("kilowatts"))){
            double ret = input * 0.7457;
            return ret;
        }


        if ((from.equals(context.getResources().getString(R.string.powerunithorsepower))
                && to.equals(context.getResources().getString(R.string.powerunitmegawatt)))) {
            //if((from.equals("horsepower")) &&(to.equals("kilowatts"))){
            double ret = input * 745.69 / 1000 / 1000;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.powerunitmegawatt))
                && to.equals(context.getResources().getString(R.string.powerunithorsepower)))) {
            //if((from.equals("horsepower")) &&(to.equals("kilowatts"))){
            double ret = input / 745.69 * 1000 * 1000;
            return ret;
        }

//
        if ((from.equals(context.getResources().getString(R.string.powerunithorsepower))
                && to.equals(context.getResources().getString(R.string.powerunitgigawatt)))) {
            //if((from.equals("horsepower")) &&(to.equals("kilowatts"))){
            double ret = input * 745.69 / 1000 / 1000 / 1000;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.powerunitgigawatt))
                && to.equals(context.getResources().getString(R.string.powerunithorsepower)))) {
            //if((from.equals("horsepower")) &&(to.equals("kilowatts"))){
            double ret = input / 745.69 * 1000 * 1000 * 1000;
            return ret;
        }

        if (from.equals(to)) {
            return input;
        }
        return 0.0;
    }

}
