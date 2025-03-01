package com.root.sms.handlers;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface APICallResponseHandler {

    void onSuccess(JSONObject jsonObject, int requestId);
    void onFailure(VolleyError e, int requestId);
    void showProgress();
    void hideProgress();

}
