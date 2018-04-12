package com.cfp.metpollen.view.objectModels;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.List;

/**
 * Created by AhmedAbbas on 11/13/2017.
 */

public class RecyclerViewObject {
    private Context context;
    private int resourceId;
    private List<Point> listCanvas;
    private List<Point> listAdapter;
    private List<Point> listOriginalTemp;
    private View ballView;
    private int itemWidth, itemHeight;
    private int visibleItemCount;
    private int minTemp, maxTemp;
    public RecyclerViewObject(Context context, int resourceId, List<Point> listCanvas, List<Point> listAdapter, View ballView) {
        this.context = context;
        this.resourceId = resourceId;
        this.listCanvas = listCanvas;
        this.listAdapter = listAdapter;
        this.ballView = ballView;
        this.itemWidth = 1;
        this.itemHeight = 1;
        this.visibleItemCount = 0;
    }

    public List<Point> getListOriginalTemp() {
        return listOriginalTemp;
    }

    public void setListOriginalTemp(List<Point> listOriginalTemp) {
        this.listOriginalTemp = listOriginalTemp;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(int minTemp) {
        this.minTemp = minTemp;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(int maxTemp) {
        this.maxTemp = maxTemp;
    }

    public List<Point> getListAdapter() {
        return listAdapter;
    }

    public void setListAdapter(List<Point> listAdapter) {
        this.listAdapter = listAdapter;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public List<Point> getListCanvas() {
        return listCanvas;
    }

    public void setListCanvas(List<Point> listCanvas) {
        this.listCanvas = listCanvas;
    }

    public View getBallView() {
        return ballView;
    }

    public void setBallView(View ballView) {
        this.ballView = ballView;
    }

    public int getItemWidth() {
        return itemWidth;
    }

    public void setItemWidth(int itemWidth) {
        this.itemWidth = itemWidth;
    }

    public int getItemHeight() {
        return itemHeight;
    }

    public void setItemHeight(int itemHeight) {
        this.itemHeight = itemHeight;
    }

    public int getVisibleItemCount() {
        return visibleItemCount;
    }

    public void setVisibleItemCount(int visibleItemCount) {
        if (this.visibleItemCount == 0) {
            this.visibleItemCount = visibleItemCount;

            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

            this.setItemWidth(displayMetrics.widthPixels / this.visibleItemCount);
//            this.setItemHeight(displayMetrics.heightPixels=this.visibleItemCount);

        }
    }
}
