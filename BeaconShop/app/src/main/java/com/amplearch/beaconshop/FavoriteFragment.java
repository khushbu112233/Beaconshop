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

public class FavoriteFragment extends Fragment
{
    GridView Fav_gridView;
    FavoriteAdapter favoriteAdapter ;
    ArrayList<Integer> favImage = new ArrayList<Integer>();
    ArrayList<String>  favText = new ArrayList<String>();

    public FavoriteFragment() { }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        favImage.add(R.drawable.ic_sale);
        favText.add("Fav Offer");

        favImage.add(R.drawable.ic_sale);
        favText.add("Fav Offer");

        favImage.add(R.drawable.ic_sale);
        favText.add("Fav Offer");

        favImage.add(R.drawable.ic_sale);
        favText.add("Fav Offer");

        favImage.add(R.drawable.ic_sale);
        favText.add("Fav Offer");

        favoriteAdapter = new FavoriteAdapter(getActivity(), favImage, favText);

        Fav_gridView = (GridView)view.findViewById(R.id.Fav_gridView);
        Fav_gridView.setAdapter(favoriteAdapter);

        return view ;
    }
}
