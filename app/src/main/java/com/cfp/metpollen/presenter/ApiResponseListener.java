package com.cfp.metpollen.presenter;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Ahmad Abbas Zaidi on 5/4/2017.
 */

public interface ApiResponseListener {
    public void setApiResponse(JSONObject jsonObject, String calledApi);
    public void setApiResponse(JSONArray jsonArray, String calledApi);
    public void setApiResponse(String jsonAsString, String calledApi);
    public void setApiError();
}
