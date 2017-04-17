package com.amplearch.beaconshop.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amplearch.beaconshop.Adapter.HelpAdapter;
import com.amplearch.beaconshop.ConnectivityReceiver;
import com.amplearch.beaconshop.R;

public class HelpFragment extends Fragment {

    public HelpFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_help, container, false);
        checkConnection();
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.view_pagerHelp);
        HelpAdapter adapter = new HelpAdapter(getContext());
        viewPager.setAdapter(adapter);

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
