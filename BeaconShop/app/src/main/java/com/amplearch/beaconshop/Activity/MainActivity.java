package com.amplearch.beaconshop.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amplearch.beaconshop.Adapter.CustomAdapter;
import com.amplearch.beaconshop.Adapter.DrawerAdapter;
import com.amplearch.beaconshop.ConnectivityReceiver;
import com.amplearch.beaconshop.Fragment.AboutUsFragment;
import com.amplearch.beaconshop.Fragment.AccountFragment;
import com.amplearch.beaconshop.Fragment.BadgesFragment;
import com.amplearch.beaconshop.Fragment.FavoriteFragment;
import com.amplearch.beaconshop.Fragment.HelpFragment;
import com.amplearch.beaconshop.Fragment.HomeFragment;
import com.amplearch.beaconshop.Fragment.VoucherFragment;
import com.amplearch.beaconshop.Model.ItemObject;
import com.amplearch.beaconshop.Model.StoreLocation;
import com.amplearch.beaconshop.Model.Voucher;
import com.amplearch.beaconshop.Model.VoucherClass;
import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.Utils.Const;
import com.amplearch.beaconshop.Utils.GillSansTextView;
import com.amplearch.beaconshop.Utils.LocationUpdateService;
import com.amplearch.beaconshop.Utils.NearbyMessagePref;
import com.amplearch.beaconshop.Utils.PrefUtils;
import com.amplearch.beaconshop.Utils.UserSessionManager;
import com.amplearch.beaconshop.Utils.cgTextView;
import com.amplearch.beaconshop.WebCall.AsyncRequest;
import com.amplearch.beaconshop.helper.DatabaseHelper;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements AsyncRequest.OnAsyncRequestComplete,  GoogleApiClient.OnConnectionFailedListener
{
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    String[] titles ;
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar topToolBar;
    private cgTextView toolbarTitle ;
    ImageView imgRightHeader;
    private static final int RC_SIGN_IN = 007;

    private boolean mIsServiceStarted = false;
    public static final String EXTRA_NOTIFICATION_ID = "notification_id";
    public static final String ACTION_STOP = "STOP_ACTION";
    public static final String ACTION_FROM_NOTIFICATION = "isFromNotification";
    private String action;
    private int notifID;
    byte[] byteStoreImage ;

    ArrayList<Integer> drawerImage = new ArrayList<Integer>();
    ArrayList<String> drawerText = new ArrayList<String>();
    DrawerAdapter drawerAdapter ;

    RelativeLayout rlButtons ;
    FrameLayout flBackImage ;
    boolean isConnected = false;
    String apiURL = "http://beacon.ample-arch.com/BeaconWebService.asmx/getVoucherList";
    ArrayList<NameValuePair> params;
    List<VoucherClass> offers;
    DatabaseHelper db;
    CallbackManager callbackManager;
    String userID, name,email;
    CircleImageView circleImageView;
    UserSessionManager session;
    NearbyMessagePref pref;
    LinearLayout llAccount,llFavourites,llVouchers,llHome;
    public static GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    ImageView imgHome,imgVoucher,imgFavourites,imgAccount, imgLogo;
    GillSansTextView txtHome,txtVoucher,txtFavourite,txtAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        facebookSDKInitialize();
        setContentView(R.layout.activity_main);
        session = new UserSessionManager(getApplicationContext());
        isConnected = checkConnection();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

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

        offers = new ArrayList<VoucherClass>();
        db = new DatabaseHelper(getApplicationContext());
        if (isConnected){

            params = getParams();
            AsyncRequest getPosts = new AsyncRequest(MainActivity.this, "GET", params);
            getPosts.execute(apiURL);

        }

        // rlButtons = (RelativeLayout)findViewById(R.id.rlButtons);

        if (!mIsServiceStarted) {
            mIsServiceStarted = true;
            // setButtonsEnabledState();
            // OnGoingLocationNotification(this);
            startService(new Intent(this, LocationUpdateService.class));
        }

//        mTitle = mDrawerTitle = getTitle();
//        titles = getResources().getStringArray(R.array.navigation_drawer_items_array);

        toolbarTitle =(cgTextView)findViewById(R.id.toolbarTitle);
        imgRightHeader = (ImageView)findViewById(R.id.imgRightHeader);
        topToolBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);
        //topToolBar.setLogo(R.mipmap.ic_launcher);
        imgHome = (ImageView)findViewById(R.id.imgHome);
        imgVoucher = (ImageView)findViewById(R.id.imgVoucher);
        imgFavourites = (ImageView)findViewById(R.id.imgFavourites);
        imgAccount = (ImageView)findViewById(R.id.imgAccount);
        txtAccount = (GillSansTextView)findViewById(R.id.txtAccount);
        txtFavourite = (GillSansTextView)findViewById(R.id.txtFavourite);
        txtVoucher = (GillSansTextView)findViewById(R.id.txtVoucher);
        txtHome = (GillSansTextView)findViewById(R.id.txtHome);
        llAccount = (LinearLayout)findViewById(R.id.llAccount);
        llFavourites = (LinearLayout)findViewById(R.id.llFavourites);
        llVouchers = (LinearLayout)findViewById(R.id.llVouchers);
        llHome = (LinearLayout)findViewById(R.id.llHome);
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        LayoutInflater inflater = getLayoutInflater();

        View listHeaderView = inflater.inflate(R.layout.header_list,null, false);

        final HashMap<String, String> user1 = session.getUserDetails();

        userID = user1.get(UserSessionManager.KEY_USER_ID);
        name = user1.get(UserSessionManager.KEY_NAME);
        email =user1.get(UserSessionManager.KEY_EMAIL);

        circleImageView = (CircleImageView) listHeaderView.findViewById(R.id.circleView);
        GillSansTextView profile_name = (GillSansTextView) listHeaderView.findViewById(R.id.profile_name);
        GillSansTextView profile_email = (GillSansTextView)listHeaderView.findViewById(R.id.profile_email);
        profile_name.setText(name);
        profile_email.setText(email);

        mDrawerList.addHeaderView(listHeaderView);

        if (checkConnection() == true)
        {
            connectWithHttpPost(userID);
            connectWithHttpPostUserRedeem(userID);
        }

        // adding values in left drawer
//        drawerImage.add(R.drawable.ic_home_black_24dp);         // for home
//        drawerText.add("Home");
//        drawerImage.add(R.drawable.ic_favorite_black_24dp);     // for home
//        drawerText.add("Favourites");
//        drawerImage.add(R.drawable.ic_email_black_24dp);       // for home
//        drawerText.add("My Vouchers");
//        drawerImage.add(R.drawable.ic_help_outline_black_24dp);     // for home
//        drawerText.add("Badges");
//        drawerImage.add(R.drawable.ic_person_black_24dp);     // for home
//        drawerText.add("My Account");
//        drawerImage.add(R.drawable.ic_settings_black_24dp);     // for home
//        drawerText.add("Settings");
//        drawerImage.add(R.drawable.ic_help_outline_black_24dp);     // for home
//        drawerText.add("Help");
//        drawerImage.add(R.drawable.ic_info_outline_black_24dp);     // for home
//        drawerText.add("About Us");
//
//        drawerAdapter = new DrawerAdapter(getApplicationContext(),drawerImage,drawerText);
//
//        mDrawerList.setAdapter(drawerAdapter);

        List<ItemObject> listViewItems = new ArrayList<ItemObject>();
        listViewItems.add(new ItemObject("Home", R.drawable.ic_home));
        // listViewItems.add(new ItemObject("Favourites", R.drawable.my_favorites));
        //listViewItems.add(new ItemObject("My Vouchers", R.drawable.my_vouchers));
        listViewItems.add(new ItemObject("Badges", R.drawable.ic_badges));
        listViewItems.add(new ItemObject("My account", R.drawable.ic_my_account));
        listViewItems.add(new ItemObject("Help", R.drawable.ic_help));
        // listViewItems.add(new ItemObject("Settings", R.drawable.ic_settings_black_24dp));
        // listViewItems.add(new ItemObject("Change Password", R.drawable.ic_change));
        listViewItems.add(new ItemObject("About us", R.drawable.ic_inform));
        listViewItems.add(new ItemObject("Sign out", R.drawable.icon_logout));

        mDrawerList.setAdapter(new CustomAdapter(this, listViewItems));

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close)
        {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
      //  mDrawerToggle.setDrawerIndicatorEnabled(true);
//        mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_logo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(null);

        imgHome.setColorFilter(getResources().getColor(R.color.logo_color));
        imgVoucher.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.divider), android.graphics.PorterDuff.Mode.MULTIPLY);
        imgFavourites.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.divider), android.graphics.PorterDuff.Mode.MULTIPLY);
        imgAccount.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.divider), android.graphics.PorterDuff.Mode.MULTIPLY);

        txtHome.setTextColor(getResources().getColor(R.color.logo_color));
        txtVoucher.setTextColor(getResources().getColor(R.color.divider));
        txtFavourite.setTextColor(getResources().getColor(R.color.divider));
        txtAccount.setTextColor(getResources().getColor(R.color.divider));
        imgRightHeader.setVisibility(View.GONE);
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.content_frame, new HomeFragment());
        tx.addToBackStack(null);
        tx.commit();

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
//                Toast.makeText(getApplicationContext(), "Position " + position, Toast.LENGTH_LONG).show();
                selectItemFragment(position);
            }
        });

        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // callMainPage("LeftMenu");
                mDrawerLayout.openDrawer(Gravity.START);
                /*boolean result = Utility.checkWriteContactPermission(DashboardActivity.this);
                if (result) {
                    contactAdd();
                }*/
            }
        });


        llHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgHome.setColorFilter(getResources().getColor(R.color.logo_color));
                imgVoucher.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.divider), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgFavourites.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.divider), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgAccount.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.divider), android.graphics.PorterDuff.Mode.MULTIPLY);

                txtHome.setTextColor(getResources().getColor(R.color.logo_color));
                txtVoucher.setTextColor(getResources().getColor(R.color.divider));
                txtFavourite.setTextColor(getResources().getColor(R.color.divider));
                txtAccount.setTextColor(getResources().getColor(R.color.divider));
                imgRightHeader.setVisibility(View.GONE);
                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.content_frame, new HomeFragment());
                tx.addToBackStack(null);
                tx.commit();
            }
        });
        llFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgHome.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.divider), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgVoucher.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.divider), android.graphics.PorterDuff.Mode.MULTIPLY);
                //imgFavourites.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.logo_color), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgAccount.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.divider), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgFavourites.setColorFilter(getResources().getColor(R.color.logo_color));
                txtHome.setTextColor(getResources().getColor(R.color.divider));
                txtVoucher.setTextColor(getResources().getColor(R.color.divider));
                txtFavourite.setTextColor(getResources().getColor(R.color.logo_color));
                txtAccount.setTextColor(getResources().getColor(R.color.divider));
                imgRightHeader.setVisibility(View.GONE);
                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.content_frame, new FavoriteFragment());
                tx.addToBackStack(null);
                tx.commit();
            }
        });
        llVouchers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgHome.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.divider), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgVoucher.setColorFilter(getResources().getColor(R.color.logo_color));
                imgFavourites.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.divider), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgAccount.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.divider), android.graphics.PorterDuff.Mode.MULTIPLY);

                txtHome.setTextColor(getResources().getColor(R.color.divider));
                txtVoucher.setTextColor(getResources().getColor(R.color.logo_color));
                txtFavourite.setTextColor(getResources().getColor(R.color.divider));
                txtAccount.setTextColor(getResources().getColor(R.color.divider));
                imgRightHeader.setVisibility(View.GONE);
                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.content_frame, new VoucherFragment());
                tx.addToBackStack(null);
                tx.commit();
            }
        });
        llAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgHome.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.divider), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgVoucher.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.divider), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgFavourites.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.divider), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgAccount.setColorFilter(getResources().getColor(R.color.logo_color));

                txtHome.setTextColor(getResources().getColor(R.color.divider));
                txtVoucher.setTextColor(getResources().getColor(R.color.divider));
                txtFavourite.setTextColor(getResources().getColor(R.color.divider));
                txtAccount.setTextColor(getResources().getColor(R.color.logo_color));
                imgRightHeader.setVisibility(View.VISIBLE);
                FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                tx.replace(R.id.content_frame, new AccountFragment());
                tx.addToBackStack(null);
                tx.commit();
            }
        });
        imgRightHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                session.logoutUser();
                            }
                        });
                PrefUtils.clearCurrentUser(MainActivity.this);

                // We can logout from facebook by calling following method
                LoginManager.getInstance().logOut();
                session.logoutUser();
            }
        });
    }

    private void connectWithHttpPostUserRedeem(final String user_id)
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
                //    System.out.println("paramUsername is :" + paramUsername + " paramPassword is :" + paramPassword);

                // Create an intermediate to connect with the Internet
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://beacon.ample-arch.com/BeaconWebService.asmx/getRedeemUserbyUserID");
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

                //Post Data
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
                nameValuePair.add(new BasicNameValuePair("user_id", paramUserID));

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
                    Toast.makeText(getApplicationContext(), "Check for data connection..", Toast.LENGTH_LONG).show();
                }else {
                    //   Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String res = jsonObject.getString("redeem");
                        // String message = jsonObject.getString("User");
                        //  Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                        if (res.equals("")){
                            // Toast.makeText(getApplicationContext(), "User does not exists..", Toast.LENGTH_LONG).show();
                        }
                        else {

                            JSONArray jsonArrayChanged = jsonObject.getJSONArray("redeem");
                            for (int i = 0, count = jsonArrayChanged.length(); i < count; i++) {
                                try {
                                    //JSONObject jObject = jsonArrayChanged.getJSONObject(i);
                                    userID = jsonArrayChanged.getJSONObject(i).get("id").toString();
                                    jsonArrayChanged.getJSONObject(i).get("user_id").toString();
                                    jsonArrayChanged.getJSONObject(i).get("offer_id").toString();
                                    jsonArrayChanged.getJSONObject(i).get("offer_code").toString();
                                    jsonArrayChanged.getJSONObject(i).get("offer_image").toString();
                                    jsonArrayChanged.getJSONObject(i).get("offer_title").toString();
                                    jsonArrayChanged.getJSONObject(i).get("offer_desc").toString();
                                    jsonArrayChanged.getJSONObject(i).get("quantity").toString();
                                    jsonArrayChanged.getJSONObject(i).get("redeem").toString();

                                    byte[] decodedString = new byte[0];
                                    try {
                                        decodedString = Base64.decode(jsonArrayChanged.getJSONObject(i).get("offer_image").toString(), Base64.DEFAULT);
                                    }catch (Exception e){}
                                    Log.d("imageurl", decodedString.toString());

                                    db.addRedeemUser(jsonArrayChanged.getJSONObject(i).get("user_id").toString(), jsonArrayChanged.getJSONObject(i).get("offer_id").toString(),
                                            jsonArrayChanged.getJSONObject(i).get("offer_code").toString(), decodedString, jsonArrayChanged.getJSONObject(i).get("offer_title").toString(),
                                            jsonArrayChanged.getJSONObject(i).get("offer_desc").toString(), jsonArrayChanged.getJSONObject(i).get("quantity").toString(),
                                            jsonArrayChanged.getJSONObject(i).get("redeem").toString());

                                    //  txtUserID.setText(userID);
                                    //   txtName.setText(jsonArrayChanged.getJSONObject(i).get("username").toString());
                                    //   Toast.makeText(getContext(),jsonArrayChanged.getJSONObject(i).get("category_id").toString(), Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

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
        httpGetAsyncTask.execute(user_id);

    }

    private void connectWithHttpPost(final String user_id)
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
                //    System.out.println("paramUsername is :" + paramUsername + " paramPassword is :" + paramPassword);

                // Create an intermediate to connect with the Internet
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://beacon.ample-arch.com/BeaconWebService.asmx/getUserbyUserID");
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

                //Post Data
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(4);
                nameValuePair.add(new BasicNameValuePair("id", paramUserID));

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
                    Toast.makeText(getApplicationContext(), "Check for data connection..", Toast.LENGTH_LONG).show();
                }else {
                    //   Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String res = jsonObject.getString("user");
                        // String message = jsonObject.getString("User");
                        //  Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                        if (res.equals("")){
                            // Toast.makeText(getApplicationContext(), "User does not exists..", Toast.LENGTH_LONG).show();
                        }
                        else {

                            JSONArray jsonArrayChanged = jsonObject.getJSONArray("user");
                            for (int i = 0, count = jsonArrayChanged.length(); i < count; i++) {
                                try {
                                    //JSONObject jObject = jsonArrayChanged.getJSONObject(i);
                                    userID = jsonArrayChanged.getJSONObject(i).get("id").toString();
                                    // email =  jsonArrayChanged.getJSONObject(i).get("email_id").toString();
                                    //  voucherClass.setStore_name(jsonArrayChanged.getJSONObject(i).get("contact").toString());
                                    name = jsonArrayChanged.getJSONObject(i).get("username").toString();
                                    //  voucherClass.setOffer_title(jsonArrayChanged.getJSONObject(i).get("password").toString());
                                    byte[] image = jsonArrayChanged.getJSONObject(i).get("image").toString().getBytes();
                                    // Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);

                                    byte[] decodedString = Base64.decode(jsonArrayChanged.getJSONObject(i).get("image").toString(), Base64.DEFAULT);
                                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                                    circleImageView.setImageBitmap(decodedByte);
                                    // byte[] byteArray =  Base64.decode(jsonArrayChanged.getJSONObject(i).get("image").toString().getBytes(), Base64.DEFAULT) ;
                                    //  Bitmap bmp1 = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                                    // Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                                    //  ivImage.setImageBitmap(bmp1);
                                    //  txtDate.setText(jsonArrayChanged.getJSONObject(i).get("dob").toString());
                                    String gen = jsonArrayChanged.getJSONObject(i).get("gender").toString();
                                    // voucherClass.setMessage(jsonArrayChanged.getJSONObject(i).get("type").toString());
                                    if (gen.equalsIgnoreCase("Male")){
                                        //      spinner.setSelection (1);
                                    }
                                    else if (gen.equalsIgnoreCase("Female")){
                                        //      spinner.setSelection (2);
                                    }

                                    //  txtUserID.setText(userID);
                                    //   txtName.setText(jsonArrayChanged.getJSONObject(i).get("username").toString());
                                    //   Toast.makeText(getContext(),jsonArrayChanged.getJSONObject(i).get("category_id").toString(), Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

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
        httpGetAsyncTask.execute(user_id);

    }

    private ArrayList<NameValuePair> getParams() {
        // define and ArrayList whose elements are of type NameValuePair
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("start", "0"));
        params.add(new BasicNameValuePair("limit", "10"));
        params.add(new BasicNameValuePair("fields", "id,title"));
        return params;
    }


    public Fragment getItem(int position)
    {
        List<Fragment> fragList = new ArrayList<Fragment>();
        fragList.add(new HomeFragment());
        fragList.add(new BadgesFragment());
        fragList.add(new AccountFragment());
        fragList.add(new HelpFragment());
        fragList.add(new AboutUsFragment());



        Fragment fragment = fragList.get(position);
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    private void selectItemFragment(int position){

        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch(position)
        {
            default:
            case 0:
                fragment = new AccountFragment();
                // rlButtons.setVisibility(View.GONE);
                toolbarTitle.setText("Profile");
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                break;

            case 1:
                fragment = new HomeFragment();
                //  rlButtons.setVisibility(View.VISIBLE);
                toolbarTitle.setText("BeaconShop");
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                imgRightHeader.setVisibility(View.GONE);
                imgHome.setColorFilter(getResources().getColor(R.color.logo_color));
                imgVoucher.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.divider), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgFavourites.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.divider), android.graphics.PorterDuff.Mode.MULTIPLY);
                imgAccount.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.divider), android.graphics.PorterDuff.Mode.MULTIPLY);

                txtHome.setTextColor(getResources().getColor(R.color.logo_color));
                txtVoucher.setTextColor(getResources().getColor(R.color.divider));
                txtFavourite.setTextColor(getResources().getColor(R.color.divider));
                txtAccount.setTextColor(getResources().getColor(R.color.divider));

                break;
            case 2:
                fragment = new BadgesFragment();
                //  rlButtons.setVisibility(View.GONE);
                toolbarTitle.setText("Badges");
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                break;

            case 3:
                fragment = new AccountFragment();
                // rlButtons.setVisibility(View.GONE);
                toolbarTitle.setText("Profile");
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                break;
            case 4:
                fragment = new HelpFragment();
                //   rlButtons.setVisibility(View.GONE);
                toolbarTitle.setText("Help");
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                break;
            case 5:
                fragment = new AboutUsFragment();
                //  rlButtons.setVisibility(View.GONE);
                toolbarTitle.setText("About us");
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                break;
            case 6:
                // fragment = new ChangePasswordFragment();
                //  rlButtons.setVisibility(View.GONE);
                // toolbarTitle.setText("Change Password");


                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                session.logoutUser();
                            }
                        });
                PrefUtils.clearCurrentUser(MainActivity.this);

                // We can logout from facebook by calling following method
                LoginManager.getInstance().logOut();
                session.logoutUser();
                /*try {
                    if (!mGoogleApiClient.isConnected()) {
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        //updateUI(false);
                                        session.logoutUser();
                                    }
                                });
                        break;
                    }
                }catch (Exception e){
                    PrefUtils.clearCurrentUser(MainActivity.this);

                    // We can logout from facebook by calling following method
                    LoginManager.getInstance().logOut();
                    session.logoutUser();
                    break;
                }*/
                break;
            // session.logoutUser();}


        }

        mDrawerList.setItemChecked(position, true);
//        setTitle(titles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    /*public void selectFragment(int position){
        mViewPager.setCurrentItem(position, true);
// true is to animate the transaction
    }*/

    public void exportDatabaseToSdCard(View view) {
        Const.ExportDatabase(this);
    }

    protected void facebookSDKInitialize() {

        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
    }

    public Fragment getCurrentFragment() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        //    Log.e("currentFragment",""+currentFragment);
        return currentFragment;

    }

   /* @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (getCurrentFragment() instanceof AccountFragment) {
            Log.e("test",""+data+"  "+requestCode+"  "+resultCode);
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK)
            {
                Log.e("test1",""+result);
                ((AccountFragment) getCurrentFragment()).onActivity(result);
            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE&&resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
        *//*if (resultCode == Activity.RESULT_OK) {

            if (requestCode == SELECT_FILE)

                onSelectFromGalleryResult(data);

            else if (requestCode == REQUEST_CAMERA)

                onCaptureImageResult(data);
        }*//*
    }*/
    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
        if (getIntent().getAction() != null) {
            action = getIntent().getAction();
            notifID = getIntent().getIntExtra(EXTRA_NOTIFICATION_ID, 0);
            if (action.equalsIgnoreCase(ACTION_FROM_NOTIFICATION)) {
                mIsServiceStarted = true;
                //setButtonsEnabledState();

            }
        }
    }

  /*  public static void OnGoingLocationNotification(Context mcontext) {
        int mNotificationId;

        mNotificationId = (int) System.currentTimeMillis();

        //Broadcast receiver to handle the stop action
        Intent mstopReceive = new Intent(mcontext, NotificationHandler.class);
        mstopReceive.putExtra(EXTRA_NOTIFICATION_ID, mNotificationId);
        mstopReceive.setAction(ACTION_STOP);
        PendingIntent pendingIntentStopService = PendingIntent.getBroadcast(mcontext, (int) System.currentTimeMillis(), mstopReceive, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mcontext)
                        .setSound(alarmSound)
                        .setSmallIcon(R.drawable.ic_cast_off_light)
                        .setContentTitle("Location Service")
                        .addAction(R.drawable.ic_cancel, "Stop Service", pendingIntentStopService)
                        .setOngoing(true).setContentText("Running...");
        mBuilder.setAutoCancel(false);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(mcontext, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        resultIntent.setAction(ACTION_FROM_NOTIFICATION);
        resultIntent.putExtra(EXTRA_NOTIFICATION_ID, mNotificationId);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(mcontext, (int) System.currentTimeMillis(), resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) mcontext.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.cancel(mNotificationId);

        Notification mNotification = mBuilder.build();
        mNotification.defaults |= Notification.DEFAULT_VIBRATE;
        mNotification.flags |= Notification.FLAG_ONGOING_EVENT;

        mNotificationManager.notify(mNotificationId, mNotification);

    }*/

    /*private void cancelNotification(Context mContext, int mnotinotifId) {
        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(mnotinotifId);
    }*/

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* @Override
     public void onBackPressed()
     {
        *//* Fragment frag = new Fragment();
        Fragment frag0 = new HomeFragment();
        Fragment frag1 = new FavoriteFragment();
        Fragment frag2 = new VoucherFragment();
        Fragment frag3 = new BadgesFragment();
        Fragment frag4 = new ProfileFragment();
        Fragment frag5 = new SettingsFragment();
        Fragment frag6 = new ChangePasswordFragment();
        Fragment frag7 = new HelpFragment();
        Fragment frag8 = new AboutUsFragment();
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        fragmentList.add(0,frag0);
        fragmentList.add(1,frag1);
        fragmentList.add(2,frag2);
        fragmentList.add(3,frag3);
        fragmentList.add(4,frag4);
        fragmentList.add(5,frag5);
        fragmentList.add(6,frag6);
        fragmentList.add(7,frag7);
        fragmentList.add(8,frag8);
        if(fragmentList.indexOf(0) == 0)
        {

        }

*//*
       *//*Fragment homeFragment = new HomeFragment();
        if(homeFragment!=null)
        {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Exit Application?");
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id)
                                {
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
        }*//*
        if(getFragmentManager().getBackStackEntryCount() == 0)
        {
           // super.onBackPressed();
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Exit Application?");
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id)
                                {
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
        else {
            getFragmentManager().popBackStack();
        }

        // for new method

    }
*/
    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
        return isConnected;
    }

    private void showSnack(boolean isConnected) {
        String message = "Check for data connection..";
        if (isConnected) {
//            message = "Good! Connected to Internet";
        } else {
//            message = "Sorry! Not connected to internet";
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }
    @Override
    public void asyncResponse(String response)
    {
        // Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

        try{
        if (response.equals("")) {
            Toast.makeText(getApplicationContext(), "No offers..", Toast.LENGTH_LONG).show();
        } else {
            // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try {
                JSONObject jsonObject = new JSONObject(response);
                String res = jsonObject.getString("Voucher");
                // String message = jsonObject.getString("User");
                //  Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                if (res == null) {

                    Toast.makeText(getApplicationContext(), "No vouchers are available..", Toast.LENGTH_LONG).show();
                } else {
                    Log.d("StoreLocation Count", "StoreLocation Count: " + db.getAllLocations().size());
                    //  Toast.makeText(getApplicationContext(), "StoreLocation Count: " + db.getAllLocations().size(), Toast.LENGTH_LONG ).show();
                    db.deleteStoreLocaion();
                    db.deleteVoucher();
                    Log.d("StoreLocation Count", "After Delete StoreLocation Count: " + db.getAllLocations().size());
                    //  Toast.makeText(getApplicationContext(), "After Delete StoreLocation Count: " + db.getAllLocations().size(), Toast.LENGTH_LONG ).show();

                    JSONArray jsonArrayChanged = jsonObject.getJSONArray("Voucher");
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

                            //myAsync.execute(jsonArrayChanged.getJSONObject(i).get("store_image").toString());
                            /*byte[] array = null;

                            int SDK_INT = android.os.Build.VERSION.SDK_INT;
                            if (SDK_INT > 8)
                            {
                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                        .permitAll().build();
                                StrictMode.setThreadPolicy(policy);
                                //your codes here
                                URL url = new URL(jsonArrayChanged.getJSONObject(i).get("store_image").toString());
                                URLConnection conn = url.openConnection();
                                InputStream inStr = conn.getInputStream();
                                BufferedInputStream buffInStr = new BufferedInputStream(inStr);
                                // Need to check the size of Byte Array Buffer.
                                ByteArrayBuffer baf = new ByteArrayBuffer(500);
                                int current = 0;
                                while ((current = buffInStr.read()) != -1) {
                                    baf.append((byte) current);
                                }
                                array = baf.toByteArray();
                            }

                            URL url = new URL(jsonArrayChanged.getJSONObject(i).get("store_image").toString());
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setDoInput(true);
                            connection.connect();
                            InputStream input = connection.getInputStream();
                            Bitmap myBitmap = BitmapFactory.decodeStream(input);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            array = stream.toByteArray();*/

                            byte[] decodedString = Base64.decode(jsonArrayChanged.getJSONObject(i).get("store_image").toString(), Base64.DEFAULT);

                            db.addRecord(
                                    jsonArrayChanged.getJSONObject(i).get("id").toString(),
                                    jsonArrayChanged.getJSONObject(i).get("category_id").toString(),
                                    jsonArrayChanged.getJSONObject(i).get("store_name").toString(), decodedString,
                                    jsonArrayChanged.getJSONObject(i).get("lat").toString(),
                                    jsonArrayChanged.getJSONObject(i).get("lng").toString(),
                                    jsonArrayChanged.getJSONObject(i).get("offer_title").toString(),
                                    jsonArrayChanged.getJSONObject(i).get("offer_desc").toString(),
                                    jsonArrayChanged.getJSONObject(i).get("start_date").toString(),
                                    jsonArrayChanged.getJSONObject(i).get("end_date").toString(),
                                    jsonArrayChanged.getJSONObject(i).get("quantity").toString(),
                                    jsonArrayChanged.getJSONObject(i).get("paid_banner").toString(),
                                    jsonArrayChanged.getJSONObject(i).get("paid_start_date").toString(),
                                    jsonArrayChanged.getJSONObject(i).get("paid_end_date").toString(),
                                    jsonArrayChanged.getJSONObject(i).get("message").toString(),
                                    jsonArrayChanged.getJSONObject(i).get("uuid").toString(),
                                    jsonArrayChanged.getJSONObject(i).get("major").toString(),
                                    jsonArrayChanged.getJSONObject(i).get("minor").toString()
                            );

                            //  db.createVoucher(voucher);

                            StoreLocation tag1 = new StoreLocation(
                                    jsonArrayChanged.getJSONObject(i).get("id").toString(),
                                    jsonArrayChanged.getJSONObject(i).get("store_name").toString(),
                                    decodedString, jsonArrayChanged.getJSONObject(i).get("lat").toString(),
                                    jsonArrayChanged.getJSONObject(i).get("lng").toString(),
                                    jsonArrayChanged.getJSONObject(i).get("quantity").toString(),
                                    jsonArrayChanged.getJSONObject(i).get("offer_title").toString(),
                                    jsonArrayChanged.getJSONObject(i).get("offer_desc").toString(),
                                    jsonArrayChanged.getJSONObject(i).get("start_date").toString(),
                                    jsonArrayChanged.getJSONObject(i).get("end_date").toString()
                            );
                           /* StoreLocation tag2 = new StoreLocation("Ghatlodia", "23.057506", "72.543392", "Cashbak", "70% Cashback, Hurry up. Offer till 6th April, 2017 only. Men's wear discount 50%, Women's Wear discount 75%.", "08/03/2017", "06/04/2017");
                            StoreLocation tag3 = new StoreLocation("Vikram Appts", "23.01210", "72.522634", "Redeem Code", "Hurry up. Offer till 6th April, 2017 only. Men's wear discount 50%, Women's Wear discount 75%", "08/02/2015", "14/02/2016");
                            StoreLocation tag4 = new StoreLocation("Titanium City Center", "23.012102", "72.522634", "Whole Sale", "70% Cashback", "08/02/2015", "14/02/2016");
*/
                            db.createStoreLocation(tag1);
                            //   Toast.makeText(getContext(),jsonArrayChanged.getJSONObject(i).get("category_id").toString(), Toast.LENGTH_LONG).show();
                            offers.add(voucherClass);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    List<StoreLocation> allTags = db.getAllLocations();
                    for (StoreLocation tag : allTags) {
                        Log.d("StoreLocation Name", tag.getStore_name());
                        //  Toast.makeText(getApplicationContext(), tag.getStore_name() + " " + tag.getOffer_title(), Toast.LENGTH_LONG).show();

                    }

                    List<Voucher> allVoucher = db.getAllBeaconVouchers();
                    for (Voucher tag : allVoucher) {
                        Log.d("StoreLocation Name", tag.getStore_name());
                        // Toast.makeText(getApplicationContext(), tag.getStore_name() + " " + tag.getStore_image(), Toast.LENGTH_LONG).show();

                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        }catch (Exception e){}

    }


    public static byte[] urlToImageBLOB(String url) throws IOException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpEntity entity = null;
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = httpclient.execute(httpGet);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            entity = response.getEntity();
        }
        return EntityUtils.toByteArray(entity);
    }

    private byte[] getLogoImage(String url){
        try {
            URL imageUrl = new URL(url);
            URLConnection ucon = imageUrl.openConnection();

            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

            ByteArrayBuffer baf = new ByteArrayBuffer(500);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }

            return baf.toByteArray();
        } catch (Exception e) {
            Log.d("ImageManager", "Error: " + e.toString());
        }
        return null;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            // Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            //  showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    //     hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    public void handleSignInResult(GoogleSignInResult result) {
        //   Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            //  Log.e(TAG, "display name: " + acct.getDisplayName());

            String personName = acct.getDisplayName();
            String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();

            //  Log.e(TAG, "Name: " + personName + ", email: " + email + ", Image: " + personPhotoUrl);

            //updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            //  updateUI(false);
        }
    }

    public class MyAsync extends AsyncTask<String, Void, Bitmap>
    {

        @Override
        protected Bitmap doInBackground(String... params)
        {
            // for converting url to bitmap
            try {
                URL url = new URL(params[0]);
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
            //      bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteStoreImage = stream.toByteArray();

            //  Toast.makeText(getApplicationContext(),"Byte: "+byteStoreImage,Toast.LENGTH_LONG).show();

            // for convert byte array to bitmap
            Bitmap bmp = BitmapFactory.decodeByteArray(byteStoreImage, 0, byteStoreImage.length);
//            ivBitmapImage.setImageBitmap(Bitmap.createScaledBitmap(bmp, ivBitmapImage.getWidth(),
//                    ivBitmapImage.getHeight(), false));
            // ivBitmapImage.setImageBitmap(bmp);

            //    Toast.makeText(getApplicationContext(),"Bitmap: "+bmp,Toast.LENGTH_LONG).show();

            //   db = new DatabaseHelper(getApplicationContext());

            db.addImageData("V-Mart",byteStoreImage);
            //      Toast.makeText(getApplicationContext(),"Image: V-Mart "+byteStoreImage+ " has to be added.",Toast.LENGTH_LONG).show();

        }
    }

}
