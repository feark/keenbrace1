package com.keenbrace.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public class Image {

    public static final int ASPECT_FREE = 1;
    public static final int ASPECT_SQUARE = 2;
    private static int width = 0;
    private static int height = 0;
    private static int SELECT_PICTURE = 0;
    private static int SELECT_CAMERE = 1;
    private static int RESULT_PICTURE = 2;
    static File tempImage = null;


    public static void getImage(Activity activity, int requestCode) {
        Intent toPhoto = new Intent(Intent.ACTION_GET_CONTENT);
        toPhoto.setType("image/*");
        activity.startActivityForResult(Intent.createChooser(toPhoto, "选锟斤拷图片"),
                requestCode);
    }

    public static void getImageFromCamere(Activity activity, String imagePath,
                                          int requestCode) {
        tempImage = new File(imagePath);//
        Intent toCamere = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        toCamere.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempImage));
        activity.startActivityForResult(toCamere, requestCode);
    }

    public static void getImageFromCamere(Context context) {
        Intent toCamere = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        context.startActivity(toCamere);
    }

    /*
     * 锟斤拷指锟斤拷图片锟斤拷锟叫硷拷锟叫达拷锟�?锟窖硷拷锟叫猴拷锟酵计拷锟斤拷馗锟斤拷锟斤拷锟斤拷 锟斤拷锟斤拷锟斤拷�?�同锟斤�?
     */
    public static void doCropImage(Activity activity, String imagePath,
                                   int imagex, int imagey, int aspectMode) {
        if (imagex != 0 && imagey != 0) {
            width = imagex;
            height = imagey;
        }
        tempImage = new File(imagePath);
        Intent intent = getCropImageIntent(aspectMode);
        activity.startActivityForResult(Intent.createChooser(intent, "锟斤拷锟斤拷"),
                RESULT_PICTURE);
    }

    public static boolean file2file(String source, String target) {


        FileInputStream fis = null;
        try {
            fis = new FileInputStream(source);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(target);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (fis != null && fos != null) {
            byte[] buff = new byte[100 * 1024];
            int readed = -1;
            try {
                while ((readed = fis.read(buff)) > 0)
                    fos.write(buff, 0, readed);
                fis.close();
                fos.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (fis == null) {
            Log.i("test", "源锟侥硷拷未锟揭碉拷锟斤�?");
        } else if (fos == null) {
            File file = new File(target);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                    //!!
                    file2file(source, target);
                } catch (IOException e) {
                }
            }
        }
        return false;
    }

    public static Intent getCropImageIntent(int aspectMode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        try {
            intent.setDataAndType(Uri.fromFile(tempImage), "image/*");// 锟斤拷锟斤拷要锟矫硷拷锟斤拷图片
            intent.putExtra("crop", "true");// crop=true 锟斤拷锟斤拷锟斤拷锟杰筹拷锟斤拷锟斤拷锟侥裁硷拷页锟斤拷.
            if (aspectMode == ASPECT_SQUARE) {
                intent.putExtra("aspectX", 1);// 锟斤拷锟斤拷锟斤拷为锟矫硷拷锟斤拷谋锟斤拷锟�?.
                intent.putExtra("aspectY", 1);// x:y=1:1
            }
            if (width != 0 && height != 0) {
                intent.putExtra("outputX", width);//
                intent.putExtra("outputY", height);
            }
            intent.putExtra("output", Uri.fromFile(tempImage));// 锟斤拷锟芥到原锟侥硷�?
            intent.putExtra("outputFormat", "JPEG");// 锟斤拷锟截革拷式
            intent.putExtra("return-data", true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return intent;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) // drawable 转锟斤拷锟斤�?
    // bitmap
    {
        int width = drawable.getIntrinsicWidth(); // �? drawable 锟侥筹拷锟斤�?
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565; // �? drawable 锟斤拷锟斤拷色锟斤拷�?
        Bitmap bitmap = Bitmap.createBitmap(width, height, config); // 锟斤拷锟斤拷锟斤拷应
        // bitmap
        Canvas canvas = new Canvas(bitmap); // 锟斤拷锟斤拷锟斤拷应 bitmap 锟侥伙拷锟斤�?
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas); // 锟斤�? drawable 锟斤拷锟捷伙拷锟斤拷锟斤拷锟斤拷锟斤�?
        return bitmap;
    }

    public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable); // drawable 转锟斤拷锟斤�? bitmap
        Matrix matrix = new Matrix(); // 锟斤拷锟斤拷锟斤拷锟斤拷图片锟矫碉拷 Matrix 锟斤拷锟斤拷
        float scaleWidth = ((float) w / width); // 锟斤拷锟斤拷锟斤拷锟脚憋拷锟斤拷
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight); // 锟斤拷锟斤拷锟斤拷锟脚憋拷锟斤拷
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                matrix, true); // 锟斤拷锟斤拷锟铰碉拷 bitmap 锟斤拷锟斤拷锟斤拷锟斤拷锟角讹拷�? bitmap 锟斤拷锟斤拷锟脚猴拷锟酵�?
        return new BitmapDrawable(newbmp); // 锟斤�? bitmap 转锟斤拷锟斤�? drawable 锟斤拷锟斤拷锟斤�?
    }

    public static Bitmap file2Bitmap(String f, int scale) {
        Bitmap bitmap = null;
        try {
            /*
			// Decode image size
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			FileInputStream fis = new FileInputStream(f);
			BitmapFactory.decodeStream(fis, null, options);
			fis.close();
			*/
            // Decode with inSampleSize
            BitmapFactory.Options options = new BitmapFactory.Options();
            //options.inJustDecodeBounds = true;
            options.inSampleSize = scale;
            FileInputStream fis = new FileInputStream(f);
            bitmap = BitmapFactory.decodeStream(fis, null, options);
            //bitmap = BitmapFactory.decodeFile(f, options);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static boolean bitmap2File(Bitmap bmp, String path) {
        CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bmp.compress(format, quality, stream);
    }

    /**
     * 鑾峰彇鍦嗚浣嶅浘鐨勬柟娉�
     *
     * @param bitmap 闇�瑕佽浆鍖栨垚鍦嗚鐨勪綅鍥�
     * @param pixels 鍦嗚鐨勫害鏁帮紝鏁板�艰秺澶э紝鍦嗚瓒婂ぇ
     * @return 澶勭悊鍚庣殑鍦嗚浣嶅浘
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

}
