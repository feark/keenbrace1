package com.keenbrace.activity;

//这个位置把登录的名字与密码放在 RunResultDBHelper里管理是不对的 后面要改正 ken

import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.amazonaws.services.cognitosync.model.transform.LambdaThrottledExceptionUnmarshaller;
import com.keenbrace.AppConfig;
import com.keenbrace.AppContext;
import com.keenbrace.R;
import com.keenbrace.base.BaseActivity;
import com.keenbrace.bean.Constant;
import com.keenbrace.bean.UserDBHelper;
import com.keenbrace.constants.UtilConstants;
import com.keenbrace.core.utils.DateUtils;
import com.keenbrace.core.utils.PreferenceHelper;
import com.keenbrace.core.utils.StringUtils;
import com.keenbrace.greendao.HistoryRecord;
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
    ImageView btnLogin;

    //点击login 检查用户名与密码的输入
    @OnClick(R.id.btn_login) void login(){
        if(StringUtils.isEmpty(etAccount.getText())){
            Snackbar.make(btnLogin, "account name", Snackbar.LENGTH_SHORT).show();
            return;
        }
        if(StringUtils.isEmpty(etPwd.getText())){
            Snackbar.make(btnLogin, "password", Snackbar.LENGTH_SHORT).show();
            return;
        }
        loginLogic();
    };

    //注册
    @OnClick (R.id.btn_register) void register(){
        //readyGo(RegisterActivity.class);
        Toast.makeText(
                this,
                "Register is not opened yet, please login as visitor",
                Toast.LENGTH_SHORT).show();
    }

    //忘记密码
    @OnClick (R.id.btn_visitor) void forgetPwd(){
        //将登录状态和登录名写入配置文件 ken ++
        PreferenceHelper.write(AppContext.getInstance(),UtilConstants.SHARE_PREF, UtilConstants.KEY_HAS_LOGIN, true);
        PreferenceHelper.write(AppContext.getInstance(),UtilConstants.SHARE_PREF, UtilConstants.KEY_ACCOUNT,"test");



        User user=new User();
        user.setLoginName("test");
        user.setPassword("test");
        user.setWeight(75);
        user.setHeight(175);
        user.setGender(1);
        user.setBirthday(DateUtils.convertServerDate2("1987-01-01 00:00:00"));
        user.setNickname("test");
        //在这里创建一条user数据 ken
        user.setId(UserDBHelper.getInstance(this).insertUser(user));

    //把user赋给全局
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

        PreferenceHelper.write(AppContext.getInstance(),UtilConstants.SHARE_PREF, UtilConstants.KEY_HAS_LOGIN, true);
        PreferenceHelper.write(AppContext.getInstance(),UtilConstants.SHARE_PREF, UtilConstants.KEY_ACCOUNT, etAccount.getText().toString());

       User user= UserDBHelper.getInstance(this).queryUserByLoginName( etAccount.getText().toString());
        if(user==null)
        {
            user=new User();
            user.setLoginName(etAccount.getText().toString());
            user.setPassword(etPwd.getText().toString());
            user.setWeight(75);
            user.setHeight(175);
            user.setGender(1);
            user.setBirthday(DateUtils.convertServerDate2("1987-01-01 00:00:00"));
            user.setNickname("test");
           user.setId(UserDBHelper.getInstance(this).insertUser(user));

        }
        Constant.user=user;

        //在数据库中新建历史记录数据
        HistoryRecord historyRecord = UserDBHelper.getInstance(this).queryHistoryByLoginName(etAccount.getText().toString());
        if(historyRecord == null)
        {
            historyRecord = new HistoryRecord();
            historyRecord.setLoginName(etAccount.getText().toString());
            historyRecord.setId(UserDBHelper.getInstance(this).insertHistory(historyRecord));
        }
        Constant.historyRecord = historyRecord;

        readyGo(MainActivity.class);
        finish();
    }



}
