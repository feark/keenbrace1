package com.keenbrace.fragment;


import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.keenbrace.R;
import com.keenbrace.activity.MainActivity;
import com.keenbrace.activity.PlanMainActivity;
import com.keenbrace.activity.SettingActivity;
import com.keenbrace.activity.UserInfoActivity;
import com.keenbrace.activity.ViewRecordActivity;
import com.keenbrace.adapter.BleDataListAdapter;
import com.keenbrace.bean.Constant;
import com.keenbrace.core.utils.DateUtils;
import com.keenbrace.core.utils.StringUtils;
import com.keenbrace.core.utils.WLoger;
import com.keenbrace.AppConfig;
import com.keenbrace.AppContext;

import com.keenbrace.api.KeenbraceRetrofit;
import com.keenbrace.base.BaseFragment;
import com.keenbrace.util.Image;
import com.keenbrace.util.StringUtil;


import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zrq on 16/1/28.
 */
public class LeftFragment extends BaseFragment {
    @Bind(R.id.ll_plan)
    LinearLayout ll_plan;

    @Bind(R.id.profile_setting)
    ImageView profile_setting;

    @Bind(R.id.system_setting)
    ImageView system_setting;

    /*
    @OnClick(R.id.tv_settings)
    void openSettiings()
    {
        readyGo(SettingActivity.class);
    }

    @OnClick(R.id.tv_edit)
    void openEdit()
    {

        readyGo(UserInfoActivity.class);
    }
    */

    @OnClick(R.id.ll_plan)
    void openPlanSettings() { readyGo(PlanMainActivity.class); }

    //系统设置 和 用户信息设置
    @OnClick(R.id.profile_setting)
    void openSettiings()
    {
        readyGo(UserInfoActivity.class);
    }

    @OnClick(R.id.system_setting)
    void OpenEdit()
    {
        readyGo(SettingActivity.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.nav_header_main;
    }

    @Override
    public void initView() {
        initData();
    }


    @Override
    public void initData() {


    }




}
