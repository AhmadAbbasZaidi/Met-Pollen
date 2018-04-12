package com.cfp.metpollen.data.api;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.cfp.metpollen.presenter.ApiResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Ahmad Abbas Zaidi on 3/21/2017.
 */

public class ResponseHandler extends BaseResponseHandler {

    public ResponseHandler(Activity activity, String calledApi, Map<String, String> body, Map<String, String> headers, ApiResponseListener apiResponseListener) {
        super(activity, calledApi, body, headers, apiResponseListener);
    }

    public ResponseHandler(Activity activity, String calledApi, JSONObject alternateBody, Map<String, String> headers, ApiResponseListener apiResponseListener) {
        super(activity, calledApi, alternateBody, headers, apiResponseListener);
    }


    @Override
    public void setResponse(JSONObject responseJson, String responseString) {
        this.responseJson = responseJson;
        this.responseString = responseString;
    }

    @Override
    public void handleResponse() {

        try {
            Log.i("ResponseJson = ", calledApi + " " + responseJson);
            if (responseJson.has("Result")) {
                JSONObject result = responseJson.getJSONObject("Result");
                if (result.has("Status")) {
                    int status = result.getInt("Status");
                    String response = result.getString("Message");
                    if (status == 200) {
                        apiResponseListener.setApiResponse(result, calledApi);
                    } else {
                        Toast.makeText(activity, response, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void errorResponse() {
        apiResponseListener.setApiError();
    }
}
