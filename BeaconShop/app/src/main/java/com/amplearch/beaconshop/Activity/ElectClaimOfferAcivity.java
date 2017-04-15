package com.amplearch.beaconshop.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.Utils.TrojanButton;
import com.amplearch.beaconshop.Utils.TrojanCheckBox;
import com.amplearch.beaconshop.Utils.TrojanText;
import com.amplearch.beaconshop.Utils.UserSessionManager;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ample-arch on 4/13/2017.
 */

public class ElectClaimOfferAcivity extends AppCompatActivity implements View.OnClickListener
{
    TrojanText tvItemOffers, tvItemOfferDetails ;
    TrojanCheckBox tvCheckAgree ;
    TrojanButton btnItemClaimOffer ;
    ImageView ivFacebook, ivFavorite, ivShare ;
    String offer_title, offer_desc, offer_id, quantity, offer_image;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    UserSessionManager session;
    String userID;
    TrojanCheckBox chAgree;
    Boolean isAgreeChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        facebookSDKInitialize();
        setContentView(R.layout.claim_offers);
        shareDialog = new ShareDialog(this);
        Intent intent = getIntent();
        offer_title = intent.getStringExtra("offer_title");
        offer_desc = intent.getStringExtra("offer_desc");
        offer_id = intent.getStringExtra("offer_id");
        quantity = intent.getStringExtra("quantity");
        offer_image = intent.getStringExtra("offer_image");

        session = new UserSessionManager(getApplicationContext());
        tvItemOffers = (TrojanText)findViewById(R.id.tvItemOffer);
        tvItemOfferDetails = (TrojanText) findViewById(R.id.tvItemOfferDetails);
        chAgree = (TrojanCheckBox) findViewById(R.id.chAgree);
        ivFavorite = (ImageView)findViewById(R.id.ivFavorite);
        ivShare = (ImageView)findViewById(R.id.ivShare);
        ivFacebook = (ImageView)findViewById(R.id.ivFacebook);
        btnItemClaimOffer = (TrojanButton)findViewById(R.id.btnItemClaimOffer);

        tvItemOffers.setText(offer_title);
        tvItemOfferDetails.setText(offer_desc);
        ivFacebook.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        ivFavorite.setOnClickListener(this);
        btnItemClaimOffer.setOnClickListener(this);

        final HashMap<String, String> user1 = session.getUserDetails();

        // get name
        userID = user1.get(UserSessionManager.KEY_USER_ID);
        getQuantityWithHttpPost(userID, offer_id);

        chAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    isAgreeChecked = true;
                }
                else {
                    isAgreeChecked = false;
                }
            }
        });

    }

    @Override
    public void onClick(View v)
    {
        if (v == ivFacebook)
        {
            if (ShareDialog.canShow(ShareLinkContent.class)) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentTitle("How to integrate Linkedin from your app")
                        .setImageUrl(Uri.parse("https://www.numetriclabz.com/wp-content/uploads/2015/11/114.png"))
                        .setContentDescription(
                                "simple LinkedIn integration")
                        .setContentUrl(Uri.parse("https://www.numetriclabz.com/android-linkedin-integration-login-tutorial/"))
                        .build();

                shareDialog.show(linkContent);  // Show facebook ShareDialog
            }
        }
        if (v == ivShare)
        {
            String shareBody = "Here is the share content body";
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Reddem Code");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share Via"));
        }
        if (v == ivFavorite)
        {
            Toast.makeText(getApplicationContext(),"Added to Favorite.",Toast.LENGTH_LONG).show();
        }
        if (v == btnItemClaimOffer)
        {
           // Toast.makeText(getApplicationContext(),"You are not ELIGIBLE to claim this offer.",Toast.LENGTH_LONG).show();
            if (isAgreeChecked) {
                connectWithHttpPost(userID, offer_id, quantity, "1", offer_image, offer_title, offer_desc);
            }else {
                Toast.makeText(getApplicationContext(), "Please Agree to Terms & Conditions before Claim Offer..", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void connectWithHttpPost(final String user_id, final String offer_id, final String quantity, final String redeem, final String offer_image, final String offer_title, final String offer_desc)
    {
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
                String paramUserID = params[0];
                String paramOfferID = params[1];
                String paramquantity = params[2];
                String paramredeem = params[3];
                String paramOfferImage = params[4];
                String paramOfferTitle = params[5];
                String paramOfferDesc = params[6];

            //    System.out.println("paramUsername is :" + paramUsername + " paramPassword is :" + paramPassword);

                // Create an intermediate to connect with the Internet
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://beacon.ample-arch.com/BeaconWebService.asmx/addRedeemUser");
                httpPost.setHeader(HTTP.CONTENT_TYPE,
                        "application/x-www-form-urlencoded;charset=UTF-8");

                //Post Data
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(4);
                nameValuePair.add(new BasicNameValuePair("user_id", paramUserID));
                nameValuePair.add(new BasicNameValuePair("offer_id", paramOfferID));
                nameValuePair.add(new BasicNameValuePair("quantity", paramquantity));
                nameValuePair.add(new BasicNameValuePair("redeem", paramredeem));
                nameValuePair.add(new BasicNameValuePair("offer_image", paramOfferImage));
                nameValuePair.add(new BasicNameValuePair("offer_title", paramOfferTitle));
                nameValuePair.add(new BasicNameValuePair("offer_desc", paramOfferDesc));


                //Encoding POST data
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                } catch (UnsupportedEncodingException e) {
                    // log exception
                    e.printStackTrace();
                }

                // Sending a GET request to the web page that we want
                // Because of we are sending a GET request, we have to pass the values through the URL
                try {
                    // execute(); executes a request using the default context.
                    // Then we assign the execution result to HttpResponse
                    HttpResponse httpResponse = httpClient.execute(httpPost);
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
                    Toast.makeText(getApplicationContext(), "Server Connection Failed..", Toast.LENGTH_LONG).show();
                }else {
                   // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String res = jsonObject.getString("message");
                        // String message = jsonObject.getString("User");
                        //  Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                        if (res.equals("Success")){

                            Toast.makeText(getApplicationContext(), "Successfully Claimed..", Toast.LENGTH_LONG).show();
                            getQuantityWithHttpPost(userID, offer_id);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
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
        httpGetAsyncTask.execute(user_id, offer_id, quantity, redeem, offer_image, offer_title, offer_desc);

    }


    private void getQuantityWithHttpPost(String user_id, String offer_id)
    {
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
                String paramUserID = params[0];
                String paramOfferID = params[1];

                //    System.out.println("paramUsername is :" + paramUsername + " paramPassword is :" + paramPassword);

                // Create an intermediate to connect with the Internet
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://beacon.ample-arch.com/BeaconWebService.asmx/getRedeemUserbyUserIDOfferID");
                httpPost.setHeader(HTTP.CONTENT_TYPE,
                        "application/x-www-form-urlencoded;charset=UTF-8");

                //Post Data
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(4);
                nameValuePair.add(new BasicNameValuePair("user_id", paramUserID));
                nameValuePair.add(new BasicNameValuePair("offer_id", paramOfferID));


                //Encoding POST data
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                } catch (UnsupportedEncodingException e) {
                    // log exception
                    e.printStackTrace();
                }


                // Sending a GET request to the web page that we want
                // Because of we are sending a GET request, we have to pass the values through the URL

                try {
                    // execute(); executes a request using the default context.
                    // Then we assign the execution result to HttpResponse
                    HttpResponse httpResponse = httpClient.execute(httpPost);
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
                  //  Toast.makeText(getApplicationContext(), "Login attempt Failed..", Toast.LENGTH_LONG).show();
                }else {
                    //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String res = jsonObject.getString("redeem");
                        // String message = jsonObject.getString("User");
                        //  Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                        if (res == null){
                           // Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                        }
                        else {

                            JSONArray jsonArrayChanged = jsonObject.getJSONArray("redeem");

                            quantity = jsonArrayChanged.getJSONObject(0).get("quantity").toString();
                          //  Toast.makeText(getApplicationContext(), "LoggedIn Successfully..", Toast.LENGTH_LONG).show();
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
        httpGetAsyncTask.execute(user_id, offer_id);

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        /*if (resultCode == Activity.RESULT_OK) {

            if (requestCode == SELECT_FILE)

                onSelectFromGalleryResult(data);

            else if (requestCode == REQUEST_CAMERA)

                onCaptureImageResult(data);
        }*/
    }

    public void ShareDialog(Bitmap imagePath){

        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(imagePath)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        shareDialog.show(content);

    }


    // Initialize the facebook sdk and then callback manager will handle the login responses.

    protected void facebookSDKInitialize() {

        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }
}
