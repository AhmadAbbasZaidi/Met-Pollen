package com.cfp.metpollen.view.utilities;

import android.util.Log;

/**
 * Created by AhmedAbbas on 1/10/2018.
 */

public class SunRiseSETUtils {
    private static int startAngle = 0;
    private static int endAngle = 180;
    private static int angle = 0;
    private static int limit = 15/*min*/;

    public static int getArcAngle(String date, String riseHrs, String riseMin, String setHrs, String setMin) {
        int riseMinutes = Integer.parseInt(riseHrs) * 60 + Integer.parseInt(riseMin);
        int setMinutes = Integer.parseInt(setHrs) * 60 + Integer.parseInt(setMin);
        int currentMin = utils.getCurrentMinOfDay();

//        if(date.equalsIgnoreCase(utils.getCurrentDate())) {
        int min_diff = setMinutes - riseMinutes;

        float single_div = (float) min_diff / 180;

        if (currentMin < riseMinutes) {
            angle = -1;
        } else if (currentMin >= riseMinutes && currentMin < setMinutes) {
            angle = (int) ((currentMin - riseMinutes) / single_div);
        } else {
            angle = endAngle;
        }
//        }
//        else
//        {
//            angle = startAngle-1;
//        }

        Log.i("minDiff = ", min_diff + "");
        Log.i("single_div = ", single_div + "");
        Log.i("riseMinutes = ", riseMinutes + "");
        Log.i("setMinutes = ", setMinutes + "");
        Log.i("cuttentMinutes = ", currentMin + "");
        Log.i("Arc Angle = ", angle + "");
        return angle;
    }

    public static boolean isDay(String riseHrs, String riseMin, String setHrs, String setMin) {
        int riseMinutes = Integer.parseInt(riseHrs) * 60 + Integer.parseInt(riseMin);
        int setMinutes = Integer.parseInt(setHrs) * 60 + Integer.parseInt(setMin);
        int currentMin = utils.getCurrentMinOfDay();

        Log.i("isDay riseMinutes = ", riseMinutes + "");
        Log.i("isDay setMinutes = ", setMinutes + "");
        Log.i("isDay cuttentMinutes = ", currentMin + "");
        if (currentMin >= riseMinutes - limit && currentMin <= setMinutes + limit) {
            Log.i("isDay = ", "true");

            return true;
        } else {
            Log.i("isDay = ", "false");
            return false;
        }

    }

    public static boolean isDayForecast(String forecastHour, String riseHrs, String riseMin, String setHrs, String setMin) {
        int riseMinutes = Integer.parseInt(riseHrs) * 60 + Integer.parseInt(riseMin);
        int setMinutes = Integer.parseInt(setHrs) * 60 + Integer.parseInt(setMin);
        int currentMin = Integer.parseInt(forecastHour) * 60;

        Log.i("isDay riseMinutes = ", riseMinutes + "");
        Log.i("isDay setMinutes = ", setMinutes + "");
        Log.i("isDay cuttentMinutes = ", currentMin + "");
        if (currentMin >= riseMinutes - limit && currentMin <= setMinutes + limit) {
            Log.i("isDay = ", "true");

            return true;
        } else {
            Log.i("isDay = ", "false");
            return false;
        }

    }

}
