package com.keenbrace.activity;

//这个页面没有被使用  ken

import java.util.HashMap;
import java.util.List;

import com.keenbrace.R;
import com.keenbrace.adapter.BleDataItemAdapter;
import com.keenbrace.adapter.BleDataListAdapter;
import com.keenbrace.bean.RunResultDBHelper;
import com.keenbrace.greendao.RunResult;
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

public class FragmentRecord extends Fragment implements OnClickListener,BleDataItemAdapter.OnKeenBraceListener,BleDataItemAdapter.OnKeenBraceDataListener {

    RelativeLayout rl_showlists, rl_showReports;
    TextView tx_sum, tx_warings, tx_mileage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_record, null);

        tx_sum = (TextView) view.findViewById(R.id.tx_sum);
        tx_warings = (TextView) view.findViewById(R.id.tx_warnings);
        tx_mileage = (TextView) view.findViewById(R.id.tx_mileage);
        rl_showlists = (RelativeLayout) view.findViewById(R.id.rl_showlists);
        rl_showReports = (RelativeLayout) view
                .findViewById(R.id.rl_showReports);

        init();
        //initBackView("");
        return view;
    }

    List<RunResult> datas;

    public void init() {
        datas = RunResultDBHelper.getInstance(this.getActivity()).queryRunResult();
        if(datas==null)
            return;
        HashMap<String, String> sumMap = RunResultDBHelper.getInstance(this.getActivity()).querySumBle();
        int sumDistance = 0;

        for (RunResult data : datas) {
            sumDistance += data.getMileage()==null?0:data.getMileage();
        }
        // setData(pl);
        BleDataItemAdapter adapter = new BleDataItemAdapter(this.getActivity(),R.layout.item_keenbrace, datas);

        tx_sum.setText("" + datas.size());

        if (sumMap.get("sumwarings") != null)
            tx_warings.setText("" + sumMap.get("sumwarings"));
        else
            tx_warings.setText("0");
        if (sumMap.get("mileage") != null)
            tx_mileage.setText("" + StringUtil.formatToLC(sumMap.get("mileage")));
        else
            tx_mileage.setText("0");

    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBoxDataUpdate(int amont, int current) {

    }

    @Override
    public void onKeenBraceFunc(RunResult boxItem) {

    }

    @Override
    public void onKeenBraceDelete(RunResult boxItem) {

    }
}
