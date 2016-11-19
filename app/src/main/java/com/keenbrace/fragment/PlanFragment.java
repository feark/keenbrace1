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


import com.keenbrace.R;
import com.keenbrace.activity.MainMenuActivity;
import com.keenbrace.bean.CommonResultDBHelper;
import com.keenbrace.calendar.MonthDateView;
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



public class PlanFragment extends BaseFragment {

    ImageView iv_drawer;

    private ImageView iv_left;
    private ImageView iv_right;
    private TextView tv_date;
    private TextView tv_week;
    private TextView tv_today;
    private MonthDateView monthDateView;

    @OnClick (R.id.iv_drawer_plan)
    void onDrawerPlan(){
        ((MainActivity)getActivity()).showMenu();
    }

    @Override
    public int getLayoutId() {
        return R.layout.frame_plan;
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
        View view = inflater.inflate(R.layout.frame_plan, null);

        iv_drawer = (ImageView) view.findViewById(R.id.iv_drawer_plan);
        iv_drawer.setOnClickListener(this);

        List<Integer> list = new ArrayList<Integer>();
        //有事件的加上右上角小红点，在这里加  但有一个问题 要自己区分是哪个月 不然每个月同样几天都会加上红点
        list.add(10);
        list.add(12);
        list.add(15);
        list.add(16);

        iv_left = (ImageView) view.findViewById(R.id.iv_left);
        iv_right = (ImageView) view.findViewById(R.id.iv_right);
        monthDateView = (MonthDateView) view.findViewById(R.id.monthDateView);
        tv_date = (TextView) view.findViewById(R.id.date_text);
        tv_week  =(TextView) view.findViewById(R.id.week_text);
        tv_today = (TextView) view.findViewById(R.id.tv_today);
        monthDateView.setTextView(tv_date,tv_week);
        monthDateView.setDaysHasThingList(list);
        monthDateView.setDateClick(new MonthDateView.DateClick() {
            @Override
            public void onClickOnDate() {
                DatePick(monthDateView.getmSelDay());
            }
        });
        setOnlistener();

        return view;
    }

    public void DatePick(int selDay)
    {
        Toast.makeText(super.getActivity(), "select " + selDay,
                Toast.LENGTH_SHORT).show();
    }

    private void setOnlistener(){
        iv_left.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                monthDateView.onLeftClick();
            }
        });

        iv_right.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                monthDateView.onRightClick();
            }
        });

        tv_today.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                monthDateView.setTodayToView();
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.iv_drawer_plan:
                ((MainActivity)getActivity()).showMenu();
                break;
        }
    }


}
