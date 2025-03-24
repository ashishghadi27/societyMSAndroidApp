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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.root.sms.R;
import com.root.sms.adapters.ParkingSpaceAdapter;
import com.root.sms.constants.APIConstants;
import com.root.sms.handlers.APICallResponseHandler;
import com.root.sms.helpers.SocietyDataHelper;
import com.root.sms.vo.ParkingSpaceRequest;
import com.root.sms.vo.ParkingSpaceVO;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddParkingSpacesFragment extends BaseFragment implements APICallResponseHandler {

    private static final org.apache.commons.logging.Log log = LogFactory.getLog(AddParkingSpacesFragment.class);
    private BottomSheetDialog bottom_sheet_dialog;
    private ProgressDialog dialog;
    private ImageView noDataImage;
    private RecyclerView recyclerView;
    private RelativeLayout saveDetails;
    private List<ParkingSpaceVO> parkingSpaceVOList;
    private ParkingSpaceAdapter parkingSpaceAdapter;
    private SocietyDataHelper societyDataHelper;
    private Long societyId;
    private Long rid;
    private boolean isFirstUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parkingSpaceVOList = new ArrayList<>();
        parkingSpaceAdapter = new ParkingSpaceAdapter(parkingSpaceVOList);
        societyDataHelper = new SocietyDataHelper(getContext(), this);
        dialog = getProgressDialog("Please wait", "API Call in progress", false, getContext());

        Bundle bundle = getArguments();
        assert bundle != null;
        societyId = bundle.getLong("societyId", 0L);
        Log.i("SOCIETY ID", societyId+"");
        rid = bundle.getLong("rid", 0);
        isFirstUser = bundle.getBoolean("isFirstUser");

        return inflater.inflate(R.layout.fragment_add_parking_spaces, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView addParkingSlot = view.findViewById(R.id.addParkingSlot);
        noDataImage = view.findViewById(R.id.noDataImage);
        recyclerView = view.findViewById(R.id.recyclerView);
        saveDetails = view.findViewById(R.id.saveDetails);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(parkingSpaceAdapter);

        if(parkingSpaceVOList.isEmpty()){
            noDataImage.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
        }

        addParkingSlot.setOnClickListener(view1 -> {
            showAddParkingSpacesDialog();
        });

        saveDetails.setOnClickListener(view2 -> {
            saveParkingSlots(parkingSpaceVOList);
        });

    }

    private void addMemberDetailsFragment() {
        Bundle bundle = new Bundle();
        bundle.putLong("societyId",societyId);
        bundle.putLong("rid",rid);
        bundle.putBoolean("isFirstUser",isFirstUser);

        Fragment fragment = new RegisterMember();
        fragment.setArguments(bundle);

        addFragment(fragment,"REGISTER MEMBER");
    }

    private void showAddParkingSpacesDialog() {
        bottom_sheet_dialog = new BottomSheetDialog(requireContext());
        View dialog = View.inflate(getContext(), R.layout.add_parking_slot_layout, null);
        bottom_sheet_dialog.setContentView(dialog);
        bottom_sheet_dialog.show();

        EditText slotId = dialog.findViewById(R.id.slotId);
        RelativeLayout submit = dialog.findViewById(R.id.addSlot);

        submit.setOnClickListener(view -> {
            addSlotsToList(slotId);
        });

        if(parkingSpaceVOList.isEmpty()){
            noDataImage.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void addSlotsToList(EditText slotId) {
        String slotIdStr = slotId.getText().toString();
        if(StringUtils.isEmpty(slotIdStr)){
            slotId.setError("Enter Slot Id");
        }  else {
            ParkingSpaceVO parkingSpaceVO = new ParkingSpaceVO();
            parkingSpaceVO.setParkingId(slotIdStr);
            parkingSpaceVO.setSocietyId(societyId);
            parkingSpaceVOList.add(parkingSpaceVO);
            bottom_sheet_dialog.dismiss();
            refreshRecyclerView();
        }
    }

    private void refreshRecyclerView(){
        if (parkingSpaceVOList.isEmpty()){
            noDataImage.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else {
            noDataImage.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        parkingSpaceAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccess(JSONObject jsonObject, int requestId) {
        switch (requestId){
            case APIConstants.addParkingSpacesApiRequestId:
                addMemberDetailsFragment();
                break;
        }
    }

    @Override
    public void onFailure(VolleyError e, int requestId) {
        switch (requestId){
            case APIConstants.addParkingSpacesApiRequestId:
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

    private void saveParkingSlots(List<ParkingSpaceVO> parkingSpaceVOList) {
        try {
            ParkingSpaceRequest request = new ParkingSpaceRequest(parkingSpaceVOList);
            societyDataHelper.addParkingSpaces(request);
        } catch (JSONException e) {
            getAlertDialog("Error", "Something went wrong. Please try again later.", getContext()).show();
            Log.e("JSON_ERROR", Objects.requireNonNull(e.getMessage()));
        }
    }

}