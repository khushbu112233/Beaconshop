package com.amplearch.beaconshop.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.amplearch.beaconshop.Activity.ElectClaimOfferAcivity;
import com.amplearch.beaconshop.Activity.MainActivity;
import com.amplearch.beaconshop.Adapter.ElectOfferAdapter;
import com.amplearch.beaconshop.Adapter.FavoriteAdapter;
import com.amplearch.beaconshop.ConnectivityReceiver;
import com.amplearch.beaconshop.Model.UserRedeem;
import com.amplearch.beaconshop.Model.UserRedeemSql;
import com.amplearch.beaconshop.Model.Voucher;
import com.amplearch.beaconshop.Model.VoucherClass;
import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.Adapter.VoucherAdapter;
import com.amplearch.beaconshop.Utils.TrojanText;
import com.amplearch.beaconshop.Utils.UserSessionManager;
import com.amplearch.beaconshop.WebCall.AsyncRequest;
import com.amplearch.beaconshop.helper.DatabaseHelper;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ample-arch on 3/31/2017.
 */

public class VoucherFragment extends Fragment implements AsyncRequest.OnAsyncRequestComplete
{
    GridView gridView_Vouch;
    VoucherAdapter voucherAdapter;
    List<UserRedeem> redeemList;
    UserSessionManager session;
    String userID;
    TrojanText tvNoVoucher;

    String voucherURL  ;
    FavoriteAdapter favoriteAdapter ;
    ArrayList<Bitmap> favImage = new ArrayList<Bitmap>();
    ArrayList<String>  favText = new ArrayList<String>();
    DatabaseHelper db;
    ArrayList<String>  UserID = new ArrayList<String>();
    ArrayList<String>  OfferID = new ArrayList<String>();
    ArrayList<byte[]>  OfferImage = new ArrayList<byte[]>();
    ArrayList<String>  OfferCode = new ArrayList<String>();
    ArrayList<String>  OfferQuantity = new ArrayList<String>();
    ArrayList<String>  OfferDesc = new ArrayList<String>();
    ArrayList<String>  OfferTitle = new ArrayList<String>();
    ArrayList<String>  Redeem = new ArrayList<String>();

    public VoucherFragment() { }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_voucher, container, false);
        checkConnection();
        session = new UserSessionManager(getContext());
        gridView_Vouch = (GridView)view.findViewById(R.id.gridView_Vouch);
        tvNoVoucher = (TrojanText) view.findViewById(R.id.tvNoVoucher);
        redeemList = new ArrayList<UserRedeem>();
        final HashMap<String, String> user1 = session.getUserDetails();
        db = new DatabaseHelper(getContext());
        // get name
        userID = user1.get(UserSessionManager.KEY_USER_ID);
        voucherURL = "http://beacon.ample-arch.com/BeaconWebService.asmx/getRedeemUserbyUserID?user_id="+userID;

        if (checkConnection()== true)
        {
            AsyncRequest getPosts = new AsyncRequest(VoucherFragment.this,getActivity(), "GET", null, "");
            getPosts.execute(voucherURL);

            gridView_Vouch.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    Intent i = new Intent(getContext(), ElectClaimOfferAcivity.class);
                    i.putExtra("offer_title", redeemList.get(position).getOffer_title() );
                    i.putExtra("offer_desc", redeemList.get(position).getOffer_desc() );
                    i.putExtra("offer_id", redeemList.get(position).getId() );
                    i.putExtra("quantity", redeemList.get(position).getQuantity() );
                    i.putExtra("offer_image", redeemList.get(position).getOffer_image() );
                    startActivity(i);
                }
            });
        }
        else {

            List<UserRedeemSql> voch = db.getAllRedeemUser();
            if(voch == null) {
                //  Toast.makeText(getContext(), "There is no favourite data available", Toast.LENGTH_LONG).show();
                tvNoVoucher.setVisibility(View.VISIBLE);
                tvNoVoucher.setText("No Vouchers are Added..");
                return view;
            }

            for (UserRedeemSql todo : voch)
            {
//            Log.d("ToDo Product_Id ", todo.getProduct_id());
              /*  Log.d("ToDo Store Name ", todo.getStore_name());
                Log.d("ToDo Offer Title ", todo.getOffer_title());
                Log.d("ToDo Offer Desc ", todo.getOffer_desc());
                Log.d("ToDo Start Date ", todo.getStart_date());
                Log.d("ToDo End Date ", todo.getEnd_date());*/

                // Toast.makeText(getContext(),"Product_ID "+todo.getProduct_id(),Toast.LENGTH_LONG).show();

                UserID.add(todo.getUser_id().toString());
                OfferTitle.add(todo.getOffer_title().toString());
                OfferQuantity.add(todo.getQuantity().toString());
                OfferID.add(todo.getOffer_id().toString());
                OfferDesc.add(todo.getOffer_desc().toString());
                OfferCode.add(todo.getOffer_code().toString());
                Redeem.add(todo.getRedeem().toString());
                OfferImage.add(todo.getStore_image());

                Bitmap bitmap = BitmapFactory.decodeByteArray(todo.getStore_image(), 0, todo.getStore_image().length);

                favImage.add(bitmap);
                favText.add(todo.getOffer_title());
            }

            if (favImage.size() == 0 && favText.size() == 0){
                tvNoVoucher.setVisibility(View.VISIBLE);
                tvNoVoucher.setText("No Vouchers are Added..");
            }
            else {
                tvNoVoucher.setVisibility(View.GONE);
            }
            favoriteAdapter = new FavoriteAdapter(getActivity(), favImage, favText);
//        favoritesAdapter = new FavoritesAdapter(getActivity(), StoreName, OfferTitle, OfferDesc, StartDate, EndDate);
          //  gridView_Vouch = (GridView) view.findViewById(R.id.Fav_gridView);
            gridView_Vouch.setAdapter(favoriteAdapter);
            gridView_Vouch.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    Intent i = new Intent(getContext(), ElectClaimOfferAcivity.class);
                    i.putExtra("offer_title", OfferTitle.get(position).toString() );
                    i.putExtra("offer_desc", OfferDesc.get(position).toString() );
                    i.putExtra("offer_id", OfferID.get(position).toString() );
                    i.putExtra("quantity", OfferQuantity.get(position).toString() );
                    i.putExtra("offer_image", OfferImage.get(position).toString() );
                    startActivity(i);
                }
            });


        }




        return view ;
    }

    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

                    // handle back button

                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);

                    return true;

                }

                return false;
            }
        });
    }

    @Override
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
                        tvNoVoucher.setVisibility(View.VISIBLE);
                        tvNoVoucher.setText("No Vouchers are Added..");
                        //  Toast.makeText(getApplicationContext(), "No Offers are Available..", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        tvNoVoucher.setVisibility(View.GONE);
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
                    gridView_Vouch.setAdapter(voucherAdapter);
                    // Toast.makeText(getContext(), res, Toast.LENGTH_LONG).show();
                           /* Toast.makeText(getApplicationContext(), "LoggedIn Successfully..", Toast.LENGTH_LONG).show();
                            session.createUserLoginSession(username, email_id, image, password, user_id);

                            Intent intent=new Intent(SignInActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();*/

                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkConnection()
    {
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
