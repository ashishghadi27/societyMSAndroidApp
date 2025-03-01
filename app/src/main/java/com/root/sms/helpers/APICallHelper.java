package com.root.sms.helpers;

import android.content.Context;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.root.sms.constants.AppConstants;
import com.root.sms.handlers.APICallHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;


public class APICallHelper {
    private RequestQueue queue;
    private APICallHandler handler;
    private Context context;

    public APICallHelper(Context context, APICallHandler handler){
        this.context = context;
        this.queue = Volley.newRequestQueue(context);
        this.handler = handler;
    }

    public void getCall(String api, int requestId){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, api, null,
                response -> {
                    Log.v("Response: ", response.toString());
                    handler.success(response, requestId);
                },
                error -> {
                    Log.v("Response:", error.getCause() + "    " + error.getMessage());
                    handler.failure(error, requestId);
                });
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.queue.add(request);
    }

    public void deleteCall(String api, int requestId){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, api, null,
                response -> {
                    Log.v("Response: ", response.toString());
                    handler.success(response, requestId);
                },
                error -> {
                    Log.v("Response:", error.getCause() + "    " + error.getMessage());
                    handler.failure(error, requestId);
                });
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.queue.add(request);
    }

    public void postCall(String api, JSONObject jsonObject, int requestId){
        Log.v("API", api);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, api, jsonObject,
                response -> {
                    Log.v("Response: ", response.toString());
                    handler.success(response, requestId);
                },
                error -> {
                    Log.v("Response Post:", error.getCause() + "    " + error.getMessage());
                    Log.v("ERROR", error.toString());
                    handler.failure(error, requestId);
                });
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.queue.add(request);
    }

    public void uploadFile(byte[] file, int requestId,
                             String url, String mimeType) {
        Log.i("Upload API Call", requestId + "");
        MultipartRequest request = new MultipartRequest(url, new HashMap<>(),
                response -> {
                    Log.v("Response: ", response.toString());
                    try {
                        handler.success(new JSONObject(new String(response.data)), requestId);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> {
                    Log.v("Response Post:", error.getCause() + "    " + error.getMessage());
                    Log.v("ERROR", error.toString());
                    handler.failure(error, requestId);
                });
        String fileName = System.currentTimeMillis() + "." +MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
        request.addPart(new MultipartRequest.FilePart("file", mimeType, fileName, file));

        this.queue.add(request);
    }

    public static  byte[] getByteArrayFromFile(File file) {
        String methodName = "getByteArrayFromFile";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try(InputStream iStream = new FileInputStream(file)) {
            int bufferSize = 2048;
            byte[] buffer = new byte[bufferSize];
            int len = 0;
            while ((len = iStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
        } catch (IOException e) {
            Log.e("Byte Array from file Error",
                    String.format(AppConstants.LOG_ERROR_FORMAT,
                            methodName, e.getCause(), e.getMessage(), ""));
        }

        //  bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}
