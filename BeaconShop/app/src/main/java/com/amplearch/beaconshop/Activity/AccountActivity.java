package com.amplearch.beaconshop.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amplearch.beaconshop.ConnectivityReceiver;
import com.amplearch.beaconshop.Fragment.ProfileFragment;
import com.amplearch.beaconshop.Model.Images;
import com.amplearch.beaconshop.Model.User;
import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.StoreLocations;
import com.amplearch.beaconshop.Utils.NearbyMessagePref;
import com.amplearch.beaconshop.Utils.PrefUtils;
import com.amplearch.beaconshop.Utils.UserSessionManager;
import com.amplearch.beaconshop.WebCall.AsyncRequest;
import com.amplearch.beaconshop.helper.DatabaseHelper;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener, AsyncRequest.OnAsyncRequestComplete {
    TextView txtSignIn, txtSignUp;

  //  private static final String TAG = MainActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;

    private SignInButton btnSignIn;
    UserSessionManager session;
    NearbyMessagePref pref;

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private TextView btnLogin;
    private ProgressDialog progressDialog;
    User user;

    public String SERVER = "http://beacon.ample-arch.com/BeaconWebService.asmx/RegisterGoogleUser",
            timestamp;

    public String SERVER2 = "http://beacon.ample-arch.com/BeaconWebService.asmx/RegisterFacebookUser",
            timestamp1;

    String apiURL = "http://beacon.ample-arch.com/BeaconWebService.asmx/RegisterGoogleUser" ;
    ArrayList<NameValuePair> params ;

    String apiURLFacebook = "http://beacon.ample-arch.com/BeaconWebService.asmx/RegisterFacebookUser" ;
    ArrayList<NameValuePair> paramsFacebook ;

    protected static final String TAG = "location-updates-sample";

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // Keys for storing activity state in the Bundle.
    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    protected final static String LOCATION_KEY = "location-key";
    protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";

    protected LocationRequest mLocationRequest;

    /**
     * Represents a geographical location.
     */
    protected Location mCurrentLocation;

    protected String mLatitudeLabel;
    protected String mLongitudeLabel;
    protected String mLastUpdateTimeLabel;

    protected Boolean mRequestingLocationUpdates;
    Boolean isConnected = false;

    /**
     * Time when the location was updated represented as a String.
     */
    protected String mLastUpdateTime;
    public static Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_account);

        isConnected = checkConnection();

        session = new UserSessionManager(getApplicationContext());
        pref = new NearbyMessagePref(getApplicationContext());
        final HashMap<String, String> nearpref = pref.getUserDetails();

        String near = nearpref.get(NearbyMessagePref.KEY_OFFER_NOTI);
        try {
            if (near.isEmpty()){
                pref.createUserLoginSession("false");
            }
        }catch (Exception e){
            pref.createUserLoginSession("false");
        }

        txtSignIn = (TextView)findViewById(R.id.btnSignIn);
        txtSignUp = (TextView)findViewById(R.id.btnSignUp);
        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            }
        });

        String URL = "content://com.amplearch.beaconshop.StoreLocations";

        Uri students = Uri.parse(URL);
        c = managedQuery(students, null, null, null, null);

       /* if (c.moveToFirst()) {
            do{
                Toast.makeText(this,
                        c.getString(c.getColumnIndex(StoreLocations._ID)) +
                                ", " +  c.getString(c.getColumnIndex( StoreLocations.FIELD_OFFER_TITLE)) +
                                ", " + c.getString(c.getColumnIndex( StoreLocations.FIELD_STORE_NAME)) +
                                ", " + c.getString(c.getColumnIndex( StoreLocations.FIELD_START_DATE)),
                        Toast.LENGTH_SHORT).show();
            } while (c.moveToNext());
        }*/

        if(PrefUtils.getCurrentUser(AccountActivity.this) != null){

            Intent homeIntent = new Intent(AccountActivity.this, MainActivity.class);

            startActivity(homeIntent);

            finish();
        }

       /* Toast.makeText(getApplicationContext(),
                "User Login Status: " + session.isUserLoggedIn(),
                Toast.LENGTH_LONG).show();*/

        if (session.isUserLoggedIn()){
            Intent intent=new Intent(AccountActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

        mLatitudeLabel = getResources().getString(R.string.latitude_label);
        mLongitudeLabel = getResources().getString(R.string.longitude_label);
        mLastUpdateTimeLabel = getResources().getString(R.string.last_update_time_label);

        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);

        // Kick off the process of building a GoogleApiClient and requesting the LocationServices
        // API.
      //  buildGoogleApiClient();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        createLocationRequest();

        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
           // setButtonsEnabledState();
          //  startLocationUpdates();
        }
        // Customizing G+ button
        btnSignIn.setSize(SignInButton.SIZE_STANDARD);
        btnSignIn.setScopes(gso.getScopeArray());

        if (isConnected) {
            btnSignIn.setOnClickListener(this);
        }
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            }
        });
    }

    protected void startLocationUpdates() {
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        Log.i(TAG, "Updating values from bundle");
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        REQUESTING_LOCATION_UPDATES_KEY);
             //   setButtonsEnabledState();
            }

            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                // Since LOCATION_KEY was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }

            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)) {
                mLastUpdateTime = savedInstanceState.getString(LAST_UPDATED_TIME_STRING_KEY);
            }
          //  updateUI();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient.isConnected()) {
           // stopLocationUpdates();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }

        callbackManager=CallbackManager.Factory.create();

        loginButton= (LoginButton)findViewById(R.id.login_button);

        loginButton.setReadPermissions("public_profile", "email","user_friends", "user_location");

        btnLogin= (TextView) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isConnected) {
                    progressDialog = new ProgressDialog(AccountActivity.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();

                    loginButton.performClick();

                    loginButton.setPressed(true);

                    loginButton.invalidate();

                    loginButton.registerCallback(callbackManager, mCallBack);

                    loginButton.setPressed(false);

                    loginButton.invalidate();
                }
            }
        });
    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    String personName= "";
    String personPhotoUrl = "";
    String email = "";
    String googleID = "";

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

            googleID = acct.getId().toString();
            personName = acct.getDisplayName();
            personPhotoUrl = acct.getPhotoUrl().toString();
            email = acct.getEmail();

            Log.e(TAG, "Name: " + personName + ", email: " + email
                    + ", Image: " + personPhotoUrl);
            new Upload("IMG_" + timestamp).execute();
           /* try {
                URL url = new URL(personPhotoUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] array = stream.toByteArray();

                String imgString = Base64.encodeToString(getBytesFromBitmap(myBitmap),
                        Base64.NO_WRAP);


                ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
                nameValuePair.add(new BasicNameValuePair("name", acct.getDisplayName().toString()));
                nameValuePair.add(new BasicNameValuePair("email", acct.getEmail().toString()));
                nameValuePair.add(new BasicNameValuePair("contact", ""));
                nameValuePair.add(new BasicNameValuePair("pass", "gmailpass"));
                nameValuePair.add(new BasicNameValuePair("type", "gmail"));
                nameValuePair.add(new BasicNameValuePair("google_id", acct.getId().toString()));
                nameValuePair.add(new BasicNameValuePair("image", imgString));

                params = nameValuePair;
                AsyncRequest getPosts = new AsyncRequest(AccountActivity.this, "GET", params);
                getPosts.execute(apiURL);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            //    session.createUserLoginSession(personName, email, personPhotoUrl, "gpass", "2977");


            // txtName.setText(personName);
           // txtEmail.setText(email);
           /* Glide.with(getApplicationContext()).load(personPhotoUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfilePic);
*/
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    private String hashMapToUrl(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }


    private class UploadFacebook extends AsyncTask<Void,Void,String>{
        private Bitmap image;
        private String name1;

        public UploadFacebook(String name1){
            this.name1 = name1;
        }

        @Override
        protected String doInBackground(Void... params) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            //compress the image to jpg format
            //  image.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            /*
            * encode image to base64 so that it can be picked by saveImage.php file
            * */


            personPhotoUrl = "https://graph.facebook.com/" + user.facebookID + "/picture?type=large";

            try {
                URL url = new URL("https://graph.facebook.com/" + user.facebookID + "/picture?type=large");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                image = BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String encodeImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT);

            // Bitmap bm = BitmapFactory.decodeFile("/path/to/image.jpg");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object

            String imgString = Base64.encodeToString(getBytesFromBitmap(image),
                    Base64.NO_WRAP);

            byte[] b = baos.toByteArray();
            //generate hashMap to store encodedImage and the name
            HashMap<String,String> detail = new HashMap<>();
            detail.put("name", user.name.toString());
            detail.put("email", user.email.toString());
            detail.put("contact", "");
            detail.put("pass", "fbpass");
            detail.put("type", "facebook");
            detail.put("facebook_id", user.facebookID);
            detail.put("image", imgString);
            Long tsLong = System.currentTimeMillis() / 1000;
            timestamp1 = tsLong.toString();
            try{
                //convert this HashMap to encodedUrl to send to php file
                String dataToSend = hashMapToUrl(detail);
                //make a Http request and send data to saveImage.php file
                String response = com.amplearch.beaconshop.WebCall.Request.post(SERVER2,dataToSend);

                //return the response
                return response;

            }catch (Exception e){
                e.printStackTrace();
                Log.e(TAG,"ERROR  "+e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            //show image uploaded


            try
            {
                JSONObject jsonObject = new JSONObject(s);
                String res = jsonObject.getString("message");
                String userId = jsonObject.getString("userId");
                // String message = jsonObject.getString("User");
                //  Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                if (res.equals("Success Facebook") || res.equalsIgnoreCase("Already Exists"))
                {
                    session.createUserLoginSession(user.name, user.email, personPhotoUrl, "fbpass", userId);
                    Intent intent=new Intent(AccountActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
//                            session.createUserLoginSession(username, email_id, image, password, user_id);
                    //etEmailAddress.setError("Email Address, Already Exist!");
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }




           /* JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(s);
                String res = jsonObject.getString("message");
                if (res.equalsIgnoreCase("Success")){
                    Toast.makeText(getApplicationContext(),"Data Uploaded Successfully..",Toast.LENGTH_SHORT).show();
                    // session.createUserLoginSession(name, email, "", password, userID);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Data not Uploaded..",Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }*/

        }
    }


    private class Upload extends AsyncTask<Void,Void,String>{
        private Bitmap image;
        private String name1;

        public Upload(String name1){
            this.name1 = name1;
        }

        @Override
        protected String doInBackground(Void... params) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            //compress the image to jpg format
            //  image.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            /*
            * encode image to base64 so that it can be picked by saveImage.php file
            * */


            try {
                URL url = new URL(personPhotoUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                image = BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String encodeImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT);

            // Bitmap bm = BitmapFactory.decodeFile("/path/to/image.jpg");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object

            String imgString = Base64.encodeToString(getBytesFromBitmap(image),
                    Base64.NO_WRAP);

            byte[] b = baos.toByteArray();
            //generate hashMap to store encodedImage and the name
            HashMap<String,String> detail = new HashMap<>();
            detail.put("name", personName);
            detail.put("email", email);
            detail.put("contact", "");
            detail.put("pass", "gmailpass");
            detail.put("type", "gmail");
            detail.put("google_id", googleID);
            detail.put("image", imgString);
            Long tsLong = System.currentTimeMillis() / 1000;
            timestamp = tsLong.toString();
            try{
                //convert this HashMap to encodedUrl to send to php file
                String dataToSend = hashMapToUrl(detail);
                //make a Http request and send data to saveImage.php file
                String response = com.amplearch.beaconshop.WebCall.Request.post(SERVER,dataToSend);

                //return the response
                return response;

            }catch (Exception e){
                e.printStackTrace();
                Log.e(TAG,"ERROR  "+e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            //show image uploaded


            if (s.equals(""))
            {
                Toast.makeText(getApplicationContext(), "Server Connection Failed..", Toast.LENGTH_LONG).show();
            }
            else {
                // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String res = jsonObject.getString("message");
                    String userId = jsonObject.getString("userId");
                    // String message = jsonObject.getString("User");
                    //  Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                    if (res.equals("Success Google") || res.equalsIgnoreCase("Already Exists")) {
                        session.createUserLoginSession(personName, email, personPhotoUrl, "gmailpass", userId);
                        updateUI(true);
                    } else {
                        updateUI(false);
                        Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
//                            session.createUserLoginSession(username, email_id, image, password, user_id);
                        //etEmailAddress.setError("Email Address, Already Exist!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }




           /* JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(s);
                String res = jsonObject.getString("message");
                if (res.equalsIgnoreCase("Success")){
                    Toast.makeText(getApplicationContext(),"Data Uploaded Successfully..",Toast.LENGTH_SHORT).show();
                    // session.createUserLoginSession(name, email, "", password, userID);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Data not Uploaded..",Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }*/

        }
    }

    public class MyAsync extends AsyncTask<Void, Void, Bitmap>
    {
        @Override
        protected Bitmap doInBackground(Void... params)
        {
            // for converting url to bitmap
            try {
                URL url = new URL(personPhotoUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap)
        {
            super.onPostExecute(bitmap);

//            ivBitmapImage.setImageBitmap(bitmap);

            // for convert bitmap to byte array
//            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            String imgString = Base64.encodeToString(getBytesFromBitmap(bitmap),
                    Base64.NO_WRAP);


            ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            nameValuePair.add(new BasicNameValuePair("name", personName));
            nameValuePair.add(new BasicNameValuePair("email", email));
            nameValuePair.add(new BasicNameValuePair("contact", ""));
            nameValuePair.add(new BasicNameValuePair("pass", "gmailpass"));
            nameValuePair.add(new BasicNameValuePair("type", "gmail"));
            nameValuePair.add(new BasicNameValuePair("google_id", googleID));
            nameValuePair.add(new BasicNameValuePair("image", imgString));

            params = nameValuePair;
            AsyncRequest getPosts = new AsyncRequest(AccountActivity.this, "GET", params);
            getPosts.execute(apiURL);

        }
    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_sign_in:
                signIn();
                break;

            /*case R.id.btn_sign_out:
                signOut();
                break;

            case R.id.btn_revoke_access:
                revokeAccess();
                break;*/
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            progressDialog.dismiss();

            // App code
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {

                            Log.e("response: ", response + "");
                           // Toast.makeText(AccountActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                            try {
                                user = new User();
                                user.facebookID = object.getString("id").toString();
                                user.email = object.getString("email").toString();
                                user.name = object.getString("name").toString();
                                user.gender = object.getString("gender").toString();
                                PrefUtils.setCurrentUser(user,AccountActivity.this);
                                new UploadFacebook("IMG_" + timestamp1).execute();
                               /* JSONObject object1 = object.getJSONObject("location");
                                Toast.makeText(getApplicationContext(), object1.toString(), Toast.LENGTH_LONG).show();
*/
                              //  session.createUserLoginSession(user.name, user.email, "", "", "");

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                          //  Toast.makeText(AccountActivity.this,"welcome "+user.name,Toast.LENGTH_LONG).show();

                        }

                    });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            progressDialog.dismiss();
        }

        @Override
        public void onError(FacebookException e) {
            progressDialog.dismiss();
        }
    };


    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");

        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in the Bundle and check for it in onCreate(). We
        // do not request it again unless the user specifically requests location updates by pressing
        // the Start Updates button.
        //
        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
           // updateUI();
        }

        // If the user presses the Start Updates button before GoogleApiClient connects, we set
        // mRequestingLocationUpdates to true (see startUpdatesButtonHandler()). Here, we check
        // the value of mRequestingLocationUpdates and if it is true, we start location updates.
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
       // updateUI();
       /* Toast.makeText(this, getResources().getString(R.string.location_updated_message),
                Toast.LENGTH_SHORT).show();*/
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    /*@Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }*/


    /**
     * Stores activity data in the Bundle.
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
        savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(AccountActivity.this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            btnSignIn.setVisibility(View.GONE);

            // Starting MainActivity
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);

            finish();
           // Intent intent = new Intent(getApplicationContext(), MainActivity.class);
          //  startActivity(intent);
           // btnSignOut.setVisibility(View.VISIBLE);
           // btnRevokeAccess.setVisibility(View.VISIBLE);
          //  llProfileLayout.setVisibility(View.VISIBLE);
        } else {
            btnSignIn.setVisibility(View.VISIBLE);
          //  btnSignOut.setVisibility(View.GONE);
          //  btnRevokeAccess.setVisibility(View.GONE);
          //  llProfileLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application?");
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
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
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void asyncResponse(String response) {

      //  Toast.makeText(getApplicationContext(), "Response "+response, Toast.LENGTH_LONG).show();
        Log.i("SignUp Response ", response);

        if (response.equals(""))
        {
            Toast.makeText(getApplicationContext(), "Server Connection Failed..", Toast.LENGTH_LONG).show();
        }
        else
        {
           /* // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try
            {
                JSONObject jsonObject = new JSONObject(response);
                String res = jsonObject.getString("message");
                String userId = jsonObject.getString("userId");
                // String message = jsonObject.getString("User");
                //  Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                if (res.equals("Success Google") || res.equalsIgnoreCase("Already Exists"))
                {
                    session.createUserLoginSession(personName, email, personPhotoUrl, "gmailpass", userId);
                    updateUI(true);
                }
                else
                {
                    updateUI(false);
                    Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
//                            session.createUserLoginSession(username, email_id, image, password, user_id);
                    //etEmailAddress.setError("Email Address, Already Exist!");
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }*/

        }


    }
}
