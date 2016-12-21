package com.keenbrace.activity;

import android.support.v4.app.FragmentManager;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.WindowManager;


import com.keenbrace.R;
import com.keenbrace.fragment.TT0Fragment;
import com.keenbrace.fragment.TT1Fragment;
import com.keenbrace.fragment.TT2Fragment;
import com.keenbrace.fragment.TT3Fragment;

import java.util.ArrayList;

//初次使用的引导

public class TutorialActivity extends FragmentActivity  {
    TT0Fragment tt0Fragment = new TT0Fragment();
    TT1Fragment tt1Fragment = new TT1Fragment();
    TT2Fragment tt2Fragment = new TT2Fragment();
    TT3Fragment tt3Fragment = new TT3Fragment();

   // private JazzyViewPager mJazzy;
    private ViewPager viewPager;
    public ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
    private MainAdapter mainAdapter=new MainAdapter(getSupportFragmentManager(), fragmentList);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        init();
    }

    public void init()
    {
        viewPager = (ViewPager) findViewById(R.id.vp_tutorial);
        //初始化fragmentList


        fragmentList.add(tt0Fragment);
        fragmentList.add(tt1Fragment);
        fragmentList.add(tt2Fragment);
        fragmentList.add(tt3Fragment);

        viewPager.setBackgroundColor(Color.GRAY);
        viewPager.setAdapter(mainAdapter);
        viewPager.setCurrentItem(0);

    }


    public class MainAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments;
        FragmentManager fm;

        public MainAdapter(FragmentManager fm, ArrayList<Fragment> fragmentsList) {
            super(fm);
            this.fm=fm;
            this.fragments = fragmentsList;
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        //刷新Fragment
        public Object instantiateItem(ViewGroup container,int position) {
            //得到缓存的fragment
            boolean[] fragmentsUpdateFlag = {false, false, false, true};
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            //得到tag ❶
            String fragmentTag = fragment.getTag();
            if (fragmentsUpdateFlag[position % fragmentsUpdateFlag.length]) {
                //复位更新标志
                fragmentsUpdateFlag[position % fragmentsUpdateFlag.length] = false;

                //如果这个fragment需要更新
                FragmentTransaction ft = fm.beginTransaction();
                //移除旧的fragment
                ft.remove(fragment);
                //换成新的fragment
                fragment = tt0Fragment.fragment;
                //添加新fragment时必须用前面获得的tag ❶
                if (fragment != null) {
                    if (!fragment.isAdded()) {
                        ft.add(container.getId(), fragment, fragmentTag);
                        ft.commit();
                        System.out.println(".............123");
                    }
                }else {
                    return tt3Fragment;
                }
            }
            return fragment;
        }
    }
}
