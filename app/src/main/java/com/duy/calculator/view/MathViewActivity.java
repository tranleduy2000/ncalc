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

package com.duy.calculator.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.duy.calculator.R;

import io.github.kexanie.library.MathView;

/**
 * MathView custom as mContext
 * Created by Duy on 04-Nov-16.
 */

public class MathViewActivity extends AppCompatActivity {
    public static final String DATA = "data_math_view";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.math_view_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = getIntent().getBundleExtra(DATA);
            if (bundle != null) {
                try {
                    String res = bundle.getString(DATA);
                    MathView mathView = (MathView) findViewById(R.id.math_view);
                    mathView.getSettings().setSupportZoom(true);
                    mathView.getSettings().setJavaScriptEnabled(true);
                    mathView.getSettings().setBuiltInZoomControls(true);
                    mathView.getSettings().setDisplayZoomControls(true);
                    mathView.setText(res);
                    Toast.makeText(this, R.string.please_wait, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    new AlertDialog.Builder(this).setTitle(R.string.error)
                            .setMessage(e.getMessage())
                            .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .create()
                            .show();
                }

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
