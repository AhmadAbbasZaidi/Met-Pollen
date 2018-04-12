package com.cfp.metpollen.view.utilities;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.cfp.metpollen.App;
import com.cfp.metpollen.R;
import com.cfp.metpollen.view.appWidget.AppWidgetWeather;
import com.cfp.metpollen.view.dialog.LocationAlertDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.activeandroid.Cache.getContext;

/**
 * Created by AhmedAbbas on 11/28/2017.
 */

public class utils {
    public static final String HOUR = "HH";
    public static final String MIN = "mm";
    public static final String DAY = "dd";
    public static final String MONTH = "MM";
    public static final String YEAR = "yyyy";
    public static final String FORMAT1 = "yyyy-MM-dd HH:mm:ss.S";
    public static final String FORMAT2 = "yyyy-MM-dd HH:mm";
    public static final String FORMAT3 = "yyyy-MM-dd";
    public static final String FORMAT4 = "EEE MMM dd HH:mm:ss zzz yyyy";
    public static final String FORMAT5 = "hh:mm a";
    public static final String FORMAT6 = "HH:mm";
    public static final String FORMAT7 = "HH";
    public static final String FORMAT8 = "EEE";

    public static String getTemperatureDateTime(String date) {


        Calendar calendar = Calendar.getInstance();


        String[] datetime = date.split(" ");
        String[] ymd = datetime[0].split("-");
        String[] hms = datetime[1].split(":");
        Date currentdate = calendar.getTime();
        int year = Integer.parseInt(ymd[0]) - 1900;
        int month = Integer.parseInt(ymd[1]) - 1;
        int day = Integer.parseInt(ymd[2]);
        int hour = Integer.parseInt(hms[0]);
        if (currentdate.getYear() == year && currentdate.getMonth() == month && currentdate.getDate() == day) {
            return "TODAY";
        }
        Date newdate = new Date();
        newdate.setYear(year);
        newdate.setMonth(month);
        newdate.setDate(day);
        newdate.setHours(hour);
        Log.i("date1 ", String.valueOf(newdate.getTime()));
        calendar.setTime(newdate);
        Log.i("date2 ", String.valueOf(calendar.getTime()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh a MMM dd");
        return dateFormat.format(newdate);
    }

    public static String getTemperatureDate(String date) {


        Calendar calendar = Calendar.getInstance();


        String[] datetime = date.split(" ");
        String[] ymd = datetime[0].split("-");
        String[] hms = datetime[1].split(":");
        Date currentdate = calendar.getTime();
        int year = Integer.parseInt(ymd[0]) - 1900;
        int month = Integer.parseInt(ymd[1]) - 1;
        int day = Integer.parseInt(ymd[2]);
        int hour = Integer.parseInt(hms[0]);
        if (currentdate.getYear() == year && currentdate.getMonth() == month && currentdate.getDate() == day) {
            return "TODAY";
        }
        Date newdate = new Date();
        newdate.setYear(year);
        newdate.setMonth(month);
        newdate.setDate(day);
        newdate.setHours(hour);
        Log.i("date1 ", String.valueOf(newdate.getTime()));
        calendar.setTime(newdate);
        Log.i("date2 ", String.valueOf(calendar.getTime()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd");
        return dateFormat.format(newdate);
    }

    public static String getCurrentTimeStampNoSeconds() {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return dateFormat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static String getCurrentTimeOfDay() {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            return dateFormat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static String getCurrentTimeOnlyWithSeconds() {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            return dateFormat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static String getCurrentDate() {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static String getFormattedDateRiseSet(String date) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date c = null;
            c = sdf.parse(date);
            String date1 = sdf2.format(c);
//            System.out.println(date1);
            return date1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String getConvertedDateFromOneFormatToOther(String date, String currentFormat, String requiredFormat) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat(currentFormat);
            SimpleDateFormat sdf2 = new SimpleDateFormat(requiredFormat);
            Date c = null;
            c = sdf.parse(date);
            String date1 = sdf2.format(c);
//            System.out.println(date1);
            return date1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String getFormattedDateRiseOrSet(String date) {

        String[] dateArray = date.split(" ");
        dateArray[4] = "PST";
        String finaldate = null;
        for (String d : dateArray) {
            finaldate = finaldate == null ? d : finaldate + " " + d;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
            Date c = null;
            c = sdf.parse(finaldate);
            String date1 = sdf2.format(c);
//            System.out.println(date1);
            return date1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String getCurrentTimeStamp(String outputFormat) {
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat(outputFormat);

            return dateFormat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public static int getHourOfDay() {

        Calendar rightNow = Calendar.getInstance();
        int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
        Log.i("current Hour", " = " + currentHour);
        return currentHour;
    }

    public static String getRequiredTimeField(String reqiuredField, String date) {
        String field = "";
        //        2017-10-19 03:00:00


        String[] datetime = date.split(" ");
        String[] dateOnly = datetime[0].split("-");
        String[] timeOnly = datetime[1].split(":");

        switch (reqiuredField) {
            case HOUR:
                return timeOnly[0];
            case MIN:
                return timeOnly[1];
            case DAY:
                return dateOnly[2];
            case MONTH:
                return dateOnly[1];
            case YEAR:
                return dateOnly[0];
            default:
                return field;
        }
    }

    public static String getHeaderDate(String date) {

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

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd");
        return dateFormat.format(newdate);
    }

    public static String getHourInTwelveHourFormat(String date) {

        Calendar calendar = Calendar.getInstance();

        String[] datetime = date.split(" ");
        String[] ymd = datetime[0].split("-");
        String[] hms = datetime[1].split(":");
        Date currentdate = calendar.getTime();
        int year = Integer.parseInt(ymd[0]) - 1900;
        int month = Integer.parseInt(ymd[1]) - 1;
        int day = Integer.parseInt(ymd[2]);
        int hour = Integer.parseInt(hms[0]);
        if (currentdate.getYear() == year && currentdate.getMonth() == month && currentdate.getDate() == day) {
            return "TODAY";
        }
        Date newdate = new Date();
        newdate.setYear(year);
        newdate.setMonth(month);
        newdate.setDate(day);
        newdate.setHours(hour);
        Log.i("date1 ", String.valueOf(newdate.getTime()));
        calendar.setTime(newdate);
        Log.i("date2 ", String.valueOf(calendar.getTime()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh a");
        return dateFormat.format(newdate);
    }

    public static boolean checkNetworkState(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static boolean checkLocationState() {
        ContentResolver contentResolver = getContext().getContentResolver();
        // Find out what the settings say about which providers are enabled
        int mode = Settings.Secure.getInt(
                contentResolver, Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_OFF);

        if (mode == Settings.Secure.LOCATION_MODE_OFF) {
            // Location is turned OFF!
            return false;
        } else {
            // Location is turned ON!
            return true;
        }
    }

    public static boolean canGetLocation(Context context) {
        boolean result = true;
        LocationManager lm = null;
        boolean gpsEnabled = false;
        boolean networkEnabled = false;
        if (lm == null)

            lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // exceptions will be thrown if provider is not permitted.
        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {

        }
        try {
            networkEnabled = lm
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        if (gpsEnabled == false || networkEnabled == false) {
            result = false;
        } else {
            result = true;
        }

        return result;
    }

    public static void showLocationSettingsAlert(final AppCompatActivity context) {
        LocationAlertDialogFragment fragment = LocationAlertDialogFragment.getInstance();
        fragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Dialog_NoTitle);
        fragment.show(context.getSupportFragmentManager(), "Location Enabler Fragment");
    }

    public static void showInternetSettingsAlert(final AppCompatActivity context) {
        LocationAlertDialogFragment fragment = LocationAlertDialogFragment.getInstance();
        fragment.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Dialog_NoTitle);
        fragment.show(context.getSupportFragmentManager(), "Wifi Enabler Fragment");
    }

    public static int getCurrentMinOfDay() {

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            Date date = new Date();
            String hrsMin = dateFormat.format(date);
            String[] str = hrsMin.split(":");
            int hrs = Integer.parseInt(str[0]);
            int min = Integer.parseInt(str[1]);
            return hrs * 60 + min;
        } catch (Exception e) {
            e.printStackTrace();

            return 0;
        }
    }

    public static void checkTimeSetting(Context context) {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        int timeSetting;
        int timeZoneSetting;
        if (currentapiVersion < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            timeSetting = android.provider.Settings.System.getInt(context.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0);
            timeZoneSetting = android.provider.Settings.System.getInt(context.getContentResolver(), Settings.System.AUTO_TIME_ZONE, 0);
        } else {
            timeSetting = android.provider.Settings.System.getInt(context.getContentResolver(), android.provider.Settings.Global.AUTO_TIME, 0);
            timeZoneSetting = android.provider.Settings.System.getInt(context.getContentResolver(), Settings.Global.AUTO_TIME_ZONE, 0);
        }

        if (timeSetting == 0) {
            makeAlertDialog("Info", "Set your Date & time to Automatic in your mobile settings", context);
            return;
        } else if (timeZoneSetting == 0) {
            makeAlertDialog("Info", "Set your time zone to Automatic in your mobile settings", context);
            return;
        }
    }

    private static void makeAlertDialog(String info, String s, final Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialogBuilder.setTitle(info);

        // Setting Dialog Message
        alertDialogBuilder.setMessage(s);
        AlertDialog alertDialog = alertDialogBuilder.create();
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // do work
                        Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
                        context.startActivity(intent);
                        break;
                    case DialogInterface.BUTTON_NEUTRAL:
                        // do work
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        // do work
                        break;
                    default:
                        break;
                }
            }
        };
        // On pressing Ok button
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Enable", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(
                        Settings.ACTION_DATE_SETTINGS);
                context.startActivity(intent);
            }
        });

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", listener);
//        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", listener);
//        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Cancel",

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                AlertDialog alertDialog = (AlertDialog) dialog;
                Button button = alertDialog.getButton(dialog.BUTTON_POSITIVE);
                button.setTextColor(context.getResources().getColor(R.color.black));
            }
        });


        alertDialog.show();
    }

    public static String getFormattedText(String text) {
        return !text.equalsIgnoreCase("--") ? (((int) Float.parseFloat(text)) < 10 && ((int) Float.parseFloat(text)) >0 ? "0" + ((int) Float.parseFloat(text)) : ((int) Float.parseFloat(text)) + "") : text;
    }

    public static void updateWidget() {
        int[] ids = AppWidgetManager.getInstance(App.getInstance()).getAppWidgetIds(new ComponentName(App.getInstance(), AppWidgetWeather.class));
        AppWidgetWeather myWidget = new AppWidgetWeather();
        myWidget.onUpdate(App.getInstance(), AppWidgetManager.getInstance(App.getInstance()), ids);
    }

    public void showSettingsAlert(final Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("Error!");

        // Setting Dialog Message
        alertDialog.setMessage("Please ");

        // On pressing Settings button
        alertDialog.setPositiveButton(
                context.getResources().getString(R.string.button_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(intent);
                    }
                });

        alertDialog.show();
    }
}
