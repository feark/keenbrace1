package com.keenbrace.adapter;

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
import com.keenbrace.greendao.KeenBrace;
import com.keenbrace.util.DateUitl;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by zrq on 15/12/24.
 */
public class BleDataItemAdapter extends ArrayAdapter<KeenBrace> {

    private Context ctx;
    private List<KeenBrace> boxList;
    private List<KeenBrace> copyBoxList;
    private boolean notiyfyByFilter=false;
    private OnKeenBraceListener listener;
    private OnKeenBraceDataListener boxDataListener;

    public void setOnKeenBraceItemListener(OnKeenBraceListener listener){
        this.listener = listener;
    }

    public void setOnBoxDataListener(OnKeenBraceDataListener listener){
        this.boxDataListener = listener;
    }

    public BleDataItemAdapter(Context context, int resource, List<KeenBrace> objects) {
        super(context, resource, objects);
        ctx = context;
        this.boxList = objects;
        copyBoxList = new ArrayList<KeenBrace>();
        copyBoxList.addAll(objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.item_keenbrace, null);
            holder = new ViewHolder();
            holder.ll_edit = (LinearLayout) convertView.findViewById(R.id.ll_edit);
            holder.item_left = (LinearLayout) convertView.findViewById(R.id.item_left);
            holder.item_right = (LinearLayout) convertView.findViewById(R.id.ll_delete);
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
        setData(holder,position);

        return convertView;
    }

    class ViewHolder {
        LinearLayout ll_edit;
        LinearLayout item_right;
        LinearLayout item_left;
        TextView tv_collection_time;
        TextView tv_format;
        TextView tv_bp;
        TextView tv_jl;
        TextView tv_waring;



        KeenBrace itemData;

        public KeenBrace getItemData() {
            return itemData;
        }

        public void setItemData(KeenBrace itemData) {
            this.itemData = itemData;
        }
    }


    public void updateView(View view,int progress,int max) {
      if(view == null) {
            return;
       }
         ViewHolder holder = (ViewHolder) view.getTag();

     }


    public void setData(final ViewHolder keenBrace, final int position){
        final KeenBrace item = keenBrace.getItemData();
        keenBrace.tv_waring.setText(item.getSumwarings()==null?"0":item.getSumwarings()+"");
        keenBrace.tv_bp.setText(item.getCadence()==null?"0":item.getCadence()+"");
        keenBrace.tv_jl.setText(item.getMileage()==null?"0":item.getMileage()+"");
        keenBrace.tv_collection_time.setText(DateUitl.getDateTimeFromLong2String(item.getStartTime()));

        keenBrace.tv_format.setText(DateUitl.getDateFormat4(item.getTimelength()));
        keenBrace.item_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onKeenBraceDelete(keenBrace.getItemData());
                }
            }
        });
        keenBrace.ll_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onKeenBraceFunc(keenBrace.getItemData());
                }
            }
        });
    }


    public interface OnKeenBraceListener{
        void onKeenBraceFunc(KeenBrace boxItem);
        void onKeenBraceDelete(KeenBrace boxItem);
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
