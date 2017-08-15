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

package com.duy.calculator.document;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duy.calculator.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class InfoActivity extends AppCompatActivity {
    private static final String TAG = InfoActivity.class.getClass().getSimpleName();
    @BindView(com.duy.calculator.R.id.list_translate)
    RecyclerView mListTranslate;
//    @BindView(R.id.list_license)
//    RecyclerView mListLicense;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");

        setContentView(R.layout.activity_info);
        ButterKnife.bind(InfoActivity.this);
        setupActionBar();
        initContent();
    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        setTitle(R.string.title_activity_app_about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initContent() {
        new TaskLoadData().execute();
        Log.d(TAG, "initContent: ");

    }

    class TaskLoadData extends AsyncTask<Void, Void, Void> {
        ArrayList<ItemInfo> dataTranslate;
//        ArrayList<ItemInfo> dataLicense;


        @Override
        protected Void doInBackground(Void... params) {
            dataTranslate = InfoAppUtil.readListTranslate(getResources().openRawResource(R.raw.help_translate));

            final String[] name = getResources().getStringArray(R.array.libs);
            final String[] license = getResources().getStringArray(R.array.lics);
//            dataLicense = new ArrayList<>();
//            for (int i = 0; i < name.length; i++) {
//                dataLicense.add(new ItemInfo(name[i], license[i], ""));
//            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            HelpTranslateAdapter adapterTranslate = new HelpTranslateAdapter(InfoActivity.this, dataTranslate);
            mListTranslate.setLayoutManager(new LinearLayoutManager(InfoActivity.this));
            mListTranslate.setHasFixedSize(false);
            mListTranslate.setAdapter(adapterTranslate);

//            LicenseAdapter adapterLicense = new LicenseAdapter(InfoActivity.this, dataLicense);
//            mListLicense.setLayoutManager(new LinearLayoutManager(InfoActivity.this));
//            mListLicense.setHasFixedSize(false);
//            mListLicense.setAdapter(adapterLicense);
//            mListLicense.addItemDecoration(new DividerItemDecoration(InfoActivity.this, DividerItemDecoration.VERTICAL));
        }
    }

    /**
     * Created by Duy on 28-Mar-17.
     */

    public static class HelpTranslateAdapter extends RecyclerView.Adapter<HelpTranslateAdapter.ViewHolder> {
        private static final String TAG = HelpTranslateAdapter.class.getSimpleName();
        private LayoutInflater inflater;
        private ArrayList<ItemInfo> listData = new ArrayList<>();
        private Context mContext;

        public HelpTranslateAdapter(Context context, ArrayList<ItemInfo> listData) {
            this.inflater = LayoutInflater.from(context);
            this.listData = listData;
            this.mContext = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.list_item_info, parent, false);
            Log.d(TAG, "onCreateViewHolder: ");
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.bindContent(listData.get(position));
    //        holder.root.setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View v) {
    //                Toast.makeText(mContext, listData.get(position).toString(), Toast.LENGTH_SHORT).show();
    //            }
    //        });

        }

        @Override
        public int getItemCount() {
            return listData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.txt_title)
            TextView txtTitle;
            @BindView(R.id.txt_desc)
            TextView txtDesc;
            @BindView(R.id.container)
            View root;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            public void bindContent(ItemInfo itemInfo) {
                txtTitle.setText(itemInfo.getLang());
                String desc = itemInfo.getTitle() + (itemInfo.getLink().trim().isEmpty() ? "" : "\n" + itemInfo.getLink());
                txtDesc.setText(desc);
            }
        }

    }

    /**
     * Created by Duy on 28-Mar-17.
     */

    public static class LicenseAdapter extends RecyclerView.Adapter<LicenseAdapter.ViewHolder> {
        private static final String TAG = LicenseAdapter.class.getSimpleName();
        private LayoutInflater inflater;
        private ArrayList<ItemInfo> listData = new ArrayList<>();
        private Context mContext;

        public LicenseAdapter(Context context, ArrayList<ItemInfo> listData) {
            this.inflater = LayoutInflater.from(context);
            this.listData = listData;
            this.mContext = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.list_item_info, parent, false);
            Log.d(TAG, "onCreateViewHolder: ");
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.bindContent(listData.get(position));
    //        holder.root.setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View v) {
    //                Toast.makeText(mContext, listData.get(position).toString(), Toast.LENGTH_SHORT).show();
    //            }
    //        });

        }

        @Override
        public int getItemCount() {
            return listData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.txt_title)
            TextView txtTitle;
            @BindView(R.id.txt_desc)
            TextView txtDesc;
            @BindView(R.id.container)
            View root;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            public void bindContent(ItemInfo itemInfo) {
                txtTitle.setText(itemInfo.getTitle());
                txtDesc.setText(itemInfo.getLink());
            }
        }

    }
}
