package com.amplearch.beaconshop.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        return rootView;
    }


}
