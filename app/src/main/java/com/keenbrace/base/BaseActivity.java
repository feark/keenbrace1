package com.keenbrace.base;

import android.content.Intent;

import com.keenbrace.core.base.KeenBraceActivity;
import com.keenbrace.core.rx.RxUtils;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by  on 15/12/23.
 */
public abstract class BaseActivity extends KeenBraceActivity {

    public CompositeSubscription _subscriptions = new CompositeSubscription();

    @Override
    public void onResume() {
        super.onResume();
        _subscriptions = RxUtils.getNewCompositeSubIfUnsubscribed(_subscriptions);
    }

    @Override
    public void onPause() {
        super.onPause();

        RxUtils.unsubscribeIfNotNull(_subscriptions);
    }
    /**
     * startActivity then finish
     *
     * @param clazz
     */
    protected void readyGoThenKill(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        finish();
    }

}
