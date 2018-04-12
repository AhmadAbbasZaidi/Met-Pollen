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
import com.cfp.metpollen.view.adapters.HourlyRecyclerAdapterWithHeader;
import com.cfp.metpollen.view.customViews.CustomSwipeToRefresh;
import com.cfp.metpollen.view.customViews.CustomViewPager;
import com.cfp.metpollen.view.customViews.GraphView;
import com.cfp.metpollen.view.utilities.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HourlyFragment extends BaseFragment {

    public static final boolean SHOW_ADAPTER_POSITIONS = true;
    private static HourlyFragment instance = null;
    private static List<Point> PointList;
    private static HourlyRecyclerAdapterWithHeader adapter;
    private static List<ForecastModel> forecastList;
    private static int blurIndex = 0;
    boolean cancel = false;
    int x = 0, y = 0;
    private GraphView rv;
    private ImageView ball;
    private RecyclerView recyclerView;
    private TextView textviewHeader;
    private TextView temp;
    private TextView conditionName;
    private ImageView conditionImage;
    private TextView datetime;
    private boolean mHasBeenVisible = false;
    private AppBarLayout appBarLayout;
    private TextView chanceOfRain;
    private CustomViewPager customViewPager;
    private CustomSwipeToRefresh swipeLayout;
    private LinearLayout lLCollapseToolbar;

    public HourlyFragment() {
        // Required empty public constructor
    }

    public static HourlyFragment getInstance(CustomViewPager customViewPager) {
        if (instance == null) {
            instance = new HourlyFragment();
            instance.customViewPager = customViewPager;
            Bundle args = new Bundle();
            args.putInt("position", 1);
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

    /*private List<ForecastModel> getList() {
        List<ForecastModel> list = new ArrayList<>();
        list.add(new ForecastModel());
        list.add(1);
        list.add(1);
        return list;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hourly, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lLCollapseToolbar = (LinearLayout) view.findViewById(R.id.ll_collapse_toolbar);
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);

        forecastList = DbUtils.getForecast();
        swipeLayout = (CustomSwipeToRefresh) view.findViewById(R.id.swipeRefreshLayout);
        appBarLayout = (AppBarLayout) view.findViewById(R.id.app_bar);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        rv = (GraphView) view.findViewById(R.id.rv);
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

//        appBarLayout.setOnTouchListener(this);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (utils.checkNetworkState(getActivity())) {
                            ((MainActivity) getActivity()).updateForecast();
                        }
                        else
                        {
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

        textviewHeader = (TextView) view.findViewById(R.id.textview_header);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        if (forecastList != null && forecastList.size() > 0) {
            adapter = new HourlyRecyclerAdapterWithHeader(getActivity(), forecastList);
            recyclerView.setAdapter(adapter);
        }

//        recyclerView.setOnTouchListener(this);

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
                String date = adapter.getDateAtPosition(posSt > 0 ? posSt - 1 : 0);
                textviewHeader.setText(date);
            }
        });

        /*StickyHeaderLayoutManager stickyHeaderLayoutManager = new StickyHeaderLayoutManager();
        recyclerView.setLayoutManager(stickyHeaderLayoutManager);

        // set a header position callback to set elevation on sticky headers, because why not
        stickyHeaderLayoutManager.setHeaderPositionChangedCallback(new StickyHeaderLayoutManager.HeaderPositionChangedCallback() {
            @Override
            public void onHeaderPositionChanged(int sectionIndex, View header, StickyHeaderLayoutManager.HeaderPosition oldPosition, StickyHeaderLayoutManager.HeaderPosition newPosition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    boolean elevated = newPosition == StickyHeaderLayoutManager.HeaderPosition.STICKY;
                    header.setElevation(elevated ? 8 : 0);
                }
            }
        });

        recyclerView.setAdapter(new HourlyHeaderRecyclerAdapter(5, 5, true, false, false, SHOW_ADAPTER_POSITIONS));
        */

        /*RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        HourlyRecyclerAdapter adapter = new HourlyRecyclerAdapter(getActivity(),getList());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView rv, int dx, int dy) {
                super.onScrolled(rv, dx, dy);
                LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) rv.getLayoutManager());
                int lastItemVisible = linearLayoutManager.findLastVisibleItemPosition();
                ((MainActivity) getActivity()).setBackground(lastItemVisible);
            }
        });*/

        temp = (TextView) view.findViewById(R.id.temp);
        chanceOfRain = (TextView) view.findViewById(R.id.chanceOfRain);
        datetime = (TextView) view.findViewById(R.id.dateTime);
        conditionName = (TextView) view.findViewById(R.id.conditionName);
        conditionImage = (ImageView) view.findViewById(R.id.conditionImage);

        rv.setSwipeLayout(swipeLayout);
        rv.setSunRiseSetBackgroundLayout(((MainActivity) getActivity()).getBackgroundViewOverlayHourly());
        rv.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                int action = e.getAction();
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
//                        rv.getParent().requestDisallowInterceptTouchEvent(true);
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
//                        rv.getParent().requestDisallowInterceptTouchEvent(false);
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
//                        rv.getParent().requestDisallowInterceptTouchEvent(false);
                    }
                    break;
                    default: {
                        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
                        AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
                        behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
                            @Override
                            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                                return true;
                            }
                        });
                        params.setBehavior(behavior);
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


        ball = (ImageView) view.findViewById(R.id.ball);

        PointList = new ArrayList<>();
        PointList.add(new Point(1, 30));
        PointList.add(new Point(2, 29));
        PointList.add(new Point(3, 28));
        PointList.add(new Point(4, 27));
        PointList.add(new Point(5, 27));
        PointList.add(new Point(6, 29));
        PointList.add(new Point(7, 30));
        PointList.add(new Point(8, 31));
        PointList.add(new Point(9, 32));
        PointList.add(new Point(10, 35));
        PointList.add(new Point(11, 40));
        PointList.add(new Point(12, 45));
        PointList.add(new Point(13, 50));
        PointList.add(new Point(14, 55));
        PointList.add(new Point(15, 60));
        PointList.add(new Point(16, 65));
        PointList.add(new Point(17, 60));
        PointList.add(new Point(18, 50));
        PointList.add(new Point(19, 47));
        PointList.add(new Point(20, 45));
        PointList.add(new Point(21, 40));
        PointList.add(new Point(22, 47));
        PointList.add(new Point(23, 45));
        PointList.add(new Point(24, 42));

        rv.setTempView(temp);
        rv.setConditionImage(conditionImage);
        rv.setConditionName(conditionName);
        rv.setDatetime(datetime);
        rv.setChanceOfRain(chanceOfRain);
        if (forecastList != null && forecastList.size() > 0) {
            appBarLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            if(checkEmptyTemperature(forecastList))
            {
                rv.setVisibility(View.INVISIBLE);
                lLCollapseToolbar.setVisibility(View.INVISIBLE);
            }
            else {
                rv.setPoints(forecastList, PointList, R.layout.recycler_list_item, ball);
                rv.setVisibility(View.VISIBLE);
                lLCollapseToolbar.setVisibility(View.VISIBLE);
            }
        } else {
            appBarLayout.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            lLCollapseToolbar.setVisibility(View.INVISIBLE);
        }

    }

    public void updateData() {
        forecastList = DbUtils.getForecast();
        adapter = new HourlyRecyclerAdapterWithHeader(getActivity(), forecastList);
        recyclerView.setAdapter(adapter);
        if(checkEmptyTemperature(forecastList))
        {
            rv.setVisibility(View.INVISIBLE);
        }
        else {
            rv.setPoints(forecastList, PointList, R.layout.recycler_list_item, ball);
            rv.setVisibility(View.VISIBLE);
        }
    }

    public boolean checkEmptyTemperature(List<ForecastModel> forecastList) {
        boolean isEmpty = false;
        for (ForecastModel model : forecastList) {
            if(!isEmpty)
            {
                isEmpty= model.getTemperature().equalsIgnoreCase("--")?true:false;
            }
            else
            {
                break;
            }

        }
        return isEmpty;
    }

    public void updateData(final List<ForecastModel> forecastList) {

        this.forecastList = forecastList;
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
        if (forecastList != null && forecastList.size() > 0 && rv != null) {
            appBarLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            if (adapter == null) {
                adapter = new HourlyRecyclerAdapterWithHeader(getActivity(), forecastList);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.setForecastModelsList(forecastList);
            }
            if(checkEmptyTemperature(forecastList))
            {
                rv.setVisibility(View.INVISIBLE);
                lLCollapseToolbar.setVisibility(View.INVISIBLE);
            }
            else {
                rv.setPoints(forecastList, PointList, R.layout.recycler_list_item, ball);
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
            ((MainActivity) getActivity()).getBackgroundViewOverlayThreeDays().setVisibility(View.GONE);
            ((MainActivity) getActivity()).getBackgroundViewOverlayHourly().setVisibility(View.VISIBLE);
            /*if (rv != null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        rv.updateBackground();
                    }
                },500);

            }*/
        }
    }

    @Override
    public void onTabUnSelected(int position) {
        mHasBeenVisible = false;
    }

}
