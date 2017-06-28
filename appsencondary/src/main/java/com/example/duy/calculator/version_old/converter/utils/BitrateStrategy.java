package com.example.duy.calculator.version_old.converter.utils;

import android.content.Context;

import com.example.duy.calculator.R;

/**
 * Created by tranleduy on 27-May-16.
 */
public class BitrateStrategy implements Strategy {
    final int BIT_RATE_1 = 1000;
    final int BIT_RATE_2 = 1024;
    private final Context context;

    public BitrateStrategy(Context context) {
        this.context = context;
    }


    @Override
    public String getUnitDefault() {
        return context.getResources().getString(R.string.bitrateunitbytes);
    }

    @Override
    public double Convert(String from, String to, double input) {

        if ((from.equals(context.getResources().getString(R.string.bitrateunitbytes))) &&
                (to.equals(context.getResources().getString(R.string.bitrateunitbytes)))) {
            return input;
        }

        if ((from.equals(context.getResources().getString(R.string.bitrateunitbytes))) &&
                (to.equals(context.getResources().getString(R.string.bitrateunitkilobytes)))) {
            double ret = input / BIT_RATE_1;
            return ret;
        }


        if ((from.equals(context.getResources().getString(R.string.bitrateunitbytes))) &&
                (to.equals(context.getResources().getString(R.string.bitrateunitmegabytes)))) {
            double ret = input / BIT_RATE_1 / BIT_RATE_1;
            return ret;
        }


        if ((from.equals(context.getResources().getString(R.string.bitrateunitbytes))) &&
                (to.equals(context.getResources().getString(R.string.bitrateunitgigabytes)))) {
            double ret = input / BIT_RATE_1 / BIT_RATE_1 / BIT_RATE_1;
            return ret;
        }


        if ((from.equals(context.getResources().getString(R.string.bitrateunitbytes))) &&
                (to.equals(context.getResources().getString(R.string.bitrateunitterabytes)))) {
            double ret = input / BIT_RATE_1 / BIT_RATE_1 / BIT_RATE_1 / BIT_RATE_1;
            return ret;
        }


        if ((from.equals(context.getResources().getString(R.string.bitrateunitbytes))) &&
                (to.equals(context.getResources().getString(R.string.bitrateunitbits)))) {
            double ret = input * 8;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.bitrateunitbytes))) &&
                (to.equals(context.getResources().getString(R.string.bitrateunitkilobits)))) {
            double ret = input * 8 / BIT_RATE_1;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.bitrateunitbytes))) &&
                (to.equals(context.getResources().getString(R.string.bitrateunitmegabits)))) {
            double ret = input * 8 / BIT_RATE_1 / BIT_RATE_1;
            return ret;
        }
        if ((from.equals(context.getResources().getString(R.string.bitrateunitbytes))) &&
                (to.equals(context.getResources().getString(R.string.bitrateunitgigabits)))) {
            double ret = input * 8 / BIT_RATE_1 / BIT_RATE_1;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.bitrateunitbytes))) &&
                (to.equals(context.getResources().getString(R.string.bitrateunitterabits)))) {
            double ret = input * 8 / BIT_RATE_1 / BIT_RATE_1;
            return ret;
        }

        if ((from.equals(context.getResources().getString(R.string.bitrateunitkilobytes))) &&
                (to.equals(context.getResources().getString(R.string.bitrateunitbytes)))) {
            double ret = input * BIT_RATE_1;
            return ret;
        }


        if ((from.equals(context.getResources().getString(R.string.bitrateunitmegabytes))) &&
                (to.equals(context.getResources().getString(R.string.bitrateunitbytes)))) {
            double ret = input * BIT_RATE_1 * BIT_RATE_1;
            return ret;
        }


        if ((from.equals(context.getResources().getString(R.string.bitrateunitgigabytes))) &&
                (to.equals(context.getResources().getString(R.string.bitrateunitbytes)))) {
            double ret = input * BIT_RATE_1 * BIT_RATE_1 * BIT_RATE_1;
            return ret;
        }


        if ((from.equals(context.getResources().getString(R.string.bitrateunitterabytes))) &&
                (to.equals(context.getResources().getString(R.string.bitrateunitbytes)))) {
            double ret = input * BIT_RATE_1 * BIT_RATE_1 * BIT_RATE_1 * BIT_RATE_1;
            return ret;
        }


        if ((from.equals(context.getResources().getString(R.string.bitrateunitbits))) &&
                (to.equals(context.getResources().getString(R.string.bitrateunitbytes)))) {
            double ret = input / 8;
            return ret;
        }


        if ((from.equals(context.getResources().getString(R.string.bitrateunitmegabits))) &&
                (to.equals(context.getResources().getString(R.string.bitrateunitbytes)))) {
            double ret = input / 8 * BIT_RATE_1 * BIT_RATE_1;
            return ret;
        }


        if ((from.equals(context.getResources().getString(R.string.bitrateunitkilobits))) &&
                (to.equals(context.getResources().getString(R.string.bitrateunitbytes)))) {
            double ret = input / 8 * BIT_RATE_1;
            return ret;
        }


        if ((from.equals(context.getResources().getString(R.string.bitrateunitgigabits))) &&
                (to.equals(context.getResources().getString(R.string.bitrateunitbytes)))) {
            double ret = input / 8 * BIT_RATE_1 * BIT_RATE_1 * BIT_RATE_1;
            return ret;
        }


        if ((from.equals(context.getResources().getString(R.string.bitrateunitterabits))) &&
                (to.equals(context.getResources().getString(R.string.bitrateunitbytes)))) {
            double ret = input / 8 * BIT_RATE_1 * BIT_RATE_1 * BIT_RATE_1 * BIT_RATE_1;
            return ret;
        }
        return 0;
    }
}
