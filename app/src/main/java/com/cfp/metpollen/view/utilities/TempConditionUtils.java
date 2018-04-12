package com.cfp.metpollen.view.utilities;

import android.content.Context;
import android.util.Log;

import com.cfp.metpollen.R;
import com.cfp.metpollen.view.objectModels.WeatherCondition;

/**
 * Created by AhmedAbbas on 1/9/2018.
 */

public class TempConditionUtils {
    private static final String SUNNY = "Sunny";
    private static final String MOSTLY_SUNNY = "Mostly Sunny";
    private static final String PARTLY_SUNNY = "Partly Sunny";
    private static final String MOSTLY_CLOUDY = "Mostly Cloudy";
    private static final String CLOUDY = "Cloudy";
    private static final String CLEAR = "Clear";
    private static final String MOSTLY_CLEAR = "Mostly Clear";
    private static final String PARTLY_CLOUDY = "Partly Cloudy";


    private static final String NORAIN = "No Rain";
    private static final String VERY_LIGHT_RAIN = "Very Light Rain";
    private static final String LIGHT_RAIN = "Light Rain";
    private static final String MODERATE_RAIN = "Moderate Rain";
    private static final String RATHER_HEAVY_RAIN = "Rather Heavy Rain";
    private static final String HEAVY_RAIN = "Heavy Rain";
    private static final String VERY_HEAVY_RAIN = "Very Heavy Rain";
    private static final String EXTREMELY_HEAVY_RAIN = "Extremely Heavy Rain";

    private static final float SUNNY_RANGE = 12.5f;
    private static final float MOSTLY_SUNNY_RANGE = 37.5f;
    private static final float PARTLY_RANGE = 62.5f;
    private static final float MOSTLY_CLOUDY_RANGE = 87.5f;
    private static final float CLOUDY_RANGE = 100f;

    private static final float NO_RAIN_RANGE = 0.0f;
    private static final float VERY_LIGHT_RAIN_RANGE = 2.4f;
    private static final float LIGHT_RAIN_RANGE = 7.5f;
    private static final float MODERATE_RANGE = 35.5f;
    private static final float RATHER_HEAVY_RAIN_RANGE = 64.4f;
    private static final float HEAVY_RAIN_RANGE = 124.4f;
    private static final float VERY_HEAVY_RAIN_RANGE = 244.4f;
    private static boolean isDay = true;

    public static void setIsDay(boolean isDay) {
        TempConditionUtils.isDay = isDay;
        Log.i("isDay Temp = ", TempConditionUtils.isDay == true ? "true" : "false");
    }

    public static String getCLEAR() {
        return CLEAR;
    }

    public static WeatherCondition getConditionForecast(String r, String c, boolean isDay) {
        WeatherCondition weatherCondition = new WeatherCondition();
        float rain;
        float cloud;
        if (r.equalsIgnoreCase("--") || c.equalsIgnoreCase("--")) {
            return null;
        } else {
            rain = Float.parseFloat(r);
            cloud = Float.parseFloat(c);
        }
        if (cloud == 0 && rain == 0) {
            if (isDay) {
                weatherCondition.setCondition(SUNNY);
                weatherCondition.setResourceId(R.drawable.clear_day);
            } else {
                weatherCondition.setCondition(CLEAR);
                weatherCondition.setResourceId(R.drawable.clear_night);
            }
        } else if (rain == 0 && cloud > 0) {
            if (cloud <= SUNNY_RANGE) {
                if (isDay) {
                    weatherCondition.setCondition(SUNNY);
                    weatherCondition.setResourceId(R.drawable.clear_day);
                } else {
                    weatherCondition.setCondition(CLEAR);
                    weatherCondition.setResourceId(R.drawable.clear_night);
                }
            } else if (cloud > SUNNY_RANGE && cloud <= MOSTLY_SUNNY_RANGE) {
                if (isDay) {
                    weatherCondition.setCondition(MOSTLY_SUNNY);
                    weatherCondition.setResourceId(R.drawable.mostly_sunny_day);
                } else {
                    weatherCondition.setCondition(MOSTLY_CLEAR);
                    weatherCondition.setResourceId(R.drawable.mostly_clear_night);
                }
            } else if (cloud > MOSTLY_SUNNY_RANGE && cloud <= PARTLY_RANGE) {
                if (isDay) {
                    weatherCondition.setCondition(PARTLY_SUNNY);
                    weatherCondition.setResourceId(R.drawable.partly_sunny);
                } else {
                    weatherCondition.setCondition(PARTLY_CLOUDY);
                    weatherCondition.setResourceId(R.drawable.partly_cloudy_night);
                }
            } else if (cloud > PARTLY_RANGE && cloud <= MOSTLY_CLOUDY_RANGE) {
                weatherCondition.setCondition(MOSTLY_CLOUDY);
                weatherCondition.setResourceId(R.drawable.mostly_cloudy_day_night);
            } else if (cloud > MOSTLY_CLOUDY_RANGE) {
                weatherCondition.setCondition(CLOUDY);
                weatherCondition.setResourceId(R.drawable.cloudy);
            }
        } else /*if(rain>0&& cloud==0)*/ {
            if (rain <= VERY_LIGHT_RAIN_RANGE) {
                weatherCondition.setCondition(VERY_LIGHT_RAIN);
                weatherCondition.setResourceId(R.drawable.light_rain_day);
            } else if (rain > VERY_LIGHT_RAIN_RANGE && rain <= LIGHT_RAIN_RANGE) {
                weatherCondition.setCondition(LIGHT_RAIN);
                weatherCondition.setResourceId(R.drawable.light_rain_day);
            } else if (rain > LIGHT_RAIN_RANGE && rain <= MODERATE_RANGE) {
                weatherCondition.setCondition(MODERATE_RAIN);
                weatherCondition.setResourceId(R.drawable.rain2);
            } else if (rain > MODERATE_RANGE && rain <= RATHER_HEAVY_RAIN_RANGE) {
                weatherCondition.setCondition(RATHER_HEAVY_RAIN);
                weatherCondition.setResourceId(R.drawable.heavy_rain_day);
            } else if (rain > RATHER_HEAVY_RAIN_RANGE && rain <= HEAVY_RAIN_RANGE) {
                weatherCondition.setCondition(HEAVY_RAIN);
                weatherCondition.setResourceId(R.drawable.heavy_rain_day);
            } else if (rain > HEAVY_RAIN_RANGE && rain <= VERY_HEAVY_RAIN_RANGE) {
                weatherCondition.setCondition(VERY_HEAVY_RAIN);
                weatherCondition.setResourceId(R.drawable.extremly_heavy_rain_day);
            } else if (rain > VERY_HEAVY_RAIN_RANGE) {
                weatherCondition.setCondition(EXTREMELY_HEAVY_RAIN);
                weatherCondition.setResourceId(R.drawable.extremly_heavy_rain_day);
            }
        }
        return weatherCondition;
    }


    public static WeatherCondition getConditionCurrent(String r, String c) {
        WeatherCondition weatherCondition = new WeatherCondition();

        float rain;
        float cloud;
        if (r.equalsIgnoreCase("--") || c.equalsIgnoreCase("--")) {
            return null;
        } else {
            rain = Float.parseFloat(r);
            cloud = Float.parseFloat(c);
        }

        if (cloud == 0 && rain == 0) {
            if (isDay) {
                weatherCondition.setCondition(SUNNY);
                weatherCondition.setResourceId(R.drawable.sunny);
            } else {
                weatherCondition.setCondition(CLEAR);
                weatherCondition.setResourceId(R.drawable.clear_wind);
            }
        } else if (rain == 0 && cloud > 0) {
            if (cloud <= SUNNY_RANGE) {
                if (isDay) {
                    weatherCondition.setCondition(SUNNY);
                    weatherCondition.setResourceId(R.drawable.sunny);
                } else {
                    weatherCondition.setCondition(CLEAR);
                    weatherCondition.setResourceId(R.drawable.clear);
                }
            } else if (cloud > SUNNY_RANGE && cloud <= MOSTLY_SUNNY_RANGE) {
                if (isDay) {
                    weatherCondition.setCondition(MOSTLY_SUNNY);
                    weatherCondition.setResourceId(R.drawable.mostly_sunny);
                } else {
                    weatherCondition.setCondition(MOSTLY_CLEAR);
                    weatherCondition.setResourceId(R.drawable.mostly_clear);
                }
            } else if (cloud > MOSTLY_SUNNY_RANGE && cloud <= PARTLY_RANGE) {
                if (isDay) {
                    weatherCondition.setCondition(PARTLY_SUNNY);
                    weatherCondition.setResourceId(R.drawable.sunny);
                } else {
                    weatherCondition.setCondition(PARTLY_CLOUDY);
                    weatherCondition.setResourceId(R.drawable.partly_cloudy);
                }
            } else if (cloud > PARTLY_RANGE && cloud <= MOSTLY_CLOUDY_RANGE) {
                weatherCondition.setCondition(MOSTLY_CLOUDY);
                weatherCondition.setResourceId(R.drawable.mostly_cloudy);
            } else if (cloud > MOSTLY_CLOUDY_RANGE) {
                weatherCondition.setCondition(CLOUDY);
                weatherCondition.setResourceId(R.drawable.cloudy);
            }
        } else /*if(rain>0&& cloud==0)*/ {
            if (rain <= VERY_LIGHT_RAIN_RANGE) {
                weatherCondition.setCondition(VERY_LIGHT_RAIN);
                weatherCondition.setResourceId(R.drawable.light_rain);
            } else if (rain > VERY_LIGHT_RAIN_RANGE && rain <= LIGHT_RAIN_RANGE) {
                weatherCondition.setCondition(LIGHT_RAIN);
                weatherCondition.setResourceId(R.drawable.light_freezing_rain);
            } else if (rain > LIGHT_RAIN_RANGE && rain <= MODERATE_RANGE) {
                weatherCondition.setCondition(MODERATE_RAIN);
                weatherCondition.setResourceId(R.drawable.rain2);
            } else if (rain > MODERATE_RANGE && rain <= RATHER_HEAVY_RAIN_RANGE) {
                weatherCondition.setCondition(RATHER_HEAVY_RAIN);
                weatherCondition.setResourceId(R.drawable.heavy_rain);
            } else if (rain > RATHER_HEAVY_RAIN_RANGE && rain <= HEAVY_RAIN_RANGE) {
                weatherCondition.setCondition(HEAVY_RAIN);
                weatherCondition.setResourceId(R.drawable.heavy_rain);
            } else if (rain > HEAVY_RAIN_RANGE && rain <= VERY_HEAVY_RAIN_RANGE) {
                weatherCondition.setCondition(VERY_HEAVY_RAIN);
                weatherCondition.setResourceId(R.drawable.heavy_rain);
            } else if (rain > VERY_HEAVY_RAIN_RANGE) {
                weatherCondition.setCondition(EXTREMELY_HEAVY_RAIN);
                weatherCondition.setResourceId(R.drawable.heavy_rain);
            }
        }
        return weatherCondition;
    }

    public static int getPrecipitationResourceId(String prec) {
        int id;
        int caseValue = 0;


        int precipitation = (int) Float.parseFloat(prec);
        if (precipitation <= 0) {
            caseValue = 0;
        } else if (precipitation > 0 && precipitation <= 25) {
            caseValue = 1;
        } else if (precipitation > 25 && precipitation <= 75) {
            caseValue = 2;
        } else if (precipitation > 75 && precipitation < 100) {
            caseValue = 3;
        } else if (precipitation >= 100) {
            caseValue = 4;
        }

        switch (caseValue) {
            case 0:
                id = R.drawable.precipi_0;
                break;
            case 1:
                id = R.drawable.precipi_10;
                break;
            case 2:
                id = R.drawable.precipi_50;
                break;
            case 3:
                id = R.drawable.precipi_90;
                break;
            case 4:
                id = R.drawable.precipi_100;
                break;
            default:
                id = R.drawable.precipi_0;
                break;
        }
        return id;
    }

    public static int getPollenColor(int pollenTotalPercentage) {

        if (pollenTotalPercentage <= 0) {
            return R.color.green_without_opacity;
        } else if (pollenTotalPercentage > 0 && pollenTotalPercentage <= 25) {
            return R.color.green_without_opacity;
        } else if (pollenTotalPercentage > 25 && pollenTotalPercentage <= 50) {
            return R.color.green_without_opacity;
        } else if (pollenTotalPercentage > 50 && pollenTotalPercentage <= 75) {
            return R.color.green_without_opacity;
        } else if (pollenTotalPercentage > 75 && pollenTotalPercentage < 100) {
            return R.color.red_without_opacity;
        } else if (pollenTotalPercentage >= 100) {
            return R.color.red_without_opacity;
        } else {
            return R.color.green_without_opacity;
        }
    }

    public static int getPollenColor(Context context, int pollenTotalPercentage) {

        if (pollenTotalPercentage <= 0) {
            return context.getResources().getColor(R.color.green_without_opacity);
        } else if (pollenTotalPercentage > 0 && pollenTotalPercentage <= 25) {
            return context.getResources().getColor(R.color.green_without_opacity);
        } else if (pollenTotalPercentage > 25 && pollenTotalPercentage <= 50) {
            return context.getResources().getColor(R.color.orange_without_opacity);
        } else if (pollenTotalPercentage > 50 && pollenTotalPercentage <= 75) {
            return context.getResources().getColor(R.color.orange_without_opacity);
        } else if (pollenTotalPercentage > 75 && pollenTotalPercentage < 100) {
            return context.getResources().getColor(R.color.red_without_opacity);
        } else if (pollenTotalPercentage >= 100) {
            return context.getResources().getColor(R.color.red_without_opacity);
        } else {
            return context.getResources().getColor(R.color.green_without_opacity);
        }
    }

    public static int getPollenImageResource(int pollenTotalPercentage) {
        if (pollenTotalPercentage <= 0) {
            return R.drawable.ic_action_check;
        } else if (pollenTotalPercentage > 0 && pollenTotalPercentage <= 25) {
            return R.drawable.ic_action_check;
        } else if (pollenTotalPercentage > 25 && pollenTotalPercentage <= 50) {
            return R.drawable.ic_action_warning;
        } else if (pollenTotalPercentage > 50 && pollenTotalPercentage <= 75) {
            return R.drawable.ic_action_warning;
        } else if (pollenTotalPercentage > 75 && pollenTotalPercentage < 100) {
            return R.drawable.ic_action_close;
        } else if (pollenTotalPercentage >= 100) {
            return R.drawable.ic_action_close;
        } else {
            return R.drawable.ic_action_check;
        }
    }

    public static int getPollenImageBackground(int pollenTotalPercentage) {
        if (pollenTotalPercentage <= 0) {
            return R.drawable.circle_background;
        } else if (pollenTotalPercentage > 0 && pollenTotalPercentage <= 25) {
            return R.drawable.circle_background;
        } else if (pollenTotalPercentage > 25 && pollenTotalPercentage <= 50) {
            return R.drawable.circle_backgroundorange;
        } else if (pollenTotalPercentage > 50 && pollenTotalPercentage <= 75) {
            return R.drawable.circle_backgroundorange;
        } else if (pollenTotalPercentage > 75 && pollenTotalPercentage < 100) {
            return R.drawable.circle_backgroundred;
        } else if (pollenTotalPercentage >= 100) {
            return R.drawable.circle_backgroundred;
        } else {
            return R.drawable.circle_background;
        }
    }

    public static int getThemeColor(Context context) {
        return isDay ? context.getResources().getColor(R.color.dayoverlay) : context.getResources().getColor(R.color.nightoverlay);
    }

/*
    public static String getRainPercentage(float value) {

        String rainPercentage
        if (value < 0.1) {
            cloudyObject.setCondition(SUNNY);
            cloudyObject.setResourceId(R.drawable.sunny);
        } else if (value > 12.5 && value <= 37.5) {
            cloudyObject.setCondition(MOSTLY_SUNNY);
            cloudyObject.setResourceId(R.drawable.mostly_sunny);
        } else if (value > 37.5 && value <= 62.5) {
            cloudyObject.setCondition(PARTLY_SUNNY);
            cloudyObject.setResourceId(R.drawable.mostly_clear);
        } else if (value > 62.5 && value <= 87.5) {
            cloudyObject.setCondition(MOSTLY_CLOUDY);
            cloudyObject.setResourceId(R.drawable.mostly_cloudy);
        } else if (value > 87.5) {
            cloudyObject.setCondition(CLOUDY);
            cloudyObject.setResourceId(R.drawable.cloudy);
        }
    }
*/


}
