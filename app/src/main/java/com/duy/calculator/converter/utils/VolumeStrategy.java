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
public class VolumeStrategy implements Strategy {
    private Context context;

    public VolumeStrategy(Context context) {
        this.context = context;

    }

    @Override
    public String getUnitDefault() {
        return context.getResources().getString(R.string.volumeunitcubiccm);
    }

    public double Convert(String from, String to, double input) {



//        if ((from.equals(mContext.getResources().getString(R.string.volumeunitlitres)) && to.equals(mContext.getResources().getString(R.string.volumeunitmillilitres)))) {
//            //if((from.equals("litre")) && (to.equals("millilitre"))){
//            double ret = input * 1000;
//            return ret;
//        }
//
//        if ((from.equals(mContext.getResources().getString(R.string.volumeunitmillilitres)) && to.equals(mContext.getResources().getString(R.string.volumeunitlitres)))) {
//            //if((from.equals("millilitre")) && (to.equals("litre"))){
//            double ret = input / 1000;
//            return ret;
//        }


//        if ((from.equals(mContext.getResources().getString(R.string.volumeunitlitres)) && to.equals(mContext.getResources().getString(R.string.volumeunitcubicm)))) {
//            //if((from.equals("litre")) && (to.equals("cubic m"))){
//            double ret = input * 0.001;
//            return ret;
//        }
//
//        if ((from.equals(mContext.getResources().getString(R.string.volumeunitcubicm)) && to.equals(mContext.getResources().getString(R.string.volumeunitlitres)))) {
//            //if((from.equals("cubic m")) && (to.equals("litre"))){
//            double ret = input * 1000;
//            return ret;
//        }

//        if ((from.equals(mContext.getResources().getString(R.string.volumeunitlitres)) && to.equals(mContext.getResources().getString(R.string.volumeunitcubiccm)))) {
//            //if((from.equals("litre")) && (to.equals("cubic cm"))){
//            double ret = 1000 * input;
//            return ret;
//        }
//
//        if ((from.equals(mContext.getResources().getString(R.string.volumeunitcubiccm)) && to.equals(mContext.getResources().getString(R.string.volumeunitlitres)))) {
//            //if((from.equals("cubic cm")) && (to.equals("litre"))){
//            double ret = 0.001 * input;
//            return ret;
//        }
//
//        if ((from.equals(mContext.getResources().getString(R.string.volumeunitlitres)) && to.equals(mContext.getResources().getString(R.string.volumeunitcubicmm)))) {
//            //if((from.equals("litre")) && (to.equals("cubic mm"))){
//            double ret = input * 1000000;
//            return ret;
//        }
//
//        if ((from.equals(mContext.getResources().getString(R.string.volumeunitcubicmm)) && to.equals(mContext.getResources().getString(R.string.volumeunitlitres)))) {
//            //if((from.equals("cubic mm")) && (to.equals("litre"))){
//            double ret = input / 1000000;
//            return ret;
//        }
//
//        if ((from.equals(mContext.getResources().getString(R.string.volumeunitlitres)) && to.equals(mContext.getResources().getString(R.string.volumeunitcubicfeet)))) {
//            //if((from.equals("litre")) && (to.equals("cubic feet"))){
//            double ret = input * 0.03531;
//            return ret;
//        }
//
//        if ((from.equals(mContext.getResources().getString(R.string.volumeunitcubicfeet)) && to.equals(mContext.getResources().getString(R.string.volumeunitlitres)))) {
//            //if((from.equals("cubic feet")) && (to.equals("litre"))){
//            double ret = input * 28.31685;
//            return ret;
//        }

        if ((from.equals(context.getResources().getString(R.string.volumeunitcubicm)) && to.equals(context.getResources().getString(R.string.volumeunitcubiccm)))) {
            //if((from.equals("cubic m")) && (to.equals("cubic cm"))){
            double ret = input * 100 * 100 * 100;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.volumeunitcubiccm)) && to.equals(context.getResources().getString(R.string.volumeunitcubicm)))) {
            //if((from.equals("cubic cm")) && (to.equals("cubic m"))){
            double ret = input / 1000000;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.volumeunitcubicm)) && to.equals(context.getResources().getString(R.string.volumeunitcubicmm)))) {
            //if((from.equals("cubic m")) && (to.equals("cubic mm"))){
            double ret = input * 1000 * 1000 * 1000;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.volumeunitcubicmm)) && to.equals(context.getResources().getString(R.string.volumeunitcubicm)))) {
            //if((from.equals("cubic mm")) && (to.equals("cubic m"))){
            double ret = input / (1000 * 1000 * 1000);
            return ret;
        }

//        if ((from.equals(mContext.getResources().getString(R.string.volumeunitcubicm)) && to.equals(mContext.getResources().getString(R.string.volumeunitmillilitres)))) {
//            //if((from.equals("cubic m")) && (to.equals("millilitre"))){
//            double ret = input * 1000000;
//            return ret;
//        }
//
//        if ((from.equals(mContext.getResources().getString(R.string.volumeunitmillilitres)) && to.equals(mContext.getResources().getString(R.string.volumeunitcubicm)))) {
//            //if((from.equals("millilitre")) && (to.equals("cubic m"))){
//            double ret = input / 1000000;
//            return ret;
//        }

        if ((from.equals(context.getResources().getString(R.string.volumeunitcubicm)) && to.equals(context.getResources().getString(R.string.volumeunitcubicfeet)))) {
            //if((from.equals("cubic m")) && (to.equals("cubic feet"))){
            double ret = input * 35.31467;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.volumeunitcubicfeet)) && to.equals(context.getResources().getString(R.string.volumeunitcubicm)))) {
            //if((from.equals("cubic feet")) && (to.equals("cubic m"))){
            double ret = input / 35.31467;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.volumeunitcubiccm)) && to.equals(context.getResources().getString(R.string.volumeunitcubicmm)))) {
            //if((from.equals("cubic cm")) && (to.equals("cubic mm"))){
            double ret = input * 1000;
            return ret;
        }


        if ((from.equals(context.getResources().getString(R.string.volumeunitcubicmm)) && to.equals(context.getResources().getString(R.string.volumeunitcubiccm)))) {
            //if((from.equals("cubic mm")) && (to.equals("cubic cm"))){
            double ret = input / 1000;
            return ret;
        }


//        if ((from.equals(mContext.getResources().getString(R.string.volumeunitcubiccm)) && to.equals(mContext.getResources().getString(R.string.volumeunitmillilitres)))) {
//            //if((from.equals("cubic cm")) && (to.equals("millilitre"))){
//            double ret = input;
//            return ret;
//        }
//
//        if ((from.equals(mContext.getResources().getString(R.string.volumeunitmillilitres)) && to.equals(mContext.getResources().getString(R.string.volumeunitcubiccm)))) {
//            //if((from.equals("millilitre")) && (to.equals("cubic cm"))){
//            double ret = input;
//            return ret;
//        }

        if ((from.equals(context.getResources().getString(R.string.volumeunitcubiccm)) && to.equals(context.getResources().getString(R.string.volumeunitcubicfeet)))) {
            //if((from.equals("cubic cm")) && (to.equals("cubic feet"))){
            double ret = input / 28316.84659;
            return ret;
        }


        if ((from.equals(context.getResources().getString(R.string.volumeunitcubicfeet)) && to.equals(context.getResources().getString(R.string.volumeunitcubiccm)))) {
            //if((from.equals("cubic feet")) && (to.equals("cubic cm"))){
            double ret = input * 28316.84659;
            return ret;
        }

//        if ((from.equals(mContext.getResources().getString(R.string.volumeunitcubicmm)) && to.equals(mContext.getResources().getString(R.string.volumeunitmillilitres)))) {
//            //if((from.equals("cubic mm")) && (to.equals("millilitre"))){
//            double ret = input * 0.001;
//            return ret;
//        }
//
//        if ((from.equals(mContext.getResources().getString(R.string.volumeunitmillilitres)) && to.equals(mContext.getResources().getString(R.string.volumeunitcubicmm)))) {
//            //if((from.equals("millilitre")) && (to.equals("cubic mm"))){
//            double ret = input * 1000;
//            return ret;
//        }


        if ((from.equals(context.getResources().getString(R.string.volumeunitcubicmm)) && to.equals(context.getResources().getString(R.string.volumeunitcubicfeet)))) {
            //if((from.equals("cubic mm")) && (to.equals("cubic feet"))){
            double ret = input / 28316846.592;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.volumeunitcubicfeet)) && to.equals(context.getResources().getString(R.string.volumeunitcubicmm)))) {
            //if((from.equals("cubic feet")) && (to.equals("cubic mm"))){
            double ret = input * 28316846.592;
            return ret;
        }

//        if ((from.equals(mContext.getResources().getString(R.string.volumeunitcubicfeet)) && to.equals(mContext.getResources().getString(R.string.volumeunitmillilitres)))) {
//            //if((from.equals("cubic feet")) && (to.equals("millilitre"))){
//            double ret = input * 28316.84659;
//            return ret;
//        }
//
//        if ((from.equals(mContext.getResources().getString(R.string.volumeunitmillilitres)) && to.equals(mContext.getResources().getString(R.string.volumeunitcubicfeet)))) {
//            //if((from.equals("millilitre")) && (to.equals("cubic feet"))){
//            double ret = input / 28316.84659;
//            return ret;
//        }
        if (from.equals(to)) {
            return input;
        }
        return 0;
    }
}
