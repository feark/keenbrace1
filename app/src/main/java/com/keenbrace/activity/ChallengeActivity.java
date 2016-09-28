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

public class ChallengeActivity extends BaseActivity implements OnClickListener, ChallengeItemAdapter.OnChallengeDataListener {

    SwipeListView challengeList;
    View view;
    List<Challenge> challengeItems;
    ChallengeItemAdapter adapter;

    @Override
    public void initData() {

        if(!PreferenceHelper.readBoolean(AppContext.getInstance(),
                UtilConstants.SHARE_PREF, UtilConstants.HAS_CHALLENGE_ADD, false)) //已经添加过
        {
            //临时添加------- test 添加控制只允许插入一次
            Challenge kbChallenge = new Challenge();
            kbChallenge.setId(ChallengeDBHelper.getInstance(this).insertChallenge(kbChallenge));

            //将KeenBrace Challenge的内容在这里加入
            kbChallenge.setTitle("KeenBrace Challenge");
            kbChallenge.setChallengeLogo(R.mipmap.kb_challenge);
            kbChallenge.setDescription((String) getText(R.string.tx_kbchallenge_description));
            kbChallenge.setRules((String) getText(R.string.tx_kbchallenge_rules));

            //将本次的运动结果更新数据库 ken
            ChallengeDBHelper.getInstance(this).updateChallenge(kbChallenge);

            //-------------------------

            Challenge fartlekChallenge = new Challenge();
            fartlekChallenge.setId(ChallengeDBHelper.getInstance(this).insertChallenge(fartlekChallenge));

            //将KeenBrace Challenge的内容在这里加入
            fartlekChallenge.setTitle("Fartlek");
            fartlekChallenge.setChallengeLogo(R.mipmap.fartlek);
            fartlekChallenge.setDescription((String) getText(R.string.tx_fartlek_description));
            fartlekChallenge.setRules((String) getText(R.string.tx_fartlek_rules));

            //将本次的运动结果更新数据库 ken
            ChallengeDBHelper.getInstance(this).updateChallenge(fartlekChallenge);
            //-------------------
        }

        PreferenceHelper.write(AppContext.getInstance(), UtilConstants.SHARE_PREF, UtilConstants.HAS_CHALLENGE_ADD, true);

        challengeItems = ChallengeDBHelper.getInstance(this).queryChallenge();

        if(challengeItems == null){

            return;
        }

        com.umeng.socialize.utils.Log.e("*************" + challengeItems.get(0).getDescription()
                +"*************"+ challengeItems.size());

        adapter = new ChallengeItemAdapter(ChallengeActivity.this, R.layout.item_challenge, challengeItems);
        //adapter.setOnChallengeItemListener(this);
        challengeList.setAdapter(adapter);

        challengeList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                Intent intent = new Intent();
                intent.putExtra("Challenge", challengeItems.get(arg2)); //点击那一项的数据项内容会传到下一个activity
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
    public void onBoxDataUpdate(int amont, int current) {

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
