package com.keenbrace.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.keenbrace.R;
import com.keenbrace.base.BaseActivity;



public class AddCustomActivity extends BaseActivity implements View.OnClickListener {


    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        this.setActionBarTitle("Custom Workout");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_custom;
    }

    @Override
    protected boolean hasActionBar() {
        return true;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
    }


}
