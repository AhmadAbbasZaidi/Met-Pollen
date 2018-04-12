package com.cfp.metpollen.data.api;

import android.app.Activity;

import com.cfp.metpollen.presenter.ApiResponseListener;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by AhmedAbbas on 11/24/2017.
 */

public class ApiHandler {

    protected final String baseUrl = "http://demo.lmkt.com/pmdpollen/";

    protected JSONObject alternatebody;
    protected JSONObject responseJson;
    protected String calledApi;
    protected int method;
    protected Map<String, String> body;
    protected Map<String, String> header;
    protected Activity activity;
    protected BaseResponseHandler responseHandler;
    protected boolean isAlternate = false;
    protected ApiResponseListener apiResponseListener;

    public ApiHandler(Map<String, String> header, Map<String, String> body, String calledApi, int method, Activity activity, ApiResponseListener apiResponseListener) {
        this.header = header;
        this.body = body;
        this.calledApi = calledApi;
        this.method = method;
        this.activity = activity;
        this.apiResponseListener = apiResponseListener;
    }

    public ApiHandler(Map<String, String> header, JSONObject body, String calledApi, int method, Activity activity, boolean isAlternate, ApiResponseListener apiResponseListener) {
        this.header = header;
        this.alternatebody = body;
        this.calledApi = calledApi;
        this.method = method;
        this.activity = activity;
        this.isAlternate = isAlternate;
        this.apiResponseListener = apiResponseListener;
    }

}
