package com.root.sms.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.root.sms.R;
import com.root.sms.activities.AuthenticationActivity;
import com.root.sms.constants.MemberConstants;
import com.root.sms.vo.MemberVO;

public class HomeFragment extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView welcomeText = view.findViewById(R.id.welcomeText);
        RelativeLayout adminControls = view.findViewById(R.id.adminControls);
        RelativeLayout meetingDetails = view.findViewById(R.id.meetingDetails);
        RelativeLayout memberDetails = view.findViewById(R.id.memberDetails);

        meetingDetails.setOnClickListener(v -> {
            getAlertDialog("No Data!", "No Meeting is completed yet!", getContext()).show();
        });

        memberDetails.setOnClickListener(v -> {
            addFragment(new MemberDetailsFragment(), "MEMBER DETAILS");
        });

        MemberVO memberVO = getMemberDetails();
        if(memberVO != null){
            welcomeText.setText(String.format("Welcome %s", memberVO.getFirstName()));
            if (MemberConstants.ADMIN.name().equals(memberVO.getType())){
                adminControls.setVisibility(View.VISIBLE);
                adminControls.setOnClickListener(v -> {
                    addFragment(new AdminControlsFragment(), "ADMIN CONTROLS");
                });
            }
        }
        else {
            Intent intent = new Intent(getContext(), AuthenticationActivity.class);
            startActivity(intent);
            requireActivity().finish();
        }
        super.onViewCreated(view, savedInstanceState);
    }

}