package com.amplearch.beaconshop;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by ample-arch on 4/17/2017.
 */

public class InternetCheck extends Activity
{
    public InternetCheck() {}

    Context context ;

    public void checkConnection()
    {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    public void showSnack(boolean isConnected)
    {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
//            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
//            color = Color.RED;
        }

//        Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout), message, Snackbar.LENGTH_LONG);

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

//        View sbView = snackbar.getView();
//        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
//        textView.setTextColor(color);
//        snackbar.show();
    }

}
