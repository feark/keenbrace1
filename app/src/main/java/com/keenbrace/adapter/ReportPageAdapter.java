package com.keenbrace.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by  on 15/12/28.
 */
public class ReportPageAdapter extends FragmentPagerAdapter{

    private List<Fragment> listFrg;

    public ReportPageAdapter(FragmentManager fm,List<Fragment> listFrg) {
        super(fm);
        this.listFrg = listFrg;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

    @Override
    public int getCount() {
        return  listFrg.size();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frg = listFrg.get(position);
        return frg;
    }
}
