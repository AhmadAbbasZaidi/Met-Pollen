package com.cfp.metpollen.view.fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cfp.metpollen.R;
import com.cfp.metpollen.data.db.DbUtils;
import com.cfp.metpollen.data.db.model.CurrentModel;
import com.cfp.metpollen.data.db.model.ForecastModel;
import com.cfp.metpollen.data.db.model.PollenModel;
import com.cfp.metpollen.presenter.ApiResponseListener;
import com.cfp.metpollen.view.activities.MainActivity;
import com.cfp.metpollen.view.customAnimations.ArcAngleAnimation;
import com.cfp.metpollen.view.customViews.ArcView;
import com.cfp.metpollen.view.customViews.CircleProgressBar;
import com.cfp.metpollen.view.customViews.HeatMapGraphView;
import com.cfp.metpollen.view.objectModels.WeatherCondition;
import com.cfp.metpollen.view.utilities.SunRiseSETUtils;
import com.cfp.metpollen.view.utilities.TempConditionUtils;
import com.cfp.metpollen.view.utilities.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodayFragmentWithoutList extends BaseFragment implements ApiResponseListener {

    private static TodayFragmentWithoutList instance;
    private static int blurIndex = 0;
    private static CurrentModel currentModel;
    private static Timer task;
    private ApiResponseListener apiResponseListener;
    private boolean mHasBeenVisible = true;
    private LinearLayout linearLayoutPollen;
    private View pollenItem;
    private TextView progressText,progressTextItem;
    private CircleProgressBar circleProgressBar, circleProgressBarPollen;
    private TextView textViewPollen, textViewCount;
    private ImageView pollenImage;
    private TextView temperature;
    private TextView temperatureMin, temperatureMax;
    private TextView weatherCondition;
    private ImageView weatherImage;
    private TextView feelsLike;
    private TextView visibility;
    private TextView windspeed;
    private TextView winddirection;
    private TextView humidity;
    private TextView dewPoint;
    private TextView precipitationNow, precipitationNoon, precipitationEvening, precipitationTonight;
    private TextView windSpeedMills, pressureSpeedMills;
    private ArcView arcView;
    private TextView pollenArea, pollenTotalCount, pollenTotalStatus;
    private ImageView precipitationNowImage, precipitationNoonImage, precipitationEveningImage, precipitationTonightImage;
    private HeatMapGraphView heatMapGraphView;
    private ImageView pollenChecked, pollenCheckedPollen;
    private TextView pollenStatus,pollenStatusItem;
    private ScrollView scrollView;
    private LinearLayout linearLayoutPollenData;
    private SwipeRefreshLayout swipeLayout;
    private boolean arcViewRefreshed = false,circleProgressBarPollenRefreshed = false,circleProgressBarRefreshed = false;

    public TodayFragmentWithoutList() {
        // Required empty public constructor
    }

    public static TodayFragmentWithoutList getInstance() {
        if (instance == null) {
            instance = new TodayFragmentWithoutList();
            currentModel = DbUtils.getCurrent();
        }
        return instance;
    }

    public int getBlurIndex() {
        return blurIndex;
    }

    public void setBlurIndex(int blurIndex) {
        this.blurIndex = blurIndex;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_today_fragment_without_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiResponseListener = this;
        heatMapGraphView = view.findViewById(R.id.heatmapgraph);
        heatMapGraphView.setDrawingCacheEnabled(false);

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (utils.checkNetworkState(getActivity())) {
                            ((MainActivity) getActivity()).updateCurrent();
                        } else {
                            swipeLayout.setRefreshing(false);
                            Toast.makeText(getActivity(), "Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 2000);

            }
        });
        swipeLayout.setColorScheme(R.color.blueoverlayOpaque);
        /*swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);*/


        View view1 = view.findViewById(R.id.view1);
        ViewGroup.LayoutParams params = view1.getLayoutParams();
        params.height = (int) ((getResources().getDisplayMetrics().heightPixels / getResources().getDisplayMetrics().density) / 2);
        view1.setLayoutParams(params);
        View view2 = view.findViewById(R.id.view2);
        ViewGroup.LayoutParams params2 = view1.getLayoutParams();
        params2.height = (int) ((getResources().getDisplayMetrics().heightPixels / getResources().getDisplayMetrics().density) / 2);
        view2.setLayoutParams(params);

        /*Item First*/
        circleProgressBar = (CircleProgressBar) view.findViewById(R.id.circleProgressBar);
        pollenChecked = (ImageView) view.findViewById(R.id.pollenChecked);
        pollenImage = (ImageView) view.findViewById(R.id.image);
        pollenStatus = (TextView) view.findViewById(R.id.pollen_status);
        progressText = (TextView) view.findViewById(R.id.progressText);
        textViewPollen = (TextView) view.findViewById(R.id.textViewPollen);
        textViewCount = (TextView) view.findViewById(R.id.textViewCount);
        temperature = (TextView) view.findViewById(R.id.temperature);
        temperatureMin = (TextView) view.findViewById(R.id.tempMin);
        temperatureMax = (TextView) view.findViewById(R.id.tempMax);
        weatherCondition = (TextView) view.findViewById(R.id.weatherCondition);
        weatherImage = (ImageView) view.findViewById(R.id.conditionImage);

        /*Item Second*/
        feelsLike = (TextView) view.findViewById(R.id.feelsLike);
        windspeed = (TextView) view.findViewById(R.id.windspeed);
        winddirection = (TextView) view.findViewById(R.id.windDirection);
        humidity = (TextView) view.findViewById(R.id.humidity);
        dewPoint = (TextView) view.findViewById(R.id.dewPoint);
        visibility = (TextView) view.findViewById(R.id.visibility);

         /*Item Third*/
        precipitationNow = (TextView) view.findViewById(R.id.now);
        precipitationNowImage = (ImageView) view.findViewById(R.id.nowImage);
        precipitationNoon = (TextView) view.findViewById(R.id.noon);
        precipitationNoonImage = (ImageView) view.findViewById(R.id.noonImage);
        precipitationEvening = (TextView) view.findViewById(R.id.evening);
        precipitationEveningImage = (ImageView) view.findViewById(R.id.eveningImage);
        precipitationTonight = (TextView) view.findViewById(R.id.tonight);
        precipitationTonightImage = (ImageView) view.findViewById(R.id.tonightImage);

         /*Item Four*/
        circleProgressBarPollen = (CircleProgressBar) view.findViewById(R.id.circleProgressBarPollen);
        pollenCheckedPollen = (ImageView) view.findViewById(R.id.pollenCheckedPollen);
        linearLayoutPollenData = view.findViewById(R.id.pollenData);
        linearLayoutPollen = view.findViewById(R.id.lLPollenData);
        pollenItem = getLayoutInflater().inflate(R.layout.pollen_item, null);
        pollenArea = (TextView) view.findViewById(R.id.pollenArea);
        pollenTotalCount = (TextView) view.findViewById(R.id.totalCountPollen);
        pollenTotalStatus = (TextView) view.findViewById(R.id.totalStatusPollen);
        pollenStatusItem = (TextView) view.findViewById(R.id.pollen_status_item);
        progressTextItem = (TextView) view.findViewById(R.id.progressTextItem);

         /*Item Five*/
        windSpeedMills = (TextView) view.findViewById(R.id.windMillSpeed);
        pressureSpeedMills = (TextView) view.findViewById(R.id.pressure);


         /*Item Six*/
        arcView = (ArcView) view.findViewById(R.id.arcView);


        ImageView fan1 = (ImageView) view.findViewById(R.id.fan1);
        ImageView fan2 = (ImageView) view.findViewById(R.id.fan2);
/*
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.circular_motion);
        anim.setFillAfter(true);
        fan1.setAnimation(anim);
        fan1.startAnimation(anim);

        Animation anim2 = AnimationUtils.loadAnimation(getActivity(), R.anim.circular_motion);
        anim2.setFillAfter(true);
        fan2.setAnimation(anim2);
        fan2.startAnimation(anim2);
*/

        ObjectAnimator animation = ObjectAnimator.ofFloat(fan1, "rotation", 0.0f, 360f);
        animation.setDuration(3500);
        animation.setRepeatMode(ValueAnimator.RESTART);
        animation.setRepeatCount(ObjectAnimator.INFINITE);
        animation.setInterpolator(new LinearInterpolator());
        animation.start();


        ObjectAnimator animation2 = ObjectAnimator.ofFloat(fan2, "rotation", 0.0f, 360f);
        animation2.setDuration(3500);
        animation2.setRepeatMode(ValueAnimator.RESTART);
        animation2.setRepeatCount(ObjectAnimator.INFINITE);
        animation2.setInterpolator(new LinearInterpolator());
        animation2.start();


        scrollView = (ScrollView) view.findViewById(R.id.scrollview);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = scrollView.getScrollY(); // For ScrollView
                if (mHasBeenVisible) {
                    setBlurIndex(scrollY);
                    ((MainActivity) getActivity()).setBackground(scrollY);
                    refreshArcView();
//                    refreshPollenProgress();
                }
//                int scrollX = scrollView.getScrollX(); // For HorizontalScrollView
                // DO SOMETHING WITH THE SCROLL COORDINATES
            }
        });
        if (currentModel != null) {
            scrollView.setVisibility(View.VISIBLE);
            heatMapGraphView.setVisibility(View.VISIBLE);
            updateBackgroundTheme();
            updateHeatMapGraphView();
            updatePollenProgress();
            updateTemperature();
            updateSecondItem();
            updateThirdItem();
            updatePollenData();
            updateFifthItem();
            updateSixthItem();
        } else {
            scrollView.setVisibility(View.GONE);
            heatMapGraphView.setVisibility(View.GONE);
        }
    }


    private void updateHeatMapGraphView() {
        Log.i("Heatmap ", "before if");
        if (DbUtils.hasForecast() && currentModel != null) {
            Log.i("Heatmap ", "first if");
            if (!currentModel.getFormattedWeatherDate().equalsIgnoreCase("--")) {
                final List<ForecastModel> forecastModelList = DbUtils.getForecastForDate(currentModel.getFormattedWeatherDate()/*,currentModel.getCurrentId()*/);
                Log.i("Heatmap ", "forecastModelList");
                if (forecastModelList != null && forecastModelList.size() > 0) {
                    Log.i("Heatmap ", "seconf if");
                    if (!currentModel.getTemperature().equalsIgnoreCase("--")) {

                        if (checkEmptyTemperature(forecastModelList)) {
                            heatMapGraphView.setVisibility(View.GONE);
                        } else {
                            heatMapGraphView.setList(forecastModelList, Integer.parseInt(currentModel.getTemperature()));
                            heatMapGraphView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        heatMapGraphView.setVisibility(View.GONE);
                    }
               /* new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        heatMapGraphView.setList(forecastModelList);
                    }
                });*/
                }
            } else {
                heatMapGraphView.setVisibility(View.GONE);
            }
        }
    }

    public boolean checkEmptyTemperature(List<ForecastModel> forecastList) {
        boolean isEmpty = false;
        for (ForecastModel model : forecastList) {
            if (!isEmpty) {
                isEmpty = model.getTemperature().equalsIgnoreCase("--") ? true : false;
            } else {
                break;
            }

        }
        return isEmpty;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (currentModel != null) {
            TempConditionUtils.setIsDay(SunRiseSETUtils.isDay(currentModel.getSunRiseHour(), currentModel.getSunRiseMin(), currentModel.getSunSetHour(), currentModel.getSunSetMin()));
            updateBackgroundTheme();
            updateHeatMapGraphView();
            updateTemperature();
        }
    }

    private void updateSixthItem() {
        if (task == null) {
            task = new Timer();
            task.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("sunRiseSet = ", "arcView.invalidate()");
                            if (currentModel != null && currentModel.getFormattedWeatherTime() != null) {
                                float arcAngle = SunRiseSETUtils.getArcAngle(utils.getConvertedDateFromOneFormatToOther(currentModel.getFormattedWeatherTime(), utils.FORMAT1, utils.FORMAT3), currentModel.getSunRiseHour(), currentModel.getSunRiseMin(), currentModel.getSunSetHour(), currentModel.getSunSetMin());
                                TempConditionUtils.setIsDay(SunRiseSETUtils.isDay(currentModel.getSunRiseHour(), currentModel.getSunRiseMin(), currentModel.getSunSetHour(), currentModel.getSunSetMin()));
//                                set background color theme
                                arcView.setArcAngle(arcAngle);
                                arcView.invalidate();
                                if (arcAngle >= 0 && arcAngle <= 180) {
                                    Log.i("isDay sunRiseSet = ", "arcView.invalidate()");
                                    updateBackgroundTheme();
                                    updateHeatMapGraphView();
                                    updateTemperature();
//                                    updateSixthItem();
                                }
                            }
                        }
                    });
                }
            }, 0, 60000/*1min*/);
        }

        // NONE of the imageView is within the visible window
        if (currentModel != null && currentModel.getFormattedWeatherTime() != null) {
            arcView.setDate(utils.getConvertedDateFromOneFormatToOther(currentModel.getFormattedWeatherTime(), utils.FORMAT1, utils.FORMAT3));
            arcView.setRiseTime(currentModel.getSunRiseHour() + ":" + currentModel.getSunRiseMin());
            arcView.setSetTime(currentModel.getSunSetHour() + ":" + currentModel.getSunSetMin());
            arcView.setArcAngle(0);
            float arcAngle = SunRiseSETUtils.getArcAngle(utils.getConvertedDateFromOneFormatToOther(currentModel.getFormattedWeatherTime(), utils.FORMAT1, utils.FORMAT3), currentModel.getSunRiseHour(), currentModel.getSunRiseMin(), currentModel.getSunSetHour(), currentModel.getSunSetMin());
//            arcView.setArcAngle(arcAngle);
//            arcView.invalidate();
            ArcAngleAnimation animation = new ArcAngleAnimation(arcView, (int) arcAngle);
            animation.setDuration(1500);
            arcView.startAnimation(animation);
        }
 /*       ArcAngleAnimation animation = new ArcAngleAnimation(arcView, arcAngle);
        animation.setDuration(1500);
        arcView.startAnimation(animation);*/
    }

    private void updateBackgroundTheme() {
        if (getActivity() != null) {
            if (((MainActivity) getActivity()).getBackgroundViewOverlayToday() != null)
                ((MainActivity) getActivity()).getBackgroundViewOverlayToday().setBackgroundColor(TempConditionUtils.getThemeColor(getActivity()));
        }
    }
    private void refreshPollenProgress() {
        Rect scrollBounds = new Rect();
        scrollView.getHitRect(scrollBounds);

        if (!circleProgressBarPollen.getLocalVisibleRect(scrollBounds) || scrollBounds.height() < circleProgressBarPollen.getHeight()) {
            // imageView is not within or only partially within the visible window
            circleProgressBarPollenRefreshed = false;
        } else {
            circleProgressBarPollen.setStrokeWidth(20);
            circleProgressBarPollen.setMin(0);
            circleProgressBarPollen.setMax(100);
            circleProgressBarPollen.setProgress(0);
            if (!currentModel.getPollenTotalPercentage().equalsIgnoreCase("--")) {
                circleProgressBarPollen.setProgressWithAnimation(Float.parseFloat(currentModel.getPollenTotalPercentage()));
                circleProgressBarPollen.setColor(TempConditionUtils.getPollenColor(getActivity(), Integer.parseInt(currentModel.getPollenTotalPercentage())));
                pollenCheckedPollen.setBackground(getResources().getDrawable(TempConditionUtils.getPollenImageBackground(Integer.parseInt(currentModel.getPollenTotalPercentage()))));
                pollenCheckedPollen.setImageResource(TempConditionUtils.getPollenImageResource(Integer.parseInt(currentModel.getPollenTotalPercentage())));
                progressTextItem.setText(utils.getFormattedText(currentModel.getPollenTotalPercentage()) + "%");
                pollenStatusItem.setText(currentModel.getPollenTotalStatus());
            } else {
                circleProgressBarPollen.setProgress(0);
                circleProgressBarPollen.setColor(TempConditionUtils.getPollenColor(getActivity(), 0));
                pollenCheckedPollen.setBackground(getResources().getDrawable(TempConditionUtils.getPollenImageBackground(0)));
                pollenCheckedPollen.setImageResource(TempConditionUtils.getPollenImageResource(0));
                progressTextItem.setText(utils.getFormattedText(currentModel.getPollenTotalPercentage()) + "%");
                pollenStatusItem.setText(currentModel.getPollenTotalStatus());
            }
            // imageView is completely visible
            circleProgressBarPollenRefreshed = true;
        }


        if (!circleProgressBar.getLocalVisibleRect(scrollBounds) || scrollBounds.height() < circleProgressBar.getHeight()) {
            // imageView is not within or only partially within the visible window
            circleProgressBarRefreshed = false;
        } else {
            if (currentModel.getPollenStation() != null && currentModel.getPollenStation().length() > 0 && !TextUtils.equals(currentModel.getPollenStation(), "--")) {
                circleProgressBar.setProgress(0);
                circleProgressBar.setStrokeWidth(30);
                circleProgressBar.setMin(0);
                circleProgressBar.setMax(100);
//            textViewPollen.setText("   Pollen");
                pollenStatus.setText(currentModel.getPollenTotalStatus());
                if (!currentModel.getPollenTotalPercentage().equalsIgnoreCase("--")) {
                    circleProgressBar.setProgressWithAnimation(Float.parseFloat(currentModel.getPollenTotalPercentage()));
                    circleProgressBar.setColor(TempConditionUtils.getPollenColor(getActivity(), Integer.parseInt(currentModel.getPollenTotalPercentage())));
//        pollenChecked.getBackground().setColorFilter(TempConditionUtils.getPollenColor((int) currentModel.getPollenTotalPercentage()), PorterDuff.Mode.SRC);
                    pollenChecked.setBackground(getResources().getDrawable(TempConditionUtils.getPollenImageBackground(Integer.parseInt(currentModel.getPollenTotalPercentage()))));
                    pollenChecked.setImageResource(TempConditionUtils.getPollenImageResource(Integer.parseInt(currentModel.getPollenTotalPercentage())));
                    progressText.setText(utils.getFormattedText(currentModel.getPollenTotalPercentage()) + "%");
                    circleProgressBar.setVisibility(View.VISIBLE);
                    pollenChecked.setVisibility(View.VISIBLE);
                    progressText.setVisibility(View.VISIBLE);
                    pollenImage.setImageResource(R.drawable.pollen);
                    linearLayoutPollenData.setVisibility(View.VISIBLE);
                    textViewPollen.setVisibility(View.VISIBLE);
                    pollenImage.setVisibility(View.VISIBLE);
                    textViewCount.setText(" Count");
                } else {
                    circleProgressBar.setVisibility(View.INVISIBLE);
                    pollenChecked.setVisibility(View.INVISIBLE);
                    progressText.setVisibility(View.INVISIBLE);
                    linearLayoutPollenData.setVisibility(View.INVISIBLE);
                    textViewPollen.setVisibility(View.INVISIBLE);
                    pollenImage.setVisibility(View.INVISIBLE);
                    textViewCount.setText("");
                }
            } else {
                circleProgressBar.setProgress(0);
                circleProgressBar.setStrokeWidth(30);
                circleProgressBar.setMin(0);
                circleProgressBar.setMax(100);
//            textViewPollen.setText("   Pollen");
                linearLayoutPollenData.setVisibility(View.GONE);
                textViewPollen.setVisibility(View.INVISIBLE);
                textViewCount.setText(" Humidity");
                if (!currentModel.getRelativeHumidity().equalsIgnoreCase("--")) {
                    circleProgressBar.setProgressWithAnimation(Float.parseFloat(currentModel.getRelativeHumidity()));
                    pollenStatus.setText("");
                    circleProgressBar.setColor(TempConditionUtils.getPollenColor(getActivity(), 0));
//              pollenChecked.getBackground().setColorFilter(TempConditionUtils.getPollenColor((int) currentModel.getPollenTotalPercentage()), PorterDuff.Mode.SRC);
                    pollenChecked.setBackground(getResources().getDrawable(TempConditionUtils.getPollenImageBackground(0)));
                    pollenChecked.setImageResource(TempConditionUtils.getPollenImageResource(0));
                    progressText.setText(utils.getFormattedText(currentModel.getRelativeHumidity()) + "%");
                    circleProgressBar.setVisibility(View.VISIBLE);
                    pollenChecked.setVisibility(View.VISIBLE);
                    progressText.setVisibility(View.VISIBLE);
                    pollenImage.setImageResource(R.drawable.humidity);
                    linearLayoutPollenData.setVisibility(View.GONE);
                    textViewPollen.setVisibility(View.INVISIBLE);
                    pollenImage.setVisibility(View.VISIBLE);
                    textViewCount.setText(" Humidity");
                } else {
                    circleProgressBar.setVisibility(View.INVISIBLE);
                    pollenChecked.setVisibility(View.INVISIBLE);
                    progressText.setVisibility(View.INVISIBLE);
                    pollenImage.setVisibility(View.INVISIBLE);
                    linearLayoutPollenData.setVisibility(View.GONE);
                    textViewPollen.setVisibility(View.INVISIBLE);
                    textViewCount.setText(" Humidity");
                }
            }
            // imageView is completely visible
            circleProgressBarRefreshed = true;
        }
    }

    public void refreshArcView() {
        Rect scrollBounds = new Rect();
        scrollView.getHitRect(scrollBounds);
//        if (!arcView.getLocalVisibleRect(scrollBounds)) {
        if (!arcView.getLocalVisibleRect(scrollBounds) || scrollBounds.height() < arcView.getHeight()) {
            // imageView is not within or only partially within the visible window
            arcViewRefreshed = false;
        } else {
            if (currentModel != null && currentModel.getFormattedWeatherTime() != null && !arcViewRefreshed) {
                arcView.setDate(utils.getConvertedDateFromOneFormatToOther(currentModel.getFormattedWeatherTime(), utils.FORMAT1, utils.FORMAT3));
                arcView.setRiseTime(currentModel.getSunRiseHour() + ":" + currentModel.getSunRiseMin());
                arcView.setSetTime(currentModel.getSunSetHour() + ":" + currentModel.getSunSetMin());
                arcView.setArcAngle(0);
                float arcAngle = SunRiseSETUtils.getArcAngle(utils.getConvertedDateFromOneFormatToOther(currentModel.getFormattedWeatherTime(), utils.FORMAT1, utils.FORMAT3), currentModel.getSunRiseHour(), currentModel.getSunRiseMin(), currentModel.getSunSetHour(), currentModel.getSunSetMin());
                Log.i("Arc View", "Arc View");
                final ArcAngleAnimation animation = new ArcAngleAnimation(arcView, (int) arcAngle);
                animation.setDuration(1500);
                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        arcView.startAnimation(animation);
                    }
                },500);
                */
                arcView.startAnimation(animation);
                arcViewRefreshed = true;
            }
            // imageView is completely visible
        }
/*
        if (arcView.getLocalVisibleRect(scrollBounds)) {

            // Any portion of the imageView, even a single pixel, is within the visible window
            if (currentModel != null && currentModel.getFormattedWeatherTime() != null) {
                arcView.setDate(utils.getConvertedDateFromOneFormatToOther(currentModel.getFormattedWeatherTime(), utils.FORMAT1, utils.FORMAT3));
                arcView.setRiseTime(currentModel.getSunRiseHour() + ":" + currentModel.getSunRiseMin());
                arcView.setSetTime(currentModel.getSunSetHour() + ":" + currentModel.getSunSetMin());
                float arcAngle = SunRiseSETUtils.getArcAngle(utils.getConvertedDateFromOneFormatToOther(currentModel.getFormattedWeatherTime(), utils.FORMAT1, utils.FORMAT3), currentModel.getSunRiseHour(), currentModel.getSunRiseMin(), currentModel.getSunSetHour(), currentModel.getSunSetMin());
                Log.i("Arc View", "Arc View");
                ArcAngleAnimation animation = new ArcAngleAnimation(arcView, (int) arcAngle);
                animation.setDuration(3000);
                arcView.startAnimation(animation);
            }
        } else {

        }
*/
    }

    private void updateFifthItem() {
        windSpeedMills.setText(utils.getFormattedText(currentModel.getWindSpeed()));
//        (!currentModel.getPressureMeanSeaLevel().equalsIgnoreCase("--")?((int)Float.parseFloat(currentModel.getPressureMeanSeaLevel())+ ""):currentModel.getPressureMeanSeaLevel());
        pressureSpeedMills.setText(utils.getFormattedText(currentModel.getPressureMeanSeaLevel()));
    }

    private void updateThirdItem() {

        precipitationNow.setText(utils.getFormattedText(currentModel.getPrecipitation()) + " mm");

//        precipitationNowImage.setImageResource(TempConditionUtils.getPrecipitationResourceId((int) currentModel.getPrecipitation()));
        if (!currentModel.getPrecipitation().equalsIgnoreCase("--")) {
            precipitationNowImage.setImageDrawable(getResources().getDrawable(TempConditionUtils.getPrecipitationResourceId(currentModel.getPrecipitation())));
            precipitationNowImage.setVisibility(View.VISIBLE);
        } else {
            precipitationNowImage.setVisibility(View.INVISIBLE);
        }


        precipitationNoon.setText(utils.getFormattedText(currentModel.getPrecipitationNoon()) + " mm");
//        precipitationNoonImage.setImageResource(TempConditionUtils.getPrecipitationResourceId((int) currentModel.getPrecipitation()));
        if (!currentModel.getPrecipitationNoon().equalsIgnoreCase("--")) {
            precipitationNoonImage.setImageDrawable(getResources().getDrawable(TempConditionUtils.getPrecipitationResourceId(currentModel.getPrecipitationNoon())));
            precipitationNoonImage.setVisibility(View.VISIBLE);
        } else {
            precipitationNoonImage.setVisibility(View.INVISIBLE);
        }


        precipitationEvening.setText(utils.getFormattedText(currentModel.getPrecipitationEvening()) + " mm");
//        precipitationEveningImage.setImageResource(TempConditionUtils.getPrecipitationResourceId((int) currentModel.getPrecipitation()));
        if (!currentModel.getPrecipitationEvening().equalsIgnoreCase("--")) {
            precipitationEveningImage.setImageDrawable(getResources().getDrawable(TempConditionUtils.getPrecipitationResourceId(currentModel.getPrecipitationEvening())));
            precipitationEveningImage.setVisibility(View.VISIBLE);
        } else {
            precipitationEveningImage.setVisibility(View.INVISIBLE);
        }

        precipitationTonight.setText(utils.getFormattedText(currentModel.getPrecipitationTonight()) + " mm");
//        precipitationTonightImage.setImageResource(TempConditionUtils.getPrecipitationResourceId((int) currentModel.getPrecipitation()));

        if (!currentModel.getPrecipitationTonight().equalsIgnoreCase("--")) {
            precipitationTonightImage.setImageDrawable(getResources().getDrawable(TempConditionUtils.getPrecipitationResourceId(currentModel.getPrecipitationTonight())));
            precipitationTonightImage.setVisibility(View.VISIBLE);
        } else {
            precipitationTonightImage.setVisibility(View.INVISIBLE);
        }
    }

    private void updateSecondItem() {
        feelsLike.setText(utils.getFormattedText(currentModel.getTemperature()) + "°");
        windspeed.setText(utils.getFormattedText(currentModel.getWindSpeed()) + " mph");
        humidity.setText(utils.getFormattedText(currentModel.getRelativeHumidity()) + "%");
        dewPoint.setText(utils.getFormattedText(currentModel.getDewPoint()) + "°");
        visibility.setText(utils.getFormattedText(currentModel.getVisibility()) + " km");
        winddirection.setText(currentModel.getWindDirection());


/*
        feelsLike.setText((int) Float.parseFloat(currentModel.getTemperature()) + "°");
        windspeed.setText((int) Float.parseFloat(currentModel.getWindSpeed()) + " mph");
        winddirection.setText(currentModel.getWindDirection());
        humidity.setText((int) Float.parseFloat(currentModel.getRelativeHumidity()) + "%");
        dewPoint.setText((int) Float.parseFloat(currentModel.getDewPoint()) + "°");
        visibility.setText((int) Float.parseFloat(currentModel.getVisibility()) + "km");
*/
    }

    private void updateTemperature() {
//        .setText(!currentModel.getPrecipitationTonight().equalsIgnoreCase("--")?((int)Float.parseFloat(currentModel.getPrecipitationTonight())+ ""):currentModel.getPrecipitationTonight());
        temperature.setText(utils.getFormattedText(currentModel.getTemperature()) + "°");
        temperatureMin.setText(utils.getFormattedText(currentModel.getMinTemperature()) + "°");
        temperatureMax.setText(utils.getFormattedText(currentModel.getMaxTemperature()) + "°");

//        temperature.setText((int) Float.parseFloat(currentModel.getTemperature()) + "°");
//        temperatureMin.setText((int) Float.parseFloat(currentModel.getMinTemperature()) + "°");
//        temperatureMax.setText((int) Float.parseFloat(currentModel.getMaxTemperature()) + "°");

        WeatherCondition weatherConditionModel = TempConditionUtils.getConditionCurrent(currentModel.getPrecipitation(), currentModel.getCloud());
        if (weatherConditionModel != null) {
            weatherCondition.setText(weatherConditionModel.getCondition());
            weatherImage.setImageResource(weatherConditionModel.getResourceId());
            weatherImage.setVisibility(View.VISIBLE);
        } else {
            weatherCondition.setText("--");
            weatherImage.setVisibility(View.INVISIBLE);
        }
    }

    private void updatePollenProgress() {
        if (currentModel.getPollenStation() != null && currentModel.getPollenStation().length() > 0 && !TextUtils.equals(currentModel.getPollenStation(), "--")) {
            circleProgressBar.setProgress(0);
            circleProgressBar.setStrokeWidth(30);
            circleProgressBar.setMin(0);
            circleProgressBar.setMax(100);
//            textViewPollen.setText("   Pollen");
            pollenStatus.setText(currentModel.getPollenTotalStatus());
            if (!currentModel.getPollenTotalPercentage().equalsIgnoreCase("--")) {
                circleProgressBar.setProgressWithAnimation(Float.parseFloat(currentModel.getPollenTotalPercentage()));
                circleProgressBar.setColor(TempConditionUtils.getPollenColor(getActivity(), Integer.parseInt(currentModel.getPollenTotalPercentage())));
//        pollenChecked.getBackground().setColorFilter(TempConditionUtils.getPollenColor((int) currentModel.getPollenTotalPercentage()), PorterDuff.Mode.SRC);
                pollenChecked.setBackground(getResources().getDrawable(TempConditionUtils.getPollenImageBackground(Integer.parseInt(currentModel.getPollenTotalPercentage()))));
                pollenChecked.setImageResource(TempConditionUtils.getPollenImageResource(Integer.parseInt(currentModel.getPollenTotalPercentage())));
                progressText.setText(utils.getFormattedText(currentModel.getPollenTotalPercentage()) + "%");
                circleProgressBar.setVisibility(View.VISIBLE);
                pollenChecked.setVisibility(View.VISIBLE);
                progressText.setVisibility(View.VISIBLE);
                pollenImage.setImageResource(R.drawable.pollen);
                linearLayoutPollenData.setVisibility(View.VISIBLE);
                textViewPollen.setVisibility(View.VISIBLE);
                pollenImage.setVisibility(View.VISIBLE);
                textViewCount.setText(" Count");
            } else {
                circleProgressBar.setVisibility(View.INVISIBLE);
                pollenChecked.setVisibility(View.INVISIBLE);
                progressText.setVisibility(View.INVISIBLE);
                linearLayoutPollenData.setVisibility(View.INVISIBLE);
                textViewPollen.setVisibility(View.INVISIBLE);
                pollenImage.setVisibility(View.INVISIBLE);
                textViewCount.setText("");
            }
        } else {
            circleProgressBar.setProgress(0);
            circleProgressBar.setStrokeWidth(30);
            circleProgressBar.setMin(0);
            circleProgressBar.setMax(100);
//            textViewPollen.setText("   Pollen");
            linearLayoutPollenData.setVisibility(View.GONE);
            textViewPollen.setVisibility(View.INVISIBLE);
            textViewCount.setText(" Humidity");
            if (!currentModel.getRelativeHumidity().equalsIgnoreCase("--")) {
                circleProgressBar.setProgressWithAnimation(Float.parseFloat(currentModel.getRelativeHumidity()));
                pollenStatus.setText("");
                circleProgressBar.setColor(TempConditionUtils.getPollenColor(getActivity(), 0));
//              pollenChecked.getBackground().setColorFilter(TempConditionUtils.getPollenColor((int) currentModel.getPollenTotalPercentage()), PorterDuff.Mode.SRC);
                pollenChecked.setBackground(getResources().getDrawable(TempConditionUtils.getPollenImageBackground(0)));
                pollenChecked.setImageResource(TempConditionUtils.getPollenImageResource(0));
                progressText.setText(utils.getFormattedText(currentModel.getRelativeHumidity()) + "%");
                circleProgressBar.setVisibility(View.VISIBLE);
                pollenChecked.setVisibility(View.VISIBLE);
                progressText.setVisibility(View.VISIBLE);
                pollenImage.setImageResource(R.drawable.humidity);
                linearLayoutPollenData.setVisibility(View.GONE);
                textViewPollen.setVisibility(View.INVISIBLE);
                pollenImage.setVisibility(View.VISIBLE);
                textViewCount.setText(" Humidity");
            } else {
                circleProgressBar.setVisibility(View.INVISIBLE);
                pollenChecked.setVisibility(View.INVISIBLE);
                progressText.setVisibility(View.INVISIBLE);
                pollenImage.setVisibility(View.INVISIBLE);
                linearLayoutPollenData.setVisibility(View.GONE);
                textViewPollen.setVisibility(View.INVISIBLE);
                textViewCount.setText(" Humidity");
            }
        }
    }

    private void updatePollenData() {
        circleProgressBarPollen.setStrokeWidth(20);
        circleProgressBarPollen.setMin(0);
        circleProgressBarPollen.setMax(100);
        circleProgressBarPollen.setProgress(0);
        if (!currentModel.getPollenTotalPercentage().equalsIgnoreCase("--")) {
            circleProgressBarPollen.setProgressWithAnimation(Float.parseFloat(currentModel.getPollenTotalPercentage()));
            circleProgressBarPollen.setColor(TempConditionUtils.getPollenColor(getActivity(), Integer.parseInt(currentModel.getPollenTotalPercentage())));
            pollenCheckedPollen.setBackground(getResources().getDrawable(TempConditionUtils.getPollenImageBackground(Integer.parseInt(currentModel.getPollenTotalPercentage()))));
            pollenCheckedPollen.setImageResource(TempConditionUtils.getPollenImageResource(Integer.parseInt(currentModel.getPollenTotalPercentage())));
            progressTextItem.setText(utils.getFormattedText(currentModel.getPollenTotalPercentage()) + "%");
            pollenStatusItem.setText(currentModel.getPollenTotalStatus());
        } else {
            circleProgressBarPollen.setProgress(0);
            circleProgressBarPollen.setColor(TempConditionUtils.getPollenColor(getActivity(), 0));
            pollenCheckedPollen.setBackground(getResources().getDrawable(TempConditionUtils.getPollenImageBackground(0)));
            pollenCheckedPollen.setImageResource(TempConditionUtils.getPollenImageResource(0));
            progressTextItem.setText(utils.getFormattedText(currentModel.getPollenTotalPercentage()) + "%");
            pollenStatusItem.setText(currentModel.getPollenTotalStatus());
        }

        linearLayoutPollen.removeAllViews();
        pollenArea.setText(currentModel.getPollenStation());
        pollenTotalCount.setText(utils.getFormattedText(currentModel.getPollenTotalCount()));
        pollenTotalStatus.setText(currentModel.getPollenTotalStatus());
        for (int i = 0; i < currentModel.getPollenList().size(); i++) {
            View pollenItem = getLayoutInflater().inflate(R.layout.pollen_item, null);
            PollenModel item = currentModel.getPollenList().get(i);
            TextView name = pollenItem.findViewById(R.id.name);
            TextView count = pollenItem.findViewById(R.id.count);
            TextView status = pollenItem.findViewById(R.id.status);
            name.setText(item.getName());
            count.setText(utils.getFormattedText(item.getCount()));
            status.setText(item.getStatus());
            linearLayoutPollen.addView(pollenItem);
        }
    }

    @Override
    public void setApiResponse(JSONObject jsonObject, String calledApi) {

    }

    @Override
    public void setApiResponse(JSONArray jsonArray, String calledApi) {

    }

    @Override
    public void setApiResponse(String jsonAsString, String calledApi) {

    }

    @Override
    public void setApiError() {

    }

    @Override
    public void onTabSelected(int position) {
        mHasBeenVisible = true;
        if (mHasBeenVisible)
            ((MainActivity) getActivity()).setBackground(blurIndex);
    }

    @Override
    public void onTabUnSelected(int position) {
        mHasBeenVisible = false;
    }

    public void updateData(CurrentModel currentModel) {
        this.currentModel = currentModel;
      /*  getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {*/
        swipeLayout.setRefreshing(false);
        scrollView.setVisibility(View.VISIBLE);
        heatMapGraphView.setVisibility(View.VISIBLE);
        if (currentModel != null) {
            TempConditionUtils.setIsDay(SunRiseSETUtils.isDay(currentModel.getSunRiseHour(), currentModel.getSunRiseMin(), currentModel.getSunSetHour(), currentModel.getSunSetMin()));
            updateBackgroundTheme();
            updateHeatMapGraphView();
            updatePollenProgress();
            updateTemperature();
            updateSecondItem();
            updateThirdItem();
            updatePollenData();
            updateFifthItem();
            updateSixthItem();
        }
            /*}
        });*/
    }
}
