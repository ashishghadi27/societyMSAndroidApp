package com.root.sms.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.VolleyError;
import com.root.sms.R;
import com.root.sms.constants.APIConstants;
import com.root.sms.handlers.APICallResponseHandler;
import com.root.sms.helpers.SocietyDataHelper;
import com.root.sms.vo.MeetingVO;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class MeetingFragment extends BaseFragment implements APICallResponseHandler {

    private EditText title, agenda;
    private TextView startDateTime, endDateTime;
    private RelativeLayout createMeeting;
    private ProgressDialog dialog;
    private SocietyDataHelper societyDataHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        societyDataHelper = new SocietyDataHelper(getContext(), this);
        dialog = getProgressDialog("Please wait", "API Call in progress", false, getContext());
        return inflater.inflate(R.layout.fragment_meeting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = view.findViewById(R.id.title);
        agenda = view.findViewById(R.id.agenda);
        startDateTime = view.findViewById(R.id.startDateTime);
        endDateTime = view.findViewById(R.id.endDateTime);
        createMeeting = view.findViewById(R.id.createMeeting);

        startDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(startDateTime);
            }
        });

        endDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(endDateTime);
            }
        });

        createMeeting.setOnClickListener(v -> {
           createMeeting();
        });

    }

    private boolean createMeeting() {
        try {
            boolean isEmpty = false;
            MeetingVO meetingVO = new MeetingVO();
            meetingVO.setTitle(title.getText().toString());
            meetingVO.setAgenda(agenda.getText().toString());
            meetingVO.setStartDateTime(startDateTime.getText().toString());
            meetingVO.setEndDateTime(endDateTime.getText().toString());
            meetingVO.setSid(getSocietyDetailsAfterLogin().getSid());

            if(StringUtils.isEmpty(meetingVO.getTitle())){
                title.setError("Meeting Title is required");
                isEmpty = true;
            }
            if(StringUtils.isEmpty(meetingVO.getAgenda())){
                agenda.setError("Meeting Agenda is required");
                isEmpty = true;
            }
            if(StringUtils.isEmpty(meetingVO.getStartDateTime())){
                startDateTime.setError("Start Date Time is required");
                isEmpty = true;
            }
            if(StringUtils.isEmpty(meetingVO.getEndDateTime())){
                endDateTime.setError("End Date Time is required");
                isEmpty = true;
            }
            if(!isEmpty){
                societyDataHelper.createMeeting(meetingVO);
            }
            return isEmpty;
        } catch (JSONException e) {
            getAlertDialog("Error", "Something went wrong. Please try again later.", getContext()).show();
        }
        return true;
    }

    private void showDatePicker(TextView startDateTime) {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        String date = String.format("%02d/%02d/%d", year, monthOfYear + 1, dayOfMonth);

                        final Calendar c = Calendar.getInstance();
                        int mHour = c.get(Calendar.HOUR_OF_DAY);
                        int mMinute = c.get(Calendar.MINUTE);

                        // Launch Time Picker Dialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                          int minute) {
                                        String time = String.format("%02d:%02d:00", hourOfDay, minute);
                                        startDateTime.setText(String.format("%s %s", date, time));
                                    }
                                }, mHour, mMinute, false);
                        timePickerDialog.show();

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @Override
    public void onSuccess(JSONObject jsonObject, int requestId) {
        switch (requestId){
            case APIConstants.createMeetingRequestId:
                getAlertDialog("Success", "Meeting Scheduled Successfully", getContext()).show();
                replaceFragment(new AdminControlsFragment(), "ADMIN CONTROLS");
                break;
        }
    }

    @Override
    public void onFailure(VolleyError e, int requestId) {
        switch (requestId){
            case APIConstants.createMeetingRequestId:
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