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
package com.duy.calculator.graph;

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

import com.duy.calculator.activities.base.AbstractAppCompatActivity;
import com.duy.calculator.R;


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