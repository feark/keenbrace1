package com.keenbrace.constants;

import java.util.HashMap;
import java.util.Map;

import com.keenbrace.greendao.User;

public class UtilConstants {

    public static User user;

    //这个是构造类似结构体数组的方法 ken
    //public static HashMap<String, WaringModel> WaringMap = new HashMap<String, WaringModel>();

    /*
     * You should replace these values with your own. See the README for details
     * on what to fill in.
     * ap-northeast-1:713c5b04-46e5-4241-8d5b-2ed673d9aae7
     */
    public static final String COGNITO_POOL_ID = "ap-northeast-1:a3c2b68e-6dd4-4414-b146-d0410dca4201";
    public static final int MAP_GOOGLE=0;
    public static final int MAP_GAODE=1;

    /*
     * Note, you must first create a bucket using the S3 console before running
     * the sample (https://console.aws.amazon.com/s3/). After creating a bucket,
     * put it's name in the field below.
     */
    public static final String BUCKET_NAME = "keenbrace";
    public static int Weight = 65;
    public static int Height = 175;
    public static int MapType=1;
    public static final String SHARE_PREF = "keenbrace_share.pref";
    public static final String KEY_HAS_LOGIN = "has_login";
    public static final String KEY_ACCOUNT  ="key_account";

    //不同的运动种类
    public static final int sport_running = 0;
    public static final int sport_squat = 1;
    public static final int sport_dumbbell = 2;
    public static final int sport_plank = 3;
    public static final int sport_pullup = 4;

}
