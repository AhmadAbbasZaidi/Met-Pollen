package com.cfp.metpollen.view.adapters;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cfp.metpollen.R;
import com.cfp.metpollen.data.db.model.ForecastModel;
import com.cfp.metpollen.view.interfaces.GraphViewListener;
import com.cfp.metpollen.view.utilities.utils;

import java.util.List;

/**
 * Created by AhmedAbbas on 11/8/2017.
 */

public class SineGraphViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public int width = 0, height;
    public int visibleItemCount = 0;
    List<ForecastModel> list;
    Context context;
    private int resourceId;
    private GraphViewListener graphViewListener;

    public SineGraphViewAdapter(List<ForecastModel> list, Context context, int resourceId, GraphViewListener graphViewListener) {
        this.list = list;
        this.resourceId = resourceId;
        this.context = context;
        this.graphViewListener = graphViewListener;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setList(List<ForecastModel> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    public void setVisibleItemCount(int visibleItemCount) {
        this.visibleItemCount = visibleItemCount;
//        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(context).inflate(R.layout.recycler_list_item_sine_wave, parent, false);
            view.findViewById(R.id.ll).setMinimumWidth(context.getResources().getDisplayMetrics().widthPixels/2);
        } else {
            view = LayoutInflater.from(context).inflate(resourceId, parent, false);
            view.findViewById(R.id.ll).setMinimumWidth(context.getResources().getDisplayMetrics().widthPixels/2);
        }
        return new MyClass(view);
    }

    @Override
    public int getItemViewType(int position) {
        return /*position==0?0:*/1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if (position < (visibleItemCount / 2) || position > (list.size() - 1 + visibleItemCount / 2)) {
            MyClass viewHolder = (MyClass) holder;
            viewHolder.ll.setVisibility(View.INVISIBLE);
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            holder.itemView.measure(0, 0);
            int w = holder.itemView.getMeasuredWidth();
            height = holder.itemView.getMeasuredHeight();

            if (width > 0) {
                ViewGroup.LayoutParams params = viewHolder.ll.getLayoutParams(); // (width, height)
                params.width = width;
                holder.itemView.setLayoutParams(params);
//                graphViewListener.OnItemWidthHeightListener(w, height);
            }
        } else {
            ForecastModel forecastModel = list.get(position-visibleItemCount/2);
            MyClass viewHolder = (MyClass) holder;
            viewHolder.ll.setVisibility(View.VISIBLE);
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            holder.itemView.measure(0, 0);
            int w = holder.itemView.getMeasuredWidth();
            height = holder.itemView.getMeasuredHeight();

            if (width > 0) {
                ViewGroup.LayoutParams params = viewHolder.ll.getLayoutParams(); // (width, height)
                params.width = width;
                holder.itemView.setLayoutParams(params);
//                graphViewListener.OnItemWidthHeightListener(w, height);
            }
            graphViewListener.OnItemWidthHeightListener(w*2, height);


//            Log.i("itemwidth ", " = " + width);
//            Log.i("itemHeight ", " = " + height);
            if ((position-visibleItemCount/2) % 2 == 0) {
                String day1 = "Mon";
                String day2 = "Tue";
                String day3 = "Wed";
                String day4 = "Thu";
                String day5 = "Fri";
                String day6 = "Sat";
                String day7 = "Sun";
//                viewHolder.tv.setText(position / 2 == 1 ? day1 : position / 2 == 2 ? day2 : day3);
                viewHolder.tv.setText(utils.getConvertedDateFromOneFormatToOther(forecastModel.getFormattedWeatherTime(),utils.FORMAT1,utils.FORMAT8));
                viewHolder.tv.setVisibility(View.VISIBLE);
                viewHolder.cloud.setImageResource(R.drawable.sun);
            }
            else
            {
                viewHolder.tv.setVisibility(View.INVISIBLE);
                viewHolder.cloud.setImageResource(R.drawable.moon);
            }

        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size()+visibleItemCount / 2+visibleItemCount / 2;
    }

    public class MyClass extends RecyclerView.ViewHolder {
        TextView tv;
        LinearLayout ll;
        ImageView cloud;

        public MyClass(View itemView) {
            super(itemView);

            ll = (LinearLayout) itemView.findViewById(R.id.ll);
            tv = (TextView) itemView.findViewById(R.id.tv);
            cloud = (ImageView) itemView.findViewById(R.id.cloud);
        }
    }


}
