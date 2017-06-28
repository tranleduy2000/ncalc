package com.example.duy.calculator.version_old.geom2d;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.duy.calculator.R;
import com.example.duy.calculator.geom2d.FragmentCircle;
import com.example.duy.calculator.geom2d.FragmentEllipse;
import com.example.duy.calculator.geom2d.FragmentLine;
import com.example.duy.calculator.geom2d.FragmentPolygon;
import com.example.duy.calculator.version_old.activities.abstract_class.AbstractNavDrawerActionBarActivity;

public class GeometryDescartesActivity extends AbstractNavDrawerActionBarActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, ViewPager.OnPageChangeListener {

    private static final int mCount = 3;
    private ViewPager mPager;
    private TabLayout mTab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geometry_descartes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mPager = (ViewPager) findViewById(R.id.viewpager);
        mPager.setOffscreenPageLimit(mCount);
        PagerApdater mAdapter = new PagerApdater(getSupportFragmentManager(), this);
        mPager.setAdapter(mAdapter);
        mPager.addOnPageChangeListener(this);

        mTab = (TabLayout) findViewById(R.id.tab);
        mTab.setupWithViewPager(mPager, true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        super.onNavigationItemSelected(item);
        return true;
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    static class PagerApdater extends FragmentPagerAdapter {
        private final Context context;

        public PagerApdater(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new com.example.duy.calculator.geom2d.FragmentVector();
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
