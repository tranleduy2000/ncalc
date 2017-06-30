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

package com.duy.calc.casio.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.duy.calculator.R;
import com.duy.calc.casio.ThemeHelper;

/**
 * Created by david on 1/16/2017.
 */

public class ThemeSelectActivity extends AppCompatActivity {
    public static final boolean DONATE_ON = true;;
    private ImageAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeHelper.setUpTheme(this,true);
        setContentView(R.layout.themes_select);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Select A Theme");
        //Sets up the theme
        ListView gv = (ListView) findViewById(R.id.themes_grid);
        adapter = new ImageAdapter(this);
        gv.setAdapter(adapter);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }


    public class ImageAdapter extends BaseAdapter {
        LayoutInflater inflater;
        private Context mContext;
        private String[] theme_names = new String[]{"Blue Delight", "Orange on Black", "Purple Monster", "Green Forest", "Carribean Blue", "Donate"};
        private int[] color1 = new int[]{R.color.dThemePrimary,R.color.byThemePrimary,R.color.pThemePrimary,R.color.bThemePrimary,R.color.bgThemePrimary,R.color.donateThemePrimary};
        private int[] color2 = new int[]{R.color.dThemePrimaryDark,R.color.byThemePrimaryDark,R.color.pThemePrimaryDark,R.color.bThemePrimaryDark,R.color.bgThemePrimaryDark,R.color.donateThemePrimaryDark};
        private int[] text = new int[]{R.color.dThemeHighlight,R.color.byThemeHighlight,R.color.pThemeHighlight,R.color.bThemeHighlight,R.color.bgThemeHighlight,R.color.donateThemeHighlight};

        // Constructor
        public ImageAdapter(Context c) {
            mContext = c;
            inflater = LayoutInflater.from(mContext);
        }

        public int getCount() {
            return DONATE_ON ? theme_names.length : theme_names.length - 1;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(final int position, View convertView, ViewGroup parent) {
            ThemeHolder mViewHolder;
            final int curr = position;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.theme_item, parent, false);
                mViewHolder = new ThemeHolder(convertView);
                convertView.setTag(mViewHolder);

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences pref = getSharedPreferences(getString(R.string.preference_key), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putInt(getString(R.string.theme), position+1);
                        editor.apply();
                        finish();
                    }
                });
            } else {
                mViewHolder = (ThemeHolder) convertView.getTag();
            }

            ((TextView) convertView.findViewById(R.id.title)).setText(theme_names[position]);
            //((TextView) convertView.findViewById(R.id.sample_text)).setTextColor(getResources().getColor(text[position]));
            ((ImageButton) convertView.findViewById(R.id.color1)).setImageDrawable(
                    new ColorDrawable(getResources().getColor(color1[position])));
            ((ImageButton) convertView.findViewById(R.id.color2)).setImageDrawable(
                    new ColorDrawable(getResources().getColor(color2[position])));

            return convertView;
        }

        private class ThemeHolder {
            TextView title;
            ImageView color1, color2;
            LinearLayout section;

            public ThemeHolder(View item) {
                title = (TextView) item.findViewById(R.id.title);
                color1 = (ImageView) item.findViewById(R.id.color1);
                color2 = (ImageView) item.findViewById(R.id.color2);
                section = (LinearLayout) item.findViewById(R.id.theme_select);
            }
        }

    }
}
