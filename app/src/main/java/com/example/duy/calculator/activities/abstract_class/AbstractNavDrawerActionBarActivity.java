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

package com.example.duy.calculator.activities.abstract_class;

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

import com.example.duy.calculator.R;
import com.example.duy.calculator.helper.HelperActivity;
import com.example.duy.calculator.helper.InfoActivity;
import com.example.duy.calculator.utils.ConfigApp;
import com.example.duy.calculator.activities.BaseCalculatorActivity;
import com.example.duy.calculator.activities.BasicCalculatorActivity;
import com.example.duy.calculator.activities.DerivativeActivity;
import com.example.duy.calculator.activities.ExpandAllExpressionActivity;
import com.example.duy.calculator.activities.FactorExpressionActivity;
import com.example.duy.calculator.activities.IntegrateActivity;
import com.example.duy.calculator.activities.LimitActivity;
import com.example.duy.calculator.activities.MatrixCalculatorActivity;
import com.example.duy.calculator.activities.PrimitiveActivity;
import com.example.duy.calculator.activities.SimplifyEquationActivity;
import com.example.duy.calculator.activities.SolveEquationActivity;
import com.example.duy.calculator.activities.StatisticActivity;
import com.example.duy.calculator.core.converter.UnitConverterParentAcitvity;
import com.example.duy.calculator.core.geom2d.GeometryDescartesActivity;
import com.example.duy.calculator.core.graph.GraphActivity;
import com.example.duy.calculator.core.number_theory.FactorPrimeActivity;
import com.example.duy.calculator.core.number_theory.ModuleActivity;
import com.example.duy.calculator.core.number_theory.NumberActivity;
import com.example.duy.calculator.core.number_theory.NumberType;
import com.example.duy.calculator.core.number_theory.PermutationActivity;
import com.example.duy.calculator.core.settings.SettingsActivity;
import com.example.duy.calculator.core.system_equation.SystemEquationActivity;
import com.example.duy.calculator.core.trigonometry.TrigActivity;

import static com.example.duy.calculator.item_math_type.TrigItem.TRIG_TYPE.EXPAND;
import static com.example.duy.calculator.item_math_type.TrigItem.TRIG_TYPE.EXPONENT;
import static com.example.duy.calculator.item_math_type.TrigItem.TRIG_TYPE.REDUCE;

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
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
                mDrawerLayout.closeDrawer(GravityCompat.START);
            else super.onBackPressed();
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
                startActivity(new Intent(getApplicationContext(), HelperActivity.class));
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
                rateApp();
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
        if (id == R.id.nav_sci_calc) {
            intent = new Intent(getApplicationContext(), BasicCalculatorActivity.class);
            startIntent(intent);
        } else if (id == R.id.nav_graph) {
            intent = new Intent(getApplicationContext(), GraphActivity.class);
            startIntent(intent);
        } else if (id == R.id.nav_unit) {
            intent = new Intent(getApplicationContext(), UnitConverterParentAcitvity.class);
            startIntent(intent);
        } else if (id == R.id.nav_base) {
            intent = new Intent(getApplicationContext(), BaseCalculatorActivity.class);
            startIntent(intent);
        } else if (id == R.id.nav_geometric_descartes) {
            intent = new Intent(getApplicationContext(), GeometryDescartesActivity.class);
            startIntent(intent);
        } else if (id == R.id.nav_setting) {
            intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startIntent(intent);
        } else if (id == R.id.nav_about_app) {
            intent = new Intent(getApplicationContext(), InfoActivity.class);
            startIntent(intent);
        } else if (id == R.id.nav_matrix) {
            intent = new Intent(getApplicationContext(), MatrixCalculatorActivity.class);
            startIntent(intent);
        } else if (id == R.id.system_equation) {
            intent = new Intent(getApplicationContext(), SystemEquationActivity.class);
            startIntent(intent);
        } else if (id == R.id.nav_solve_equation) {
            intent = new Intent(getApplicationContext(), SolveEquationActivity.class);
            startIntent(intent);
        } else if (id == R.id.nav_simplify_equation) {
            intent = new Intent(getApplicationContext(), SimplifyEquationActivity.class);
            startIntent(intent);
        } else if (id == R.id.nav_factor_equation) {
            intent = new Intent(getApplicationContext(), FactorExpressionActivity.class);
            startIntent(intent);
        } else if (id == R.id.nav_derivitive) {
            intent = new Intent(getApplicationContext(), DerivativeActivity.class);
            startIntent(intent);
        } else if (id == R.id.nav_table) {
            intent = new Intent(getApplicationContext(), StatisticActivity.class);
            startIntent(intent);
        } else if (id == R.id.nav_expand_binomial) {
            intent = new Intent(getApplicationContext(), ExpandAllExpressionActivity.class);
            startIntent(intent);
        } else if (id == R.id.nav_limit) {
            intent = new Intent(getApplicationContext(), LimitActivity.class);
            startIntent(intent);
        } else if (id == R.id.nav_integrate) {
            intent = new Intent(getApplicationContext(), IntegrateActivity.class);
            startIntent(intent);
        } else if (id == R.id.nav_primitive) {
            intent = new Intent(getApplicationContext(), PrimitiveActivity.class);
            startIntent(intent);
        } else if (id == R.id.nav_rate) {
            rateApp();
        } else if (id == R.id.nav_prime_factor) {
            intent = new Intent(getApplicationContext(), FactorPrimeActivity.class);
            startIntent(intent);
        } else if (id == R.id.nav_mod) {
            intent = new Intent(getApplicationContext(), ModuleActivity.class);
            startIntent(intent);
        } else if (id == R.id.nav_trig_expand) {
            intent = new Intent(getApplicationContext(), TrigActivity.class);
            intent.putExtra(TrigActivity.TYPE, EXPAND);
            startIntent(intent);
        } else if (id == R.id.nav_trig_reduce) {
            intent = new Intent(getApplicationContext(), TrigActivity.class);
            intent.putExtra(TrigActivity.TYPE, REDUCE);
            startIntent(intent);
        } else if (id == R.id.nav_trig_to_exp) {
            intent = new Intent(getApplicationContext(), TrigActivity.class);
            intent.putExtra(TrigActivity.TYPE, EXPONENT);
            startIntent(intent);
        } else if (id == R.id.nav_permutation) {
            intent = new Intent(getApplicationContext(), PermutationActivity.class);
            intent.putExtra(PermutationActivity.TYPE_NUMBER, PermutationActivity.TYPE_PERMUTATION);
            startIntent(intent);
        } else if (id == R.id.nav_combination) {
            intent = new Intent(getApplicationContext(), PermutationActivity.class);
            intent.putExtra(PermutationActivity.TYPE_NUMBER, PermutationActivity.TYPE_COMBINATION);
            startIntent(intent);
        }
        switch (id) {
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
        }
        return true;
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
