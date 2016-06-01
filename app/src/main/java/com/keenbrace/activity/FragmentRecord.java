package com.keenbrace.activity;

import java.util.HashMap;
import java.util.List;

import com.keenbrace.AppConfig;
import com.keenbrace.AppContext;
import com.keenbrace.R;
import com.keenbrace.adapter.BleDataItemAdapter;
import com.keenbrace.adapter.BleDataListAdapter;
import com.keenbrace.api.KeenbraceRetrofit;
import com.keenbrace.bean.Constant;
import com.keenbrace.bean.KeenbraceDBHelper;
import com.keenbrace.bean.request.RunRequest;
import com.keenbrace.bean.response.LoginResponse;
import com.keenbrace.bean.response.Result;
import com.keenbrace.core.utils.StringUtils;
import com.keenbrace.greendao.KeenBrace;
import com.keenbrace.greendao.User;
import com.keenbrace.storage.BleData;
import com.keenbrace.util.StringUtil;

import android.os.Bundle;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FragmentRecord extends BaseFragment implements OnClickListener,BleDataItemAdapter.OnKeenBraceListener,BleDataItemAdapter.OnKeenBraceDataListener {
    ListView lv_datas;
    RelativeLayout rl_showlists, rl_showReports;
    TextView tx_sum, tx_warings, tx_mileage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_record, null);
        lv_datas = (ListView) view.findViewById(R.id.lv_datas);
        tx_sum = (TextView) view.findViewById(R.id.tx_sum);
        tx_warings = (TextView) view.findViewById(R.id.tx_warings);
        tx_mileage = (TextView) view.findViewById(R.id.tx_mileage);
        rl_showlists = (RelativeLayout) view.findViewById(R.id.rl_showlists);
        rl_showReports = (RelativeLayout) view
                .findViewById(R.id.rl_showReports);

        init();
        //initBackView("");
        return view;
    }

    List<KeenBrace> datas;

    public void init() {
        datas = KeenbraceDBHelper.getInstance(this.getActivity()).queryKeenBraces();
        if(datas==null)
            return;
        HashMap<String, String> sumMap = KeenbraceDBHelper.getInstance(this.getActivity()).querySumBle();
        int sumlc = 0;

        float[] pl = new float[datas.size()];
        int i = 0;
        for (KeenBrace data : datas) {
            sumlc += data.getMileage()==null?0:data.getMileage();
            pl[i] = data.getSumscore()==null?0:data.getSumscore();
            i++;
        }
        // setData(pl);
        BleDataItemAdapter adapter = new BleDataItemAdapter(this.getActivity(),R.layout.item_keenbrace, datas);
        lv_datas.setAdapter(adapter);
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
        lv_datas.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                Intent intent = new Intent();
                intent.putExtra("bleData", datas.get(arg2));
                intent.setClass(FragmentRecord.this.getActivity(), ViewRecordActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBoxDataUpdate(int amont, int current) {

    }

    @Override
    public void onKeenBraceFunc(KeenBrace boxItem) {
        RunRequest runRequest=new RunRequest();

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

                        Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onNext(Result result) {
                        if (result.getResultCode().equals("0")) {

                        }else {

                            String msg = result.getMsg();
                            if (StringUtils.isEmpty(msg)) {
                                msg = "登录失败";
                            }
                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                }));

    }

    @Override
    public void onKeenBraceDelete(KeenBrace boxItem) {
    }
}
