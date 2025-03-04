package com.root.sms.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.root.sms.R;
import com.root.sms.adapters.RoomSelectorAdapter;
import com.root.sms.adapters.SocietySelectorAdapter;
import com.root.sms.constants.APIConstants;
import com.root.sms.handlers.APICallResponseHandler;
import com.root.sms.handlers.SocietyClickHandler;
import com.root.sms.helpers.SocietyDataHelper;
import com.root.sms.vo.RoomVO;
import com.root.sms.vo.SocietyVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SocietySelectorFragment extends BaseFragment implements SocietyClickHandler, APICallResponseHandler {

    private ProgressDialog dialog;
    private ImageView noDataImage;
    private RecyclerView recyclerView;
    private List<SocietyVO> societyVOList;
    private SocietySelectorAdapter societySelectorAdapter;
    private SocietyDataHelper societyDataHelper;
    private TextView registerSociety;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        societyVOList = new ArrayList<>();
        societySelectorAdapter = new SocietySelectorAdapter(societyVOList, this);
        societyDataHelper = new SocietyDataHelper(getContext(), this);

        dialog = getProgressDialog("Please wait", "API Call in progress", false, getContext());
        return inflater.inflate(R.layout.fragment_society_selector, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerSociety = view.findViewById(R.id.registerSociety);
        noDataImage = view.findViewById(R.id.noDataImage);
        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(societySelectorAdapter);

        societyDataHelper.getSocieties(0L);

        if(societyVOList.isEmpty()) {
            noDataImage.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else {
            noDataImage.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        registerSociety.setOnClickListener(view1 -> {
            addFragment(new RegisterSocietyFragment(), "register-society");
        });
    }

    @Override
    public void onSocietyClick(SocietyVO societyVO) {
        addRoomSelectorFragment(String.valueOf(societyVO.getSid()));
    }

    @Override
    public void onSuccess(JSONObject jsonObject, int requestId) {
        switch (requestId){
            case APIConstants.getSocietyApiRequestId:
                populateSocietyList(jsonObject);
                societySelectorAdapter.notifyDataSetChanged();
                break;
        }
    }

    private void populateSocietyList(JSONObject jsonObject) {
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                SocietyVO societyVO = new SocietyVO();
                societyVO.setSid(object.getLong("sid"));
                societyVO.setName(object.getString("name"));
                societyVO.setAddressLine1(object.getString("addressLine1"));
                societyVO.setAddressLine2(object.getString("addressLine2"));
                societyVO.setPlotNumber(object.getString("plotNumber"));
                societyVO.setParkingAvailable(object.getBoolean("parkingAvailable"));

                societyVOList.add(societyVO);
            }
            if(societyVOList.isEmpty()) {
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

    private void addRoomSelectorFragment(String societyId) {
        Bundle bundle = new Bundle();
        bundle.putString("societyId", societyId);
        bundle.putBoolean("isFirstUser", true);
        Fragment fragment = new RoomSelector();
        fragment.setArguments(bundle);
        addFragment(fragment, "ROOM SELECTOR");
    }
}