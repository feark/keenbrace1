package com.keenbrace.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.keenbrace.R;
import com.keenbrace.base.BaseActivity;
import com.keenbrace.bean.Constant;
import com.keenbrace.bean.UserDBHelper;
import com.keenbrace.core.datepicker.picker.DatePicker;
import com.keenbrace.core.datepicker.picker.NumberPicker;
import com.keenbrace.core.datepicker.picker.OptionPicker;
import com.keenbrace.core.datepicker.picker.SexPicker;
import com.keenbrace.core.datepicker.util.DateUtils;
import com.keenbrace.greendao.User;
import com.keenbrace.services.BluetoothConstant;
import com.keenbrace.util.DateUitl;
import com.keenbrace.util.StringUtils;

import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;


public class UserInfoActivity extends BaseActivity {
    private int gender = 0;
    private int type;
    User users;
    int year,month,day;
    @Bind(R.id.tv_sex)
    TextView tvGender;
    @Bind(R.id.tv_age)
    TextView tvAge;
    @Bind(R.id.tv_name)
    TextView tvName;

    @Bind(R.id.btn_calibrate)
    ImageView btn_calibrate;

    @Bind(R.id.tv_email)
    TextView tvEmail;
    @Bind(R.id.tv_mobile)
    TextView tvMobile;
    @Bind(R.id.tv_height)
    TextView tvHeight;
    @Bind(R.id.tv_weight)
    TextView tvWeight;


    int sex;
    int sexwich;

    @OnClick(R.id.rl_sex)
    void pickGender() {
        SexPicker picker = new SexPicker(this);
        picker.onlyMaleAndFemale();
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
                if (option.equalsIgnoreCase("male")) {
                    gender = 0;
                } else {
                    gender = 1;
                }
                tvGender.setText(option);
            }
        });
        picker.show();

        /*
        AlertDialog.Builder builder = new AlertDialog.Builder(
                this);
        builder.setTitle("Gender");
        final String[] sexs = new String[]{
                "male",
                "female"};
        builder.setSingleChoiceItems(sexs, 0,
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        sexwich = which;
                        sex = which;
                    }
                });

        builder.setNegativeButton(getResources().getString(R.string.cancle),
                null);
        builder.setPositiveButton(getResources().getString(R.string.sure),
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        //et_sex.setText(sexs[sexwich]);
                    }
                });
        builder.show();
        */
    }

    public boolean check() {
        String userName = tvName.getText().toString();
        String weight = tvWeight.getText().toString();
        String height = tvHeight.getText().toString();
        String age = tvAge.getText().toString();
        if ("".equals(userName) || "--".equals(userName)) {
            Toast.makeText(this, "name is null!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if ("".equals(age) || "--".equals(age)) {
            Toast.makeText(this, "age is null!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if ("".equals(weight) || "--".equals(weight)) {
            Toast.makeText(this, "weight is null!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if ("".equals(height) || "--".equals(height)) {
            Toast.makeText(this, "height is null!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @OnClick(R.id.btn_calibrate)
    void calibration(){
        //发送校准信息
        if (!check())
            return;
        byte[] data = new byte[4];
        data[0] = 0x51;

        if (sex == 1)
            data[1] = 0x01;
        else
            data[1] = 0x00;

        int w = 75;
        int h = 175;
        try {
            w = Integer.parseInt(tvWeight.getText().toString());
            h = Integer.parseInt(tvHeight.getText().toString());
        } catch (Exception e) {
        }
        data[2] = (byte) w;
        data[3] = (byte) h;

        if (BluetoothConstant.mConnected && BluetoothConstant.mwriteCharacteristic != null) {

            BluetoothConstant.mwriteCharacteristic.setValue(data);
            BluetoothConstant.mBluetoothLeService
                    .writeCharacteristic(BluetoothConstant.mwriteCharacteristic);
            Toast.makeText(UserInfoActivity.this, "Calibration sucess!", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(UserInfoActivity.this, "Calibration Faild!", Toast.LENGTH_SHORT)
                    .show();
        }

    }

    @OnClick(R.id.rl_birthday)
    void pickBirthday() {
        NumberPicker picker = new NumberPicker(this);
        picker.setRange(8, 100);
        if(year!=0&&month!=0&&day!=0)
            picker.setSelectedItem("25");
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
                tvAge.setText(option);
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
        users.setWeight(StringUtils.partToInt(tvWeight.getText().toString()));
        users.setHeight(StringUtils.partToInt(tvHeight.getText().toString()));
        users.setGender(gender);
        //要换成年龄 leave
        //users.setBirthday(com.keenbrace.core.utils.DateUtils.convertServerDate2(tvBirthday.getText() + " 00:00:00"));
        users.setNickname(tvName.getText().toString());
        users.setEmail(tvEmail.getText().toString());
        users.setMobile(tvMobile.getText().toString());
        UserDBHelper.getInstance(this).upateUser(users);
        finish();
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
            if(Constant.user.getGender()==0)
            tvGender.setText("male");
            else
                tvGender.setText("female");
            if(users.getBirthday()!=null)
            {


                    year=users.getBirthday().getYear();
                    month=users.getBirthday().getMonth();
                    day=users.getBirthday().getDate();

            }
            //换成年龄
            //tvBirthday.setText(DateUitl.getDatetoString(Constant.user.getBirthday().getTime()));
            tvEmail.setText(Constant.user.getEmail());
            tvMobile.setText(Constant.user.getMobile());

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
