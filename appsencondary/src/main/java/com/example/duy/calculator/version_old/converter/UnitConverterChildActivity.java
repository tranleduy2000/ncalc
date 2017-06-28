package com.example.duy.calculator.version_old.converter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.duy.calculator.AbstractAppCompatActivity;
import com.example.duy.calculator.R;
import com.example.duy.calculator.version_old.converter.utils.AreaStrategy;
import com.example.duy.calculator.version_old.converter.utils.BitrateStrategy;
import com.example.duy.calculator.version_old.converter.utils.EnergyStrategy;
import com.example.duy.calculator.version_old.converter.utils.LengthStrategy;
import com.example.duy.calculator.version_old.converter.utils.PowerStrategy;
import com.example.duy.calculator.version_old.converter.utils.Strategy;
import com.example.duy.calculator.version_old.converter.utils.TemperatureStrategy;
import com.example.duy.calculator.version_old.converter.utils.TimeStratery;
import com.example.duy.calculator.version_old.converter.utils.VelocityStrategy;
import com.example.duy.calculator.version_old.converter.utils.VolumeStrategy;
import com.example.duy.calculator.version_old.converter.utils.WeightStrategy;

import java.util.ArrayList;

public class UnitConverterChildActivity extends AbstractAppCompatActivity {
    String TAG = UnitConverterChildActivity.class.getName();
    RecyclerView mRecycleView;
    private Spinner spinner;
    private EditText editText;
    private Strategy strategy;
    private String arrUnit[];
    private UnitAdapter mUnitAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_converter_child);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        int pos = bundle.getInt("POS");
        String name = bundle.getString("NAME");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
        setUpSpinnerAndStratery(pos);
    }


    private void setUpSpinnerAndStratery(int pos) {
        arrUnit = new String[]{};
        switch (pos) {
            case 0: //temp
                arrUnit = getResources().getStringArray(R.array.temp);
                setStrategy(new TemperatureStrategy(getApplicationContext()));
                break;
            case 1: //WEight
                arrUnit = getResources().getStringArray(R.array.weight);
                setStrategy(new WeightStrategy(getApplicationContext()));
                break;
            case 2: //temp
                arrUnit = getResources().getStringArray(R.array.length);
                setStrategy(new LengthStrategy(getApplicationContext()));
                break;
            case 3: //temp
                arrUnit = getResources().getStringArray(R.array.power);
                setStrategy(new PowerStrategy(getApplicationContext()));
                break;
            case 4: //temp
                arrUnit = getResources().getStringArray(R.array.energy);
                setStrategy(new EnergyStrategy(getApplicationContext()));
                break;
            case 5: //temp
                arrUnit = getResources().getStringArray(R.array.velocity);
                setStrategy(new VelocityStrategy(getApplicationContext()));
                break;
            case 6: //temp
                setStrategy(new AreaStrategy(getApplicationContext()));
                arrUnit = getResources().getStringArray(R.array.area);
                break;
            case 7: //temp
                arrUnit = getResources().getStringArray(R.array.volume);
                setStrategy(new VolumeStrategy(getApplicationContext()));
                break;
            case 8: //temp
                arrUnit = getResources().getStringArray(R.array.bitrate);
                setStrategy(new BitrateStrategy(getApplicationContext()));
                break;
            case 9: //temp
                arrUnit = getResources().getStringArray(R.array.time);
                setStrategy(new TimeStratery(getApplicationContext()));
                break;


        }
        ArrayAdapter<String> adapterUnit = new ArrayAdapter<>(UnitConverterChildActivity.this, android.R.layout.simple_list_item_1, arrUnit);
        adapterUnit.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinner.setAdapter(adapterUnit);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateListView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void initView() {
        editText = (EditText) findViewById(R.id.editInput);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateListView();
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        String text = bundle.getString("input", "");
        editText.append(text);

        spinner = (Spinner) findViewById(R.id.spinner_unit);
        mRecycleView = (RecyclerView) findViewById(R.id.listview);
        mRecycleView.setHasFixedSize(true);
        LinearLayoutManager mLayout = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(mLayout);
        mUnitAdapter = new UnitAdapter(this, new ArrayList<ItemUnitConverter>());
        mRecycleView.setAdapter(mUnitAdapter);

    }

    private void updateListView() {
        String currentUnit = spinner.getSelectedItem().toString();
        String defaultUnit = strategy.getUnitDefault();
        Log.i("currentUnit", currentUnit);
        ArrayList<ItemUnitConverter> list = new ArrayList<>();
        if (!editText.getText().toString().equals("")) {
            try {
                double input = Double.parseDouble(editText.getText().toString());
                double defaultValue = strategy.Convert(currentUnit, defaultUnit, input);
                Log.e("INP", String.valueOf(input));
                Log.e("DEF", String.valueOf(defaultValue));

                for (String anArrUnit : arrUnit) {
                    double res = strategy.Convert(defaultUnit, anArrUnit, defaultValue);
                    ItemUnitConverter itemUnitConverter = new ItemUnitConverter();
                    itemUnitConverter.setTitle(anArrUnit);
                    itemUnitConverter.setRes(String.valueOf(res));
                    list.add(itemUnitConverter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                double input = 0d;
                double defaultValue = strategy.Convert(currentUnit, defaultUnit, input);
                Log.e("INP", String.valueOf(input));
                Log.e("DEF", String.valueOf(defaultValue));

                for (String anArrUnit : arrUnit) {
                    double res = strategy.Convert(defaultUnit, anArrUnit, defaultValue);
                    ItemUnitConverter itemUnitConverter = new ItemUnitConverter();
                    itemUnitConverter.setTitle(anArrUnit);
                    itemUnitConverter.setRes(String.valueOf(res));
                    list.add(itemUnitConverter);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mUnitAdapter.clear();
        mUnitAdapter.addAll(list);
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


//    class UnitAdapter extends ArrayAdapter<ItemUnitConverter> {
//        private Context mContext;
//        private int resource;
//        private ArrayList<ItemUnitConverter> arrayList = new ArrayList<>();
//
//        public UnitAdapter(Context mContext, int resource, ArrayList<ItemUnitConverter> objects) {
//            super(mContext, resource, objects);
//            this.mContext = mContext;
//            this.resource = resource;
//            this.arrayList = objects;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            View view = LayoutInflater.from(mContext).inflate(resource, parent, false);
//            ItemUnitConverter itemUnitConverter = arrayList.get(position);
//            TextView txtName = (TextView) view.findViewById(R.id.txtTitle);
//            TextView txtRes = (TextView) view.findViewById(R.id.txtResult);
//            txtName.setText(itemUnitConverter.getTitle());
//            txtRes.setText(String.valueOf(itemUnitConverter.getRes()));
//            return view;
//        }
//
//        @Override
//        public View getDropDownView(int position, View convertView, ViewGroup parent) {
//            return getView(position, convertView, parent);
//        }
//
//        public void addUnit(ItemUnitConverter itemUnitConverter) {
//            arrayList.add(itemUnitConverter);
//            notifyDataSetChanged();
//        }
//
//        public void clear() {
//            arrayList.clear();
//            notifyDataSetChanged();
//        }
//    }

}


