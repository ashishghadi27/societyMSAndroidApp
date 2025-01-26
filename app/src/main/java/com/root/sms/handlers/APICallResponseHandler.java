package com.root.sms.handlers;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface APICallResponseHandler {

    void onSuccess(JSONObject jsonObject);
    void onFailure(VolleyError e);
    void showProgress();
    void hideProgress();

}
