package com.keenbrace.activity;

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
import android.widget.Toast;

import com.keenbrace.AppContext;
import com.keenbrace.R;
import com.keenbrace.adapter.BleDataItemAdapter;
import com.keenbrace.adapter.BleDataListAdapter;
import com.keenbrace.api.KeenbraceRetrofit;
import com.keenbrace.base.BaseActivity;
import com.keenbrace.bean.KeenbraceDBHelper;
import com.keenbrace.bean.request.RunRequest;
import com.keenbrace.bean.response.Result;
import com.keenbrace.constants.UtilConstants;
import com.keenbrace.core.utils.PreferenceHelper;
import com.keenbrace.core.utils.StringUtils;
import com.keenbrace.greendao.KeenBrace;
import com.keenbrace.util.StringUtil;
import com.keenbrace.widget.SwipeListView;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RecordActivity extends BaseActivity implements OnClickListener,BleDataItemAdapter.OnKeenBraceListener {
    RelativeLayout rl_showlists, rl_showReports;
    TextView tx_sum, tx_warings, tx_mileage;
    SwipeListView lvdata;

    BleDataItemAdapter adapter;
    List<KeenBrace> datas;

    public void init() {
        datas = KeenbraceDBHelper.getInstance(this).queryKeenBraces();
        if(datas==null)
            return;
        HashMap<String, String> sumMap = KeenbraceDBHelper.getInstance(this).querySumBle();
        int sumlc = 0;

        float[] pl = new float[datas.size()];
        int i = 0;
        for (KeenBrace data : datas) {
            sumlc += data.getMileage()==null?0:data.getMileage();
            pl[i] = data.getSumscore()==null?0:data.getSumscore();
            i++;
        }
        // setData(pl);
        adapter = new BleDataItemAdapter(RecordActivity.this, R.layout.item_keenbrace, datas);
        adapter.setOnKeenBraceItemListener(this);
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
        tx_warings = (TextView) findViewById(R.id.tx_warings);
        tx_mileage = (TextView) findViewById(R.id.tx_mileage);
        rl_showlists = (RelativeLayout) findViewById(R.id.rl_showlists);
        rl_showReports = (RelativeLayout)
                findViewById(R.id.rl_showReports);


    }

    @Override
    public void initData() {
         init();
    }

    @Override
    public void onKeenBraceFunc(final KeenBrace boxItem) {
        if(PreferenceHelper.readBoolean(AppContext.getInstance(),
                UtilConstants.SHARE_PREF, UtilConstants.KEY_HAS_LOGIN, true)){
            readyGoThenKill(LoginActivity.class);
        }
        if(boxItem.getState()==1)
        {
            showToast("上传成功！");
        }
        showWaitDialog();
        RunRequest runRequest=new RunRequest();
        runRequest.setKeenBrace(boxItem);
        runRequest.setRunWarings(KeenbraceDBHelper.getInstance(this).queryRunWaringByRunId(boxItem.getId()));
        Observable<Result> observable = new KeenbraceRetrofit()
                .createBaseApi()
                .putRunData(runRequest);

        _subscriptions.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideWaitDialog();
                        Toast.makeText(RecordActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Result result) {
                        hideWaitDialog();
                        if (result.getResultCode().equals("0")) {
                            boxItem.setState(1);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(RecordActivity.this, "上传记录成功", Toast.LENGTH_SHORT).show();
                        } else {

                            String msg = result.getMsg();
                            if (StringUtils.isEmpty(msg)) {
                                msg = "上传记录失败";
                            }
                            Toast.makeText(RecordActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                }));
    }

    @Override
    public void onKeenBraceDelete(KeenBrace boxItem) {
        KeenbraceDBHelper.getInstance(this).deleteRunWarings(boxItem.getId());
        datas.remove(boxItem);
        adapter.notifyDataSetChanged();
        Toast.makeText(RecordActivity.this, "删除记录成功", Toast.LENGTH_SHORT).show();
    }
}
