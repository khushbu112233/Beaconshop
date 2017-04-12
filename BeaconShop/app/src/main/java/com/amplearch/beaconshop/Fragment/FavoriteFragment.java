package com.amplearch.beaconshop.Fragment;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.amplearch.beaconshop.Adapter.FavoriteAdapter;
import com.amplearch.beaconshop.FavouriteDatabase;
import com.amplearch.beaconshop.R;

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

    String URL = "content://com.amplearch.beaconshop.FavoriteDatabase";
    String res ;
    Cursor cursor ;

    String product_id ;
    String user_id ;

    public FavoriteFragment() { }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

//        cursor = getActivity().getContentResolver().query(Uri.parse(String.valueOf(FavouriteDatabase.CONTENT_URI)), null, null, null, null);
//        if(cursor.moveToFirst())
//        {
//            do
//            {
//                product_id = cursor.getString(cursor.getColumnIndex(FavouriteDatabase.FIELD_PRODUCT_ID));
//                user_id =  cursor.getString(cursor.getColumnIndex(FavouriteDatabase.FIELD_USER_ID));
//
//                Toast.makeText(getContext(),"Productid: "+product_id,Toast.LENGTH_SHORT).show();
//            }
//            while (cursor.moveToNext());
//        }
//        cursor.close();

        favImage.add(R.drawable.ic_sale);
        favText.add("p_Id");

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
