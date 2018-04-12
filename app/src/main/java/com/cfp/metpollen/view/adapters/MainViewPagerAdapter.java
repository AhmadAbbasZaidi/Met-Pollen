package com.cfp.metpollen.view.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.cfp.metpollen.view.customViews.CustomViewPager;
import com.cfp.metpollen.view.fragments.HourlyFragment;
import com.cfp.metpollen.view.fragments.ThreeDaysFragment;
import com.cfp.metpollen.view.fragments.TodayFragmentWithoutList;

/**
 * Created by AhmedAbbas on 11/16/2017.
 */

public class MainViewPagerAdapter extends FragmentStatePagerAdapter {
    int count;

    CustomViewPager customViewPager;

    public MainViewPagerAdapter(FragmentManager fm, int count, CustomViewPager mViewPager) {
        super(fm);
        this.count = count;
        this.customViewPager = mViewPager;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return TodayFragmentWithoutList.getInstance();
            case 1:
                return HourlyFragment.getInstance(customViewPager);
            case 2:
                return ThreeDaysFragment.getInstance(customViewPager);
        }
        return null;
    }

    @Override
    public int getCount() {
        return count;
    }
}
