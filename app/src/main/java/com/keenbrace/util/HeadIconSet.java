package com.keenbrace.util;

import java.io.File;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.keenbrace.R;


public class HeadIconSet extends Activity {

    private static final int PHOTO_REQUEST_CAMERA = 1;//
    private static final int PHOTO_REQUEST_GALLERY = 2;//
    private static final int PHOTO_REQUEST_CUT = 3;//
    private RelativeLayout rl_local, rl_camera;
    private Bitmap bitmap;


    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private File tempFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
        setContentView(R.layout.headiconset);

        rl_camera = (RelativeLayout) findViewById(R.id.rl_camera);
        rl_local = (RelativeLayout) findViewById(R.id.rl_local);
        rl_camera.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                camera();
            }
        });
        rl_local.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                gallery();
            }
        });
    }

    /*
     * ������ȡ
     */
    public void gallery() {
        // ����ϵͳͼ�⣬ѡ��һ��ͼƬ
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    /*
     *
     */
    public void camera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        //
        if (hasSdcard()) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME)));
        }
        startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                //
                Uri uri = data.getData();
                //data.getDataString();
                crop(uri);
            }
        } else if (requestCode == PHOTO_REQUEST_CAMERA) {
            if (hasSdcard()) {
                tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME);
                crop(Uri.fromFile(tempFile));
            } else {
                Toast.makeText(HeadIconSet.this, R.string.cannotfindsdcard, 0).show();
            }
        } else if (requestCode == PHOTO_REQUEST_CUT) {
            try {
                Toast.makeText(HeadIconSet.this, R.string.localtrim, 0).show();
                bitmap = data.getParcelableExtra("data");
                bitmap = toRoundBitmap(bitmap);

                Intent intent3 = new Intent();
                intent3.putExtra("headicon", bitmap);
                setResult(8, intent3);
                finish();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * ����ͼƬ
     */
    private void crop(Uri uri) {
        // �ü�ͼƬ��ͼ
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // �ü���ı���1��1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // �ü������ͼƬ�ĳߴ��С
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);
        // ͼƬ��ʽ
        intent.putExtra("outputFormat", "png");
        intent.putExtra("noFaceDetection", true);// ȡ������ʶ��
        intent.putExtra("return-data", true);// true:������uri��false������uri
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * ת��ͼƬ��Բ��
     *
     * @param bitmap ����Bitmap����
     * @return
     */
    public Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;

            left = 0;
            top = 0;
            right = width;
            bottom = width;
            height = width;

            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;

            float clip = (width - height) / 2;

            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;

            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);// ���û����޾��
        canvas.drawARGB(0, 0, 0, 0); // ������Canvas

        // ���������ַ�����Բ,drawRounRect��drawCircle
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);// ��Բ�Ǿ��Σ���һ������Ϊͼ����ʾ���򣬵ڶ�������͵��������ֱ���ˮƽԲ�ǰ뾶�ʹ�ֱԲ�ǰ뾶��
        // canvas.drawCircle(roundPx, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint); // ��Mode.SRC_INģʽ�ϲ�bitmap���Ѿ�draw�˵�Circle

        return output;
    }

    /*
     * �ϴ�ͼƬ
     */
    public void upload() {
    }

}