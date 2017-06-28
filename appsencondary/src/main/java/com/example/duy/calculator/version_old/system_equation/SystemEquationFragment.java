package com.example.duy.calculator.version_old.system_equation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.duy.calculator.R;
import com.example.duy.calculator.AbstractFragment;


/**
 * Created by DUy on 18-Nov-16.
 */

public class SystemEquationFragment extends AbstractFragment {
    private static final String STARTED = SystemEquationFragment.class.getName() + "started";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);

        SectionPagerAdapter adapter = new SectionPagerAdapter(getChildFragmentManager(), getActivity());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected View getView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.app_bar_linear_system, container, false);
    }

    @Override
    protected void onChangeModeFraction() {

    }
}
