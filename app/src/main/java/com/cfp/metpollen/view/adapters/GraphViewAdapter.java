package com.cfp.metpollen.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cfp.metpollen.R;
import com.cfp.metpollen.data.db.model.ForecastModel;
import com.cfp.metpollen.view.interfaces.GraphViewListener;
import com.cfp.metpollen.view.objectModels.WeatherCondition;
import com.cfp.metpollen.view.utilities.SunRiseSETUtils;
import com.cfp.metpollen.view.utilities.TempConditionUtils;
import com.cfp.metpollen.view.utilities.utils;

import java.util.List;

/**
 * Created by AhmedAbbas on 11/8/2017.
 */

public class GraphViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public int width = 0, height;
    public int visibleItemCount = 0;
    List<ForecastModel> list;
    Context context;
    private int resourceId;
    private GraphViewListener graphViewListener;

    public GraphViewAdapter(List<ForecastModel> list, Context context, int resourceId, GraphViewListener graphViewListener) {
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
            view = LayoutInflater.from(context).inflate(R.layout.recycler_list_item2, parent, false);
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
            Log.i("Position1 = ",position+"");
            MyClass viewHolder = (MyClass) holder;
            viewHolder.ll.setVisibility(View.INVISIBLE);
            DisplayMetrics displayMetrics =  context.getResources().getDisplayMetrics();
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
            Log.i("Position2 = ",position+"");
            ForecastModel forecastModel = list.get(position-visibleItemCount / 2);
            MyClass viewHolder = (MyClass) holder;
            viewHolder.ll.setVisibility(View.VISIBLE);
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            holder.itemView.measure(0, 0);
            int w = holder.itemView.getMeasuredWidth();
            height = holder.itemView.getMeasuredHeight();

            if(position == (visibleItemCount / 2))
            {
                viewHolder.lineLeft.setVisibility(View.GONE);
                viewHolder.lineRight.setVisibility(View.GONE);
            }
            else if(position == (list.size() - 1 - visibleItemCount / 2))
            {
                viewHolder.lineLeft.setVisibility(View.GONE);
                viewHolder.lineRight.setVisibility(View.GONE);
            }
            else
            {
                viewHolder.lineLeft.setVisibility(View.GONE);
                viewHolder.lineRight.setVisibility(View.GONE);
            }
            if (width > 0) {
                ViewGroup.LayoutParams params = viewHolder.ll.getLayoutParams(); // (width, height)
                params.width = width;
                holder.itemView.setLayoutParams(params);
//                graphViewListener.OnItemWidthHeightListener(w, height);
            }
            if(graphViewListener!=null)
            {
                graphViewListener.OnItemWidthHeightListener(w, height);
            }


//            Log.i("itemwidth ", " = " + width);
//            Log.i("itemHeight ", " = " + height);

/*
            if (forecastModellist.get(position).x == 5) {
                viewHolder.tv_risefall.setText("05:20");
                viewHolder.rl.setVisibility(View.VISIBLE);
            } else if (list.get(position).x == 18) {
                viewHolder.tv_risefall.setText("06:15");
                viewHolder.rl.setVisibility(View.VISIBLE);
            } else {

            viewHolder.rl.setVisibility(View.GONE);

            }*/
            if(/*Integer.parseInt(utils.getConvertedDateFromOneFormatToOther(forecastModel.getFormattedWeatherTime(),utils.FORMAT1,utils.FORMAT7))==Integer.parseInt(forecastModel.getSunRiseHour())||*/Integer.parseInt(utils.getConvertedDateFromOneFormatToOther(forecastModel.getFormattedWeatherTime(),utils.FORMAT1,utils.FORMAT7))==Integer.parseInt(forecastModel.getSunRiseHour())+1)
            {
                viewHolder.rl.setVisibility(View.GONE);
                viewHolder.rlleft.setVisibility(View.VISIBLE);
                viewHolder.imgleft.setImageResource(R.drawable.sunrise);
                viewHolder.tv_risefall_left.setText(utils.getConvertedDateFromOneFormatToOther(forecastModel.getSunRiseHour()+":"+forecastModel.getSunRiseMin(),utils.FORMAT6,utils.FORMAT5));
            }
            else if(Integer.parseInt(utils.getConvertedDateFromOneFormatToOther(forecastModel.getFormattedWeatherTime(),utils.FORMAT1,utils.FORMAT7))==Integer.parseInt(forecastModel.getSunRiseHour())||Integer.parseInt(utils.getConvertedDateFromOneFormatToOther(forecastModel.getFormattedWeatherTime(),utils.FORMAT1,utils.FORMAT7))==Integer.parseInt(forecastModel.getSunRiseHour())-1)
            {
                viewHolder.rl.setVisibility(View.VISIBLE);
                viewHolder.rlleft.setVisibility(View.GONE);
                viewHolder.img.setImageResource(R.drawable.sunrise);
                viewHolder.tv_risefall.setText(utils.getConvertedDateFromOneFormatToOther(forecastModel.getSunRiseHour()+":"+forecastModel.getSunRiseMin(),utils.FORMAT6,utils.FORMAT5));
            }
            else if(/*Integer.parseInt(utils.getConvertedDateFromOneFormatToOther(forecastModel.getFormattedWeatherTime(),utils.FORMAT1,utils.FORMAT7))==Integer.parseInt(forecastModel.getSunRiseHour())||*/Integer.parseInt(utils.getConvertedDateFromOneFormatToOther(forecastModel.getFormattedWeatherTime(),utils.FORMAT1,utils.FORMAT7))==Integer.parseInt(forecastModel.getSunSetHour())+1)
            {
                viewHolder.rl.setVisibility(View.GONE);
                viewHolder.rlleft.setVisibility(View.VISIBLE);
                viewHolder.imgleft.setImageResource(R.drawable.sunset);
                viewHolder.tv_risefall_left.setText(utils.getConvertedDateFromOneFormatToOther(forecastModel.getSunSetHour()+":"+forecastModel.getSunSetMin(),utils.FORMAT6,utils.FORMAT5));
            }
            else if(Integer.parseInt(utils.getConvertedDateFromOneFormatToOther(forecastModel.getFormattedWeatherTime(),utils.FORMAT1,utils.FORMAT7))==Integer.parseInt(forecastModel.getSunSetHour())||Integer.parseInt(utils.getConvertedDateFromOneFormatToOther(forecastModel.getFormattedWeatherTime(),utils.FORMAT1,utils.FORMAT7))==Integer.parseInt(forecastModel.getSunSetHour())-1)
            {
                viewHolder.rl.setVisibility(View.VISIBLE);
                viewHolder.rlleft.setVisibility(View.GONE);
                viewHolder.img.setImageResource(R.drawable.sunset);
                viewHolder.tv_risefall.setText(utils.getConvertedDateFromOneFormatToOther(forecastModel.getSunSetHour()+":"+forecastModel.getSunSetMin(),utils.FORMAT6,utils.FORMAT5));
            }
            else
            {
                viewHolder.rl.setVisibility(View.GONE);
                viewHolder.rlleft.setVisibility(View.GONE);
            }

            viewHolder.tv.setText(utils.getConvertedDateFromOneFormatToOther(forecastModel.getFormattedWeatherTime(),utils.FORMAT1,utils.FORMAT5));
            WeatherCondition weatherCondition = TempConditionUtils.getConditionForecast(forecastModel.getPrecipitation(), forecastModel.getCloud(), SunRiseSETUtils.isDayForecast(forecastModel.getFormattedHour(),forecastModel.getSunRiseHour(),forecastModel.getSunRiseMin(),forecastModel.getSunSetHour(),forecastModel.getSunSetMin()));
            viewHolder.weatherCondition.setImageResource(weatherCondition.getResourceId());
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size()+visibleItemCount/2+visibleItemCount/2;
    }

    public class MyClass extends RecyclerView.ViewHolder {
        TextView tv, tv_risefall, tv_risefall_left;
        LinearLayout ll;
        RelativeLayout rl,rlleft;
        View lineLeft;
        View lineRight;
        ImageView imgleft,img,weatherCondition;

        public MyClass(View itemView) {
            super(itemView);

            ll = (LinearLayout) itemView.findViewById(R.id.ll);
            rl = (RelativeLayout) itemView.findViewById(R.id.rl);
            rlleft = (RelativeLayout) itemView.findViewById(R.id.rlleft);
            weatherCondition = (ImageView) itemView.findViewById(R.id.cloud);
            img = (ImageView) itemView.findViewById(R.id.img);
            imgleft = (ImageView) itemView.findViewById(R.id.imgleft);
            tv = (TextView) itemView.findViewById(R.id.tv);
            tv_risefall = (TextView) itemView.findViewById(R.id.tv_risefall);
            tv_risefall_left = (TextView) itemView.findViewById(R.id.tv_risefall_left);
            lineLeft = itemView.findViewById(R.id.lineLeft);
            lineRight = itemView.findViewById(R.id.lineRight);

        }
    }


}
