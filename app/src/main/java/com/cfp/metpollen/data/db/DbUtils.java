package com.cfp.metpollen.data.db;

import android.util.Log;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.cfp.metpollen.data.db.model.CurrentModel;
import com.cfp.metpollen.data.db.model.ForecastModel;
import com.cfp.metpollen.data.db.model.PollenModel;
import com.cfp.metpollen.data.db.model.StationModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AhmedAbbas on 12/7/2017.
 */

public class DbUtils {
    public static List<StationModel> getStations() {
        List<StationModel> stationModelList = new Select().all().from(StationModel.class).execute();
        return stationModelList;
    }

    public static List<ForecastModel> getForecast() {
        List<ForecastModel> forecastModelList = new Select().all().from(ForecastModel.class).execute();
        return forecastModelList != null && forecastModelList.size() > 0 ? forecastModelList : null;
    }

    public static List<ForecastModel> getForecastDayNight() {
        List<ForecastModel> forecastModelList = new Select().from(ForecastModel.class).where("Formated_Hour = ?","09").or("Formated_Hour = ?","21").orderBy("Formated_Weather_Date ASC").execute();
        Log.i("sinewave ", "forecast list size = " + forecastModelList.size());
        return forecastModelList != null && forecastModelList.size() > 0 ? forecastModelList : null;
    }

    public static List<ForecastModel> getForecastForDate(String date) {
        List<ForecastModel> forecastModelList = new Select().from(ForecastModel.class).where("Formated_Weather_Date = ?", date).execute();
        Log.i("heatmap ", "forecast list size = " + forecastModelList.size());
        return forecastModelList != null && forecastModelList.size() > 0 ? forecastModelList : null;
    }

    public static CurrentModel getCurrent() {
        List<CurrentModel> currentModelList = new Select().all().from(CurrentModel.class).execute();
        return currentModelList != null && currentModelList.size() > 0 ? currentModelList.get(0) : null;
    }

    public static void deleteForecast() {
        new Delete().from(ForecastModel.class).execute();
    }

    public static void deleteCurrent() {
        new Delete().from(PollenModel.class).execute();
        new Delete().from(CurrentModel.class).execute();
    }

    public static boolean hasForecast() {
        List<ForecastModel> forecastModelList = new Select().all().from(ForecastModel.class).execute();
        return forecastModelList != null && forecastModelList.size() > 0 ? true : false;
    }

    public static String getPrecipitationForTime(String date, String hour) {
        ForecastModel forecastModel = (ForecastModel) new Select().from(ForecastModel.class).where("Formated_Weather_Date = ?", date).and("Formated_Hour = ?", hour).execute().get(0);
        Log.i("date = ", date + " db Date = " + forecastModel.getFormattedWeatherDate() + " Precipitation = " + forecastModel.getPrecipitation());
        return forecastModel != null && !forecastModel.getPrecipitation().equalsIgnoreCase("--") ? String.valueOf((int)Float.parseFloat(forecastModel.getPrecipitation())) : "--";
    }

    public static List<PollenModel> getPollenData(String currentId) {
        List<PollenModel> pollenModelList = new Select().all().from(PollenModel.class).where("Current_Id = ?", currentId).execute();
        return pollenModelList;
    }
}
