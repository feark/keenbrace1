package com.keenbrace.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.keenbrace.R;
import com.keenbrace.activity.MainMenuActivity;
import com.keenbrace.bean.CommonResultDBHelper;
import com.keenbrace.constants.UtilConstants;
import com.keenbrace.activity.MainActivity;
import com.keenbrace.base.BaseFragment;
import com.keenbrace.greendao.CommonResult;

import java.util.HashMap;
import java.util.List;


import butterknife.Bind;
import butterknife.OnClick;



/**
 * Created by zrq on 16/1/28.
 */
public class MainFragment extends BaseFragment {
    /*
    @Bind(R.id.tx_times)
    TextView tx_times;
    @Bind(R.id.tx_warnings)
    TextView tx_warnings;
    @Bind(R.id.tx_mileage)
    TextView tx_mileage;
    */
    @Bind(R.id.tv_msg)
    TextView tv_msg;

    @Bind(R.id.iv_outdoor)
    ImageView iv_outdoor;
    @Bind(R.id.iv_apparatus)
    ImageView iv_apparatus;
    @Bind(R.id.iv_bodyweight)
    ImageView iv_bodyweight;

    @Bind(R.id.iv_run)
    ImageView iv_run;
    @Bind(R.id.iv_squat)
    ImageView iv_squat;
    @Bind(R.id.iv_dumbbell)
    ImageView iv_dumbbell;
    @Bind(R.id.iv_plank)
    ImageView iv_plank;
    @Bind(R.id.iv_pullup)
    ImageView iv_pullup;

    @Bind(R.id.howtowear)
    ImageView howtowear;

    @Bind(R.id.ss_start)
    Button ss_start;

    @Bind(R.id.start_loading)
    ImageView start_loading;

    //将选中的运动类型传到下一个Activity
    int sport_type = 0;

    @OnClick(R.id.ss_start)
    void onClickStart() {
        //ss_start.setVisibility(View.INVISIBLE);//ken 直接到主界面
        //start_loading.setVisibility(View.VISIBLE);

        //ss_start.setBackgroundResource(R.mipmap.select_y);

        Toast.makeText(super.getActivity(), "loading...",
                Toast.LENGTH_SHORT).show();

        //ss_start.setBackgroundResource(R.mipmap.select);

        readyGo(MainMenuActivity.class);

    }

    @OnClick (R.id.iv_outdoor)
    void categoryOutdoor(){
        tv_msg.setText("");

        iv_outdoor.setImageResource(R.mipmap.outdoor_sports_y);
        iv_apparatus.setImageResource(R.mipmap.facilities_sports);
        iv_bodyweight.setImageResource(R.mipmap.non_apparatus_sports);

        iv_run.setVisibility(View.VISIBLE);
        iv_squat.setVisibility(View.GONE);
        iv_dumbbell.setVisibility(View.GONE);
        iv_plank.setVisibility(View.GONE);
        iv_pullup.setVisibility(View.GONE);
    }

    @OnClick (R.id.iv_apparatus)
    void categoryApparatus(){
        tv_msg.setText("");

        iv_outdoor.setImageResource(R.mipmap.outdoor_sports);
        iv_apparatus.setImageResource(R.mipmap.facilities_sports_y);
        iv_bodyweight.setImageResource(R.mipmap.non_apparatus_sports);

        iv_run.setVisibility(View.GONE);
        iv_squat.setVisibility(View.GONE);
        iv_dumbbell.setVisibility(View.VISIBLE);
        iv_plank.setVisibility(View.GONE);
        iv_pullup.setVisibility(View.VISIBLE);
    }

    @OnClick (R.id.iv_bodyweight)
    void categoryBodyweight(){
        tv_msg.setText("");

        iv_outdoor.setImageResource(R.mipmap.outdoor_sports);
        iv_apparatus.setImageResource(R.mipmap.facilities_sports);
        iv_bodyweight.setImageResource(R.mipmap.non_apparatus_sports_y);

        iv_run.setVisibility(View.GONE);
        iv_squat.setVisibility(View.VISIBLE);
        iv_dumbbell.setVisibility(View.GONE);
        iv_plank.setVisibility(View.VISIBLE);
        iv_pullup.setVisibility(View.GONE);
    }

    @OnClick (R.id.iv_more)
    void onMore(){
        ((MainActivity)getActivity()).showMenu();
    }

    //主界面上的运动选择点击行为
    @OnClick(R.id.iv_run)
    void gotoRun()
    {
        iv_run.setImageResource(R.mipmap.main_run_y);

        iv_squat.setImageResource(R.mipmap.main_squat_w);
        iv_dumbbell.setImageResource(R.mipmap.main_dumbbell_w);
        iv_plank.setImageResource(R.mipmap.main_plank_w);
        iv_pullup.setImageResource(R.mipmap.main_pullup_w);

        tv_msg.setText("Running");

        howtowear.setImageResource(R.mipmap.wear_on_thigh);

        sport_type = UtilConstants.sport_running;
    }

    @OnClick(R.id.iv_squat)
    void gotoSquat()
    {
        iv_squat.setImageResource(R.mipmap.main_squat_y);

        iv_run.setImageResource(R.mipmap.main_run_w);
        iv_dumbbell.setImageResource(R.mipmap.main_dumbbell_w);
        iv_plank.setImageResource(R.mipmap.main_plank_w);
        iv_pullup.setImageResource(R.mipmap.main_pullup_w);

        tv_msg.setText("Squat");

        howtowear.setImageResource(R.mipmap.wear_on_thigh);

        sport_type = UtilConstants.sport_squat;
    }

    @OnClick(R.id.iv_dumbbell)
    void gotoDumbbell()
    {
        iv_dumbbell.setImageResource(R.mipmap.main_dumbbell_y);

        iv_squat.setImageResource(R.mipmap.main_squat_w);
        iv_run.setImageResource(R.mipmap.main_run_w);
        iv_plank.setImageResource(R.mipmap.main_plank_w);
        iv_pullup.setImageResource(R.mipmap.main_pullup_w);

        tv_msg.setText("Dumb Bell");

        howtowear.setImageResource(R.mipmap.wear_on_wrist);

        sport_type = UtilConstants.sport_dumbbell;
    }

    @OnClick(R.id.iv_plank)
    void gotoPlank()
    {
        iv_plank.setImageResource(R.mipmap.main_plank_y);

        iv_dumbbell.setImageResource(R.mipmap.main_dumbbell_w);
        iv_squat.setImageResource(R.mipmap.main_squat_w);
        iv_run.setImageResource(R.mipmap.main_run_w);
        iv_pullup.setImageResource(R.mipmap.main_pullup_w);

        tv_msg.setText("Plank");

        howtowear.setImageResource(R.mipmap.wear_on_thigh);

        sport_type = UtilConstants.sport_plank;
    }

    @OnClick(R.id.iv_pullup)
    void gotoPullup()
    {
        iv_pullup.setImageResource(R.mipmap.main_pullup_y);

        iv_plank.setImageResource(R.mipmap.main_plank_w);
        iv_dumbbell.setImageResource(R.mipmap.main_dumbbell_w);
        iv_squat.setImageResource(R.mipmap.main_squat_w);
        iv_run.setImageResource(R.mipmap.main_run_w);

        tv_msg.setText("Pull Up");

        howtowear.setImageResource(R.mipmap.wear_on_arm);

        sport_type = UtilConstants.sport_pullup;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_sport_select;
    }

    @Override
    public void initView() {
        iv_outdoor.setImageResource(R.mipmap.outdoor_sports_y);

        //默认为跑步
        //iv_run.setImageResource(R.mipmap.main_run_y);
        iv_run.setVisibility(View.VISIBLE);
        iv_squat.setVisibility(View.GONE);
        iv_dumbbell.setVisibility(View.GONE);
        iv_plank.setVisibility(View.GONE);
        iv_pullup.setVisibility(View.GONE);

        ss_start.setBackgroundResource(R.mipmap.select);
    }

    @Override
    public void initData() {
        List<CommonResult> datas = CommonResultDBHelper.getInstance(this.getActivity()).queryCommonResult();
        if(datas==null)
            return;
        HashMap<String, String> sumMap = CommonResultDBHelper.getInstance(this.getActivity()).querySumRunResult();
        int sumDistance = 0;

        //计算总里程
        for (CommonResult data : datas) {
            sumDistance += data.getMileage()==null?0:data.getMileage();
        }

        /*
        tx_times.setText("" + datas.size());
//		if (sumMap.get("timelength") != null)
//			tx_times.setText( DateUitl.getDateFormat4(Integer.parseInt(sumMap.get("timelength"))));
//		else
//			tx_times.setText("0");
        if (sumMap.get("sumwarings") != null)
            tx_warnings.setText("" + sumMap.get("sumwarings"));
        else
            tx_warnings.setText("0");
        if (sumMap.get("mileage") != null)
            tx_mileage.setText("" + StringUtil.formatToLC(sumMap.get("mileage")));
        else
            tx_mileage.setText("0");
        */
    }


    public void readyGo(Class c)

    {
        Intent intent1 = new Intent(this.getActivity(),c);
        intent1.putExtra("sport_type", sport_type);

        startActivity(intent1);

    }

}
