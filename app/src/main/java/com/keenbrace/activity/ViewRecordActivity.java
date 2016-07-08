package com.keenbrace.activity;

//结果页

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnMapLoadedListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.PolylineOptions;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.keenbrace.R;
import com.keenbrace.base.BaseActivity;
import com.keenbrace.constants.UtilConstants;
import com.keenbrace.greendao.RunResult;
import com.keenbrace.widget.CircularProgressBar;
import com.keenbrace.widget.MyValueFormatter;
import com.keenbrace.widget.SwipeListView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import butterknife.Bind;

public class ViewRecordActivity extends BaseActivity implements OnMapLoadedListener {
    private MapView mapView;
    private AMap aMap;

   // @Bind(R.id.lv_data)
    //SwipeListView lvdata;

    int sport_type;

    RunResult run_Result;
    TextView tx_mileage, tx_warings, tx_times, tx_calories;

    //横向柱状图用来做新纪录
    //HorizontalBarChart horChart_record;
    CircularProgressBar circle_workoutMinute;
    CircularProgressBar circle_workoutSecond;

    //柱状图
    BarChart repsNset_barChart;

    //饼图
    PieChart mPieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView.onCreate(savedInstanceState);

    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_view_record;
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
    public void initView() {

        mapView = (MapView) findViewById(R.id.map);

        //横向的柱状图
        //horChart_record = (HorizontalBarChart) findViewById(R.id.horbar_record);
        //initBarChart(horChart_record, "New Record", Color.YELLOW);
        //horChart_record.getAxisLeft().setAxisMaxValue(30);
        //horChart_record.getAxisLeft().setAxisMinValue(0);

        //时间的两个环形进度
        circle_workoutMinute = (CircularProgressBar) findViewById(R.id.circle_workoutMinute);
        circle_workoutSecond = (CircularProgressBar) findViewById(R.id.circle_workoutSecond);

        circle_workoutMinute.setCircleWidth(30);
        circle_workoutSecond.setCircleWidth(20);

        circle_workoutSecond.setMax(60);
        circle_workoutMinute.setMax(60);

        circle_workoutMinute.setRotation(180);
        circle_workoutSecond.setRotation(180);

        circle_workoutSecond.setPrimaryColor(Color.rgb(107, 181, 77));

        //circle_workoutSecond.setProgress(15);
        //circle_workoutMinute.setProgress(35);

        //柱状图
        repsNset_barChart = (BarChart) findViewById(R.id.bar_repsnset);
        initBarChart(repsNset_barChart, "REPS OF EACH SET", Color.rgb(107, 181, 77));

        repsNset_barChart.getAxisLeft().setAxisMaxValue(30);
        repsNset_barChart.getAxisLeft().setAxisMinValue(0);
        repsNset_barChart.getXAxis().setSpaceBetweenLabels(20);

        //横向柱状图  参数是做了多少次/组
        //addRecordBarEntry(10);

        //饼图
        mPieChart = (PieChart) findViewById(R.id.pie_resttime);
        initPieChart();

        //测试柱状图
        addRepsBarEntry(10);
        addRepsBarEntry(20);
        addRepsBarEntry(15);


        //得到数据
        run_Result = (RunResult) this.getIntent().getSerializableExtra("bleData");

        //得到运动种类
        sport_type = this.getIntent().getIntExtra("sport_type", 0);

        init();

        if(sport_type == UtilConstants.sport_running)
        {
            this.setActionBarTitle(getString(R.string.tx_running));
            mapView.setVisibility(View.VISIBLE);

            //horChart_record.setVisibility(View.GONE);
        }
        else
        {
            mapView.setVisibility(View.GONE);

            //horChart_record.setVisibility(View.VISIBLE);

            if(sport_type == UtilConstants.sport_squat)
                this.setActionBarTitle(getString(R.string.tx_squat));

            if(sport_type == UtilConstants.sport_dumbbell)
                this.setActionBarTitle(getString(R.string.tx_dumbbell));

            if(sport_type == UtilConstants.sport_plank)
                this.setActionBarTitle(getString(R.string.tx_plank));

            if(sport_type == UtilConstants.sport_pullup)
                this.setActionBarTitle(getString(R.string.tx_pullup));
        }

    }

    @Override
    public void initData() {

        /*
        float distance = KeenBrace_Sports.getMileage()!=null?KeenBrace_Sports.getMileage():0.0f;
        String ss = "m";
        String kk = "cal";
        if (distance > 100000) {
            distance = distance / 100000.0f;
            ss = "km";
            kk = "kcal";
        } else
            distance = distance / 100.0f;

        float calories = UtilConstants.Weight * distance * 1.306f;
        */

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_record, menu);
        return true;
    }

    /**
     *
            */
    private void init() {


        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setOnMapLoadedListener(this);
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onMapLoaded() {
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(new LatLng(run_Result.getStartlatitude(), run_Result.getStartlongitude())).build();
        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));
        String latlngs = run_Result.getLatLngs();
        double bakx = run_Result.getStartlatitude();
        double baky = run_Result.getStartlongitude();
        Marker startMarker = aMap.addMarker(new MarkerOptions().icon(
                BitmapDescriptorFactory
                        .fromResource(R.mipmap.start_map)).anchor(
                (float) 0.5, (float) 1));

        startMarker.setPosition(new LatLng(bakx, baky));

        Marker endtMarker = aMap.addMarker(new MarkerOptions().icon(
                BitmapDescriptorFactory
                        .fromResource(R.mipmap.end_map)).anchor(
                (float) 0.5, (float) 1));

        endtMarker.setPosition(new LatLng(run_Result.getEndlatitude(), run_Result.getEndlatitude()));

        if (latlngs != null && !"".equals(latlngs)) {
            String[] lng_str = latlngs.split(";");
            if (lng_str != null && lng_str.length > 0) {

                for (String s : lng_str) {
                    String[] vs = s.split(",");

                    //在地图上画线
                    aMap.addPolyline((new PolylineOptions())
                            .add(new LatLng(bakx, baky),
                                    new LatLng(Double.parseDouble(vs[0]), Double.parseDouble(vs[1])))
                            .width(20.0f));

                    bakx = Double.parseDouble(vs[0]);
                    baky = Double.parseDouble(vs[1]);
                }
            }
        }

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

        Legend mLegend = mChart.getLegend(); // 设置比例图标示

        mLegend.setTextColor(Color.rgb(107, 181, 77));// 颜色
        mLegend.setTextSize(12f);

        YAxis leftAxis = mChart.getAxisLeft();
        //leftAxis.setTextColor(Color.rgb(107, 181, 77));
        leftAxis.setLabelCount(5, false);
        leftAxis.setTextSize(12f);
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


    int barValue = -1;

    void addRecordBarEntry(int set) {
        //addBarEntry(set, horChart_record);
    }

    void addRepsBarEntry(int set){addBarEntry(set, repsNset_barChart);}

    public void addBarEntry(int reps, BarChart mChart) {

        barValue = reps;
        BarData data = mChart.getData();

        BarDataSet set = data.getDataSetByIndex(0);

        data.addXValue("");
        set.addEntry(new BarEntry(reps, set.getEntryCount()));
        mChart.notifyDataSetChanged();
        mChart.setVisibleXRangeMaximum(500);
        mChart.moveViewTo(data.getXValCount() - 500, 0.0f,
                AxisDependency.LEFT);
        mChart.invalidate();

    }

    //==================================================================
    //  饼图
    public void initPieChart()
    {
        mPieChart.setUsePercentValues(true);
        mPieChart.setDescription("测试饼图");

        // 设置偏移量
        mPieChart.setExtraOffsets(5, 10, 5, 5);
        // 设置滑动减速摩擦系数
        mPieChart.setDragDecelerationFrictionCoef(0.95f);

        mPieChart.setCenterText("测试饼图，中间文字");

        /*
            设置饼图中心是否是空心的
            true 中间是空心的，环形图
            false 中间是实心的 饼图
         */
        mPieChart.setDrawHoleEnabled(true);
        /*
            设置中间空心圆孔的颜色是否透明
            true 透明的
            false 非透明的
         */
        mPieChart.setHoleColorTransparent(true);
        // 设置环形图和中间空心圆之间的圆环的颜色
        mPieChart.setTransparentCircleColor(Color.WHITE);
        // 设置环形图和中间空心圆之间的圆环的透明度
        mPieChart.setTransparentCircleAlpha(110);

        // 设置圆孔半径
        mPieChart.setHoleRadius(58f);
        // 设置空心圆的半径
        mPieChart.setTransparentCircleRadius(61f);
        // 设置是否显示中间的文字
        mPieChart.setDrawCenterText(true);


        // 设置旋转角度   ？？
        mPieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mPieChart.setRotationEnabled(true);
        //mPieChart.setHighlightPerTapEnabled(false);

        // add a selection listener
        // mPieChart.setOnChartValueSelectedListener(this);

        TreeMap<String, Float> data = new TreeMap<>();
        data.put("data1", 0.5f);
        data.put("data2", 0.3f);
        data.put("data3", 0.1f);
        data.put("data4", 0.1f);
        setData(data);

        // 设置动画
        //mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        // 设置显示的比例
        Legend l = mPieChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
    }

    public void setData(TreeMap<String, Float> data) {
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        int i = 0;
        Iterator it = data.entrySet().iterator();
        while (it.hasNext()) {
            // entry的输出结果如key0=value0等
            Map.Entry entry = (Map.Entry) it.next();
            String key = (String) entry.getKey();
            float value = (float) entry.getValue();
            xVals.add(key);
            yVals1.add(new Entry(value, i++));
        }

        PieDataSet dataSet = new PieDataSet(yVals1, "Election Results");
        // 设置饼图区块之间的距离
        dataSet.setSliceSpace(2f);
        dataSet.setSelectionShift(5f);

        // 添加颜色
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        // dataSet.setSelectionShift(0f);

        PieData data1 = new PieData(xVals, dataSet);
        data1.setValueFormatter(new MyValueFormatter());
        data1.setValueTextSize(10f);
        data1.setValueTextColor(Color.BLACK);
        mPieChart.setData(data1);

        // undo all highlights
        mPieChart.highlightValues(null);

        mPieChart.invalidate();
    }
}
