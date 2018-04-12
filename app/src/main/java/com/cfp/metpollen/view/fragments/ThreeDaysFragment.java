package com.cfp.metpollen.view.fragments;


import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cfp.metpollen.R;
import com.cfp.metpollen.data.db.DbUtils;
import com.cfp.metpollen.data.db.model.ForecastModel;
import com.cfp.metpollen.view.activities.MainActivity;
import com.cfp.metpollen.view.adapters.FragmentThreeDayRecyclerAdapter;
import com.cfp.metpollen.view.customViews.CustomSwipeToRefresh;
import com.cfp.metpollen.view.customViews.CustomViewPager;
import com.cfp.metpollen.view.customViews.SineGraphView;
import com.cfp.metpollen.view.utilities.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThreeDaysFragment extends BaseFragment {

    private static List<Point> PointList;
    private static ThreeDaysFragment instance = null;
    private static FragmentThreeDayRecyclerAdapter adapter;
    private static List<ForecastModel> forecastList;
    private static int blurIndex = 0;
    private SineGraphView rv;
    private ImageView ball;
    private TextView temp;
    private TextView conditionName;
    private ImageView conditionImage;
    private RecyclerView recyclerView;
    private TextView datetime;
    private boolean mHasBeenVisible = false;
    private AppBarLayout appBarLayout;
    private TextView chanceOfRain;
    private CustomViewPager customViewPager;
    private CustomSwipeToRefresh swipeLayout;
    private LinearLayout lLCollapseToolbar;

    public ThreeDaysFragment() {
        // Required empty public constructor
    }

    public static ThreeDaysFragment getInstance(CustomViewPager customViewPager) {
        if (instance == null) {
            instance = new ThreeDaysFragment();
            instance.customViewPager = customViewPager;
            forecastList = DbUtils.getForecastDayNight();
            Bundle args = new Bundle();
            args.putInt("position", 2);
            instance.setArguments(args);
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
        return inflater.inflate(R.layout.fragment_three_days, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lLCollapseToolbar = (LinearLayout) view.findViewById(R.id.ll_collapse_toolbar);

        this.forecastList = DbUtils.getForecastDayNight();
        if (forecastList != null)
            Log.i("sinewave 2 ", "forecast list size = " + forecastList.size());
        swipeLayout = (CustomSwipeToRefresh) view.findViewById(R.id.swipeRefreshLayout);
        appBarLayout = (AppBarLayout) view.findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float percentage = ((float) Math.abs(verticalOffset) / appBarLayout.getTotalScrollRange());
                lLCollapseToolbar.setAlpha(1 - percentage);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    lLCollapseToolbar.setZ(-5);
                    lLCollapseToolbar.setCameraDistance(getResources().getDisplayMetrics().density * verticalOffset);
                }

                verticalOffset = Math.abs(verticalOffset);
                int difference = appBarLayout.getTotalScrollRange() - lLCollapseToolbar.getHeight();
                if (verticalOffset >= difference) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        lLCollapseToolbar.setElevation(verticalOffset * 10);
                    }
                    // start elevating gradually to our target elevation
                }

                if (mHasBeenVisible) {
                    swipeLayout.setEnabled(verticalOffset < 0.5f);
                    setBlurIndex(verticalOffset);
                    ((MainActivity) getActivity()).setBackground(verticalOffset);
                }
            }
        });

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (utils.checkNetworkState(getActivity())) {
                            ((MainActivity) getActivity()).updateForecast();
                        } else {
                            swipeLayout.setRefreshing(false);
                            Toast.makeText(getActivity(), "Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
                        }
                        /*new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                swipeLayout.setRefreshing(false);

                            }
                        }, 2000);*/
                    }
                }, 2000);

            }
        });
        swipeLayout.setColorScheme(R.color.blueoverlayOpaque);


        temp = (TextView) view.findViewById(R.id.temp);
        datetime = (TextView) view.findViewById(R.id.dateTime);
        conditionName = (TextView) view.findViewById(R.id.conditionName);
        conditionImage = (ImageView) view.findViewById(R.id.conditionImage);
        chanceOfRain = (TextView) view.findViewById(R.id.chanceOfRain);

        rv = (SineGraphView) view.findViewById(R.id.rv);
        rv.setSwipeLayout(swipeLayout);
        ball = (ImageView) view.findViewById(R.id.ball);

        PointList = new ArrayList<>();
        PointList.add(new Point(1, 16));
        PointList.add(new Point(2, 4));
        PointList.add(new Point(3, 15));
        PointList.add(new Point(4, 3));
        PointList.add(new Point(5, 17));
        PointList.add(new Point(6, 4));
        /*
        PointList.add(new Point(7, 16));
        PointList.add(new Point(8, 4));
        PointList.add(new Point(9, 15));
        PointList.add(new Point(10, 3));
        PointList.add(new Point(11, 17));
        PointList.add(new Point(12, 4));
        PointList.add(new Point(13, 16));
        PointList.add(new Point(14, 4));*/

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        rv.setSunRiseSetBackgroundLayout(((MainActivity) getActivity()).getBackgroundViewOverlayThreeDays());
        rv.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                int action = e.getAction();
                Log.i("MotionEvent = ", e.toString());
                switch (action) {
                    case MotionEvent.ACTION_DOWN: {
                        customViewPager.setPagingEnabled(false);
                        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
                        AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
                        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
                            @Override
                            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                                return false;
                            }
                        });
                        params.setBehavior(behavior);
                        appBarLayout.setLayoutParams(params);
//                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    break;
                    case MotionEvent.ACTION_MOVE: {
                        customViewPager.setPagingEnabled(false);
                        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
                        AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
                        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
                            @Override
                            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                                return false;
                            }
                        });
                        params.setBehavior(behavior);
                        appBarLayout.setLayoutParams(params);
//                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    break;
                    case MotionEvent.ACTION_CANCEL: {
                        customViewPager.setPagingEnabled(true);
                        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
                        AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
                        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
                            @Override
                            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                                return true;
                            }
                        });
                        params.setBehavior(behavior);
                        appBarLayout.setLayoutParams(params);
//                        rv.getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    break;
                    case MotionEvent.ACTION_UP: {
                        customViewPager.setPagingEnabled(true);
                        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
                        AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
                        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
                            @Override
                            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                                return true;
                            }
                        });
                        params.setBehavior(behavior);
                        appBarLayout.setLayoutParams(params);
//                        rv.getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    break;
                    default: {
                        customViewPager.setPagingEnabled(true);
                        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
                        AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
                        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
                            @Override
                            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                                return true;
                            }
                        });
                        params.setBehavior(behavior);
                        appBarLayout.setLayoutParams(params);
//                        rv.getParent().requestDisallowInterceptTouchEvent(false);
                    }

                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        /*PointList.add(new Point(7, 19));
        PointList.add(new Point(8, 7));
        PointList.add(new Point(9, 20));
        PointList.add(new Point(10, 9));
        PointList.add(new Point(11, 19));
        PointList.add(new Point(12, 7));
        PointList.add(new Point(13, 21));
        PointList.add(new Point(14, 7));
        PointList.add(new Point(15, 21));
        PointList.add(new Point(16, 8));
        PointList.add(new Point(17, 20));
        PointList.add(new Point(18, 7));
        PointList.add(new Point(19, 21));
        PointList.add(new Point(20, 7));
        PointList.add(new Point(21, 17));
        PointList.add(new Point(22, 8));
        PointList.add(new Point(23, 18));
        PointList.add(new Point(24, 9));*/

        rv.setTempView(temp);
        rv.setConditionImage(conditionImage);
        rv.setConditionName(conditionName);
        rv.setDatetime(datetime);
        rv.setChanceOfRain(chanceOfRain);
        if (forecastList != null && forecastList.size() > 0) {
            appBarLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            if (checkEmptyTemperature(forecastList)) {
                rv.setVisibility(View.INVISIBLE);
                lLCollapseToolbar.setVisibility(View.INVISIBLE);
            } else {
                rv.setPoints(forecastList, PointList, R.layout.recycler_list_item_sine_wave, ball);
                rv.setVisibility(View.VISIBLE);
                lLCollapseToolbar.setVisibility(View.VISIBLE);
            }
        } else {
            appBarLayout.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            rv.setVisibility(View.INVISIBLE);
            lLCollapseToolbar.setVisibility(View.INVISIBLE);
        }

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        if (forecastList != null && forecastList.size() > 0) {
            adapter = new FragmentThreeDayRecyclerAdapter(getActivity(), R.layout.recycler_item_three_day, forecastList);
            recyclerView.setAdapter(adapter);
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                /*if (mHasBeenVisible) {
                    setBlurIndex(recyclerView.computeVerticalScrollOffset());
                    ((MainActivity) getActivity()).setBackground(recyclerView.computeVerticalScrollOffset());
                }*/

                int start = 0;
                View startView = recyclerView.findChildViewUnder(start, recyclerView.getTop());
                int startPos = recyclerView.getChildAdapterPosition(startView);

                LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                int pos = layoutManager.findLastCompletelyVisibleItemPosition();
                int posSt = layoutManager.findFirstCompletelyVisibleItemPosition();


//                Log.i("scrolly ", " =  " + recyclerView.computeHorizontalScrollOffset());
//                Log.i("pos ", " =  " + pos);
//                Log.i("posSt ", " =  " + posSt);
//                Log.i("dx ", " =  " + dx);
//                Log.i("dy ", " =  " + dy);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public boolean checkEmptyTemperature(List<ForecastModel> forecastList) {
        boolean isEmpty = false;
        for (ForecastModel model : forecastList) {
            if (!isEmpty) {
                isEmpty = model.getMaxTemperature().equalsIgnoreCase("--") || model.getMinTemperature().equalsIgnoreCase("--") ? true : false;
            } else {
                break;
            }

        }
        return isEmpty;
    }

    public void updateData(List<ForecastModel> forecastModelList) {

        this.forecastList = DbUtils.getForecastDayNight();
//        Log.i("sinewave 3 ", "forecast list size = " + forecastList.size());
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
        if (forecastList != null && forecastList.size() > 0 && rv != null) {
            appBarLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            if (adapter == null) {
                adapter = new FragmentThreeDayRecyclerAdapter(getActivity(), R.layout.recycler_item_three_day, forecastList);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.setForecastModelsList(forecastList);
            }

            if (checkEmptyTemperature(forecastList)) {
                rv.setVisibility(View.INVISIBLE);
                lLCollapseToolbar.setVisibility(View.INVISIBLE);
            } else {
                rv.setPoints(forecastList, PointList, R.layout.recycler_list_item_sine_wave, ball);
                rv.setVisibility(View.VISIBLE);
                lLCollapseToolbar.setVisibility(View.VISIBLE);
            }
            swipeLayout.setRefreshing(false);
        }
//            }
//        }, 1000);


    }

    @Override
    public void onTabSelected(int position) {
//        Toast.makeText(getActivity(), "TabPosition = " + position + "\nblurIndex = " + blurIndex, Toast.LENGTH_LONG).show();
        Log.i("Tab = ", position + "");
        mHasBeenVisible = true;
        if (mHasBeenVisible) {
            ((MainActivity) getActivity()).setBackground(blurIndex);
            ((MainActivity) getActivity()).getBackgroundViewOverlayToday().setVisibility(View.GONE);
            ((MainActivity) getActivity()).getBackgroundViewOverlayHourly().setVisibility(View.GONE);
            ((MainActivity) getActivity()).getBackgroundViewOverlayThreeDays().setVisibility(View.VISIBLE);

//            if (rv != null) {
                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        rv.updateBackground();
                    }
                },300);*/
//            }
        }
    }

    @Override
    public void onTabUnSelected(int position) {
        mHasBeenVisible = false;
    }


}
