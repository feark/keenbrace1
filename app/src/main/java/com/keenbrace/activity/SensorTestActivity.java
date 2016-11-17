package com.keenbrace.activity;


import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.keenbrace.R;
import com.keenbrace.base.BaseActivity;
import com.keenbrace.widget.MyValueFormatter;

import java.util.ArrayList;

public class SensorTestActivity extends BaseActivity implements View.OnClickListener {

    //每次偏差
    BarChart biasBar;
    //角度
    RadarChart angleRadar;
    //距离
    RadarChart distanceRadar;
    //肌电
    LineChart emgLine;

    ImageView btn_valid;    //确认该动作是否有效的按钮

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
        this.setActionBarTitle("KeenBrace Machine Learn");

        btn_valid = (ImageView) findViewById(R.id.iv_valid);

        //初始化肌电波形图
        emgLine = (LineChart) findViewById(R.id.line_emg);
        initEmgLineChart();

        //初始化角度雷达图
        angleRadar = (RadarChart) findViewById(R.id.radar_angle);
        initAngleRadarChart();

        //初始化距离雷达图
        distanceRadar = (RadarChart) findViewById(R.id.radar_distance);
        initDistanceRadarChart();

        //初始化偏差柱状图
        biasBar = (BarChart) findViewById(R.id.bar_bias);
        initBiasBarChart();
    }

    @Override
    public void onClick(View v) {

    }

    //==============================================================
    public void initEmgLineChart(){
        emgLine.setDescription("");
        emgLine.setHighlightEnabled(true);
        emgLine.setDrawGridBackground(false);
        emgLine.setDragEnabled(true);
        emgLine.setScaleEnabled(true);

        XAxis xAxis = emgLine.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setSpaceBetweenLabels(2);

        YAxis leftAxis = emgLine.getAxisLeft();
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

        emgLine.getAxisRight().setEnabled(false);
        emgLine.setData(new LineData());
        emgLine.invalidate();

        Legend mLegend = emgLine.getLegend(); // 设置比例图标示

        mLegend.setTextColor(Color.GRAY);//(Color.rgb(107, 181, 77));// 下标颜色
        mLegend.setTextSize(12f);
    }

    private LineDataSet createSet(String title) {
        LineDataSet set = new LineDataSet(null, "emg");

        set.setLineWidth(2f);
        set.setCircleSize(5f);
        set.setDrawCircleHole(false);
        set.setDrawValues(false);
        set.setDrawFilled(true);
        set.setDrawCircles(false);

        return set;
    }

    int linValue = -1;

    public void addEmgLineEntry(int emg) {
        if (linValue == emg)
            return;
        linValue = emg;
        LineData data = emgLine.getData();
        if (data != null) {

            LineDataSet set = data.getDataSetByIndex(0);
            if (set == null) {
                set = createSet("");
                data.addDataSet(set);
            }
            data.addXValue("");
            data.addEntry(new Entry(emg, set.getEntryCount()), 0);
            emgLine.notifyDataSetChanged();
            emgLine.setVisibleXRangeMaximum(50);
            emgLine.moveViewTo(data.getXValCount() - 50, 0.0f,
                    YAxis.AxisDependency.LEFT);
            emgLine.invalidate();
        }

    }

    public void initAngleRadarChart(){
        // 描述，在底部
        angleRadar.setDescription("Angle");
        // 绘制线条宽度，圆形向外辐射的线条
        angleRadar.setWebLineWidth(1.5f);
        // 内部线条宽度，外面的环状线条
        angleRadar.setWebLineWidthInner(1.0f);
        // 所有线条WebLine透明度
        angleRadar.setWebAlpha(100);

        //更新数据
        setAngleData(angleRadar);

        XAxis xAxis = angleRadar.getXAxis();

        // X坐标值字体大小
        xAxis.setTextSize(12f);

        YAxis yAxis = angleRadar.getYAxis();

        // Y坐标值标签个数
        yAxis.setLabelCount(6, false);
        // Y坐标值字体大小
        yAxis.setTextSize(15f);
        // Y坐标值是否从0开始
        yAxis.setStartAtZero(true);

        Legend l = angleRadar.getLegend();
        // 图例位置
        l.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        // 图例X间距
        l.setXEntrySpace(2f);
        // 图例Y间距
        l.setYEntrySpace(1f);
    }

    private String[] mParties = new String[] {
            "X", "Y", "Z",

    };

    public void setAngleData(RadarChart mChart) {

        float mult = 150;
        int cnt = 3; // 不同的维度Party A、B、C...总个数

        // Y的值，数据填充
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<Entry> yVals2 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        /*
        for (int i = 0; i < cnt; i++) {
            yVals1.add(new Entry((float) (Math.random() * mult) + mult / 2, i));
        }
        */
        yVals1.add(new Entry((float) 85, 0));   //85
        yVals1.add(new Entry((float) 120, 1));
        yVals1.add(new Entry((float) 166, 2));


        for (int i = 0; i < cnt; i++) {
            yVals2.add(new Entry((float) (Math.random() * mult) + mult / 2, i));
        }

        // Party A、B、C..
        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < cnt; i++)
            xVals.add(mParties[i % mParties.length]);

        RadarDataSet set1 = new RadarDataSet(yVals1, " ");
        // Y数据颜色设置
        set1.setColor(/*ColorTemplate.VORDIPLOM_COLORS[0]*/Color.rgb(107, 181, 77));
        // 是否实心填充区域
        set1.setDrawFilled(true);
        // 数据线条宽度
        set1.setLineWidth(2f);

        RadarDataSet set2 = new RadarDataSet(yVals2, " ");
        set2.setColor(/*ColorTemplate.VORDIPLOM_COLORS[4]*/Color.rgb(28, 166, 220));
        set2.setDrawFilled(true);
        set2.setLineWidth(2f);

        ArrayList<RadarDataSet> sets = new ArrayList<RadarDataSet>();
        sets.add(set1);
        //sets.add(set2); 第二个表不画出来

        RadarData data = new RadarData(xVals, sets);

        // 数据字体大小
        data.setValueTextSize(8f);
        // 是否绘制Y值到图表
        data.setDrawValues(true);

        mChart.setData(data);

        mChart.invalidate();
    }

    public void initDistanceRadarChart(){
        // 描述，在底部
        distanceRadar.setDescription("Distance");
        // 绘制线条宽度，圆形向外辐射的线条
        distanceRadar.setWebLineWidth(1.5f);
        // 内部线条宽度，外面的环状线条
        distanceRadar.setWebLineWidthInner(1.0f);
        // 所有线条WebLine透明度
        distanceRadar.setWebAlpha(100);

        setDistanceData(distanceRadar);

        XAxis xAxis = distanceRadar.getXAxis();

        // X坐标值字体大小
        xAxis.setTextSize(12f);

        YAxis yAxis = distanceRadar.getYAxis();

        // Y坐标值标签个数
        yAxis.setLabelCount(6, false);
        // Y坐标值字体大小
        yAxis.setTextSize(15f);
        // Y坐标值是否从0开始
        yAxis.setStartAtZero(true);

        Legend l = distanceRadar.getLegend();
        // 图例位置
        l.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        // 图例X间距
        l.setXEntrySpace(2f);
        // 图例Y间距
        l.setYEntrySpace(1f);
    }

    //leave 应该是距离混合速度
    public void setDistanceData(RadarChart mChart) {

        float mult = 150;
        int cnt = 3; // 不同的维度Party A、B、C...总个数

        // Y的值，数据填充
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<Entry> yVals2 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        /*
        for (int i = 0; i < cnt; i++) {
            yVals1.add(new Entry((float) (Math.random() * mult) + mult / 2, i));
        }
        */
        yVals1.add(new Entry((float) 85, 0));   //85
        yVals1.add(new Entry((float) 120, 1));
        yVals1.add(new Entry((float) 166, 2));


        for (int i = 0; i < cnt; i++) {
            yVals2.add(new Entry((float) (Math.random() * mult) + mult / 2, i));
        }

        // Party A、B、C..
        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < cnt; i++)
            xVals.add(mParties[i % mParties.length]);

        RadarDataSet set1 = new RadarDataSet(yVals1, " ");
        // Y数据颜色设置
        set1.setColor(/*ColorTemplate.VORDIPLOM_COLORS[0]*/Color.rgb(107, 181, 77));
        // 是否实心填充区域
        set1.setDrawFilled(true);
        // 数据线条宽度
        set1.setLineWidth(2f);

        RadarDataSet set2 = new RadarDataSet(yVals2, " ");
        set2.setColor(/*ColorTemplate.VORDIPLOM_COLORS[4]*/Color.rgb(28, 166, 220));
        set2.setDrawFilled(true);
        set2.setLineWidth(2f);

        ArrayList<RadarDataSet> sets = new ArrayList<RadarDataSet>();
        sets.add(set1);
        //sets.add(set2); 第二个表不画出来

        RadarData data = new RadarData(xVals, sets);

        // 数据字体大小
        data.setValueTextSize(8f);
        // 是否绘制Y值到图表
        data.setDrawValues(true);

        mChart.setData(data);

        mChart.invalidate();
    }

    public void initBiasBarChart(){
        // barChart
        biasBar.setDrawBarShadow(false);
        biasBar.setDrawValueAboveBar(true);
        biasBar.setDragEnabled(true);
        biasBar.setScaleEnabled(true);
        biasBar.setTouchEnabled(false);
        biasBar.setDescription("");
        biasBar.setNoDataText("");
        //biasBar.setMaxVisibleValueCount(60);
        biasBar.setDrawGridBackground(false);

        XAxis xAxis = biasBar.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        Legend mLegend = biasBar.getLegend(); // 设置比例图标示

        mLegend.setTextColor(Color.GRAY);//(Color.rgb(107, 181, 77));// 颜色
        mLegend.setTextSize(12f);

        YAxis leftAxis = biasBar.getAxisLeft();
        //leftAxis.setTextColor(Color.rgb(107, 181, 77));
        leftAxis.setLabelCount(5, false);
        leftAxis.setTextSize(12f);
        leftAxis.setTextColor(Color.GRAY);
        leftAxis.setValueFormatter(new MyValueFormatter());
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setDrawGridLines(false);
        biasBar.getAxisRight().setEnabled(false);

        biasBar.setData(new BarData());
        BarData data = biasBar.getData();


        BarDataSet set = data.getDataSetByIndex(0);
        if (set == null) {
            ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
            set = new BarDataSet(yVals1, "Movement Bias");
            set.setDrawValues(true);    //柱状图上方的数值显示出来
            set.setValueFormatter(new MyValueFormatter());
            set.setValueTextSize(12f);
            set.setValueTextColor(Color.rgb(107, 181, 77));
            set.setBarSpacePercent(10);

            data.addDataSet(set);
            set.setColor(Color.rgb(28, 166, 220));
        }
        biasBar.invalidate();
    }

    public void addBiasEntry(int bias, BarChart mChart) {
        BarData data = mChart.getData();

        BarDataSet set = data.getDataSetByIndex(0);

        //data.addXValue("");
        set.addEntry(new BarEntry(bias, set.getEntryCount()));
        mChart.notifyDataSetChanged();
        mChart.setVisibleXRangeMaximum(100);
        mChart.moveViewTo(data.getXValCount() - 100, 0.0f,
                YAxis.AxisDependency.LEFT);
        mChart.invalidate();

    }
}
