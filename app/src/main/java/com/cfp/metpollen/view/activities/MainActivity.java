package com.cfp.metpollen.view.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.cfp.metpollen.App;
import com.cfp.metpollen.R;
import com.cfp.metpollen.data.api.ApiCalls;
import com.cfp.metpollen.data.api.Handlers.CurrentHandler;
import com.cfp.metpollen.data.api.Handlers.ForecastHandler;
import com.cfp.metpollen.data.api.Handlers.StationHandler;
import com.cfp.metpollen.data.api.Interface.InitApi;
import com.cfp.metpollen.data.api.Requests.StringRequestCall;
import com.cfp.metpollen.data.db.DbUtils;
import com.cfp.metpollen.data.db.model.CurrentModel;
import com.cfp.metpollen.data.db.model.ForecastModel;
import com.cfp.metpollen.data.db.model.StationModel;
import com.cfp.metpollen.presenter.ApiResponseListener;
import com.cfp.metpollen.view.adapters.MainViewPagerAdapter;
import com.cfp.metpollen.view.adapters.SearchRecyclerAdapter;
import com.cfp.metpollen.view.backgroundService.LocationService;
import com.cfp.metpollen.view.broadcastRecievers.ConnectivityReceiver;
import com.cfp.metpollen.view.broadcastRecievers.LocationProviderChangedReceiver;
import com.cfp.metpollen.view.customViews.BlurImageView;
import com.cfp.metpollen.view.fragments.HourlyFragment;
import com.cfp.metpollen.view.fragments.LocationEnablerFragment;
import com.cfp.metpollen.view.fragments.TabsFragment;
import com.cfp.metpollen.view.fragments.ThreeDaysFragment;
import com.cfp.metpollen.view.fragments.TodayFragmentWithoutList;
import com.cfp.metpollen.view.interfaces.BackgroundUpdateListener;
import com.cfp.metpollen.view.interfaces.SearchDropdownListener;
import com.cfp.metpollen.view.utilities.AppWideVariables;
import com.cfp.metpollen.view.utilities.BackgroundUtils;
import com.cfp.metpollen.view.utilities.BlurBuilder;
import com.cfp.metpollen.view.utilities.PermissionsRequest;
import com.cfp.metpollen.view.utilities.TabUtils;
import com.cfp.metpollen.view.utilities.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;

import static com.cfp.metpollen.view.utilities.PermissionsRequest.LOACTION_ENABLE_REQUEST;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BackgroundUpdateListener, SearchDropdownListener, ApiResponseListener, ConnectivityReceiver.ConnectivityReceiverListener, LocationProviderChangedReceiver.LocationReceiverListener {

    static final public String SERVICE_NAME = "com.lmkt.weather.view.backgroundService.LocationService";

    public static boolean blurred = false;
    public static boolean permissionsEnabled = false, dBHasData = false;
    public static boolean locationButtonPressed = false;
    private static int position = 0;
    private static BackgroundUpdateListener backgroundUpdateListener;
    private static boolean mServiceBound = false;
    private static boolean isTabLayout = false;
    private static boolean appLaunched = true;
    private static boolean isClosed = true;
    private static boolean shouldBlur = true;
    LocationService mBoundService;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private MainViewPagerAdapter adapter;
    private BlurImageView backgroundView;
    private SimpleCursorAdapter mAdapter;
    private View fragment;
    private boolean shouldCloseApp;
    private RecyclerView rVSearchDropDown;
    private SearchRecyclerAdapter searchAdapter;
    private EditText searchEditText;
    private SearchDropdownListener searchDropdownListener;
    private SearchView searchView;
    private boolean isSearchExpanded;
    private TextView title;
    private boolean isRefresh = true;
    private boolean isRefreshCurrent = true;
    private ProgressBar progressBar;
    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationService.MyBinder myBinder = (LocationService.MyBinder) service;
            mBoundService = myBinder.getService();
            mServiceBound = true;
        }
    };
    private ImageView backgroundViewOverlayToday;
    private ApiResponseListener apiResponseListener;
    private Timer timer;
    private ConnectivityReceiver connectivityReceiver;
    private Toolbar toolbar;
    private LinearLayout rootLayout;
    private String lat, lon;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("INTENT RECIEVED", " = INTENT RECIEVED");
            if (intent.hasExtra(LocationService.MESSAGE)) {
                String s = intent.getStringExtra(LocationService.MESSAGE);
                if (s == "1") {
//                    ((TodayFragment) adapter.getItem(0)).updateForecast();
                    Log.i("setCurrentCity = ", "0");
                    if (utils.checkNetworkState(getApplicationContext()))
                        setCurrentCity();

                }
            }
            // do something here.
        }
    };
    private ImageView backgroundViewOverlayHourly;
    private ImageView backgroundViewOverlayThreeDays;

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public ImageView getBackgroundViewOverlayHourly() {
        return backgroundViewOverlayHourly;
    }

    public ImageView getBackgroundViewOverlayThreeDays() {
        return backgroundViewOverlayThreeDays;
    }

    public ImageView getBackgroundViewOverlayToday() {
        return backgroundViewOverlayToday;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        utils.checkTimeSetting(MainActivity.this);

        initializeViews();
        Log.i("setCurrentCity = ", "1");
//        setCurrentCity();
        resolveIntent();
        //Set the schedule function and rate
        /*t.scheduleAtFixedRate(new TimerTask() {
                                  @Override
                                  public void run() {
                                      //Called each time when 10000 milliseconds (10 second) (the period parameter)
//                                      BackgroundUtils.settime();
                                  }

                              },
                //Set how long before to start calling the TimerTask (in milliseconds)
                0,
                //Set the amount of time between each execution (in milliseconds)
                10000);
*/
        connectivityReceiver = new ConnectivityReceiver();

    }

    private void resolveIntent() {
        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();
        if (bundle.containsKey("permissionsEnabled") && bundle.containsKey("dbHasData")) {
            permissionsEnabled = bundle.getBoolean("permissionsEnabled");
            dBHasData = bundle.getBoolean("dbHasData");

            if (dBHasData) {
                SharedPreferences spref = getSharedPreferences("USER", MODE_PRIVATE);

                if (spref.contains("isCurrent")) {
                    Boolean isCurrent = spref.getBoolean("isCurrent", false);
                    if (isCurrent && spref.contains("City")) {
                        title.setText(spref.getString("City", ""));
                    } else if (spref.contains("StationCity")) {
                        title.setText(spref.getString("StationCity", ""));
                    }
                }
            }

//            SharedPreferences spref = getSharedPreferences("USER", MODE_PRIVATE);
            if (/*spref.contains("isCurrent") &&*/ appLaunched && !dBHasData && permissionsEnabled) {
                locationButtonPressed = true;
                // to stop api call when app is launched
                appLaunched = false;
            }


            changeLayoutToTabs(permissionsEnabled, dBHasData);


        }
    }

    private void changeLayoutToTabs(boolean permissionsEnabled, boolean dBHasData) {
        try {
            Log.i("Main Activity = ", dBHasData ? "dbHasData = " + "true" : "dbHasData = " + "false");
            if (/*!utils.checkNetworkState(MainActivity.this)&&*/!dBHasData && !permissionsEnabled || !dBHasData && !utils.canGetLocation(MainActivity.this)) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                LocationEnablerFragment locationEnablerFragment = new LocationEnablerFragment();
                fragmentTransaction.replace(R.id.fragmentContainer, locationEnablerFragment, "LocationEnablerFragment");
                fragmentTransaction.commit();
                isTabLayout = false;
//          setToolbarLocationIconEnabled(false);

            } else {
                if (permissionsEnabled) {
                    if (utils.canGetLocation(MainActivity.this) == true) {
                        //DO SOMETHING USEFUL HERE. ALL GPS PROVIDERS ARE CURRENTLY ENABLED
                   /* SharedPreferences spref = getSharedPreferences("USER", MODE_PRIVATE);
                    SharedPreferences.Editor editor = spref.edit();
                    editor.putBoolean("isCurrent", true);
                    editor.commit(); */
                        Log.i("Service Binded = ", "3");
//                        locationButtonPressed = true;
                        bindService();
                        Log.i("changeLayoutToTabs", "changeLayoutToTabs");
                    }
//             setToolbarLocationIconEnabled(true);
                } else {
//             setToolbarLocationIconEnabled(false);
                }
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                TabsFragment tabsFragment = TabsFragment.getInstance();
                fragmentTransaction.replace(R.id.fragmentContainer, tabsFragment, "TabsFragment");
                fragmentTransaction.commit();
                isTabLayout = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean bindService() {
        if (!mServiceBound) {
            Intent intent = new Intent(this, LocationService.class);
            startService(intent);
            bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);

            LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                    new IntentFilter(LocationService.SERVICE_NAME)
            );
            Log.i("Service Binded", " true");
            return true;
        } else {
            Log.i("Service Binded", " already");
            return false;
        }
    }

    public void unBindService() {
        if (mServiceConnection != null) {
            unbindService(mServiceConnection);
            mBoundService.stopSelf();
            mServiceConnection = null;
            mServiceBound = false;
            Log.i("Service UnBinded", " true");
        } else {
            Log.i("Service UnBinded", " already");
        }
    }

    public void updateForecast() {
        isRefresh = true;
        isRefreshCurrent = true;
        locationButtonPressed = true;
        setCurrentCity();
    }

    public void setCurrentCity() {
        Log.i("ApiMultiRun = ", "0");
        SharedPreferences spref = getSharedPreferences("USER", MODE_PRIVATE);

        if (spref.contains("isCurrent")) {
            Log.i("ApiMultiRun1 = ", "0");
            Boolean isCurrent = spref.getBoolean("isCurrent", false);
            if (isCurrent) {
                Log.i("ApiMultiRun2 = ", "0");
                if (spref.contains("City")) {
                    title.setText(spref.getString("City", ""));
                    Log.i("getForecast = ", "0");
                    if (locationButtonPressed) {
                        Log.i("getForecast = ", "00");
                        getForecast("0", String.valueOf(spref.getFloat("lat", 0)), String.valueOf(spref.getFloat("long", 0)), "0");
                        locationButtonPressed = false;
                    }
                    lat = String.valueOf(spref.getFloat("lat", 0));
                    lon = String.valueOf(spref.getFloat("long", 0));
//                    getCurrent(String.valueOf(spref.getFloat("lat", 0)), String.valueOf(spref.getFloat("long", 0)));
                } else {
//                    title.setText("");
                }
            } else {
                if (spref.contains("StationCity")) {
                    title.setText(spref.getString("StationCity", ""));
                    lat = String.valueOf(spref.getFloat("StationLat", 0));
                    lon = String.valueOf(spref.getFloat("StationLong", 0));
                    if (locationButtonPressed) {
                        Log.i("getForecast = ", "00");
                        getForecast("1", String.valueOf(spref.getFloat("lat", 0)), String.valueOf(spref.getFloat("long", 0)), spref.getString("StationId", "0"));
                        locationButtonPressed = false;
                    }
                } else {
//                    title.setText("");
                }
            }
        } else if (!DbUtils.hasForecast() && utils.checkNetworkState(MainActivity.this) && utils.canGetLocation(MainActivity.this)) {
            Log.i("ApiMultiRun2 = ", "0");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
            Log.i("getForecast = ", "1");
            if (toolbar.getMenu().findItem(R.id.action_location) != null) {
                toolbar.getMenu().findItem(R.id.action_location).setIcon(R.drawable.ic_action_my_location);
            }
            getForecast("0", String.valueOf(spref.getFloat("lat", 0)), String.valueOf(spref.getFloat("long", 0)), "0");
            lat = String.valueOf(spref.getFloat("lat", 0));
            lon = String.valueOf(spref.getFloat("long", 0));
        }
        if (spref.contains("image")) {
            BackgroundUtils.setBackgroundUrl(spref.getString("image", ""));
        }
    }

    private void getCurrent(final String lat, final String aLong) {
        if (progressBar != null && !isRefreshCurrent) {
            progressBar.setVisibility(View.VISIBLE);
        }
        isRefreshCurrent = false;
        InitApi initApi = new InitApi() {
            @Override
            public HashMap<String, String> getBody() {
                HashMap<String, String> body = new HashMap<>();
                body.put("latitude", lat);
                body.put("longitude", aLong);
                return body;
            }

            @Override
            public HashMap<String, String> getHeader() {
                HashMap<String, String> header = new HashMap<>();
//                header.put("Content-Type", "application/json");
                return header;
            }
        };

        Log.i("body", initApi.getBody().toString());
        StringRequestCall getForeCast = new StringRequestCall(initApi.getHeader(), initApi.getBody(), ApiCalls.GET_CURRENT, Request.Method.POST, this, apiResponseListener);
        getForeCast.sendData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.getInstance().setLocationConnectivityListener(this);
        App.getInstance().setConnectivityListener(this);
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onDestroy() {
        unBindService();
        unregisterReceiver(connectivityReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void initializeViews() {

        apiResponseListener = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        title = (TextView) findViewById(R.id.title);
        title.setText("");

        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        DrawerArrowDrawable aero = new DrawerArrowDrawable(this);
        aero.setBarLength(50);
        aero.setColor(Color.WHITE);
        aero.setBarThickness(5);
        aero.setGapSize(10);
        toggle.setDrawerArrowDrawable(aero);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getBackground().setColorFilter(getResources().getColor(R.color.black_overlay_navigation_view)/*0xE6FFFFFF*/, PorterDuff.Mode.SRC);
        headerView.getBackground().setColorFilter(getResources().getColor(R.color.black_overlay_navigation_header)/*0x20FFFFFF*/, PorterDuff.Mode.DST_OVER);

        /*final Spinner mySpinner = (Spinner) findViewById(R.id.spinner);
        final LocationSpinnerAdapter adapter = new LocationSpinnerAdapter(MainActivity.this, AppWideVariables.getStationList()==null?new ArrayList<StationModel>(): AppWideVariables.getStationList());
        mySpinner.setAdapter(adapter);*/


//        final float slideOfset = 0.01f;

        drawer.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes

                      /*  if (slideOffset > 0f) {
                            if (slideOffset - slideOfset > 0 && slideOffset - slideOfset < 0.01f) {
                                Bitmap bmp = BlurBuilder.blurRefreshed(coordinatorLayout, 15f);
                                findViewById(R.id.blurView).setBackground(new BitmapDrawable(getResources(), bmp));
                                findViewById(R.id.blurView).setVisibility(View.VISIBLE);
                            } else if (slideOffset - slideOfset < 0f) {
//                                Bitmap bmp = BlurBuilder.blurRefreshed(coordinatorLayout, 0.1f*//**slideOffset*//* + 0.1f);
//                                findViewById(R.id.blurView).setBackground(new BitmapDrawable(getResources(), bmp));
                                findViewById(R.id.blurView).setVisibility(View.GONE);
                            }
                        } else
                            findViewById(R.id.blurView).setVisibility(View.GONE);
*/
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Respond when the drawer is opened
//                      BlurKit.getInstance().blur(coordinatorLayout,10);
                        Bitmap bmp = BlurBuilder.blurRefreshed(coordinatorLayout, 5f);
                        findViewById(R.id.blurView).setBackground(new BitmapDrawable(getResources(), bmp));
                        findViewById(R.id.blurView).setVisibility(View.VISIBLE);
//                        isClosed = false;
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Respond when the drawer is closed
//                        BlurKit.getInstance().blur(drawer,1);
//                        findViewById(R.id.blurView).setBackground(null);

                        findViewById(R.id.blurView).setVisibility(View.GONE);
//                        isClosed = true;
//                        shouldBlur = true;
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                       /* if (newState == DrawerLayout.STATE_DRAGGING && isClosed &&shouldBlur) {
                            Bitmap bmp = BlurBuilder.blurRefreshed(coordinatorLayout, 5f);
                            findViewById(R.id.blurView).setBackground(new BitmapDrawable(getResources(), bmp));
                            findViewById(R.id.blurView).setVisibility(View.VISIBLE);
                            shouldBlur=false;
                        } else if (newState == DrawerLayout.STATE_IDLE && isClosed) {
                            findViewById(R.id.blurView).setVisibility(View.GONE);
                        }*/
                    }
                }
        );

        fragment = findViewById(R.id.searchDropdown);
        fragment.setVisibility(View.GONE);

        backgroundView = (BlurImageView) findViewById(R.id.backgroundView);
        backgroundViewOverlayToday = (ImageView) findViewById(R.id.backgroundViewOverlay);
        backgroundViewOverlayHourly = (ImageView) findViewById(R.id.backgroundViewOverlayHourly);
        backgroundViewOverlayThreeDays = (ImageView) findViewById(R.id.backgroundViewOverlayThreeDays);

        setupListeners();
        setupupSearchDropDown();


    }

    private void setupupSearchDropDown() {
        rootLayout = (LinearLayout) fragment.findViewById(R.id.rootLayout);
        rVSearchDropDown = (RecyclerView) fragment.findViewById(R.id.rVLocations);
        searchAdapter = new SearchRecyclerAdapter(MainActivity.this, searchDropdownListener, rootLayout);
        rVSearchDropDown.setAdapter(searchAdapter);
        LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        rVSearchDropDown.setLayoutManager(manager);
//        LinearLayout linearLayout = (LinearLayout) fragment.findViewById(R.id.lLCurrentLocation);
    }

    private void setupListeners() {

//        setup listeners
        backgroundUpdateListener = this;
        searchDropdownListener = this;
        BackgroundUtils.setBackgroundUpdateListener(backgroundUpdateListener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                setToolbarLocationIconEnabled(false);
            } else {
                if (utils.checkLocationState()) {
                    setToolbarLocationIconEnabled(true);
                } else {
                    setToolbarLocationIconEnabled(false);
                }
            }
        } else {
            if (utils.checkLocationState()) {
                setToolbarLocationIconEnabled(true);
            } else {
                setToolbarLocationIconEnabled(false);
            }
        }

        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
//        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

        searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.white_overlay));
        searchEditText.setHint("Search Location...");

        final ImageView closeButtonImage = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        closeButtonImage.setImageResource(R.drawable.ic_action_close);
        closeButtonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collapseSearchView();
            }
        });


        ImageView mSearchHintIcon = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        mSearchHintIcon.setImageResource(R.drawable.ic_action_search);

        ImageView mSearchButton = searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        mSearchButton.setImageResource(R.drawable.ic_action_search);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandSearchView();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
//                    adapter.filter("");
//                    listView.clearTextFilter();
                    rVSearchDropDown.setVisibility(View.GONE);
                } else {
//                    adapter.filter(newText);
                    rVSearchDropDown.setVisibility(View.VISIBLE);
                    searchAdapter.filter(newText);
                }
                isSearchExpanded = true;
                return true;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_search) {
            return true;
        }
        if (id == R.id.action_location) {
            locationButtonPressed = true;
            checkLocationPermissionEnabled();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkLocationPermissionEnabled() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, PermissionsRequest.LOCATION_PERMISSIONS, PermissionsRequest.LOCATION_REQUEST_CODE);
            } else {
                if (utils.canGetLocation(MainActivity.this) == true) {
                    //DO SOMETHING USEFUL HERE. ALL GPS PROVIDERS ARE CURRENTLY ENABLED
                    if (utils.checkNetworkState(MainActivity.this)) {
                        //SHOW OUR SETTINGS ALERT, AND LET THE USE TURN ON ALL THE GPS PROVIDERS
                        Log.i("Service Binded = ", "4");
                        if (!bindService()) {
                            SharedPreferences spref = getSharedPreferences("USER", MODE_PRIVATE);
                            SharedPreferences.Editor editor = spref.edit();
                            editor.putBoolean("isCurrent", true);
                            editor.commit();
                            Log.i("setCurrentCity = ", "2");
                            locationButtonPressed = true;
                            setCurrentCity();
                            toolbar.getMenu().findItem(R.id.action_location).setEnabled(false);
                        } else {
                            SharedPreferences spref = getSharedPreferences("USER", MODE_PRIVATE);
                            SharedPreferences.Editor editor = spref.edit();
                            editor.putBoolean("isCurrent", true);
                            editor.commit();
                            Log.i("setCurrentCity = ", "3");
                            locationButtonPressed = true;
                            setCurrentCity();
                            toolbar.getMenu().findItem(R.id.action_location).setEnabled(false);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "You are not connected to Internet", Toast.LENGTH_LONG).show();
                    }
                    Log.i("actionLocation", "actionLocation");
//                        setCurrentCity();
                } else {
                    if (utils.checkNetworkState(MainActivity.this)) {
                        //SHOW OUR SETTINGS ALERT, AND LET THE USE TURN ON ALL THE GPS PROVIDERS
                        utils.showLocationSettingsAlert(MainActivity.this);
                    } else {
                        Toast.makeText(MainActivity.this, "You are not connected to Internet", Toast.LENGTH_LONG).show();
                    }
                }
            }
        } else {
            if (utils.canGetLocation(MainActivity.this) == true) {
                //DO SOMETHING USEFUL HERE. ALL GPS PROVIDERS ARE CURRENTLY ENABLED
                if (utils.checkNetworkState(MainActivity.this)) {
                    //SHOW OUR SETTINGS ALERT, AND LET THE USE TURN ON ALL THE GPS PROVIDERS
                    Log.i("Service Binded = ", "5");

                    if (!bindService()) {
                        SharedPreferences spref = getSharedPreferences("USER", MODE_PRIVATE);
                        SharedPreferences.Editor editor = spref.edit();
                        editor.putBoolean("isCurrent", true);
                        editor.commit();
                        Log.i("setCurrentCity = ", "4");
                        locationButtonPressed = true;
                        setCurrentCity();
                        toolbar.getMenu().findItem(R.id.action_location).setEnabled(false);

                    } else {
                        SharedPreferences spref = getSharedPreferences("USER", MODE_PRIVATE);
                        SharedPreferences.Editor editor = spref.edit();
                        editor.putBoolean("isCurrent", true);
                        editor.commit();
                        Log.i("setCurrentCity = ", "5");
                        locationButtonPressed = true;
                        setCurrentCity();
                        toolbar.getMenu().findItem(R.id.action_location).setEnabled(false);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "You are not connected to Internet", Toast.LENGTH_LONG).show();
                }
                Log.i("actionLocation else", "actionLocation else");
//                        setCurrentCity();
            } else {
                if (utils.checkNetworkState(MainActivity.this)) {
                    //SHOW OUR SETTINGS ALERT, AND LET THE USE TURN ON ALL THE GPS PROVIDERS
                    utils.showLocationSettingsAlert(MainActivity.this);
                } else {
                    Toast.makeText(MainActivity.this, "You are not connected to Internet", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void setBackground(int pos) {
        //set background

        this.position = pos;

        if (pos <= 10) {
            Glide.with(MainActivity.this).load(BackgroundUtils.getBackgroundUrl() != null && BackgroundUtils.getBackgroundUrl().length() > 1 ? BackgroundUtils.getBackgroundUrl() : BackgroundUtils.getBackgroundId()).transition(DrawableTransitionOptions.withCrossFade(1000)).into(backgroundView);
//            backgroundView.setImageDrawable(getResources().getDrawable(BackgroundUtils.getBackgroundId()));
            backgroundView.setBlur(0);
            blurred = false;
            Log.i("pos blur0", " = " + position);
        } else if (pos > 10 && !blurred) {
            Log.i("pos blur1", " = " + position);
//            backgroundView.setImageBitmap(BlurBuilder.blur(backgroundView, 2.5f));
//            backgroundView.setImageDrawable(getResources().getDrawable(BackgroundUtils.getBackgroundIdBlur()));
            Glide.with(MainActivity.this).load(BackgroundUtils.getBackgroundUrl() != null && BackgroundUtils.getBackgroundUrl().length() > 1 ? BackgroundUtils.getBackgroundUrl() : BackgroundUtils.getBackgroundId()).transition(DrawableTransitionOptions.withCrossFade(2000)).into(backgroundView);
//            backgroundView.setImageDrawable(getResources().getDrawable(BackgroundUtils.getBackgroundId()));
            backgroundView.setBlur(2);
            blurred = true;
        }


        //        updatebackground();
/*
        if (position <= 10) {
            backgroundView.setImageDrawable(getResources().getDrawable(BackgroundUtils.getBackgroundId()));
            blurred = false;
            Log.i("pos blur0", " = " + position);
        } else if (position > 10 && !blurred) {
            Log.i("pos blur1", " = " + position);
//            backgroundView.setImageBitmap(BlurBuilder.blur(backgroundView, 2.5f));
            backgroundView.setImageDrawable(getResources().getDrawable(BackgroundUtils.getBackgroundIdBlur()));
            blurred = true;
        }
*/

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.contactUs) {
            Uri webpage = Uri.parse(ApiCalls.CONTACT_US_PMD);
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                //Page not found
            }
        } else if (id == R.id.aboutUs) {
            Uri webpage = Uri.parse(ApiCalls.ABOUT_US_PMD);
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                //Page not found
            }
        }

        /*if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void updatebackground() {
        Log.i("updatebackground", " called ");

//        update background morning/evening
//        blur or clear background

        if (position == 0) {
            Glide.with(MainActivity.this).load(BackgroundUtils.getBackgroundUrl() != null && BackgroundUtils.getBackgroundUrl().length() > 1 ? BackgroundUtils.getBackgroundUrl() : BackgroundUtils.getBackgroundId())./*transition(DrawableTransitionOptions.withCrossFade(1000)).*/into(backgroundView);
//            backgroundView.setBlur(0);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backgroundView.setBlur(0);
                }
            }, 100);
//                    backgroundView.setImageDrawable(getResources().getDrawable(BackgroundUtils.getBackgroundId()));
//                    backgroundView.invalidate();
            Log.i("pos blur00", " = " + position);
        } else if (position > 0) {
            Log.i("pos blur10", " = " + position);
            Glide.with(MainActivity.this).load(BackgroundUtils.getBackgroundUrl() != null && BackgroundUtils.getBackgroundUrl().length() > 1 ? BackgroundUtils.getBackgroundUrl() : BackgroundUtils.getBackgroundId())./*transition(DrawableTransitionOptions.withCrossFade(1000)).*/into(backgroundView);
//            backgroundView.setBlur(2);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backgroundView.setBlur(2);
                }
            }, 100);
//                    backgroundView.setImageDrawable(getResources().getDrawable(BackgroundUtils.getBackgroundIdBlur()));
        }


    }

    @Override
    public void onBackPressed() {
        handleBackKeyEvent();
    }

    public void handleBackKeyEvent() {

        if (isSearchExpanded) {
            collapseSearchView();
            return;
        }

        if (shouldCloseApp) {
            shouldCloseApp = false;
            super.onBackPressed();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity();
            }
        } else {
            shouldCloseApp = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    shouldCloseApp = false;
                }
            }, 500);
        }
    }

    public void collapseSearchView() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
        isSearchExpanded = false;
        searchView.onActionViewCollapsed();
        searchEditText.getText().clear();
        fragment.setVisibility(View.GONE);
        rVSearchDropDown.setVisibility(View.GONE);
        searchAdapter.getStationModelsList().clear();
        searchEditText.clearFocus();
//        setCurrentCity();
    }

    public void expandSearchView() {
        isSearchExpanded = true;
        searchView.onActionViewExpanded();
        fragment.setVisibility(View.VISIBLE);
    }

    @Override
    public void setSelectedLocation(StationModel stationModel) {
        Log.i("ApiMultiRun = ", "1");
        collapseSearchView();
        if (utils.checkNetworkState(MainActivity.this)) {
            title.setText(stationModel.getStationName());
            Log.i("getForecast = ", "2");
            getForecast("1", stationModel.getStationLatitude(), stationModel.getStationLongitude(), stationModel.getStationId() + "");
            lat = stationModel.getStationLatitude();
            lon = stationModel.getStationLongitude();
//            getCurrent(stationModel.getStationLatitude(), stationModel.getStationLongitude());
            SharedPreferences spref = getSharedPreferences("USER", MODE_PRIVATE);
            SharedPreferences.Editor editor = spref.edit();
            editor.putFloat("StationLat", Float.parseFloat(stationModel.getStationLatitude()));
            editor.putFloat("StationLong", Float.parseFloat(stationModel.getStationLongitude()));
            editor.putString("StationCity", stationModel.getStationName());
            editor.putString("StationId", String.valueOf(stationModel.getStationId()));
//            editor.putBoolean("isCurrent", false);
            editor.commit();
        } else {
            Toast.makeText(MainActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void getForecast(final String isStation, final String latitude, final String longitude, final String stationId) {
        if (progressBar != null && !isRefresh) {
            progressBar.setVisibility(View.VISIBLE);
        }
        isRefresh = false;
        if (!utils.checkNetworkState(MainActivity.this))
            return;

        InitApi initApi = new InitApi() {
            @Override
            public HashMap<String, String> getBody() {
                HashMap<String, String> body = new HashMap<>();
                body.put("is_station", isStation);
                body.put("latitude", latitude);
                body.put("longitude", longitude);
                if (isStation == "1") {
                    body.put("station_id", stationId);
                    SharedPreferences spref = getSharedPreferences("USER", MODE_PRIVATE);
                    SharedPreferences.Editor editor = spref.edit();
                    editor.putBoolean("isCurrent", false);
                    editor.commit();
                } else {
                    body.put("station_id", "0");
                    SharedPreferences spref = getSharedPreferences("USER", MODE_PRIVATE);
                    SharedPreferences.Editor editor = spref.edit();
                    editor.putBoolean("isCurrent", true);
                    editor.commit();
                }
                return body;
            }

            @Override
            public HashMap<String, String> getHeader() {
                HashMap<String, String> header = new HashMap<>();
//                header.put("Content-Type", "application/json");
                return header;
            }
        };

        Log.i("body", initApi.getBody().toString());
        StringRequestCall getForeCast = new StringRequestCall(initApi.getHeader(), initApi.getBody(), ApiCalls.GET_FORECAST, Request.Method.POST, this, apiResponseListener);
        getForeCast.sendData();
    }

    public void getStations() {
        InitApi initApi = new InitApi() {
            @Override
            public HashMap<String, String> getBody() {
                return null;
            }

            @Override
            public HashMap<String, String> getHeader() {
                return new HashMap<>();
            }
        };

        StringRequestCall stringRequestCall = new StringRequestCall(initApi.getHeader(), null, ApiCalls.GET_STATIONS, Request.Method.GET, this, apiResponseListener);
        stringRequestCall.sendData();
    }

    @Override
    public void setApiResponse(JSONObject jsonObject, String calledApi) {

        if (calledApi == ApiCalls.GET_FORECAST) {
            Log.i("GET Stations", jsonObject.toString());
            ForecastHandler handler = new ForecastHandler(MainActivity.this, jsonObject);
            handler.handleResponse();
            if (!isTabLayout) {
                changeLayoutToTabs(permissionsEnabled, true);
            }
            /*if (timer != null) {
                timer.cancel();
            }*/
        } else if (calledApi == ApiCalls.GET_CURRENT) {
            Log.i("GET Stations", jsonObject.toString());
            CurrentHandler handler = new CurrentHandler(MainActivity.this, jsonObject);
            handler.handleResponse();
            if (!isTabLayout) {
                changeLayoutToTabs(permissionsEnabled, true);
            }
            /*if (timer != null) {
                timer.cancel();
            }*/
        } else if (calledApi == ApiCalls.GET_STATIONS) {
            StationHandler stationHandler = new StationHandler(MainActivity.this, jsonObject);
            stationHandler.handleResponse();
        }

    }

    public void updateCurrent() {
        isRefreshCurrent = true;
        getCurrent(lat, lon);
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

    public void updateForecast(List<ForecastModel> forecastList) {
        changeLayoutToTabs(permissionsEnabled, true);
        updatebackground();
        if (TabsFragment.getInstance() != null && TabsFragment.getInstance().getAdapter() != null) {
            ((HourlyFragment) TabsFragment.getInstance().getAdapter().getItem(TabUtils.TAB_HOURLY)).updateData(forecastList);
            ((ThreeDaysFragment) TabsFragment.getInstance().getAdapter().getItem(TabUtils.TAB_3DAYS)).updateData(forecastList);
        }
        getCurrent(lat, lon);
//        toolbar.getMenu().findItem(R.id.action_location).setEnabled(true);
    }

    public void updateCurrent(CurrentModel currentModel) {
        changeLayoutToTabs(permissionsEnabled, true);
//        updatebackground();
        if (TabsFragment.getInstance() != null && TabsFragment.getInstance().getAdapter() != null) {
//            ((TodayFragment) TabsFragment.getInstance().getAdapter().getItem(TabUtils.TAB_TODAY)).updateData(currentModel);
            ((TodayFragmentWithoutList) TabsFragment.getInstance().getAdapter().getItem(TabUtils.TAB_TODAY)).updateData(currentModel);
        }
        toolbar.getMenu().findItem(R.id.action_location).setEnabled(true);
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        utils.updateWidget();
    }

    public void setBackgroundOverlayVisibility(int visible) {
        backgroundViewOverlayToday.setVisibility(visible);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
//            Toast.makeText(MainActivity.this, "MA Internet connected", Toast.LENGTH_LONG).show();
            Log.i("Internet = ", "Connected");
            if (DbUtils.getStations() == null || AppWideVariables.getStationList() == null) {

              /*  if (timer == null) {
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            //Called each time when 10000 milliseconds (10 second) (the period parameter)
//                                      BackgroundUtils.settime();
                            Log.i("Timer Running", " = true");
                            if (ConnectivityReceiver.isConnected()) {*/
                getStations();
/*
                            }
                        }
                    }, 0, 2000);
                }
*/
            }
        } else {
//            Toast.makeText(MainActivity.this, "MA Internet disconnected", Toast.LENGTH_LONG).show();
            Log.i("Internet = ", "Disconnected");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOACTION_ENABLE_REQUEST) {
            checkLocationPermissionEnabled();
        }

    }

    public void requestLocationIntent() {
        Intent intent = new Intent(
                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, LOACTION_ENABLE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionsRequest.LOCATION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    Log.i("PermissionResult = 0 ", "permissionResult");
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this, PermissionsRequest.LOCATION_FINE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, PermissionsRequest.LOCATION_COARSE) == PackageManager.PERMISSION_GRANTED) {
                        //Request location updates:
                        if (utils.canGetLocation(MainActivity.this)) {
                            //DO SOMETHING USEFUL HERE. ALL GPS PROVIDERS ARE CURRENTLY ENABLED
                            setToolbarLocationIconEnabled(true);
                            if (utils.checkNetworkState(MainActivity.this)) {
                                //SHOW OUR SETTINGS ALERT, AND LET THE USE TURN ON ALL THE GPS PROVIDERS
                                Log.i("PermissionResult = 0 ", "bind");
                                Log.i("Service Binded = ", "6");
                                permissionsEnabled = true;
                                locationButtonPressed = true;
                                SharedPreferences spref = getSharedPreferences("USER", MODE_PRIVATE);
                                SharedPreferences.Editor editor = spref.edit();
                                editor.putBoolean("isCurrent", true);
                                editor.commit();
                                bindService();
                            } else {
                                Toast.makeText(MainActivity.this, "You are not connected to Internet", Toast.LENGTH_LONG).show();
                            }
                            Log.i("Permissions", "onRequestPermissionsResult");
//                        setCurrentCity();
                        } else {
                            utils.showLocationSettingsAlert(MainActivity.this);
                            if (utils.checkNetworkState(MainActivity.this)) {
                                //SHOW OUR SETTINGS ALERT, AND LET THE USE TURN ON ALL THE GPS PROVIDERS
                            } else {
                                Toast.makeText(MainActivity.this, "You are not connected to Internet", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                } else {
                    // permission denied! Disable the
                    // functionality that depends on this permission.
                    permissionsEnabled = false;
                    setToolbarLocationIconEnabled(false);
                    Log.i("PermissionResult = 1 ", "permissionResult");
                }
                return;
            }
        }
    }

    @Override
    public void onLocationEnabledStateChanged(boolean isConnected) {

        if (isConnected) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION],0);
                    ActivityCompat.requestPermissions(MainActivity.this, PermissionsRequest.LOCATION_PERMISSIONS, PermissionsRequest.LOCATION_REQUEST_CODE);
                } else {
                    Log.i("onPermissionsAllowed", "1");
                    setToolbarLocationIconEnabled(true);
                    //LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
                }
            } else {
                Log.i("onPermissionsAllowed", "2");
                setToolbarLocationIconEnabled(true);
                //LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            }
        } else {
            setToolbarLocationIconEnabled(false);
        }

    }

    public void setToolbarLocationIconEnabled(boolean enabled) {
        if (toolbar.getMenu().findItem(R.id.action_location) != null) {
            if (enabled) {
                toolbar.getMenu().findItem(R.id.action_location).setIcon(R.drawable.ic_action_my_location);
            } else {
                toolbar.getMenu().findItem(R.id.action_location).setIcon(R.drawable.ic_action_location_disabled);

            }
        }
    }
}
