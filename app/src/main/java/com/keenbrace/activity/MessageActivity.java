package com.keenbrace.activity;

import java.util.ArrayList;
import java.util.List;

import com.keenbrace.R;
import com.keenbrace.adapter.WarnsListAdapter;
import com.keenbrace.base.BaseActivity;
import com.keenbrace.bean.KeenbraceDBHelper;
import com.keenbrace.greendao.KeenBrace;
import com.keenbrace.greendao.RunWaring;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MessageActivity extends BaseActivity implements OnClickListener {
    public static String SHOW_MODEL = "show_model";
    ListView lv_data;
    WarnsListAdapter adapter;
    private SoundPool soundMsg;
    private int msgsourceid = 0;
    List<RunWaring> rws;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    public void initView() {
        lv_data = (ListView) findViewById(R.id.lv_data);
        adapter = new WarnsListAdapter(this);
        lv_data.setAdapter(adapter);
        long id = this.getIntent().getLongExtra("id", 0);

        rws = KeenbraceDBHelper.getInstance(this).queryRunWaringByRunId(id);
        adapter.addRunWarings(rws);
        lv_data.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                Intent intent = new Intent();
                intent.putExtra("modelIndex", rws.get(arg2).getIndex());
                intent.setClass(MessageActivity.this, ModelAcitvity.class);
                startActivity(intent);
            }
        });

        soundMsg = new SoundPool(10, AudioManager.STREAM_RING, 0);
        msgsourceid = soundMsg.load(this, R.raw.smsreceived1, 0);
    }

    @Override
    public void initData() {

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
