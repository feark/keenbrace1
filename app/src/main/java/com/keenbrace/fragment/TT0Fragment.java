package com.keenbrace.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.keenbrace.R;
import com.keenbrace.activity.MainActivity;
import com.keenbrace.base.BaseFragment;


public class TT0Fragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    RadioGroup rg_gender;
    RadioButton rb_female;
    RadioButton rb_male;

    RelativeLayout rl_age;
    RelativeLayout rl_weight;
    RelativeLayout rl_height;

    TextView tv_age;
    TextView tv_weight;
    TextView tv_height;

    public TT0Fragment() {
        // Required empty public constructor
    }

    public void initData() {
    }

    @Override
    public void initView() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tt0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tt0, container, false);

        rg_gender = (RadioGroup) view.findViewById(R.id.rg_gender);
        rb_female = (RadioButton) view.findViewById(R.id.rb_gender_female);
        rb_male = (RadioButton) view.findViewById(R.id.rb_gender_male);

        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == rb_female.getId())
                {

                }
                else if(checkedId == rb_male.getId())
                {

                }
            }
        });

        rl_age = (RelativeLayout) view.findViewById(R.id.rl_as_age);
        rl_age.setOnClickListener(this);
        rl_weight = (RelativeLayout) view.findViewById(R.id.rl_as_weight);
        rl_weight.setOnClickListener(this);
        rl_height = (RelativeLayout) view.findViewById(R.id.rl_as_height);
        rl_height.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.rl_as_age:

                break;

            case R.id.rl_as_weight:
                break;

            case R.id.rl_as_height:
                break;
        }
    }

}
