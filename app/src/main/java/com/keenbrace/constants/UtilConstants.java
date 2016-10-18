package com.keenbrace.constants;

import android.bluetooth.BluetoothDevice;

import java.util.HashMap;
import java.util.Map;

import com.keenbrace.R;
import com.keenbrace.greendao.ShortPlan;
import com.keenbrace.greendao.SportsStructure;
import com.keenbrace.greendao.User;

public class UtilConstants {

    public static User user;

    //这个是构造类似结构体数组的方法 ken
    //第一个值是索引 第二个是结构体的值
    public static HashMap<String, SportsStructure> SportsContent = new HashMap<String, SportsStructure>();

    //不同的单次计划
    public static HashMap<String, ShortPlan> ShortPlanContent = new HashMap<String, ShortPlan>();

    //public static HashMap<String, BluetoothDevice> DeviceList = new HashMap<String, BluetoothDevice>();
    //public static int DeviceNumber = 0;

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
    public static int MapType=1;        //这种可以直接设置值
    public static final String SHARE_PREF = "keenbrace_share.pref";
    public static final String KEY_HAS_LOGIN = "has_login";
    public static final String KEY_ACCOUNT  ="key_account";
    public static final String BLE_NAME = "ble_name";   //上一次连接的设备名称
    public static final String HAS_CHALLENGE_ADD = "has_challenge";

    public static final String KEY_HAS_SHORTPLAN = "has_shortplan"; //是否有单项计划
    public static final String KEY_TRAIN_TODAY  = "train_today"; //今天的单项计划是否已经完成

    public static final String KEY_HAS_LONGPLAN = "has_longplan"; //是否有长期计划
    public static final String KEY_LONGPLAN_FINISH  = "long_plan_finish"; //长期计划是否已经完成

    //历史记录的数据查询名称同user一样用登录名

    //不同的运动种类
    public static final int sport_running = 0;
    public static final int sport_squat = 3;
    public static final int sport_pushup = 4;
    public static final int sport_pullup = 5;
    public static final int sport_dumbbell = 6;
    public static final int sport_plank = 9;
    public static final int sport_bicyclesitup = 10;
    public static final int sport_closestancesquat = 11;

<<<<<<< HEAD
    //从哪里开始
    public static final int fromChallenge = 1;
    public static final int fromPlan = 2;
    public static final int fromMainPage = 3;
    public static final int fromCustom = 4;

    //每个事件给一个固定ID
    public static final int eventGeneral = 0;
    public static final int eventLean = 1;      // tgt iangle steprate iangle由stride衍生
    public static final int eventCadence = 2;   // speed steprate
    public static final int eventStride = 3;    // stride steprate
    public static final int eventLand = 4;      // pressTimes
    public static final int eventDrive = 5;     // 多次Bounce发生
    public static final int eventBounce = 6;    // speed osc
    public static final int eventStability = 7; // 在动 pressTimes stability
    public static final int eventFoot = 8;      // tgt steprate
    public static final int eventReach = 9;     // steprate stride
    public static final int eventCalf = 10;     // 多次reach发生
    public static final int eventCog = 11;      // 在动 pressTimes stride很大
    public static final int eventGravity = 12;  // 在动 pressTimes stride中等偏大
    public static final int eventEnergy = 13;   // speed osc中等偏大
    public static final int eventRest = 14;     // 2公里 stability power
    public static final int eventToe = 15;      // 多次eventFoot
    public static final int eventSprint = 16;   //
    public static final int eventSwingArm = 17;
    public static final int eventStill = 18;    // 站着没动
    public static final int eventNormalRun = 19;// 一般跑动
    public static final int eventWalk = 20;

    public static final int[] event2str = {
            R.string.tx_relax_smooth,
            R.string.tx_forward_lean,
            R.string.tx_increase_cadence,
            R.string.tx_decrease_stride,
            R.string.tx_land_softer,
            R.string.tx_hip_energy,
            R.string.tx_avoid_bouncing,
            R.string.tx_enhance_stability,
            R.string.tx_lift_faster,
            R.string.tx_reach_toomuch,
            R.string.tx_unfold_leg,
            R.string.tx_strike_closer,
            R.string.tx_land_underneath,
            R.string.tx_energy_forward,
            R.string.tx_take_rest,
            R.string.tx_spring_toe,
            R.string.tx_dont_sprint,
            R.string.tx_arm_swing,
            R.string.tx_stand_still,
            R.string.tx_goodrunform,
            R.string.tx_walking,
    };
=======


>>>>>>> KeenBrace_Android/master
}
