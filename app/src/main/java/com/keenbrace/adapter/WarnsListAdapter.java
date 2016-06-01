package com.keenbrace.adapter;

import java.util.ArrayList;
import java.util.List;

import com.keenbrace.R;
import com.keenbrace.constants.UtilConstants;
import com.keenbrace.greendao.RunWaring;
import com.keenbrace.util.DateUitl;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WarnsListAdapter extends BaseAdapter {
    private ArrayList<RunWaring> runWarings;
    Context context;
    private int selectId = -1;
    private int showId = -1;

    public int getSelectId() {
        return selectId;
    }

    public void setSelectId(int selectId) {
        this.selectId = selectId;
    }

    public WarnsListAdapter(Context context) {
        super();
        this.context = context;
        runWarings = new ArrayList<RunWaring>();

    }

    public void addRunWarings(List<RunWaring> runWaring) {
        runWarings.addAll(runWaring);
    }

    public void addRunWaring(RunWaring runWaring) {
        runWarings.add(runWaring);
    }

    public RunWaring getDevice(int position) {
        return runWarings.get(position);
    }

    public void clear() {
        runWarings.clear();
    }

    @Override
    public int getCount() {
        return runWarings.size();
    }

    @Override
    public Object getItem(int i) {
        return runWarings.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = View.inflate(context, R.layout.row_message, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_title = (TextView) view
                    .findViewById(R.id.tv_title);
//			viewHolder.tv_content = (TextView) view
//					.findViewById(R.id.tv_content);
            viewHolder.tv_time = (TextView) view.findViewById(R.id.tv_time);
            viewHolder.ll_waring = (LinearLayout) view
                    .findViewById(R.id.ll_waring);
            viewHolder.tv_result = (TextView) view.findViewById(R.id.tv_result);
            //viewHolder.tv_function = (TextView) view.findViewById(R.id.tv_function);
            viewHolder.iv_warning = (ImageView) view.findViewById(R.id.iv_warning);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        try {
            final RunWaring rw = runWarings.get(i);
//			if(selectId==i&&rw.getIndex()>0)
//			{
//				if(showId!=selectId){
//				viewHolder.ll_waring.setVisibility(View.VISIBLE);
//				showId=selectId;
//				}else
//				{
//					showId=-1;
//				}
//			
//			}else
//			{
//				viewHolder.ll_waring.setVisibility(View.GONE);
//		
//			}
            int t = UtilConstants.WaringMap.get(rw.getIndex() + "").getGrade();
            if (t == 1) {
                viewHolder.iv_warning
                        .setImageResource(R.mipmap.medal_g);
            } else if (t == 2) {
                viewHolder.iv_warning
                        .setImageResource(R.mipmap.warning_y);
            } else if (t == 3) {
                viewHolder.iv_warning
                        .setImageResource(R.mipmap.warning_r);
            } else if (t == 0) {
                viewHolder.iv_warning
                        .setImageResource(R.mipmap.medal_g);
            }
            viewHolder.tv_title.setText(UtilConstants.WaringMap.get(rw.getIndex() + "").getTitle());
            //viewHolder.tv_content.setText(UtilConstants.WaringMap.get(rw.getIndex()+"").getInfo());
            //viewHolder.tv_function.setText(UtilConstants.WaringMap.get(rw.getIndex()+"").getFunction());
            viewHolder.tv_result.setText(UtilConstants.WaringMap.get(rw.getIndex() + "").getResult());
            viewHolder.tv_time.setText(DateUitl.getDateFormat4Id(rw
                    .getCreateTime()));

        } catch (Exception e) {
        }
        return view;
    }

    static class ViewHolder {
        LinearLayout ll_waring;
        ImageView iv_warning;
        //TextView tv_content;
        TextView tv_time;
        TextView tv_title;
        TextView tv_result;

    }


}
