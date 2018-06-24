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
public class VelocityStrategy implements Strategy {
    private Context context;

    public VelocityStrategy(Context context) {
        this.context = context;
    }

    public double Convert(String from, String to, double input) {


        if ((from.equals(context.getResources().getString(R.string.velocityunitmilesperh)) && to.equals(context.getResources().getString(R.string.velocityunitkmph)))) {
            //if((from.equals("miles/hr")) && (to.equals("Km/hr"))){
            double ret = input * 1.60934;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.velocityunitkmph)) && to.equals(context.getResources().getString(R.string.velocityunitmilesperh)))) {
            //if((from.equals("Km/hr")) && (to.equals("miles/hr"))){
            double ret = input * 0.62137;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.velocityunitmilesperh)) && to.equals(context.getResources().getString(R.string.velocityunitmeterpers)))) {
            //if((from.equals("miles/hr")) && (to.equals("m/s"))){
            double ret = input * 1609.34 / 3600;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.velocityunitmeterpers)) && to.equals(context.getResources().getString(R.string.velocityunitmilesperh)))) {
            //if((from.equals("m/s")) && (to.equals("miles/hr"))){
            double ret = input * 3600 / 1609.34;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.velocityunitmilesperh)) && to.equals(context.getResources().getString(R.string.velocityunitfeetpers)))) {
            //if((from.equals("miles/hr")) && (to.equals("feet/s"))){
            double ret = input * 5280 / 3600;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.velocityunitfeetpers)) && to.equals(context.getResources().getString(R.string.velocityunitmilesperh)))) {
            //if((from.equals("feet/s")) && (to.equals("miles/hr"))){
            double ret = input * 3600 / 5280;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.velocityunitkmph)) && to.equals(context.getResources().getString(R.string.velocityunitmeterpers)))) {
            //if((from.equals("Km/hr")) && (to.equals("m/s"))){
            double ret = input * 1000 / 3600;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.velocityunitmeterpers)) && to.equals(context.getResources().getString(R.string.velocityunitkmph)))) {
            //if((from.equals("m/s")) && (to.equals("Km/hr"))){
            double ret = input * 3600 / 1000;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.velocityunitkmph)) && to.equals(context.getResources().getString(R.string.velocityunitfeetpers)))) {
            //if((from.equals("Km/hr")) && (to.equals("feet/s"))){
            double ret = input * 3280.84 / 3600;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.velocityunitfeetpers)) && to.equals(context.getResources().getString(R.string.velocityunitkmph)))) {
            //if((from.equals("feet/s")) && (to.equals("Km/hr"))){
            double ret = input * 3600 / 3280.84;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.velocityunitmeterpers)) && to.equals(context.getResources().getString(R.string.velocityunitfeetpers)))) {
            //if((from.equals("m/s")) && (to.equals("feet/s"))){
            double ret = input * 3.28084;
            return ret;
        }


        if ((from.equals(context.getResources().getString(R.string.velocityunitfeetpers)) && to.equals(context.getResources().getString(R.string.velocityunitmeterpers)))) {
            //if((from.equals("feet/s")) && (to.equals("m/s"))){
            double ret = input / 3.28084;
            return ret;
        }

        if (from.equals(to)) {
            return input;
        }
        return 0;
    }

    @Override
    public String getUnitDefault() {
        return context.getResources().getString(R.string.velocityunitmeterpers);
    }
}
