package com.cfp.metpollen.view.abstracts;

import android.view.View;
import android.widget.AbsListView;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Created by AhmedAbbas on 12/14/2017.
 */

public abstract class OnScrollPositionChangedListener implements AbsListView.OnScrollListener {
    private Dictionary<Integer, Integer> mListViewItemHeights = new Hashtable<Integer, Integer>();
    private int pos;

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


        try {
            pos = getScrollY(view);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            onScrollPositionChanged(view, firstVisibleItem, visibleItemCount,totalItemCount,pos);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {
    }

    public abstract void onScrollPositionChanged(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int scrollYPosition);

    private int getScrollY(AbsListView view) {
        View child = view.getChildAt(0); //this is the first visible row
        if (child == null) return 0;

        int scrollY = -child.getTop();

        mListViewItemHeights.put(view.getFirstVisiblePosition(), child.getHeight());

        for (int i = 0; i < view.getFirstVisiblePosition(); ++i) {
            Integer hei = mListViewItemHeights.get(i);

            //Manual add hei each row into scrollY
            if (hei != null)
                scrollY += hei;
        }

        return scrollY;
    }
}