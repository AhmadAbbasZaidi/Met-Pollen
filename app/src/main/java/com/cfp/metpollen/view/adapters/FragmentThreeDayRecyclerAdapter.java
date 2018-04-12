package com.cfp.metpollen.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cfp.metpollen.R;
import com.cfp.metpollen.data.db.model.ForecastModel;
import com.cfp.metpollen.view.customViews.ArcView;
import com.cfp.metpollen.view.customViews.CircleProgressBar;
import com.cfp.metpollen.view.objectModels.WeatherCondition;
import com.cfp.metpollen.view.utilities.SunRiseSETUtils;
import com.cfp.metpollen.view.utilities.TempConditionUtils;
import com.cfp.metpollen.view.utilities.utils;

import java.util.ArrayList;
import java.util.List;

import static java.sql.Types.NULL;


/**
 * Created by AhmedAbbas on 11/17/2017.
 */

public class FragmentThreeDayRecyclerAdapter extends RecyclerView.Adapter<FragmentThreeDayRecyclerAdapter.HolderItemOne> {
    private final int resource;
    Context context;

    private List<ForecastModel> list;
    private List<ForecastModel> listRefined;

    public FragmentThreeDayRecyclerAdapter(Context context, int resource, List<ForecastModel> list) {
        this.context = context;
        this.resource = resource;
        this.list = new ArrayList<>();
        this.listRefined = new ArrayList<>();
        for (ForecastModel forecastModel : list) {
            if (forecastModel.getFormattedHour().equalsIgnoreCase("09")) {
                this.list.add(forecastModel);
                this.listRefined.add(forecastModel);
            }
        }
//        this.list = list;
//        this.listRefined = list;
/*

        int currentHour = utils.getHourOfDay();
        int reqHour = currentHour - currentHour % 3;
        listRefined = new ArrayList<>();
        for (ForecastModel model : list) {
            if (Integer.parseInt(model.getFormattedHour()) == reqHour) {
                listRefined.add(model);
                Log.i("Model Hour = ", model.getFormattedHour() + "");
            }
        }


        Log.i("List Size = ", listRefined.size() + "");
        Log.i("REQ Hour = ", reqHour + "");
*/

    }


    @Override
    public FragmentThreeDayRecyclerAdapter.HolderItemOne onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(resource, parent, false);
        return new HolderItemOne(view);
    }

    @Override
    public void onBindViewHolder(FragmentThreeDayRecyclerAdapter.HolderItemOne holder, int position) {
//        ArcAngleAnimation animation = new ArcAngleAnimation(holder.arcView, 150);
//        animation.setDuration(3000);
//        holder.arcView.startAnimation(animation);

        ForecastModel item = list.get(position);
        holder.arcView.setDate(utils.getConvertedDateFromOneFormatToOther(item.getFormattedWeatherTime(), utils.FORMAT1, utils.FORMAT3));
        holder.arcView.setRiseTime(item.getSunRiseHour() + ":" + item.getSunRiseMin());
        holder.arcView.setSetTime(item.getSunSetHour() + ":" + item.getSunSetMin());
        if( !item.getSunRiseHour().equalsIgnoreCase("--")&&!item.getSunRiseMin().equalsIgnoreCase("--")&&!item.getSunSetHour().equalsIgnoreCase("--")&&!item.getSunSetMin().equalsIgnoreCase("--"))
        holder.arcView.setArcAngle(SunRiseSETUtils.getArcAngle(utils.getConvertedDateFromOneFormatToOther(item.getFormattedWeatherTime(), utils.FORMAT1, utils.FORMAT3), item.getSunRiseHour(), item.getSunRiseMin(), item.getSunSetHour(), item.getSunSetMin()));
//        String tmp = item.getMaxTemperature() < 10 ? "0" + (int) item.getMaxTemperature() +"°": (int) item.getMaxTemperature() +"°";
//        tmp = item.getMinTemperature() < 10 ? tmp+" / "+0 + (int) item.getMinTemperature() +"°": tmp+" / "+(int) item.getMinTemperature() + "°";
//        holder.temp.setText(tmp);
        String tmpMax = item.getMaxTemperature().equalsIgnoreCase("--") ? "--" : (int)Float.parseFloat(item.getMaxTemperature()) < 10 ? "0" + (int)Float.parseFloat(item.getMaxTemperature()) + "°" : (int)Float.parseFloat(item.getMaxTemperature()) + "°";
        String tmpMin = item.getMinTemperature().equalsIgnoreCase("--") ? "--" : (int)Float.parseFloat(item.getMinTemperature()) < 10 ? "0" + (int)Float.parseFloat(item.getMinTemperature()) + "°" : (int)Float.parseFloat(item.getMinTemperature()) + "°";
        holder.tempMax.setText(tmpMax);
        holder.tempMin.setText(tmpMin);
        holder.date.setText(utils.getHeaderDate(item.getFormattedWeatherTime()));
        WeatherCondition weatherCondition = TempConditionUtils.getConditionForecast(item.getPrecipitation(), item.getCloud(), SunRiseSETUtils.isDayForecast(item.getFormattedHour(), item.getSunRiseHour(), item.getSunRiseMin(), item.getSunSetHour(), item.getSunSetMin()));
        if (weatherCondition != null) {
            holder.conditionName.setText(weatherCondition.getCondition());
            holder.conditionImage.setImageResource(weatherCondition.getResourceId());
        }
        else
        {
            holder.conditionName.setText("--");
            holder.conditionImage.setImageResource(NULL);
        }
        String perc = item.getPrecipitation().equalsIgnoreCase("--") ? "--" : (int)Float.parseFloat(item.getPrecipitation()) < 10 ? "0" + (int)Float.parseFloat(item.getPrecipitation()) + "°" : (int)Float.parseFloat(item.getPrecipitation()) + "°";
        holder.chanceOfRain.setText(perc + " mm");
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setForecastModelsList(List<ForecastModel> forecastModelsList) {

        list.clear();
        listRefined.clear();
        for (ForecastModel forecastModel : forecastModelsList) {
            if (forecastModel.getFormattedHour().equalsIgnoreCase("09")) {
                this.list.add(forecastModel);
                this.listRefined.add(forecastModel);
            }
        }

//        this.list = forecastModelsList;
//        this.listRefined = forecastModelsList;
        Log.i("sinewave 4 ", "forecast list size = " + forecastModelsList.size());


/*
        int currentHour = utils.getHourOfDay();

        int reqHour = currentHour - currentHour % 3;
        if (listRefined == null) {
            listRefined = new ArrayList<>();
        } else {
            listRefined.clear();
        }
        for (ForecastModel model : list) {
            if (Integer.parseInt(model.getFormattedHour()) == reqHour) {
                listRefined.add(model);
                Log.i("Model Hour = ", model.getFormattedHour() + "");
            }
        }

        Log.i("List Size = ", listRefined.size() + "");
        Log.i("REQ Hour = ", reqHour + "");
*/

        notifyDataSetChanged();
    }


    class HolderItemOne extends RecyclerView.ViewHolder {
        CircleProgressBar circleProgressBar;
        TextView progressText, date, temp, tempMin, tempMax, conditionName, chanceOfRain;
        ImageView conditionImage;
        int poition;
        ArcView arcView;

        public HolderItemOne(View itemView) {
            super(itemView);
//            circleProgressBar = (CircleProgressBar) itemView.findViewById(R.id.circleProgressBar);
//            progressText = (TextView) itemView.findViewById(R.id.progressText);
            arcView = (ArcView) itemView.findViewById(R.id.arcView);
            chanceOfRain = (TextView) itemView.findViewById(R.id.chanceOfRain);
            date = (TextView) itemView.findViewById(R.id.tv_date);
//            temp = (TextView) itemView.findViewById(R.id.temp);
            tempMax = (TextView) itemView.findViewById(R.id.tempMax);
            tempMin = (TextView) itemView.findViewById(R.id.tempMin);
            conditionName = (TextView) itemView.findViewById(R.id.conditionName);
            conditionImage = (ImageView) itemView.findViewById(R.id.conditionImage);
        }
    }

}
