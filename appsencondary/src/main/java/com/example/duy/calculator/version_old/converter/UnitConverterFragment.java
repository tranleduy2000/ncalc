package com.example.duy.calculator.version_old.converter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.duy.calculator.R;
import com.example.duy.calculator.AbstractFragment;

import java.util.ArrayList;


public class UnitConverterFragment extends AbstractFragment implements CategoryUnitAdapter.OnItemClickListener {
    protected static final String TAG = UnitConverterFragment.class.getName();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
        CategoryUnitAdapter adapter = new CategoryUnitAdapter(arrayList, mContext);
        adapter.setListener(this);
        recyclerView.setAdapter(adapter);


    }

    public void startActivity(int position, String text) {
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.key_pos), position);
        bundle.putString(getString(R.string.key_name), text);
        Intent intent = new Intent(mContext, UnitConverterChildActivity.class);
        intent.putExtra(getString(R.string.key_data), bundle);
        startActivity(intent);
    }


    @Override
    protected View getView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.unit_converter_acitvity, container, false);
    }

    @Override
    protected void onChangeModeFraction() {

    }

    @Override
    public void onItemClick(int pos, String text) {
        startActivity(pos, text);
    }

    @Override
    public void onItemLongClick() {

    }
}
