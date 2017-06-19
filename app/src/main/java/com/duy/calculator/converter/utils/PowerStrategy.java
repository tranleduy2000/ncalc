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
        // TODO Auto-generated method stub

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
