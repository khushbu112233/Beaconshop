package com.amplearch.beaconshop.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.amplearch.beaconshop.Activity.ElectClaimOfferAcivity;
import com.amplearch.beaconshop.Adapter.FavoriteAdapter;
import com.amplearch.beaconshop.Adapter.FavoritesAdapter;
import com.amplearch.beaconshop.Adapter.VoucherAdapter;
import com.amplearch.beaconshop.ConnectivityReceiver;
import com.amplearch.beaconshop.Model.Favourites;
import com.amplearch.beaconshop.Model.StoreLocation;
import com.amplearch.beaconshop.Model.UserRedeem;
import com.amplearch.beaconshop.Model.Voucher;
import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.Utils.UserSessionManager;
import com.amplearch.beaconshop.WebCall.AsyncRequest;
import com.amplearch.beaconshop.helper.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
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
    Context context ;

    UserSessionManager session;
    String userID;
    String voucherURL  ;
    VoucherAdapter voucherAdapter;

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

        session = new UserSessionManager(getContext());
        final HashMap<String, String> user1 = session.getUserDetails();
        // get name
//        userID = user1.get(UserSessionManager.KEY_USER_ID);
//        voucherURL = "http://beacon.ample-arch.com/BeaconWebService.asmx/getRedeemUserbyUserID?user_id="+userID;

//        AsyncRequest getPosts = new AsyncRequest(FavoriteFragment.this,getActivity(), "GET", null, "");
//        getPosts.execute(voucherURL);

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

            favImage.add(R.drawable.ic_sale);
            favText.add(todo.getStore_name());

        }


        favoriteAdapter = new FavoriteAdapter(getActivity(), favImage, favText);
//        favoritesAdapter = new FavoritesAdapter(getActivity(), StoreName, OfferTitle, OfferDesc, StartDate, EndDate);
        Fav_gridView = (GridView) view.findViewById(R.id.Fav_gridView);
        Fav_gridView.setAdapter(favoriteAdapter);     // adapter for image and text
//        Fav_gridView.setAdapter(favoritesAdapter);    // adapter for all text

        Fav_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent i = new Intent(getContext(), ElectClaimOfferAcivity.class);
                i.putExtra("offer_title", OfferTitle.get(position).toString() );
                i.putExtra("offer_desc", OfferDesc.get(position).toString() );
//                i.putExtra("offer_id", redeemList.get(position).getId() );
//                i.putExtra("quantity", redeemList.get(position).getQuantity() );
//                i.putExtra("offer_image", redeemList.get(position).getOffer_image() );
                startActivity(i);
            }
        });

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

  /*  @Override
    public void asyncResponse(String response)
    {
//        Toast.makeText(getContext(), "Response: "+response, Toast.LENGTH_LONG).show();
        Log.i("Voch res: ", response);

        if (response.equals(""))
        {
            Toast.makeText(getContext(), "Vouchers not Loaded..", Toast.LENGTH_LONG).show();
        }
        else
        {
            //   Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try
            {
                JSONObject jsonObject = new JSONObject(response);
                String res = jsonObject.getString("redeem");
                // String message = jsonObject.getString("User");
                //  Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                if (res==null)
                {
                    Toast.makeText(getContext(), "No Vouchers are Added..", Toast.LENGTH_LONG).show();
                }
                else
                {
                    JSONArray jsonArrayChanged = jsonObject.getJSONArray("redeem");
                    if (jsonArrayChanged.length() == 0)
                    {
//                        tvNoVoucher.setVisibility(View.VISIBLE);
//                        tvNoVoucher.setText("No Vouchers are Added..");
                          Toast.makeText(getContext(), "No Offers are Available..", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
//                        tvNoVoucher.setVisibility(View.GONE);
                    }
                    for (int i = 0, count = jsonArrayChanged.length(); i < count; i++)
                    {
                        try
                        {
                            //JSONObject jObject = jsonArrayChanged.getJSONObject(i);
                            UserRedeem voucherClass = new UserRedeem();
                            voucherClass.setId(jsonArrayChanged.getJSONObject(i).get("id").toString());
                            voucherClass.setOffer_title(jsonArrayChanged.getJSONObject(i).get("offer_title").toString());
                            voucherClass.setOffer_desc(jsonArrayChanged.getJSONObject(i).get("offer_desc").toString());
                            voucherClass.setQuantity(jsonArrayChanged.getJSONObject(i).get("quantity").toString());
                            voucherClass.setUser_id(jsonArrayChanged.getJSONObject(i).get("user_id").toString());
                            voucherClass.setOffer_id(jsonArrayChanged.getJSONObject(i).get("offer_id").toString());
                            voucherClass.setOffer_image(jsonArrayChanged.getJSONObject(i).get("offer_image").toString());
                            voucherClass.setRedeem(jsonArrayChanged.getJSONObject(i).get("redeem").toString());

                            //   Toast.makeText(getContext(),jsonArrayChanged.getJSONObject(i).get("category_id").toString(), Toast.LENGTH_LONG).show();
                            redeemList.add(voucherClass);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    voucherAdapter = new VoucherAdapter(getActivity(), redeemList);
                    // adapter = new CustomFrameList(FestivalListPage.this, friends);
                    Fav_gridView.setAdapter(voucherAdapter);
                    // Toast.makeText(getContext(), res, Toast.LENGTH_LONG).show();
                           *//* Toast.makeText(getApplicationContext(), "LoggedIn Successfully..", Toast.LENGTH_LONG).show();
                            session.createUserLoginSession(username, email_id, image, password, user_id);

                            Intent intent=new Intent(SignInActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();*//*

                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }*/
}