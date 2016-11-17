package com.keenbrace.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.keenbrace.R;
import com.keenbrace.base.BaseFragment;

import butterknife.Bind;

//改成搜索运动的一种分类方式

public class SearchIndexFragment extends BaseFragment{
    // TODO: Rename parameter arguments, choose names that match
    TextView txTriceps, txBiceps, txShoulder, txForearm, txChest, txCardio, txBack, txAbs, txGlutes,
    txUpperleg, txLowerleg, txWhole;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_own_plan_index, container, false);

        txTriceps = (TextView) view.findViewById(R.id.tx_triceps);
        txBiceps = (TextView) view.findViewById(R.id.tx_biceps);
        txShoulder = (TextView) view.findViewById(R.id.tx_shoulder);
        txForearm = (TextView) view.findViewById(R.id.tx_forearm);
        txChest = (TextView) view.findViewById(R.id.tx_chest);
        txCardio = (TextView) view.findViewById(R.id.tx_cardio);
        txBack = (TextView) view.findViewById(R.id.tx_back);
        txAbs = (TextView) view.findViewById(R.id.tx_abs);
        txGlutes = (TextView) view.findViewById(R.id.tx_glutes);
        txUpperleg = (TextView) view.findViewById(R.id.tx_upperleg);
        txLowerleg = (TextView) view.findViewById(R.id.tx_lowerleg);
        txWhole = (TextView) view.findViewById(R.id.tx_wholebody);

        return view;
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search_main;
    }

    @Override
    public void initView() {
    }

    public void updateView(int id, Boolean state)
    {
        switch (id)
        {
            case R.id.tx_triceps:
                if(state == true)   txTriceps.setTextColor(Color.YELLOW);
                else    txTriceps.setTextColor(Color.WHITE);
                break;

            case R.id.tx_biceps:
                if(state == true)   txBiceps.setTextColor(Color.YELLOW);
                else    txBiceps.setTextColor(Color.WHITE);
                break;

            case R.id.tx_shoulder:
                if(state == true)   txShoulder.setTextColor(Color.YELLOW);
                else    txShoulder.setTextColor(Color.WHITE);
                break;

            case R.id.tx_forearm:
                if(state == true)   txForearm.setTextColor(Color.YELLOW);
                else    txForearm.setTextColor(Color.WHITE);
                break;

            case R.id.tx_chest:
                if(state == true)   txChest.setTextColor(Color.YELLOW);
                else    txChest.setTextColor(Color.WHITE);
                break;

            case R.id.tx_cardio:
                if(state == true)   txCardio.setTextColor(Color.YELLOW);
                else    txCardio.setTextColor(Color.WHITE);
                break;

            case R.id.tx_back:
                if(state == true)   txBack.setTextColor(Color.YELLOW);
                else    txBack.setTextColor(Color.WHITE);
                break;

            case R.id.tx_abs:
                if(state == true)   txAbs.setTextColor(Color.YELLOW);
                else    txAbs.setTextColor(Color.WHITE);
                break;

            case R.id.tx_glutes:
                if(state == true)   txGlutes.setTextColor(Color.YELLOW);
                else    txGlutes.setTextColor(Color.WHITE);
                break;

            case R.id.tx_upperleg:
                if(state == true)   txUpperleg.setTextColor(Color.YELLOW);
                else    txUpperleg.setTextColor(Color.WHITE);
                break;

            case R.id.tx_lowerleg:
                if(state == true)   txLowerleg.setTextColor(Color.YELLOW);
                else    txLowerleg.setTextColor(Color.WHITE);
                break;

            case R.id.tx_wholebody:
                if(state == true)   txWhole.setTextColor(Color.YELLOW);
                else    txWhole.setTextColor(Color.WHITE);
                break;
        }
    }
}
