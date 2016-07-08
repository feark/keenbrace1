package com.keenbrace.activity;

import android.os.Bundle;
import com.keenbrace.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.keenbrace.base.BaseActivity;
import com.keenbrace.core.datepicker.picker.OptionPicker;
import com.keenbrace.core.datepicker.picker.SexPicker;
import com.keenbrace.core.datepicker.picker.TargetPicker;
import com.keenbrace.fragment.CustomizePlanFragment;
import com.keenbrace.fragment.OwnPlanIndexFragment;
import com.keenbrace.fragment.RoutineFragment;
import com.keenbrace.util.Image;

import org.w3c.dom.Text;

public class PlanMainActivity extends BaseActivity implements View.OnClickListener {

    OwnPlanIndexFragment ownPlanIndexFragment;
    CustomizePlanFragment customizePlanFragment;
    RoutineFragment routineFragment;

    ImageView btn_own_plan;
    ImageView btn_sys_plan;

    RelativeLayout rl_target;
    ImageView btn_addtarget;
    TextView tx_ownplan, tx_sysplan, tx_addtarget;

    ImageView btn_prev, btn_next;

    private Fragment curFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_main);

        ownPlanIndexFragment = new OwnPlanIndexFragment();
        customizePlanFragment = new CustomizePlanFragment();
        routineFragment = new RoutineFragment();

        //打开activity就先切换到这个Fragment
        switchConent(customizePlanFragment);

        curFragment = customizePlanFragment;

        rl_target = (RelativeLayout) findViewById(R.id.rl_target);

        btn_own_plan = (ImageView) findViewById(R.id.own_plan);
        btn_own_plan.setOnClickListener(this);

        btn_sys_plan = (ImageView) findViewById(R.id.sys_plan);
        btn_sys_plan.setOnClickListener(this);

        btn_addtarget = (ImageView) findViewById(R.id.btn_addtarget);
        btn_addtarget.setOnClickListener(this);

        tx_addtarget = (TextView) findViewById(R.id.tx_target);
        tx_ownplan = (TextView) findViewById(R.id.tx_ownplan);
        tx_sysplan = (TextView) findViewById(R.id.tx_sysplan);

        btn_prev = (ImageView) findViewById(R.id.btn_prev);
        btn_prev.setOnClickListener(this);
        btn_next = (ImageView) findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);

        rl_target.setVisibility(View.GONE);
    }

    @Override
    public void initView()
    {
    }

    public void switchConent(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.plan_content_frame, fragment).commit();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_plan_main;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.own_plan:
                switchConent(ownPlanIndexFragment);

                curFragment = ownPlanIndexFragment;

                btn_own_plan.setVisibility(View.GONE);
                btn_sys_plan.setVisibility(View.GONE);
                tx_ownplan.setVisibility(View.GONE);
                tx_sysplan.setVisibility(View.GONE);

                rl_target.setVisibility(View.VISIBLE);
                break;

            case R.id.sys_plan:

                break;

            case R.id.btn_prev:
                if(curFragment == ownPlanIndexFragment)
                {
                    switchConent(customizePlanFragment);
                    rl_target.setVisibility(View.GONE);

                    btn_own_plan.setVisibility(View.VISIBLE);
                    btn_sys_plan.setVisibility(View.VISIBLE);
                    tx_ownplan.setVisibility(View.VISIBLE);
                    tx_sysplan.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.btn_next:
                break;

            //添加运动部位
            case R.id.btn_addtarget:
                TargetPicker picker = new TargetPicker(this);

                picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(String option) {
                        if (option.equalsIgnoreCase("Triceps")) {
                            ownPlanIndexFragment.updateView(R.id.tx_triceps, true);
                        }

                        if (option.equalsIgnoreCase("Biceps")) {
                            ownPlanIndexFragment.updateView(R.id.tx_biceps, true);
                        }

                        if (option.equalsIgnoreCase("Shoulder")) {
                            ownPlanIndexFragment.updateView(R.id.tx_shoulder, true);
                        }

                        if (option.equalsIgnoreCase("Forearm")) {
                            ownPlanIndexFragment.updateView(R.id.tx_forearm, true);
                        }

                        if (option.equalsIgnoreCase("Chest")) {
                            ownPlanIndexFragment.updateView(R.id.tx_chest, true);
                        }

                        if (option.equalsIgnoreCase("Back")) {
                            ownPlanIndexFragment.updateView(R.id.tx_back, true);
                        }

                        if (option.equalsIgnoreCase("Abs/Core")) {
                            ownPlanIndexFragment.updateView(R.id.tx_abs, true);
                        }

                        if (option.equalsIgnoreCase("Glutes")) {
                            ownPlanIndexFragment.updateView(R.id.tx_glutes, true);
                        }

                        if (option.equalsIgnoreCase("Upper Leg")) {
                            ownPlanIndexFragment.updateView(R.id.tx_upperleg, true);
                        }

                        if (option.equalsIgnoreCase("Lower Leg")) {
                            ownPlanIndexFragment.updateView(R.id.tx_lowerleg, true);
                        }

                        if (option.equalsIgnoreCase("Cardio")) {
                            ownPlanIndexFragment.updateView(R.id.tx_cardio, true);
                        }

                        if (option.equalsIgnoreCase("Whole")) {
                            ownPlanIndexFragment.updateView(R.id.tx_wholebody, true);
                        }

                    }
                });
                picker.show();
                break;
        }
    }

    @Override
    public void initData()
    {

    }

}
