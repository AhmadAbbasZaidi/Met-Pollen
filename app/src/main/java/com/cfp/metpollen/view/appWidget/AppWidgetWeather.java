package com.cfp.metpollen.view.appWidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.cfp.metpollen.R;
import com.cfp.metpollen.data.db.DbUtils;
import com.cfp.metpollen.data.db.model.CurrentModel;
import com.cfp.metpollen.view.objectModels.WeatherCondition;
import com.cfp.metpollen.view.utilities.TempConditionUtils;
import com.cfp.metpollen.view.utilities.utils;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static java.sql.Types.NULL;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link AppWidgetWeatherConfigureActivity AppWidgetWeatherConfigureActivity}
 */
public class AppWidgetWeather extends AppWidgetProvider {


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        int color = AppWidgetWeatherConfigureActivity.loadBackgroundColorPref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget_weather);
//        views.setTextViewText(R.id., color);
        if (utils.canGetLocation(context)) {
            views.setImageViewResource(R.id.loc, R.drawable.ic_action_my_location);
        } else {
            views.setImageViewResource(R.id.loc, R.drawable.ic_action_location_disabled);
        }

        SharedPreferences spref = context.getSharedPreferences("USER", MODE_PRIVATE);
        if (spref.contains("isCurrent")) {

            views.setViewVisibility(R.id.ll, VISIBLE);
            views.setViewVisibility(R.id.noLoc, GONE);
            Log.i("ApiMultiRun1 = ", "0");
            Boolean isCurrent = spref.getBoolean("isCurrent", false);
            String locName = "";
            if (isCurrent) {
                if (spref.contains("City")) {
                    locName = (spref.getString("City", ""));
                } else {
                    locName = ("");
                }
            } else {
                if (spref.contains("StationCity")) {
                    locName = (spref.getString("StationCity", ""));
                } else {
                    locName = ("");
                }
            }
            String[] splitloc = locName.split(" ");
            views.setTextViewText(R.id.location, splitloc[splitloc.length - 1]);
            CurrentModel currentModel = DbUtils.getCurrent();
            if (currentModel != null) {
                Log.i("CurrentModel = ", "not null");
                views.setTextViewText(R.id.temperatureWidget, utils.getFormattedText(currentModel.getTemperature()) + "°");
                views.setTextViewText(R.id.tempMinWidget, utils.getFormattedText(currentModel.getMinTemperature()) + "°");
                views.setTextViewText(R.id.tempMaxWidget, utils.getFormattedText(currentModel.getMaxTemperature()) + "°");
                String prec = currentModel.getPrecipitation().equalsIgnoreCase("--")?"--":(int)Float.parseFloat(currentModel.getPrecipitation()) < 10 ? "0" + (int)Float.parseFloat(currentModel.getPrecipitation()) : (int)Float.parseFloat(currentModel.getPrecipitation()) + "";
                views.setTextViewText(R.id.chanceOfRain, prec + "mm");
                views.setTextViewText(R.id.time,utils.getConvertedDateFromOneFormatToOther(currentModel.getEntryTime(),utils.FORMAT1,utils.FORMAT5));

                WeatherCondition weatherConditionModel = TempConditionUtils.getConditionCurrent(currentModel.getPrecipitation(), currentModel.getCloud());
                if (weatherConditionModel != null) {
                    views.setTextViewText(R.id.weatherCondition, weatherConditionModel.getCondition());
                    views.setImageViewResource(R.id.conditionImage, weatherConditionModel.getResourceId());
                } else {
//                    views.setTextViewText(R.id.weatherCondition, "--");
//                    views.setImageViewResource(R.id.conditionImage, NULL);
                }
                updatePollenProgress(views, currentModel);
            } else {
                Log.i("CurrentModel = ", "is null");
                if (utils.canGetLocation(context)) {
                    views.setImageViewResource(R.id.loc, R.drawable.ic_action_my_location);
                } else {
                    views.setImageViewResource(R.id.loc, R.drawable.ic_action_location_disabled);
                }
                views.setTextViewText(R.id.location, "--");
                views.setTextViewText(R.id.temperature, "--");
                views.setTextViewText(R.id.tempMin, "--");
                views.setTextViewText(R.id.tempMax, "--");
                views.setTextViewText(R.id.weatherCondition, "--");
                views.setImageViewResource(R.id.conditionImage, NULL);
                updatePollenProgress(views, null);
            }
        } else {
            Log.i("CurrentModel = ", "null");
            views.setViewVisibility(R.id.ll, GONE);
            views.setViewVisibility(R.id.noLoc, VISIBLE);
        }


        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.setComponent(new ComponentName(context.getPackageName(),
                "com.lmkt.weather.view.activities.SplashActivity"));
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.rl, pendingIntent);

        views.setInt(R.id.background, "setColorFilter", color);
//        views.setImageViewBitmap(R.id.mainView, getBitmap(context.getResources().getDrawable(R.drawable.widget_background), color));
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static void updatePollenProgress(RemoteViews views, CurrentModel currentModel) {
        if (currentModel != null && views != null) {
            views.setViewVisibility(R.id.ll, VISIBLE);
            views.setViewVisibility(R.id.noLoc, GONE);
            if (currentModel.getPollenStation() != null && currentModel.getPollenStation().length() > 0 && !TextUtils.equals(currentModel.getPollenStation(), "--")) {
                views.setProgressBar(R.id.progressBar, 100, 0, false);
                views.setTextViewText(R.id.pollen_status, currentModel.getPollenTotalStatus());
                if (!currentModel.getPollenTotalPercentage().equalsIgnoreCase("--")) {
//                circleProgressBar.setColor(TempConditionUtils.getPollenColor(getActivity(), Integer.parseInt(currentModel.getPollenTotalPercentage())));
//        pollenChecked.getBackground().setColorFilter(TempConditionUtils.getPollenColor((int) currentModel.getPollenTotalPercentage()), PorterDuff.Mode.SRC);
//                pollenChecked.setBackground(getResources().getDrawable(TempConditionUtils.getPollenImageBackground(Integer.parseInt(currentModel.getPollenTotalPercentage()))));
//                pollenChecked.setImageResource(TempConditionUtils.getPollenImageResource(Integer.parseInt(currentModel.getPollenTotalPercentage())));
                    views.setProgressBar(R.id.progressBar, 100, (int) Float.parseFloat(currentModel.getPollenTotalPercentage()), false);
                    views.setTextViewText(R.id.progressText, utils.getFormattedText(currentModel.getPollenTotalPercentage()) + "%");
                    views.setViewVisibility(R.id.progressBar, VISIBLE);
                    views.setViewVisibility(R.id.pollenChecked, VISIBLE);
                    views.setViewVisibility(R.id.progressText, VISIBLE);
                    views.setViewVisibility(R.id.ll2, VISIBLE);
                    views.setViewVisibility(R.id.image, VISIBLE);
                    views.setViewVisibility(R.id.textViewPollen, VISIBLE);
                    views.setViewVisibility(R.id.textViewCount, VISIBLE);
                    views.setTextViewText(R.id.textViewCount, " Count");
                    views.setImageViewResource(R.id.image, R.drawable.pollen);
                    views.setImageViewResource(R.id.pollenChecked, TempConditionUtils.getPollenImageResource((int) Float.parseFloat(currentModel.getPollenTotalPercentage())));
//                pollenImage.setImageResource(R.drawable.pollen);
                } else {
                    views.setTextViewText(R.id.progressText, utils.getFormattedText(currentModel.getPollenTotalPercentage()) + "%");
                    views.setViewVisibility(R.id.progressBar, INVISIBLE);
                    views.setViewVisibility(R.id.pollenChecked, INVISIBLE);
                    views.setViewVisibility(R.id.progressText, INVISIBLE);
                    views.setViewVisibility(R.id.ll2, INVISIBLE);
                    views.setViewVisibility(R.id.image, INVISIBLE);
                    views.setViewVisibility(R.id.textViewPollen, INVISIBLE);
                    views.setViewVisibility(R.id.textViewCount, INVISIBLE);
                    views.setTextViewText(R.id.textViewCount, "");
                }
            } else {
                views.setProgressBar(R.id.progressBar, 100, 0, false);
                views.setTextViewText(R.id.pollen_status, currentModel.getPollenTotalStatus());
                views.setViewVisibility(R.id.textViewPollen, INVISIBLE);
                if (!currentModel.getRelativeHumidity().equalsIgnoreCase("--")) {
                    views.setProgressBar(R.id.progressBar, 100, (int) Float.parseFloat(currentModel.getRelativeHumidity()), false);
                    //                circleProgressBar.setColor(TempConditionUtils.getPollenColor(getActivity(), Integer.parseInt(currentModel.getPollenTotalPercentage())));
//        pollenChecked.getBackground().setColorFilter(TempConditionUtils.getPollenColor((int) currentModel.getPollenTotalPercentage()), PorterDuff.Mode.SRC);
//                pollenChecked.setBackground(getResources().getDrawable(TempConditionUtils.getPollenImageBackground(Integer.parseInt(currentModel.getPollenTotalPercentage()))));
//                pollenChecked.setImageResource(TempConditionUtils.getPollenImageResource(Integer.parseInt(currentModel.getPollenTotalPercentage())));
                    views.setTextViewText(R.id.progressText, utils.getFormattedText(currentModel.getRelativeHumidity()) + "%");
                    views.setViewVisibility(R.id.progressBar, VISIBLE);
                    views.setViewVisibility(R.id.pollenChecked, VISIBLE);
                    views.setViewVisibility(R.id.progressText, VISIBLE);
                    views.setViewVisibility(R.id.ll2, VISIBLE);
                    views.setViewVisibility(R.id.image, VISIBLE);
                    views.setViewVisibility(R.id.textViewCount, VISIBLE);
                    views.setTextViewText(R.id.pollen_status, "");
                    views.setTextViewText(R.id.textViewCount, "Humidity");
                    views.setImageViewResource(R.id.image, R.drawable.humidity);
                    views.setImageViewResource(R.id.pollenChecked, TempConditionUtils.getPollenImageResource(0));
                } else {
                    views.setTextViewText(R.id.progressText, utils.getFormattedText(currentModel.getPollenTotalPercentage()) + "%");
                    views.setViewVisibility(R.id.progressBar, INVISIBLE);
                    views.setViewVisibility(R.id.pollenChecked, INVISIBLE);
                    views.setViewVisibility(R.id.progressText, INVISIBLE);
                    views.setViewVisibility(R.id.ll2, INVISIBLE);
                    views.setViewVisibility(R.id.image, INVISIBLE);
                    views.setViewVisibility(R.id.textViewPollen, INVISIBLE);
                    views.setViewVisibility(R.id.textViewCount, INVISIBLE);
                    views.setTextViewText(R.id.textViewCount, "");
                }
            }
        } else {
            views.setViewVisibility(R.id.ll, GONE);
            views.setViewVisibility(R.id.noLoc, VISIBLE);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Log.i("Context", context.getPackageName());

        Log.i("CurrentModel = ", "abc");
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            AppWidgetWeatherConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent arg1) {
        Log.i("CurrentModel = ", "def");
        utils.updateWidget();
    }
}

