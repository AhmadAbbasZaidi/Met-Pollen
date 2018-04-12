package com.cfp.metpollen.view.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.icu.text.LocaleDisplayNames;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.cfp.metpollen.App;
import com.cfp.metpollen.R;
import com.cfp.metpollen.data.api.ApiCalls;
import com.cfp.metpollen.data.api.Handlers.StationHandler;
import com.cfp.metpollen.data.api.Interface.InitApi;
import com.cfp.metpollen.data.api.Requests.StringRequestCall;
import com.cfp.metpollen.data.db.DbUtils;
import com.cfp.metpollen.presenter.ApiResponseListener;
import com.cfp.metpollen.view.backgroundService.GetStationsService;
import com.cfp.metpollen.view.broadcastRecievers.ConnectivityReceiver;
import com.cfp.metpollen.view.utilities.PermissionsRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class SplashActivity extends AppCompatActivity implements ApiResponseListener, ConnectivityReceiver.ConnectivityReceiverListener {
    private static SplashActivity splashActivity;
    private ApiResponseListener apiResponseListener;

    public static SplashActivity getInstance() {
        return splashActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        splashActivity = this;
        // Manually checking internet connection

        requestPermissions();
        Intent intent = new Intent(SplashActivity.this, GetStationsService.class);
        startService(intent);
    }

    public void requestPermissions()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION],0);
                ActivityCompat.requestPermissions(SplashActivity.this, PermissionsRequest.LOCATION_PERMISSIONS, PermissionsRequest.LOCATION_REQUEST_CODE);
            } else {
                Log.i("onPermissionsAllowed", "1");
                onPermissionsAllowed(true);
                //LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            }
        } else {
            Log.i("onPermissionsAllowed", "2");
            onPermissionsAllowed(true);
            //LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Resume", "true");
        App.getInstance().setConnectivityListener(this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionsRequest.LOCATION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this, PermissionsRequest.LOCATION_FINE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, PermissionsRequest.LOCATION_COARSE) == PackageManager.PERMISSION_GRANTED) {
                        //Request location updates:

                        Log.i("onPermissionsAllowed", "3");
                        onPermissionsAllowed(true);
                    }

                } else {

                    Log.i("onPermissionsAllowed", "4");
                    onPermissionsAllowed(false);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    public void onPermissionsAllowed(final boolean permissionsEnabled) {

        final boolean dbHasData = DbUtils.hasForecast();
        Log.i("onPermissionsAllowed", "5 "+"dbHasData = "+dbHasData);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("permissionsEnabled", permissionsEnabled);
                intent.putExtra("dbHasData", dbHasData);
                Log.i("Splash Activity = ", permissionsEnabled ? "permissionsEnabled = " + "true" : "permissionsEnabled = " + "false");
                Log.i("Splash Activity = ", dbHasData ? "dbHasData = " + "true" : "dbHasData = " + "false");
                startActivity(intent);
            }
        }, 1500);

       /* apiResponseListener = this;
        if (checkConnection()) {
            getStations();
        }*/
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AppWideVariables.setStationList(DbUtils.getStations());
                if (AppWideVariables.getStationList() != null && AppWideVariables.getStationList().size() > 0) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
//                    finish();
                } else {
                    Toast.makeText(SplashActivity.this, getResources().getString(R.string.please_connect_to_internet), Toast.LENGTH_LONG).show();
                }
            }
        }, 3000);*/
    }


    @Override
    public void setApiResponse(JSONObject jsonObject, String calledApi) {
        Log.i("String Response = ", jsonObject.toString());

        if (calledApi == ApiCalls.GET_STATIONS) {
            StationHandler stationHandler = new StationHandler(SplashActivity.this, jsonObject);
            stationHandler.handleResponse();
        }
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

    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
        return isConnected;
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.progressBar), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
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

        StringRequestCall stringRequestCall = new StringRequestCall(initApi.getHeader(), null, ApiCalls.GET_STATIONS, Request.Method.GET, null, apiResponseListener);
        stringRequestCall.sendData();
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
//            Toast.makeText(SplashActivity.this, "SA Internet connected", Toast.LENGTH_LONG).show();
            getStations();
        } else {
//            Toast.makeText(SplashActivity.this, "SA Internet disconnected", Toast.LENGTH_LONG).show();
            Log.i("Internet = ", "Disconnected");
        }
        showSnack(isConnected);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
