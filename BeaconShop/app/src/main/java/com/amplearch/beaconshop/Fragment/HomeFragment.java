package com.amplearch.beaconshop.Fragment;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.amplearch.beaconshop.Activity.ElectClaimOfferAcivity;
import com.amplearch.beaconshop.Activity.ElectronicOfferActivity;
import com.amplearch.beaconshop.Adapter.CategoryAdapter;
import com.amplearch.beaconshop.Adapter.FavoriteAdapter;
import com.amplearch.beaconshop.Adapter.PaidBannerHorizontal;
import com.amplearch.beaconshop.ConnectivityReceiver;
import com.amplearch.beaconshop.Model.StoreLocation;
import com.amplearch.beaconshop.Model.VoucherClass;
import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.Utils.GPSTracker;
import com.amplearch.beaconshop.Utils.RecyclerItemClickListener;
import com.amplearch.beaconshop.Utils.TrojanText;
import com.amplearch.beaconshop.WebCall.AsyncRequest;
import com.amplearch.beaconshop.WebCall.JayRequest;
import com.amplearch.beaconshop.WebCall.JoyRequest;
import com.amplearch.beaconshop.helper.DatabaseHelper;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements JayRequest.OnAsyncRequestComplete, AsyncRequest.OnAsyncRequestComplete, JoyRequest.OnJoyRequestComplete
{

    GridView listCategory;
    GridView listNearby;

    ArrayList<String> title_array = new ArrayList<String>();
    ArrayList<String> notice_array = new ArrayList<String>();
    ArrayList<String> id_array = new ArrayList<String>();
    ArrayList<String> count_array = new ArrayList<String>();

    RecyclerView recyclerPaidBanner;
    String apiURL = "http://beacon.ample-arch.com/BeaconWebService.asmx/GetCategories";
    ArrayList<NameValuePair> params;

    String apiURLPaidBanner = "http://beacon.ample-arch.com/BeaconWebService.asmx/getPaidOffer";
    ArrayList<NameValuePair> paramsPaid;

    String category_id;
    List<VoucherClass> offers;
    PaidBannerHorizontal paidBannerHorizontal;
    Button btnALLOfferList;
    DatabaseHelper db;
    double fix_Latitude, fix_Longitude;
    double cur_Latitude, cur_Longitude;
    double dist, theta;
    int dist_int;
    float dist_float;
    double dist_double;
    GPSTracker gps;
    Button btnNearby;
    TrojanText tvNoNearby;
    FavoriteAdapter favoriteAdapter ;
    ArrayList<Bitmap> favImage = new ArrayList<Bitmap>();
    ArrayList<String>  favText = new ArrayList<String>();
    ArrayList<String>  favText1 = new ArrayList<String>();
    ArrayList<String>  StoreName = new ArrayList<String>();
    ArrayList<String>  OfferTitle = new ArrayList<String>();
    ArrayList<byte[]>  OfferImage = new ArrayList<byte[]>();
    ArrayList<String>  OfferID = new ArrayList<String>();
    ArrayList<String>  OfferQuantity = new ArrayList<String>();
    ArrayList<String>  OfferDesc = new ArrayList<String>();
    ArrayList<String>  StartDate = new ArrayList<String>();
    ArrayList<String>  EndDate = new ArrayList<String>();

    public HomeFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        listCategory =(GridView) rootView.findViewById(R.id.category_list);
        listNearby = (GridView) rootView.findViewById(R.id.nearby_list);
        btnALLOfferList = (Button) rootView.findViewById(R.id.btnALLOfferList);
        btnNearby = (Button) rootView.findViewById(R.id.btnNearby);
        recyclerPaidBanner = (RecyclerView) rootView.findViewById(R.id.recyclerPaidBanner);
        tvNoNearby = (TrojanText) rootView.findViewById(R.id.tvNoNearby);
        tvNoNearby.setVisibility(View.GONE);
        offers = new ArrayList<VoucherClass>();
        listCategory.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id)
            {
                //  Toast.makeText(getContext(), "GridView Item: " , Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), ElectronicOfferActivity.class);
                category_id = id_array.get(i).toString();
                intent.putExtra("category_id", category_id);
                intent.putExtra("category_name", title_array.get(i).toString());
                // Toast.makeText(getContext(), id_array.get(i).toString(), Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        });


        listNearby.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent i = new Intent(getContext(), ElectClaimOfferAcivity.class);
                i.putExtra("offer_title", OfferTitle.get(position).toString() );
                i.putExtra("offer_desc", OfferDesc.get(position).toString() );
                i.putExtra("offer_id", OfferID.get(position).toString());
                i.putExtra("quantity", OfferQuantity.get(position).toString() );

                ImageView image = new ImageView(getContext());
                Bitmap bitmap = BitmapFactory.decodeByteArray(OfferImage.get(position), 0, OfferImage.get(position).length);

                image.setImageBitmap(bitmap);
                Bitmap image1 = ((BitmapDrawable) image.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                image1.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object

                String imgString = Base64.encodeToString(getBytesFromBitmap(image1), Base64.NO_WRAP);

               // Toast.makeText(getContext(), imgString, Toast.LENGTH_LONG).show();

                i.putExtra("offer_image",imgString );
                startActivity(i);
            }
        });


        btnNearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tvNoNearby.setVisibility(View.VISIBLE);
                listNearby.setVisibility(View.VISIBLE);
                listCategory.setVisibility(View.INVISIBLE);
                recyclerPaidBanner.setVisibility(View.INVISIBLE);
                btnNearby.setBackgroundColor(getResources().getColor(R.color.logo_color));
                btnNearby.setTextColor(getResources().getColor(R.color.white));
                btnALLOfferList.setBackgroundColor(getResources().getColor(R.color.white));
                btnALLOfferList.setTextColor(getResources().getColor(R.color.logo_color));
            }
        });

        btnALLOfferList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvNoNearby.setVisibility(View.GONE);
                listNearby.setVisibility(View.GONE);
                listCategory.setVisibility(View.VISIBLE);
                recyclerPaidBanner.setVisibility(View.VISIBLE);
                btnALLOfferList.setBackgroundColor(getResources().getColor(R.color.logo_color));
                btnALLOfferList.setTextColor(getResources().getColor(R.color.white));
                btnNearby.setBackgroundColor(getResources().getColor(R.color.white));
                btnNearby.setTextColor(getResources().getColor(R.color.logo_color));
            }
        });
        recyclerPaidBanner.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerPaidBanner.setLayoutManager(layoutManager);

        if (checkConnection())
        {
            params = getParams();
            JayRequest getPosts = new JayRequest(HomeFragment.this, getActivity(), "GET", params, "");
            getPosts.execute(apiURL);

            paramsPaid = getParamPaid();
            JoyRequest getPostBanner = new JoyRequest(HomeFragment.this, getActivity(), "GET", paramsPaid, "");
            getPostBanner.execute(apiURLPaidBanner);
        }
        gps = new GPSTracker(getContext());

        // check if GPS enabled
        if(gps.canGetLocation()){

            cur_Latitude = gps.getLatitude();
            cur_Longitude = gps.getLongitude();

            // \n is for new line
           // Toast.makeText(getContext(), "Your Location is - \nLat: " + cur_Latitude + "\nLong: " + cur_Longitude, Toast.LENGTH_LONG).show();
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }


        db = new DatabaseHelper(getContext());

        List<StoreLocation> allTags = db.getAllLocations();
        for (StoreLocation tag : allTags) {
            Log.d("StoreLocation Name", tag.getStore_name());
            //  Toast.makeText(getApplicationContext(), tag.getStore_name() + " " + tag.getOffer_title(), Toast.LENGTH_LONG).show();

                /*Toast.makeText(this,
                        cursor.getString(cursor.getColumnIndex(StoreLocations._ID)) +
                                ", " + cursor.getString(cursor.getColumnIndex(StoreLocations.FIELD_OFFER_TITLE)) +
                                ", " + cursor.getString(cursor.getColumnIndex(StoreLocations.FIELD_STORE_NAME)) +
                                ", " + cursor.getString(cursor.getColumnIndex(StoreLocations.FIELD_START_DATE)),
                        Toast.LENGTH_SHORT).show();*/
            try {

                fix_Latitude = Double.parseDouble(tag.getLat());
                fix_Longitude = Double.parseDouble(tag.getLng());
            }catch (NumberFormatException e){

            }
            theta = fix_Longitude - cur_Longitude;
            dist = (Math.sin(deg2rad(fix_Latitude)) * Math.sin(deg2rad(cur_Latitude)))
                    + (Math.cos(deg2rad(fix_Latitude)) * Math.cos(deg2rad(cur_Latitude)))
                    * Math.cos(deg2rad(theta));
            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.753; //1.1515

            //Number Format code
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            numberFormat.setMinimumFractionDigits(2);
            numberFormat.setMaximumFractionDigits(3);
            String distt_km = numberFormat.format(dist);
            float disttt_km = Float.parseFloat(String.valueOf((dist)));

            //distance units
            dist_double = dist; // double
            dist_float = disttt_km * 1000;  //
            dist_int = (int) dist_float;

            String dist_D = String.valueOf(dist_double);
            String dist_F = String.valueOf(disttt_km);
            String dist_I = String.valueOf(dist_int);
            // tvDouble_Dist.setText(dist_D+" Km");
            //tvFloat_Dist.setText(dist_F+" Km");
            // tvInteger_Dist.setText(dist_I+" M");

            if (dist_int > 0 && dist_int < 500)
            {
                int mId = Integer.parseInt(String.valueOf(tag.getId()));
                // Toast.makeText(getApplicationContext(), "Please! Enable Bluetooth " + dist_int, Toast.LENGTH_LONG).show();
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (!mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.enable();

                }

              //  Toast.makeText(getContext(), tag.getOffer_title() + " at " + tag.getStore_name(), Toast.LENGTH_LONG).show();

                Bitmap bitmap = BitmapFactory.decodeByteArray(tag.getStore_image(), 0, tag.getStore_image().length);

                StoreName.add(tag.getStore_name().toString());
                OfferTitle.add(tag.getOffer_title().toString());
                OfferQuantity.add(tag.getQuantity().toString());
                OfferID.add(String.valueOf(tag.getProduct_id()));
                OfferDesc.add(tag.getOffer_desc().toString());
                StartDate.add(tag.getStart_date().toString());
                EndDate.add(tag.getEnd_date().toString());
                OfferImage.add(tag.getStore_image());

                favImage.add(bitmap);
                favText.add(tag.getStore_name());
                favText1.add(tag.getOffer_desc());

               /* NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.ic_bluetooth_black_24dp)
                                .setContentTitle(tag.getOffer_title() + " at " + tag.getStore_name())
                                .setContentText(tag.getOffer_desc());
// Creates an explicit intent for an Activity in your app
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
                mNotificationManager.notify(mId, mBuilder.build());
*/

                //     tvStatus.setText("Status: "+"Please! Enable Bluetooth Mode.");
            } else if (dist_int > 500 && dist_int < 1000) {
                // Toast.makeText(getApplicationContext(), "You are nearest to Mall within" + dist_int + " Meter", Toast.LENGTH_LONG).show();
                //       tvStatus.setText("Status: "+"You are nearest to Mall within "+dist_int+" meters.");
            } else if (dist_int > 1000 && dist_int < 2000) {
                // Toast.makeText(getApplicationContext(), "Your distance from Mall is: " + dist_int + " Meters.", Toast.LENGTH_LONG).show();
                //       tvStatus.setText("Status: "+"Your distance from Mall is "+distt_km+" km.");
            } else if (dist_int > 2000 && dist_int < 3000) {
                // Toast.makeText(getApplicationContext(), "Distance from mall: " + distt_km + " Km", Toast.LENGTH_LONG).show();
                //      tvStatus.setText("Status: "+"Find Alpha-One Mall in range of "+distt_km+" kms distance.");
            } else if (dist_int > 3000) {
                // Toast.makeText(getApplicationContext(), "Away from.." + distt_km + " Km", Toast.LENGTH_LONG).show();
                //   tvStatus.setText("You are away from Alpha-One Mall at "+distt_km+" Km.");
            } else {
                //    tvStatus.setText("Ta-Ta Bye.. Bye.. "+distt_km+" Km.");
            }

            int dist_cm = dist_int * 100;
            String cm = String.valueOf(dist_cm);

        }

        if (favImage.size() == 0 && favText.size() == 0&&favText1.size()==0){
            tvNoNearby.setVisibility(View.VISIBLE);
        }
        else {
            tvNoNearby.setVisibility(View.GONE);
        }
        favoriteAdapter = new FavoriteAdapter(getActivity(), favImage, favText,favText1);
//        favoritesAdapter = new FavoritesAdapter(getActivity(), StoreName, OfferTitle, OfferDesc, StartDate, EndDate);
       // Fav_gridView = (GridView) view.findViewById(R.id.Fav_gridView);
        listNearby.setAdapter(favoriteAdapter);

        recyclerPaidBanner.addOnItemTouchListener
                (
                new RecyclerItemClickListener(getContext(), recyclerPaidBanner ,new RecyclerItemClickListener.OnItemClickListener()
                {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                        if(offers.get(position).getStore_image() != null) {

                            Intent i = new Intent(getContext(), ElectClaimOfferAcivity.class);
                            i.putExtra("offer_title", offers.get(position).getOffer_title() );
                            i.putExtra("offer_desc", offers.get(position).getOffer_desc() );
                            i.putExtra("offer_id", offers.get(position).getId() );
                            i.putExtra("quantity", offers.get(position).getQuantity() );
                            i.putExtra("offer_image", offers.get(position).getStore_image() );
                            i.putExtra("category_id", offers.get(position).getCategory_id() );

                            startActivity(i);

                            /*Picasso.with(getApplicationContext()).load("http://www.kumbhdesign.in/mobile-app/depost/api/assets/"+back_list.get(position).getBackground_img())
                                    .into(main_image.set);*/
                        }
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        return rootView;
    }

    private double deg2rad(double deg)
    {
        return ( (deg * Math.PI )/ 180.0 );
    }

    private double rad2deg(double rad )
    {
        return ( (rad * 180.0)/ (Math.PI) );
    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
        return  isConnected;
    }

    private void showSnack(boolean isConnected) {
        String message = "Check For Data Connection..";
        if (isConnected) {
//            message = "Good! Connected to Internet";
        } else {
//            message = "Sorry! Not connected to internet";
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void asyncResponse(String response)
    {
        //  Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();

        if (response.equals("")) {
            Toast.makeText(getContext(), "Category not Loaded..", Toast.LENGTH_LONG).show();
        }
        else
            {
            // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try {
                JSONObject jsonObject = new JSONObject(response);
                String res = jsonObject.getString("category");
                // String message = jsonObject.getString("User");
                //  Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                if (res == null) {

                    Toast.makeText(getContext(), "No Categories are Available..", Toast.LENGTH_LONG).show();
                } else {

                    JSONArray jsonArrayChanged = jsonObject.getJSONArray("category");
                    for (int i = 0, count = jsonArrayChanged.length(); i < count; i++) {
                        try {
                            //JSONObject jObject = jsonArrayChanged.getJSONObject(i);

                            title_array.add(jsonArrayChanged.getJSONObject(i).get("category_name").toString());
                            notice_array.add(jsonArrayChanged.getJSONObject(i).get("category_image").toString());
                            id_array.add(jsonArrayChanged.getJSONObject(i).get("category_id").toString());
                            count_array.add(jsonArrayChanged.getJSONObject(i).get("count").toString());
                            //   Toast.makeText(getContext(),jsonArrayChanged.getJSONObject(i).get("category_id").toString(), Toast.LENGTH_LONG).show();
                            CategoryAdapter adapterViewAndroid = new CategoryAdapter(getContext(), title_array, notice_array, count_array);
                            listCategory.setAdapter(adapterViewAndroid);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            /*try {
                JSONObject jsonObject = new JSONObject(response);
                String res = jsonObject.getString("offers");
                // String message = jsonObject.getString("User");
                //  Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                if (res==null){

                    Toast.makeText(getContext(), "No Paid Offers are Available..", Toast.LENGTH_LONG).show();
                }
                else {

                    JSONArray jsonArrayChanged = jsonObject.getJSONArray("offers");
                    if (jsonArrayChanged.length() == 0){
                        recyclerPaidBanner.setVisibility(View.GONE);
                        //  tvNoOffer.setText("No Offers are Available..");
                        //  Toast.makeText(getApplicationContext(), "No Offers are Available..", Toast.LENGTH_LONG).show();
                    }else {
                        recyclerPaidBanner.setVisibility(View.VISIBLE);
                    }
                    for (int i = 0, count = jsonArrayChanged.length(); i < count; i++) {
                        try {
                            //JSONObject jObject = jsonArrayChanged.getJSONObject(i);
                            VoucherClass voucherClass = new VoucherClass();
                            voucherClass.setId(jsonArrayChanged.getJSONObject(i).get("id").toString());
                            voucherClass.setCategory_id(jsonArrayChanged.getJSONObject(i).get("category_id").toString());
                            voucherClass.setStore_name(jsonArrayChanged.getJSONObject(i).get("store_name").toString());
                            voucherClass.setStore_image(jsonArrayChanged.getJSONObject(i).get("store_image").toString());
                            voucherClass.setOffer_title(jsonArrayChanged.getJSONObject(i).get("offer_title").toString());
                            voucherClass.setOffer_desc(jsonArrayChanged.getJSONObject(i).get("offer_desc").toString());
                            voucherClass.setStart_date(jsonArrayChanged.getJSONObject(i).get("start_date").toString());
                            voucherClass.setEnd_date(jsonArrayChanged.getJSONObject(i).get("end_date").toString());
                            voucherClass.setMessage(jsonArrayChanged.getJSONObject(i).get("message").toString());
                            voucherClass.setUuid(jsonArrayChanged.getJSONObject(i).get("uuid").toString());
                            voucherClass.setMajor(jsonArrayChanged.getJSONObject(i).get("major").toString());
                            voucherClass.setMinor(jsonArrayChanged.getJSONObject(i).get("minor").toString());
                            voucherClass.setQuantity(jsonArrayChanged.getJSONObject(i).get("quantity").toString());
                            voucherClass.setPaid_banner(jsonArrayChanged.getJSONObject(i).get("paid_banner").toString());
                            voucherClass.setPaid_start_date(jsonArrayChanged.getJSONObject(i).get("paid_start_date").toString());
                            voucherClass.setPaid_end_date(jsonArrayChanged.getJSONObject(i).get("paid_end_date").toString());
                            voucherClass.setLat(jsonArrayChanged.getJSONObject(i).get("lat").toString());
                            voucherClass.setLng(jsonArrayChanged.getJSONObject(i).get("lng").toString());

                            //   Toast.makeText(getContext(),jsonArrayChanged.getJSONObject(i).get("category_id").toString(), Toast.LENGTH_LONG).show();
                            offers.add(voucherClass);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    paidBannerHorizontal = new PaidBannerHorizontal(getActivity(), offers);
                    // adapter = new CustomFrameList(FestivalListPage.this, friends);
                    recyclerPaidBanner.setAdapter(paidBannerHorizontal);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }*/

        }
    }

    @Override
    public void joyResponse(String response)
    {
        if (response.equals("")) {
            Toast.makeText(getContext(), "Category not Loaded..", Toast.LENGTH_LONG).show();
        }
        else {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String res = jsonObject.getString("offers");
                // String message = jsonObject.getString("User");
                //  Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                if (res == null) {

                    Toast.makeText(getContext(), "No Paid Offers are Available..", Toast.LENGTH_LONG).show();
                } else {

                    JSONArray jsonArrayChanged = jsonObject.getJSONArray("offers");
                    if (jsonArrayChanged.length() == 0) {
                        recyclerPaidBanner.setVisibility(View.GONE);
                        //  tvNoOffer.setText("No Offers are Available..");
                        //  Toast.makeText(getApplicationContext(), "No Offers are Available..", Toast.LENGTH_LONG).show();
                    } else {
                        recyclerPaidBanner.setVisibility(View.VISIBLE);
                    }
                    for (int i = 0, count = jsonArrayChanged.length(); i < count; i++) {
                        try {
                            //JSONObject jObject = jsonArrayChanged.getJSONObject(i);
                            VoucherClass voucherClass = new VoucherClass();
                            voucherClass.setId(jsonArrayChanged.getJSONObject(i).get("id").toString());
                            voucherClass.setCategory_id(jsonArrayChanged.getJSONObject(i).get("category_id").toString());
                            voucherClass.setStore_name(jsonArrayChanged.getJSONObject(i).get("store_name").toString());
                            voucherClass.setStore_image(jsonArrayChanged.getJSONObject(i).get("store_image").toString());
                            voucherClass.setOffer_title(jsonArrayChanged.getJSONObject(i).get("offer_title").toString());
                            voucherClass.setOffer_desc(jsonArrayChanged.getJSONObject(i).get("offer_desc").toString());
                            voucherClass.setStart_date(jsonArrayChanged.getJSONObject(i).get("start_date").toString());
                            voucherClass.setEnd_date(jsonArrayChanged.getJSONObject(i).get("end_date").toString());
                            voucherClass.setMessage(jsonArrayChanged.getJSONObject(i).get("message").toString());
                            voucherClass.setUuid(jsonArrayChanged.getJSONObject(i).get("uuid").toString());
                            voucherClass.setMajor(jsonArrayChanged.getJSONObject(i).get("major").toString());
                            voucherClass.setMinor(jsonArrayChanged.getJSONObject(i).get("minor").toString());
                            voucherClass.setQuantity(jsonArrayChanged.getJSONObject(i).get("quantity").toString());
                            voucherClass.setPaid_banner(jsonArrayChanged.getJSONObject(i).get("paid_banner").toString());
                            voucherClass.setPaid_start_date(jsonArrayChanged.getJSONObject(i).get("paid_start_date").toString());
                            voucherClass.setPaid_end_date(jsonArrayChanged.getJSONObject(i).get("paid_end_date").toString());
                            voucherClass.setLat(jsonArrayChanged.getJSONObject(i).get("lat").toString());
                            voucherClass.setLng(jsonArrayChanged.getJSONObject(i).get("lng").toString());

                            //   Toast.makeText(getContext(),jsonArrayChanged.getJSONObject(i).get("category_id").toString(), Toast.LENGTH_LONG).show();
                            offers.add(voucherClass);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    paidBannerHorizontal = new PaidBannerHorizontal(getActivity(), offers);
                    // adapter = new CustomFrameList(FestivalListPage.this, friends);
                    recyclerPaidBanner.setAdapter(paidBannerHorizontal);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    private ArrayList<NameValuePair> getParams() {
        // define and ArrayList whose elements are of type NameValuePair
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("start", "0"));
        params.add(new BasicNameValuePair("limit", "10"));
        params.add(new BasicNameValuePair("fields", "id,title"));
        return params;
    }


    private ArrayList<NameValuePair> getParamPaid() {
        // define and ArrayList whose elements are of type NameValuePair
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("paid_banner", "1"));
        return params;
    }
   /* private void connectWithHttpPost() {

        // Connect with a server is a time consuming process.
        //Therefore we use AsyncTask to handle it
        // From the three generic types;
        //First type relate with the argument send in execute()
        //Second type relate with onProgressUpdate method which I haven't use in this code
        //Third type relate with the return type of the doInBackground method, which also the input type of the onPostExecute method
        class HttpGetAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                // As you can see, doInBackground has taken an Array of Strings as the argument
                //We need to specifically get the givenUsername and givenPassword
              //  String paramUsername = params[0];
               // String paramPassword = params[1];
          //      System.out.println("paramUsername is :" + paramUsername + " paramPassword is :" + paramPassword);

                // Create an intermediate to connect with the Internet
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("http://beacon.ample-arch.com/BeaconWebService.asmx/GetCategories");

                try {
                    // execute(); executes a request using the default context.
                    // Then we assign the execution result to HttpResponse
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    System.out.println("httpResponse");

                    // getEntity() ; obtains the message entity of this response
                    // getContent() ; creates a new InputStream object of the entity.
                    // Now we need a readable source to read the byte stream that comes as the httpResponse
                    InputStream inputStream = httpResponse.getEntity().getContent();

                    // We have a byte stream. Next step is to convert it to a Character stream
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                    // Then we have to wraps the existing reader (InputStreamReader) and buffer the input
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    // InputStreamReader contains a buffer of bytes read from the source stream and converts these into characters as needed.
                    //The buffer size is 8K
                    //Therefore we need a mechanism to append the separately coming chunks in to one String element
                    // We have to use a class that can handle modifiable sequence of characters for use in creating String
                    StringBuilder stringBuilder = new StringBuilder();

                    String bufferedStrChunk = null;

                    // There may be so many buffered chunks. We have to go through each and every chunk of characters
                    //and assign a each chunk to bufferedStrChunk String variable
                    //and append that value one by one to the stringBuilder
                    while((bufferedStrChunk = bufferedReader.readLine()) != null){
                        stringBuilder.append(bufferedStrChunk);
                    }

                    // Now we have the whole response as a String value.
                    //We return that value then the onPostExecute() can handle the content
                    System.out.println("Returning value of doInBackground :" + stringBuilder.toString());

                    // If the Username and Password match, it will return "working" as response
                    // If the Username or Password wrong, it will return "invalid" as response
                    return stringBuilder.toString();

                } catch (ClientProtocolException cpe) {
                    System.out.println("Exception generates caz of httpResponse :" + cpe);
                    cpe.printStackTrace();
                } catch (IOException ioe) {
                    System.out.println("Second exception generates caz of httpResponse :" + ioe);
                    ioe.printStackTrace();
                }

                return null;
            }

            // Argument comes for this method according to the return type of the doInBackground() and
            //it is the third generic type of the AsyncTask
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                if (result.equals("")){
                    Toast.makeText(getContext(), "Category not Loaded..", Toast.LENGTH_LONG).show();
                }else {
                    // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String res = jsonObject.getString("category");
                        // String message = jsonObject.getString("User");
                        //  Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                        if (res==null){

                            Toast.makeText(getContext(), "No Categories are Available..", Toast.LENGTH_LONG).show();
                        }
                        else {

                           *//* JSONArray jsonArrayChanged = jsonObject.getJSONArray("User");

                            String email_id = jsonArrayChanged.getJSONObject(0).get("email_id").toString();
                            String user_id = jsonArrayChanged.getJSONObject(0).get("id").toString();
                            String username = jsonArrayChanged.getJSONObject(0).get("username").toString();
                            String image = jsonArrayChanged.getJSONObject(0).get("image").toString();
                            String dob = jsonArrayChanged.getJSONObject(0).get("dob").toString();
                            String gender = jsonArrayChanged.getJSONObject(0).get("gender").toString();
                            String password = jsonArrayChanged.getJSONObject(0).get("password").toString();
*//*

                            JSONArray jsonArrayChanged = jsonObject.getJSONArray("category");
                            for (int i = 0, count = jsonArrayChanged.length(); i < count; i++) {
                                try {
                                    //JSONObject jObject = jsonArrayChanged.getJSONObject(i);

                                    title_array.add(jsonArrayChanged.getJSONObject(i).get("category_name").toString());
                                    notice_array.add(jsonArrayChanged.getJSONObject(i).get("category_image").toString());
                                    id_array.add(jsonArrayChanged.getJSONObject(i).get("category_id").toString());
                                //  Toast.makeText(getContext(),jsonArrayChanged.getJSONObject(i).get("category_id").toString(), Toast.LENGTH_LONG).show();
                                    CategoryAdapter adapterViewAndroid = new CategoryAdapter(getContext(), title_array, notice_array);
                                    listCategory.setAdapter(adapterViewAndroid);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                           // Toast.makeText(getContext(), res, Toast.LENGTH_LONG).show();
                           *//* Toast.makeText(getApplicationContext(), "LoggedIn Successfully..", Toast.LENGTH_LONG).show();
                            session.createUserLoginSession(username, email_id, image, password, user_id);

                            Intent intent=new Intent(SignInActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();*//*
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // Initialize the AsyncTask class
        HttpGetAsyncTask httpGetAsyncTask = new HttpGetAsyncTask();
        // Parameter we pass in the execute() method is relate to the first generic type of the AsyncTask
        // We are passing the connectWithHttpGet() method arguments to that
        httpGetAsyncTask.execute();

    }
*/

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

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                    alertDialogBuilder.setTitle("Exit Application?");
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            getActivity().moveTaskToBack(true);
                                            android.os.Process.killProcess(android.os.Process.myPid());
                                            System.exit(1);
                                        }
                                    })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.cancel();
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    return true;

                }

                return false;
            }
        });
    }


}
