package com.amplearch.beaconshop.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.amplearch.beaconshop.Adapter.CategoryAdapter;
import com.amplearch.beaconshop.Adapter.ElectOfferAdapter;
import com.amplearch.beaconshop.Model.Voucher;
import com.amplearch.beaconshop.Model.VoucherClass;
import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.Utils.TrojanText;

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
import java.util.List;

public class ElectronicOfferActivity extends AppCompatActivity
{
    ListView listView_Elect ;
    ElectOfferAdapter  electOfferAdapter ;
    ArrayList<Integer> elect_Image = new ArrayList<Integer>();
    ArrayList<String> elect_Text = new ArrayList<String>();
    String category_id;
    List<VoucherClass> offers;
    TrojanText tvNoOffer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electronic_offer);
        final Intent intent = getIntent();
        category_id = intent.getStringExtra("category_id");
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


        listView_Elect = (ListView)findViewById(R.id.listView_Elect);
        tvNoOffer = (TrojanText) findViewById(R.id.tvNoOffer);
       // listView_Elect.setAdapter(electOfferAdapter);

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

        connectWithHttpPost();
    }


    private void connectWithHttpPost() {

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
                           /* Toast.makeText(getApplicationContext(), "LoggedIn Successfully..", Toast.LENGTH_LONG).show();
                            session.createUserLoginSession(username, email_id, image, password, user_id);

                            Intent intent=new Intent(SignInActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();*/

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
}
