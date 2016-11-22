package com.keenbrace.activity;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.keenbrace.R;
import com.keenbrace.constants.UtilConstants;
import com.keenbrace.util.DateUitl;
import com.keenbrace.widget.CircularProgressBar;
import com.keenbrace.widget.GifView;
import com.keenbrace.widget.MyValueFormatter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class FragmentRun extends Fragment implements OnClickListener {

    TextView tv_factors, tv_sumtimes, tv_steprate, tv_stride, tv_step, tv_speed, tv_distance, tv_calories;
    CircularProgressBar pd_circle3;
    RelativeLayout rl_time;
    LinearLayout ll_rundatabox;
    private long id = -1;
    float weight = 65;

    ImageView indicate_box;

    //因运动不同而会有不同功能的按钮
    TextView indicate_message;

    RelativeLayout rl_countdown;
    TextView tx_count_title;
    TextView restTime;
    int count_minutes, count_seconds;

    LineChart lc_muscle;

    //不同的运动种类显示不同动画
    GifView gif_anima;
    int sport_type = 0;
    int start_from = 0;

    int delay_times;

    //------------------------------------------------------
    //运动参数相关public static final
    Boolean isAnyMove = false;
    TextView reps_number;
    //------------------------------------------------------

    RelativeLayout rl_cadence, rl_stride, rl_step, rl_sporttitle;
    TextView sport_title;
    TextView train_target;
    TextView tv_speed_t, tv_distance_t, tv_calories_t;

    //------------------------------------------------------
    //挑战或计划的界同调度
    boolean isStartRun = false;
    //------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frame_workout, null);

        ll_rundatabox = (LinearLayout) view.findViewById(R.id.rundatabox);
        ll_rundatabox.setOnClickListener(this);

        rl_cadence = (RelativeLayout) view.findViewById(R.id.rl_cadence);
        rl_stride = (RelativeLayout) view.findViewById(R.id.rl_stride);
        rl_step = (RelativeLayout) view.findViewById(R.id.rl_step);
        rl_sporttitle = (RelativeLayout) view.findViewById(R.id.rl_sporttitle);
        sport_title = (TextView) view.findViewById(R.id.sporttitle);
        train_target = (TextView) view.findViewById(R.id.train_target);
        tv_speed_t = (TextView)view.findViewById(R.id.tv_speed_t);
        tv_distance_t = (TextView) view.findViewById(R.id.tv_distance_t);
        tv_calories_t = (TextView) view.findViewById(R.id.tv_calories_t);

        lc_muscle = (LineChart) view.findViewById(R.id.lc_muscle);
        lc_muscle.setVisibility(View.GONE);

        initLineChart(view);

        //倒计时秒表
        rl_countdown = (RelativeLayout) view.findViewById(R.id.rl_countdown);
        restTime = (TextView) view.findViewById(R.id.restTime);

        tx_count_title = (TextView) view.findViewById(R.id.tx_count_title);

        indicate_message = (TextView) view.findViewById(R.id.indicate_message);
        indicate_box = (ImageView) view.findViewById(R.id.indicate_box);
        indicate_box.setOnClickListener(this);

        indicate_box.setVisibility(View.GONE);
        indicate_message.setVisibility(View.GONE);

        reps_number = (TextView) view.findViewById(R.id.reps_number);

        //关于如何显示gif ken
        gif_anima = (GifView) view.findViewById(R.id.gif_animation);
        sport_type = getArguments().getInt("sport_type");

        start_from = getArguments().getInt("StartFrom");

        indicate_message.setText(R.string.tx_indicate_tap);

        //不同运动显示不同的GIF和提示框信息
        switch(sport_type) {
            case UtilConstants.sport_running:
                gif_anima.setMovieResource(R.raw.still_male);

                indicate_box.setVisibility(View.VISIBLE);
                indicate_message.setVisibility(View.VISIBLE);

                lc_muscle.setVisibility(View.GONE);

                rl_cadence.setVisibility(View.VISIBLE);
                rl_stride.setVisibility(View.VISIBLE);
                rl_step.setVisibility(View.VISIBLE);
                rl_sporttitle.setVisibility(View.GONE);
                break;

            case UtilConstants.sport_dumbbell:
                //beginning animation
                gif_anima.setMovieResource(R.raw.dumbbellcount);
                gif_anima.setPaused(true);

                indicate_box.setVisibility(View.GONE);
                indicate_message.setVisibility(View.GONE);

                lc_muscle.setVisibility(View.VISIBLE);

                rl_cadence.setVisibility(View.GONE);
                rl_stride.setVisibility(View.GONE);
                rl_step.setVisibility(View.GONE);
                rl_sporttitle.setVisibility(View.VISIBLE);

                //直接在原有的界面资源里设置成不同的内容 共用 要注意区分
                sport_title.setText("Dumb bell");
                train_target.setText("Biceps");

                tv_speed_t.setText("Muscle Activation");
                tv_distance_t.setText("Duration");
                tv_calories_t.setText("Stability");
                break;

            case UtilConstants.sport_squat:
                gif_anima.setMovieResource(R.raw.squat);
                gif_anima.setPaused(true);

                indicate_box.setVisibility(View.GONE);
                indicate_message.setVisibility(View.GONE);

                lc_muscle.setVisibility(View.VISIBLE);

                sport_title.setText("Squat");
                train_target.setText("Quadriceps");

                tv_speed_t.setText("Muscle Activation");
                tv_distance_t.setText("Duration");
                tv_calories_t.setText("Stability");
                break;

            case UtilConstants.sport_plank:
                gif_anima.setMovieResource(R.raw.still_male);
                //gif_anima.setMovieResource(R.raw.plank);
                //gif_anima.setPaused(true);

                indicate_box.setVisibility(View.GONE);
                indicate_message.setVisibility(View.GONE);

                lc_muscle.setVisibility(View.VISIBLE);

                sport_title.setText("Plank");
                train_target.setText("Core");

                tv_speed_t.setText("Muscle Activation");
                tv_distance_t.setText("Duration");
                tv_calories_t.setText("Stability");
                break;

            case UtilConstants.sport_pullup:
                sport_title.setText("Pull up");
                train_target.setText("Lats");

                indicate_box.setVisibility(View.GONE);
                indicate_message.setVisibility(View.GONE);

                lc_muscle.setVisibility(View.VISIBLE);

                tv_speed_t.setText("Muscle Activation");
                tv_distance_t.setText("Duration");
                tv_calories_t.setText("Stability");

                gif_anima.setMovieResource(R.raw.pull_up);
                gif_anima.setPaused(true);

                break;

            case UtilConstants.sport_pushup:
                gif_anima.setMovieResource(R.raw.basicpushup);
                gif_anima.setPaused(true);

                indicate_box.setVisibility(View.GONE);
                indicate_message.setVisibility(View.GONE);

                lc_muscle.setVisibility(View.VISIBLE);

                sport_title.setText("Push Up");
                train_target.setText("Chest");

                tv_speed_t.setText("Muscle Activation");
                tv_distance_t.setText("Duration");
                tv_calories_t.setText("Stability");
                break;

            case UtilConstants.sport_closestancesquat:
                gif_anima.setMovieResource(R.raw.closestancefrontsquat);
                gif_anima.setPaused(true);

                indicate_box.setVisibility(View.GONE);
                indicate_message.setVisibility(View.GONE);

                lc_muscle.setVisibility(View.VISIBLE);

                sport_title.setText("Dumbbells Squat");
                train_target.setText("Quadriceps");

                tv_speed_t.setText("Muscle Activation");
                tv_distance_t.setText("Duration");
                tv_calories_t.setText("Stability");
                break;

            case UtilConstants.sport_bicyclesitup:
                gif_anima.setMovieResource(R.raw.bicyclesitup);
                gif_anima.setPaused(true);

                indicate_box.setVisibility(View.GONE);
                indicate_message.setVisibility(View.GONE);

                lc_muscle.setVisibility(View.VISIBLE);

                sport_title.setText("Bicycle Sit Up");
                train_target.setText("Core");

                tv_speed_t.setText("Muscle Activation");
                tv_distance_t.setText("Duration");
                tv_calories_t.setText("Stability");
                break;

            default:
                break;
        }

        pd_circle3 = (CircularProgressBar) view.findViewById(R.id.pd_circle_blue);
        tv_factors = (TextView) view.findViewById(R.id.tv_factors);
        tv_sumtimes = (TextView) view.findViewById(R.id.tv_sumtimes);
        tv_steprate = (TextView) view.findViewById(R.id.tv_steprate);
        tv_stride = (TextView) view.findViewById(R.id.tv_stride);
        tv_step = (TextView) view.findViewById(R.id.tv_step);
        tv_speed = (TextView) view.findViewById(R.id.tv_speed);
        tv_distance = (TextView) view.findViewById(R.id.tv_distance);
        tv_calories = (TextView) view.findViewById(R.id.tv_calories);
        rl_time = (RelativeLayout) view.findViewById(R.id.rl_time);

        pd_circle3.setCircleWidth(30);
        rl_time.setOnClickListener(this);
        return view;
    }

    int ancount = 0;

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.indicate_box:

                /*
                updateAnimation(ancount, 1);

                if(ancount <= UtilConstants.eventWalk)
                {
                    ancount++;
                }
                */
                showCountDown(0, 35, true);
                break;

            case R.id.rundatabox:
                updateAnimation(0, 1);
                break;
        }

    }

    public void updateBleId(long id) {
        this.id = id;
    }

    public void updateSpeed(float speed) {
        tv_speed.setText("" + DateUitl.formatToM(speed / 100000)
                + "km/h");
    }

    public void updateView(int factors, int steprate, int stride, int step, long mins, float distance) {

        tv_factors.setText("" + factors);
        tv_steprate.setText(steprate + "/min");
        String stride_str = "Great";
        if (stride < 70)
            stride_str = "Great";
        else if (stride > 90 && stride < 110)
            stride_str = "Good";
        else if (stride >= 110 && stride < 130)
            stride_str = "Average";
        else if (stride >= 130 && stride < 160)
            stride_str = "Bad";
        else if (stride > 160)
            stride_str = "Risk";
        tv_stride.setText(stride_str);
        if (step != 0 && distance == 0) {
            distance = (int) (step * 74);// ����㷨��δʵ�� **

        }
        tv_step.setText("" + step);

        float calories;
        String ss = "m";
        String kk = "kcal";
        if (distance > 100000) {
            calories = UtilConstants.Weight * distance * 1.306f / 100.0f;
            distance = distance / 100000.0f;
            ss = "km";
            kk = "kcal";
        }
        else
        {
            distance = distance / 100.0f;
            calories = UtilConstants.Weight * distance * 1.306f;
        }

        tv_distance.setText("" + DateUitl.formatToM(distance) + ss);
        tv_calories.setText("" + DateUitl.formatToM(calories / 1000.0f) + kk);
    }

    //刷新参数框
    //tv_speed to muscle used
    //tv_distance to duration
    //tv_calories to stability
    public void updateParaBox(int reps, int muscle, int duration, int stability)
    {
        float minisec;
        minisec = duration/1000;
        reps_number.setText(""+reps);
        tv_speed.setText(""+muscle+" %");
        tv_distance.setText("" + minisec + " sec");
        tv_calories.setText("" + stability + " %");

        //addLineEntry(muscle);
    }

    // 显示倒计时
    public void showCountDown(int minute, int second, Boolean enShow)
    {
        //显示倒计时
        if(enShow) {
            rl_countdown.setVisibility(View.VISIBLE);
            tx_count_title.setText("Rest");
            restTime.setText(String.format("%02d : %02d", minute, second));

            gif_anima.setVisibility(View.GONE);
        }else
        {
            rl_countdown.setVisibility(View.GONE);
            gif_anima.setVisibility(View.VISIBLE);
        }
    }

    //gif动画放在这个位置update ken
    //有动作才会进到这个函数 也同时更新indicate_message ken
    //添加一个已经在播放的变量 没播放不播下一次
    static boolean isPlaying = false;

    public void updateAnimation(int anino, int cadence){
        isAnyMove = true;

        //让图片从最开始播放
        gif_anima.setMovieTime(0);

        switch(sport_type) {
            case UtilConstants.sport_running:

                indicate_message.setText(UtilConstants.event2str[anino]);

                if(anino == UtilConstants.eventGeneral)
                {
                    gif_anima.setMovieResource(R.raw.normalrun_male);
                }

                if(anino == UtilConstants.eventCadence)
                {
                    //显示当前步频和建议步频 数字
                    //隐藏动画 显示倒计时元素
                    rl_countdown.setVisibility(View.VISIBLE);
                    tx_count_title.setText("Cadence");
                    restTime.setText(String.format("%d\\min", cadence));

                    gif_anima.setVisibility(View.GONE);
                }
                else
                {
                    rl_countdown.setVisibility(View.GONE);
                    gif_anima.setVisibility(View.VISIBLE);
                }

                if(anino == UtilConstants.eventLean)
                {
                    gif_anima.setMovieResource(R.raw.normalrun_male);
                }

                if(anino == UtilConstants.eventStride)
                {
                    gif_anima.setMovieResource(R.raw.big_stride_male);
                }

                if(anino == UtilConstants.eventLand)
                {
                    gif_anima.setMovieResource(R.raw.land_softer_male);
                }

                //这项应该是脚太直 现在没有这一项
                if(anino == UtilConstants.eventDrive)
                {
                    gif_anima.setMovieResource(R.raw.big_bouncing_male);
                }

                if(anino == UtilConstants.eventBounce)
                {
                    gif_anima.setMovieResource(R.raw.bouncing_male);
                }

                if(anino == UtilConstants.eventStability)
                {
                    //显示一个瞄准中心的动画
                    gif_anima.setMovieResource(R.raw.bouncing_male);
                }

                if(anino == UtilConstants.eventFoot)
                {
                    gif_anima.setMovieResource(R.raw.lift_faster);
                }

                if(anino == UtilConstants.eventReach)
                {
                    gif_anima.setMovieResource(R.raw.big_stride_male);
                }

                if(anino == UtilConstants.eventCalf)
                {
                    gif_anima.setMovieResource(R.raw.small_stride_male);
                }

                if(anino == UtilConstants.eventCog)
                {
                    gif_anima.setMovieResource(R.raw.small_stride_male);
                }

                if(anino == UtilConstants.eventGravity)
                {
                    gif_anima.setMovieResource(R.raw.small_stride_male);
                }

                if(anino == UtilConstants.eventEnergy)
                {
                    //箭头
                    gif_anima.setMovieResource(R.raw.small_stride_male);
                }

                if(anino == UtilConstants.eventRest)
                {
                    gif_anima.setMovieResource(R.raw.rest_male);
                }

                if(anino == UtilConstants.eventToe)
                {
                    gif_anima.setMovieResource(R.raw.spring_toe);
                }

                if(anino == UtilConstants.eventSprint)
                {
                    gif_anima.setMovieResource(R.raw.normalrun_male);
                }

                if(anino == UtilConstants.eventSwingArm)
                {
                    gif_anima.setMovieResource(R.raw.bouncing_male);
                }

                if(anino == UtilConstants.eventStill)
                {
                    gif_anima.setMovieResource(R.raw.still_male);
                }

                if(anino == UtilConstants.eventNormalRun)
                {
                    gif_anima.setMovieResource(R.raw.normalrun_male);
                }

                if(anino == UtilConstants.eventWalk)
                {
                    gif_anima.setMovieResource(R.raw.walking);
                }

                break;

            case UtilConstants.sport_dumbbell:
                if(isPlaying == false) {
                    isPlaying = true;
                    delay_times = 26;
                    handler.sendEmptyMessageDelayed(1, 100);
                }
                break;

            case UtilConstants.sport_squat:
                if(isPlaying == false) {
                    isPlaying = true;
                    delay_times = 25;
                    handler.sendEmptyMessageDelayed(1, 100);
                }
                break;

            case UtilConstants.sport_plank:
                gif_anima.setMovieResource(R.raw.plank);
                break;

            case UtilConstants.sport_pullup:
                if(isPlaying == false) {
                    isPlaying = true;
                    delay_times = 36;
                    handler.sendEmptyMessageDelayed(1, 100);
                }
                break;

            case UtilConstants.sport_pushup:
                if(isPlaying == false) {
                    isPlaying = true;
                    delay_times = 14;
                    handler.sendEmptyMessageDelayed(1, 100);
                }
                break;

            case UtilConstants.sport_closestancesquat:
                if(isPlaying == false) {
                    isPlaying = true;
                    delay_times = 39;
                    handler.sendEmptyMessageDelayed(1, 100);
                }
                break;

            case UtilConstants.sport_bicyclesitup:
                if(isPlaying == false) {
                    isPlaying = true;
                    delay_times = 27;
                    handler.sendEmptyMessageDelayed(1, 100);
                }
                break;

            default:
                break;
        }
    }

    public void updateTime(long mins, int blue, int yellow, int red) {
        //tv_sumtimes.setText(DateUitl.getDateFormat4(mins * 1000) + " mins");
        pd_circle3.setMax((int) mins);
        pd_circle3.setProgress(blue);

    }

    //开始倒计时
    public void CountDownBegin()
    {
        updateTime(60, count_seconds, 0, 0);

        //隐藏动画 显示倒计时元素
        rl_countdown.setVisibility(View.VISIBLE);
        pd_circle3.setVisibility(View.VISIBLE);

        gif_anima.setVisibility(View.GONE);
    }

    //倒计时中刷新界面
    public void DuringCountDown(int minutes, int seconds) {
        restTime.setText(String.format("%02d:%02d", minutes, seconds));
        updateTime(60, seconds, 0, 0);
    }

    //倒计时结束
    public void CountDownEnd()
    {
        //隐藏动画 显示倒计时元素
        rl_countdown.setVisibility(View.GONE);
        pd_circle3.setVisibility(View.GONE);

        gif_anima.setVisibility(View.VISIBLE);
    }

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //延时的方法 ken
                    if (delay_times > 0) {
                        if(sport_type == UtilConstants.sport_dumbbell) {
                            gif_anima.setMovieResource(R.raw.dumbbellcount);
                        }
                        delay_times--;
                        gif_anima.setPaused(false);
                        handler.sendEmptyMessageDelayed(1, 100);
                    }
                    else
                    {
                        if(sport_type == UtilConstants.sport_dumbbell)
                        {
                            gif_anima.setMovieResource(R.raw.dumbbellstand);
                        }
                        gif_anima.setPaused(true);
                        isPlaying = false;
                    }
                    break;

            }
        }
    };

    public void setIsStartRun(boolean v)
    {
        isStartRun = v;

        //重新显示出动画
        gif_anima.setVisibility(View.VISIBLE);
    }

    //--------------------------------------------------------
    public void initLineChart(View view)
    {
        lc_muscle.setDescription("");
        lc_muscle.setHighlightEnabled(true);
        lc_muscle.setDrawGridBackground(false);
        lc_muscle.setDragEnabled(true);
        lc_muscle.setScaleEnabled(true);
        lc_muscle.setNoDataText(" ");
        XAxis xAxis = lc_muscle.getXAxis();

        Legend mLegend = lc_muscle.getLegend(); // 设置比例图标示

        mLegend.setTextColor(Color.GRAY);//(Color.rgb(107, 181, 77));// 下标颜色
        mLegend.setTextSize(12f);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setSpaceBetweenLabels(2);
        YAxis leftAxis = lc_muscle.getAxisLeft();
        leftAxis.setLabelCount(5, false);
        leftAxis.setAxisMaxValue(255);
        leftAxis.setAxisMinValue(0);
        leftAxis.setValueFormatter(new MyValueFormatter());
        leftAxis.setSpaceTop(15f);
        leftAxis.setStartAtZero(true);
        leftAxis.setDrawGridLines(false);
        lc_muscle.getAxisRight().setEnabled(false);
        //隐藏Y轴
        lc_muscle.getAxisLeft().setEnabled(false);
        lc_muscle.setData(new LineData());
        lc_muscle.invalidate();
    }

    int linValue = -1;

    public void addLineEntry(int power) {
        if (linValue == power)
            return;
        linValue = power;
        LineData data = lc_muscle.getData();
        if (data != null) {

            LineDataSet set = data.getDataSetByIndex(0);
            if (set == null) {
                set = createSet("");
                data.addDataSet(set);
            }
            data.addXValue("");
            data.addEntry(new Entry(power, set.getEntryCount()), 0);
            lc_muscle.notifyDataSetChanged();
            lc_muscle.setVisibleXRangeMaximum(50);
            lc_muscle.moveViewTo(data.getXValCount() - 50, 0.0f,
                    YAxis.AxisDependency.LEFT);
            lc_muscle.invalidate();
        }
    }

    private LineDataSet createSet(String title) {
        LineDataSet set = new LineDataSet(null, "muscle activation");

        set.setColor(Color.rgb(0x1C, 0xA6, 0xDC));
        set.setDrawCubic(true);
        set.setCubicIntensity(0.2f);
        set.setLineWidth(2f);
        set.setCircleSize(5f);
        set.setDrawCircleHole(false);
        set.setDrawValues(false);
        set.setDrawFilled(true);
        set.setFillColor(Color.rgb(0x1C, 0xA6, 0xDC));
        set.setDrawCircles(false);

        return set;
    }


}
