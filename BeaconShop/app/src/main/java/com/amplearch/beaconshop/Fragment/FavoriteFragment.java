package com.amplearch.beaconshop.Fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.amplearch.beaconshop.Adapter.FavoriteAdapter;
import com.amplearch.beaconshop.FavouriteDatabase;
import com.amplearch.beaconshop.Model.Favourites;
import com.amplearch.beaconshop.Model.StoreLocation;
import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.helper.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ample-arch on 3/31/2017.
 */

public class FavoriteFragment extends Fragment
{
    GridView Fav_gridView;
    FavoriteAdapter favoriteAdapter ;
    ArrayList<Integer> favImage = new ArrayList<Integer>();
    ArrayList<String>  favText = new ArrayList<String>();
    DatabaseHelper db;

    public FavoriteFragment() { }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        db = new DatabaseHelper(getContext());
        favImage.add(R.drawable.ic_audiotrack);
        favText.add("Fav Offer");

        favImage.add(R.drawable.ic_audiotrack);
        favText.add("Fav Offer");

        favImage.add(R.drawable.ic_audiotrack);
        favText.add("Fav Offer");

        favImage.add(R.drawable.ic_audiotrack);
        favText.add("Fav Offer");

        favImage.add(R.drawable.ic_audiotrack);
        favText.add("Fav Offer");

        favoriteAdapter = new FavoriteAdapter(getActivity(), favImage, favText);

        Fav_gridView = (GridView) view.findViewById(R.id.Fav_gridView);
        Fav_gridView.setAdapter(favoriteAdapter);

        List<Favourites> allTags = db.getAllFavourites();
        for (Favourites tag : allTags) {
            Log.d("Favourites Name", tag.getProduct_id());
            Toast.makeText(getContext(), "Product Id = " + tag.getProduct_id() + "  User Id = " + tag.getUser_id(), Toast.LENGTH_LONG).show();
        }

            return view;
    }
}