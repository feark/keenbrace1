package com.keenbrace.activity;


import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.amazonaws.services.cognitosync.model.transform.LambdaThrottledExceptionUnmarshaller;
import com.keenbrace.AppConfig;
import com.keenbrace.AppContext;
import com.keenbrace.R;
import com.keenbrace.api.KeenbraceRetrofit;
import com.keenbrace.base.BaseActivity;
import com.keenbrace.bean.Constant;
import com.keenbrace.bean.KeenbraceDBHelper;
import com.keenbrace.bean.response.LoginResponse;
import com.keenbrace.constants.UtilConstants;
import com.keenbrace.core.utils.DateUtils;
import com.keenbrace.core.utils.PreferenceHelper;
import com.keenbrace.core.utils.StringUtils;
import com.keenbrace.greendao.User;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class LoginActivity extends BaseActivity {

    @Bind(R.id.et_account)
    EditText etAccount;
    @Bind(R.id.et_pwd)
    EditText etPwd;
    @Bind(R.id.btn_login)
    Button btnLogin;

    @OnClick(R.id.btn_login) void login(){
        if(StringUtils.isEmpty(etAccount.getText())){
            Snackbar.make(btnLogin, "请输入账号", Snackbar.LENGTH_SHORT).show();
            return;
        }
        if(StringUtils.isEmpty(etPwd.getText())){
            Snackbar.make(btnLogin, "请输入密码", Snackbar.LENGTH_SHORT).show();
            return;
        }
        loginLogic();
    };

    @OnClick (R.id.btn_register) void register(){
        readyGo(RegisterActivity.class);
    }

    @OnClick (R.id.btn_test) void forgetPwd(){
        PreferenceHelper.write(AppContext.getInstance(),UtilConstants.SHARE_PREF, UtilConstants.KEY_HAS_LOGIN, true);
        PreferenceHelper.write(AppContext.getInstance(), UtilConstants.SHARE_PREF, UtilConstants.KEY_ACCOUNT, "test");
        User user=new User();
        user.setLoginName("test");
        user.setPassword("test");
        user.setWeight(75);
        user.setHeight(175);
        user.setSex(1);
        user.setBirthday("1987-01-01");
        user.setNickname("test");
        user.setId(KeenbraceDBHelper.getInstance(this).insertUser(user));


    Constant.user=user;
    readyGo(MainActivity.class);
    finish();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        if(toolbar!=null)
        {
            toolbar.setTitle("登录");

        }
        etAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                etPwd.setText("");
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        if (!StringUtils.isEmpty(AppContext.get(AppConfig.KEY_ACCOUNT, ""))) {
            etAccount.setText(AppContext.get(AppConfig.KEY_ACCOUNT,""));
        }
    }

    @Override
    public void initData() {
    }


    private void loginLogic(){
         showWaitDialog();

        Observable<LoginResponse> observable = new KeenbraceRetrofit()
                .createBaseApi()
                .login("2",etAccount.getText().toString(),
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
                        Toast.makeText(LoginActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onNext(LoginResponse loginResponse) {
                        hideWaitDialog();
                        if (loginResponse.getResultCode().equals("0")) {
                            AppContext.set(AppConfig.KEY_HAS_LOGIN, true);
                            AppContext.set(AppConfig.PATIENT_ID, loginResponse.getUser().getId()+"");
                            AppContext.set(AppConfig.KEY_ACCOUNT, etAccount.getText().toString());
                            PreferenceHelper.write(AppContext.getInstance(), UtilConstants.SHARE_PREF, UtilConstants.KEY_HAS_LOGIN, true);
                            PreferenceHelper.write(AppContext.getInstance(), UtilConstants.SHARE_PREF, UtilConstants.KEY_ACCOUNT, etAccount.getText().toString());
                            User user=loginResponse.getUser();
                            KeenbraceDBHelper.getInstance(LoginActivity.this).insertUser(user);
                            Constant.user=user;

                            readyGo(MainActivity.class);
                            finish();
                        }else {
                            String msg = loginResponse.getMsg();
                            if (StringUtils.isEmpty(msg)) {
                                msg = "登录失败";
                            }
                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                }));


    }



}
