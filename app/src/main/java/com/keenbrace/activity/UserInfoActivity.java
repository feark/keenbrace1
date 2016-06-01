package com.keenbrace.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.amazonaws.com.google.gson.Gson;
import com.keenbrace.AppConfig;
import com.keenbrace.AppContext;
import com.keenbrace.R;
import com.keenbrace.api.KeenbraceRetrofit;
import com.keenbrace.base.BaseActivity;
import com.keenbrace.bean.Constant;
import com.keenbrace.bean.KeenbraceDBHelper;
import com.keenbrace.bean.response.LoginResponse;
import com.keenbrace.core.datepicker.picker.DatePicker;
import com.keenbrace.core.datepicker.picker.NumberPicker;
import com.keenbrace.core.datepicker.picker.OptionPicker;
import com.keenbrace.core.datepicker.picker.SexPicker;
import com.keenbrace.core.datepicker.util.DateUtils;
import com.keenbrace.greendao.User;
import com.keenbrace.util.DateUitl;
import com.keenbrace.util.StringUtils;

import java.text.ParseException;
import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class UserInfoActivity extends BaseActivity {
    private int gender = 0;
    private int type;
    User users;
    int year,month,day;
    @Bind(R.id.tv_sex)
    TextView tvGender;
    @Bind(R.id.tv_birthday)
    TextView tvBirthday;
    @Bind(R.id.tv_name)
    TextView tvName;

    @Bind(R.id.tv_email)
    TextView tvEmail;
    @Bind(R.id.tv_mobile)
    TextView tvMobile;
    @Bind(R.id.tv_height)
    TextView tvHeight;
    @Bind(R.id.tv_weight)
    TextView tvWeight;


    @OnClick(R.id.rl_sex)
    void pickGender() {

        SexPicker picker = new SexPicker(this);
        picker.onlyMaleAndFemale();
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
                if (option.equalsIgnoreCase("男")) {
                    gender = 0;
                } else {
                    gender = 1;
                }
                tvGender.setText(option);
            }
        });
        picker.show();
    }

    @OnClick(R.id.rl_birthday)
    void pickBirthday() {
        DatePicker picker = new DatePicker(this);
        picker.setRange(1900, 2016);
        if(year!=0&&month!=0&&day!=0)
        picker.setSelectedItem(year, month, day);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                tvBirthday.setText(year + "-" + month + "-" + day);
            }
        });
        picker.show();
    }
    @OnClick(R.id.rl_height)
    void openHeight()
    {
        NumberPicker picker=new NumberPicker(this);
        picker.setRange(50, 250);
        picker.setSelectedItem("170");
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
                tvHeight.setText(option);
            }
        });
        picker.show();
    }
    @OnClick(R.id.rl_weight)
    void openWeight()
    {
        NumberPicker picker=new NumberPicker(this);
        picker.setRange(10, 250);
        picker.setSelectedItem("80");
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
                tvWeight.setText(option);
            }
        });
        picker.show();
    }



    @OnClick(R.id.rl_mobile)
    void openMobileInput()
    {
        openInput(2, tvMobile.getText().toString());
    }
    @OnClick(R.id.rl_name)
    void openNameInput()
    {
        openInput(0, tvName.getText().toString());
    }

    @OnClick(R.id.rl_mail)
    void openEmailInput()
    {
        openInput(1, tvEmail.getText().toString());
    }


    void saveUser()
    {
        showWaitDialog();
        users.setWeight(StringUtils.partToInt(tvWeight.getText().toString()));
        users.setHeight(StringUtils.partToInt(tvHeight.getText().toString()));
        users.setSex(gender);
        users.setBirthday(tvBirthday.getText().toString());
        users.setNickname(tvName.getText().toString());
        users.setEmail(tvEmail.getText().toString());
        users.setMobile(tvMobile.getText().toString());
        Gson gson= new Gson();
        Observable<LoginResponse> observable = new KeenbraceRetrofit()
                .createBaseApi()
                .login("3","",
                        "",
                        gson.toJson(users));
        _subscriptions.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideWaitDialog();
                        Toast.makeText(UserInfoActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                        KeenbraceDBHelper.getInstance(UserInfoActivity.this).upateUser(users);
                        finish();
                    }

                    @Override
                    public void onNext(LoginResponse loginResponse) {
                        if (loginResponse.getResultCode().equals("0")) {
                            Toast.makeText(UserInfoActivity.this, "更新用户信息成功", Toast.LENGTH_SHORT).show();
                            KeenbraceDBHelper.getInstance(UserInfoActivity.this).upateUser(users);
                            finish();

                        } else {
                            hideWaitDialog();
                            String msg = loginResponse.getMsg();
                            if (com.keenbrace.core.utils.StringUtils.isEmpty(msg)) {
                                msg = "更新用户信息失败";
                            }
                            Toast.makeText(UserInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                }));


    }


    public void openInput(int t,String value)
    {
        Intent intent = new Intent(this, InputActivity.class);

        intent.putExtra("type", t);
        intent.putExtra("value", value);

        startActivityForResult(intent, 0);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.aty_user_info;
    }

    @Override
    public void initView() {
       users=(User)this.getIntent().getSerializableExtra("users");
        if(users==null)
            users= Constant.user;

        type=this.getIntent().getIntExtra("type",1);


        toolbar.inflateMenu(R.menu.menu_save);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                saveUser();
                return true;
            }
        });


    }
    @Override
    protected boolean hasBackButton() {
        return true;
    }
    @Override
    protected boolean hasActionBar() {
        return true;
    }
    @Override
    public void initData() {
        if(users!=null)
        {
            tvHeight.setText(users.getHeight()+"");
            tvWeight.setText(users.getWeight()+"");
            tvName.setText(users.getNickname());
            if(Constant.user.getSex()==0)
            tvGender.setText("男");
            else
                tvGender.setText("女");
            if(users.getBirthday()!=null)
            {

                     Date d=new Date();
                try {
                    d.setTime(DateUitl.getDateTolong(users.getBirthday()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                year=d.getYear();
                    month=d.getMonth();
                    day=d.getDate();
                tvBirthday.setText(users.getBirthday());

            }

            tvEmail.setText(users.getEmail());
            tvMobile.setText(users.getMobile());

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode==0)
        { Bundle mBundle = data.getExtras();
            String value = mBundle.getString("value");
            tvName.setText(value);
        }else if(resultCode==1)
        {
            Bundle mBundle = data.getExtras();
            String value = mBundle.getString("value");
            tvEmail.setText(value);
        }else if(resultCode==2)
        {
            Bundle mBundle = data.getExtras();
            String value = mBundle.getString("value");
            tvMobile.setText(value);
        }
    }
}
