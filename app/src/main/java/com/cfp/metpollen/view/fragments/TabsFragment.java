package com.cfp.metpollen.view.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.cfp.metpollen.R;
import com.cfp.metpollen.view.activities.MainActivity;
import com.cfp.metpollen.view.adapters.MainViewPagerAdapter;
import com.cfp.metpollen.view.customViews.CustomViewPager;
import com.cfp.metpollen.view.utilities.TabUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabsFragment extends Fragment {


    private static TabsFragment INSTANCE;
    private static MainViewPagerAdapter adapter;
    private CustomViewPager mViewPager;
    private TabLayout tabLayout;
    private ProgressBar progressBar;

    public static TabsFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TabsFragment();
            Bundle args = new Bundle();
            args.putString("fragment", TabsFragment.class.getName());
            INSTANCE.setArguments(args);

        }
        return INSTANCE;
    }

    public MainViewPagerAdapter getAdapter() {
        return adapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tabs, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewPager = (CustomViewPager) view.findViewById(R.id.viewPagerTabs);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setPagingEnabled(true);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        ((MainActivity)getActivity()).setProgressBar(progressBar);

        setupTabs();
        setupAdapter();
        setupListeners();
    }

    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.home_tab_today)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.home_tab_hourly)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.home_tab_threedays)));
    }

    private void setupAdapter() {
//        setup adapters
        adapter = new MainViewPagerAdapter(getFragmentManager(), tabLayout.getTabCount(),mViewPager);
        mViewPager.setAdapter(adapter);
    }

    private void setupListeners() {

//        setup listeners

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                MainActivity.blurred = false;
                ((BaseFragment) adapter.getItem(tab.getPosition())).onTabSelected(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((BaseFragment) adapter.getItem(tab.getPosition())).onTabUnSelected(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();
                if (position == TabUtils.TAB_TODAY) {
//                    ((BaseFragment) adapter.getItem(position)).onTabSelected(position);
//                    ((MainActivity) getActivity()).getBackgroundViewOverlayToday().setBackgroundColor(getActivity().getResources().getColor(R.color.blueoverlay));

                    ((MainActivity) getActivity()).getBackgroundViewOverlayThreeDays().setVisibility(View.GONE);
                    ((MainActivity) getActivity()).getBackgroundViewOverlayHourly().setVisibility(View.GONE);
                    ((MainActivity) getActivity()).getBackgroundViewOverlayToday().setVisibility(View.VISIBLE);
                }
//                ((MainActivity) getActivity()).setBackgroundOverlayVisibility(View.VISIBLE);
//                } else {
//                    ((MainActivity) getActivity()).setBackgroundOverlayVisibility(View.GONE);
//
                Log.i("Tab Index = ", position + "");


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
