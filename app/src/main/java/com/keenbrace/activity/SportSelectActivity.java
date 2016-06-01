package com.keenbrace.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.keenbrace.R;

import butterknife.OnClick;

public class SportSelectActivity extends Activity {
    ImageView iv_run;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_select);

        iv_run = (ImageView) findViewById(R.id.iv_run);

        iv_run.setOnClickListener(start_run);


    }


    OnClickListener start_run = new OnClickListener() {

        @Override
        public void onClick(View v) {
            iv_run.setImageResource(R.mipmap.main_run_y);
            Intent intent = new Intent();
            intent.setClass(SportSelectActivity.this, MainMenuActivity.class);
            startActivity(intent);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                showTips();

                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showTips() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(getText(R.string.remind))
                .setMessage(getText(R.string.sureexit))
                .setPositiveButton(getText(R.string.sure),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                android.os.Process
                                        .killProcess(android.os.Process.myPid());
                            }
                        })
                .setNegativeButton(getText(R.string.cancle),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                return;
                            }
                        }).create();
        alertDialog.show();
    }
}
