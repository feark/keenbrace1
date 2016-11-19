package com.keenbrace.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.keenbrace.R;
import com.keenbrace.activity.MainMenuActivity;
import com.keenbrace.bean.CommonResultDBHelper;
import com.keenbrace.constants.UtilConstants;
import com.keenbrace.activity.MainActivity;
import com.keenbrace.base.BaseFragment;
import com.keenbrace.greendao.CommonResult;
import com.keenbrace.widget.MyValueFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import butterknife.Bind;
import butterknife.OnClick;



public class IndexFragment extends BaseFragment {

    LineChart lc_bmr, lc_bodyfat;
    HorizontalBarChart hb_intake, hb_consume;
    ImageView iv_drawer;

    /*
    @OnClick (R.id.iv_drawer_index)
    void onDrawerIndex(){
        ((MainActivity)getActivity()).showMenu();
    }
    */

    @Override
    public int getLayoutId() {
        return R.layout.frame_index;
    }

    @Override
    public void initView() {
    }

    @Override
    public void initData() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frame_index, null);

        lc_bmr = (LineChart)view.findViewById(R.id.lc_bmr);
        lc_bodyfat = (LineChart)view.findViewById(R.id.lc_bodyfat);

        hb_intake = (HorizontalBarChart) view.findViewById(R.id.hbc_intake);
        hb_consume = (HorizontalBarChart) view.findViewById(R.id.hbc_burn);

        iv_drawer = (ImageView) view.findViewById(R.id.iv_drawer_index);
        iv_drawer.setOnClickListener(this);

        initBmrLineChart();

        initBodyFatLineChart();

        addBmrLineEntry(0);
        addBodyFatLineEntry(0);

        initBarChart(hb_intake, " ", Color.rgb(255, 255, 255));
        hb_intake.getAxisLeft().setAxisMaxValue(3000);
        hb_intake.getAxisLeft().setAxisMinValue(0);
        addBarEntry(1925, hb_intake);

        initBarChart(hb_consume, " ", Color.rgb(255, 255, 255));
        hb_consume.getAxisLeft().setAxisMaxValue(3000);
        hb_consume.getAxisLeft().setAxisMinValue(0);
        addBarEntry(800, hb_consume);

        return view;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.iv_drawer_index:
                ((MainActivity)getActivity()).showMenu();
                break;
        }
    }

    //-------------------------------------------------------- BMR
    public void initBmrLineChart()
    {
        lc_bmr.setDescription("");
        lc_bmr.setHighlightEnabled(true);
        lc_bmr.setDrawGridBackground(false);
        lc_bmr.setDragEnabled(true);
        lc_bmr.setScaleEnabled(true);
        lc_bmr.setNoDataText(" ");
        XAxis xAxis = lc_bmr.getXAxis();

        Legend mLegend = lc_bmr.getLegend(); // 设置比例图标示

        mLegend.setTextColor(Color.WHITE);//(Color.rgb(107, 181, 77));// 下标颜色
        mLegend.setTextSize(12f);
        //不显示
        mLegend.setEnabled(false);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisLineColor(Color.WHITE);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setSpaceBetweenLabels(2);
        YAxis leftAxis = lc_bmr.getAxisLeft();
        leftAxis.setLabelCount(5, false);
        leftAxis.setAxisMaxValue(4000);
        leftAxis.setAxisMinValue(0);
        leftAxis.setValueFormatter(new MyValueFormatter());
        leftAxis.setSpaceTop(15f);
        leftAxis.setStartAtZero(true);
        leftAxis.setDrawGridLines(false);
        lc_bmr.getAxisRight().setEnabled(false);
        //隐藏Y轴
        lc_bmr.getAxisLeft().setEnabled(false);
        lc_bmr.setData(new LineData());
        lc_bmr.invalidate();

        //画出他这个年龄的平均BMR
    }

    public void addBmrLineEntry(int bmr) {
        if (bmr < 0)
            return;

        LineData data = lc_bmr.getData();
        if (data != null) {

            LineDataSet set = data.getDataSetByIndex(0);
            if (set == null) {
                set = createBmrSet("Basal metabolic rate");
                data.addDataSet(set);
            }
            data.addXValue("");
            data.addEntry(new Entry(bmr, set.getEntryCount()), 0);
            lc_bmr.notifyDataSetChanged();
            //X轴画15个数 代表半个月周期的变化
            lc_bmr.setVisibleXRangeMaximum(15);
            lc_bmr.moveViewTo(data.getXValCount() - 50, 0.0f,
                    YAxis.AxisDependency.LEFT);
            lc_bmr.invalidate();
        }
    }

    //-------------------------------------------------------- BodyFat
    public void initBodyFatLineChart()
    {
        lc_bodyfat.setDescription("");
        lc_bodyfat.setHighlightEnabled(true);
        lc_bodyfat.setDrawGridBackground(false);
        lc_bodyfat.setDragEnabled(true);
        lc_bodyfat.setScaleEnabled(true);
        lc_bodyfat.setNoDataText(" ");
        XAxis xAxis = lc_bodyfat.getXAxis();

        Legend mLegend = lc_bodyfat.getLegend(); // 设置比例图标示

        mLegend.setTextColor(Color.WHITE);//(Color.rgb(107, 181, 77));// 下标颜色
        mLegend.setTextSize(12f);
        //不显示
        mLegend.setEnabled(false);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisLineColor(Color.WHITE);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setSpaceBetweenLabels(2);
        YAxis leftAxis = lc_bodyfat.getAxisLeft();
        leftAxis.setLabelCount(5, false);
        leftAxis.setAxisMaxValue(100);
        leftAxis.setAxisMinValue(0);
        leftAxis.setValueFormatter(new MyValueFormatter());
        leftAxis.setSpaceTop(15f);
        leftAxis.setStartAtZero(true);
        leftAxis.setDrawGridLines(false);
        lc_bodyfat.getAxisRight().setEnabled(false);
        //隐藏Y轴
        lc_bodyfat.getAxisLeft().setEnabled(false);
        lc_bodyfat.setData(new LineData());
        lc_bodyfat.invalidate();

        //画出他这个年龄的平均BMR
    }

    public void addBodyFatLineEntry(int bodyFat) {
        if (bodyFat < 0)
            return;

        LineData data = lc_bodyfat.getData();
        if (data != null) {

            LineDataSet set = data.getDataSetByIndex(0);
            if (set == null) {
                set = createBmrSet("Body Fat");
                data.addDataSet(set);
            }
            data.addXValue("");
            data.addEntry(new Entry(bodyFat, set.getEntryCount()), 0);
            lc_bodyfat.notifyDataSetChanged();
            //X轴画15个数 代表半个月周期的变化
            lc_bodyfat.setVisibleXRangeMaximum(15);
            lc_bodyfat.moveViewTo(data.getXValCount() - 50, 0.0f,
                    YAxis.AxisDependency.LEFT);
            lc_bodyfat.invalidate();
        }
    }

    //==================通用的数据集创建==================
    private LineDataSet createBmrSet(String title) {
        LineDataSet set = new LineDataSet(null, title);

        set.setColor(Color.rgb(0xFF, 0xFF, 0xFF));
        set.setDrawCubic(true);
        set.setCubicIntensity(0.2f);
        set.setLineWidth(2f);
        set.setCircleSize(5f);
        set.setDrawCircleHole(false);
        set.setDrawValues(false);
        set.setDrawFilled(true);
        set.setFillColor(Color.rgb(0xFF, 0xFF, 0xFF));
        set.setDrawCircles(false);

        return set;
    }

    //-------------------------------------------------------- Intake
    public void initBarChart(HorizontalBarChart mChart, String title, int color) {

        // barChart
        mChart.setDrawBarShadow(false);
        mChart.setDrawBorders(false);
        mChart.setBorderColor(Color.WHITE);
        mChart.setDrawValueAboveBar(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setTouchEnabled(false);
        mChart.setDescription("");
        mChart.setNoDataText("");
        mChart.setDrawGridBackground(false);

        //mChart.getAxisRight().setEnabled(false);
        mChart.getAxisRight().setAxisLineColor(Color.WHITE);
        mChart.getAxisRight().setGridColor(color);
        mChart.getAxisRight().setDrawLabels(false);

        Legend mLegend = mChart.getLegend(); // 设置比例图标示
        mLegend.setEnabled(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisLineColor(Color.WHITE);
        leftAxis.setGridColor(color);
        leftAxis.setDrawLabels(false);
        leftAxis.setEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setAxisLineColor(color);
        xAxis.setGridColor(color);
        xAxis.setDrawLabels(false);

        mChart.setData(new BarData());
        BarData data = mChart.getData();

        BarDataSet set = data.getDataSetByIndex(0);
        if (set == null) {
            ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
            set = new BarDataSet(yVals1, title);
            set.setDrawValues(true);    //柱状图上方的数值显示出来
            set.setValueFormatter(new MyValueFormatter());
            set.setValueTextSize(12f);
            set.setValueTextColor(Color.rgb(255, 255, 255));
            set.setBarSpacePercent(10);
            data.setValueTextColor(color);
            data.addDataSet(set);
            set.setColor(color);
        }
        mChart.invalidate();
    }

    int barValue = -1;

    public void addBarEntry(int value, HorizontalBarChart mChart) {

        barValue = value;
        BarData data = mChart.getData();

        BarDataSet set = data.getDataSetByIndex(0);

        //data.addYValue("");
        set.addEntry(new BarEntry(value, set.getEntryCount()));

        mChart.notifyDataSetChanged();

        mChart.invalidate();

    }

    //-------------------------------------------------------- Consume
    public void initConsumeBarChart(BarChart mChart, String title, int color) {

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
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        Legend mLegend = mChart.getLegend(); // 设置比例图标示

        mLegend.setTextColor(Color.GRAY);//(Color.rgb(107, 181, 77));// 颜色
        mLegend.setTextSize(12f);

        YAxis leftAxis = mChart.getAxisLeft();
        //leftAxis.setTextColor(Color.rgb(107, 181, 77));
        leftAxis.setLabelCount(5, false);
        leftAxis.setTextSize(12f);
        leftAxis.setTextColor(Color.GRAY);
        leftAxis.setValueFormatter(new MyValueFormatter());
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setDrawGridLines(false);
        mChart.getAxisRight().setEnabled(false);

        mChart.setData(new BarData());
        BarData data = mChart.getData();


        BarDataSet set = data.getDataSetByIndex(0);
        if (set == null) {
            ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
            set = new BarDataSet(yVals1, title);
            set.setDrawValues(true);    //柱状图上方的数值显示出来
            set.setValueFormatter(new MyValueFormatter());
            set.setValueTextSize(12f);
            set.setValueTextColor(Color.rgb(107, 181, 77));
            set.setBarSpacePercent(10);

            data.addDataSet(set);
            set.setColor(color);
        }
        mChart.invalidate();
    }
}
