package com.keenbrace.activity;

import android.os.Bundle;
import android.widget.TextView;


import com.keenbrace.AppConfig;
import com.keenbrace.AppContext;
import com.keenbrace.R;
import com.keenbrace.base.BaseActivity;
import com.keenbrace.bean.Constant;
import com.keenbrace.bean.UserDBHelper;
import com.keenbrace.constants.UtilConstants;
import com.keenbrace.core.utils.PreferenceHelper;
import com.keenbrace.greendao.HistoryRecord;
import com.keenbrace.greendao.User;

import butterknife.Bind;


public class SplashActivity extends BaseActivity {





    @Override
    protected int getLayoutId() {
        return R.layout.activity_logo;
    }





    public void waitIn(){
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {

                }
                if(PreferenceHelper.readBoolean(AppContext.getInstance(),
                        UtilConstants.SHARE_PREF, UtilConstants.KEY_HAS_LOGIN, false)){ //已经登录过
                    //在这里查询是否已经登录过 并按登录名 查找出数据库中的user数据 ken
                    String account=  PreferenceHelper.readString(AppContext.getInstance(),UtilConstants.SHARE_PREF, UtilConstants.KEY_ACCOUNT, "");

                    User user= UserDBHelper.getInstance(SplashActivity.this).queryUserByLoginName(account);
                    //在这个位置把数据库新建的user赋给全局变量
                    Constant.user=user;

                    //已经登录账号的历史记录
                    HistoryRecord historyRecord = UserDBHelper.getInstance(SplashActivity.this).queryHistoryByLoginName(account);
                    Constant.historyRecord = historyRecord;

                    readyGoThenKill(MainActivity.class);
                }else{
                    //未登录过 去登录
                    readyGoThenKill(LoginActivity.class);
                }
            }
        }).start();
    }

    @Override
    public void initView() {

        waitIn();
    }

    @Override
    public void initData() {

        readXml();
    }
    public void readXml() {
        /*
        WaringModel wm = new WaringModel();
        wm.setCondition("膝盖压力过大 ");
        wm.setGrade(2);
        wm.setIndex("1");
        wm.setTitle("Too Much Force On Knee");
        wm.setInfo("膝盖压力过大 ");
        wm.setResult("Your knee is suffering force over the allowable amout, which will cause danger to your health.");
        wm.setFunction("1.Lower your vertical oscillation\n2.Keep your cadence above 160/min\n3.Land softly underneath a bent knee");
        int[] ids = new int[]{R.raw.male_big_boy, R.raw.male_small_boy};
        wm.setBids(ids);
        int[] gids = new int[]{R.raw.male_big_girl, R.raw.male_small_girl};
        wm.setGids(gids);
        UtilConstants.WaringMap.put("1", wm);


        WaringModel wm2 = new WaringModel();
        wm2.setCondition("");
        wm2.setGrade(3);
        wm2.setTitle("Foot Strike");
        wm2.setIndex("2");
        wm2.setInfo("脚落地点（1--脚尖，2--脚跟）");
        wm2.setResult("Should not change one's foot strike habit suddenly, otherwise there is a big chance to have Achilles tendon injuries. ");
        wm2.setFunction("Should not change one's foot strike habit suddenly, otherwise there is a big chance to have Achilles tendon injuries. ");
        int[] ids2 = new int[]{R.raw.jiaogen, R.raw.jiaozhang, R.raw.jiaozhi};
        wm2.setBids(ids2);
        wm2.setGids(ids2);
        UtilConstants.WaringMap.put("2", wm2);

        WaringModel wm3 = new WaringModel();
        wm3.setCondition("");
        wm3.setGrade(3);
        wm3.setIndex("3");
        wm3.setTitle("Over Stride");
        wm3.setInfo("步幅过大—脚尖着地 ");
        wm3.setResult("Overstriding has been shown to increase stress on the body.Further, overstriding leads to a straighter knee and a more aggressive heel strike which significantly reduces the knee muscles’ ability to absorb shock. The shock is then transferred to the knee menisci, knee joint and on to the hip and back joints.");
        wm3.setFunction(".Increase your cadence");
        int[] bids3 = new int[]{R.raw.big_stride_boy, R.raw.smaill_stride_boy};
        int[] gids3 = new int[]{R.raw.big_stride_girl, R.raw.smaill_stride_girl};
        wm3.setBids(bids3);
        wm3.setGids(gids3);
        UtilConstants.WaringMap.put("3", wm3);

        WaringModel wm32 = new WaringModel();
        wm32.setCondition("");
        wm32.setGrade(3);
        wm32.setIndex("32");
        wm32.setTitle("Over Stride");
        wm32.setInfo("步幅过大—脚尖着地 ");
        wm32.setResult("Overstriding has been shown to increase stress on the body.Further, overstriding leads to a straighter knee and a more aggressive heel strike which significantly reduces the knee muscles’ ability to absorb shock. The shock is then transferred to the knee menisci, knee joint and on to the hip and back joints.");
        wm32.setFunction(".Increase your cadence. do not land with your heel");
        int[] bids32 = new int[]{R.raw.big_stride_boy, R.raw.smaill_stride_boy};
        int[] gids32 = new int[]{R.raw.big_stride_girl, R.raw.smaill_stride_girl};
        wm32.setBids(bids32);
        wm32.setGids(gids32);
        UtilConstants.WaringMap.put("32", wm32);

        WaringModel wm4 = new WaringModel();
        wm4.setCondition("");
        wm4.setGrade(3);
        wm4.setTitle("Over Pronation");
        wm4.setIndex("4");
        wm4.setInfo("内旋不足-外八-足内翻(脚跟外侧—脚掌外侧着地)");
        wm4.setFunction("Try to feel the contact point when  you land your foot on the ground, try to land in the midline of your foot");
        wm4.setResult("This means the foot and ankle have problems stabilizing the body, and shock isn't absorbed as efficiently.");
        int[] ids4 = new int[]{R.raw.nxbz};
        wm4.setBids(ids4);
        wm4.setGids(ids4);
        UtilConstants.WaringMap.put("4", wm4);

        WaringModel wm5 = new WaringModel();
        wm5.setCondition("");
        wm5.setTitle("Under Pronation");
        wm5.setGrade(3);
        wm5.setIndex("5");
        wm5.setInfo("过度内旋-内八-足外翻 (脚跟外侧—脚掌内侧着地)");
        wm5.setFunction("Try to feel the contact point when  you land your foot on the ground, try to land in the midline of your foot");
        wm5.setResult("This means the foot and ankle have problems stabilizing the body, and shock isn't absorbed as efficiently.");
        int[] ids5 = new int[]{R.raw.nxgd};
        wm5.setBids(ids5);
        wm5.setGids(ids5);
        UtilConstants.WaringMap.put("5", wm5);

        WaringModel wm6 = new WaringModel();
        wm6.setCondition("");
        wm6.setGrade(2);
        wm6.setTitle("Quadriceps Femoris Fatigue");
        wm6.setIndex("6");
        wm6.setInfo("股四头肌疲劳 ");
        wm6.setFunction("Please take a rest. ");
        wm6.setResult("The quadriceps, play the important role of stabilizing the patella and the knee joint during running.");
        int[] bids6 = new int[]{R.raw.jrpl_boy};
        wm6.setBids(bids6);
        int[] gids6 = new int[]{R.raw.jrpl_girl};
        wm6.setGids(gids6);
        UtilConstants.WaringMap.put("6", wm6);

        WaringModel wm7 = new WaringModel();
        wm7.setCondition("");
        wm7.setGrade(1);
        wm7.setIndex("7");
        wm7.setTitle("Knee Flexion Angle Not Enough");
        wm7.setInfo("屈膝不足");
        wm7.setFunction("Make sure your foot don't pass your knee when you land it on the ground");
        wm7.setResult("Increasing the knee flexion angle at ground contact can reduce the peak vertical ground reaction impact force");
        int[] bids7 = new int[]{R.raw.big_stride_boy, R.raw.smaill_stride_boy};
        int[] gids7 = new int[]{R.raw.big_stride_girl, R.raw.smaill_stride_girl};
        wm7.setBids(bids7);
        wm7.setGids(gids7);
        UtilConstants.WaringMap.put("7", wm7);

        WaringModel wm0 = new WaringModel();
        wm0.setCondition("开始");
        wm0.setGrade(0);
        wm0.setIndex("0");
        wm0.setTitle("start to run!");
        wm0.setInfo("开始动中");
        wm0.setResult(" ");
        wm0.setFunction("Run");
        int[] bids0 = new int[]{R.raw.stand_boy};
        int[] gids0 = new int[]{R.raw.stand_girl};
        wm0.setBids(bids0);
        wm0.setGids(gids0);
        UtilConstants.WaringMap.put("0", wm0);

        WaringModel wm10 = new WaringModel();
        wm10.setCondition("");
        wm10.setGrade(2);
        wm10.setIndex("10");
        wm10.setTitle("Vertical Oscillation High");
        wm10.setInfo("Vertical Oscillation High");
        wm10.setFunction(".Land fast and softer");
        wm10.setResult("Larger Vertical oscillation means larger vertical ground reaction force ");
        int[] bids10 = new int[]{R.raw.zzwlgd_big_boy, R.raw.zzwlgd_big_boy};
        int[] gids10 = new int[]{R.raw.zzwlgd_big_girl, R.raw.zzwlgd_big_girl};
        wm10.setBids(bids10);
        wm10.setGids(gids10);
        UtilConstants.WaringMap.put("10", wm10);

        WaringModel wm11 = new WaringModel();
        wm11.setCondition("");
        wm11.setGrade(2);
        wm11.setIndex("11");
        wm11.setTitle("Vertical Oscillation Too Large");
        wm11.setInfo("股四头肌疲劳 ");
        wm11.setFunction("Light soft & quick foot placement");
        wm11.setResult("Causes the body to spend too much time on the ground, pulling itself first into a mid-stance phase, and then propelling itself forward. Increased time on the ground means increased time for injury to occur");
        int[] bids11 = new int[]{R.raw.zzwlgd_big_boy, R.raw.zzwlgd_big_boy};
        int[] gids11 = new int[]{R.raw.zzwlgd_big_girl, R.raw.zzwlgd_big_girl};
        wm11.setBids(bids11);
        wm11.setGids(gids11);
        UtilConstants.WaringMap.put("11", wm11);
        */

    }
}
