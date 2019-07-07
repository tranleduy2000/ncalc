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
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.duy.calculator.R;
import com.duy.calculator.symja.activities.DerivativeActivity;
import com.duy.calculator.symja.activities.ExpandAllExpressionActivity;
import com.duy.calculator.symja.activities.FactorExpressionActivity;
import com.duy.calculator.symja.activities.FactorPrimeActivity;
import com.duy.calculator.symja.activities.IdeActivity;
import com.duy.calculator.symja.activities.IntegrateActivity;
import com.duy.calculator.symja.activities.LimitActivity;
import com.duy.calculator.symja.activities.ModuleActivity;
import com.duy.calculator.symja.activities.NumberActivity;
import com.duy.calculator.symja.activities.PermutationActivity;
import com.duy.calculator.symja.activities.PiActivity;
import com.duy.calculator.symja.activities.PrimitiveActivity;
import com.duy.calculator.symja.activities.SimplifyExpressionActivity;
import com.duy.calculator.symja.activities.SolveEquationActivity;
import com.duy.calculator.symja.activities.TrigActivity;
import com.duy.ncalc.calculator.BasicCalculatorActivity;
import com.duy.ncalc.calculator.LogicCalculatorActivity;
import com.duy.ncalc.document.MarkdownListDocumentActivity;
import com.duy.ncalc.document.info.InfoActivity;
import com.duy.ncalc.geom2d.GeometryDescartesActivity;
import com.duy.ncalc.graph.GraphActivity;
import com.duy.ncalc.matrix.MatrixCalculatorActivity;
import com.duy.ncalc.settings.SettingsActivity;
import com.duy.ncalc.systemequations.SystemEquationActivity;
import com.duy.ncalc.unitconverter.UnitCategoryActivity;

import static com.duy.calculator.symja.models.TrigItem.TRIG_TYPE.EXPAND;
import static com.duy.calculator.symja.models.TrigItem.TRIG_TYPE.EXPONENT;
import static com.duy.calculator.symja.models.TrigItem.TRIG_TYPE.REDUCE;

/**
 * Created by Duy on 19/7/2016
 */
public abstract class NavDrawerActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    protected final Handler handler = new Handler();
    protected DrawerLayout mDrawerLayout;

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

    public void closeDrawer() {
        mDrawerLayout.closeDrawers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        setupHeaderNavigation(navigationView);
    }

    private void setupHeaderNavigation(NavigationView navigationView) {
        View header = navigationView.getHeaderView(0);
        header.findViewById(R.id.img_help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
                startActivity(new Intent(getApplicationContext(), MarkdownListDocumentActivity.class));
            }
        });
        header.findViewById(R.id.img_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        closeDrawer();
        Intent intent;
        switch (id) {
            case R.id.action_all_functions: {
                intent = new Intent(getApplicationContext(), MarkdownListDocumentActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.action_ide_mode:
                intent = new Intent(getApplicationContext(), IdeActivity.class);
                postStartActivity(intent);
                break;
            case R.id.nav_sci_calc:
                intent = new Intent(getApplicationContext(), BasicCalculatorActivity.class);
                postStartActivity(intent);
                break;
            case R.id.nav_graph:
                intent = new Intent(getApplicationContext(), GraphActivity.class);
                postStartActivity(intent);
                break;
            case R.id.nav_unit:
                intent = new Intent(getApplicationContext(), UnitCategoryActivity.class);
                postStartActivity(intent);
                break;
            case R.id.nav_base:
                intent = new Intent(getApplicationContext(), LogicCalculatorActivity.class);
                postStartActivity(intent);
                break;
            case R.id.nav_geometric_descartes:
                intent = new Intent(getApplicationContext(), GeometryDescartesActivity.class);
                postStartActivity(intent);
                break;
            case R.id.nav_setting:
                intent = new Intent(getApplicationContext(), SettingsActivity.class);
                postStartActivity(intent);
                break;
            case R.id.nav_about_app:
                intent = new Intent(getApplicationContext(), InfoActivity.class);
                postStartActivity(intent);
                break;
            case R.id.nav_matrix:
                intent = new Intent(getApplicationContext(), MatrixCalculatorActivity.class);
                postStartActivity(intent);
                break;
            case R.id.system_equation:
                intent = new Intent(getApplicationContext(), SystemEquationActivity.class);
                postStartActivity(intent);
                break;
            case R.id.nav_solve_equation:
                intent = new Intent(getApplicationContext(), SolveEquationActivity.class);
                postStartActivity(intent);
                break;
            case R.id.nav_simplify_equation:
                intent = new Intent(getApplicationContext(), SimplifyExpressionActivity.class);
                postStartActivity(intent);
                break;
            case R.id.nav_factor_equation:
                intent = new Intent(getApplicationContext(), FactorExpressionActivity.class);
                postStartActivity(intent);
                break;
            case R.id.nav_derivitive:
                intent = new Intent(getApplicationContext(), DerivativeActivity.class);
                postStartActivity(intent);
                break;
            case R.id.nav_expand_binomial:
                intent = new Intent(getApplicationContext(), ExpandAllExpressionActivity.class);
                postStartActivity(intent);
                break;
            case R.id.nav_limit:
                intent = new Intent(getApplicationContext(), LimitActivity.class);
                postStartActivity(intent);
                break;
            case R.id.nav_integrate:
                intent = new Intent(getApplicationContext(), IntegrateActivity.class);
                postStartActivity(intent);
                break;
            case R.id.nav_primitive:
                intent = new Intent(getApplicationContext(), PrimitiveActivity.class);
                postStartActivity(intent);
                break;
            case R.id.nav_rate:
                gotoPlayStore();
                break;
            case R.id.nav_prime_factor:
                intent = new Intent(getApplicationContext(), FactorPrimeActivity.class);
                postStartActivity(intent);
                break;
            case R.id.nav_mod:
                intent = new Intent(getApplicationContext(), ModuleActivity.class);
                postStartActivity(intent);
                break;
            case R.id.nav_trig_expand:
                intent = new Intent(getApplicationContext(), TrigActivity.class);
                intent.putExtra(TrigActivity.TYPE, EXPAND);
                postStartActivity(intent);
                break;
            case R.id.nav_trig_reduce:
                intent = new Intent(getApplicationContext(), TrigActivity.class);
                intent.putExtra(TrigActivity.TYPE, REDUCE);
                postStartActivity(intent);
                break;
            case R.id.nav_trig_to_exp:
                intent = new Intent(getApplicationContext(), TrigActivity.class);
                intent.putExtra(TrigActivity.TYPE, EXPONENT);
                postStartActivity(intent);
                break;
            case R.id.nav_permutation:
                intent = new Intent(getApplicationContext(), PermutationActivity.class);
                if (this instanceof PermutationActivity) {
                    finish();
                }
                intent.putExtra(PermutationActivity.TYPE_NUMBER, PermutationActivity.TYPE_PERMUTATION);
                postStartActivity(intent);
                break;
            case R.id.nav_combination:
                intent = new Intent(getApplicationContext(), PermutationActivity.class);
                if (this instanceof PermutationActivity) {
                    finish();
                }
                intent.putExtra(PermutationActivity.TYPE_NUMBER, PermutationActivity.TYPE_COMBINATION);
                postStartActivity(intent);
                break;
            case R.id.nav_catalan:
                intent = new Intent(getApplicationContext(), NumberActivity.class);
                if (this instanceof NumberActivity) {
                    finish();
                }
                intent.putExtra(NumberActivity.DATA, NumberActivity.NumberType.CATALAN);
                postStartActivity(intent);
                break;
            case R.id.nav_fibo:
                intent = new Intent(getApplicationContext(), NumberActivity.class);
                if (this instanceof NumberActivity) {
                    finish();
                }
                intent.putExtra(NumberActivity.DATA, NumberActivity.NumberType.FIBONACCI);
                postStartActivity(intent);
                break;
            case R.id.nav_prime:
                intent = new Intent(getApplicationContext(), NumberActivity.class);
                if (this instanceof NumberActivity) {
                    finish();
                }
                intent.putExtra(NumberActivity.DATA, NumberActivity.NumberType.PRIME);
                postStartActivity(intent);
                break;
            case R.id.action_divisors:
                intent = new Intent(getApplicationContext(), NumberActivity.class);
                if (this instanceof NumberActivity) {
                    finish();
                }
                intent.putExtra(NumberActivity.DATA, NumberActivity.NumberType.DIVISORS);
                postStartActivity(intent);
                break;
            case R.id.action_pi_number:
                intent = new Intent(getApplicationContext(), PiActivity.class);
                postStartActivity(intent);
                break;
        }
        return true;
    }

    private void postStartActivity(final Intent intent) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
            }
        }, 100);
    }

}
