package com.root.sms.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.root.sms.R;

public class SignInFragment extends BaseFragment {

    private EditText emailEditText;
    private EditText passwordEditText;
    private TextView signUp, forgotPass;
    private RelativeLayout login;
    private ProgressDialog dialog;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        dialog = getProgressDialog("Authenticating", "Verifying Credentials", false, getContext());

    }
}