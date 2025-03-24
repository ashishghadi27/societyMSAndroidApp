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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.root.sms.R;
import com.root.sms.adapters.MemberSelectorAdapter;
import com.root.sms.constants.APIConstants;
import com.root.sms.handlers.APICallResponseHandler;
import com.root.sms.handlers.MemberClickHandler;
import com.root.sms.helpers.SocietyDataHelper;
import com.root.sms.vo.MemberVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MemberSelectorFragment extends BaseFragment implements MemberClickHandler, APICallResponseHandler {

    private ProgressDialog dialog;
    private ImageView noDataImage;
    private RecyclerView recyclerView;
    private List<MemberVO> membersList;
    private MemberSelectorAdapter adapter;
    private SocietyDataHelper societyDataHelper;
    private Long pid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        membersList = new ArrayList<>();
        adapter = new MemberSelectorAdapter(membersList, this);
        societyDataHelper = new SocietyDataHelper(getContext(), this);

        Bundle bundle = getArguments();
        assert bundle != null;
        pid = bundle.getLong("pid", 0L);

        dialog = getProgressDialog("Please wait", "API Call in progress", false, getContext());
        return inflater.inflate(R.layout.fragment_member_selector, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        noDataImage = view.findViewById(R.id.noDataImage);
        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        societyDataHelper.getMembersWithNoParkingSpace(getSocietyDetailsAfterLogin().getSid());

        if(membersList.isEmpty()) {
            noDataImage.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else {
            noDataImage.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void populateMemberList(JSONObject jsonObject) {
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                MemberVO memberVO = new MemberVO();
                memberVO.setFirstName(object.getString("firstName"));
                memberVO.setLastName(object.getString("lastName"));
                memberVO.setRoomId(object.getLong("rid"));
                memberVO.setMid(object.getLong("mid"));
                membersList.add(memberVO);
            }
            if(membersList.isEmpty()) {
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
            case APIConstants.getMembersWithNoParkingRequestId:
                populateMemberList(jsonObject);
                adapter.notifyDataSetChanged();
                break;
            case APIConstants.allotParkingRequestId:
                societyDataHelper.getMembersWithNoParkingSpace(getSocietyDetailsAfterLogin().getSid());
                getAlertDialog("Success", "Parking Allotment Done.", getContext()).show();
                break;
        }
    }

    @Override
    public void onFailure(VolleyError e, int requestId) {
        switch (requestId){
            case APIConstants.getMembersWithNoParkingRequestId:
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
    public void onMemberClick(MemberVO memberVO) {
        if(pid != 0L){
            societyDataHelper.allotParkingSpace(memberVO.getMid(), pid);
        }
        else {
            getAlertDialog("Error", "Something went wrong. Please try again later.", getContext()).show();
        }
    }
}