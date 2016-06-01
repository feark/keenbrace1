package com.keenbrace.activity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.amazonaws.com.google.gson.Gson;
import com.keenbrace.AppConfig;
import com.keenbrace.AppContext;
import com.keenbrace.R;
import com.keenbrace.api.KeenbraceRetrofit;
import com.keenbrace.base.BaseActivity;
import com.keenbrace.bean.Constant;
import com.keenbrace.bean.KeenbraceDBHelper;
import com.keenbrace.bean.response.LoginResponse;
import com.keenbrace.constants.UtilConstants;
import com.keenbrace.services.BluetoothConstant;
import com.keenbrace.greendao.User;
import com.keenbrace.util.StringUtil;
import com.keenbrace.util.StringUtils;
import com.keenbrace.widget.RoundImageView;


import android.net.Uri;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PersonInfoActivity extends BaseActivity implements OnClickListener {
    private File mPhotoFile;
    private String mPhotoPath;
    RoundImageView iv_head;
    View view, rl_save, rl_name, rl_sex, rl_weight, rl_height, rl_age;
    TextView et_age, et_sex, et_name, et_weight, et_height;

    Button btn_save, btn_cancle, btn_calibration;
    User user;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_personinfo;
    }
    @Override
    public void initView() {
        TextView tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_title.setText("Profile");
        rl_save = findViewById(R.id.rl_save);
        iv_head = (RoundImageView) findViewById(R.id.iv_head);
        et_age = (TextView) findViewById(R.id.et_age);
        et_sex = (TextView) findViewById(R.id.et_sex);
        et_name = (TextView) findViewById(R.id.et_name);
        et_weight = (TextView) findViewById(R.id.et_weight);
        et_height = (TextView) findViewById(R.id.et_height);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_cancle = (Button) findViewById(R.id.btn_cancel);
        btn_calibration = (Button) findViewById(R.id.btn_calibration);
        btn_calibration.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_cancle.setOnClickListener(this);
        rl_save.setOnClickListener(this);
        iv_head.setOnClickListener(open_camera);

        user = KeenbraceDBHelper.getInstance(this).queryUserByLoginName("test");
        if (user != null) {
            mPhotoPath = user.getPicturePath();
            setPsphoto();
            et_age.setText(user.getBirthday().toString());
            et_sex.setText(user.getSex());
            et_name.setText(user.getNickname());
            et_height.setText(user.getHeight());
            et_weight.setText(user.getWeight());

        }
        rl_name = this.findViewById(R.id.rl_name);
        rl_age = this.findViewById(R.id.rl_age);
        rl_sex = this.findViewById(R.id.rl_sex);
        rl_weight = this.findViewById(R.id.rl_weight);
        rl_height = this.findViewById(R.id.rl_height);
        rl_name.setOnClickListener(this);
        rl_age.setOnClickListener(this);
        rl_sex.setOnClickListener(this);
        rl_weight.setOnClickListener(this);
        rl_height.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    OnClickListener open_camera = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.iv_head) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        PersonInfoActivity.this);

                final AlertDialog dialog = builder.create();
                dialog.show();
                Window window = dialog.getWindow();
                window.setContentView(R.layout.pesional_select);

                RelativeLayout rl_local = (RelativeLayout) window
                        .findViewById(R.id.rl_local);
                RelativeLayout rl_camera = (RelativeLayout) window
                        .findViewById(R.id.rl_camera);

                rl_camera.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(
                                "android.media.action.IMAGE_CAPTURE");
                        mPhotoPath = "mnt/sdcard/DCIM/Camera/"
                                + getPhotoFileName();
                        mPhotoFile = new File(mPhotoPath);
                        if (!mPhotoFile.exists()) {
                            try {
                                mPhotoFile.createNewFile();
                            } catch (IOException e) {
                            }
                        }
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(mPhotoFile));
                        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                        PersonInfoActivity.this.startActivityForResult(intent,
                                1);
                        dialog.dismiss();

                    }
                });
                rl_local.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent picture = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        PersonInfoActivity.this.startActivityForResult(picture,
                                2);
                        dialog.dismiss();

                    }
                });
                dialog.show();

            }

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (2 == requestCode) {
            if (data.getData() != null) {
                Uri selectedImage = data.getData();
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = PersonInfoActivity.this
                        .getContentResolver()
                        .query(selectedImage, filePathColumns, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                mPhotoPath = c.getString(columnIndex);
            }

        }
        setPsphoto();

    }

    public void setPsphoto2() {
        if (mPhotoPath != null && !"".equals(mPhotoPath)) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap photo = BitmapFactory.decodeFile(mPhotoPath, options);
            iv_head.setImageBitmap(photo);

        }
    }

    public void setPsphoto() {
        if (mPhotoPath != null && !"".equals(mPhotoPath)) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 20;
            Bitmap photo = BitmapFactory.decodeFile(mPhotoPath, options);
            iv_head.setImageBitmap(photo);

        }
    }

    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";

    }

    public boolean check() {
        String userName = et_name.getText().toString();
        String weight = et_weight.getText().toString();
        String height = et_height.getText().toString();
        String age = et_age.getText().toString();
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

    public void openInputDialog(String title, final TextView tv) {
        final EditText inputServer = new EditText(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(
                this);
        builder.setTitle(title)
                .setView(inputServer)
                .setNegativeButton(getResources().getString(R.string.cancle),
                        null);
        builder.setPositiveButton(getResources().getString(R.string.sure),
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        String name = inputServer.getText().toString();
                        tv.setText(name);
                    }
                });
        builder.show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_save:
                if (!check())
                    return;
                if (user == null) {
                    user = new User();
                }

                user.setBirthday("1987-01-01");
                user.setPicturePath(mPhotoPath);
                user.setWeight(StringUtils.partToInt(et_weight.getText().toString()));
                user.setHeight(StringUtils.partToInt(et_height.getText().toString()));
                user.setNickname(et_name.getText().toString());
                user.setSex(et_sex.getText().toString().equals("男") ? 1 : 0);
                Gson gson=new Gson();
                Observable<LoginResponse> observable = new KeenbraceRetrofit()
                        .createBaseApi()
                        .login("3","",
                                "",
                                gson.toJson(user));

                _subscriptions.add(observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<LoginResponse>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                hideWaitDialog();
                                Toast.makeText(PersonInfoActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(LoginResponse loginResponse) {
                                if (loginResponse.getResultCode().equals("0")) {


                                    User user = loginResponse.getUser();
                                    KeenbraceDBHelper.getInstance(PersonInfoActivity.this).upateUser(user);
                                    Constant.user = user;

                                    Toast.makeText(PersonInfoActivity.this, "save success!",
                                            Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    hideWaitDialog();
                                    String msg = loginResponse.getMsg();
                                    if (com.keenbrace.core.utils.StringUtils.isEmpty(msg)) {
                                        msg = "更新失败";
                                    }
                                    Toast.makeText(PersonInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }));



                break;
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_calibration:
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
                    w = Integer.parseInt(et_weight.getText().toString());
                    h = Integer.parseInt(et_height.getText().toString());
                } catch (Exception e) {
                }
                data[2] = (byte) w;
                data[3] = (byte) h;

                if (BluetoothConstant.mConnected && BluetoothConstant.mwriteCharacteristic != null) {

                    BluetoothConstant.mwriteCharacteristic.setValue(data);
                    BluetoothConstant.mBluetoothLeService
                            .writeCharacteristic(BluetoothConstant.mwriteCharacteristic);
                    Toast.makeText(PersonInfoActivity.this, "Calibration sucess!", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(PersonInfoActivity.this, "Calibration Faild!", Toast.LENGTH_SHORT)
                            .show();
                }

                break;
            case R.id.rl_name:
                openInputDialog("Name", et_name);
                break;
            case R.id.rl_sex:
                openSex();
                break;
            case R.id.rl_age:
                openInputDialog("Age", et_age);
                break;
            case R.id.rl_weight:
                openInputDialog("Weigth", et_weight);
                break;
            case R.id.rl_height:
                openInputDialog("Heigth", et_height);
                break;

        }

    }

    int sex;
    int sexwich;

    public void openSex() {
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
                        et_sex.setText(sexs[sexwich]);
                    }
                });
        builder.show();

    }
}
