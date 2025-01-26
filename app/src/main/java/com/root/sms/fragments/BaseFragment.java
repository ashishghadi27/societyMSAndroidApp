package com.root.sms.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.root.sms.R;

public class BaseFragment extends Fragment {

    public void addFragment(Fragment fragment, String tag){
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left, R.anim.enter_from_left, R.anim.exit_from_right)
                .add(R.id.fragment_container, fragment, tag).addToBackStack(tag)
                .commit();
    }

    public void replaceFragment(Fragment fragment, String tag){
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left, R.anim.enter_from_left, R.anim.exit_from_right)
                .replace(R.id.fragment_container, fragment, tag)
                .commit();
    }

    public ProgressDialog getProgressDialog(String title, String message, boolean isCancellable, Context context){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(isCancellable);
        return progressDialog;
    }

    public AlertDialog getAlertDialog(String title, String message, Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialog = View.inflate(getContext(), R.layout.alert_layout, null);
        TextView titleText = dialog.findViewById(R.id.title);
        TextView messageText = dialog.findViewById(R.id.message);
        titleText.setText(title);
        messageText.setText(message);
        builder.setView(dialog);
        builder.setCancelable(true);
        return builder.create();
    }

    public void saveUserDetails(String id, String name,  String email, String isAdmin){
        SharedPreferences preferences = requireContext().getSharedPreferences("userDetails",  Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("id", id);
        editor.putString("name", name);
        editor.putString("email", email);
        editor.putString("isAdmin", isAdmin);
        editor.apply();
    }

    public void saveVideoState(String id, int state){
        Log.i("SAVE_STATE", state +"");
        Log.i("STORY ID", id);
        SharedPreferences preferences = requireContext().getSharedPreferences("storyState",  Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(id, state);
        editor.apply();
    }

    public int getVideoState(String id){
        SharedPreferences preferences = requireContext().getSharedPreferences("storyState",  Context.MODE_PRIVATE);
        Log.i("STORY ID", id);
        int state = preferences.getInt(id, 0);
        Log.i("GET_STATE", state +"");
        return state;
    }

    public String getUserId(){
        SharedPreferences preferences = requireContext().getSharedPreferences("userDetails",  Context.MODE_PRIVATE);
        return preferences.getString("id", null);
    }

    public boolean isAdmin(){
        SharedPreferences preferences = requireContext().getSharedPreferences("userDetails",  Context.MODE_PRIVATE);
        return preferences.getString("isAdmin", "0").equals("1");
    }

    public String getEmailAddress(){
        SharedPreferences preferences = requireContext().getSharedPreferences("userDetails",  Context.MODE_PRIVATE);
        return preferences.getString("email", null);
    }

    public void signOut(){
        SharedPreferences preferences = requireContext().getSharedPreferences("userDetails",  Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

}