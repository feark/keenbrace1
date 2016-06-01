package com.keenbrace.core.base;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.keenbrace.core.R;
import com.keenbrace.core.dialog.DialogControl;
import com.keenbrace.core.dialog.WaitDialog;

import butterknife.ButterKnife;


public abstract class KeenbraceFragment extends Fragment implements
        View.OnClickListener{

	protected View view_parent;

    public abstract int getLayoutId();
    public abstract void initView();
    public abstract void initData();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view_parent = LayoutInflater.from(getActivity()).inflate(getLayoutId(), null);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		if(view_parent.getParent()!=null){
			((ViewGroup)view_parent.getParent()).removeView(view_parent);
		}
        ButterKnife.bind(this, view_parent);
        initView();
        initData();
		return view_parent;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    protected void hideWaitDialog() {
        FragmentActivity activity = getActivity();
        if (activity instanceof DialogControl) {
            ((DialogControl) activity).hideWaitDialog();
        }
    }

    protected WaitDialog showWaitDialog() {
        return showWaitDialog(R.string.loading);
    }

    protected WaitDialog showWaitDialog(int resid) {
        FragmentActivity activity = getActivity();
        if (activity instanceof DialogControl) {
            return ((DialogControl) activity).showWaitDialog(resid);
        }
        return null;
    }

    protected WaitDialog showWaitDialog(String resid) {
        FragmentActivity activity = getActivity();
        if (activity instanceof DialogControl) {
            return ((DialogControl) activity).showWaitDialog(resid);
        }
        return null;
    }

    @Override
    public void onClick(View v) {
    }
}
