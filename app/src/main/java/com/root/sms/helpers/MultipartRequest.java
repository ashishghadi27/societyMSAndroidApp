package com.root.sms.helpers;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.ByteArrayBody;

public class MultipartRequest extends Request<NetworkResponse> {
    private final MultipartEntityBuilder entityBuilder;
    private final Response.Listener<NetworkResponse> responseListener;
    private final Map<String, String> headers;

    public MultipartRequest(String url,
                            Response.Listener<NetworkResponse> listener,
                            Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.responseListener = listener;
        this.headers = new HashMap<>();
        this.entityBuilder = MultipartEntityBuilder.create();
        this.entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
    }

    public void addFile(String fieldName, byte[] fileData, String fileName, String mimeType) {
        entityBuilder.addPart(fieldName, new ByteArrayBody(fileData, ContentType.create(mimeType), fileName));
    }

    public void addStringPart(String fieldName, String value) {
        entityBuilder.addTextBody(fieldName, value, ContentType.TEXT_PLAIN);
    }

    @Override
    public String getBodyContentType() {
        return entityBuilder.build().getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            entityBuilder.build().writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response, getCacheEntry());
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        responseListener.onResponse(response);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
    }
}

