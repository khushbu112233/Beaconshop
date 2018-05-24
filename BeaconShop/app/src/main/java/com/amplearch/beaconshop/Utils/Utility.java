package com.amplearch.beaconshop.Utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.amplearch.beaconshop.Activity.SignUpActivity;
import com.amplearch.beaconshop.ConnectivityReceiver;

import java.io.ByteArrayOutputStream;

public class Utility {
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;


    public boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
       // showSnack(isConnected);
        return  isConnected;
    }

   /* private void showSnack(boolean isConnected) {
        String message = "Check For Data Connection..";
        if (isConnected) {
//            message = "Good! Connected to Internet";
        } else {
//            message = "Sorry! Not connected to internet";
            Toast.makeText(g(), message, Toast.LENGTH_LONG).show();
        }
    }*/

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static long imageCalculateSize(Bitmap bitmapOrg){
        Bitmap bitmap = bitmapOrg;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageInByte = stream.toByteArray();
        long lengthbmp = imageInByte.length;
        long MEGABYTE = 1024L * 1024L;
        long b = lengthbmp / MEGABYTE;

        return b;
    }

    public static boolean validate(String userName, String emailAddress, String contactNo, String password, String rePassword)
    {
        boolean valid = true ;

        if(userName.isEmpty() || userName.length() < 3 )
        {
            SignUpActivity.etUserName.setError("Minimum 3 Characters.");
            valid = false ;
        }
        else
        {
            SignUpActivity.etUserName.setError(null);
        }

        if (emailAddress.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches())
        {
            SignUpActivity.etEmailAddress.setError("Not a valid Email Address");
            valid = false;
        }
        else {
            SignUpActivity.etEmailAddress.setError(null);
        }

        if(contactNo.isEmpty() || contactNo.length() != 10  )
        {
            SignUpActivity.etContactNo.setError("10 Characters Required");
            valid = false ;
        }
        else if(contactNo.length() > 10){
            SignUpActivity.etContactNo.setError("10 Characters Required");
        }
        else {
            SignUpActivity.etContactNo.setError(null);
        }

        if (password.isEmpty() || password.length() < 4)
        {
            SignUpActivity.etPassword.setError("Minimum 4 Characters");
            valid = false;
        }
        else {
            SignUpActivity.etPassword.setError(null);
        }

        if (rePassword.equals(password))
        {

            SignUpActivity.etRePassword.setError(null);
        }
        else {
            SignUpActivity.etRePassword.setError("Password does not match");
            valid = false;
        }

        return valid;
    }

}