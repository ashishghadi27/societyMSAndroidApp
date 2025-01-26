package com.root.sms.handlers;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface APICallHandler {

    void success(JSONObject object, int requestId);
    void failure(VolleyError e, int requestId);

}
