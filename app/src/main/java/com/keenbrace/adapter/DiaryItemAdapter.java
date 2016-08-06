package com.keenbrace.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.ArrayAdapter;

import com.keenbrace.greendao.CommonResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  on 15/12/28.
 */
public class DiaryItemAdapter extends ArrayAdapter<CommonResult> {

    public DiaryItemAdapter(Context context, int resource, List<CommonResult> objects) {
        super(context, resource, objects);

    }
}
