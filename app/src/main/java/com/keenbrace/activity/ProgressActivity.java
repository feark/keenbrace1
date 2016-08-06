package com.keenbrace.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.keenbrace.R;
import com.keenbrace.base.BaseActivity;

public class ProgressActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected boolean hasActionBar() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_progress;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

        this.setActionBarTitle("Workout Progress");
    }

    @Override
    public void onClick(View v) {

    }
}
