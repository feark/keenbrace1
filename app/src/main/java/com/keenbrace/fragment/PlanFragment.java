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
import com.keenbrace.constants.UtilConstants;
import com.keenbrace.activity.MainActivity;
import com.keenbrace.base.BaseFragment;
import com.keenbrace.greendao.CommonResult;
import com.keenbrace.widget.MyValueFormatter;

import java.util.HashMap;
import java.util.List;


import butterknife.Bind;
import butterknife.OnClick;



public class PlanFragment extends BaseFragment {

    ImageView iv_drawer;

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

        return view;
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
