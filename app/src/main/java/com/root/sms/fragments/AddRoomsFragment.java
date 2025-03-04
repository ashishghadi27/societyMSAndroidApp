package com.root.sms.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.root.sms.R;
import com.root.sms.adapters.RoomAdapter;
import com.root.sms.constants.APIConstants;
import com.root.sms.handlers.APICallResponseHandler;
import com.root.sms.helpers.SocietyDataHelper;
import com.root.sms.vo.RoomRequest;
import com.root.sms.vo.RoomVO;
import com.root.sms.vo.SocietyVO;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddRoomsFragment extends BaseFragment implements APICallResponseHandler {

    private BottomSheetDialog bottom_sheet_dialog;
    private ProgressDialog dialog;
    private ImageView noDataImage;
    private RecyclerView recyclerView;
    private RelativeLayout saveDetails;
    private List<RoomVO> roomVOList;
    private RoomAdapter roomAdapter;
    private SocietyDataHelper societyDataHelper;
    private String societyId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        roomVOList = new ArrayList<>();
        roomAdapter = new RoomAdapter(roomVOList);
        societyDataHelper = new SocietyDataHelper(getContext(), this);
        dialog = getProgressDialog("Please wait", "API Call in progress", false, getContext());
        return inflater.inflate(R.layout.fragment_add_rooms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView addRoom = view.findViewById(R.id.addRoom);
        noDataImage = view.findViewById(R.id.noDataImage);
        recyclerView = view.findViewById(R.id.recyclerView);
        saveDetails = view.findViewById(R.id.saveDetails);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(roomAdapter);

        if(roomVOList.isEmpty()){
            noDataImage.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
        }

        addRoom.setOnClickListener(view1 -> {
          showAddRoomDialog();
        });

        saveDetails.setOnClickListener(view2 -> {
            saveSocietyAndRoomDetails();
        });

    }

    private void addRoomSelectorFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("societyId", societyId);
        bundle.putBoolean("isFirstUser", true);
        Fragment fragment = new RoomSelector();
        fragment.setArguments(bundle);
        addFragment(fragment, "ROOM SELECTOR");
    }

    private void showAddRoomDialog() {
        bottom_sheet_dialog = new BottomSheetDialog(requireContext());
        View dialog = View.inflate(getContext(), R.layout.add_room_layout, null);
        bottom_sheet_dialog.setContentView(dialog);
        bottom_sheet_dialog.show();

        EditText roomNumberEditText = dialog.findViewById(R.id.roomNo);
        EditText roomSizeEditText = dialog.findViewById(R.id.roomSize);
        RelativeLayout submit = dialog.findViewById(R.id.addRooms);

        submit.setOnClickListener(view -> {
            addRoomToList(roomNumberEditText, roomSizeEditText);
        });

        if(roomVOList.isEmpty()){
            noDataImage.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void addRoomToList(EditText roomNumberEditText, EditText roomSizeEditText) {
        String roomNumber = roomNumberEditText.getText().toString();
        String roomSize = roomSizeEditText.getText().toString();
        if(StringUtils.isEmpty(roomNumber)){
            roomNumberEditText.setError("Enter Room Number");
        } else if(StringUtils.isEmpty(roomSize)){
            roomSizeEditText.setError("Enter Room Size");
        } else {
            RoomVO roomVO = new RoomVO();
            roomVO.setRoomNo(roomNumber);
            roomVO.setRoomSize(roomSize);
            roomVOList.add(roomVO);
            bottom_sheet_dialog.dismiss();
            refreshRecyclerView();
        }
    }

    private void refreshRecyclerView(){
        if (roomVOList.isEmpty()){
            noDataImage.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else {
            noDataImage.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        roomAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccess(JSONObject jsonObject, int requestId) {
        switch (requestId){
            case APIConstants.addSocietyApiRequestId:
                setSocietyIdForAllRooms(jsonObject);
                saveRooms(roomVOList);
                break;
            case APIConstants.addRoomsApiRequestId:
                getAlertDialog("Success", "Added Rooms to Society Successfully", getContext()).show();
                addRoomSelectorFragment();
                break;
        }
    }

    @Override
    public void onFailure(VolleyError e, int requestId) {
        switch (requestId){
            case APIConstants.addSocietyApiRequestId:
            case APIConstants.addRoomsApiRequestId:
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

    private void saveSocietyAndRoomDetails() {
        SocietyVO societyVO = getSocietyDetails();
        try {
            societyDataHelper.addSociety(societyVO);
        } catch (JSONException e) {
            getAlertDialog("Error", "Something went wrong. Please try again later.", getContext()).show();
            Log.e("JSON_ERROR", Objects.requireNonNull(e.getMessage()));
        }
    }

    private void saveRooms(List<RoomVO> roomVOList) {
        try {
            RoomRequest request = new RoomRequest(roomVOList);
            societyDataHelper.addRooms(request);
        } catch (JSONException e) {
            getAlertDialog("Error", "Something went wrong. Please try again later.", getContext()).show();
            Log.e("JSON_ERROR", Objects.requireNonNull(e.getMessage()));
        }
    }

    private void setSocietyIdForAllRooms(JSONObject jsonObject) {
        try{
            String societyId = jsonObject.getJSONObject("data").getString("sid");
            Log.i("SOCIETY ID", societyId);
            this.societyId = societyId;
            for (RoomVO roomVO : roomVOList){
                roomVO.setSocietyId(Long.parseLong(societyId));
            }
        }
        catch (JSONException e){
            getAlertDialog("Error", "Something went wrong. Please try again later.", getContext()).show();
            Log.e("JSON_ERROR", Objects.requireNonNull(e.getMessage()));
        }
    }
}