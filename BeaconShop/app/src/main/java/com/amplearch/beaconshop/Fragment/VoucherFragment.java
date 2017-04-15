package com.amplearch.beaconshop.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.amplearch.beaconshop.Activity.ElectClaimOfferAcivity;
import com.amplearch.beaconshop.Adapter.ElectOfferAdapter;
import com.amplearch.beaconshop.Model.UserRedeem;
import com.amplearch.beaconshop.Model.VoucherClass;
import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.Adapter.VoucherAdapter;
import com.amplearch.beaconshop.Utils.TrojanText;
import com.amplearch.beaconshop.Utils.UserSessionManager;

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

public class VoucherFragment extends Fragment
{
    GridView gridView_Vouch;
    VoucherAdapter voucherAdapter;
    List<UserRedeem> redeemList;
    UserSessionManager session;
    String userID;
    TrojanText tvNoVoucher;

    public VoucherFragment() { }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_voucher, container, false);

        session = new UserSessionManager(getContext());
        gridView_Vouch = (GridView)view.findViewById(R.id.gridView_Vouch);
        tvNoVoucher = (TrojanText) view.findViewById(R.id.tvNoVoucher);
        redeemList = new ArrayList<UserRedeem>();
        final HashMap<String, String> user1 = session.getUserDetails();

        // get name
        userID = user1.get(UserSessionManager.KEY_USER_ID);
        connectWithHttpPost();

        gridView_Vouch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        return view ;
    }

    private void connectWithHttpPost() {

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
                HttpGet httpGet = new HttpGet("http://beacon.ample-arch.com/BeaconWebService.asmx/getRedeemUserbyUserID?user_id="+userID);

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
                    Toast.makeText(getContext(), "Vouchers not Loaded..", Toast.LENGTH_LONG).show();
                }else {
                    //   Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String res = jsonObject.getString("redeem");
                        // String message = jsonObject.getString("User");
                        //  Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                        if (res==null){

                            Toast.makeText(getContext(), "No Vouchers are Added..", Toast.LENGTH_LONG).show();
                        }
                        else {

                            JSONArray jsonArrayChanged = jsonObject.getJSONArray("redeem");
                            if (jsonArrayChanged.length() == 0){
                                tvNoVoucher.setVisibility(View.VISIBLE);
                                tvNoVoucher.setText("No Vouchers are Added..");
                                //  Toast.makeText(getApplicationContext(), "No Offers are Available..", Toast.LENGTH_LONG).show();
                            }else {
                                tvNoVoucher.setVisibility(View.GONE);
                            }
                            for (int i = 0, count = jsonArrayChanged.length(); i < count; i++) {
                                try {
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
                                } catch (JSONException e) {
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
