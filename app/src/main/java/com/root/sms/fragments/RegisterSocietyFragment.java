package com.root.sms.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.root.sms.R;
import com.root.sms.constants.AppConstants;
import com.root.sms.handlers.APICallResponseHandler;
import com.root.sms.helpers.FileOperationsHelper;

import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public class RegisterSocietyFragment extends BaseFragment implements APICallResponseHandler {

    private FileOperationsHelper fileOperationsHelper;
    private ActivityResultLauncher<Intent> imageActivityResultLauncher;
    private ActivityResultLauncher<Intent> fileActivityResultLauncher;
    private Button uploadImageButton, uploadFileButton;
    private ImageView imagePreview, filePreview;
    private ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = getProgressDialog("Please wait", "API Call in progress", false, getContext());
        fileOperationsHelper = new FileOperationsHelper(getContext(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_society, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uploadImageButton = view.findViewById(R.id.uploadImage);
        uploadFileButton = view.findViewById(R.id.uploadSocietyRegistration);
        imagePreview = view.findViewById(R.id.uploadImageView);
        filePreview = view.findViewById(R.id.uploadFileView);

        uploadImageButton.setOnClickListener(view1 -> imagePicker());
        uploadFileButton.setOnClickListener(view1 -> filePicker());

        imageActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Log.i("HERE", "");
                        Intent data = result.getData();
                        Log.i("HERE:", (data == null) +"");
                        Log.i("Image selected:" , data.getData().getPath().toString());
                        byte[] file = uriToFile(data.getData(), requireContext());
                        fileOperationsHelper.uploadProfile(file,
                                getMimeType(getContext(), data.getData()));
                        imagePreview.setVisibility(View.VISIBLE);
                        imagePreview.setImageURI(data.getData());
                    }
                    else {
                        Log.i("RESULT:" , result.toString());
                    }
                });

        fileActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Log.i("HERE", "");
                        Intent data = result.getData();
                        Log.i("HERE:", (data == null) +"");
                        Log.i("Image selected:", data.getData().getPath().toString());
                        byte[] file = uriToFile(data.getData(), requireContext());
                        fileOperationsHelper.uploadFile(file,
                                getMimeType(getContext(), data.getData()));
                        String fileType = getMimeType(getContext(), data.getData());
                        Log.i("File Name:", fileType);
                        setFilePreview(getContext(), data.getData());
                    }
                    else {
                        Log.i("RESULT:" , result.toString());
                    }

                });
    }

    public void imagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*"); // Accept all file types
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{
                "image/png",
                "image/jpeg"
        });
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        imageActivityResultLauncher.launch(intent);
    }

    public void filePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*"); // Accept all file types
        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{
                AppConstants.FILE_TYPE_PDF,
                AppConstants.FILE_TYPE_DOC,
                AppConstants.FILE_TYPE_DOCX,
                AppConstants.FILE_TYPE_XLS,
                AppConstants.FILE_TYPE_XLSX,
                AppConstants.FILE_TYPE_CSV,
                AppConstants.FILE_TYPE_PNG,
                AppConstants.FILE_TYPE_JPG
        });
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        fileActivityResultLauncher.launch(intent);
    }

    public boolean hasStoragePermission(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) { // Android 13+
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{
                            Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.READ_MEDIA_VIDEO,
                            Manifest.permission.READ_MEDIA_AUDIO,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    }, 1);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Storage permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private byte[] uriToFile(Uri uri, Context context) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = context.getContentResolver().openInputStream(uri)) {
            if (inputStream != null) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                }
            }
        } catch (IOException e) {
            Log.i("UPLOAD_ERROR", "Error reading file: " + e.getMessage());
        }
        return byteArrayOutputStream.toByteArray();
    }

    private void setFilePreview(Context context, Uri uri){
        filePreview.setVisibility(View.VISIBLE);
        if(AppConstants.FILE_TYPE_PDF.equals(getMimeType(context, uri))){
            filePreview.setImageResource(R.drawable.pdf);
        }
        else if(AppConstants.FILE_TYPE_XLS.equals(getMimeType(context, uri))
                || AppConstants.FILE_TYPE_XLSX.equals(getMimeType(context, uri))){
            filePreview.setImageResource(R.drawable.excel);
        }
        else if(AppConstants.FILE_TYPE_CSV.equals(getMimeType(context, uri))){
            filePreview.setImageResource(R.drawable.csv);
        }
        else if(AppConstants.FILE_TYPE_DOC.equals(getMimeType(context, uri))
                || AppConstants.FILE_TYPE_DOCX.equals(getMimeType(context, uri))){
            filePreview.setImageResource(R.drawable.word);
        }
        else if(AppConstants.FILE_TYPE_PNG.equals(getMimeType(context, uri))
                || AppConstants.FILE_TYPE_JPG.equals(getMimeType(context, uri))){
            filePreview.setImageURI(uri);
        }
        else {
            filePreview.setImageResource(R.drawable.document);
        }
    }


    private String getMimeType(Context context, Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            mimeType = context.getContentResolver().getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
        }
        return mimeType;
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {

    }

    @Override
    public void onFailure(VolleyError e) {

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