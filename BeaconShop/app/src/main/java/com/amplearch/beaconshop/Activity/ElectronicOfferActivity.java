package com.amplearch.beaconshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.amplearch.beaconshop.Adapter.ElectOfferAdapter;
import com.amplearch.beaconshop.ConnectivityReceiver;
import com.amplearch.beaconshop.Model.InfoWindowData;
import com.amplearch.beaconshop.Model.VoucherClass;
import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.Utils.cgTextView;
import com.amplearch.beaconshop.WebCall.AsyncRequest;
import com.amplearch.beaconshop.helper.CustomInfoWindowGoogleMap;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ElectronicOfferActivity extends AppCompatActivity  implements AsyncRequest.OnAsyncRequestComplete ,OnMapReadyCallback
{
    ListView listView_Elect ;
    ElectOfferAdapter  electOfferAdapter ;
    ArrayList<Integer> elect_Image = new ArrayList<Integer>();
    ArrayList<String> elect_Text = new ArrayList<String>();
    String category_id;
    List<VoucherClass> offers;
    cgTextView tvNoOffer, tvCategoryTitle;
    String apiURL = "http://beacon.ample-arch.com/BeaconWebService.asmx/getOfferbyCategoryID";
    ArrayList<NameValuePair> params;
    String category_name;
    ImageView imgMap;
    private GoogleMap map;
    SupportMapFragment mapFragment;
    ImageView imgLeft;
    int click=0;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electronic_offer);
        checkConnection();
        final Intent intent = getIntent();
        category_id = intent.getStringExtra("category_id");
        category_name = intent.getStringExtra("category_name");
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        // getIntent();
        //  Toast.makeText(getApplicationContext(), category_id, Toast.LENGTH_LONG).show();
        elect_Image.add(R.drawable.ic_sale);
        elect_Text.add("20% Discount on Samsung Mobile.");
        elect_Image.add(R.drawable.ic_sale);
        elect_Text.add("10% Discount on L.G. Mobile.");
        elect_Image.add(R.drawable.ic_sale);
        elect_Text.add("50% Discount on iPhone 7+ .");
        elect_Image.add(R.drawable.ic_sale);
        elect_Text.add("10% Discount on Asus Mobile.");
        elect_Image.add(R.drawable.ic_sale);
        elect_Text.add("20% Discount on Moto G5+ Mobile.");

        offers = new ArrayList<VoucherClass>();
        imgLeft = (ImageView)findViewById(R.id.imgLeft);
        listView_Elect = (ListView)findViewById(R.id.listView_Elect);
        tvNoOffer = (cgTextView) findViewById(R.id.tvNoOffer);
        tvCategoryTitle = (cgTextView) findViewById(R.id.tvCategoryTitle);
        imgMap = (ImageView)findViewById(R.id.imgMap);
        tvCategoryTitle.setText(category_name);
        // listView_Elect.setAdapter(electOfferAdapter);
        listView_Elect.setVisibility(View.VISIBLE);
        mapFragment.getView().setVisibility(View.GONE);

        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click++;
                if(click%2==0)
                {
                    listView_Elect.setVisibility(View.VISIBLE);
                    mapFragment.getView().setVisibility(View.GONE);
                    imgMap.setImageResource(R.drawable.ic_map);

                }else
                {
                    listView_Elect.setVisibility(View.GONE);
                    mapFragment.getView().setVisibility(View.VISIBLE);
                    imgMap.setImageResource(R.drawable.ic_list);

                }
            }
        });

        listView_Elect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent i = new Intent(getApplicationContext(), ElectClaimOfferAcivity.class);
                i.putExtra("offer_title", offers.get(position).getOffer_title() );
                i.putExtra("offer_desc", offers.get(position).getOffer_desc() );
                i.putExtra("offer_id", offers.get(position).getId() );
                i.putExtra("quantity", offers.get(position).getQuantity() );
                i.putExtra("offer_image", offers.get(position).getStore_image() );
                startActivity(i);
            }
        });

        if (checkConnection() == true)
        {
            params = getParams();
            AsyncRequest getPosts = new AsyncRequest(this, "GET", params);
            getPosts.execute(apiURL);
        }
        // connectWithHttpPost();
    }

    private ArrayList<NameValuePair> getParams()
    {
        // define and ArrayList whose elements are of type NameValuePair
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("category_id", category_id));
        return params;
    }

    @Override
    public void asyncResponse(String response)
    {
        Log.e("response",""+response);
        //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
        if (response.equals("")){
            Toast.makeText(getApplicationContext(), "Offers not loaded..", Toast.LENGTH_LONG).show();
        }else {
            //   Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try {
                JSONObject jsonObject = new JSONObject(response);
                String res = jsonObject.getString("offers");
                // String message = jsonObject.getString("User");
                //  Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                if (res.equals("")){
                    tvNoOffer.setVisibility(View.VISIBLE);
                    tvNoOffer.setText("No offers are available..");
                    // Toast.makeText(getApplicationContext(), "No Offers are Available..", Toast.LENGTH_LONG).show();
                }
                else {

                    JSONArray jsonArrayChanged = jsonObject.getJSONArray("offers");
                    String strCount = jsonObject.getString("count");
                    // Toast.makeText(getApplicationContext(), strCount, Toast.LENGTH_LONG).show();

                    tvNoOffer.setVisibility(View.GONE);
                    for (int i = 0, count = jsonArrayChanged.length(); i < count; i++)
                    {
                        try
                        {
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

                    Collections.sort(offers, new Comparator<VoucherClass>()
                    {
                        @Override
                        public int compare(VoucherClass o1, VoucherClass o2) {
                            String s1 = o1.getPaid_banner();
                            String s2 = o2.getPaid_banner();
                            return s2.compareToIgnoreCase(s1);
                        }

                    });

                    electOfferAdapter = new ElectOfferAdapter(getApplicationContext(), offers);
                    // adapter = new CustomFrameList(FestivalListPage.this, friends);
                    listView_Elect.setAdapter(electOfferAdapter);
                    mapFragment.getMapAsync(this);
                    Log.e("size",""+offers.size());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        for(int i=0;i<offers.size();i++)
        {
            LatLng sydney = new LatLng(Double.parseDouble(offers.get(i).getLat()), Double.parseDouble(offers.get(i).getLng()));

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(sydney);

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            InfoWindowData info = new InfoWindowData();
            info.setImage(offers.get(i).getStore_image());
            info.setTitle(offers.get(i).getOffer_title());
            info.setDesc(offers.get(i).getOffer_desc());
            info.setStorename(offers.get(i).getStore_name());

            CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this);
            googleMap.setInfoWindowAdapter(customInfoWindow);

            Marker m = googleMap.addMarker(markerOptions);
            m.setTag(info);
            m.showInfoWindow();


        }

    }


    /*private void connectWithHttpPost() {

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
                HttpGet httpGet = new HttpGet("http://beacon.ample-arch.com/BeaconWebService.asmx/getOfferbyCategoryID?category_id=" + category_id);

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
                    Toast.makeText(getApplicationContext(), "Offers not Loaded..", Toast.LENGTH_LONG).show();
                }else {
                  //   Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String res = jsonObject.getString("offers");
                        // String message = jsonObject.getString("User");
                        //  Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                        if (res==null){

                            Toast.makeText(getApplicationContext(), "No Offers are Available..", Toast.LENGTH_LONG).show();
                        }
                        else {

                            JSONArray jsonArrayChanged = jsonObject.getJSONArray("offers");
                            String strCount = jsonObject.getString("count");
                            Toast.makeText(getApplicationContext(), strCount, Toast.LENGTH_LONG).show();
                            if (jsonArrayChanged.length() == 0){
                                tvNoOffer.setVisibility(View.VISIBLE);
                                tvNoOffer.setText("No Offers are Available..");
                              //  Toast.makeText(getApplicationContext(), "No Offers are Available..", Toast.LENGTH_LONG).show();
                            }else {
                                tvNoOffer.setVisibility(View.GONE);
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
                            electOfferAdapter = new ElectOfferAdapter(getApplicationContext(), offers);
                           // adapter = new CustomFrameList(FestivalListPage.this, friends);
                            listView_Elect.setAdapter(electOfferAdapter);
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

    }*/
}
