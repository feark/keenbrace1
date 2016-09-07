package com.keenbrace.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.keenbrace.R;
import com.keenbrace.core.utils.DensityUtils;
import com.keenbrace.greendao.Challenge;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ken on 16-9-6.
 */
public class ChallengeItemAdapter extends ArrayAdapter<Challenge> {

    private Context ctx;
    private List<Challenge> boxList;
    private List<Challenge> copyBoxList;
    private boolean notiyfyByFilter=false;
    private OnChallengeListener listener;
    private OnChallengeDataListener boxDataListener;

    public void setOnChallengeItemListener(OnChallengeListener listener){
        this.listener = listener;
    }

    public void setOnBoxDataListener(OnChallengeDataListener listener){
        this.boxDataListener = listener;
    }

    public ChallengeItemAdapter(Context context, int resource, List<Challenge> objects) {
        super(context, resource, objects);
        ctx = context;
        this.boxList = objects;
        copyBoxList = new ArrayList<Challenge>();
        copyBoxList.addAll(objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.item_challenge, null);
            holder = new ViewHolder();
            holder.challengeLogo = (ImageView) convertView.findViewById(R.id.challengelogo);
            holder.challengeDescription = (TextView) convertView.findViewById(R.id.challengedescription);
            holder.challengeRules = (TextView) convertView.findViewById(R.id.challengerules);
            holder.challengeGo = (ImageView) convertView.findViewById(R.id.challenge_go);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.setItemData(getItem(position));
        }

        holder.setItemData(getItem(position));

        return convertView;
    }

    class ViewHolder {
        ImageView challengeLogo;
        TextView challengeDescription;
        TextView challengeRules;
        ImageView challengeGo;

        Challenge itemData;

        public Challenge getItemData() {
            return itemData;
        }

        public void setItemData(Challenge itemData) {
            this.itemData = itemData;
        }
    }


    public void updateView(View view,int progress,int max) {
        if(view == null) {
            return;
        }
        ViewHolder holder = (ViewHolder) view.getTag();

    }

    public interface OnChallengeListener{
        void onChallengeFunc(Challenge boxItem);
    }


    public interface OnChallengeDataListener{
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