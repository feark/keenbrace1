package com.keenbrace.activity;


import com.keenbrace.R;
import com.keenbrace.storage.WaringModel;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentModel extends BaseFragment implements OnClickListener {
    TextView tv_whistle, iv_message;
    ImageView iv_t, iv_model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frame_model, null);
        tv_whistle = (TextView) view.findViewById(R.id.tv_whistle);
        iv_message = (TextView) view.findViewById(R.id.iv_message);
        iv_t = (ImageView) view.findViewById(R.id.iv_t);
        iv_model = (ImageView) view.findViewById(R.id.iv_model);
        return view;
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

    }

    WaringModel crw;
    int i = 0;

    public void update(WaringModel rw) {
        if (rw == null)
            return;
        iv_t.setImageResource(R.mipmap.medal_y);
        int[] ids = rw.getBids();
        iv_message.setText(rw.getTitle());
        tv_whistle.setText(rw.getFunction());
        tv_whistle.setVisibility(View.VISIBLE);
        iv_model.setImageResource(ids[0]);
        if (ids.length > 1) {
            i = 0;
            crw = rw;
            handler.sendEmptyMessageDelayed(1, 2000);


        } else {
            handler.removeMessages(1);
        }
    }

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (i == (crw.getBids().length / 2 - 1)) {
                        iv_t.setImageResource(R.mipmap.medal_g);
                        iv_message.setText("The Right Move");
                    } else {
                        iv_t.setImageResource(R.mipmap.medal_y);
                        iv_message.setText("The Wrong Move");
                    }
                    i++;
                    if (i >= crw.getBids().length)
                        i = 0;


                    iv_model.setImageResource(crw.getBids()[i]);
                    handler.sendEmptyMessageDelayed(1, 2000);
            }
        }
    };


}
