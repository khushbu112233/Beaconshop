package com.amplearch.beaconshop.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.amplearch.beaconshop.ConnectivityReceiver;
import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.Utils.GillSansButton;
import com.amplearch.beaconshop.Utils.GillSansTextView;
import com.amplearch.beaconshop.Utils.UserSessionManager;
import com.amplearch.beaconshop.helper.DatabaseHelper;
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
import java.util.Random;

/**
 * Created by ample-arch on 4/13/2017.
 */

public class ElectClaimOfferAcivity extends AppCompatActivity implements View.OnClickListener
{
    GillSansTextView tvItemOffers, tvItemOfferDetails, tvItemOfferCode ;
    CheckBox tvCheckAgree ;
    GillSansButton btnItemClaimOffer ;
    ImageView ivFacebook, ivFavorite, ivShare ;
    String offer_title, offer_desc, offer_id, quantity, offer_image;
    Bitmap offerImg;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    UserSessionManager session;
    String userID;
    CheckBox chAgree;
    Boolean isAgreeChecked = false;
    DatabaseHelper db;
    String offerCode;
    FrameLayout redeemid, frameOpacity;
    String category_id;
    ImageView imgOffer,imgLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        facebookSDKInitialize();
        setContentView(R.layout.claim_offers);

        checkConnection();

        db = new DatabaseHelper(getApplicationContext());

        tvItemOfferCode = (GillSansTextView) findViewById(R.id.tvItemOfferCode);
        redeemid = (FrameLayout) findViewById(R.id.redeemid);
        frameOpacity = (FrameLayout) findViewById(R.id.frameOpacity);

        shareDialog = new ShareDialog(this);
        Intent intent = getIntent();
        offer_title = intent.getStringExtra("offer_title");
        offer_desc = intent.getStringExtra("offer_desc");
        offer_id = intent.getStringExtra("offer_id");
        quantity = intent.getStringExtra("quantity");
        offer_image = intent.getStringExtra("offer_image");
        category_id = intent.getStringExtra("category_id");
       // offerImg = (Bitmap) intent.getParcelableExtra("BitmapImage");
        // Toast.makeText(getApplicationContext(), offer_image, Toast.LENGTH_LONG).show();
        session = new UserSessionManager(getApplicationContext());
        tvItemOffers = (GillSansTextView) findViewById(R.id.tvItemOffer);
        imgOffer = (ImageView)findViewById(R.id.imgOffer);
        imgLeft = (ImageView)findViewById(R.id.imgLeft);
        tvItemOfferDetails = (GillSansTextView) findViewById(R.id.tvItemOfferDetails);
        chAgree = (CheckBox) findViewById(R.id.chAgree);
        ivFavorite = (ImageView)findViewById(R.id.ivFavorite);
        ivShare = (ImageView)findViewById(R.id.ivShare);
        ivFacebook = (ImageView)findViewById(R.id.ivFacebook);
        btnItemClaimOffer = (GillSansButton) findViewById(R.id.btnItemClaimOffer);

        tvItemOffers.setText(offer_title);
        tvItemOfferDetails.setText(offer_desc);
        ivFacebook.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        ivFavorite.setOnClickListener(this);
        btnItemClaimOffer.setOnClickListener(this);

        try {
            byte[] decodedString = Base64.decode(offer_image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            imgOffer.setImageBitmap(decodedByte);
        } catch (Exception e){

        }
        final HashMap<String, String> user1 = session.getUserDetails();

        // get name
        userID = user1.get(UserSessionManager.KEY_USER_ID);
        if (checkConnection() == true)
        {
            getQuantityWithHttpPost(userID, offer_id);
        }

        Boolean exists = db.verification(offer_id);

      /*  if (exists) {

            ivFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart_blue));
        }
        else {
            ivFavorite.setImageDrawable(getResources().getDrawable(R.drawable.heart_grey));
        }*/
        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
            final String appPackageName = getPackageName();
            if (ShareDialog.canShow(ShareLinkContent.class)) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentTitle("BeaconShop")
                        .setImageUrl(Uri.parse("http://beacon.ample-arch.com/Images/ic_launcher.png"))
                        .setContentDescription(
                                "Hey Download this App before going to any Mall and Get best offer of mall in your Apps")
                        .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName))
                        .build();

                shareDialog.show(linkContent);  // Show facebook ShareDialog
            }
        }
        if (v == ivShare)
        {
            final String appPackageName = getPackageName();
            String shareBody = "Hey Download this App before going to any Mall and Get best offer of mall in your Apps  \n" +
                    "https://play.google.com/store/apps/details?id=" + appPackageName;
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "BeaconShop");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share Via"));
        }
        if (v == ivFavorite)
        {
/*            List<Favourites> allTags = db.getAllFavouritesRecords();
            for (Favourites tag : allTags) {
                Log.d("StoreLocation Name", tag.getProduct_id());
                Toast.makeText(getApplicationContext(), tag.getProduct_id() + " " + tag.getUser_id(), Toast.LENGTH_LONG).show();

            }*/


            Boolean exists = db.verification(offer_id);

            if (exists) {
                ivFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart_blue));

                Boolean del = db.deleteFavourites(offer_id);
                if (del) {
                    Toast.makeText(getApplicationContext(), "Removed from favourites", Toast.LENGTH_LONG).show();
                    ivFavorite.setImageDrawable(getResources().getDrawable(R.drawable.heart_grey));
                }
            }
            else {
                ivFavorite.setImageDrawable(getResources().getDrawable(R.drawable.heart_grey));
                db.addFavourites(offer_id, userID);

                Toast.makeText(getApplicationContext(),"Added to favourites.",Toast.LENGTH_LONG).show();
                ivFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart_blue));

            }



//            Toast.makeText(getApplicationContext(),"Added to Favorite.",Toast.LENGTH_LONG).show();
            // Inserting tags in db
//            long tag1_id = db.createFavorites(tag1);
        }
        if (v == btnItemClaimOffer)
        {
            // Toast.makeText(getApplicationContext(),"You are not ELIGIBLE to claim this offer.",Toast.LENGTH_LONG).show();
            if (isAgreeChecked)
            {
                if (checkConnection() == true )
                {
                    offerCode = tvItemOfferCode.getText().toString();
                    connectWithHttpPost(userID, offer_id, quantity, "1", offer_image, offer_title, offer_desc, offerCode);
                }
            }
            else {
                Toast.makeText(getApplicationContext(), "Please agree terms & conditions before claim offer..", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void connectWithHttpPost(final String user_id, final String offer_id, final String quantity, final String redeem, final String offer_image, final String offer_title, final String offer_desc, final String offer_code)
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
                String paramOfferCode = params[7];

                //    System.out.println("paramUsername is :" + paramUsername + " paramPassword is :" + paramPassword);

                // Create an intermediate to connect with the Internet
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://beacon.ample-arch.com/BeaconWebService.asmx/addRedeemUser");
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

                //Post Data
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(4);
                nameValuePair.add(new BasicNameValuePair("user_id", paramUserID));
                nameValuePair.add(new BasicNameValuePair("offer_id", paramOfferID));
                nameValuePair.add(new BasicNameValuePair("quantity", paramquantity));
                nameValuePair.add(new BasicNameValuePair("redeem", paramredeem));
                nameValuePair.add(new BasicNameValuePair("offer_image", paramOfferImage));
                nameValuePair.add(new BasicNameValuePair("offer_title", paramOfferTitle));
                nameValuePair.add(new BasicNameValuePair("offer_desc", paramOfferDesc));
                nameValuePair.add(new BasicNameValuePair("offer_code", paramOfferCode));


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
                    Toast.makeText(getApplicationContext(), "Server connection failed..", Toast.LENGTH_LONG).show();
                }else {
                    // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String res = jsonObject.getString("message");
                        // String message = jsonObject.getString("User");
                        //  Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                        if (res.equals("Success")){

                            Toast.makeText(getApplicationContext(), "Successfully claimed..", Toast.LENGTH_LONG).show();

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
        httpGetAsyncTask.execute(user_id, offer_id, quantity, redeem, offer_image, offer_title, offer_desc, offer_code);

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
                HttpPost httpPost = new HttpPost("http://beacon.ample-arch.com/BeaconWebService.asmx/CheckRedeemUser");
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
                        String res = jsonObject.getString("message");
                        // String message = jsonObject.getString("User");
                        //  Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                        if (res.equalsIgnoreCase("not exists")){
                            // Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                            redeemid.setVisibility(View.GONE);
                            frameOpacity.setVisibility(View.GONE);

                            final String ALLOWED_CHARACTERS ="0123456789QWERTYUIOPASDFGHJKLZXCVBNM";

                            final Random random=new Random();
                            final StringBuilder sb=new StringBuilder(6);
                            for(int i=0;i<6;++i)
                                sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
                            //return sb.toString();
//                            tvItemOfferCode.setText(sb.toString());
                        }
                        else if (res.equalsIgnoreCase("exists")){
                            redeemid.setVisibility(View.VISIBLE);
                            frameOpacity.setVisibility(View.VISIBLE);

                            String code = jsonObject.getString("code");
                            tvItemOfferCode.setText(code);
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
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
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
