package com.amplearch.beaconshop.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.amplearch.beaconshop.R;

public class SettingsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    CheckBox chboxNotification, chboxPopup, chboxWelcome;

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        chboxNotification = (CheckBox) rootView.findViewById(R.id.checkbox_offer_noti);
        chboxPopup = (CheckBox) rootView.findViewById(R.id.checkbox_offer_popup);
        chboxWelcome = (CheckBox) rootView.findViewById(R.id.checkbox_welcome);

        chboxNotification.setOnCheckedChangeListener(this);
        chboxPopup.setOnCheckedChangeListener(this);
        chboxWelcome.setOnCheckedChangeListener(this);

        return rootView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.checkbox_offer_noti:
                if (isChecked) {
                }
                // Put some meat on the sandwich
                else {

                }
                break;
            case R.id.checkbox_offer_popup:
                if (isChecked) {

                }
                // Cheese me
                else {

                }
                // I'm lactose intolerant
                break;

            case R.id.checkbox_welcome:
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
}
