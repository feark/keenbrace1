package com.keenbrace.activity;

//没有使用 ken

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.keenbrace.R;
import com.keenbrace.adapter.BleDataItemAdapter;
import com.keenbrace.adapter.BleDataListAdapter;
import com.keenbrace.base.BaseActivity;
import com.keenbrace.bean.RunResultDBHelper;
import com.keenbrace.greendao.RunResult;
import com.keenbrace.util.StringUtil;
import com.keenbrace.widget.SwipeListView;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

public class RecordActivity extends BaseActivity implements OnClickListener {
    RelativeLayout rl_showlists, rl_showReports;
    TextView tx_sum, tx_warings, tx_mileage;
    SwipeListView lvdata;


    List<RunResult> datas;

    public void init() {
        datas = RunResultDBHelper.getInstance(this).queryRunResult();
        if(datas==null)
            return;
        HashMap<String, String> sumMap = RunResultDBHelper.getInstance(this).querySumBle();
        int sumDistance = 0;

        for (RunResult data : datas) {
            sumDistance += data.getMileage()==null?0:data.getMileage();
        }
        // setData(pl);
        BleDataItemAdapter adapter = new BleDataItemAdapter(RecordActivity.this, R.layout.item_keenbrace, datas);
        lvdata.setAdapter(adapter);
        tx_sum.setText("" + datas.size());
//		if (sumMap.get("timelength") != null)
//			tx_times.setText( DateUitl.getDateFormat4(Integer.parseInt(sumMap.get("timelength"))));
//		else
//			tx_times.setText("0");
        if (sumMap.get("sumwarings") != null)
            tx_warings.setText("" + sumMap.get("sumwarings"));
        else
            tx_warings.setText("0");
        if (sumMap.get("mileage") != null)
            tx_mileage.setText("" + StringUtil.formatToLC(sumMap.get("mileage")));
        else
            tx_mileage.setText("0");
        lvdata.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                Intent intent = new Intent();
                intent.putExtra("bleData", datas.get(arg2));
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
        this.setActionBarTitle("History");
        lvdata = (SwipeListView) findViewById(R.id.lv_data);
        tx_sum = (TextView) findViewById(R.id.tx_sum);
        tx_warings = (TextView) findViewById(R.id.tx_warnings);
        tx_mileage = (TextView) findViewById(R.id.tx_mileage);
        rl_showlists = (RelativeLayout) findViewById(R.id.rl_showlists);
        rl_showReports = (RelativeLayout)
                findViewById(R.id.rl_showReports);


    }

    @Override
    public void initData() {
         init();
    }
}
