package com.example.duy.calculator.version_old.system_equation;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.duy.calculator.R;

public class SectionPagerAdapter extends FragmentPagerAdapter {
    private static final int mCount = 2;
    private Activity activity;

    public SectionPagerAdapter(FragmentManager fm, Activity activity) {
        super(fm);
        this.activity = activity;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return new UndefineSystemEquationFragment();
            case 0:
                return new DefineSystemEquationFragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return activity.getString(R.string.input_matrix);
            case 1:
                return activity.getString(R.string.input_equation_);
        }
        return "";
    }

    @Override
    public int getCount() {
        return mCount;
    }
}