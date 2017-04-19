package com.amplearch.beaconshop.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.amplearch.beaconshop.ConnectivityReceiver;
import com.amplearch.beaconshop.InternetCheck;
import com.amplearch.beaconshop.R;

public class SplashActivity extends AppCompatActivity
{
    InternetCheck internetCheck ;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        checkConnection();

//        internetCheck.checkConnection();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                finish();
            }
        },1000);


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
