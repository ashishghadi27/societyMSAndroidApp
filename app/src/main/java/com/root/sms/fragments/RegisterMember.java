package com.root.sms.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.VolleyError;
import com.root.sms.R;
import com.root.sms.activities.AuthenticationActivity;
import com.root.sms.constants.APIConstants;
import com.root.sms.constants.MemberConstants;
import com.root.sms.handlers.APICallResponseHandler;
import com.root.sms.helpers.SocietyDataHelper;
import com.root.sms.util.HashingUtils;
import com.root.sms.vo.MemberVO;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class RegisterMember extends BaseFragment implements APICallResponseHandler {

    private Long rid;
    private SocietyDataHelper societyDataHelper;
    private ProgressDialog dialog;
    private EditText firstName, lastName, email, password;
    private RelativeLayout register;
    private boolean isFirstUser;

    public RegisterMember() {
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
        Bundle bundle = getArguments();
        assert bundle != null;
        rid = bundle.getLong("rid", 0);
        isFirstUser = bundle.getBoolean("isFirstUser");

        societyDataHelper = new SocietyDataHelper(getContext(), this);
        dialog = getProgressDialog("Please wait", "API Call in progress", false, getContext());
        return inflater.inflate(R.layout.fragment_register_member, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        register = view.findViewById(R.id.registerMember);

        register.setOnClickListener(v -> {
            MemberVO memberVO = new MemberVO();
            memberVO.setRoomId(rid);
            memberVO.setFirstName(firstName.getText().toString());
            memberVO.setLastName(lastName.getText().toString());
            memberVO.setEmail(email.getText().toString());
            memberVO.setType(isFirstUser ? MemberConstants.ADMIN.name() : MemberConstants.MEMBER.name());
            memberVO.setHashedPassword(HashingUtils.getMd5HashedString(password.getText().toString()));
            boolean isValid = validateEnteredData(memberVO);
            if(isValid) {
                try {
                    societyDataHelper.registerMember(memberVO);
                } catch (JSONException e) {
                    getAlertDialog("Error", "Something went wrong. Please try again later.", getContext()).show();
                    Log.e("JSON_ERROR", Objects.requireNonNull(e.getMessage()));
                }
            }
        });
    }

    private boolean validateEnteredData(MemberVO memberVO) {
        if(StringUtils.isEmpty(memberVO.getFirstName())){
            firstName.setError("First Name is required");
        }
        else if(StringUtils.isEmpty(memberVO.getLastName())){
            lastName.setError("Last Name is required");
        }
        else if(StringUtils.isEmpty(memberVO.getEmail())){
            email.setError("Email is required");
        }
        else if(StringUtils.isEmpty(memberVO.getHashedPassword())){
            password.setError("Password is required");
        }
        else {
            return true;
        }
        return false;
    }

    @Override
    public void onSuccess(JSONObject jsonObject, int requestId) {
        switch (requestId){
            case APIConstants.registerMemberApiRequestId:
                getAlertDialog("Success", "Member Registered Successfully", getContext()).show();
                Intent intent = new Intent(getContext(), AuthenticationActivity.class);
                startActivity(intent);
                requireActivity().finish();
                break;
        }
    }

    @Override
    public void onFailure(VolleyError e, int requestId) {
        switch (requestId){
            case APIConstants.registerMemberApiRequestId:
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