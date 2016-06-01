package com.keenbrace.activity;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.keenbrace.R;
import com.keenbrace.constants.UtilConstants;
import com.keenbrace.widget.MyValueFormatter;

public class FragmentReport extends BaseFragment {
    TextView tv_whistle, iv_message;
    ImageView iv_t, iv_model;
    LineChart lineChart;
    BarChart osChart, xgylChart, jzdChart, bpChart;
    public static final int[] VORDIPLOM_COLORS = {
            Color.rgb(192, 255, 140), Color.rgb(255, 247, 140), Color.rgb(255, 208, 140),
            Color.rgb(140, 234, 255), Color.rgb(255, 140, 157)};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frame_report, null);
        tv_whistle = (TextView) view.findViewById(R.id.tv_whistle);
        iv_message = (TextView) view.findViewById(R.id.iv_message);
        iv_t = (ImageView) view.findViewById(R.id.iv_t);
        iv_model = (ImageView) view.findViewById(R.id.iv_model);
        osChart = (BarChart) view.findViewById(R.id.osChart);
        xgylChart = (BarChart) view.findViewById(R.id.xgylChart);
        jzdChart = (BarChart) view.findViewById(R.id.jzdChart);
        bpChart = (BarChart) view.findViewById(R.id.bpChart);
        initLineChart(view);
        initBarChart(osChart, "vertical oscillation", VORDIPLOM_COLORS[0]);
        initBarChart(xgylChart, "knee press", VORDIPLOM_COLORS[3]);
        initBarChart(jzdChart, "ground contact time", VORDIPLOM_COLORS[0]);
        initBarChart(bpChart, "Step Rate", VORDIPLOM_COLORS[3]);
        osChart.getAxisLeft().setAxisMaxValue(30);
        osChart.getAxisLeft().setAxisMinValue(0);
        xgylChart.getAxisLeft().setAxisMaxValue(20);
        xgylChart.getAxisLeft().setAxisMinValue(0);
        jzdChart.getAxisLeft().setAxisMaxValue(1000);
        jzdChart.getAxisLeft().setAxisMinValue(0);
        bpChart.getAxisLeft().setAxisMaxValue(300);
        bpChart.getAxisLeft().setAxisMinValue(0);
        return view;
    }

    public void initLineChart(View view) {

        lineChart = (LineChart) view.findViewById(R.id.lineChart);
        lineChart.setDescription("");
        lineChart.setHighlightEnabled(true);
        lineChart.setDrawGridBackground(false);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        XAxis xAxis = lineChart.getXAxis();

        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setSpaceBetweenLabels(2);
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setLabelCount(5, false);
        leftAxis.setAxisMaxValue(255);
        leftAxis.setAxisMinValue(0);
        leftAxis.setValueFormatter(new MyValueFormatter());
        leftAxis.setSpaceTop(15f);
        leftAxis.setStartAtZero(true);
        leftAxis.setDrawGridLines(false);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.setData(new LineData());
        lineChart.invalidate();


    }

    public void initBarChart(BarChart mChart, String title, int color) {

        // barChart
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setTouchEnabled(false);
        mChart.setDescription("");
        mChart.setNoDataText("");
        //mChart.setMaxVisibleValueCount(60);
        mChart.setDrawGridBackground(false);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);


        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setLabelCount(5, false);
        leftAxis.setValueFormatter(new MyValueFormatter());
        leftAxis.setPosition(YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setDrawGridLines(false);
        mChart.getAxisRight().setEnabled(false);

        mChart.setData(new BarData());
        BarData data = mChart.getData();


        BarDataSet set = data.getDataSetByIndex(0);
        if (set == null) {
            ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
            set = new BarDataSet(yVals1, title);
            set.setDrawValues(false);
            set.setBarSpacePercent(10);

            data.addDataSet(set);
            set.setColor(color);
        }
        mChart.invalidate();
    }

    int linValue = -1;

    public void addLineEntry(int power) {
        if (linValue == power)
            return;
        linValue = power;
        LineData data = lineChart.getData();
        if (data != null) {

            LineDataSet set = data.getDataSetByIndex(0);
            if (set == null) {
                set = createSet("");
                data.addDataSet(set);
            }
            data.addXValue("");
            data.addEntry(new Entry(power, set.getEntryCount()), 0);
            lineChart.notifyDataSetChanged();
            lineChart.setVisibleXRangeMaximum(50);
            lineChart.moveViewTo(data.getXValCount() - 50, 0.0f,
                    AxisDependency.LEFT);
            lineChart.invalidate();
        }

    }

    public void addJldBarEntry(int jld) {
        addBarEntry(jld, jzdChart);
    }

    int barValue = -1;

    public void addBarEntry(int os, int xgyl, int bp) {
        addBarEntry(os, osChart);
        float ya = xgyl / (UtilConstants.Weight * 9.8f);
        addBarEntry(ya, xgylChart);

        addBarEntry(bp, bpChart);
    }

    public void addBarEntry(int value, BarChart mChart) {

        barValue = value;
        BarData data = mChart.getData();


        BarDataSet set = data.getDataSetByIndex(0);

        data.addXValue("");
        set.addEntry(new BarEntry(value, set.getEntryCount()));
        mChart.notifyDataSetChanged();
        mChart.setVisibleXRangeMaximum(500);
        mChart.moveViewTo(data.getXValCount() - 500, 0.0f,
                AxisDependency.LEFT);
        mChart.invalidate();

    }

    public void addBarEntry(float value, BarChart mChart) {

        BarData data = mChart.getData();


        BarDataSet set = data.getDataSetByIndex(0);

        data.addXValue("");
        set.addEntry(new BarEntry(value, set.getEntryCount()));
        mChart.notifyDataSetChanged();
        mChart.setVisibleXRangeMaximum(500);
        mChart.moveViewTo(data.getXValCount() - 500, 0.0f,
                AxisDependency.LEFT);
        mChart.invalidate();

    }

    private LineDataSet createSet(String title) {
        LineDataSet set = new LineDataSet(null, "power");

        set.setLineWidth(2f);
        set.setCircleSize(5f);
        set.setDrawCircleHole(false);
        set.setDrawValues(false);
        set.setDrawFilled(true);
        set.setDrawCircles(false);

        return set;
    }
}
