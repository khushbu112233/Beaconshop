package com.amplearch.beaconshop.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.amplearch.beaconshop.ConnectivityReceiver;
import com.amplearch.beaconshop.InternetCheck;
import com.amplearch.beaconshop.R;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.BLUETOOTH;
import static android.Manifest.permission.BLUETOOTH_ADMIN;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SplashActivity extends AppCompatActivity
{
    InternetCheck internetCheck ;
    private static final int PERMISSION_REQUEST_CODE = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        checkConnection();

//        internetCheck.checkConnection();
        if (!checkPermission()) {

            requestPermission();

        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                    finish();
                }
            }, 1000);
        }


    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), GET_ACCOUNTS);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_NETWORK_STATE);
        int result4 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
        int result5 = ContextCompat.checkSelfPermission(getApplicationContext(), BLUETOOTH_ADMIN);
        int result6 = ContextCompat.checkSelfPermission(getApplicationContext(), BLUETOOTH);
        int result7 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
                && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED
                && result4 == PackageManager.PERMISSION_GRANTED && result5 == PackageManager.PERMISSION_GRANTED
                && result6 == PackageManager.PERMISSION_GRANTED && result7 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, WRITE_EXTERNAL_STORAGE, GET_ACCOUNTS, ACCESS_NETWORK_STATE, ACCESS_COARSE_LOCATION, BLUETOOTH_ADMIN, BLUETOOTH, CAMERA}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean contactAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean contactAccepted1 = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean contactAccepted2 = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    boolean contactAccepted3 = grantResults[5] == PackageManager.PERMISSION_GRANTED;
                    boolean contactAccepted4 = grantResults[6] == PackageManager.PERMISSION_GRANTED;
                    boolean contactAccepted5 = grantResults[7] == PackageManager.PERMISSION_GRANTED;


                    if (locationAccepted && cameraAccepted && contactAccepted && contactAccepted1 && contactAccepted2 && contactAccepted3
                            && contactAccepted4 && contactAccepted5) {

                        new Handler().postDelayed(new Runnable()
                        {
                            // Using handler with postDelayed called runnable run method
                            @Override
                            public void run()
                            {
                                Intent i = new Intent(SplashActivity.this, AccountActivity.class);
                                startActivity(i);
                                // close this activity
                                finish();
                            }
                        }, 3*1000);

                        //  Snackbar.make(view, "Permission Granted, Now you can access location data and camera.", Snackbar.LENGTH_LONG).show();
                    }
                    else {

                        //   Snackbar.make(view, "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION, WRITE_EXTERNAL_STORAGE, GET_ACCOUNTS, ACCESS_NETWORK_STATE
                                                                    , ACCESS_COARSE_LOCATION, BLUETOOTH_ADMIN, BLUETOOTH, CAMERA},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            } else {
                                new Handler().postDelayed(new Runnable() {
                                    // Using handler with postDelayed called runnable run method
                                    @Override
                                    public void run() {
                                        Intent i = new Intent(SplashActivity.this, AccountActivity.class);
                                        startActivity(i);
                                        // close this activity
                                        finish();
                                    }
                                }, 3 * 1000);
                            }
                        }
                        else {
                            new Handler().postDelayed(new Runnable()
                            {
                                // Using handler with postDelayed called runnable run method
                                @Override
                                public void run()
                                {
                                    Intent i = new Intent(SplashActivity.this, AccountActivity.class);
                                    startActivity(i);
                                    // close this activity
                                    finish();
                                }
                            }, 3*1000);
                        }

                    }
                }
                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(SplashActivity.this)
                .setTitle(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    private boolean checkConnection()
    {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
        return isConnected ;
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
//            message = "Good! Connected to Internet";
//            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
//            color = Color.RED;
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }

//        Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout), message, Snackbar.LENGTH_LONG);

//        View sbView = snackbar.getView();
//        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
//        textView.setTextColor(color);
//        snackbar.show();
    }
}
