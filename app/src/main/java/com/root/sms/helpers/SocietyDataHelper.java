package com.root.sms.helpers;

import android.content.Context;

import com.android.volley.VolleyError;
import com.root.sms.handlers.APICallHandler;
import com.root.sms.handlers.APICallResponseHandler;

import org.json.JSONObject;

public class SocietyDataHelper implements APICallHandler {

    private final APICallHelper apiCaller;

    private final APICallResponseHandler responseHandler;

    public SocietyDataHelper(Context context, APICallResponseHandler responseHandler){
        this.responseHandler = responseHandler;
        apiCaller = new APICallHelper(context, this);
    }

    @Override
    public void success(JSONObject object, int requestId) {

    }

    @Override
    public void failure(VolleyError e, int requestId) {

    }
}
