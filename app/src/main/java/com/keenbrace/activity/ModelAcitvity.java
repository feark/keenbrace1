package com.keenbrace.activity;

import java.util.Locale;

import com.keenbrace.R;
import com.keenbrace.base.BaseActivity;
import com.keenbrace.constants.UtilConstants;
import com.keenbrace.storage.WaringModel;
import com.keenbrace.widget.GifView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ModelAcitvity extends BaseActivity implements OnClickListener,
        OnInitListener {
    TextView tv_whistle, iv_message, tv_times, tv_flag;
    ImageView iv_t, iv_flag;
    GifView iv_model;
    int times = 10;
    ImageButton open_right, open_wrong;
    int[] ids;
    private TextToSpeech tts;
    int index;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_model;
    }
    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        tv_times = (TextView) findViewById(R.id.tv_times);
        tv_whistle = (TextView) findViewById(R.id.tv_whistle);
        iv_message = (TextView) findViewById(R.id.iv_message);
        tv_flag = (TextView) findViewById(R.id.tv_flag);
        iv_t = (ImageView) findViewById(R.id.iv_t);
        iv_flag = (ImageView) findViewById(R.id.iv_flag);
        iv_model = (GifView) findViewById(R.id.iv_model);
        tv_whistle = (TextView) findViewById(R.id.tv_whistle);
        index = this.getIntent().getIntExtra("modelIndex", 0);
        int isClose = this.getIntent().getIntExtra("isClose", 0);

        int t = UtilConstants.WaringMap.get("" + index).getGrade();
        if (t == 1) {
            iv_t.setImageResource(R.mipmap.medal_g);
        } else if (t == 2) {
            iv_t.setImageResource(R.mipmap.warning_y);
        } else if (t == 3) {
            iv_t.setImageResource(R.mipmap.warning_r);
        } else if (t == 0) {
            iv_t.setImageResource(R.mipmap.medal_g);
        }
        iv_message.setText(UtilConstants.WaringMap.get(index + "").getTitle());
        update(UtilConstants.WaringMap.get("" + index));
        if (isClose == 1) {
            tv_times.setVisibility(View.VISIBLE);
            handler.sendEmptyMessage(2);
        }
        open_right = (ImageButton) findViewById(R.id.open_right);
        open_wrong = (ImageButton) findViewById(R.id.open_wrong);
        open_right.setOnClickListener(this);
        open_wrong.setOnClickListener(this);
        tts = new TextToSpeech(this, this);
        handler.sendEmptyMessageDelayed(1, 1000);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.open_right:
                iv_flag.setImageResource(R.mipmap.right_bg);
                iv_model.setMovieResource(ids[1]);
                tv_flag.setText("The right movement");
                open_right.setImageResource(R.mipmap.right_s);
                open_wrong.setImageResource(R.mipmap.wrong_n);
//                tts.speak("The right movement", TextToSpeech.QUEUE_FLUSH, null);
//                tts.speak(UtilConstants.WaringMap.get("" + index).getFunction(),
//                        TextToSpeech.QUEUE_FLUSH, null);
                break;
            case R.id.open_wrong:
                tv_flag.setText("The wrong movement");
                iv_flag.setImageResource(R.mipmap.wrong_bg);
                iv_model.setMovieResource(ids[0]);
                open_right.setImageResource(R.mipmap.right_n);
                open_wrong.setImageResource(R.mipmap.wrong_s);
//                tts.speak("The wrong movement", TextToSpeech.QUEUE_FLUSH, null);
//                tts.speak(UtilConstants.WaringMap.get("" + index).getFunction(),
//                        TextToSpeech.QUEUE_FLUSH, null);
                break;
        }

    }

    int i = 0;

    public void update(WaringModel rw) {
        if (rw == null)
            return;
        iv_flag.setImageResource(R.mipmap.right_bg);
        ids = rw.getBids();
        tv_whistle.setText(rw.getFunction());
        tv_whistle.setVisibility(View.VISIBLE);
        iv_model.setMovieResource(ids[0]);

    }

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
//                    tts.speak(UtilConstants.WaringMap.get(index + "").getTitle()
//                                    + UtilConstants.WaringMap.get("" + index).getFunction(),
//                            TextToSpeech.QUEUE_FLUSH, null);
                    break;
                case 2:

                    tv_times.setText("" + times);
                    times--;
                    if (times > 0)
                        handler.sendEmptyMessageDelayed(2, 1000);
                    else
                        finish();

                    break;
            }
        }
    };

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(ModelAcitvity.this, "not speek",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

}
