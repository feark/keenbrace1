package com.keenbrace.activity;

import com.keenbrace.R;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.keenbrace.base.BaseActivity;
import com.keenbrace.core.datepicker.picker.OptionPicker;
import com.keenbrace.core.datepicker.picker.TargetPicker;
import com.keenbrace.fragment.SearchIndexFragment;

//这个改成运动搜索

public class SearchSportsActivity extends BaseActivity implements View.OnClickListener {

    SearchIndexFragment searchIndexFragment;

    RelativeLayout rl_target;
    ImageView btn_addtarget;
    TextView tx_ownplan, tx_sysplan, tx_addtarget;

    private Fragment curFragment;

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected boolean hasActionBar() {
        return true;
    }


    @Override
    public void initView()
    {
        searchIndexFragment = new SearchIndexFragment();

        //打开activity就先切换到这个Fragment
        switchConent(searchIndexFragment);

        curFragment = searchIndexFragment;

        rl_target = (RelativeLayout) findViewById(R.id.rl_target);


        btn_addtarget = (ImageView) findViewById(R.id.btn_addtarget);
        btn_addtarget.setOnClickListener(this);

        tx_addtarget = (TextView) findViewById(R.id.tx_target);

        //rl_target.setVisibility(View.GONE);
    }

    public void switchConent(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.search_content_frame, fragment).commit();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_main;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            //添加运动部位
            case R.id.btn_addtarget:
                TargetPicker picker = new TargetPicker(this);

                picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(String option) {
                        if (option.equalsIgnoreCase("Triceps")) {
                            searchIndexFragment.updateView(R.id.tx_triceps, true);
                        }

                        if (option.equalsIgnoreCase("Biceps")) {
                            searchIndexFragment.updateView(R.id.tx_biceps, true);
                        }

                        if (option.equalsIgnoreCase("Shoulder")) {
                            searchIndexFragment.updateView(R.id.tx_shoulder, true);
                        }

                        if (option.equalsIgnoreCase("Forearm")) {
                            searchIndexFragment.updateView(R.id.tx_forearm, true);
                        }

                        if (option.equalsIgnoreCase("Chest")) {
                            searchIndexFragment.updateView(R.id.tx_chest, true);
                        }

                        if (option.equalsIgnoreCase("Back")) {
                            searchIndexFragment.updateView(R.id.tx_back, true);
                        }

                        if (option.equalsIgnoreCase("Abs/Core")) {
                            searchIndexFragment.updateView(R.id.tx_abs, true);
                        }

                        if (option.equalsIgnoreCase("Glutes")) {
                            searchIndexFragment.updateView(R.id.tx_glutes, true);
                        }

                        if (option.equalsIgnoreCase("Upper Leg")) {
                            searchIndexFragment.updateView(R.id.tx_upperleg, true);
                        }

                        if (option.equalsIgnoreCase("Lower Leg")) {
                            searchIndexFragment.updateView(R.id.tx_lowerleg, true);
                        }

                        if (option.equalsIgnoreCase("Cardio")) {
                            searchIndexFragment.updateView(R.id.tx_cardio, true);
                        }

                        if (option.equalsIgnoreCase("Whole")) {
                            searchIndexFragment.updateView(R.id.tx_wholebody, true);
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
