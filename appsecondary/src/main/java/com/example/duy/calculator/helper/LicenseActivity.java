package com.example.duy.calculator.helper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.duy.calculator.AbstractAppCompatActivity;
import com.example.duy.calculator.R;

/**
 * <<<<<<< HEAD
 * License view
 * =======
 * >>>>>>> refs/remotes/origin/master
 * Created by DUy on 12-Dec-16.
 */

public class LicenseActivity extends AbstractAppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.license_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String[] arr = getResources().getStringArray(R.array.libs);
        final String[] lics = getResources().getStringArray(R.array.lics);

        ListViewCompat listViewCompat = (ListViewCompat) findViewById(R.id.rc_view);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(LicenseActivity.this, android.R.layout.simple_list_item_1, arr);
        listViewCompat.setAdapter(arrayAdapter);
        listViewCompat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(LicenseActivity.this, InfoLicenseActivity.class);
                intent.putExtra("data", lics[i]);
                intent.putExtra("title", arr[i]);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            this.finish();
        return super.onOptionsItemSelected(item);
    }
}
