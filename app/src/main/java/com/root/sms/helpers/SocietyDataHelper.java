package com.root.sms.helpers;

import android.content.Context;

import com.android.volley.VolleyError;
import com.root.sms.constants.APIConstants;
import com.root.sms.handlers.APICallHandler;
import com.root.sms.handlers.APICallResponseHandler;
import com.root.sms.util.JsonSerializationUtils;
import com.root.sms.vo.MemberVO;
import com.root.sms.vo.RoomRequest;
import com.root.sms.vo.RoomVO;
import com.root.sms.vo.SocietyVO;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SocietyDataHelper implements APICallHandler {

    private final APICallHelper apiCaller;

    private final APICallResponseHandler responseHandler;

    public SocietyDataHelper(Context context, APICallResponseHandler responseHandler){
        this.responseHandler = responseHandler;
        apiCaller = new APICallHelper(context, this);
    }

    public void getSocieties(Long id){
        responseHandler.showProgress();
        apiCaller.getCall(String.format(APIConstants.getSocietyApi, id), APIConstants.getSocietyApiRequestId);
    }

    public void addSociety(SocietyVO societyVO) throws JSONException {
        responseHandler.showProgress();
        apiCaller.postCall(APIConstants.addSocietyApi,
                new JSONObject(JsonSerializationUtils.objectToJson(societyVO)),
                APIConstants.addSocietyApiRequestId);
    }

    public void addRooms(RoomRequest request) throws JSONException {
        responseHandler.showProgress();
        String json = JsonSerializationUtils.objectToJson(request);
        apiCaller.postCall(APIConstants.addRoomsApi,
                new JSONObject(json),
                APIConstants.addRoomsApiRequestId);
    }

    public void getRooms(Long sid){
        responseHandler.showProgress();
        String api = String.format(APIConstants.getRoomsApi, sid);
        apiCaller.getCall(api, APIConstants.getRoomsApiRequestId);
    }

    @Override
    public void success(JSONObject object, int requestId) {
        responseHandler.hideProgress();
        responseHandler.onSuccess(object, requestId);
    }

    @Override
    public void failure(VolleyError e, int requestId) {
        responseHandler.hideProgress();
        responseHandler.onFailure(e, requestId);
    }

    public void registerMember(MemberVO memberVO) throws JSONException {
        responseHandler.showProgress();
        apiCaller.postCall(APIConstants.registerMemberApi,
                new JSONObject(JsonSerializationUtils.objectToJson(memberVO)),
                APIConstants.registerMemberApiRequestId);
    }
}
