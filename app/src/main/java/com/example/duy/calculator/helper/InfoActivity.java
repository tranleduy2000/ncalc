package com.example.duy.calculator.helper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.duy.calculator.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class InfoActivity extends AppCompatActivity {
    private static final String TAG = InfoActivity.class.getClass().getSimpleName();
    @BindView(com.example.duy.calculator.R.id.list_translate)
    RecyclerView mListTranslate;
    @BindView(R.id.list_license)
    RecyclerView mListLicense;
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
        ArrayList<ItemInfo> dataLicense;


        @Override
        protected Void doInBackground(Void... params) {
            dataTranslate = InfoAppUtil.readListTranslate(getResources().openRawResource(R.raw.help_translate));
//            dataLicense = InfoAppUtil.readListLicense(getResources().openRawResource(R.raw.license));

            final String[] name = getResources().getStringArray(R.array.libs);
            final String[] license = getResources().getStringArray(R.array.lics);
            dataLicense = new ArrayList<>();
            for (int i = 0; i < name.length; i++) {
                dataLicense.add(new ItemInfo(name[i], license[i], ""));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            HelpTranslateAdapter adapterTranslate = new HelpTranslateAdapter(InfoActivity.this, dataTranslate);
            mListTranslate.setLayoutManager(new LinearLayoutManager(InfoActivity.this));
            mListTranslate.setHasFixedSize(false);
            mListTranslate.setAdapter(adapterTranslate);

            LicenseAdapter adapterLicense = new LicenseAdapter(InfoActivity.this, dataLicense);
            mListLicense.setLayoutManager(new LinearLayoutManager(InfoActivity.this));
            mListLicense.setHasFixedSize(false);
            mListLicense.setAdapter(adapterLicense);
            mListLicense.addItemDecoration(new DividerItemDecoration(InfoActivity.this, DividerItemDecoration.VERTICAL));
        }
    }

}
