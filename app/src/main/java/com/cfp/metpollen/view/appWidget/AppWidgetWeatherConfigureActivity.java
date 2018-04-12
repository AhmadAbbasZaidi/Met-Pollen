package com.cfp.metpollen.view.appWidget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.cfp.metpollen.R;
import com.cfp.metpollen.view.adapters.WidgetColorSelectorRecyclerAdapter;
import com.cfp.metpollen.view.customViews.CircleImageView;
import com.cfp.metpollen.view.interfaces.ColorSelectorListener;

import java.util.ArrayList;
import java.util.List;

/**
 * The configuration screen for the {@link AppWidgetWeather AppWidgetWeather} AppWidget.
 */
public class AppWidgetWeatherConfigureActivity extends Activity implements ColorSelectorListener {

    private static final String PREFS_NAME = "com.lmkt.weather.view.appWidget.AppWidgetWeather";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    private static int selectedBackgroundColor;
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = AppWidgetWeatherConfigureActivity.this;

            // When the button is clicked, store the string locally
            saveBackgroundColorPref(context, mAppWidgetId, selectedBackgroundColor);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            AppWidgetWeather.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };
    private CircleImageView selectedColorView;

    public AppWidgetWeatherConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveBackgroundColorPref(Context context, int appWidgetId, int color) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(PREF_PREFIX_KEY + appWidgetId, color);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static int loadBackgroundColorPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        int color = prefs.getInt(PREF_PREFIX_KEY + appWidgetId, 0);
        return color;
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);
        setContentView(R.layout.app_widget_weather_configure);

        RecyclerView recyclerViewColor = (RecyclerView) findViewById(R.id.recyclerViewColor);
        selectedColorView = (CircleImageView) findViewById(R.id.selectedColor);
        setDefaultSelectedColor();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
//        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewColor.setLayoutManager(gridLayoutManager);
        WidgetColorSelectorRecyclerAdapter adapter = new WidgetColorSelectorRecyclerAdapter(this, R.layout.recycler_item_color, getColors(), this);
        recyclerViewColor.setAdapter(adapter);

        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
    }

    private void setDefaultSelectedColor() {
        Bitmap bmp = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(getColors().get(0), PorterDuff.Mode.SRC);
        selectedColorView.setImageBitmap(bmp);
        selectedColorView.setBackgroundColor(getResources().getColor(R.color.overlay));
        selectedBackgroundColor = getColors().get(0);
    }

    private List<Integer> getColors() {
        List<Integer> colors = new ArrayList<>();
        int[] rainbow = getResources().getIntArray(R.array.rainbow);
        for (int color : rainbow) {
            colors.add(color);
        }
        return colors;
    }

    @Override
    public void onColorSelected(int selectedColor) {
        Bitmap bmp = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(selectedColor, PorterDuff.Mode.SRC);
        selectedColorView.setImageBitmap(bmp);
        selectedColorView.setBackgroundColor(getResources().getColor(R.color.overlay));
        selectedBackgroundColor = selectedColor;

    }
}

