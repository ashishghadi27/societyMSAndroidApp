package com.root.sms.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.root.sms.R;
import com.root.sms.adapters.RoomSelectorAdapter;
import com.root.sms.constants.APIConstants;
import com.root.sms.handlers.APICallResponseHandler;
import com.root.sms.handlers.RoomClickHandler;
import com.root.sms.helpers.SocietyDataHelper;
import com.root.sms.vo.RoomVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RoomSelector extends BaseFragment implements RoomClickHandler, APICallResponseHandler {

    private ProgressDialog dialog;
    private ImageView noDataImage;
    private RecyclerView recyclerView;
    private List<RoomVO> roomVOList;
    private RoomSelectorAdapter roomAdapter;
    private SocietyDataHelper societyDataHelper;
    private String societyId;
    private boolean isFirstUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        roomVOList = new ArrayList<>();
        roomAdapter = new RoomSelectorAdapter(roomVOList, this);
        societyDataHelper = new SocietyDataHelper(getContext(), this);

        Bundle bundle = getArguments();
        assert bundle != null;
        societyId = bundle.getString("societyId", "");
        isFirstUser = bundle.getBoolean("isFirstUser");

        dialog = getProgressDialog("Please wait", "API Call in progress", false, getContext());
        return inflater.inflate(R.layout.fragment_room_selector, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        noDataImage = view.findViewById(R.id.noDataImage);
        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(roomAdapter);

        societyDataHelper.getRooms(Long.valueOf(societyId));

        if(roomVOList.isEmpty()) {
            noDataImage.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else {
            noDataImage.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRoomClick(RoomVO roomVO) {

        Bundle bundle = new Bundle();
        bundle.putLong("societyId",roomVO.getSocietyId());
        bundle.putLong("rid",roomVO.getRid());
        bundle.putString("roomNo",roomVO.getRoomNo());
        bundle.putString("roomSize",roomVO.getRoomSize());
        bundle.putBoolean("isFirstUser",isFirstUser);

        Fragment fragment = new RegisterMember();
        fragment.setArguments(bundle);

        addFragment(fragment,"REGISTER MEMBER");
    }

    private void populateRoomList(JSONObject jsonObject) {
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                RoomVO roomVO = new RoomVO();
                roomVO.setRid(object.getLong("rid"));
                roomVO.setRoomNo(object.getString("roomNo"));
                roomVO.setRoomSize(object.getString("roomSize"));
                roomVO.setSocietyId(Long.valueOf(societyId));
                roomVOList.add(roomVO);
            }
            if(roomVOList.isEmpty()) {
                noDataImage.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
            else {
                noDataImage.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            getAlertDialog("Error", "Something went wrong. Please try again later.", getContext()).show();
            Log.e("JSON_ERROR", Objects.requireNonNull(e.getMessage()));
        }
    }

    @Override
    public void onSuccess(JSONObject jsonObject, int requestId) {
        switch (requestId){
            case APIConstants.getRoomsApiRequestId:
                populateRoomList(jsonObject);
                roomAdapter.notifyDataSetChanged();
                getAlertDialog("Success", "Society Registered Successfully", getContext()).show();
                break;
        }
    }

    @Override
    public void onFailure(VolleyError e, int requestId) {
        switch (requestId){
            case APIConstants.getRoomsApiRequestId:
                getAlertDialog("Error", "Something went wrong. Please try again later.", getContext()).show();
                break;
        }
    }

    @Override
    public void showProgress() {
        dialog.show();
    }

    @Override
    public void hideProgress() {
        dialog.dismiss();
    }
}