package com.example.duy.calculator.version_old.settings;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.example.duy.calculator.R;
import com.example.duy.calculator.version_old.activities.abstract_class.AbstractAppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Setting for calcualtor
 * <p>
 * Include precision of calculate. Font, theme, style. Dev mode, trace mode.
 */
public class SettingsActivity extends AbstractAppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.settings);

        getFragmentManager().beginTransaction().replace(R.id.container, new FragmentSetting()).commit();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
