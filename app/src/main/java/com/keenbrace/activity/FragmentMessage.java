package com.keenbrace.activity;

import com.keenbrace.R;
import com.keenbrace.adapter.WarnsListAdapter;
import com.keenbrace.greendao.RunWaring;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class FragmentMessage extends BaseFragment implements OnClickListener {
    public static String SHOW_MODEL = "show_model";
    ListView lv_data;
    WarnsListAdapter adapter;
    private SoundPool soundMsg;
    private int msgsourceid = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frame_message, null);
        lv_data = (ListView) view.findViewById(R.id.lv_data);
        adapter = new WarnsListAdapter(this.getActivity());
        lv_data.setAdapter(adapter);
        lv_data.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                adapter.setSelectId(arg2);
                adapter.notifyDataSetChanged();
            }
        });
        lv_data.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                adapter.setSelectId(arg2);
                Intent intent = new Intent();
                intent.setAction(SHOW_MODEL);
                intent.putExtra("selectId", adapter.getDevice(arg2).getIndex()
                        + "");
                FragmentMessage.this.getActivity().sendBroadcast(intent);
                return false;
            }

        });
        soundMsg = new SoundPool(10, AudioManager.STREAM_RING, 0);
        msgsourceid = soundMsg.load(this.getActivity(), R.raw.smsreceived1, 0);
        //initMessage();
        return view;
    }

    public void initMessage() {
        for (int i = 0; i < 11; i++) {
            RunWaring rw = new RunWaring();
            rw.setIndex(i+"");

            rw.setLatitude(0.0);
            rw.setLongitude(0.0);
            rw.setCreateTime(System.currentTimeMillis() + i * 1000 * 60);
            adapter.addRunWaring(rw);
        }
        adapter.notifyDataSetChanged();
    }

    public ListView getMessageListView() {
        return lv_data;
    }

    public void addRunWaring(RunWaring rw) {
        adapter.addRunWaring(rw);
        adapter.notifyDataSetChanged();
        soundMsg.stop(msgsourceid);
        soundMsg.play(msgsourceid, 1, 1, 0, 0, 1);
    }

    public int getRwCount() {
        return adapter.getCount();
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

    }

}
