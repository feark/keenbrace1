package com.keenbrace.activity;

//运动结果历史列表 ken

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.keenbrace.R;
import com.keenbrace.adapter.BleDataItemAdapter;
import com.keenbrace.base.BaseActivity;
import com.keenbrace.bean.CommonResultDBHelper;
import com.keenbrace.greendao.CommonResult;
import com.keenbrace.widget.SwipeListView;

import java.util.HashMap;
import java.util.List;


public class RecordActivity extends BaseActivity implements OnClickListener {
    RelativeLayout rl_showlists, rl_showReports;
    TextView tx_sum, tx_warings, tx_mileage;
    SwipeListView lvdata;
    View view;
    List<CommonResult> datas;

    public void init() {
        datas = CommonResultDBHelper.getInstance(this).queryCommonResult();
        if(datas==null) {
            return;
        }

        HashMap<String, String> sumMap = CommonResultDBHelper.getInstance(this).querySumRunResult();
        int sumDistance = 0;

        //求总路程
        for (CommonResult data : datas) {
            sumDistance += data.getMileage()==null?0:data.getMileage();
        }

        // setData(pl);
        //每个记录的背景 ken
        BleDataItemAdapter adapter = new BleDataItemAdapter(RecordActivity.this, R.layout.item_runrecord, datas);
        //设置列表里的数据 ken
        lvdata.setAdapter(adapter);

//        tx_sum.setText("" + datas.size());
//		if (sumMap.get("timelength") != null)
//			tx_times.setText( DateUitl.getDateFormat4(Integer.parseInt(sumMap.get("timelength"))));
//		else
//			tx_times.setText("0");
/*
        if (sumMap.get("sumwarings") != null)
            tx_warings.setText("" + sumMap.get("sumwarings"));
        else
            tx_warings.setText("0");
        if (sumMap.get("mileage") != null)
            tx_mileage.setText("" + StringUtil.formatToLC(sumMap.get("mileage")));
        else
            tx_mileage.setText("0");
*/
        //点击结果列表的其中一项 进入详细信息 ken
        lvdata.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                Intent intent = new Intent();
                intent.putExtra("CommonResult", datas.get(arg2));
                intent.setClass(RecordActivity.this, ViewRecordActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_record;
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
    public void onClick(View v) {

    }

    @Override
    public void initView() {
        this.setActionBarTitle("Activities");
        lvdata = (SwipeListView) findViewById(R.id.lv_data);
        /*
        tx_sum = (TextView) findViewById(R.id.tx_sum);
        tx_warings = (TextView) findViewById(R.id.tx_warnings);
        tx_mileage = (TextView) findViewById(R.id.tx_mileage);
        rl_showlists = (RelativeLayout) findViewById(R.id.rl_showlists);
        rl_showReports = (RelativeLayout)
                findViewById(R.id.rl_showReports);
                */


    }

    @Override
    public void initData() {
         init();
    }
}
