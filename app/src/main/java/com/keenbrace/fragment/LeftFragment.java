package com.keenbrace.fragment;


import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.keenbrace.R;
import com.keenbrace.activity.MainActivity;
import com.keenbrace.activity.SettingActivity;
import com.keenbrace.activity.UserInfoActivity;
import com.keenbrace.activity.ViewRecordActivity;
import com.keenbrace.adapter.BleDataListAdapter;
import com.keenbrace.bean.Constant;
import com.keenbrace.bean.KeenbraceDBHelper;
import com.keenbrace.core.utils.DateUtils;
import com.keenbrace.core.utils.StringUtils;
import com.keenbrace.core.utils.WLoger;
import com.keenbrace.AppConfig;
import com.keenbrace.AppContext;

import com.keenbrace.api.KeenbraceRetrofit;
import com.keenbrace.base.BaseFragment;
import com.keenbrace.greendao.KeenBrace;
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
    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.tv_account)
    TextView tvAccount;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_gender)
    TextView tvGender;
    @Bind(R.id.tv_birthday)
    TextView tvBirthday;
    @Bind(R.id.tv_email)
    TextView tvEmail;
    @Bind(R.id.tv_mobile)
    TextView tvMobile;
    @Bind(R.id.tv_height)
    TextView tvHeight;
    @Bind(R.id.tv_weight)
    TextView tvWeight;


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

        if(Constant.user!=null) {
            if (Constant.user.getPicturePath() != null && !"".equals(Constant.user.getPicturePath()))
                Glide.with(this).load(Constant.user.getPicturePath()).into(imageView);
            tvAccount.setText(Constant.user.getLoginName());
            tvName.setText(Constant.user.getNickname());
            tvBirthday.setText(Constant.user.getBirthday());
            tvEmail.setText(Constant.user.getEmail());
            tvGender.setText(Constant.user.getSex() == 1 ? "男" : "女");
            tvHeight.setText(Constant.user.getHeight() + "");
            tvWeight.setText(Constant.user.getWeight() + "");
            tvMobile.setText(Constant.user.getMobile());
        }



    }




}
