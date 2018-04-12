package com.cfp.metpollen.data.api.Requests;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cfp.metpollen.data.api.ApiHandler;
import com.cfp.metpollen.data.api.BaseResponseHandler;
import com.cfp.metpollen.data.api.ResponseHandler;
import com.cfp.metpollen.presenter.ApiResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Ahmad Abbas Zaidi on 3/21/2017.
 */

public class StringRequestCall extends ApiHandler {

    private BaseResponseHandler responseHandler;
    private Response.ErrorListener responseErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.i("volley error = ", error.toString());
            ResponseHandler rh;
            if (isAlternate) {
                rh = new ResponseHandler(activity, calledApi, alternatebody, header,apiResponseListener);
            } else {
                rh = new ResponseHandler(activity, calledApi, body, header,apiResponseListener);
            }
            responseHandler = rh;
            responseHandler.errorResponse();
//            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
        }
    };
    private Response.Listener<String> responseListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            try {
                responseJson = new JSONObject(response);

                ResponseHandler rh;
                if (isAlternate) {
                    rh = new ResponseHandler(activity, calledApi, alternatebody, header,apiResponseListener);
                } else {
                    rh = new ResponseHandler(activity, calledApi, body, header,apiResponseListener);
                }
                responseHandler = rh;
                responseHandler.setResponse(responseJson, response);
                responseHandler.handleResponse();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };
    private RequestQueue requestQueue;

    public StringRequestCall(Map<String, String> header, Map<String, String> body, String calledApi, int method, Activity activity, ApiResponseListener apiResponseListener) {
        super(header, body, calledApi, method, activity, apiResponseListener);
        this.header = header;
        this.body = body;
        this.calledApi = calledApi;
        this.method = method;
        this.activity = activity;
        this.apiResponseListener = apiResponseListener;

        if (body != null)
            Log.i("body = ", body.toString());
    }

    public StringRequestCall(Map<String, String> header, JSONObject body, String calledApi, int method, Activity activity, boolean isAlternate, ApiResponseListener apiResponseListener) {
        super(header, body, calledApi, method, activity, isAlternate, apiResponseListener);
        this.header = header;
        this.alternatebody = body;
        this.calledApi = calledApi;
        this.method = method;
        this.activity = activity;
        this.isAlternate = isAlternate;
        this.apiResponseListener = apiResponseListener;

        if (alternatebody != null)
            Log.i("alternateBody = ", alternatebody.toString());
    }

    public void sendData() {
        StringRequest stringRequest = null;
        if (method == Request.Method.POST) {


            stringRequest = new StringRequest(method, baseUrl + calledApi, responseListener, responseErrorListener) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Log.i("Header", header.toString());
                    return header;
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Log.i("Body", isAlternate?alternatebody.toString():body.toString());
                    return body;
                }

            };
        } else if (method == Request.Method.GET) {
            stringRequest = new StringRequest(method, this.baseUrl + calledApi, responseListener, responseErrorListener) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Log.i("Header", header.toString());
                    return header;
                }
            };
        }

        if (stringRequest != null) {
            if (requestQueue==null)
            requestQueue = Volley.newRequestQueue(activity.getApplicationContext());
            requestQueue.add(stringRequest);
        }
        /*progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Please Wait....");
        progressDialog.show();*/
    }

}
