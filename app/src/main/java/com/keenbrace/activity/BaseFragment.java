package com.keenbrace.activity;

import android.support.v4.app.Fragment;

import rx.subscriptions.CompositeSubscription;

public  class  BaseFragment extends Fragment {
    public CompositeSubscription _subscriptions = new CompositeSubscription();

}
