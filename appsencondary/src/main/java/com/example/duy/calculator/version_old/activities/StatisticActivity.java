package com.example.duy.calculator.version_old.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.duy.calculator.R;
import com.example.duy.calculator.data.CalculatorSetting;
import com.example.duy.calculator.utils.ColorUtil;
import com.example.duy.calculator.version_old.activities.abstract_class.AbstractNavDrawerActionBarActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import org.apache.commons.math4.stat.descriptive.DescriptiveStatistics;

import java.util.ArrayList;

@Deprecated
public class StatisticActivity extends AbstractNavDrawerActionBarActivity {

    private static final String TAG = StatisticActivity.class.getSimpleName();
    private EditText mInput;
    private Button btnSubmit;
    private LineChart mLineChart;
    private BarChart mBarChart;
    private PieChart mPieChart;
    private SwitchCompat swAuto;

    @Override
    public void onPause() {
        super.onPause();
        mSetting.put(CalculatorSetting.INPUT_STATISTIC, mInput.getText().toString());
    }

    @Override
    public void onResume() {
        super.onResume();
        mInput.setText(mSetting.getString(CalculatorSetting.INPUT_STATISTIC));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        swAuto = (SwitchCompat) findViewById(R.id.sw_auto_process);
        mInput = (EditText) findViewById(R.id.edit_input);
        mInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (swAuto.isChecked()) doSubmit();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSubmit();
            }
        });

        mPieChart = (PieChart) findViewById(R.id.pie_chart);
        mLineChart = (LineChart) findViewById(R.id.line_chart);
        mBarChart = (BarChart) findViewById(R.id.bar_chart);
    }


    private void doSubmit() {
        Log.d(TAG, "doSubmit: ");
        try {
            DescriptiveStatistics mStatistics = new DescriptiveStatistics();
            final String[] input = mInput.getText().toString().split(",");
            for (int i = 0; i < input.length; i++) {
                mStatistics.addValue(Double.parseDouble(input[i]));
            }

            ((TextView) findViewById(R.id.txt_geo_mean)).setText(String.valueOf(mStatistics.getGeometricMean()));
            ((TextView) findViewById(R.id.txtKurtosis)).setText(String.valueOf(mStatistics.getKurtosis()));
            ((TextView) findViewById(R.id.txt_maximum)).setText(String.valueOf(mStatistics.getMax()));
            ((TextView) findViewById(R.id.txt_minimum)).setText(String.valueOf(mStatistics.getMin()));
            ((TextView) findViewById(R.id.txt_arithmetic_mean)).setText(String.valueOf(mStatistics.getMean()));
//            ((TextView) findViewById(R.id.txt_n)).setText(String.valueOf(mStatistics.getN()));
            ((TextView) findViewById(R.id.txt_population_variance)).setText(String.valueOf(mStatistics.getPopulationVariance()));
            ((TextView) findViewById(R.id.txt_quadratic_mean)).setText(String.valueOf(mStatistics.getQuadraticMean()));
            ((TextView) findViewById(R.id.txt_skewness)).setText(String.valueOf(mStatistics.getSkewness()));
            ((TextView) findViewById(R.id.txt_sum)).setText(String.valueOf(mStatistics.getSum()));
            ((TextView) findViewById(R.id.txt_variance)).setText(String.valueOf(mStatistics.getVariance()));

            drawLineChart(input);
            drawBarChart(input);
            drawPieChart(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawLineChart(String[] data) {
        LineData lineData = getLineData(data);
        mLineChart.setData(lineData);
        mLineChart.setDescription("");
        mLineChart.setDrawGridBackground(false);

        XAxis xAxis = mLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(false);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);

        YAxis leftAxis = mLineChart.getAxisLeft();
        leftAxis.setLabelCount(5, false);
        leftAxis.setAxisMinValue(0f);

        YAxis rightAxis = mLineChart.getAxisRight();
        rightAxis.setLabelCount(5, false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinValue(0f);

        mLineChart.animateX(750);


        /*bar mBarChart*/

    }

    private void drawBarChart(String[] data) {
        // apply styling
        mBarChart.setDescription("");
        mBarChart.setDrawGridBackground(false);
        mBarChart.setDrawBarShadow(false);

        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(false);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);

        YAxis leftAxis = mBarChart.getAxisLeft();
        leftAxis.setLabelCount(5, false);
        leftAxis.setSpaceTop(20f);
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mBarChart.getAxisRight();
        rightAxis.setLabelCount(5, false);
        rightAxis.setSpaceTop(20f);
        rightAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)


        // set data
        mBarChart.setData(getBarData(data));
        mBarChart.setFitBars(true);
        // do not forget to refresh the mBarChart
//        mBarChart.invalidate();
        mBarChart.animateY(700);
    }

    private LineData getLineData(String[] data) {
        ArrayList<Entry> mList = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            mList.add(new Entry(i, Float.parseFloat(data[i])));
        }
        LineDataSet lineDataSet = new LineDataSet(mList, "");
        lineDataSet.setLineWidth(2.5f);
        lineDataSet.setCircleRadius(4.5f);
        lineDataSet.setHighLightColor(ColorUtil.ORANGE);
        lineDataSet.setDrawValues(true);
        return new LineData(lineDataSet);
    }

    private BarData getBarData(String[] data) {
        ArrayList<BarEntry> mList = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            mList.add(new BarEntry(i, Float.parseFloat(data[i])));
        }
        BarDataSet d = new BarDataSet(mList, "");
        d.setColors(ColorUtil.COLOR);
        d.setHighLightAlpha(255);

        BarData cd = new BarData(d);
        cd.setBarWidth(0.9f);
        return cd;
    }

    private void drawPieChart(String[] data) {
        PieData mChartData = getPieData(data);
        // apply styling
        mPieChart.setDescription("");
        mPieChart.setHoleRadius(52f);
        mPieChart.setTransparentCircleRadius(57f);
        mPieChart.setCenterText("PieChart");
        mPieChart.setCenterTextSize(9f);
        mPieChart.setUsePercentValues(false);
        mPieChart.setExtraOffsets(5, 10, 50, 10);

        mChartData.setValueFormatter(new PercentFormatter());
        mChartData.setValueTextSize(11f);
        mChartData.setValueTextColor(Color.WHITE);
        // set data
        mPieChart.setData(mChartData);


        Legend l = mPieChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_RIGHT);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // do not forget to refresh the mPieChart
        // mPieChart.invalidate();
        mPieChart.animateY(900);
    }

    private PieData getPieData(String[] data) {
        ArrayList<PieEntry> mList = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            mList.add(new PieEntry(i, Float.parseFloat(data[i])));
        }

        PieDataSet d = new PieDataSet(mList, "");
        // space between slices
        d.setSliceSpace(2f);
        d.setColors(ColorUtil.COLOR);
        d.setDrawValues(true);
        return new PieData(d);
    }
}
