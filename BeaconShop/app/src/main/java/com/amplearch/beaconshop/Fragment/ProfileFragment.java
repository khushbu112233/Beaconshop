package com.amplearch.beaconshop.Fragment;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amplearch.beaconshop.Activity.AccountActivity;
import com.amplearch.beaconshop.Activity.MainActivity;
import com.amplearch.beaconshop.Adapter.ElectOfferAdapter;
import com.amplearch.beaconshop.Adapter.VoucherAdapter;
import com.amplearch.beaconshop.ConnectivityReceiver;
import com.amplearch.beaconshop.Model.User;
import com.amplearch.beaconshop.Model.UserRedeem;
import com.amplearch.beaconshop.Model.VoucherClass;
import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.Utils.LocationUpdateService;
import com.amplearch.beaconshop.Utils.PrefUtils;
import com.amplearch.beaconshop.Utils.TrojanButton;
import com.amplearch.beaconshop.Utils.TrojanEditText;
import com.amplearch.beaconshop.Utils.TrojanText;
import com.amplearch.beaconshop.Utils.UserSessionManager;
import com.amplearch.beaconshop.Utils.Utility;
import com.amplearch.beaconshop.WebCall.AsyncRequest;
import com.android.internal.http.multipart.MultipartEntity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


public class ProfileFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, AsyncRequest.OnAsyncRequestComplete{

    TrojanButton btnSave;
    int count = 0;

    TrojanText btnDatePicker;
    TrojanText txtDate;
    ShareDialog shareDialog;

    private int mYear, mMonth, mDay, mHour, mMinute;

   // private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Button btnSelect;
    private CircleImageView ivImage;
    private String userChoosenTask;
    TrojanText voucher, Offer;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;

    TrojanText txtName, txtUserID;
    private TrojanButton btnSignOut, btnRevokeAccess;
    UserSessionManager session;
    TrojanButton btnLogout;

    private User user;
    Bitmap bitmap;
    Bitmap photo;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int CAMERA_REQUEST = 1888;
    String name;

    String email;
    String userID;
    private File file1;

    private String UPLOAD_URL ="http://beacon.ample-arch.com/BeaconWebService.asmx/UpdateProfile";

    private String KEY_IMAGE = "image";
    private String KEY_ID = "id";
    private String KEY_DOB = "dob";
    private String KEY_GEN = "gender";

    private int serverResponseCode = 0;
    private ProgressDialog dialog = null;

    private String upLoadServerUri = null;
    private String imagepath=null;
    Bitmap reminder_bitmap;
    public String SERVER = "http://beacon.ample-arch.com/BeaconWebService.asmx/UpdateProfile",
            timestamp;
    Spinner spinner;
    String password;
    LinearLayout share, invite;
    String voucherURL  ;
    ArrayList<NameValuePair> params;
    LinearLayout lnrvoucher, lnrOffer;
    public ProfileFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        checkConnection();
        shareDialog = new ShareDialog(this);
        upLoadServerUri = "http://beacon.ample-arch.com/BeaconWebService.asmx/UpdateProfile";
        spinner = (Spinner) rootView.findViewById(R.id.gender_spinner);
        session = new UserSessionManager(getContext());
        user= PrefUtils.getCurrentUser(getContext());
        btnDatePicker = (TrojanText) rootView.findViewById(R.id.btn_date);
        txtDate = (TrojanText) rootView.findViewById(R.id.in_date);
        ivImage = (CircleImageView) rootView.findViewById(R.id.profile_image);
        lnrvoucher = (LinearLayout) rootView.findViewById(R.id.lnrvoucher);
        lnrOffer = (LinearLayout) rootView.findViewById(R.id.lnrOffer);

        btnSave = (TrojanButton) rootView.findViewById(R.id.btnSave);
        share = (LinearLayout) rootView.findViewById(R.id.share);
        invite = (LinearLayout) rootView.findViewById(R.id.invite);
        txtName = (TrojanText) rootView.findViewById(R.id.txtName);
        txtUserID = (TrojanText) rootView.findViewById(R.id.txtUser_id);
        btnLogout = (TrojanButton) rootView.findViewById(R.id.logout);
        btnSignOut = (TrojanButton) rootView.findViewById(R.id.btn_sign_out);
        btnRevokeAccess = (TrojanButton) rootView.findViewById(R.id.btn_revoke_access);
        voucher = (TrojanText) rootView.findViewById(R.id.voucher);
        Offer = (TrojanText) rootView.findViewById(R.id.offer);

       // Toast.makeText(getContext(), "User Login Status: " + session.isUserLoggedIn(), Toast.LENGTH_LONG).show();

        ivImage.setOnClickListener(this);
        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.gender_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        btnDatePicker.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        btnRevokeAccess.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        lnrvoucher.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new VoucherFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        lnrOffer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        /*if(session.checkLogin()){
            getActivity().finish();
        }*/

        // get user data from session
        final HashMap<String, String> user1 = session.getUserDetails();

        // get name
      // name = user1.get(UserSessionManager.KEY_NAME);

        // get email
       //email = user1.get(UserSessionManager.KEY_EMAIL);

        userID = user1.get(UserSessionManager.KEY_USER_ID);
        voucherURL = "http://beacon.ample-arch.com/BeaconWebService.asmx/getRedeemUserbyUserID";
        if (checkConnection()== true)
        {
            params = getParams();
            AsyncRequest getPosts = new AsyncRequest(ProfileFragment.this.getActivity(), "GET", params);
            getPosts.execute(voucherURL);
        }

       // password = user1.get(UserSessionManager.KEY_PASSWORD);
        // Show user data on activity

        btnSave.setOnClickListener(this);
       // txtName.setText(name);
      //  txtUserID.setText(user1.get(UserSessionManager.KEY_USER_ID));
       // Toast.makeText(getContext(), name + " " + email, Toast.LENGTH_LONG).show();

        // Customizing G+ button

        if (checkConnection() == true)
        {
            connectWithHttpPost(userID);
            connectWithHttpPostVoucher(userID);
            connectWithHttpPostOffers();
        }

        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                URL imageURL = null;
                try {

                    if (user != null)
                        imageURL = new URL("https://graph.facebook.com/" + user.facebookID + "/picture?type=large");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    if (imageURL != null)
                        bitmap  = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                try {
                    if (user.name != null) {
                        ivImage.setImageBitmap(bitmap);
                        txtName.setText(user.name + System.lineSeparator() + user.email);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.execute();



        share.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = getActivity().getPackageName();
                String shareBody = "Hey Download this App before going to any Mall and Get best offer of mall in your Apps  \n" +
                        "https://play.google.com/store/apps/details?id=" + appPackageName;
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "BeaconShop");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share Via"));
            }
        });

        invite.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                final String appPackageName = getActivity().getPackageName();
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
        });

        return rootView;
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
                    Toast.makeText(getContext(), "Check For Data Connection..", Toast.LENGTH_LONG).show();
                }else {
                    //   Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String res = jsonObject.getString("user");
                        // String message = jsonObject.getString("User");
                        //  Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                        if (res.equals("")){
                            Toast.makeText(getContext(), "User does not exists..", Toast.LENGTH_LONG).show();
                        }
                        else {

                            JSONArray jsonArrayChanged = jsonObject.getJSONArray("user");
                            for (int i = 0, count = jsonArrayChanged.length(); i < count; i++) {
                                try {
                                    //JSONObject jObject = jsonArrayChanged.getJSONObject(i);
                                    userID = jsonArrayChanged.getJSONObject(i).get("id").toString();
                                    email =  jsonArrayChanged.getJSONObject(i).get("email_id").toString();
                                    //  voucherClass.setStore_name(jsonArrayChanged.getJSONObject(i).get("contact").toString());
                                    name = jsonArrayChanged.getJSONObject(i).get("username").toString();
                                    //  voucherClass.setOffer_title(jsonArrayChanged.getJSONObject(i).get("password").toString());
                                    byte[] image = jsonArrayChanged.getJSONObject(i).get("image").toString().getBytes();
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);

                                    byte[] decodedString = Base64.decode(jsonArrayChanged.getJSONObject(i).get("image").toString(), Base64.DEFAULT);
                                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                                    ivImage.setImageBitmap(decodedByte);
                                    // byte[] byteArray =  Base64.decode(jsonArrayChanged.getJSONObject(i).get("image").toString().getBytes(), Base64.DEFAULT) ;
                                  //  Bitmap bmp1 = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                                   // Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                                  //  ivImage.setImageBitmap(bmp1);
                                    txtDate.setText(jsonArrayChanged.getJSONObject(i).get("dob").toString());
                                     String gen = jsonArrayChanged.getJSONObject(i).get("gender").toString();
                                    // voucherClass.setMessage(jsonArrayChanged.getJSONObject(i).get("type").toString());
                                    if (gen.equalsIgnoreCase("Male")){
                                        spinner.setSelection (1);
                                    }
                                    else if (gen.equalsIgnoreCase("Female")){
                                        spinner.setSelection (2);
                                    }

                                    txtUserID.setText(userID);
                                    txtName.setText(jsonArrayChanged.getJSONObject(i).get("username").toString());
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

    private void connectWithHttpPostVoucher(final String user_id)
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
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(4);
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
                //Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
                if (result.equals(""))
                {
                    count = 0;
                }
                else
                {
                    //   Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                    try
                    {
                        JSONObject jsonObject = new JSONObject(result);
                        String res = jsonObject.getString("redeem");
                        // String message = jsonObject.getString("User");
                        //  Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                        if (res==null)
                        {
                            count = 0;
                        }
                        else
                        {
                            JSONArray jsonArrayChanged = jsonObject.getJSONArray("redeem");
                            if (jsonArrayChanged.length() == 0)
                            {
                                count = 0;
                            }
                            else
                            {
                                count = jsonArrayChanged.length();
                            }
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                voucher.setText(String.valueOf(count));
            }
        }

        // Initialize the AsyncTask class
        HttpGetAsyncTask httpGetAsyncTask = new HttpGetAsyncTask();
        // Parameter we pass in the execute() method is relate to the first generic type of the AsyncTask
        // We are passing the connectWithHttpGet() method arguments to that
        httpGetAsyncTask.execute(user_id);

    }

    int OfferCount = 0;
    private void connectWithHttpPostOffers()
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
              //  String paramUserID = params[0];
                //    System.out.println("paramUsername is :" + paramUsername + " paramPassword is :" + paramPassword);

                // Create an intermediate to connect with the Internet
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://beacon.ample-arch.com/BeaconWebService.asmx/getVoucherList");
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

                //Post Data
              //  List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(4);
               // nameValuePair.add(new BasicNameValuePair("user_id", paramUserID));



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
                //Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
                if (result.equals(""))
                {
                    OfferCount = 0;
                }
                else
                {
                    //   Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                    try
                    {
                        JSONObject jsonObject = new JSONObject(result);
                        String res = jsonObject.getString("Voucher");
                        // String message = jsonObject.getString("User");
                        //  Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                        if (res==null)
                        {
                            OfferCount = 0;
                        }
                        else
                        {
                            JSONArray jsonArrayChanged = jsonObject.getJSONArray("Voucher");
                            if (jsonArrayChanged.length() == 0)
                            {
                                OfferCount = 0;
                            }
                            else
                            {
                                OfferCount = jsonArrayChanged.length();
                            }
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Offer.setText(String.valueOf(OfferCount));
            }
        }

        // Initialize the AsyncTask class
        HttpGetAsyncTask httpGetAsyncTask = new HttpGetAsyncTask();
        // Parameter we pass in the execute() method is relate to the first generic type of the AsyncTask
        // We are passing the connectWithHttpGet() method arguments to that
        httpGetAsyncTask.execute();

    }

    private ArrayList<NameValuePair> getParams()
    {
        // define and ArrayList whose elements are of type NameValuePair
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("user_id", userID));
        return params;
    }

    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
        return isConnected ;
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


    private void showSnack(boolean isConnected) {
        String message = "Check For Data Connection..";
        if (isConnected) {
//            message = "Good! Connected to Internet";
        } else {
//            message = "Sorry! Not connected to internet";
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    private void uploadImage(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(getContext(),"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(getContext(), s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(getContext(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String

                int day, month, year;
                int second, minute, hour;
                GregorianCalendar date = new GregorianCalendar();

                day = date.get(Calendar.DAY_OF_MONTH);
                month = date.get(Calendar.MONTH);
                year = date.get(Calendar.YEAR);

                second = date.get(Calendar.SECOND);
                minute = date.get(Calendar.MINUTE);
                hour = date.get(Calendar.HOUR);

                String name=(hour+""+minute+""+second+""+day+""+(month+1)+""+year);
                String tag=name+".jpg";
                String fileName = imagepath.replace(imagepath,tag);

                File sourceFile = new File(imagepath);





                String image = getStringImage(photo);

                //Getting Image Name
               // String name = editTextName.getText().toString().trim();

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_ID, userID);
                params.put(KEY_IMAGE, image);
                params.put(KEY_DOB, "08/08/1996");
                params.put(KEY_GEN, "Female");

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }


    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void activeTakePhoto() {  // if select open camera
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    private void activeGallery()
    { // if select choose from gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                        session.logoutUser();
                    }
                });
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                        session.logoutUser();
                    }
                });
    }

    public void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

            String personName = acct.getDisplayName();
            String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();

            Log.e(TAG, "Name: " + personName + ", email: " + email
                    + ", Image: " + personPhotoUrl);

            txtName.setText(personName + System.lineSeparator() + email );
            // txtEmail.setText(email);
            Glide.with(getContext()).load(personPhotoUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivImage);

            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }
    String gender;
    String date;
    @Override
    public void onClick(View v) {
        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                        {
                            Date date = new Date(year-1900, monthOfYear,dayOfMonth);
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            String cdate = formatter.format(date);
                            txtDate.setText(cdate);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }

        if (v == ivImage) {
            selectImage();
        }

        if (v == btnSave){
           // reminder_bitmap = ivImage.getDrawingCache();

            /*AsyncUpdateClass asyncRequestObject = new AsyncUpdateClass();
            asyncRequestObject.execute();*/


            boolean isConnected = false;
            isConnected = checkConnection();
            if (isConnected) {
                gender = spinner.getSelectedItem().toString();
                date = txtDate.getText().toString();
                Bitmap image = ((BitmapDrawable) ivImage.getDrawable()).getBitmap();
                Drawable myDrawable = ivImage.getDrawable();
                if(ivImage.getDrawable().getConstantState().equals
                        (getResources().getDrawable(R.drawable.default1).getConstantState())){
                    Toast.makeText(getContext(), "Please Select Profile Picture..", Toast.LENGTH_LONG).show();
                } else if (date.equals("")) {
                    Toast.makeText(getContext(), "Please Select BirthDate..", Toast.LENGTH_LONG).show();
                } else if (gender.equals("Select")) {
                    Toast.makeText(getContext(), "Please Select Gender..", Toast.LENGTH_LONG).show();
                } else if (image == null) {
                    Toast.makeText(getContext(), "Please Select Profile Picture..", Toast.LENGTH_LONG).show();
                } else {
                    //execute the async task and upload the image to server
                    new Upload(image, "IMG_" + timestamp).execute();
                    // uploadImage();
                }
            }
           /* dialog = ProgressDialog.show(getContext(), "", "Uploading file...", true);
          //  messageText.setText("uploading started.....");
            new Thread(new Runnable() {
                public void run() {

                    uploadFile(imagepath);

                }
            }).start();*/
        }

        if (v == btnSignOut){
            signOut();
        }

        if (v == btnRevokeAccess){
            revokeAccess();
        }

        if (v == btnLogout){


            PrefUtils.clearCurrentUser(getContext());


            // We can logout from facebook by calling following method
            LoginManager.getInstance().logOut();
            session.logoutUser();

        }

    }

    @Override
    public void asyncResponse(String response) {

        Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
        if (response.equals(""))
        {
           count = 0;
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
                    count = 0;
                }
                else
                {
                    JSONArray jsonArrayChanged = jsonObject.getJSONArray("redeem");
                    if (jsonArrayChanged.length() == 0)
                    {
                        count = 0;
                    }
                    else
                    {
                       count = jsonArrayChanged.length();
                    }
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        voucher.setText(String.valueOf(count));

    }

    class ImageUploadTask extends AsyncTask<Void, Void, String> {
        private String webAddressToPost = "http://beacon.ample-arch.com/BeaconWebService.asmx/UpdateProfile";

        // private ProgressDialog dialog;
        private ProgressDialog dialog = new ProgressDialog(getContext());

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Uploading...");
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                HttpPost httpPost = new HttpPost(webAddressToPost);
                httpPost.setHeader(HTTP.CONTENT_TYPE,
                        "application/x-www-form-urlencoded;charset=UTF-8");
                org.apache.http.entity.mime.MultipartEntity entity = new org.apache.http.entity.mime.MultipartEntity(
                        HttpMultipartMode.BROWSER_COMPATIBLE);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                byte[] data = bos.toByteArray();
                String file = Base64.encodeToString(data, 0);
                entity.addPart("id", new StringBody(userID));
                entity.addPart("image", new StringBody(file));

                entity.addPart("dob", new StringBody("29/03/1994"));
                entity.addPart("gender", new StringBody("female"));

                httpPost.setEntity(entity);
                HttpResponse response = httpClient.execute(httpPost,
                        localContext);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                response.getEntity().getContent(), "UTF-8"));

                String sResponse = reader.readLine();
                return sResponse;
            } catch (Exception e) {
                // something went wrong. connection with the server error
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            Toast.makeText(getContext(), result,
                    Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        activeTakePhoto();
                    else if (userChoosenTask.equals("Choose from Library"))
                        activeGallery();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage()
    {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(getContext());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result) {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        activeGallery();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

  /*  private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image*//*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
*/

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }


    public int uploadFile(String sourceFileUri) {

        //sourceFileUri.replace(sourceFileUri, "ashifaq");
        //

        int day, month, year;
        int second, minute, hour;
        GregorianCalendar date = new GregorianCalendar();

        day = date.get(Calendar.DAY_OF_MONTH);
        month = date.get(Calendar.MONTH);
        year = date.get(Calendar.YEAR);

        second = date.get(Calendar.SECOND);
        minute = date.get(Calendar.MINUTE);
        hour = date.get(Calendar.HOUR);

        String name=(hour+""+minute+""+second+""+day+""+(month+1)+""+year);
        String tag=name+".jpg";
        String fileName = sourceFileUri.replace(sourceFileUri,tag);

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

            dialog.dismiss();

            Log.e("uploadFile", "Source File not exist :"+imagepath);

            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                  //  messageText.setText("Source File not exist :"+ imagepath);
                    Toast.makeText(getContext(), "Source File not exist :"+ imagepath, Toast.LENGTH_LONG).show();
                }
            });

            return 0;

        }
        else
        {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + fileName + "\"" + lineEnd);

                dos.writeBytes(lineEnd);




                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if(serverResponseCode == 200){

                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                    +" C:/wamp/wamp/www/uploads";
                           // messageText.setText(msg);
                            Toast.makeText(getContext(), "File Upload Complete.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                dialog.dismiss();
                ex.printStackTrace();

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                       // messageText.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(getContext(), "MalformedURLException", Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                dialog.dismiss();
                e.printStackTrace();

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                       // messageText.setText("Got Exception : see logcat ");
                      //  Toast.makeText(getContext(), "Got Exception : see logcat ", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file to server Exception", "Exception : "  + e.getMessage(), e);
            }
            dialog.dismiss();
            return serverResponseCode;

        }
    }


    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_LOAD_IMAGE:
                if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK & null != data) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver()
                            .query(selectedImage, filePathColumn, null, null,
                                    null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    Bitmap a = (BitmapFactory.decodeFile(picturePath));
                    file1 = null;
                    try {
                        Bitmap thumbnail1 = MediaStore.Images.Media.getBitmap(
                                getContext().getContentResolver(), data.getData());
                        file1 = persistImage(thumbnail1, "profileImage");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imagepath = getPath(selectedImage);
                    Bitmap bitmap=BitmapFactory.decodeFile(imagepath);
                    Long tsLong = System.currentTimeMillis() / 1000;
                    timestamp = tsLong.toString();
                 //   photo = scaleBitmap(a, 200, 200);
                    photo = scaleDown(bitmap, 100, true);
                    ivImage.setImageBitmap(photo);
                    //photo = decodeSampledBitmapFromUri(picturePath, 100, 20);
                   // ivImage.setImageBitmap(photo);
                }
                break;

            case REQUEST_IMAGE_CAPTURE:
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);


                //to generate random file name
                String fileName = "tempimg.jpg";

                try {
                    file1 = null;
                    Bitmap thumbnail1 = MediaStore.Images.Media.getBitmap(
                            getContext().getContentResolver(), data.getData());
                    photo = (Bitmap) data.getExtras().get("data");
                    file1 = persistImage(thumbnail1, "profileImage");
                    Long tsLong = System.currentTimeMillis() / 1000;
                    timestamp = tsLong.toString();
                    //captured image set in imageview
                    ivImage.setImageBitmap(thumbnail);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case CAMERA_REQUEST:
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes1 = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 90, bytes1);
                ivImage.setImageBitmap(photo);
                break;
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

    private class Upload extends AsyncTask<Void,Void,String>{
        private Bitmap image;
        private String name1;

        public Upload(Bitmap image,String name1){
            this.image = image;
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
            String encodeImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT);

            // Bitmap bm = BitmapFactory.decodeFile("/path/to/image.jpg");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object

            String imgString = Base64.encodeToString(getBytesFromBitmap(image),
                    Base64.NO_WRAP);

            byte[] b = baos.toByteArray();
            //generate hashMap to store encodedImage and the name
            HashMap<String,String> detail = new HashMap<>();
            detail.put("id", userID);
            detail.put("dob", date);
            detail.put("gender", gender);
            detail.put("image", imgString);

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

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(s);
                String res = jsonObject.getString("message");
                if (res.equalsIgnoreCase("Success")){
                    Toast.makeText(getContext(),"Data Uploaded Successfully..",Toast.LENGTH_SHORT).show();
                   // session.createUserLoginSession(name, email, "", password, userID);
                }
                else {
                    Toast.makeText(getContext(),"Data not Uploaded..",Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    private File persistImage(Bitmap bitmap, String name) {
        File filesDir = getContext().getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return imageFile;
    }

    @Override
    public void onResume() {

        super.onResume();

        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();

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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }


    @Override
    public void onPause() {
        super.onPause();

        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }
    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            // btnSignIn.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);
            btnRevokeAccess.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.GONE);
            // llProfileLayout.setVisibility(View.VISIBLE);
        } else {
            // btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
            btnRevokeAccess.setVisibility(View.GONE);
            btnLogout.setVisibility(View.VISIBLE);
            //   Intent intent = new Intent(getContext(), AccountActivity.class);
            //  startActivity(intent);
            //  llProfileLayout.setVisibility(View.GONE);
        }
    }

    private class AsyncUpdateClass extends AsyncTask<Void, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Saving Data..");
            //dialog.setTitle("Saving Reminder");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... params) {

            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 9000);
            HttpConnectionParams.setSoTimeout(httpParameters, 9000);

            HttpClient httpClient = new DefaultHttpClient(httpParameters);

            String urlString = "http://beacon.ample-arch.com/BeaconWebService.asmx/UpdateProfile";

            HttpPost httpPost = new HttpPost(urlString);
            httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

            // Bitmap bmp = BitmapFactory.decodeFile(selectedPath);
            // ByteArrayOutputStream bos = new ByteArrayOutputStream();
            //  bmp.compress(Bitmap.CompressFormat.JPEG, 70, bos);
            //  InputStream in = new ByteArrayInputStream(bos.toByteArray());
            //  ContentBody foto = new InputStreamBody(in, "image/jpeg", "filename");
            // FileBody bin1 = new FileBody(file1);

            String jsonResult = "";
            try {

                org.apache.http.entity.mime.MultipartEntity reqEntity = new org.apache.http.entity.mime.MultipartEntity();
                /*if (file1 == null) {
                    file1 = persistImage(reminder_bitmap, "profileimage");
                    FileBody bin1 = new FileBody(file1);
                    reqEntity.addPart("id", new StringBody(userID));
                    reqEntity.addPart("device_id", new StringBody(android_id));
                    reqEntity.addPart("cate_id", new StringBody(reminder_cate_id));
                    reqEntity.addPart("post_id", new StringBody(reminder_post_id));
                    reqEntity.addPart("title", new StringBody(str));
                    reqEntity.addPart("date", new StringBody(date));
                    //reqEntity.addPart("reminder_oldimg", new StringBody(reminder_post_image));
                    reqEntity.addPart("reminder_img", bin1);
                } else {*/
                    FileBody bin1 = new FileBody(file1);
                    reqEntity.addPart("id", new StringBody(userID));
                    reqEntity.addPart("image", bin1);
                    reqEntity.addPart("dob", new StringBody("08/08/1996"));
                    reqEntity.addPart("gender", new StringBody("female"));


              //  }
                httpPost.setEntity(reqEntity);
                HttpResponse response = httpClient.execute(httpPost);
                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();


            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonResult;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.dismiss();
            Log.e("Resulted Value: ", result);
            System.out.println("Resulted Value: " + result);
          //   Toast.makeText(getContext(), "Resulted value" + result, Toast.LENGTH_LONG).show();
            if (result.equals("") || result == null) {

                /*Snackbar snackbar = Snackbar.make(mRoot, "Unable to Fetch From Server !", Snackbar.LENGTH_LONG);

                // Changing message text color
                snackbar.setActionTextColor(Color.RED);

                // Changing action button text color
                View sbView = snackbar.getView();
                // sbView.setBackground(getResources().getColor(R.color.actionbar_background));
                sbView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.actionbar_background));
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(17);
                snackbar.show();*/

                Toast.makeText(getContext(), "Server connection failed...", Toast.LENGTH_LONG).show();
                return;
            }
        }
          /*  String error = returnParsedJsonObject(result);

            String message = returnParsedJsonObject1(result);
            //Toast.makeText(getApplicationContext(),"error_code : " + error,Toast.LENGTH_LONG).show();
            // Toast.makeText(getApplicationContext(),"msg : " + message,Toast.LENGTH_LONG).show();

            if (message.equals("unsuccess") || error.equals("0")) {

                Toast.makeText(getContext(), "Reminder is not Added..", Toast.LENGTH_LONG).show();
                return;
            } else if (message.equals("success")) {


                if (error.equals("1")) {

                    Toast.makeText(getApplicationContext(), "Reminder Updated Successfully..", Toast.LENGTH_LONG).show();

                    Intent myIntent = new Intent(getApplicationContext(), MyReceiver.class);
                    myIntent.putExtra("ticker", "Check it out !");
                    myIntent.putExtra("title", str);
                    myIntent.putExtra("text", event_category);

                    pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, myIntent, 0);

                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);


                    Intent intent = new Intent(getApplicationContext(), FestivalListPage.class);
                    startActivity(intent);
                    finish();
                }
            }
        }*/


        private String returnParsedJsonObject(String result) {

            JSONObject resultObject = null;
            String returnedResult = "";
            try {

                resultObject = new JSONObject(result);
                JSONArray jsonArray = resultObject.getJSONArray("response");
                // Toast.makeText(getApplicationContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();
                returnedResult = jsonArray.getJSONObject(0).getString("error");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnedResult;
        }

        private String returnParsedJsonObject1(String result) {

            JSONObject resultObject = null;
            String returnedResult = "";
            try {
                resultObject = new JSONObject(result);
                JSONArray jsonArray = resultObject.getJSONArray("response");
                // Toast.makeText(getApplicationContext(), jsonArray.toString(), Toast.LENGTH_LONG).show();
                returnedResult = jsonArray.getJSONObject(0).getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnedResult;
        }

        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            try {
                while ((rLine = br.readLine()) != null) {
                    answer.append(rLine);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return answer;
        }
    }




    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ivImage.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ivImage.setImageBitmap(bm);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }


}