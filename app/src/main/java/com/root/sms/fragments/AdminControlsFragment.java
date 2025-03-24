package com.root.sms.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.root.sms.R;
import com.root.sms.constants.MemberConstants;
import com.root.sms.vo.MemberVO;

public class AdminControlsFragment extends BaseFragment {

    public AdminControlsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_controls, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView welcomeText = view.findViewById(R.id.welcomeText);
        RelativeLayout parkingAllotment = view.findViewById(R.id.alotParking);
        RelativeLayout scheduleMeeting = view.findViewById(R.id.scheduleMeeting);

        parkingAllotment.setOnClickListener( view1 -> {
            addFragment(new ParkingSelectorFragment(), "parking-allotment");
        });

        scheduleMeeting.setOnClickListener(view1 -> {
            addFragment(new MeetingFragment(), "MEETING FRAGMENT");
        });

        MemberVO memberVO = getMemberDetails();
        if(memberVO != null){
            welcomeText.setText(String.format("Welcome %s", memberVO.getFirstName()));
        }
    }
}