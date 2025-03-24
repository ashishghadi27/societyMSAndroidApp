package com.root.sms.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.root.sms.R;
import com.root.sms.vo.MemberVO;
import com.root.sms.vo.RoomVO;
import com.root.sms.vo.SocietyVO;


public class MemberDetailsFragment extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_member_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MemberVO memberVO = getMemberDetails();
        RoomVO roomVO = getRoomDetails();
        SocietyVO societyVO = getSocietyDetailsAfterLogin();

        TextView welcomeText = view.findViewById(R.id.welcomeText);
        TextView roomNumber = view.findViewById(R.id.roomNumber);
        TextView roomDesc = view.findViewById(R.id.roomDesc);

        welcomeText.setText(String.format("hello %s", memberVO.getFirstName()));
        roomNumber.setText(String.format("Room No. %s", roomVO.getRoomNo()));
        roomDesc.setText(String.format("Room No. %s in %s", roomVO.getRoomNo(), societyVO.getName()));

        super.onViewCreated(view, savedInstanceState);
    }
}