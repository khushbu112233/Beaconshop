package com.amplearch.beaconshop.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.amplearch.beaconshop.Activity.StoreListActivity;
import com.amplearch.beaconshop.Adapter.CategoryAdapter;
import com.amplearch.beaconshop.ConnectivityReceiver;
import com.amplearch.beaconshop.R;

/**
 * Created by ample-arch on 4/6/2017.
 */

public class Nearbyfragment extends Fragment
{
    public Nearbyfragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_nearby, container, false);
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
