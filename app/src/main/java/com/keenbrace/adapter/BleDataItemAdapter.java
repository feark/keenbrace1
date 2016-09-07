package com.keenbrace.adapter;

//这个文件是与历史结果列表相关的 ken

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.keenbrace.R;
import com.keenbrace.core.utils.DensityUtils;
import com.keenbrace.greendao.CommonResult;
import com.keenbrace.util.DateUitl;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by zrq on 15/12/24.
 */
public class BleDataItemAdapter extends ArrayAdapter<CommonResult> {

    private Context ctx;
    private List<CommonResult> boxList;
    private List<CommonResult> copyBoxList;
    private boolean notiyfyByFilter=false;
    private OnKeenBraceListener listener;
    private OnKeenBraceDataListener boxDataListener;

    public void setOnKeenBraceItemListener(OnKeenBraceListener listener){
        this.listener = listener;
    }

    public void setOnBoxDataListener(OnKeenBraceDataListener listener){
        this.boxDataListener = listener;
    }

    public BleDataItemAdapter(Context context, int resource, List<CommonResult> objects) {
        super(context, resource, objects);
        ctx = context;
        this.boxList = objects;
        copyBoxList = new ArrayList<CommonResult>();
        copyBoxList.addAll(objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.item_runrecord, null);
            holder = new ViewHolder();
            holder.item_left = (LinearLayout) convertView.findViewById(R.id.item_left);
            holder.item_right = (RelativeLayout) convertView.findViewById(R.id.item_right);
            holder.tv_waring = (TextView) convertView.findViewById(R.id.tv_waring);
            holder.tv_bp = (TextView) convertView.findViewById(R.id.tv_bp);
            holder.tv_jl = (TextView) convertView.findViewById(R.id.tv_jl);
            holder.tv_collection_time = (TextView) convertView.findViewById(R.id.tv_collection_time);
            holder.tv_format = (TextView) convertView.findViewById(R.id.tv_format);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.setItemData(getItem(position));
        }

        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        holder.item_left.setLayoutParams(lp1);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(DensityUtils.dip2px(ctx, 80), LinearLayout.LayoutParams.MATCH_PARENT);
        holder.item_right.setLayoutParams(lp2);


        holder.setItemData(getItem(position));
        //setData(holder,position);

        return convertView;
    }

    class ViewHolder {
        LinearLayout item_left;
        RelativeLayout item_right;
        TextView tv_collection_time;
        TextView tv_format;
        TextView tv_bp;
        TextView tv_jl;
        TextView tv_waring;



        CommonResult itemData;

        public CommonResult getItemData() {
            return itemData;
        }

        public void setItemData(CommonResult itemData) {
            this.itemData = itemData;
        }
    }


    public void updateView(View view,int progress,int max) {
      if(view == null) {
            return;
       }
         ViewHolder holder = (ViewHolder) view.getTag();

     }

    /*
    public void setData(final ViewHolder RunResult, final int position){
        final CommonResult item = RunResult.getItemData();
        CommonResult.tv_bp.setText(item.getCadence()==null?"0":item.getCadence()+"");
        CommonResult.tv_jl.setText(item.getMileage()==null?"0":item.getMileage()+"");
        CommonResult.tv_collection_time.setText(DateUitl.getDateTimeFromLong2String(item.getStartTime()));

        CommonResult.tv_format.setText(DateUitl.getDateFormat4(item.getDuration()));
        CommonResult.item_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onKeenBraceDelete(CommonResult.getItemData());
                }
            }
        });
    }
    */

    public interface OnKeenBraceListener{
        void onKeenBraceFunc(CommonResult boxItem);
        void onKeenBraceDelete(CommonResult boxItem);
    }


    public interface OnKeenBraceDataListener{
        void onBoxDataUpdate(int amont, int current);
    }




    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if(!notiyfyByFilter){
            copyBoxList.clear();
            copyBoxList.addAll(boxList);
            notiyfyByFilter = false;
        }
        if (boxDataListener!=null){
            boxDataListener.onBoxDataUpdate(copyBoxList.size(), boxList.size());
        }
    }


}
