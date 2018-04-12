package com.cfp.metpollen.data.api.Handlers;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.cfp.metpollen.data.db.DbUtils;
import com.cfp.metpollen.data.db.model.ForecastModel;
import com.cfp.metpollen.view.activities.MainActivity;
import com.cfp.metpollen.view.utilities.BackgroundUtils;
import com.cfp.metpollen.view.utilities.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by AhmedAbbas on 12/7/2017.
 */

public class ForecastHandler {

    Activity activity;
    JSONObject response;
    HashMap<String, String> sunSet, sunRise;

    public ForecastHandler(Activity activity, JSONObject response) {
        this.activity = activity;
        this.response = response;
        sunRise = new HashMap<>();
        sunSet = new HashMap<>();
    }

    public void handleResponse() {
        new AsyncTask<Void, Void, List<ForecastModel>>() {
            @Override
            protected List<ForecastModel> doInBackground(Void... voids) {
                try {
                    if (response.has("Message")) {
                        JSONObject message = response.getJSONObject("Message");
                        List<ForecastModel> forecastList = new ArrayList<>();
                        DbUtils.deleteForecast();

                        if (message.has("SunRiseSet")) {
                            JSONObject sunRiseSet = message.getJSONObject("SunRiseSet");
                            parseJson(sunRiseSet);
                        }
//                    String stat = response.getString("Message");

                        if (message.has("Weatherdata")) {
                            JSONArray forecast = new JSONArray(message.getString("Weatherdata"));
                            Log.i("Message = ", forecast.toString());
                            for (int i = 0; i < forecast.length(); i++) {
                                JSONObject object = forecast.getJSONObject(i);
                                ForecastModel forecastModel = new ForecastModel();
                                if (object.has("imageurl")) {
                                    String imageUrl = object.getString("imageurl");
                                    forecastModel.setImageUrl(imageUrl);
                                } else {
                                    forecastModel.setImageUrl("");
                                }
                                BackgroundUtils.setBackgroundUrl(forecastModel.getImageUrl());
                                Log.i("image Url = ", forecastModel.getImageUrl());
                                if (object.has("Min_Temperature"))
                                    forecastModel.setMinTemperature(object.getString("Min_Temperature"));
                                if (object.has("Temperature"))
                                    forecastModel.setTemperature(object.getString("Temperature"));
                                if (object.has("Entry_Time"))
                                    forecastModel.setEntryTime(object.getString("Entry_Time"));
                                if (object.has("Wind_Speed"))
                                    forecastModel.setWindSpeed(object.getString("Wind_Speed"));


//                              check if sun rise and sun set is valid and not null
                                if (object.has("Formatted_Weather_Time")) {
                                    forecastModel.setFormattedWeatherTime(object.getString("Formatted_Weather_Time"));
                                    if (sunRise.containsKey(utils.getConvertedDateFromOneFormatToOther(object.getString("Formatted_Weather_Time"), utils.FORMAT1, utils.FORMAT3))) {
                                        forecastModel.setSunRiseHour(utils.getRequiredTimeField(utils.HOUR, sunRise.get(utils.getConvertedDateFromOneFormatToOther(object.getString("Formatted_Weather_Time"), utils.FORMAT1, utils.FORMAT3))));
                                        forecastModel.setSunRiseMin(utils.getRequiredTimeField(utils.MIN, sunRise.get(utils.getConvertedDateFromOneFormatToOther(object.getString("Formatted_Weather_Time"), utils.FORMAT1, utils.FORMAT3))));
                                        if (sunSet.containsKey(utils.getConvertedDateFromOneFormatToOther(object.getString("Formatted_Weather_Time"), utils.FORMAT1, utils.FORMAT3))) {
                                            forecastModel.setSunSetHour(utils.getRequiredTimeField(utils.HOUR, sunSet.get(utils.getConvertedDateFromOneFormatToOther(object.getString("Formatted_Weather_Time"), utils.FORMAT1, utils.FORMAT3))));
                                            forecastModel.setSunSetMin(utils.getRequiredTimeField(utils.MIN, sunSet.get(utils.getConvertedDateFromOneFormatToOther(object.getString("Formatted_Weather_Time"), utils.FORMAT1, utils.FORMAT3))));
                                        }
                                    } else {
                                        forecastModel.setDefault();
                                    }
                                }
                                if (object.has("Max_Temperature"))
                                    forecastModel.setMaxTemperature(object.getString("Max_Temperature"));
                                if (object.has("Precipitation"))
                                    forecastModel.setPrecipitation(object.getString("Precipitation"));
                                if (object.has("Weather_Time"))
                                    forecastModel.setWeatherTime(object.getString("Weather_Time"));
                                if (object.has("PMSL"))
                                    forecastModel.setPressureMeanSeaLevel(object.getString("PMSL"));
                                if (object.has("Rain_Humidity"))
                                    forecastModel.setRelativeHumidity(object.getString("Rain_Humidity"));
                                if (object.has("Wind_Direction"))
                                    forecastModel.setWindDirection(object.getString("Wind_Direction"));
                                if (object.has("Cloud"))
                                    forecastModel.setCloud(object.getString("Cloud"));
                                if (object.has("Id"))
                                    forecastModel.setForecastId(object.getString("Id"));
                                if (object.has("Grid_Location_Id"))
                                    forecastModel.setGridLocationId(object.getString("Grid_Location_Id"));
                                forecastModel.save();
                                forecastList.add(forecastModel);
                                SharedPreferences spref = activity.getSharedPreferences("USER", MODE_PRIVATE);
                                SharedPreferences.Editor editor = spref.edit();
                                editor.putString("image", forecastModel.getImageUrl());
                                editor.commit();

                            }
                        }
                        return forecastList;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<ForecastModel> forecastList) {
                super.onPostExecute(forecastList);
                if (forecastList != null && forecastList.size() > 0)
                    ((MainActivity) activity).updateForecast(forecastList);
            }
        }.execute();
    }

    private void parseJson(JSONObject sunRiseSet) {
        if (sunRiseSet != null) {
            Iterator<String> it = sunRiseSet.keys();
            while (it.hasNext()) {
                String key = it.next();
                try {
                    /*if (sunRiseSet.get(key) instanceof JSONArray) {
                        JSONArray arry = sunRiseSet.getJSONArray(key);
                        int size = arry.length();
                        for (int i = 0; i < size; i++) {
                            parseJson(arry.getJSONObject(i));
                        }
                    } else */
                    if (sunRiseSet.get(key) instanceof JSONObject) {
                        parseJsonObject(sunRiseSet.getJSONObject(key), key);
                    } /*else {


                        System.out.println(key + ":" + sunRiseSet.getString(key));
                    }*/
                } catch (Throwable e) {
                    e.printStackTrace();

                }
            }
        }
    }

    private void parseJsonObject(JSONObject jsonObject, String key) {

        try {
            sunRise.put(utils.getConvertedDateFromOneFormatToOther(key, utils.FORMAT1, utils.FORMAT3), utils.getConvertedDateFromOneFormatToOther(jsonObject.getString("SunRise"), utils.FORMAT1, utils.FORMAT2));
            sunSet.put(utils.getConvertedDateFromOneFormatToOther(key, utils.FORMAT1, utils.FORMAT3), utils.getConvertedDateFromOneFormatToOther(jsonObject.getString("SunSet"), utils.FORMAT1, utils.FORMAT2));
            Log.i("key = ", utils.getConvertedDateFromOneFormatToOther(key, utils.FORMAT1, utils.FORMAT3));
            Log.i("SunRise = ", utils.getConvertedDateFromOneFormatToOther(jsonObject.getString("SunRise"), utils.FORMAT1, utils.FORMAT2));
            Log.i("SunSet = ", utils.getConvertedDateFromOneFormatToOther(jsonObject.getString("SunSet"), utils.FORMAT1, utils.FORMAT2));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
