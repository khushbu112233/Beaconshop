package com.amplearch.beaconshop.Fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.amplearch.beaconshop.Adapter.BadgeAdapter;
import com.amplearch.beaconshop.ConnectivityReceiver;
import com.amplearch.beaconshop.R;

import java.util.ArrayList;

/**
 * Created by ample-arch on 3/30/2017.
 */

public class BadgesFragment extends Fragment
{
    GridView gridView ;
    BadgeAdapter badgeAdapter ;
    ArrayList<Integer> badgeImages = new ArrayList<Integer>();
    ArrayList<String>  badgeText = new ArrayList<String>();
    Dialog dialog ;

    public BadgesFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_badges, container, false);

        checkConnection();
        badgeImages.add(R.drawable.award);
        badgeText.add(" GOLD\nDIGGER");
        badgeImages.add(R.drawable.award);
        badgeText.add("  EARLY\nSHOPPER");
        badgeImages.add(R.drawable.award);
        badgeText.add("  GOOD\nSHOPPER");
        badgeImages.add(R.drawable.award);
        badgeText.add("HAPPY\n GIRL");
        badgeImages.add(R.drawable.award);
        badgeText.add("MYSTERY\n  BADGE");
        badgeImages.add(R.drawable.award);
        badgeText.add("COFFEE\nMANIA");
        badgeImages.add(R.drawable.award);
        badgeText.add(" SALE\nMANIA");

        badgeAdapter = new BadgeAdapter(getActivity(),badgeImages,badgeText);

        gridView = (GridView)rootView.findViewById(R.id.gridView);
        gridView.setAdapter(badgeAdapter);

        if (checkConnection() == true)
        {
            
        }

        gridView.setVisibility(View.GONE);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Toast.makeText(getActivity(), "You badges Number: " + position, Toast.LENGTH_SHORT).show();

//                dialog = new Dialog(getActivity());
//                dialog.setContentView(R.layout.badges_dialog_box);
//                dialog.show();
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

    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
        return isConnected;
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
