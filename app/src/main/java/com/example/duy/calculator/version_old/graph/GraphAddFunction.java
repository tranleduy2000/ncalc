/*
 * Copyright (C) 2010 Andrew P McSherry
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.duy.calculator.version_old.graph;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.duy.calculator.AbstractAppCompatActivity;
import com.example.duy.calculator.R;


public class GraphAddFunction extends AbstractAppCompatActivity {
    private Button btnSave;
    private EditText[] editFunctions;
    private SharedPreferences preferences;
    private final View.OnKeyListener onKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_NUMPAD_ENTER:
                case KeyEvent.KEYCODE_ENTER:
                    if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
                        updateRect();
                        finish();
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_add_function);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        editFunctions = new EditText[6];
        editFunctions[0] = (EditText) findViewById(R.id.funText1);
        editFunctions[1] = (EditText) findViewById(R.id.funText2);
        editFunctions[2] = (EditText) findViewById(R.id.funText3);
        editFunctions[3] = (EditText) findViewById(R.id.funText4);
        editFunctions[4] = (EditText) findViewById(R.id.funText5);
        editFunctions[5] = (EditText) findViewById(R.id.funText6);

        for (int i = 0; i < 6; i++) editFunctions[i].setOnKeyListener(onKeyListener);

        btnSave = (Button) findViewById(R.id.update);
        btnSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] f = new String[6];
                for (int i = 0; i < 6; i++) {
                    f[i] = editFunctions[i].getText().toString();
                }
                for (int i = 0; i < 6; i++) {
                    if (!f[i].equals("")) {
                        if (!GraphMath.isValid(f[i], new String[]{"x"})) {
//                            Toast.makeText(GraphAddFunction.this, getString(R.string.warningFun) + (i + 1) + getString(R.string.isInvalid),
//                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                updateRect();
                finish();
            }
        });

        initData();

    }

    /**
     * get data and set text
     */
    private void initData() {
        for (int i = 0; i < 6; i++) {
            String f = preferences.getString("f" + (i + 1), "");
            if (!f.isEmpty())
                editFunctions[i].setText(f);
        }
    }


    public void updateRect() {
        SharedPreferences.Editor edit = preferences.edit();
        for (int i = 0; i < 6; i++) {
            edit.putString("f" + (i + 1), editFunctions[i].getText().toString());
        }
        edit.apply();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            updateRect();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}