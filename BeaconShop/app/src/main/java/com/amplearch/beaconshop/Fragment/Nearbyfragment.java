package com.amplearch.beaconshop.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.amplearch.beaconshop.Activity.StoreListActivity;
import com.amplearch.beaconshop.Adapter.CategoryAdapter;
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


        return rootView;
    }

}
