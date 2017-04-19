package com.amplearch.beaconshop.Fragment;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.amplearch.beaconshop.Adapter.FavoritesAdapter;
import com.amplearch.beaconshop.ConnectivityReceiver;
import com.amplearch.beaconshop.Model.Favourites;
import com.amplearch.beaconshop.Model.StoreLocation;
import com.amplearch.beaconshop.Model.Voucher;
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
    ArrayList<Bitmap> favImage = new ArrayList<Bitmap>();
    ArrayList<String>  favText = new ArrayList<String>();
    DatabaseHelper db;

    List<Voucher> vouchers ;
    ArrayList<String>  StoreName = new ArrayList<String>();
    ArrayList<String>  OfferTitle = new ArrayList<String>();
    ArrayList<String>  OfferDesc = new ArrayList<String>();
    ArrayList<String>  StartDate = new ArrayList<String>();
    ArrayList<String>  EndDate = new ArrayList<String>();

//    FavoritesAdapter favoritesAdapter ;
//    String fav_id;
    List<Favourites> favourites ;

    public FavoriteFragment() { }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        db = new DatabaseHelper(getContext());
        checkConnection();
        long fav_user_id = 5 ;

        if (checkConnection() == true)
        {

        }

        List<Voucher> voch = db.getVoucherbyID();
        if(voch == null) {
            Toast.makeText(getContext(), "There is no favourite data available", Toast.LENGTH_LONG).show();
            return view;
        }

        for (Voucher todo : voch)
        {
//            Log.d("ToDo Product_Id ", todo.getProduct_id());
            Log.d("ToDo Store Name ", todo.getStore_name());
            Log.d("ToDo Offer Title ", todo.getOffer_title());
            Log.d("ToDo Offer Desc ", todo.getOffer_desc());
            Log.d("ToDo Start Date ", todo.getStart_date());
            Log.d("ToDo End Date ", todo.getEnd_date());

            Toast.makeText(getContext(),"Product_ID "+todo.getProduct_id(),Toast.LENGTH_LONG).show();

            StoreName.add(todo.getStore_name().toString());
            OfferTitle.add(todo.getOffer_title().toString());
            OfferDesc.add(todo.getOffer_desc().toString());
            StartDate.add(todo.getStart_date().toString());
            EndDate.add(todo.getEnd_date().toString());

            Bitmap bitmap = BitmapFactory.decodeByteArray(todo.getStore_image(), 0, todo.getStore_image().length);

            favImage.add(bitmap);
            favText.add(todo.getStore_name());
        }

        favoriteAdapter = new FavoriteAdapter(getActivity(), favImage, favText);
//        favoritesAdapter = new FavoritesAdapter(getActivity(), StoreName, OfferTitle, OfferDesc, StartDate, EndDate);

        Fav_gridView = (GridView) view.findViewById(R.id.Fav_gridView);
        Fav_gridView.setAdapter(favoriteAdapter);     // adapter for image and text
//        Fav_gridView.setAdapter(favoritesAdapter);    // adapter for all text

        // For getting favorites data..
//        favourites = db.getFavoritesData();

      /*  for(Favourites fav : favourites)
        {
            int fav_id = fav.getId();
            String user_id = fav.getUser_id();
            String product_id = fav.getProduct_id();

            Toast.makeText(getActivity(),"fav_id: "+fav_id,Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(),"user_id: "+user_id,Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(),"product_id: "+product_id,Toast.LENGTH_SHORT).show();
        }*/

//        List<Favourites> allTags = db.getAllFavourites();
//        for (Favourites fav : favourites)
//        {
//            String fav_id = fav.getProduct_id();
//            Log.d("Fav product_id", fav_id);
//
////            List<Voucher> voch = db.getVoucherByProductId(fav_id);
////            vouchers = db.getVoucherByProductId(fav_id);
//
//            if(fav_id == null)
//            {
//                Toast.makeText(getContext(), "Product Id = " +fav_id, Toast.LENGTH_LONG).show();
//            }
//            else
//            {
//                Toast.makeText(getContext(), "Product Id = " +fav_id, Toast.LENGTH_LONG).show();
//                getVocher(fav_id);
////                for (Voucher todo : voch)
////                {
////                    Log.d("ToDo Product_Id ", todo.getProduct_id());
////                    Log.d("ToDo Store Name ", todo.getStore_name());
////                    Log.d("ToDo Offer Title ", todo.getOffer_title());
////                    Log.d("ToDo Offer Desc ", todo.getOffer_desc());
////                    Log.d("ToDo Start Date ", todo.getStart_date());
////                    Log.d("ToDo End Date ", todo.getEnd_date());
////
////                    Toast.makeText(getContext(),"Product_id: "+todo.getProduct_id(),Toast.LENGTH_LONG).show();
////                    Toast.makeText(getContext(),"Store Name: "+todo.getStore_name(),Toast.LENGTH_LONG).show();
////                }
//            }
////            Toast.makeText(getContext(), "Product Id = " +fav_id, Toast.LENGTH_LONG).show();
//        }


        return view;
    }

    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
        return isConnected ;
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