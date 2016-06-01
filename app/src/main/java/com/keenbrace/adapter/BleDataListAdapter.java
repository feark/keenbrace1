package com.keenbrace.adapter;

import java.util.List;

import com.github.mikephil.charting.charts.LineChart;
import com.keenbrace.R;
import com.keenbrace.activity.ViewRecordActivity;

import com.keenbrace.greendao.KeenBrace;
import com.keenbrace.storage.BleData;
import com.keenbrace.util.DateUitl;
import com.keenbrace.util.StringUtil;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BleDataListAdapter extends BaseAdapter {
    private List<KeenBrace> bleDatas;
    Context context;

    public BleDataListAdapter(Context context, List<KeenBrace> datas) {
        super();
        this.context = context;
        bleDatas = datas;

    }

    public void addBleData(KeenBrace device) {
        if (!bleDatas.contains(device)) {
            bleDatas.add(device);
        }
    }

    public KeenBrace getDevice(int position) {
        return bleDatas.get(position);
    }

    public void clear() {
        bleDatas.clear();
    }

    @Override
    public int getCount() {
        return bleDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return bleDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = View.inflate(context, R.layout.listitem_history, null);
            viewHolder = new ViewHolder();

            viewHolder.tv_startTime = (TextView) view
                    .findViewById(R.id.tv_startTime);
            viewHolder.tx_pj = (TextView) view
                    .findViewById(R.id.tx_pj);
            viewHolder.tv_bp = (TextView) view
                    .findViewById(R.id.tv_bp);
            viewHolder.tv_jl = (TextView) view
                    .findViewById(R.id.tv_jl);
            viewHolder.tv_time = (TextView) view
                    .findViewById(R.id.tv_time);
            viewHolder.rl_goto = (RelativeLayout) view
                    .findViewById(R.id.rl_goto);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final KeenBrace data = bleDatas.get(i);
        viewHolder.tv_startTime.setText(DateUitl.getDateTimeFromLong2String(data.getStartTime()));
        viewHolder.tv_bp.setText(context.getResources().getString(R.string.bp_template, data.getCadence()));
        viewHolder.tx_pj.setText(context.getResources().getString(R.string.pj_template, data.getSumscore()));
        viewHolder.tv_jl.setText(context.getResources().getString(R.string.jl_template, StringUtil.formatToLC("" + data.getMileage())));
        viewHolder.tv_time.setText(context.getResources().getString(R.string.time_template, DateUitl.getDateFormat4(data.getTimelength())));
        viewHolder.rl_goto.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("bleData", data);
                intent.setClass(context, ViewRecordActivity.class);
                context.startActivity(intent);

            }
        });
        return view;
    }

    static class ViewHolder {
        TextView tv_startTime;
        TextView tx_pj;
        TextView tv_bp;
        TextView tv_jl;
        TextView tv_time;
        RelativeLayout rl_goto;
    }
}
