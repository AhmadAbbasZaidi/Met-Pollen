package com.cfp.metpollen.data.api.Requests;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cfp.metpollen.data.api.ApiHandler;
import com.cfp.metpollen.data.api.BaseResponseHandler;
import com.cfp.metpollen.data.api.ResponseHandler;
import com.cfp.metpollen.presenter.ApiResponseListener;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Ahmad Abbas Zaidi on 3/21/2017.
 */

public class JsonObjectRequestCall extends ApiHandler {

    private BaseResponseHandler responseHandler;
    private Response.ErrorListener responseErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.i("volley error = ",error.toString() );
//            Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
        }
    };
    private ProgressDialog progressDialog;
    private Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {

            responseJson = response;
            ResponseHandler rh;
            if (isAlternate) {
                rh = new ResponseHandler(activity, calledApi, alternatebody, header,apiResponseListener);
            } else {
                rh = new ResponseHandler(activity, calledApi, body, header,apiResponseListener);
            }
            responseHandler = rh;
            /*if (calledApi == ApiCalls.UPLOAD_PROFILE_PICTURE) {
                ((LoginBase) responseHandler).setProgressDialog(progressDialog);
            }
            if (calledApi == ApiCalls.GET_CONTRACTS) {
                ((LoginBase) responseHandler).setContractsHandler(contractsHandler);
            }*/

            responseHandler.setResponse(responseJson, response.toString());
            responseHandler.handleResponse();
//            progressDialog.dismiss();

        }
    };

    public JsonObjectRequestCall(Map<String, String> header, Map<String, String> body, String calledApi, int method, Activity activity, ApiResponseListener apiResponseListener) {
        super(header, body, calledApi, method, activity,apiResponseListener);
        this.header = header;
        this.body = body;
        this.calledApi = calledApi;
        this.method = method;
        this.activity = activity;
        this.apiResponseListener = apiResponseListener;

        if (body != null)
        Log.i("body = ", body.toString());
    }

    public JsonObjectRequestCall(Map<String, String> header, JSONObject body, String calledApi, int method, Activity activity, boolean isAlternate,ApiResponseListener apiResponseListener) {
        super(header, body, calledApi, method, activity, isAlternate,apiResponseListener);
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
        JsonObjectRequest jsonRequest = null;
        if (method == Request.Method.POST) {
            Log.i("Method ", "post");
            if (isAlternate) {
                jsonRequest = new JsonObjectRequest(method, baseUrl + calledApi, alternatebody, responseListener, responseErrorListener) {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Log.i("Header", header.toString());
                        return header;
                    }

                };


            } else {
                jsonRequest = new JsonObjectRequest(method, baseUrl + calledApi, new JSONObject(body), responseListener, responseErrorListener) {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Log.i("Header", header.toString());
                        return header;
                    }

                };
            }
        } else if (method == Request.Method.GET) {
            Log.i("Method ", "get");

            jsonRequest = new JsonObjectRequest(method, baseUrl + calledApi, null, responseListener, responseErrorListener) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Log.i("Header", header.toString());
                    return header;
                }
            };
        }

        if (jsonRequest != null) {
        RequestQueue requestQueue = Volley.newRequestQueue(activity.getApplicationContext());
        requestQueue.add(jsonRequest);
        }
        /*progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Please Wait....");
        progressDialog.show();*/
    }

}
