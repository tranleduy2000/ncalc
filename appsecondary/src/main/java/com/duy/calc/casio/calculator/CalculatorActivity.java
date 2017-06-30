/*
 * Copyright (c) 2017 by Tran Le Duy
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

package com.duy.calc.casio.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;

import com.duy.calc.casio.activities.AbstractActivity;
import com.duy.calc.casio.constant.ConstantFragment;
import com.duy.calc.casio.settings.SettingsActivity;
import com.duy.calc.casio.token.ConstantToken;
import com.example.duy.calculator.R;

public class CalculatorActivity extends AbstractActivity implements ConstantFragment.ConstantSelectListener, NavigationView.OnNavigationItemSelectedListener {

    public static final int REQUEST_CODE_SETTING = 1001;
    public static final int REQUEST_CODE_HISTORY = 1002;

    private CalculatorPresenter mPresenter;
    private DisplayFragment mDisplayFragment;
    private KeyboardFragment mKeyboardFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calc);
        if (savedInstanceState != null) {
            return;
        }

        mDisplayFragment = DisplayFragment.newInstance();
        mKeyboardFragment = KeyboardFragment.newInstance();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_display, mDisplayFragment).commit();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, mKeyboardFragment).commit();

        mPresenter = new CalculatorPresenter(this, mDisplayFragment, mKeyboardFragment);

        showAdView();
        setupNavigationView();
    }

    private void setupNavigationView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void showAdView() {

    }


    /**
     * When the settings button has been pressed.
     *
     * @param v Not Used
     */
    public void clickSettings(View v) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public int getFontSize() {
        return 64;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SETTING) {
            mPresenter.updateSettings();
        }
    }

    @Override
    public void onConstantSelected(ConstantToken constantToken) {
        mPresenter.onConstantSelected(constantToken);
    }

    public void showDialogConstant() {
        ConstantFragment constantFragment = ConstantFragment.newInstance();
        constantFragment.show(getSupportFragmentManager(), constantFragment.getTag());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawers();
        switch (item.getItemId()) {
            case R.id.action_get_pro:
                getProVersion();
                return true;
            case R.id.action_report_bug:
                reportBug();
                return true;
            case R.id.action_rate:
                rateApp();
                return true;
        }
        return false;
    }

    private void rateApp() {

    }

    private void reportBug() {

    }

    private void getProVersion() {

    }

    public void openDrawer() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.openDrawer(GravityCompat.START);
    }
}