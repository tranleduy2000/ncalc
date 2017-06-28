package com.example.duy.calculator.version_old.converter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.duy.calculator.R;
import com.example.duy.calculator.version_old.activities.abstract_class.AbstractNavDrawerActionBarActivity;

import java.util.ArrayList;


public class UnitConverterParentAcitvity extends AbstractNavDrawerActionBarActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    protected static final String TAG = UnitConverterParentAcitvity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_unit_converter_acitvity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(R.drawable.ic_temp);
        arrayList.add(R.drawable.ic_weight);
        arrayList.add(R.drawable.ic_length);
        arrayList.add(R.drawable.ic_power);
        arrayList.add(R.drawable.ic_power);
        arrayList.add(R.drawable.ic_speed);
        arrayList.add(R.drawable.ic_area);
        arrayList.add(R.drawable.ic_cubic);
        arrayList.add(R.drawable.ic_bitrate);
        arrayList.add(R.drawable.ic_time);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rcview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        CategoryUnitAdapter adapter = new CategoryUnitAdapter(arrayList, UnitConverterParentAcitvity.this);
        adapter.setListener(new CategoryUnitAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, String text) {
                startActivity(pos, text);
            }

            @Override
            public void onItemLongClick() {

            }
        });
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        super.onNavigationItemSelected(item);
        return true;
    }


    void startActivity(int position, String text) {
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.key_pos), position);
        bundle.putString(getString(R.string.key_name), text);
        Intent intent = new Intent(UnitConverterParentAcitvity.this, UnitConverterChildActivity.class);
        intent.putExtra(getString(R.string.key_data), bundle);
        startActivity(intent);
    }


}
