package com.root.sms.helpers;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.root.sms.constants.AppConstants;
import com.root.sms.handlers.APICallHandler;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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

    public void uploadBitmap(File file, int requestId,
                             String url, String mimeType) {
        MultipartRequest multipartRequest = new MultipartRequest(
                url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        // Handle success response
                        String result = new String(response.data);
                        System.out.println("Response: " + result);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        System.err.println("Error: " + error.getMessage());
                    }
                }
        );
        multipartRequest.addFile("file", getByteArrayFromFile(file), file.getName(), "image/jpeg");
        multipartRequest.addStringPart("description", "This is a sample file upload");
        this.queue.add(multipartRequest);
    }

//    public void uploadVideo(Uri uri, int requestId) {
//
//        //our custom volley request
//        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, Constants.uploadImageApi,
//                response -> {
//                    try {
//                        JSONObject obj = new JSONObject(new String(response.data));
//                        handler.success(obj, requestId);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                },
//                error -> {
//                    handler.failure(error, requestId);
//                }) {
//
//            @Override
//            protected Map<String, DataPart> getByteData() {
//                Map<String, DataPart> params = new HashMap<>();
//                long imagename = System.currentTimeMillis();
//                params.put("file", new DataPart(imagename + ".png", getFileDataFromDrawable(context, uri)));
//                return params;
//            }
//        };
//
//        //adding the request to volley
//        this.queue.add(volleyMultipartRequest);
//    }
//
//    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
//        return byteArrayOutputStream.toByteArray();
//    }

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
