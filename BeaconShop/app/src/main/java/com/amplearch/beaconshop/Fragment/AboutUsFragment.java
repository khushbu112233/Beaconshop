package com.amplearch.beaconshop.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amplearch.beaconshop.ConnectivityReceiver;
import com.amplearch.beaconshop.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUsFragment extends Fragment
{
    Context context;
    public AboutUsFragment() {     }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_about_us, container, false);

        checkConnection();

        return rootView;
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        String message = "Sorry! No Internet connection.";
        if (isConnected) {
//            message = "Good! Connected to Internet";
        } else {
//            message = "Sorry! Not connected to internet";
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    }


}
