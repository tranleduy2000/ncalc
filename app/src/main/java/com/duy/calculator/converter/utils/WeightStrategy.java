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
public class WeightStrategy implements Strategy {
    private Context context;

    public WeightStrategy(Context context) {
        this.context = context;

    }

    @Override
    public String getUnitDefault() {
        return context.getResources().getString(R.string.weightunitkg);
    }

    public double Convert(String from, String to, double input) {
        // TODO Auto-generated method stub

        if ((from.equals(context.getResources().getString(R.string.weightunitkg)) && to.equals(context.getResources().getString(R.string.weightunitgm)))) {
            //if((from.equals("Kg")) && (to.equals("gm"))){
            double ret = (double) (1000 * input);
            return ret;
        }


        if ((from.equals(context.getResources().getString(R.string.weightunitgm)) && to.equals(context.getResources().getString(R.string.weightunitkg)))) {
            //if((from.equals("gm")) && (to.equals("Kg"))){
            double ret = (double) (input / 1000);
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.weightunitkg)) && to.equals(context.getResources().getString(R.string.weightunitlb)))) {
            //if((from.equals("Kg")) && (to.equals("lb"))){
            double ret = 2.2046 * input;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.weightunitlb)) && to.equals(context.getResources().getString(R.string.weightunitkg)))) {
            //if((from.equals("lb")) && (to.equals("Kg"))){
            double ret = 0.454 * input;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.weightunitkg)) && to.equals(context.getResources().getString(R.string.weightunitounce)))) {
            //if((from.equals("Kg")) && (to.equals("ounce"))){
            double ret = input * 35.27396;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.weightunitounce)) && to.equals(context.getResources().getString(R.string.weightunitkg)))) {
            //if((from.equals("ounce")) && (to.equals("Kg"))){
            double ret = input * 0.02835;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.weightunitkg)) && to.equals(context.getResources().getString(R.string.weightunitmg)))) {
            //if((from.equals("Kg")) && (to.equals("mg"))){
            double ret = input * 1000000;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.weightunitmg)) && to.equals(context.getResources().getString(R.string.weightunitkg)))) {
            //if((from.equals("mg")) && (to.equals("Kg"))){
            double ret = input / 1000000;
            return ret;
        }


        if ((from.equals(context.getResources().getString(R.string.weightunitgm)) && to.equals(context.getResources().getString(R.string.weightunitlb)))) {
            //if((from.equals("gm")) && (to.equals("lb"))){
            double ret = 0.0022 * input;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.weightunitlb)) && to.equals(context.getResources().getString(R.string.weightunitgm)))) {
            //if((from.equals("lb")) && (to.equals("gm"))){
            double ret = 453.6 * input;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.weightunitgm)) && to.equals(context.getResources().getString(R.string.weightunitmg)))) {
            //if((from.equals("gm")) && (to.equals("mg"))){
            double ret = input * 1000;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.weightunitmg)) && to.equals(context.getResources().getString(R.string.weightunitgm)))) {
            //if((from.equals("mg")) && (to.equals("gm"))){
            double ret = input / 1000;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.weightunitgm)) && to.equals(context.getResources().getString(R.string.weightunitounce)))) {
            //if((from.equals("gm")) && (to.equals("ounce"))){
            double ret = input * 0.03527;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.weightunitounce)) && to.equals(context.getResources().getString(R.string.weightunitgm)))) {
            //if((from.equals("ounce")) && (to.equals("gm"))){
            double ret = input * 28.34952;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.weightunitlb)) && to.equals(context.getResources().getString(R.string.weightunitmg)))) {
            //if((from.equals("lb")) && (to.equals("mg"))){
            double ret = input * 453592.37;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.weightunitmg)) && to.equals(context.getResources().getString(R.string.weightunitlb)))) {
            //if((from.equals("mg")) && (to.equals("lb"))){
            double ret = input / 453592.37;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.weightunitounce)) && to.equals(context.getResources().getString(R.string.weightunitmg)))) {
            //if((from.equals("ounce")) && (to.equals("mg"))){
            double ret = input * 28349.52313;
            return ret;
        }


        if ((from.equals(context.getResources().getString(R.string.weightunitmg)) && to.equals(context.getResources().getString(R.string.weightunitounce)))) {
            //if((from.equals("mg")) && (to.equals("ounce"))){
            double ret = input / 28349;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.weightunitlb)) && to.equals(context.getResources().getString(R.string.weightunitounce)))) {
            //if((from.equals("lb")) && (to.equals("ounce"))){
            double ret = 16 * input;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.weightunitounce)) && to.equals(context.getResources().getString(R.string.weightunitlb)))) {
            //if ((from.equals("ounce")) && (to.equals("lb"))){
            double ret = input / 16;
            return ret;
        }
        if (from.equals(to)) {
            return input;
        }
        return 0.0;
    }
}
