package com.root.sms.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.root.sms.R;
import com.root.sms.adapters.ParkingSlotSelectorAdapter;
import com.root.sms.constants.APIConstants;
import com.root.sms.handlers.APICallResponseHandler;
import com.root.sms.handlers.ParkingSlotClickHandler;
import com.root.sms.helpers.SocietyDataHelper;
import com.root.sms.vo.ParkingSpaceVO;
import com.root.sms.vo.RoomVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ParkingSelectorFragment extends BaseFragment implements ParkingSlotClickHandler, APICallResponseHandler {

    private ProgressDialog dialog;
    private ImageView noDataImage;
    private RecyclerView recyclerView;
    private List<ParkingSpaceVO> parkingSpaceVOList;
    private ParkingSlotSelectorAdapter adapter;
    private SocietyDataHelper societyDataHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        parkingSpaceVOList = new ArrayList<>();
        adapter = new ParkingSlotSelectorAdapter(parkingSpaceVOList, this);
        societyDataHelper = new SocietyDataHelper(getContext(), this);

        dialog = getProgressDialog("Please wait", "API Call in progress", false, getContext());
        return inflater.inflate(R.layout.fragment_parking_selector, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        noDataImage = view.findViewById(R.id.noDataImage);
        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        societyDataHelper.getParkingSpaces(getSocietyDetailsAfterLogin().getSid());

        if(parkingSpaceVOList.isEmpty()) {
            noDataImage.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else {
            noDataImage.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void populateParkingList(JSONObject jsonObject) {
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                ParkingSpaceVO parkingSpaceVO = new ParkingSpaceVO();
                parkingSpaceVO.setPid(object.getLong("pid"));
                parkingSpaceVO.setParkingId(object.getString("parkingId"));
                parkingSpaceVO.setSocietyId(object.getLong("societyId"));

                parkingSpaceVOList.add(parkingSpaceVO);
            }
            if(parkingSpaceVOList.isEmpty()) {
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
            case APIConstants.getParkingSpacesApiRequestId:
                populateParkingList(jsonObject);
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onFailure(VolleyError e, int requestId) {
        switch (requestId){
            case APIConstants.getParkingSpacesApiRequestId:
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

    @Override
    public void onSlotClick(ParkingSpaceVO parkingSpaceVO) {
        Bundle bundle = new Bundle();
        bundle.putLong("pid",parkingSpaceVO.getPid());
        bundle.putString("parkingSlotId",parkingSpaceVO.getParkingId());

        Fragment fragment = new MemberSelectorFragment();
        fragment.setArguments(bundle);

        addFragment(fragment,"REGISTER MEMBER");
    }
}