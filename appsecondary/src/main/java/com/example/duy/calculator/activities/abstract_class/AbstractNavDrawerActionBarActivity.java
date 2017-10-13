package com.example.duy.calculator.activities.abstract_class;

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

import com.example.duy.calculator.AbstractAppCompatActivity;
import com.example.duy.calculator.DLog;
import com.example.duy.calculator.R;
import com.example.duy.calculator.SettingsActivity;
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
import com.example.duy.calculator.converter.UnitConverterParentAcitvity;
import com.example.duy.calculator.document.DocumentActivity;
import com.example.duy.calculator.document.InfoActivity;
import com.example.duy.calculator.geom2d.GeometryDescartesActivity;
import com.example.duy.calculator.graph.GraphActivity;
import com.example.duy.calculator.hand_write.HandCalculatorActivity;
import com.example.duy.calculator.hand_write.HandGeometryActivity;
import com.example.duy.calculator.number_theory.FactorPrimeActivity;
import com.example.duy.calculator.number_theory.ModuleActivity;
import com.example.duy.calculator.number_theory.NumberActivity;
import com.example.duy.calculator.number_theory.NumberType;
import com.example.duy.calculator.number_theory.PermutationActivity;
import com.example.duy.calculator.system_equation.SystemEquationActivity;
import com.example.duy.calculator.trigonometry.TRIG_TYPE;
import com.example.duy.calculator.trigonometry.TrigActivity;
import com.example.duy.calculator.utils.ConfigApp;

/**
 * Created by Duy on 19/7/2016
 */
public abstract class AbstractNavDrawerActionBarActivity extends AbstractAppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private boolean debug = ConfigApp.DEBUG;
    private Handler handler = new Handler();

    /**
     * call on user click back
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout != null) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START))
                drawerLayout.closeDrawer(GravityCompat.START);
            else super.onBackPressed();
        } else
            super.onBackPressed();
    }

    /**
     * close drawer when user click on history
     * and close when user click item navigation
     */
    public void closeDrawer() {
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        setupHeaderNavigation(navigationView);
    }

    private void setupHeaderNavigation(NavigationView navigationView) {
        View header = navigationView.getHeaderView(0);
        header.findViewById(R.id.img_help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DocumentActivity.class));
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
                shareApp();
            }
        });
        header.findViewById(R.id.img_rate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateApp();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        DLog.d(TAG, "onNavigationItemSelected: " + item.getTitle());
        closeDrawer();
        Intent intent;
        if (id == R.id.nav_sci_calc) {
            intent = new Intent(this, BasicCalculatorActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_graph) {
            intent = new Intent(this, GraphActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_hand_calc) {
            intent = new Intent(this, HandCalculatorActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_hand_geo) {
            intent = new Intent(this, HandGeometryActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_unit) {
            intent = new Intent(this, UnitConverterParentAcitvity.class);
            startActivity(intent);
        } else if (id == R.id.nav_base) {
            intent = new Intent(this, BaseCalculatorActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_geometric_descartes) {
            intent = new Intent(this, GeometryDescartesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_setting) {
            intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_about_app) {
            intent = new Intent(getApplicationContext(), InfoActivity.class);
            startIntent(intent);
        }
        else if (id == R.id.nav_matrix) {
            intent = new Intent(this, MatrixCalculatorActivity.class);
            startActivity(intent);
        } else if (id == R.id.system_equation) {
            intent = new Intent(this, SystemEquationActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_solve_equation) {
            intent = new Intent(this, SolveEquationActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_simplify_equation) {
            intent = new Intent(this, SimplifyEquationActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_factor_equation) {
            intent = new Intent(this, FactorExpressionActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_derivitive) {
            intent = new Intent(this, DerivativeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_table) {
            intent = new Intent(this, StatisticActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_expand_binomial) {
            intent = new Intent(this, ExpandAllExpressionActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_limit) {
            intent = new Intent(this, LimitActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_integrate) {
            intent = new Intent(this, IntegrateActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_primitive) {
            intent = new Intent(this, PrimitiveActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_rate) {
            rateApp();
        } else if (id == R.id.nav_prime_factor) {
            intent = new Intent(this, FactorPrimeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_mod) {
            intent = new Intent(this, ModuleActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_trig_expand) {
            intent = new Intent(this, TrigActivity.class);
            intent.putExtra(TrigActivity.DATA, TRIG_TYPE.EXPAND);
            startIntent(intent);
        } else if (id == R.id.nav_trig_reduce) {
            intent = new Intent(this, TrigActivity.class);
            intent.putExtra(TrigActivity.DATA, TRIG_TYPE.REDUCE);
            startIntent(intent);
        } else if (id == R.id.nav_trig_to_exp) {
            intent = new Intent(this, TrigActivity.class);
            intent.putExtra(TrigActivity.DATA, TRIG_TYPE.EXPONENT);
            startIntent(intent);
        } else if (id == R.id.nav_permutation) {
            intent = new Intent(this, PermutationActivity.class);
            intent.putExtra(PermutationActivity.TYPE_NUMBER, PermutationActivity.TYPE_PERMUTATION);
            intent.putExtra(PermutationActivity.TAG, PermutationActivity.TAG);
            startIntent(intent);
        } else if (id == R.id.nav_combination) {
            intent = new Intent(this, PermutationActivity.class);
            intent.putExtra(PermutationActivity.TYPE_NUMBER, PermutationActivity.TYPE_COMBINATION);
            intent.putExtra(PermutationActivity.TAG, PermutationActivity.TAG);
            startIntent(intent);
        } else if (id == R.id.nav_catalan) {
            intent = new Intent(this, NumberActivity.class);
            intent.putExtra(NumberActivity.DATA, NumberType.CATALAN);
            intent.putExtra(NumberActivity.TAG, NumberActivity.TAG);
            startIntent(intent);

        } else if (id == R.id.nav_fibo) {
            intent = new Intent(this, NumberActivity.class);
            intent.putExtra(NumberActivity.DATA, NumberType.FIBONACCI);
            intent.putExtra(NumberActivity.TAG, NumberActivity.TAG);

            startIntent(intent);

        } else if (id == R.id.nav_prime) {
            intent = new Intent(this, NumberActivity.class);
            intent.putExtra(NumberActivity.DATA, NumberType.PRIME);
            intent.putExtra(NumberActivity.TAG, NumberActivity.TAG);

            startIntent(intent);

        } else if (id == R.id.action_document) {
            intent = new Intent(getApplicationContext(), DocumentActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_natural_calc) {
//            intent = new Intent(getApplicationContext(), CalculatorActivity.class);
//            startActivity(intent);
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
}
