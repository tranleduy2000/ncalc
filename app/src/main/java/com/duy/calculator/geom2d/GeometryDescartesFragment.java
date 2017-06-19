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

package com.duy.calculator.geom2d;

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

import com.duy.calculator.AbstractFragment;
import com.duy.calculator.R;

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
