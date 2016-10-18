package com.keenbrace.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.keenbrace.R;
import com.keenbrace.base.BaseActivity;
import com.keenbrace.constants.UtilConstants;
import com.keenbrace.greendao.CommonResult;
import com.keenbrace.widget.MyValueFormatter;

//

//

public class InsightActivity extends BaseActivity implements View.OnClickListener {

    LineChart lcCadence, lcPress, lcVertical, lcStability, lcEmg;

    int sport_type;
    CommonResult commonResult;
    int mileage_km = 0;
    int minutes;

    byte[] cadencePerMin = new byte[600];
    byte[] kneePressPerMin = new byte[600];
    byte[] verticalPerMin = new byte[600];
    byte[] stabilityPerMin = new byte[600];
    byte[] emgPerKm = new byte[50];

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected boolean hasActionBar() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_insight;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        this.setActionBarTitle("Insights");

        lcCadence = (LineChart) findViewById(R.id.cadenceChart);
        lcCadence.animateX(1500);
        initCadenceChart();

        lcPress = (LineChart) findViewById(R.id.pressChart);
        lcPress.animateX(1500);
        initPressChart();

        lcVertical = (LineChart) findViewById(R.id.verticalChart);
        lcVertical.animateX(1500);
        initVerticalChart();

        lcEmg = (LineChart) findViewById(R.id.emgChart);
        lcEmg.animateX(1500);
        initEmgChart();

        lcStability = (LineChart) findViewById(R.id.stabilityChart);
        lcStability.animateX(1500);
        initStabilityChart();

        //如果是跑步以外的运动就将这些图表变一变 与其他运动并用 leave
        //EMG
        //每一下的运动与静止时间
        //每一下的稳定度

        //得到数据并加载到图表中
        //得到数据
        commonResult = (CommonResult) this.getIntent().getSerializableExtra("CommonResult");

        //得到运动种类
        sport_type = this.getIntent().getIntExtra("sport_type", 0);

        if(sport_type == UtilConstants.sport_running){
            minutes = commonResult.getMinuteCount();    //得到时间长度
            mileage_km = commonResult.getMileage();     //得到公里数

            cadencePerMin = commonResult.getCadencePerKm();
            kneePressPerMin = commonResult.getKneePress();
            verticalPerMin = commonResult.getVertOsci();
            stabilityPerMin = commonResult.getStability();
            emgPerKm = commonResult.getEmgDecrease();

            for(int n=0; n<50; n++)
            {
                addEmgDataEntry(emgPerKm[n] & 0xff);
            }

            if(minutes == 0)
            {
                addCadenceDataEntry(1);
                addPressDataEntry(1);
                addVerticalDataEntry(1);
                addStabilityDataEntry(1);
                addEmgDataEntry(1);
            }
            else
            {
                for(int n=0; n<minutes; n++){
                    addCadenceDataEntry(cadencePerMin[n] & 0xff);
                    addPressDataEntry(kneePressPerMin[n] & 0xff);
                    addVerticalDataEntry(verticalPerMin[n] & 0xff);
                    addStabilityDataEntry(stabilityPerMin[n] & 0xff);
                }
            }
        }

    }

    @Override
    public void onClick(View v) {

    }

    //==============================================================
    public void initCadenceChart(){
        lcCadence.setDescription("");
        lcCadence.setHighlightEnabled(true);
        lcCadence.setDrawGridBackground(false);
        lcCadence.setDragEnabled(true);
        lcCadence.setScaleEnabled(true);

        XAxis xAxis = lcCadence.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setSpaceBetweenLabels(2);

        YAxis leftAxis = lcCadence.getAxisLeft();
        leftAxis.setLabelCount(5, false);
        leftAxis.setAxisMaxValue(280);
        leftAxis.setAxisMinValue(0);
        leftAxis.setValueFormatter(new MyValueFormatter());
        leftAxis.setTextColor(Color.GRAY);
        leftAxis.setSpaceTop(15f);
        leftAxis.setStartAtZero(true);
        leftAxis.setDrawGridLines(false);

        LimitLine cadenceStdLine = new LimitLine(180f, "");
        cadenceStdLine.setLineWidth(4f);
        cadenceStdLine.enableDashedLine(10f, 10f, 0f);
        cadenceStdLine.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        cadenceStdLine.setTextSize(10f);
        cadenceStdLine.setLineColor(Color.rgb(107, 181, 77));
        leftAxis.addLimitLine(cadenceStdLine);

        lcCadence.getAxisRight().setEnabled(false);
        lcCadence.setData(new LineData());
        lcCadence.invalidate();

        Legend mLegend = lcCadence.getLegend(); // 设置比例图标示

        mLegend.setTextColor(Color.GRAY);//(Color.rgb(107, 181, 77));// 下标颜色
        mLegend.setTextSize(12f);
    }

    public void initPressChart(){
        lcPress.setDescription("");
        lcPress.setHighlightEnabled(true);
        lcPress.setDrawGridBackground(false);
        lcPress.setDragEnabled(true);
        lcPress.setScaleEnabled(true);
        XAxis xAxis = lcPress.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setSpaceBetweenLabels(2);

        YAxis leftAxis = lcPress.getAxisLeft();
        leftAxis.setLabelCount(5, false);
        leftAxis.setAxisMaxValue(12);
        leftAxis.setAxisMinValue(0);
        leftAxis.setValueFormatter(new MyValueFormatter());
        leftAxis.setTextColor(Color.GRAY);
        leftAxis.setSpaceTop(15f);
        leftAxis.setStartAtZero(true);
        leftAxis.setDrawGridLines(false);

        LimitLine PressStdLine = new LimitLine(6f, "");
        PressStdLine.setLineWidth(4f);
        PressStdLine.enableDashedLine(10f, 10f, 0f);
        PressStdLine.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        PressStdLine.setTextSize(10f);
        PressStdLine.setLineColor(Color.rgb(28, 166, 220));
        leftAxis.addLimitLine(PressStdLine);

        lcPress.getAxisRight().setEnabled(false);
        lcPress.setData(new LineData());
        lcPress.invalidate();

        Legend mLegend = lcPress.getLegend(); // 设置比例图标示

        mLegend.setTextColor(Color.GRAY);//(Color.rgb(107, 181, 77));// 下标颜色
        mLegend.setTextSize(12f);
    }

    public void initVerticalChart(){
        lcVertical.setDescription("");
        lcVertical.setHighlightEnabled(true);
        lcVertical.setDrawGridBackground(false);
        lcVertical.setDragEnabled(true);
        lcVertical.setScaleEnabled(true);
        XAxis xAxis = lcVertical.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setSpaceBetweenLabels(2);
        YAxis leftAxis = lcVertical.getAxisLeft();
        leftAxis.setLabelCount(5, false);
        leftAxis.setAxisMaxValue(30);
        leftAxis.setAxisMinValue(0);
        leftAxis.setValueFormatter(new MyValueFormatter());
        leftAxis.setTextColor(Color.GRAY);
        leftAxis.setSpaceTop(15f);
        leftAxis.setStartAtZero(true);
        leftAxis.setDrawGridLines(false);

        LimitLine verticalStdLine = new LimitLine(10f, "");
        verticalStdLine.setLineWidth(4f);
        verticalStdLine.enableDashedLine(10f, 10f, 0f);
        verticalStdLine.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        verticalStdLine.setTextSize(10f);
        verticalStdLine.setLineColor(Color.rgb(28, 166, 220));
        leftAxis.addLimitLine(verticalStdLine);

        lcVertical.getAxisRight().setEnabled(false);
        lcVertical.setData(new LineData());
        lcVertical.invalidate();

        Legend mLegend = lcVertical.getLegend(); // 设置比例图标示

        mLegend.setTextColor(Color.GRAY);//(Color.rgb(107, 181, 77));// 下标颜色
        mLegend.setTextSize(12f);
    }

    public void initEmgChart(){
        lcEmg.setDescription("");
        lcEmg.setHighlightEnabled(true);
        lcEmg.setDrawGridBackground(false);
        lcEmg.setDragEnabled(true);
        lcEmg.setScaleEnabled(true);
        XAxis xAxis = lcEmg.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setSpaceBetweenLabels(2);
        YAxis leftAxis = lcEmg.getAxisLeft();
        leftAxis.setLabelCount(5, false);
        leftAxis.setAxisMaxValue(50);
        leftAxis.setAxisMinValue(0);
        leftAxis.setValueFormatter(new MyValueFormatter());
        leftAxis.setTextColor(Color.GRAY);
        leftAxis.setSpaceTop(15f);
        leftAxis.setStartAtZero(true);
        leftAxis.setDrawGridLines(false);
        lcEmg.getAxisRight().setEnabled(false);
        lcEmg.setData(new LineData());
        lcEmg.invalidate();

        Legend mLegend = lcEmg.getLegend(); // 设置比例图标示

        mLegend.setTextColor(Color.GRAY);//(Color.rgb(107, 181, 77));// 下标颜色
        mLegend.setTextSize(12f);
    }

    public void initStabilityChart(){
        lcStability.setDescription("");
        lcStability.setHighlightEnabled(true);
        lcStability.setDrawGridBackground(false);
        lcStability.setDragEnabled(true);
        lcStability.setScaleEnabled(true);
        XAxis xAxis = lcStability.getXAxis();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setSpaceBetweenLabels(2);
        YAxis leftAxis = lcStability.getAxisLeft();
        leftAxis.setLabelCount(5, false);
        leftAxis.setAxisMaxValue(200);
        leftAxis.setAxisMinValue(0);
        leftAxis.setValueFormatter(new MyValueFormatter());
        leftAxis.setTextColor(Color.GRAY);
        leftAxis.setSpaceTop(15f);
        leftAxis.setStartAtZero(true);
        leftAxis.setDrawGridLines(false);
        lcStability.getAxisRight().setEnabled(false);
        lcStability.setData(new LineData());
        lcStability.invalidate();

        Legend mLegend = lcStability.getLegend(); // 设置比例图标示

        mLegend.setTextColor(Color.GRAY);//(Color.rgb(107, 181, 77));// 下标颜色
        mLegend.setTextSize(12f);
    }

    public void addCadenceDataEntry(int cadence){
        LineData data = lcCadence.getData();
        if (data != null) {

            LineDataSet set = data.getDataSetByIndex(0);
            if (set == null) {
                set = createSet("Cadence");
                data.addDataSet(set);
            }

            data.addXValue("");
            data.addEntry(new Entry(cadence, set.getEntryCount()), 0);

            lcCadence.notifyDataSetChanged();
            lcCadence.setVisibleXRangeMaximum(10);
            lcCadence.moveViewTo(data.getXValCount() - 50, 0.0f,
                    YAxis.AxisDependency.LEFT);
            lcCadence.invalidate();
        }
    }

    public void addPressDataEntry(int press){
        LineData data = lcPress.getData();
        if (data != null) {

            LineDataSet set = data.getDataSetByIndex(0);
            if (set == null) {
                set = createSet("Knee Press");
                data.addDataSet(set);
            }
            data.addXValue("");
            data.addEntry(new Entry(press, set.getEntryCount()), 0);
            lcPress.notifyDataSetChanged();
            lcPress.setVisibleXRangeMaximum(10);
            lcPress.moveViewTo(data.getXValCount() - 50, 0.0f,
                    YAxis.AxisDependency.LEFT);
            lcPress.invalidate();
        }
    }

    public void addVerticalDataEntry(int vertical){
        LineData data = lcVertical.getData();
        if (data != null) {

            LineDataSet set = data.getDataSetByIndex(0);
            if (set == null) {
                set = createSet("Vertical oscillation");
                data.addDataSet(set);
            }
            data.addXValue("");
            data.addEntry(new Entry(vertical, set.getEntryCount()), 0);
            lcVertical.notifyDataSetChanged();
            lcVertical.setVisibleXRangeMaximum(10);
            lcVertical.moveViewTo(data.getXValCount() - 50, 0.0f,
                    YAxis.AxisDependency.LEFT);
            lcVertical.invalidate();
        }
    }

    public void addStabilityDataEntry(int stability){
        LineData data = lcStability.getData();
        if (data != null) {

            LineDataSet set = data.getDataSetByIndex(0);
            if (set == null) {
                set = createSet("Instability");
                data.addDataSet(set);
            }
            data.addXValue("");
            data.addEntry(new Entry(stability, set.getEntryCount()), 0);
            lcStability.notifyDataSetChanged();
            lcStability.setVisibleXRangeMaximum(10);
            lcStability.moveViewTo(data.getXValCount() - 50, 0.0f,
                    YAxis.AxisDependency.LEFT);
            lcStability.invalidate();
        }
    }

    public void addEmgDataEntry(int emg){
        LineData data = lcEmg.getData();
        if (data != null) {

            LineDataSet set = data.getDataSetByIndex(0);
            if (set == null) {
                set = createSet("Muscle energy loss");
                data.addDataSet(set);
            }
            data.addXValue("");
            data.addEntry(new Entry(emg, set.getEntryCount()), 0);
            lcEmg.notifyDataSetChanged();
            lcEmg.setVisibleXRangeMaximum(10);
            lcEmg.moveViewTo(data.getXValCount() - 50, 0.0f,
                    YAxis.AxisDependency.LEFT);
            lcEmg.invalidate();
        }
    }

    private LineDataSet createSet(String title) {
        LineDataSet set = new LineDataSet(null, title);

        set.setLineWidth(2f);
        set.setCircleSize(5f);
        set.setValueTextColor(Color.GRAY);
        set.setDrawCircleHole(false);
        set.setDrawValues(false);
        set.setDrawFilled(true);
        set.setDrawCircles(false);

        return set;
    }
}
