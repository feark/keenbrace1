package com.keenbrace.activity;


import android.widget.EditText;
import android.widget.Toast;

import com.keenbrace.core.utils.StringUtils;
import com.keenbrace.core.utils.WLoger;
import com.keenbrace.AppConfig;
import com.keenbrace.AppContext;
import com.keenbrace.R;
import com.keenbrace.api.KeenbraceRetrofit;
import com.keenbrace.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ModifyPwdActivity extends BaseActivity {

    @Bind(R.id.et_pwd)
    EditText etPwd;
    @Bind(R.id.et_pwd_again)
    EditText etPwdAgain;

    private String mobile;
    private String newPwd;



    @OnClick(R.id.btn_apply) void modifyPwd(){
        if (StringUtils.isEmpty(etPwd.getText().toString())
                ||StringUtils.isEmpty(etPwdAgain.getText().toString())) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!etPwd.getText().toString().equals(etPwdAgain.getText().toString())) {
            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        firstLogin();
    }

//    http://jspatch.qq.com/offline/check?qver=6.2.1.430&hver=0&biz=2164&pf=2&uin=412469748&osrelease=9.1&mask=0

    @Override
    protected int getLayoutId() {
        return R.layout.aty_modify_pwd;
    }

    @Override
    public void initView() {
        mobile = getIntent().getExtras().getString("mobile");
        newPwd = getIntent().getExtras().getString("newPwd");
    }

    @Override
    public void initData() {
    }

    public void firstLogin(){}


    private void modify(){}



}
