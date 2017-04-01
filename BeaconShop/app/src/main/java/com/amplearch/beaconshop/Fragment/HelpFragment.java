package com.amplearch.beaconshop.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amplearch.beaconshop.Adapter.HelpAdapter;
import com.amplearch.beaconshop.R;

public class HelpFragment extends Fragment {

    public HelpFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_help, container, false);

        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.view_pagerHelp);
        HelpAdapter adapter = new HelpAdapter(getContext());
        viewPager.setAdapter(adapter);

        return rootView;
    }

}
