package com.keenbrace.fragment;


import android.widget.ImageView;
import android.widget.LinearLayout;

import com.keenbrace.R;
import com.keenbrace.activity.HistoryActivity;
import com.keenbrace.activity.MainActivity;
import com.keenbrace.activity.PlanMainActivity;
import com.keenbrace.activity.ProgressActivity;
import com.keenbrace.activity.SettingActivity;
import com.keenbrace.activity.StoreActivity;

import com.keenbrace.api.KeenbraceRetrofit;
import com.keenbrace.base.BaseFragment;

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

    @Bind(R.id.device_list)
    ImageView device_list;

    @Bind(R.id.system_setting)
    ImageView system_setting;

    @Bind(R.id.ll_store)
    LinearLayout ll_store;

    @Bind(R.id.ll_history)
    LinearLayout ll_history;

    @Bind(R.id.ll_progress)
    LinearLayout ll_progress;

    @OnClick(R.id.ll_plan)
    void openPlanSettings() { readyGo(PlanMainActivity.class); }

    @OnClick(R.id.ll_progress)
    void openProgress(){ readyGo(ProgressActivity.class); }

    @OnClick(R.id.ll_store)
    void openStoreWebSite() { readyGo(StoreActivity.class); }

    //打开蓝牙设备列表
    @OnClick(R.id.device_list)
    void openKeenBrace()
    {
        MainActivity parentActivity = (MainActivity) getActivity();

        parentActivity.openDeives();

    }

    @OnClick(R.id.system_setting)
    void OpenEdit()
    {
        readyGo(SettingActivity.class);
    }

    //运动历史记录列表
    @OnClick(R.id.ll_history)
    void OpenHistory() { readyGo(HistoryActivity.class); }

    @Override
    public int getLayoutId() {
        return R.layout.nav_header_main;
    }

    @Override
    public void initView() {

    }


    @Override
    public void initData() {

    }


}
