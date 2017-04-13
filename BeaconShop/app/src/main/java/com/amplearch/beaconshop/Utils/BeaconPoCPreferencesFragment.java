package com.amplearch.beaconshop.Utils;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.amplearch.beaconshop.R;

public class BeaconPoCPreferencesFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	addPreferencesFromResource(R.xml.preferences);
    }

}
