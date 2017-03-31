package com.amplearch.beaconshop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * Created by ample-arch on 3/31/2017.
 */

public class VoucherFragment extends Fragment
{
    GridView gridView_Vouch;
    VoucherAdapter voucherAdapter;
    ArrayList<Integer> vouchImage = new ArrayList<Integer>();
    ArrayList<String> vouchText = new ArrayList<String>();

    public VoucherFragment() { }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_voucher, container, false);

        vouchImage.add(R.drawable.ic_sale);
        vouchText.add("Redeem Offered");

        vouchImage.add(R.drawable.ic_sale);
        vouchText.add("Redeem Offered");

        vouchImage.add(R.drawable.ic_sale);
        vouchText.add("Redeem Offered");

        vouchImage.add(R.drawable.ic_sale);
        vouchText.add("Redeem Offered");

        vouchImage.add(R.drawable.ic_sale);
        vouchText.add("Redeem Offered");

        voucherAdapter = new VoucherAdapter(getActivity(), vouchImage, vouchText);

        gridView_Vouch = (GridView)view.findViewById(R.id.gridView_Vouch);
        gridView_Vouch.setAdapter(voucherAdapter);

        return view ;
    }
}
