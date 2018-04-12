package com.cfp.metpollen.data.api;

import android.app.Activity;

import com.android.volley.RequestQueue;
import com.cfp.metpollen.presenter.ApiResponseListener;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Ahmad Abbas Zaidi on 3/21/2017.
 */

public abstract class BaseResponseHandler {

    protected static RequestQueue requestQueue;
    protected Activity activity;
    protected JSONObject responseJson;
    protected String responseString;
    protected String calledApi;
    protected Map<String, String> body;
    protected JSONObject alternateBody;
    protected Map<String, String> headers;
    protected ApiResponseListener apiResponseListener;

    public BaseResponseHandler(Activity activity, String calledApi, Map<String, String> body, Map<String, String> headers,ApiResponseListener apiResponseListener) {
        this.activity = activity;
        this.calledApi = calledApi;
        this.body = body;
        this.headers = headers;
        this.apiResponseListener =apiResponseListener;
    }

    public BaseResponseHandler(Activity activity, String calledApi, JSONObject alternateBody, Map<String, String> headers,ApiResponseListener apiResponseListener) {
        this.activity = activity;
        this.calledApi = calledApi;
        this.alternateBody = alternateBody;
        this.headers = headers;
        this.apiResponseListener =apiResponseListener;
    }


    public abstract void setResponse(JSONObject responseJson, String responseString);

    public abstract void handleResponse();
    public abstract void errorResponse();
}
