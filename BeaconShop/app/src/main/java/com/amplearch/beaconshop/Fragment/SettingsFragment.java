package com.amplearch.beaconshop.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.amplearch.beaconshop.Activity.MainActivity;
import com.amplearch.beaconshop.ConnectivityReceiver;
import com.amplearch.beaconshop.R;

public class SettingsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    CheckBox chboxNotification, chboxPopup, chboxWelcome, cbHQimages;

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        checkConnection();
        cbHQimages = (CheckBox)rootView.findViewById(R.id.cbHQimages);
        chboxNotification = (CheckBox) rootView.findViewById(R.id.cbOfferNotification);
        chboxPopup = (CheckBox) rootView.findViewById(R.id.cbOfferPopup);
        chboxWelcome = (CheckBox) rootView.findViewById(R.id.cbWelcomeMessage);

        chboxNotification.setOnCheckedChangeListener(this);
        chboxPopup.setOnCheckedChangeListener(this);
        chboxWelcome.setOnCheckedChangeListener(this);

        return rootView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.cbHQimages:
                if (isChecked) {
                }
                // Put some meat on the sandwich
                else {

                }
                break;
            case R.id.cbOfferNotification:
                if (isChecked) {

                }
                // Cheese me
                else {

                }
                // I'm lactose intolerant
                break;

            case R.id.cbOfferPopup:
                if (isChecked) {

                }
                // Cheese me
                else {

                }
                // I'm lactose intolerant
                break;

            case R.id.cbWelcomeMessage:
                if (isChecked) {

                }
                // Cheese me
                else {

                }
                // I'm lactose intolerant
                break;
            // TODO: Veggie sandwich
        }
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

                    // handle back button

                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);

                    return true;

                }

                return false;
            }
        });
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
