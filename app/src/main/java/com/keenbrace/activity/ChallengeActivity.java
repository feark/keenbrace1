package com.keenbrace.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.keenbrace.AppContext;
import com.keenbrace.R;
import com.keenbrace.adapter.ChallengeItemAdapter;
import com.keenbrace.base.BaseActivity;
import com.keenbrace.bean.ChallengeDBHelper;
import com.keenbrace.constants.UtilConstants;
import com.keenbrace.core.utils.PreferenceHelper;
import com.keenbrace.greendao.Challenge;
import com.keenbrace.widget.SwipeListView;

import java.util.List;

public class ChallengeActivity extends BaseActivity implements OnClickListener {

    SwipeListView challengeList;
    View view;
    List<Challenge> challengeItems;

    @Override
    public void initData() {

        if(!PreferenceHelper.readBoolean(AppContext.getInstance(),
                UtilConstants.SHARE_PREF, UtilConstants.HAS_CHALLENGE_ADD, false)) //已经添加过
        {
            //临时添加------- test 添加控制只允许插入一次
            Challenge challenge = new Challenge();
            challenge.setId(ChallengeDBHelper.getInstance(this).insertChallenge(challenge));

            //将本次的运动结果更新数据库 ken
            ChallengeDBHelper.getInstance(this).updateChallenge(challenge);
            //-------------------
        }

        PreferenceHelper.write(AppContext.getInstance(), UtilConstants.SHARE_PREF, UtilConstants.HAS_CHALLENGE_ADD, true);

        challengeItems = ChallengeDBHelper.getInstance(this).queryChallenge();

        if(challengeItems == null){
            return;
        }

        ChallengeItemAdapter adapter = new ChallengeItemAdapter(ChallengeActivity.this, R.layout.item_challenge, challengeItems);

        challengeList.setAdapter(adapter);

        challengeList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                Intent intent = new Intent();
                intent.putExtra("Challenge", challengeItems.get(arg2));
                intent.putExtra("StartFrom", UtilConstants.fromChallenge);
                intent.setClass(ChallengeActivity.this, MainMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void initView() {
        this.setActionBarTitle("Challenge");

        challengeList = (SwipeListView) findViewById(R.id.challengelist);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_challenge;
    }

    @Override
    protected boolean hasActionBar() {
        return true;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    public void onClick(View v) {

    }
}
