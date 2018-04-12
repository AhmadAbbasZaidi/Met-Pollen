package com.cfp.metpollen.data.api.Handlers;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.cfp.metpollen.data.db.DbUtils;
import com.cfp.metpollen.data.db.model.StationModel;
import com.cfp.metpollen.view.activities.MainActivity;
import com.cfp.metpollen.view.backgroundService.GetStationsService;
import com.cfp.metpollen.view.utilities.AppWideVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AhmedAbbas on 12/7/2017.
 */

public class StationHandler {

    Activity activity;
    JSONObject response;
    GetStationsService getStationsService;

    public void setGetStationsService(GetStationsService getStationsService) {
        this.getStationsService = getStationsService;
    }

    public StationHandler(Activity activity, JSONObject response) {
        this.activity = activity;
        this.response = response;
    }

    public void handleResponse() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Log.i("GET STATIONS HANDLER","DO IN BACKGROUND");
                    List<StationModel> stationsList = new ArrayList<>();
                    String stat = response.getString("Message");
                    JSONArray stations = new JSONArray(response.getString("Message"));
                    Log.i("Message = ", stations.toString());
                    for (int i = 0; i < stations.length(); i++) {
                        JSONObject object = stations.getJSONObject(i);
                        Log.i("Station " + i, object.toString());
                        String latitude = String.valueOf(object.getDouble("Latitude"));
                        String longitude = String.valueOf(object.getDouble("Longitude"));
                        String stationName = object.getString("Station_Name");
                        int id = object.getInt("Id");
                        StationModel model = new StationModel(latitude, longitude, stationName, id);
                        /*if(object.has("Images"))
                        {
                            JSONObject images = new JSONObject(object.getString("Images"));
                            String day_Url = images.getString("Day");
                            String day_blur_Url = images.getString("DayBlur");
                            String night_Url = images.getString("Night");
                            String night_blur_Url = images.getString("NightBlur");
                            model.setDayImage(day_Url!=null?day_Url:"");
                            model.setDayBlurImage(day_blur_Url!=null?day_blur_Url:"");
                            model.setNightImage(night_Url!=null?night_Url:"");
                            model.setNightBlurImage(night_blur_Url!=null?night_blur_Url:"");
                        }
                        else
                        {
                            model.setDayImage("");
                            model.setDayBlurImage("");
                            model.setNightImage("");
                            model.setNightBlurImage("");
                        }*/
                        model.save();
                        stationsList.add(model);
                        AppWideVariables.setStationList(stationsList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.i("GET STATIONS HANDLER","ONPOSTEXECUTE");
                if(getStationsService!=null)
                {
                    getStationsService.stopSelf();
                    Log.i("GetStationsService()","getStationsService.stopSelf()");
                    return;
                }
                if (DbUtils.getStations() == null) {
                    /*Intent intent = new Intent(activity, MainActivity.class);
                    activity.startActivity(intent);
                    activity.finish();*/
                }
            }
        }.execute();
    }
}
