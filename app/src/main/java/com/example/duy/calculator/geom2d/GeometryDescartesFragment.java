package com.example.duy.calculator.geom2d;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.duy.calculator.version_new_error.AbstractFragment;
import com.example.duy.calculator.R;

public class GeometryDescartesFragment extends AbstractFragment {

    private static final int mCount = 3;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ViewPager mPager = (ViewPager) findViewById(R.id.viewpager);
        mPager.setOffscreenPageLimit(mCount);
        SectionPagerAdapter mAdapter = new SectionPagerAdapter(getChildFragmentManager(), mContext);
        mPager.setAdapter(mAdapter);
        TabLayout mTab = (TabLayout) findViewById(R.id.tab);
        mTab.setupWithViewPager(mPager, true);
    }


    @Override
    protected View getView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.app_bar_geometry_descartes, container, false);
    }

    @Override
    protected void onChangeModeFraction() {

    }


    public static class SectionPagerAdapter extends FragmentPagerAdapter {
        private final Context context;

        public SectionPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FragmentVector();
                case 1:
                    return new FragmentLine();
                case 3:
                    return new FragmentPolygon();
                case 2:
                    return new FragmentCircle();
                case 4:
                    return new FragmentEllipse();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mCount;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Resources resources = context.getResources();
            switch (position) {

                case 0:
                    return resources.getString(R.string.vector);
                case 1:
                    return resources.getString(R.string.line);
                case 2:
                    return resources.getString(R.string.circle);
                case 3:
                    return resources.getString(R.string.polygon);
                case 4:
                    return resources.getString(R.string.ellipse);
                default:
                    return null;
            }
        }
    }
}
