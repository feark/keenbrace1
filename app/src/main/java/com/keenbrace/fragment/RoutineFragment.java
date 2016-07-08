package com.keenbrace.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keenbrace.R;
import com.keenbrace.base.BaseFragment;

public class RoutineFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match


    public RoutineFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_routine, container, false);

        return view;
    }

    @Override
    public void initData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_plan_main;
    }

    @Override
    public void initView() {
    }

}
