package com.keenbrace.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.keenbrace.AppContext;
import com.keenbrace.R;
import com.keenbrace.activity.MainMenuActivity;
import com.keenbrace.adapter.ChallengeItemAdapter;
import com.keenbrace.adapter.TrainOfDateItemAdapter;
import com.keenbrace.bean.ChallengeDBHelper;
import com.keenbrace.bean.CommonResultDBHelper;
import com.keenbrace.bean.ShortPlanDBHelper;
import com.keenbrace.calendar.MonthDateView;
import com.keenbrace.constants.UtilConstants;
import com.keenbrace.activity.MainActivity;
import com.keenbrace.base.BaseFragment;
import com.keenbrace.core.utils.PreferenceHelper;
import com.keenbrace.greendao.Challenge;
import com.keenbrace.greendao.CommonResult;
import com.keenbrace.greendao.ShortPlan;
import com.keenbrace.widget.MyValueFormatter;
import com.keenbrace.widget.SwipeListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import butterknife.Bind;
import butterknife.OnClick;



public class PlanFragment extends BaseFragment {

    ImageView iv_drawer;

    //日历视图
    private ImageView iv_left;
    private ImageView iv_right;
    private TextView tv_date;
    private TextView tv_week;
    private TextView tv_today;
    private MonthDateView monthDateView;

    //计划视图
    SwipeListView trainOfDateList;
    List<ShortPlan> shortPlanItems;
    TrainOfDateItemAdapter adapter;

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

    public void initData() {
        //临时生成项目添加-------
        if(!PreferenceHelper.readBoolean(AppContext.getInstance(),
                UtilConstants.SHARE_PREF, UtilConstants.KEY_HAS_SHORTPLAN, false))
        {
            ShortPlan trainOfDate = new ShortPlan();

            trainOfDate.setId(ShortPlanDBHelper.getInstance((MainActivity) getActivity()).insertShortPlan(trainOfDate));

            //将KeenBrace Challenge的内容在这里加入  计划名是唯一的 不能重复
            trainOfDate.setShortPlanName("KeenBrace Cross Fit");
            trainOfDate.setLogo(R.mipmap.crossfit);
            trainOfDate.setStatus(0);
            trainOfDate.setPos("Full Body");
            trainOfDate.setTotalTime(40);
            trainOfDate.setIntense(3);

            //将本次的运动结果更新数据库 ken
            ShortPlanDBHelper.getInstance(getActivity()).updateShortPlan(trainOfDate);
        }

        PreferenceHelper.write(AppContext.getInstance(), UtilConstants.SHARE_PREF, UtilConstants.KEY_HAS_SHORTPLAN, true);

        shortPlanItems = ShortPlanDBHelper.getInstance(getActivity()).queryShortPlan();

        if(shortPlanItems == null){

            return;
        }

        adapter = new TrainOfDateItemAdapter(getActivity(), R.layout.item_trainofdate, shortPlanItems);
        //adapter.setOnChallengeItemListener(this);
        trainOfDateList.setAdapter(adapter);

        trainOfDateList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                //应跳转到短计划详情
                //Intent intent = new Intent();
                //intent.putExtra("Challenge", challengeItems.get(arg2)); //点击那一项的数据项内容会传到下一个activity
                //intent.putExtra("StartFrom", UtilConstants.fromChallenge);
                //intent.setClass((MainActivity) getActivity(), MainActivity.class);
                //startActivity(intent);
                //finish();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frame_plan, container, false);

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

        trainOfDateList = (SwipeListView) view.findViewById(R.id.trainofthedate);

        trainOfDateList.setPressed(false);

        initData();

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

    //===========================================================计划相关


}
