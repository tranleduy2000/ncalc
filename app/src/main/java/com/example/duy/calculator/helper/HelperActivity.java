package com.example.duy.calculator.helper;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.duy.calculator.AbstractAppCompatActivity;
import com.example.duy.calculator.R;

import io.github.kexanie.library.MathView;

/**
 * Helper Activity
 * Created by Duy on 29-Nov-16.
 */

public class HelperActivity extends AbstractAppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        MathView view = (MathView) findViewById(R.id.webview);
        String data = getString(R.string.help_main_calc);
        view.setText(data);
        //done
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
