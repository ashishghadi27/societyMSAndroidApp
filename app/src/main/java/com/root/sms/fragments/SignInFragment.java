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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.VolleyError;
import com.root.sms.R;
import com.root.sms.activities.MainActivity;
import com.root.sms.constants.APIConstants;
import com.root.sms.handlers.APICallHandler;
import com.root.sms.handlers.APICallResponseHandler;
import com.root.sms.helpers.AuthenticationHelper;
import com.root.sms.util.HashingUtils;
import com.root.sms.vo.MemberVO;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

public class SignInFragment extends BaseFragment implements APICallResponseHandler {

    private EditText emailEditText;
    private EditText passwordEditText;
    private TextView signUp, forgotPass;
    private RelativeLayout login;
    private ProgressDialog dialog;

    private AuthenticationHelper authenticationHelper;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isValidSession()){
            launchHome();
        }
        authenticationHelper = new AuthenticationHelper(getContext(), this);
        dialog = getProgressDialog("Please wait", "API Call in progress", false, getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emailEditText = view.findViewById(R.id.email);
        passwordEditText = view.findViewById(R.id.password);
        login = view.findViewById(R.id.login);
        signUp = view.findViewById(R.id.signUp);
        forgotPass = view.findViewById(R.id.forgotPass);
        dialog = getProgressDialog("Authenticating", "Verifying Credentials",
                false, getContext());

        login.setOnClickListener(view1 -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            if(isValidInput(email, password)){
                password = HashingUtils.getMd5HashedString(password);
                try {
                    authenticationHelper.login(email, password);
                } catch (JSONException e) {

                }
            }
        });

        signUp.setOnClickListener((vw) -> {
            addFragment(new SocietySelectorFragment(), "society-selector");
        });
    }

    private boolean isValidSession() {
        String authCookieStr = getAuthCookie();
        String sessionCookieStr = getSessionCookie();
        MemberVO memberVO = getMemberDetails();
        if(!StringUtils.isEmpty(authCookieStr) && !StringUtils.isEmpty(sessionCookieStr)){
            HttpCookie sessionCookie = new HttpCookie("session-id", sessionCookieStr.replace("session-id=", ""));
            HttpCookie authCookie = new HttpCookie("auth", authCookieStr.replace("auth=", ""));
            if(sessionCookie.hasExpired() || authCookie.hasExpired()){
                return false;
            }
        }
        else {
            return false;
        }
        return memberVO != null;
    }

    private boolean isValidInput(String email, String password) {
        if(StringUtils.isEmpty(email)){
            emailEditText.setError("Email cannot be empty");
        }
        if(StringUtils.isEmpty(password)){
            passwordEditText.setError("Password cannot be empty");
        }
        return !StringUtils.isEmpty(email) && !StringUtils.isEmpty(password);
    }

    @Override
    public void onSuccess(JSONObject jsonObject, int requestId) {
        switch (requestId){
            case APIConstants.loginPostApiRequestId:
                Log.i("RESPONSE", jsonObject.toString());
                validateResponse(jsonObject);
                break;
        }
    }

    private void validateResponse(JSONObject jsonObject) {
        try {
            if (jsonObject.getBoolean("validUser")){
                JSONObject member = jsonObject.getJSONObject("user");
                saveMemberDetails(member);
                saveSocietyDetails(jsonObject.getJSONObject("society"));
                saveRoomDetails(jsonObject.getJSONObject("room"));
                launchHome();
            }
            else {
                getAlertDialog("Error", "Invalid Credentials", getContext()).show();
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void launchHome() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }

    @Override
    public void onFailure(VolleyError e, int requestId) {
        switch (requestId){
            case APIConstants.loginPostApiRequestId:
                Log.i("RESPONSE", new String(e.networkResponse.data));
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