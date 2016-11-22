package com.keenbrace.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.keenbrace.R;
import com.keenbrace.greendao.ShortPlan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ken on 16-9-6.
 */
public class TrainOfDateItemAdapter extends ArrayAdapter<ShortPlan> {

    private Context ctx;
    private List<ShortPlan> boxList;
    private List<ShortPlan> copyBoxList;
    private boolean notiyfyByFilter=false;
    private OnTrainOfDateListener listener;
    private OnTrainOfDateDataListener boxDataListener;

    public void setOnTrainOfDateItemListener(OnTrainOfDateListener listener){
        this.listener = listener;
    }

    public void setOnBoxDataListener(OnTrainOfDateDataListener listener){
        this.boxDataListener = listener;
    }

    public TrainOfDateItemAdapter(Context context, int resource, List<ShortPlan> objects) {
        super(context, resource, objects);
        ctx = context;
        this.boxList = objects;
        copyBoxList = new ArrayList<ShortPlan>();
        copyBoxList.addAll(objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.item_trainofdate, null);
            holder = new ViewHolder();
            holder.shortplanLogo = (ImageView) convertView.findViewById(R.id.iv_workoutlogo);
            holder.shortplanstatus = (ImageView) convertView.findViewById(R.id.iv_workoutstatus);
            holder.shortplantitle = (TextView) convertView.findViewById(R.id.tx_workouttitle);
            holder.shortplantime = (TextView) convertView.findViewById(R.id.tx_workouttime);
            holder.shortplanpos = (TextView) convertView.findViewById(R.id.tx_workoutpos);
            holder.shortplanstrength = (TextView) convertView.findViewById(R.id.tx_workoutstrength);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.setItemData(getItem(position));
        }

        holder.setItemData(getItem(position));
        setData(holder, position);

        return convertView;
    }

    class ViewHolder {
        ImageView shortplanLogo;
        ImageView shortplanstatus;
        TextView shortplantitle;
        TextView shortplantime;
        TextView shortplanpos;
        TextView shortplanstrength;

        ShortPlan itemData;

        public ShortPlan getItemData() {
            return itemData;
        }

        public void setItemData(ShortPlan itemData) {
            this.itemData = itemData;
        }
    }

    public void setData(final ViewHolder shortplan, int position){
        final ShortPlan item = shortplan.getItemData();

        shortplan.shortplanLogo.setImageResource(item.getLogo() == null ? R.mipmap.kb_challenge : item.getLogo());

        switch (item.getStatus())
        {
            case 0:
                shortplan.shortplanstatus.setImageResource(R.mipmap.wrong_s);
                break;

            case 1:
                shortplan.shortplanstatus.setImageResource(R.mipmap.right_s);
                break;

            default:
                shortplan.shortplanstatus.setImageResource(R.mipmap.wrong_s);
                break;
        }


        shortplan.shortplantitle.setText(item.getShortPlanName() == null ? "KeenBrace CrossFit" : item.getShortPlanName() + "");
        shortplan.shortplantime.setText(item.getTotalTime() == null ? "0 minutes" : item.getTotalTime()+" minutes");

        shortplan.shortplanpos.setText(item.getPos() == null ? "Full Body" : item.getPos()+"");

        switch (item.getIntense())
        {
            case 1:
                shortplan.shortplanstrength.setText("Casual");
                break;

            case 2:
                shortplan.shortplanstrength.setText("Moderate");
                break;

            case 3:
                shortplan.shortplanstrength.setText("Intense");
                break;

            default:
                break;
        }

        /*
        keenBrace.ll_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onKeenBraceFunc(keenBrace.getItemData());
                }
            }
        });
        */
    }


    public void updateView(View view,int progress,int max) {
        if(view == null) {
            return;
        }
        ViewHolder holder = (ViewHolder) view.getTag();

    }

    public interface OnTrainOfDateListener{
        void onTrainOfDateFunc(ShortPlan boxItem);
    }


    public interface OnTrainOfDateDataListener{
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