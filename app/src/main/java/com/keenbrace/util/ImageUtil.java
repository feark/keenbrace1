package com.keenbrace.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class ImageUtil {
    private static final String TAG = "ImageUtil";
    private final static String ALBUM_PATH = Environment.getExternalStorageDirectory() + "/download_test/";

    public static String saveBitmap(String savePath, String picName, Bitmap bm) {
        Log.e(TAG, "保存图片");
        /* String sdStatus = Environment.getExternalStorageState();
         if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { 
             Log.i("TestFile",  
                     "SD card is not avaiable/writeable right now.");  
             return;  
         }  */
        String photoUrl = "";
        if (null == savePath || null == picName || null == bm) {
            return photoUrl;
        }
        File file = new File(savePath);
        if (!file.exists()) {//如果目录不存在就创建目录
            file.mkdirs();
        }

        try {
            FileOutputStream out = new FileOutputStream(savePath + picName);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            photoUrl = savePath + picName;
            out.flush();
            out.close();
            Log.i(TAG, "已经保存");
        } catch (FileNotFoundException e) {
            Log.i(TAG, "保存失败:" + e.getMessage());
            photoUrl = "";
        } catch (IOException e) {
            Log.i(TAG, "保存失败:" + e.getMessage());
            photoUrl = "";
        }
        return photoUrl;
    }

    private static boolean checkFsWritable(String savePath) {
        // Create a temporary file to see whether a volume is really writeable.  
        // It's important not to put it in the root directory which may have a  
        // limit on the number of files.  
        String directoryName = Environment.getExternalStorageDirectory().toString() + savePath;
        File directory = new File(directoryName);
        if (directory.isDirectory() && !directory.exists()) {
            if (!directory.mkdirs()) {
                return false;
            }
        }
        return directory.canWrite();
    }

    /***
     * ��ݲ�ͬ��·������bitmap;
     *
     * @param path
     * @return
     */
    public Bitmap getBitmapTodifferencePath(String path, Context context) {


        if (path.length() < 7) {
            return null;
        }
        String str = path.substring(0, 7);

        if ("content".equals(str)) {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;

                BitmapFactory.decodeStream(context.getContentResolver()
                        .openInputStream(Uri.parse(path)), null, options);

                int height = options.outHeight;

                if (options.outWidth > 100) {
                    options.inSampleSize = options.outWidth / 100 + 1 + 1;
                    options.outWidth = 100;
                    height = options.outHeight / options.inSampleSize;
                    options.outHeight = height;
                }
                options.inJustDecodeBounds = false;
                options.inPurgeable = true;
                options.inInputShareable = true;


                return BitmapFactory.decodeStream(context.getContentResolver()
                        .openInputStream(Uri.parse(path)), null, options);

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            // �ⲿ��ַ

            BitmapFactory.Options options = new BitmapFactory.Options();
            // options.inSampleSize = 8;
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);

            int height = options.outHeight;

            if (options.outWidth > 100) {
                // ��ݿ��������ű���
                options.inSampleSize = options.outWidth / 100 + 1 + 1;
                options.outWidth = 100;

                // �������ź�ĸ߶�
                height = options.outHeight / options.inSampleSize;
                options.outHeight = height;
            }
            options.inJustDecodeBounds = false;
            options.inPurgeable = true;
            options.inInputShareable = true;
            return BitmapFactory.decodeFile(path, options);

            // public static Bitmap decodeFile(String filepath,final int
            // REQUIRED_SIZE){
            //

        }

        return null;
    }

    /**
     * 获取网络图片,如果图片存在于缓存中，就返回该图片，否则从网络中加载该图片并缓存起来
     *
     * @param path 图片路径
     * @return
     */
    public static Uri getImage(String path, File cacheDir, String name) throws Exception {// path -> MD5 ->32字符串.jpg
        File localFile = new File(cacheDir, name);
        if (localFile.exists()) {
            return Uri.fromFile(localFile);
        } else {
            HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                FileOutputStream outStream = new FileOutputStream(localFile);
                InputStream inputStream = conn.getInputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                inputStream.close();
                outStream.close();
                return Uri.fromFile(localFile);
            }
        }
        return null;
    }

}
