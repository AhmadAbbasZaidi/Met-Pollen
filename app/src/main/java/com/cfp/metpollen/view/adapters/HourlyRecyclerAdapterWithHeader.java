package com.cfp.metpollen.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cfp.metpollen.R;
import com.cfp.metpollen.data.db.model.ForecastModel;
import com.cfp.metpollen.view.objectModels.HourlyModel;
import com.cfp.metpollen.view.objectModels.WeatherCondition;
import com.cfp.metpollen.view.utilities.SunRiseSETUtils;
import com.cfp.metpollen.view.utilities.TempConditionUtils;
import com.cfp.metpollen.view.utilities.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static java.sql.Types.NULL;


/**
 * Created by AhmedAbbas on 11/17/2017.
 */

public class HourlyRecyclerAdapterWithHeader extends RecyclerView.Adapter<HourlyRecyclerAdapterWithHeader.ViewHolder> {
    private static List<ForecastModel> forecastModelsList;
    Context context;
    HashMap<String, Integer> headerKeyValuePair;
    private List<HourlyModel> hourlyModelsList;

    public HourlyRecyclerAdapterWithHeader(Context context, List<ForecastModel> list) {
        this.context = context;
        setForecastModelsList(list);


    }

    public static List<ForecastModel> getForecastModelsList() {
        return forecastModelsList;
    }

    public void setForecastModelsList(List<ForecastModel> forecastModelsList) {
        this.forecastModelsList = forecastModelsList;
        headerKeyValuePair = new HashMap<>();

        hourlyModelsList = new ArrayList<>();
        for (int i = 0; i < forecastModelsList.size(); i++) {
            ForecastModel forecastModel = forecastModelsList.get(i);
            if (i == 0) {
                headerKeyValuePair.put(getDate(forecastModel.getFormattedWeatherTime()), i);
                hourlyModelsList.add(new HourlyModel(forecastModel.getTemperature(), forecastModel.getFormattedWeatherTime(), forecastModel.getPrecipitation(), forecastModel.getCloud(),forecastModel.getSunRiseHour(),forecastModel.getSunRiseMin(),forecastModel.getSunSetHour(),forecastModel.getSunSetMin(), true));
                hourlyModelsList.add(new HourlyModel(forecastModel.getTemperature(), forecastModel.getFormattedWeatherTime(), forecastModel.getPrecipitation(), forecastModel.getCloud(),forecastModel.getSunRiseHour(),forecastModel.getSunRiseMin(),forecastModel.getSunSetHour(),forecastModel.getSunSetMin(), false));
            } else {
                if (headerKeyValuePair.containsKey(getDate(forecastModel.getFormattedWeatherTime()))) {
                    hourlyModelsList.add(new HourlyModel(forecastModel.getTemperature(), forecastModel.getFormattedWeatherTime(), forecastModel.getPrecipitation(), forecastModel.getCloud(),forecastModel.getSunRiseHour(),forecastModel.getSunRiseMin(),forecastModel.getSunSetHour(),forecastModel.getSunSetMin(), false));
                } else {
                    headerKeyValuePair.put(getDate(forecastModel.getFormattedWeatherTime()), i);
                    hourlyModelsList.add(new HourlyModel(forecastModel.getTemperature(), forecastModel.getFormattedWeatherTime(), forecastModel.getPrecipitation(), forecastModel.getCloud(),forecastModel.getSunRiseHour(),forecastModel.getSunRiseMin(),forecastModel.getSunSetHour(),forecastModel.getSunSetMin(), true));
                    hourlyModelsList.add(new HourlyModel(forecastModel.getTemperature(), forecastModel.getFormattedWeatherTime(), forecastModel.getPrecipitation(), forecastModel.getCloud(),forecastModel.getSunRiseHour(),forecastModel.getSunRiseMin(),forecastModel.getSunSetHour(),forecastModel.getSunSetMin(), false));
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public HourlyRecyclerAdapterWithHeader.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_hourly, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HourlyRecyclerAdapterWithHeader.ViewHolder holder, int position) {
        /*ForecastModel model = forecastModelsList.get(position);
        String date = getDate(model.getFormattedWeatherTime());
        if (position==headerKeyValuePair.get(date)) {
            if(position!=0) {
                holder.sectionHeader.setText(date);
                holder.sectionHeader.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.sectionHeader.setVisibility(View.GONE);
            }
        } else {
            holder.sectionHeader.setVisibility(View.GONE);
        }
        holder.time.setText(model.getEntryTime());
        holder.temp.setText(model.getTemperature()+"°");
        holder.precipitation.setText(model.getPrecipitation()+"%");*/


        HourlyModel hourly = hourlyModelsList.get(position);
        if (hourly.isHeader()) {
            if (position != 0) {
                holder.item.setVisibility(View.GONE);
                holder.sectionHeader.setVisibility(View.VISIBLE);
                holder.sectionHeader.setText(utils.getHeaderDate(hourly.getFormattedWeatherTime()));
                holder.hLine.setVisibility(View.GONE);
            } else {
                holder.item.setVisibility(View.GONE);
                holder.sectionHeader.setVisibility(View.GONE);
                holder.hLine.setVisibility(View.GONE);
            }
        } else {
            if (position + 1 < hourlyModelsList.size()) {
                if (hourlyModelsList.get(position + 1).isHeader()) {
                    holder.hLine.setVisibility(View.GONE);
                } else {
                    holder.hLine.setVisibility(View.VISIBLE);
                }
            } else {
                holder.hLine.setVisibility(View.GONE);
            }
            holder.sectionHeader.setVisibility(View.GONE);
            holder.item.setVisibility(View.VISIBLE);
            holder.time.setText(utils.getHourInTwelveHourFormat(hourly.getFormattedWeatherTime()));
            String tmp = hourly.getTemperature().equalsIgnoreCase("--")?"--":(int)Float.parseFloat(hourly.getTemperature()) < 10 ? "0" + (int)Float.parseFloat(hourly.getTemperature()) : (int)Float.parseFloat(hourly.getTemperature()) + "";
            holder.temp.setText(tmp + "°");

            WeatherCondition weatherCondition = TempConditionUtils.getConditionForecast(hourly.getPrecipitation(), hourly.getCloud(), SunRiseSETUtils.isDayForecast(hourly.getFormattedHour(), hourly.getSunRiseHour(), hourly.getSunRiseMin(), hourly.getSunSetHour(), hourly.getSunSetMin()));
            if(weatherCondition!=null) {
                holder.imageCondition.setImageResource(weatherCondition.getResourceId());
            }
            else
            {
                holder.imageCondition.setImageResource(NULL);
            }
            String prec = hourly.getPrecipitation().equalsIgnoreCase("--") ? "--" : (int)Float.parseFloat(hourly.getPrecipitation()) < 10 ? "0" + (int)Float.parseFloat(hourly.getPrecipitation()) : (int)Float.parseFloat(hourly.getPrecipitation()) + "";
            holder.precipitation.setText(prec + " mm");
        }
    }

    @Override
    public int getItemCount() {
        return hourlyModelsList.size();
    }

    public String getDateAtPosition(int start) {
        if (hourlyModelsList.get(start).isHeader() && start > 0) {
            return utils.getHeaderDate(hourlyModelsList.get(start - 1).getFormattedWeatherTime());
        } else {
            return utils.getHeaderDate(hourlyModelsList.get(start).getFormattedWeatherTime());
        }
    }

    public String getDay(String date) {
        Calendar calendar = Calendar.getInstance();

        String[] datetime = date.split(" ");
        String[] ymd = datetime[0].split("-");
        Date currentdate = calendar.getTime();
        int year = Integer.parseInt(ymd[0]) - 1900;
        int month = Integer.parseInt(ymd[1]) - 1;
        int day = Integer.parseInt(ymd[2]);
        if (currentdate.getYear() == year && currentdate.getMonth() == month && currentdate.getDate() == day) {
            return "TODAY";
        }

        Date newdate = new Date();
        newdate.setYear(year);
        newdate.setMonth(month);
        newdate.setDate(day);
        Log.i("date1 ", String.valueOf(newdate.getTime()));
        calendar.setTime(newdate);
        Log.i("date2 ", String.valueOf(calendar.getTime()));

        String[] days = new String[]{"", "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};

        String d = days[calendar.get(Calendar.DAY_OF_WEEK)];
        return d;
    }

    public String getDate(String date) {
        String d = "";
        try {
            String[] datetime = date.split(" ");
            d = datetime[0];
            Log.i("Date = ", " " + datetime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return d;
    }

    public String getHour(String date) {
        String[] d = new String[0];
        try {
            String[] datetime = date.split(" ");
            d = datetime[1].split(":");
            Log.i("Date = ", " " + datetime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return d[0];
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sectionHeader;
        TextView time;
        TextView temp;
        TextView precipitation;
        ImageView imageCondition;
        LinearLayout item;
        View hLine;

        public ViewHolder(View itemView) {
            super(itemView);
            sectionHeader = (TextView) itemView.findViewById(R.id.textview_section_header);
            time = (TextView) itemView.findViewById(R.id.time);
            temp = (TextView) itemView.findViewById(R.id.temp);
            precipitation = (TextView) itemView.findViewById(R.id.precipitation);
            imageCondition = (ImageView) itemView.findViewById(R.id.imagePrecipitation);
            item = itemView.findViewById(R.id.item);
            hLine = itemView.findViewById(R.id.hLine);
        }
    }
}
