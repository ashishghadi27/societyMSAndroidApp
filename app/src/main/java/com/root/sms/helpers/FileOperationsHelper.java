package com.root.sms.helpers;

import android.content.Context;

import com.android.volley.VolleyError;
import com.root.sms.constants.APIConstants;
import com.root.sms.handlers.APICallHandler;
import com.root.sms.handlers.APICallResponseHandler;

import org.json.JSONObject;

import java.io.File;

public class FileOperationsHelper implements APICallHandler {

    private final APICallHelper apiCaller;

    private final APICallResponseHandler responseHandler;

    public FileOperationsHelper(Context context, APICallResponseHandler responseHandler){
        this.responseHandler = responseHandler;
        apiCaller = new APICallHelper(context, this);
    }

    public void uploadFile(byte[] file, String mimeType){
        apiCaller.uploadFile(file, APIConstants.fileUploadApiRequestId, APIConstants.fileUploadApi, mimeType);
    }

    public void uploadProfile(byte[] file, String mimeType){
        apiCaller.uploadFile(file, APIConstants.profileUploadApiRequestId, APIConstants.profileUploadApi, mimeType);
    }

    @Override
    public void success(JSONObject object, int requestId) {

    }

    @Override
    public void failure(VolleyError e, int requestId) {

    }
}
