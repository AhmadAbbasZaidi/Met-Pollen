package com.cfp.metpollen.view.backgroundService;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.cfp.metpollen.data.api.ApiCalls;
import com.cfp.metpollen.data.api.Handlers.StationHandler;
import com.cfp.metpollen.data.api.Interface.InitApi;
import com.cfp.metpollen.data.api.Requests.StringRequestCall;
import com.cfp.metpollen.presenter.ApiResponseListener;
import com.cfp.metpollen.view.activities.SplashActivity;
import com.cfp.metpollen.view.utilities.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by AhmedAbbas on 12/22/2017.
 */

public class GetStationsService extends IntentService implements ApiResponseListener {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;
    private static GetStationsService INSTANCE = null;
    private ApiResponseListener apiResponseListener;

    public GetStationsService() {
        super(GetStationsService.class.getName());
        Log.i("GetStationsService()", "GetStationsService.class.getName()");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.i("GetStationsService()", "onHandleIntent()");
        apiResponseListener = this;
        if (utils.checkNetworkState(SplashActivity.getInstance())) {
            getStations();
        } else {
            stopSelf();
        }

    }

    @Override
    public void setApiResponse(JSONObject jsonObject, String calledApi) {

        Log.i("GetStationsService()", "setApiResponse()");
        if (calledApi == ApiCalls.GET_STATIONS) {
            StationHandler stationHandler = new StationHandler(null, jsonObject);
            stationHandler.setGetStationsService(this);
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

    public void getStations() {
        Log.i("GetStationsService()", "getStations()");
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

        StringRequestCall stringRequestCall = new StringRequestCall(initApi.getHeader(), null, ApiCalls.GET_STATIONS, Request.Method.GET, SplashActivity.getInstance(), apiResponseListener);
        stringRequestCall.sendData();
    }
}
