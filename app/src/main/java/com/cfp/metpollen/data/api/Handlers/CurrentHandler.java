package com.cfp.metpollen.data.api.Handlers;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.cfp.metpollen.data.db.DbUtils;
import com.cfp.metpollen.data.db.model.CurrentModel;
import com.cfp.metpollen.data.db.model.PollenModel;
import com.cfp.metpollen.view.activities.MainActivity;
import com.cfp.metpollen.view.utilities.BackgroundUtils;
import com.cfp.metpollen.view.utilities.SunRiseSETUtils;
import com.cfp.metpollen.view.utilities.TempConditionUtils;
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

public class CurrentHandler {

    Activity activity;
    JSONObject response;
    HashMap<String, String> sunSet, sunRise;

    public CurrentHandler(Activity activity, JSONObject response) {
        this.activity = activity;
        this.response = response;
        sunRise = new HashMap<>();
        sunSet = new HashMap<>();
    }


/*
    {
        "Result": {
        "Status": 200,
                "Message": "[
        {
            "Min_Temperature": 0,
                "Temperature": 0,
                "Entry_Time": "2017-12-0810: 34: 11.216452",
                "Wind_Speed": 0,
                "Formatted_Weather_Time": "2017-10-1903: 00: 00.0",
                "Max_Temperature": 0,
                "Precipitation": 0,
                "Weather_Time": "03Z19OCT2017",
                "PMSL": 0,
                "Rain_Humidity": 0,
                "Wind_Direction": 0,
                "Cloud": 0,
                "Id": 203638,
                "Grid_Location_Id": 4042

        },

*/

    public void handleResponse() {
        new AsyncTask<Void, Void, CurrentModel>() {
            @Override
            protected CurrentModel doInBackground(Void... voids) {
                try {
                    DbUtils.deleteCurrent();
                    JSONObject message = response.getJSONObject("Message");

//                    String stat = response.getString("Message");
                    CurrentModel currentModel = new CurrentModel();
                    if (message.has("Weatherdata")) {
                        JSONObject currentData = message.getJSONObject("Weatherdata");
                        Log.i("Message = ", currentData.toString());
                        if (currentData.has("imageurl")) {
                            String imageUrl = currentData.getString("imageurl");
                            currentModel.setImageUrl(imageUrl);
                        } else {
                            currentModel.setImageUrl("");
                        }
                        BackgroundUtils.setBackgroundUrl(currentModel.getImageUrl());
                        Log.i("image Url = ", currentModel.getImageUrl() == null ? "null**********************************************************" : currentModel.getImageUrl());
                        if (currentData.has("Min_Temperature"))
                            currentModel.setMinTemperature(currentData.getString("Min_Temperature"));
                        if (currentData.has("Precipitation"))
                            currentModel.setPrecipitation(currentData.getString("Precipitation"));
                        if (currentData.has("Mean_Temperature"))
                            currentModel.setTemperature(currentData.getString("Mean_Temperature"));
                        if (currentData.has("Entry_Time"))
                            currentModel.setEntryTime(currentData.getString("Entry_Time"));
                        if (currentData.has("Station_Id"))
                            currentModel.setStationId(currentData.getString("Station_Id"));
                        if (currentData.has("Id"))
                            currentModel.setCurrentId(currentData.getString("Id"));
                        if (currentData.has("Dew_Point"))
                            currentModel.setDewPoint(currentData.getString("Dew_Point"));
                        if (currentData.has("Visibility"))
                            currentModel.setVisibility(currentData.getString("Visibility"));
                        if (currentData.has("Wind_Speed"))
                            currentModel.setWindSpeed(currentData.getString("Wind_Speed"));
                        if (currentData.has("Wind_Direction"))
                            currentModel.setWindDirection(currentData.getString("Wind_Direction"));
                        if (currentData.has("Formatted_Weather_Time")) {
                            currentModel.setFormattedWeatherTime(currentData.getString("Formatted_Weather_Time"));
                            currentModel.setPrecipitationNoon(DbUtils.getPrecipitationForTime(currentModel.getFormattedWeatherDate(), CurrentModel.NOON));
                            currentModel.setPrecipitationEvening(DbUtils.getPrecipitationForTime(currentModel.getFormattedWeatherDate(), CurrentModel.EVENING));
                            currentModel.setPrecipitationTonight(DbUtils.getPrecipitationForTime(currentModel.getFormattedWeatherDate(), CurrentModel.TONIGHT));
                        }
                        if (currentData.has("Max_Temperature"))
                            currentModel.setMaxTemperature(currentData.getString("Max_Temperature"));
                        if (currentData.has("Relative_Humidity"))
                            currentModel.setRelativeHumidity(currentData.getString("Relative_Humidity"));

                        if (message.has("SunRiseSet")) {
                            JSONObject sunRiseSet = message.getJSONObject("SunRiseSet");
                            if (sunRiseSet.has("SunRise") && sunRiseSet.has("SunSet")) {
                                currentModel.setSunRise(utils.getConvertedDateFromOneFormatToOther(sunRiseSet.getString("SunRise"), utils.FORMAT1, utils.FORMAT2));
                                currentModel.setSunSet(utils.getConvertedDateFromOneFormatToOther(sunRiseSet.getString("SunSet"), utils.FORMAT1, utils.FORMAT2));
                                currentModel.setSunRiseHour(utils.getRequiredTimeField(utils.HOUR, currentModel.getSunRise()));
                                currentModel.setSunRiseMin(utils.getRequiredTimeField(utils.MIN, currentModel.getSunRise()));
                                currentModel.setSunSetHour(utils.getRequiredTimeField(utils.HOUR, currentModel.getSunSet()));
                                currentModel.setSunSetMin(utils.getRequiredTimeField(utils.MIN, currentModel.getSunSet()));
                                TempConditionUtils.setIsDay(SunRiseSETUtils.isDay(currentModel.getSunRiseHour(), currentModel.getSunRiseMin(), currentModel.getSunSetHour(), currentModel.getSunSetMin()));
                            }
                        }
/*
                    parseJson(sunRiseSet);
                    if (sunRise.containsKey(utils.getConvertedDateFromOneFormatToOther(currentData.getString("Formatted_Weather_Time"), utils.FORMAT1, utils.FORMAT3))) {
                        currentModel.setSunRiseHour(utils.getRequiredTimeField(utils.HOUR, sunRise.get(utils.getConvertedDateFromOneFormatToOther(currentData.getString("Formatted_Weather_Time"), utils.FORMAT1, utils.FORMAT3))));
                        currentModel.setSunRiseMin(utils.getRequiredTimeField(utils.MIN, sunRise.get(utils.getConvertedDateFromOneFormatToOther(currentData.getString("Formatted_Weather_Time"), utils.FORMAT1, utils.FORMAT3))));
                        if (sunSet.containsKey(utils.getConvertedDateFromOneFormatToOther(currentData.getString("Formatted_Weather_Time"), utils.FORMAT1, utils.FORMAT3))) {
                            currentModel.setSunSetHour(utils.getRequiredTimeField(utils.HOUR, sunSet.get(utils.getConvertedDateFromOneFormatToOther(currentData.getString("Formatted_Weather_Time"), utils.FORMAT1, utils.FORMAT3))));
                            currentModel.setSunSetMin(utils.getRequiredTimeField(utils.MIN, sunSet.get(utils.getConvertedDateFromOneFormatToOther(currentData.getString("Formatted_Weather_Time"), utils.FORMAT1, utils.FORMAT3))));
                        }
                    } else {
                        currentModel.setDefault();
                    }
*/
                    }

                    if (message.has("Pollen")) {
                        JSONObject pollenObject = message.getJSONObject("Pollen");

                        if (pollenObject.has("StationName"))
                            currentModel.setPollenStation(pollenObject.getString("StationName"));
                        if(pollenObject.has("total"))
                            currentModel.setPollenTotalCount(pollenObject.getString("total"));
                        if(pollenObject.has("total_status"))
                            currentModel.setPollenTotalStatus(pollenObject.getString("total_status"));
                        if(pollenObject.has("pollen_percentage"))
                            currentModel.setPollenTotalPercentage(pollenObject.getString("pollen_percentage"));

                        if(pollenObject.has("Pollendata")) {
                            JSONArray pollenData = pollenObject.getJSONArray("Pollendata");
                                List<PollenModel> pollenModels = new ArrayList<>();
                            for (int i = 0; i < pollenData.length(); i++) {
                                JSONObject pollen = pollenData.getJSONObject(i);
                                PollenModel pollenModel = new PollenModel();
                                if(pollen.has("name"))
                                    pollenModel.setName(pollen.getString("name"));
                                if(pollen.has("count"))
                                    pollenModel.setCount(pollen.getString("count"));
                                if(pollen.has("status"))
                                    pollenModel.setStatus(pollen.getString("status"));
                                pollenModel.setCurrentId(currentModel.getCurrentId());
                                pollenModel.save();
                                pollenModels.add(pollenModel);
                            }
                            currentModel.setPollenList(pollenModels);
                        }
                    }
/*
                        currentModel.setWindSpeed(object.getInt("Wind_Speed"));
                        currentModel.setWeatherTime(object.getString("Weather_Time"));
                        currentModel.setPressureMeanSeaLevel(object.getInt("PMSL"));
                        currentModel.setWindDirection(object.getInt("Wind_Direction"));
                        currentModel.setCloud(object.getInt("Cloud"));
                        currentModel.setGridLocationId(object.getInt("Grid_Location_Id"));
*/


//                    currentModel.setPressureMeanSeaLevel(0);
//                    currentModel.setCloud(0);



                    currentModel.save();
                    SharedPreferences spref = activity.getSharedPreferences("USER", MODE_PRIVATE);
                    SharedPreferences.Editor editor = spref.edit();
                    editor.putString("image", currentModel.getImageUrl());
                    editor.commit();

                    return currentModel;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(CurrentModel currentModel) {
                super.onPostExecute(currentModel);
                ((MainActivity) activity).updateCurrent(currentModel);
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
                    try {
//                        System.out.println(key + ":" + sunRiseSet.getString(key));
                    } catch (Exception ee) {
                    }
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
