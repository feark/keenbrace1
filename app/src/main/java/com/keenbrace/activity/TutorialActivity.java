package com.keenbrace.activity;

import android.support.v4.app.FragmentManager;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.keenbrace.R;
import com.keenbrace.fragment.TT1Fragment;
import com.keenbrace.fragment.TT2Fragment;
import com.keenbrace.fragment.TT3Fragment;
import com.keenbrace.jazzyviewpager.JazzyViewPager;
import com.keenbrace.jazzyviewpager.OutlineContainer;

import java.util.ArrayList;
import java.util.List;

//初次使用的引导

public class TutorialActivity extends FragmentActivity {

   // private JazzyViewPager mJazzy;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        init();

    }

    public void init()
    {
        viewPager = (ViewPager) findViewById(R.id.vp_tutorial);

        LayoutInflater mInflater = getLayoutInflater();
        View activityView = mInflater.inflate(R.layout.fragment_tt1, null);

        TT1Fragment tt1Fragment = new TT1Fragment();
        TT2Fragment tt2Fragment = new TT2Fragment();
        TT3Fragment tt3Fragment = new TT3Fragment();

        fragmentList.add(tt1Fragment);
        fragmentList.add(tt2Fragment);
        fragmentList.add(tt3Fragment);

        viewPager.setBackgroundColor(Color.GRAY);
        viewPager.setAdapter(new MainAdapter(getSupportFragmentManager(), fragmentList));
        viewPager.setCurrentItem(0);

    }

    private class MainAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragmentsList;

        public MainAdapter(FragmentManager fm) {
            super(fm);
        }

        public MainAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
            super(fm);
            this.fragmentsList = fragments;
        }


        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }
    }
}
