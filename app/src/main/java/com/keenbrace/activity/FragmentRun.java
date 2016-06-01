package com.keenbrace.activity;


import com.keenbrace.R;
import com.keenbrace.constants.UtilConstants;
import com.keenbrace.util.DateUitl;
import com.keenbrace.widget.CircularProgressBar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FragmentRun extends BaseFragment implements OnClickListener {

    TextView tv_factors, tv_sumtimes, tv_steprate, tv_stride, tv_step, tv_speed, tv_distance, tv_calories;
    CircularProgressBar pd_circle1, pd_circle2, pd_circle3;
    RelativeLayout rl_time;
    private long id = -1;
    float weight = 65;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frame_run, null);
        pd_circle1 = (CircularProgressBar) view.findViewById(R.id.pd_circle_red);
        pd_circle2 = (CircularProgressBar) view.findViewById(R.id.pd_circle_yellow);
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
        pd_circle1.setCircleWidth(30);
        pd_circle1.setPrimaryColor(Color.rgb(252, 128, 48));
        pd_circle2.setCircleWidth(30);
        pd_circle2.setPrimaryColor(Color.rgb(252, 248, 0));
        pd_circle3.setCircleWidth(30);
        rl_time.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rl_time && id != -1) {
            Intent intent = new Intent();
            intent.putExtra("id", id);
            intent.setClass(FragmentRun.this.getActivity(), MessageActivity.class);
            startActivity(intent);
        }

    }

    public void updateBleId(long id) {
        this.id = id;
    }

    public void updateSpeed(int speed) {
        tv_speed.setText("" + DateUitl.formatToM(speed / 100000)
                + "km/h");
    }

    public void updateView(int factors, int steprate, int stride, int step, long mins, float distance) {

        tv_factors.setText("" + factors);
        tv_steprate.setText(steprate + "/min");
        String stride_str = "Great";
        if (stride < 70)
            stride_str = "Great";
        else if (stride > 70 && stride < 90)
            stride_str = "Good";
        else if (stride >= 90 && stride < 110)
            stride_str = "Average";
        else if (stride >= 110 && stride < 130)
            stride_str = "Bad";
        else if (stride > 130)
            stride_str = "Risk";
        tv_stride.setText(stride_str);
        if (step != 0 && distance == 0) {
            distance = (int) (step * 74);// ����㷨��δʵ�� **

        }
        tv_step.setText("" + step);
        String ss = "m";
        String kk = "cal";
        if (distance > 100000) {
            distance = distance / 100000.0f;
            ss = "km";
            kk = "kcal";
        } else
            distance = distance / 100.0f;

        float calories = UtilConstants.Weight * distance * 1.306f;

        tv_distance.setText("" + DateUitl.formatToM(distance) + ss);
        tv_calories.setText("" + DateUitl.formatToM(calories / 1000.0f) + kk);

    }

    public void updateTime(long mins, int blue, int yellow, int red) {
        tv_sumtimes.setText(DateUitl.getDateFormat4(mins) + " mins");
        pd_circle3.setMax((int) mins);
        pd_circle3.setProgress(blue);

        pd_circle2.setMax((int) mins);
        pd_circle2.setProgress(yellow);

        pd_circle1.setMax((int) mins);
        pd_circle1.setProgress(red);
    }


}
