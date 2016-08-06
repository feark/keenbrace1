package com.keenbrace.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.keenbrace.R;
import com.keenbrace.base.BaseActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DiaryActivity extends BaseActivity implements View.OnClickListener {

    ImageView iv_writenote;
    ImageView iv_camera;
    ImageView iv_ruler;
    EditText editcomment;

    TextView tv_post;

    private File mPhotoFile;
    private String mPhotoPath;

    private int PICTURE_FROM_CAMERA = 1;
    private int PICTURE_FROM_ALBUM = 2;


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
        return R.layout.activity_diary;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

        this.setActionBarTitle("Training Diary");

        iv_writenote = (ImageView) findViewById(R.id.iv_writenote);
        iv_writenote.setOnClickListener(this);
        iv_camera = (ImageView) findViewById(R.id.iv_camera);
        iv_camera.setOnClickListener(this);
        iv_ruler = (ImageView) findViewById(R.id.iv_ruler);
        iv_ruler.setOnClickListener(this);

        tv_post = (TextView) findViewById(R.id.tv_post);
        tv_post.setOnClickListener(this);

        editcomment = (EditText) findViewById(R.id.editComment);
        getWindow().setSoftInputMode(   WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        editcomment.clearFocus();
    }

    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager) editcomment.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        switch (v.getId())
        {
            case R.id.iv_camera:
                //隐藏键盘
                imm.hideSoftInputFromWindow(v.getWindowToken(),0);

                AlertDialog.Builder builder = new AlertDialog.Builder(
                        DiaryActivity.this);

                //怎样设置自定义风格的弹出对话框 ken
                final AlertDialog dialog = builder.create();
                dialog.show();
                Window window = dialog.getWindow();
                window.setContentView(R.layout.headiconset);

                RelativeLayout rl_local = (RelativeLayout) window
                        .findViewById(R.id.rl_local);
                RelativeLayout rl_camera = (RelativeLayout) window
                        .findViewById(R.id.rl_camera);

                rl_camera.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(
                                "android.media.action.IMAGE_CAPTURE");

                        mPhotoPath = "mnt/sdcard/DCIM/"
                                + getPhotoFileName();
                        //mPhotoPath = Environment.getExternalStorageDirectory()
                          //      .getAbsolutePath() + getPhotoFileName();

                        mPhotoFile = new File(mPhotoPath);
                        if (!mPhotoFile.exists()) {
                            try {
                                mPhotoFile.createNewFile();
                            } catch (IOException e) {
                            }
                        }

                        //Uri photoFileUri = Uri.fromFile(mPhotoFile);
                        //intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoFileUri);

                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(mPhotoFile));

                        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);

                        DiaryActivity.this.startActivityForResult(intent,
                                PICTURE_FROM_CAMERA);

                        dialog.dismiss();

                    }
                });

                rl_local.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent picture = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        DiaryActivity.this.startActivityForResult(picture,
                                PICTURE_FROM_ALBUM);
                        dialog.dismiss();

                    }
                });
                dialog.show();
                break;

            case R.id.iv_writenote:
                //弹出软键盘
                editcomment.requestFocus();
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                break;

            case R.id.iv_ruler:
                imm.hideSoftInputFromWindow(v.getWindowToken(),0);

                break;

            case R.id.tv_post:
                imm.hideSoftInputFromWindow(v.getWindowToken(),0);

                //没有文字也没有图片时
                if(editcomment.getText().length() == 0){
                    if(mPhotoPath == null || "".equals(mPhotoPath))
                    {
                        Toast.makeText(DiaryActivity.this, "Nothing to post", Toast.LENGTH_SHORT)
                             .show();
                    }
                }
                break;
        }

    }

    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //从相机拍的
        if(PICTURE_FROM_CAMERA == requestCode){
            if(RESULT_OK == resultCode){
                //成功从摄像头得到照片
                //Toast.makeText(DiaryActivity.this, "Capture sucess!", Toast.LENGTH_SHORT)
                  //      .show();


            }
        }

        //从相册选的
        if (PICTURE_FROM_ALBUM == requestCode) {
            if (data.getData() != null) {
                Uri selectedImage = data.getData();
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = DiaryActivity.this
                        .getContentResolver()
                        .query(selectedImage, filePathColumns, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                mPhotoPath = c.getString(columnIndex);
            }

        }

        setPsphoto();
    }

    //更新缩略图
    public void setPsphoto() {
        if (mPhotoPath != null && !"".equals(mPhotoPath)) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 20;
            Bitmap photo = BitmapFactory.decodeFile(mPhotoPath, options);
            //iv_head.setImageBitmap(photo);
        }
    }


}
