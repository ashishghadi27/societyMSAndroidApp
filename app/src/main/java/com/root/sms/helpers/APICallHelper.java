package com.root.sms.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Header;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.root.sms.constants.AppConstants;
import com.root.sms.handlers.APICallHandler;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class APICallHelper {

    private static CookieManager cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);

    private RequestQueue queue;
    private APICallHandler handler;
    private Context context;

    public APICallHelper(Context context, APICallHandler handler) {
        this.context = context;
        this.queue = Volley.newRequestQueue(context);
        this.handler = handler;
    }

    public void getCall(String api, int requestId) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, api, null,
                response -> {
                    Log.v("Response: ", response.toString());
                    handler.success(response, requestId);
                },
                error -> {
                    Log.v("Response:", error.getCause() + "    " + error.getMessage());
                    handler.failure(error, requestId);
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>(super.getHeaders());
                headers.putAll(getCookieMap());
                return headers;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(com.android.volley.NetworkResponse response) {
                List<Header> headers = response.allHeaders;
                for (Header header : headers) {
                    if ("Set-Cookie".equals(header.getName())) {
                        Log.i("COOKIE", header.getValue());
                        if (header.getValue().startsWith("session-id")) {
                            saveSessionCookie(header.getValue());
                        }
                        if (header.getValue().startsWith("auth")) {
                            saveAuthCookie(header.getValue());
                        }
                    }
                }
                return super.parseNetworkResponse(response);
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.queue.add(request);
    }

    public void deleteCall(String api, int requestId) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, api, null,
                response -> {
                    Log.v("Response: ", response.toString());
                    handler.success(response, requestId);
                },
                error -> {
                    Log.v("Response:", error.getCause() + "    " + error.getMessage());
                    handler.failure(error, requestId);
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>(super.getHeaders());
                headers.putAll(getCookieMap());
                return headers;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(com.android.volley.NetworkResponse response) {
                List<Header> headers = response.allHeaders;
                for (Header header : headers) {
                    if ("Set-Cookie".equals(header.getName())) {
                        Log.i("COOKIE", header.getValue());
                        if (header.getValue().startsWith("session-id")) {
                            saveSessionCookie(header.getValue());
                        }
                        if (header.getValue().startsWith("auth")) {
                            saveAuthCookie(header.getValue());
                        }
                    }
                }
                //response.headers.forEach((k, v) -> Log.i("HEADERS", k + " : " + v));
                return super.parseNetworkResponse(response);
            }
        };
        request.setShouldCache(false);
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.queue.add(request);
    }

    public void postCall(String api, JSONObject jsonObject, int requestId) {
        Log.v("API", api);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, api, jsonObject,
                response -> {
                    Log.i("Response: ", response.toString());
                    handler.success(response, requestId);
                },
                error -> {
                    Log.v("Response Post:", error.getCause() + "    " + error.getMessage());
                    Log.v("ERROR", error.toString());
                    handler.failure(error, requestId);
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(com.android.volley.NetworkResponse response) {
                List<Header> headers = response.allHeaders;
                for (Header header : headers) {
                    if ("Set-Cookie".equals(header.getName())) {
                        Log.i("COOKIE", header.getValue());
                        if (header.getValue().startsWith("session-id")) {
                            saveSessionCookie(header.getValue());
                        }
                        if (header.getValue().startsWith("auth")) {
                            saveAuthCookie(header.getValue());
                        }
                    }
                }
                //response.headers.forEach((k, v) -> Log.i("HEADERS", k + " : " + v));
                return super.parseNetworkResponse(response);
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.queue.add(request);
    }

    public void uploadFile(byte[] file, int requestId,
                           String url, String mimeType, String fileName) {
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
        request.addPart(new MultipartRequest.FilePart("file", mimeType, fileName, file));

        this.queue.add(request);
    }

    public static byte[] getByteArrayFromFile(File file) {
        String methodName = "getByteArrayFromFile";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (InputStream iStream = new FileInputStream(file)) {
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

    private Map<String, String> getCookieMap() {
        Map<String, String> headers = new HashMap<>();
        StringBuilder cookieHeader = new StringBuilder();
        List<HttpCookie> cookies = getCookies();
        for (HttpCookie cookie : cookies) {
            if (!cookie.hasExpired()) {
                if (cookieHeader.length() > 0) {
                    cookieHeader.append("; ");
                }
                cookieHeader.append(cookie.getName()).append("=").append(cookie.getValue());
            }
        }
        if (cookieHeader.length() > 0) {
            headers.put("Set-Cookie", cookieHeader.toString());
        }
        return headers;
    }

    //
    public List<HttpCookie> getCookies() {
        String authCookieStr = getAuthCookie();
        String sessionCookieStr = getSessionCookie();
        List<HttpCookie> cookieList = new ArrayList<>();
        if(!StringUtils.isEmpty(authCookieStr) && !StringUtils.isEmpty(sessionCookieStr)){
            HttpCookie sessionCookie = new HttpCookie("session-id", sessionCookieStr.replace("session-id=", ""));
            HttpCookie authCookie = new HttpCookie("auth", authCookieStr.replace("auth=", ""));
            cookieList.add(sessionCookie);
            cookieList.add(authCookie);
        }
        return cookieList;
    }

    public void saveAuthCookie(String cookie) {
        SharedPreferences preferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("auth", cookie);
        editor.apply();
    }

    public String getAuthCookie() {
        SharedPreferences preferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
        return preferences.getString("auth", null);
    }

    public void saveSessionCookie(String cookie) {
        SharedPreferences preferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("session", cookie);
        editor.apply();
    }

    public String getSessionCookie() {
        SharedPreferences preferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
        return preferences.getString("session", null);
    }


}
