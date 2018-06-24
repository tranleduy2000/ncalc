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

package com.duy.calculator.activities.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.duy.calculator.R;
import com.duy.calculator.calc.BasicCalculatorActivity;
import com.duy.calculator.activities.DerivativeActivity;
import com.duy.calculator.activities.ExpandAllExpressionActivity;
import com.duy.calculator.activities.FactorExpressionActivity;
import com.duy.calculator.document.fragment.GettingStartedFragment;
import com.duy.calculator.activities.IntegrateActivity;
import com.duy.calculator.activities.LimitActivity;
import com.duy.calculator.calc.LogicCalculatorActivity;
import com.duy.calculator.activities.PrimitiveActivity;
import com.duy.calculator.activities.SimplifyEquationActivity;
import com.duy.calculator.activities.SolveEquationActivity;
import com.duy.calculator.converter.UnitCategoryActivity;
import com.duy.calculator.deprecated.StatisticActivity;
import com.duy.calculator.document.activities.DocumentActivity;
import com.duy.calculator.document.activities.InfoActivity;
import com.duy.calculator.geom2d.GeometryDescartesActivity;
import com.duy.calculator.graph.GraphActivity;
import com.duy.calculator.matrix.MatrixCalculatorActivity;
import com.duy.calculator.number.FactorPrimeActivity;
import com.duy.calculator.number.ModuleActivity;
import com.duy.calculator.number.NumberActivity;
import com.duy.calculator.number.NumberType;
import com.duy.calculator.number.PermutationActivity;
import com.duy.calculator.number.PiActivity;
import com.duy.calculator.settings.SettingsActivity;
import com.duy.calculator.equations.SystemEquationActivity;
import com.duy.calculator.trigonometry.TrigActivity;
import com.duy.calculator.utils.ConfigApp;

import static com.duy.calculator.model.TrigItem.TRIG_TYPE.EXPAND;
import static com.duy.calculator.model.TrigItem.TRIG_TYPE.EXPONENT;
import static com.duy.calculator.model.TrigItem.TRIG_TYPE.REDUCE;

/**
 * Created by Duy on 19/7/2016
 */
public abstract class AbstractNavDrawerActionBarActivity extends AbstractAppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener {
    protected DrawerLayout mDrawerLayout;
    private boolean debug = ConfigApp.DEBUG;
    private Handler handler = new Handler();

    /**
     * call on user click back
     */
    @Override
    public void onBackPressed() {
        if (mDrawerLayout != null) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        } else
            super.onBackPressed();
    }

    /**
     * close drawer when user click on history
     * and close when user click item navigation
     */
    public void closeDrawer() {
        mDrawerLayout.closeDrawers();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        mDrawerLayout.addDrawerListener(this);

        setupHeaderNavigation(navigationView);
    }

    private void setupHeaderNavigation(NavigationView navigationView) {
        View header = navigationView.getHeaderView(0);
        header.findViewById(R.id.img_help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
                startActivity(new Intent(getApplicationContext(), DocumentActivity.class));
            }
        });
        header.findViewById(R.id.img_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "setLocale: default");
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            }
        });
        header.findViewById(R.id.img_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
                shareApp();
            }
        });
        header.findViewById(R.id.img_rate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
                gotoPlayStore();
            }
        });
        header.findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
            }
        });
    }

    /**
     * event for item navigation click
     *
     * @param item - item
     * @return boolean
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        closeDrawer();
        Intent intent;
        switch (id) {
            case R.id.nav_sci_calc:
                intent = new Intent(getApplicationContext(), BasicCalculatorActivity.class);
                startIntent(intent);
                break;
            case R.id.nav_graph:
                intent = new Intent(getApplicationContext(), GraphActivity.class);
                startIntent(intent);
                break;
            case R.id.nav_unit:
                intent = new Intent(getApplicationContext(), UnitCategoryActivity.class);
                startIntent(intent);
                break;
            case R.id.nav_base:
                intent = new Intent(getApplicationContext(), LogicCalculatorActivity.class);
                startIntent(intent);
                break;
            case R.id.nav_geometric_descartes:
                intent = new Intent(getApplicationContext(), GeometryDescartesActivity.class);
                startIntent(intent);
                break;
            case R.id.nav_setting:
                intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startIntent(intent);
                break;
            case R.id.nav_about_app:
                intent = new Intent(getApplicationContext(), InfoActivity.class);
                startIntent(intent);
                break;
            case R.id.nav_matrix:
                intent = new Intent(getApplicationContext(), MatrixCalculatorActivity.class);
                startIntent(intent);
                break;
            case R.id.system_equation:
                intent = new Intent(getApplicationContext(), SystemEquationActivity.class);
                startIntent(intent);
                break;
            case R.id.nav_solve_equation:
                intent = new Intent(getApplicationContext(), SolveEquationActivity.class);
                startIntent(intent);
                break;
            case R.id.nav_simplify_equation:
                intent = new Intent(getApplicationContext(), SimplifyEquationActivity.class);
                startIntent(intent);
                break;
            case R.id.nav_factor_equation:
                intent = new Intent(getApplicationContext(), FactorExpressionActivity.class);
                startIntent(intent);
                break;
            case R.id.nav_derivitive:
                intent = new Intent(getApplicationContext(), DerivativeActivity.class);
                startIntent(intent);
                break;
            case R.id.nav_table:
                intent = new Intent(getApplicationContext(), StatisticActivity.class);
                startIntent(intent);
                break;
            case R.id.nav_expand_binomial:
                intent = new Intent(getApplicationContext(), ExpandAllExpressionActivity.class);
                startIntent(intent);
                break;
            case R.id.nav_limit:
                intent = new Intent(getApplicationContext(), LimitActivity.class);
                startIntent(intent);
                break;
            case R.id.nav_integrate:
                intent = new Intent(getApplicationContext(), IntegrateActivity.class);
                startIntent(intent);
                break;
            case R.id.nav_primitive:
                intent = new Intent(getApplicationContext(), PrimitiveActivity.class);
                startIntent(intent);
                break;
            case R.id.nav_rate:
                gotoPlayStore();
                break;
            case R.id.nav_prime_factor:
                intent = new Intent(getApplicationContext(), FactorPrimeActivity.class);
                startIntent(intent);
                break;
            case R.id.nav_mod:
                intent = new Intent(getApplicationContext(), ModuleActivity.class);
                startIntent(intent);
                break;
            case R.id.nav_trig_expand:
                intent = new Intent(getApplicationContext(), TrigActivity.class);
                intent.putExtra(TrigActivity.TYPE, EXPAND);
                startIntent(intent);
                break;
            case R.id.nav_trig_reduce:
                intent = new Intent(getApplicationContext(), TrigActivity.class);
                intent.putExtra(TrigActivity.TYPE, REDUCE);
                startIntent(intent);
                break;
            case R.id.nav_trig_to_exp:
                intent = new Intent(getApplicationContext(), TrigActivity.class);
                intent.putExtra(TrigActivity.TYPE, EXPONENT);
                startIntent(intent);
                break;
            case R.id.nav_permutation:
                intent = new Intent(getApplicationContext(), PermutationActivity.class);
                intent.putExtra(PermutationActivity.TYPE_NUMBER, PermutationActivity.TYPE_PERMUTATION);
                startIntent(intent);
                break;
            case R.id.nav_combination:
                intent = new Intent(getApplicationContext(), PermutationActivity.class);
                intent.putExtra(PermutationActivity.TYPE_NUMBER, PermutationActivity.TYPE_COMBINATION);
                startIntent(intent);
                break;
            case R.id.nav_catalan:
                intent = new Intent(getApplicationContext(), NumberActivity.class);
                intent.putExtra(NumberActivity.DATA, NumberType.CATALAN);
                startIntent(intent);

                break;
            case R.id.nav_fibo:
                intent = new Intent(getApplicationContext(), NumberActivity.class);
                intent.putExtra(NumberActivity.DATA, NumberType.FIBONACCI);
                startIntent(intent);
                break;
            case R.id.nav_prime:
                intent = new Intent(getApplicationContext(), NumberActivity.class);
                intent.putExtra(NumberActivity.DATA, NumberType.PRIME);
                startIntent(intent);
                break;
            case R.id.action_divisors:
                intent = new Intent(getApplicationContext(), NumberActivity.class);
                intent.putExtra(NumberActivity.DATA, NumberType.DIVISORS);
                startIntent(intent);
                break;
            case R.id.action_pi_number:
                intent = new Intent(getApplicationContext(), PiActivity.class);
                startIntent(intent);
                break;
            case R.id.action_document:
                intent = new Intent(getApplicationContext(), DocumentActivity.class);
                startActivity(intent);
                break;
            case R.id.action_getting_started:
                showFragmentGettingStarted();
                break;
        }
        return true;
    }

    private void showFragmentGettingStarted() {
        GettingStartedFragment fragment = GettingStartedFragment.newInstance();
        fragment.show(getSupportFragmentManager(), GettingStartedFragment.TAG);
    }


    private void startIntent(final Intent intent) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
            }
        }, 100);
    }

    /**
     * open drawer left, menu of math
     */
    protected void openMenuDrawer() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout != null) {
            if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        }
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
        hideKeyboard(null);
    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }
}
