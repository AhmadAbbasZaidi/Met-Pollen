package com.cfp.metpollen.view.fragments;

import android.support.v4.app.Fragment;

/**
 * Created by AhmedAbbas on 1/17/2018.
 */

public abstract class BaseFragment extends Fragment {

    public abstract void onTabSelected(int position);
    public abstract void onTabUnSelected(int position);
}
