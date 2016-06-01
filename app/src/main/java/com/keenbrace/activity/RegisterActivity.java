package com.keenbrace.activity;


import android.widget.EditText;
import android.widget.Toast;


import com.keenbrace.AppConfig;
import com.keenbrace.AppContext;
import com.keenbrace.R;
import com.keenbrace.api.KeenbraceRetrofit;
import com.keenbrace.base.BaseActivity;
import com.keenbrace.bean.Constant;
import com.keenbrace.bean.KeenbraceDBHelper;
import com.keenbrace.bean.response.LoginResponse;
import com.keenbrace.core.utils.StringUtils;
import com.keenbrace.greendao.User;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegisterActivity extends BaseActivity {


    @Bind(R.id.et_account)
    EditText etAccount;
    @Bind(R.id.et_pwd)
    EditText etPwd;
    @Bind(R.id.et_confirm_pwd)
    EditText etPwdAgain;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initView() {
        if(toolbar!=null)
        {
            toolbar.setTitle("注册");

        }
    }

    @OnClick (R.id.btn_goto)
    void nextPage(){
        register();
    }


    @Override
    public void initData() {
    }

    private void register(){
        if (StringUtils.isEmpty(etAccount.getText())) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isEmpty(etPwd.getText())) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isEmpty(etPwdAgain.getText())) {
            Toast.makeText(this, "请确认密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!etPwd.getText().toString().equals(etPwdAgain.getText().toString())){
            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        showWaitDialog();
        Observable<LoginResponse> observable = new KeenbraceRetrofit()
                .createBaseApi()
                .login("1",etAccount.getText().toString(),
                        etPwd.getText().toString(),
                        "");

        _subscriptions.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideWaitDialog();
                        Toast.makeText(RegisterActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(LoginResponse loginResponse) {
                        if (loginResponse.getResultCode().equals("0")) {
                            AppContext.set(AppConfig.KEY_HAS_LOGIN, true);
                            AppContext.set(AppConfig.PATIENT_ID, loginResponse.getUser().getId() + "");
                            AppContext.set(AppConfig.KEY_ACCOUNT, etAccount.getText().toString());

                            User user = loginResponse.getUser();
                            KeenbraceDBHelper.getInstance(RegisterActivity.this).insertUser(user);
                            Constant.user = user;

                            readyGo(UserInfoActivity.class);
                            finish();

                        } else {
                            hideWaitDialog();
                            String msg = loginResponse.getMsg();
                            if (StringUtils.isEmpty(msg)) {
                                msg = "注册失败";
                            }
                            Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                }));

    }

}
