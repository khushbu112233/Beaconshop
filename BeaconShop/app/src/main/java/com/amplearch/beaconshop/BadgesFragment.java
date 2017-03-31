package com.amplearch.beaconshop;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by ample-arch on 3/30/2017.
 */

public class BadgesFragment extends Fragment
{
    GridView gridView ;
    BadgeAdapter badgeAdapter ;
    ArrayList<Integer> badgeImages = new ArrayList<Integer>();
    Dialog dialog ;

    public BadgesFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_badges, container, false);

        badgeImages.add(R.drawable.ic_sale);
        badgeImages.add(R.drawable.ic_sale);
        badgeImages.add(R.drawable.ic_sale);
        badgeImages.add(R.drawable.ic_sale);
        badgeImages.add(R.drawable.ic_sale);
        badgeImages.add(R.drawable.ic_sale);
        badgeImages.add(R.drawable.ic_sale);
        badgeImages.add(R.drawable.ic_sale);

        badgeAdapter = new BadgeAdapter(getActivity(),badgeImages);

        gridView = (GridView)rootView.findViewById(R.id.gridView);
        gridView.setAdapter(badgeAdapter);



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Toast.makeText(getActivity(), "You badges Number: " + position, Toast.LENGTH_SHORT).show();

                dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.badges_dialog_box);
                dialog.show();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//
//                    }
//                },2000);
//
//                dialog.dismiss();
            }
        });

        return rootView;
    }

}
