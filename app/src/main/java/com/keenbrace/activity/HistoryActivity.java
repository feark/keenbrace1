package com.keenbrace.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.keenbrace.R;
import com.keenbrace.base.BaseActivity;
import com.keenbrace.widget.MyValueFormatter;

import java.util.ArrayList;

public class HistoryActivity extends BaseActivity implements View.OnClickListener {
    ImageView his_backhome;
    RelativeLayout rl_activity;
    RelativeLayout rl_gallery;

    BarChart trainWeekday;
    RadarChart trainRadar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        his_backhome = (ImageView) findViewById(R.id.his_backhome);
        his_backhome.setOnClickListener(this);

        rl_activity = (RelativeLayout) findViewById(R.id.rl_activity);
        rl_activity.setOnClickListener(this);

        rl_gallery = (RelativeLayout) findViewById(R.id.rl_gallery);
        rl_gallery.setOnClickListener(this);

        trainRadar = (RadarChart) findViewById(R.id.train_radar);
        initRadarChart(trainRadar);

        trainWeekday = (BarChart) findViewById(R.id.train_weekday);
        initBarChart(trainWeekday, "Workout sets of the recent week", Color.rgb(28, 166, 220));

        //leave
        addBarEntry(10, trainWeekday);
        addBarEntry(15,trainWeekday);
        addBarEntry(12,trainWeekday);
        addBarEntry(10,trainWeekday);
        addBarEntry(13,trainWeekday);
        addBarEntry(0,trainWeekday);
        addBarEntry(10,trainWeekday);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        this.setActionBarTitle("History");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_history;
    }

    @Override
    protected boolean hasActionBar() {
        return true;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();

        switch (v.getId())
        {
            case R.id.his_backhome:
                finish();
                break;

            case R.id.rl_activity:
                intent.setClass(this, RecordActivity.class);
                startActivity(intent);
                break;

            case R.id.rl_gallery:
                intent.setClass(this, PicwallActivity.class);
                startActivity(intent);
                break;
        }
    }

    //---------------------------------柱状图-------------------------------------//
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
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(true);
        xAxis.setTextColor(Color.GRAY);

        ArrayList<String> xValues = new ArrayList<String>();

        // x轴显示的数据
        xValues.add("MON");
        xValues.add("TUE");
        xValues.add("WED");
        xValues.add("THU");
        xValues.add("FRI");
        xValues.add("SAT");
        xValues.add("SUN");

        mChart.getAxisLeft().setAxisMaxValue(30);
        mChart.getAxisLeft().setAxisMinValue(0);
        //mChart.getXAxis().setSpaceBetweenLabels(7);

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

        mChart.setData(new BarData(xValues));
        BarData data = mChart.getData();

        BarDataSet set = data.getDataSetByIndex(0);
        if (set == null) {
            ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
            set = new BarDataSet(yVals1, title);
            set.setDrawValues(true);    //柱状图上方的数值显示出来
            set.setValueFormatter(new MyValueFormatter());
            set.setValueTextSize(12f);
            set.setValueTextColor(Color.GRAY);
            set.setBarSpacePercent(10);

            data.addDataSet(set);
            set.setColor(color);
        }
        mChart.invalidate();
    }

    public void addBarEntry(int workout, BarChart mChart) {
        BarData data = mChart.getData();

        BarDataSet set = data.getDataSetByIndex(0);

        //data.addXValue("");
        set.addEntry(new BarEntry(workout, set.getEntryCount()));
        mChart.notifyDataSetChanged();
        mChart.setVisibleXRangeMaximum(100);
        mChart.moveViewTo(data.getXValCount() - 100, 0.0f,
                YAxis.AxisDependency.LEFT);
        mChart.invalidate();

    }

    //---------------------------------雷达图-------------------------------------//
    public void initRadarChart(RadarChart mChart)
    {
        // 描述，在底部
        mChart.setDescription("我是描述");
        // 绘制线条宽度，圆形向外辐射的线条
        mChart.setWebLineWidth(1.5f);
        // 内部线条宽度，外面的环状线条
        mChart.setWebLineWidthInner(1.0f);
        // 所有线条WebLine透明度
        mChart.setWebAlpha(100);

        setData(mChart);

        XAxis xAxis = mChart.getXAxis();

        // X坐标值字体大小
        xAxis.setTextSize(12f);

        YAxis yAxis = mChart.getYAxis();

        // Y坐标值标签个数
        yAxis.setLabelCount(6, false);
        // Y坐标值字体大小
        yAxis.setTextSize(15f);
        // Y坐标值是否从0开始
        yAxis.setStartAtZero(true);

        Legend l = mChart.getLegend();
        // 图例位置
        l.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        // 图例X间距
        l.setXEntrySpace(2f);
        // 图例Y间距
        l.setYEntrySpace(1f);
    }

    private String[] mParties = new String[] {
            "Triceps", "Shoulder", "Biceps", "Chest", "Back", "Forearm", "Abs", "Cardio",
            "Glutes", "Upper leg", "Lower leg"
    };

    public void setData(RadarChart mChart) {

        float mult = 150;
        int cnt = 11; // 不同的维度Party A、B、C...总个数

        // Y的值，数据填充
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<Entry> yVals2 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        for (int i = 0; i < cnt; i++) {
            yVals1.add(new Entry((float) (Math.random() * mult) + mult / 2, i));
        }

        for (int i = 0; i < cnt; i++) {
            yVals2.add(new Entry((float) (Math.random() * mult) + mult / 2, i));
        }

        // Party A、B、C..
        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < cnt; i++)
            xVals.add(mParties[i % mParties.length]);

        RadarDataSet set1 = new RadarDataSet(yVals1, "Set 1");
        // Y数据颜色设置
        set1.setColor(/*ColorTemplate.VORDIPLOM_COLORS[0]*/Color.rgb(107, 181, 77));
        // 是否实心填充区域
        set1.setDrawFilled(true);
        // 数据线条宽度
        set1.setLineWidth(2f);

        RadarDataSet set2 = new RadarDataSet(yVals2, "Set 2");
        set2.setColor(/*ColorTemplate.VORDIPLOM_COLORS[4]*/Color.rgb(28, 166, 220));
        set2.setDrawFilled(true);
        set2.setLineWidth(2f);

        ArrayList<RadarDataSet> sets = new ArrayList<RadarDataSet>();
        sets.add(set1);
        //sets.add(set2);

        RadarData data = new RadarData(xVals, sets);

        // 数据字体大小
        data.setValueTextSize(8f);
        // 是否绘制Y值到图表
        data.setDrawValues(true);

        mChart.setData(data);

        mChart.invalidate();
    }

}
