package com.keenbrace.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.keenbrace.R;
import com.keenbrace.base.BaseActivity;
import com.keenbrace.bean.Constant;
import com.keenbrace.constants.UtilConstants;
import com.keenbrace.services.BluetoothConstant;
import com.keenbrace.util.SharePreferUtil;
import com.keenbrace.widget.RoundImageView;

import java.util.List;

public class SettingActivity extends BaseActivity implements OnClickListener {
    View rl_profile, rl_language, rl_fiexd, rl_kv, rl_version,rl_map;
    TextView tv_userName, tv_language, tv_kv, tv_version,tv_map;

    RoundImageView ps_photo;

    public void init() {


        if (Constant.user != null) {
            if (Constant.user.getPicturePath() != null && !"".equals(Constant.user.getPicturePath())) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 20;
                Bitmap photo = BitmapFactory.decodeFile(Constant.user.getPicturePath(),
                        options);
                ps_photo.setImageBitmap(photo);

            }
            tv_userName.setText(Constant.user.getNickname());
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.rl_profile:
                intent.setClass(this, UserInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_language:
                break;
            case R.id.rl_fiexd:
                //发送校准信息 ken

                break;
            case R.id.tv_version:
                break;
            case R.id.rl_map:
                openMap();
                break;
        }

    }
    int mwhich;
    public void openMap() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                this);
        builder.setTitle("Gender");
        final String[] ts = new String[]{
                "Google Map",
                "Gaode Map"};
        builder.setSingleChoiceItems(ts, 0,
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        UtilConstants.MapType=which;
                        mwhich=which;

                    }
                });

        builder.setNegativeButton(getResources().getString(R.string.cancle),
                null);
        builder.setPositiveButton(getResources().getString(R.string.sure),
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        tv_map.setText(ts[mwhich]);
                        SharePreferUtil.setParamValue(SettingActivity.this,"MapType",UtilConstants.MapType);
                    }
                });
        builder.show();

    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView() {
        this.setActionBarTitle("Setting");
        rl_profile = findViewById(R.id.rl_profile);
        rl_language = findViewById(R.id.rl_language);
        rl_fiexd = findViewById(R.id.rl_fiexd);
        rl_version = findViewById(R.id.rl_version);
        rl_map = findViewById(R.id.rl_map);
        ps_photo = (RoundImageView)findViewById(R.id.ps_photo);
        rl_profile.setOnClickListener(this);
        rl_language.setOnClickListener(this);
        rl_fiexd.setOnClickListener(this);
        rl_version.setOnClickListener(this);
        rl_map.setOnClickListener(this);
        tv_userName = (TextView)findViewById(R.id.tv_userName);
        tv_language = (TextView) findViewById(R.id.tv_language);
        tv_kv = (TextView)findViewById(R.id.tv_kv);
        tv_version = (TextView)findViewById(R.id.tv_version);
        tv_map= (TextView) findViewById(R.id.tv_map);

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
    public void initData() {

    }
}
