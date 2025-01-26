package com.root.sms.helpers;

import android.content.Context;

import com.android.volley.VolleyError;
import com.root.sms.constants.APIConstants;
import com.root.sms.handlers.APICallHandler;
import com.root.sms.handlers.APICallResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class AuthenticationHelper implements APICallHandler {

    private final APICallHelper apiCaller;

    private final APICallResponseHandler responseHandler;

    public AuthenticationHelper(Context context, APICallResponseHandler responseHandler){
        this.responseHandler = responseHandler;
        apiCaller = new APICallHelper(context, this);
    }

    public void login(String email, String password) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", email);
        jsonObject.put("password", password);
        responseHandler.showProgress();
        apiCaller.postCall(APIConstants.loginPostApi, jsonObject, APIConstants.loginPostApiRequestId);
    }

    @Override
    public void success(JSONObject object, int requestId) {
        switch (requestId){
            case APIConstants.loginPostApiRequestId:
                responseHandler.onSuccess(object);
                responseHandler.hideProgress();
                break;
        }
    }

    @Override
    public void failure(VolleyError e, int requestId) {

    }
}
