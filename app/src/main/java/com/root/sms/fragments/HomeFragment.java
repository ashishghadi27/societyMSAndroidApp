package com.root.sms.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.root.sms.R;
import com.root.sms.activities.AuthenticationActivity;
import com.root.sms.vo.MemberVO;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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
        MemberVO memberVO = getMemberDetails();
        if(memberVO != null){
            welcomeText.setText(String.format("Welcome %s", memberVO.getFirstName()));
        }
        else {
            Intent intent = new Intent(getContext(), AuthenticationActivity.class);
            startActivity(intent);
            requireActivity().finish();
        }
        super.onViewCreated(view, savedInstanceState);
    }
}