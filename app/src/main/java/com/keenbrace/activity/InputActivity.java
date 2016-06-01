package com.keenbrace.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.EditText;


import com.keenbrace.R;
import com.keenbrace.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

public class InputActivity extends BaseActivity {
    int type=1;
    public static int INPUT_TEXT=0;

    public static int INPUT_EMAIL=1;
    public static int INPUT_MOBIEL=2;
    public static int INPUT_MORE=3;
    String value;
    @Bind(R.id.et_input)
    EditText etInput;



    @OnClick(R.id.iv_clean)
    void clean()
    {
        etInput.setText("");
    }
    void updateTextType() {
        etInput.setText(value);
    switch (type)
    {
        case 0:
            break;
        case 1:
            etInput.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
            etInput.setHint(R.string.hit_email);
            break;
        case 2:

            etInput.setInputType(InputType.TYPE_CLASS_PHONE);
            etInput.setHint(R.string.hit_mobile);
            break;
        case 3:
            etInput.setLines(5);
            etInput.setHint("请输入备注");


    }
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_input;
    }
    @Override
    public void initView() {
        type = getIntent().getIntExtra("type", 0);
        value= getIntent().getStringExtra("value");
        updateTextType();

        toolbar.inflateMenu(R.menu.menu_save);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                save();
                return true;
            }
        });
    }

    @Override
    public void initData() {

    }
    @Override
    protected boolean hasBackButton() {
        return true;
    }
    @Override
    protected boolean hasActionBar() {
        return true;
    }

    void save()
    {
        Intent intent3 = new Intent();
        Bundle mBundle = new Bundle();
        mBundle.putString("value",etInput.getText().toString());
        intent3.putExtras(mBundle);
        setResult(type, intent3);
        finish();
    }
}
