package com.keenbrace.base;

import android.content.Intent;

import com.keenbrace.core.base.KeenbraceFragment;
import com.keenbrace.core.rx.RxUtils;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by  on 15/12/23.
 */
public abstract class BaseFragment extends KeenbraceFragment {

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
    protected void readyGo(Class<?> clazz) {
        Intent intent = new Intent(getActivity(), clazz);
        startActivity(intent);
    }

}
