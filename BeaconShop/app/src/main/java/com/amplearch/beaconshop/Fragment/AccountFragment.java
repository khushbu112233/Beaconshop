package com.amplearch.beaconshop.Fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.amplearch.beaconshop.ConnectivityReceiver;
import com.amplearch.beaconshop.Model.User;
import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.Utils.GillSansButton;
import com.amplearch.beaconshop.Utils.GillSansEditText;
import com.amplearch.beaconshop.Utils.GillSansTextView;
import com.amplearch.beaconshop.Utils.NearbyMessagePref;
import com.amplearch.beaconshop.Utils.PrefUtils;
import com.amplearch.beaconshop.Utils.UserSessionManager;
import com.amplearch.beaconshop.Utils.Utility;
import com.amplearch.beaconshop.WebCall.AsyncRequest;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.amplearch.beaconshop.Fragment.ProfileFragment.scaleDown;

/**
 * Created by ample-arch on 5/18/2018.
 */

public class AccountFragment extends Fragment implements AsyncRequest.OnAsyncRequestComplete, AdapterView.OnItemSelectedListener{

    // FragmentProfileNewLayoutBinding fragmentProfileNewLayoutBinding;
    //View view;
    private String UPLOAD_URL ="http://beacon.ample-arch.com/BeaconWebService.asmx/UpdateProfile";
    GillSansTextView txtProfile,txtChangePassword,txtSettings,etBday;
    RelativeLayout llProfile,llChangePassword;
    LinearLayout llSettings;
    View vProfile,vChangePassword,vSettings;
    GillSansButton btnUpdate,btnChangePassword;
    GillSansEditText etName,etEmail,etContact,etPass,etNewPass,etConfirmPass;
    ArrayList<NameValuePair> params ;
    String voucherURL  ;
    UserSessionManager session;
    String changePasswordURL = "http://beacon.ample-arch.com/BeaconWebService.asmx/ChangePassword" ;
    String email, password, userId, name;
    public String SERVER = "http://beacon.ample-arch.com/BeaconWebService.asmx/UpdateProfile",
            timestamp;
    String user_Email, user_OldPassword, user_NewPassword ;
    ImageView imgConfirmPass,imgNewPass,imgPass;
    Switch sNewOffer,sAllowPopup,sWelcomeMessage;
    NearbyMessagePref pref;
    Calendar myCalendar;
    Spinner spinner;
    String gender,date2;
    CircularImageView profile_image;
    Bitmap bitmap;
    Bitmap photo;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int CAMERA_REQUEST = 1888;
    private String userChoosenTask;
    private User user;
    int count = 0;
    private File file1;
    private String imagepath=null;
    TextView txtName, txtEmail;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_profile_new_layout, container, false);

        checkConnection();
        pref = new NearbyMessagePref(getContext());
        final HashMap<String, String> nearpref = pref.getUserDetails();

        String near = nearpref.get(NearbyMessagePref.KEY_OFFER_NOTI);

        Date date = new Date();
        myCalendar = new GregorianCalendar();
        myCalendar.setTime(date);
        myCalendar.add(Calendar.YEAR, 0);
        session = new UserSessionManager(getContext());
        // get user data from session
        final HashMap<String, String> user1 = session.getUserDetails();
        final String login_email = user1.get(UserSessionManager.KEY_EMAIL);
        user= PrefUtils.getCurrentUser(getActivity());
        email = user1.get(UserSessionManager.KEY_EMAIL);
        name = user1.get(UserSessionManager.KEY_NAME);
        password = user1.get(UserSessionManager.KEY_PASSWORD);
        userId = user1.get(UserSessionManager.KEY_USER_ID);
        profile_image = (CircularImageView)rootview.findViewById(R.id.profile_image);
        sNewOffer = (Switch)rootview.findViewById(R.id.sNewOffer);
        sAllowPopup = (Switch)rootview.findViewById(R.id.sAllowPopup);
        sWelcomeMessage = (Switch)rootview.findViewById(R.id.sWelcomeMessage);
        imgConfirmPass = (ImageView)rootview.findViewById(R.id.imgConfirmPass);
        imgNewPass = (ImageView)rootview.findViewById(R.id.imgNewPass);
        imgPass = (ImageView)rootview.findViewById(R.id.imgPass);
        btnChangePassword = (GillSansButton)rootview.findViewById(R.id.btnChangePassword);
        etBday = (GillSansTextView) rootview.findViewById(R.id.etBday);
        etContact = (GillSansEditText)rootview.findViewById(R.id.etContact);
        etEmail = (GillSansEditText)rootview.findViewById(R.id.etEmail);
        etName = (GillSansEditText)rootview.findViewById(R.id.etName);
        spinner = (Spinner) rootview.findViewById(R.id.gender_spinner);
        etPass = (GillSansEditText)rootview.findViewById(R.id.etPass);
        etNewPass = (GillSansEditText)rootview.findViewById(R.id.etNewPass);
        etConfirmPass = (GillSansEditText)rootview.findViewById(R.id.etConfirmPass);
        txtProfile = (GillSansTextView)rootview.findViewById(R.id.txtProfile);
        txtChangePassword = (GillSansTextView)rootview.findViewById(R.id.txtChangePassword);
        txtSettings = (GillSansTextView)rootview.findViewById(R.id.txtSettings);
        llProfile = (RelativeLayout)rootview.findViewById(R.id.llProfile);
        llChangePassword = (RelativeLayout)rootview.findViewById(R.id.llChangePassword);
        llSettings =(LinearLayout)rootview.findViewById(R.id.llSettings);
        txtName = (TextView) rootview.findViewById(R.id.txtName);
        vProfile = (View)rootview.findViewById(R.id.vProfile);
        vChangePassword = (View)rootview.findViewById(R.id.vChangePassword);
        vSettings = (View)rootview.findViewById(R.id.vSettings);
        txtEmail = (TextView) rootview.findViewById(R.id.txtEmail);
        btnUpdate = (GillSansButton)rootview.findViewById(R.id.btnUpdate);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.gender_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        final HashMap<String, String> user2 = session.getUserDetails();

        // get name
        // name = user1.get(UserSessionManager.KEY_NAME);

        // get email
        //email = user1.get(UserSessionManager.KEY_EMAIL);

        voucherURL = "http://beacon.ample-arch.com/BeaconWebService.asmx/getRedeemUserbyUserID";
        if (checkConnection()== true)
        {
            params = getParams();
            AsyncRequest getPosts = new AsyncRequest((Activity)getContext(), "GET", params);
            getPosts.execute(voucherURL);
        }

        if (checkConnection() == true)
        {
            connectWithHttpPost(userId);
            connectWithHttpPostVoucher(userId);
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
                        profile_image.setImageBitmap(bitmap);
                        etName.setText(user.name + System.lineSeparator() + user.email);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.execute();

        if (near.equals("true")){
            sNewOffer.setChecked(true);
        }
        else {
            sNewOffer.setChecked(false);
        }
        sNewOffer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    pref.createUserLoginSession("true");

                }
                // Cheese me
                else {
                    pref.createUserLoginSession("false");
                }
            }
        });
        imgPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPass.setTransformationMethod(new PasswordTransformationMethod());
            }
        });
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_OldPassword = etPass.getText().toString();
                user_NewPassword = etNewPass.getText().toString();
                user_Email =email;

                if (checkConnection() == true)
                {
                    params = getParams();
                    AsyncRequest getPosts = new AsyncRequest(AccountFragment.this,getActivity(), "GET", params, "");
                    getPosts.execute(changePasswordURL);
                }
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //selectImage();

                boolean result= Utility.checkPermission(getContext());

                if(result) {
                    /*CropImage.activity(null)
                            .setCropShape(CropImageView.CropShape.OVAL)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .start(getContext(), AccountFragment.this);*/

                    Intent intent = CropImage.activity(null)
                            .setCropShape(CropImageView.CropShape.OVAL)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .getIntent(getContext());
                    startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
                }

            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isConnected = false;
                isConnected = checkConnection();
                if (isConnected) {
                    gender = spinner.getSelectedItem().toString();
                    date2 = etBday.getText().toString();
                    Bitmap image = ((BitmapDrawable) profile_image.getDrawable()).getBitmap();
                    Drawable myDrawable = profile_image.getDrawable();
                    if(profile_image.getDrawable().getConstantState().equals
                            (getResources().getDrawable(R.drawable.default1).getConstantState())){
                        Toast.makeText(getContext(), "Please Select Profile Picture..", Toast.LENGTH_LONG).show();
                    } else if (date2.equals("")) {
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
            }
        });
        txtProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vProfile.setVisibility(View.VISIBLE);
                vChangePassword.setVisibility(View.INVISIBLE);
                vSettings.setVisibility(View.INVISIBLE);
                llProfile.setVisibility(View.VISIBLE);
                llChangePassword.setVisibility(View.GONE);
                llSettings.setVisibility(View.GONE);
            }
        });
        txtChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                vProfile.setVisibility(View.INVISIBLE);
                vChangePassword.setVisibility(View.VISIBLE);
                vSettings.setVisibility(View.INVISIBLE);
                llProfile.setVisibility(View.GONE);
                llChangePassword.setVisibility(View.VISIBLE);
                llSettings.setVisibility(View.GONE);
            }
        });

        txtSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                vProfile.setVisibility(View.INVISIBLE);
                vChangePassword.setVisibility(View.INVISIBLE);
                vSettings.setVisibility(View.VISIBLE);
                llProfile.setVisibility(View.GONE);
                llChangePassword.setVisibility(View.GONE);
                llSettings.setVisibility(View.VISIBLE);
            }
        });
        etBday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        setupCalendar();
        return rootview;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
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
                boolean result= Utility.checkPermission(getContext());

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

    public void setupCalendar() {

        etBday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new DatePickerDialog(getActivity(), date1, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
    }
    DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            DateTime dt = DateTime.now().withYear(myCalendar.get(Calendar.YEAR))
                    .withMonthOfYear(myCalendar.get(Calendar.MONTH) + 1)
                    .withDayOfMonth(myCalendar.get(Calendar.DAY_OF_MONTH));
            etBday.setText(dt.toString("dd-MM-yyyy"));
        }

    };

    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
        return true;
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
    private ArrayList<NameValuePair> getParams() {
        // define and ArrayList whose elements are of type NameValuePair
        ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        nameValuePair.add(new BasicNameValuePair("email_id", user_Email));
        nameValuePair.add(new BasicNameValuePair("oldPassword", user_OldPassword));
        nameValuePair.add(new BasicNameValuePair("newPassword", user_NewPassword));
        return nameValuePair;
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
                   // photo = scaleDown(bitmap, 100, true);
                    profile_image.setImageBitmap(bitmap);
                    //photo = decodeSampledBitmapFromUri(picturePath, 100, 20);
                    // ivImage.setImageBitmap(photo);
                }
                break;

            case REQUEST_IMAGE_CAPTURE:
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);


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
                    profile_image.setImageBitmap(thumbnail);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case CAMERA_REQUEST:
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes1 = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes1);
                profile_image.setImageBitmap(photo);
                break;


            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == Activity.RESULT_OK) {
                    profile_image.setImageURI(result.getUri());
                }
                else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Toast.makeText(getActivity(), "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private File persistImage(Bitmap bitmap, String name) {
        File filesDir = getContext().getFilesDir();
        File imageFile = new File(filesDir, name + ".png");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }
        return imageFile;
    }

    @Override
    public void asyncResponse(String response)
    {
//        Toast.makeText(getContext(),"Result: "+response,Toast.LENGTH_LONG).show();
        Log.i("ChPassword response", response);

        if (response.equals(""))
        {
            Toast.makeText(getContext(), "Email Address..?", Toast.LENGTH_LONG).show();
        }
        else
        {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String res = jsonObject.getString("User");
//                        Toast.makeText(getApplicationContext(), "res: "+res, Toast.LENGTH_LONG).show();
                if (res.equals("Invalid Credential"))
                {
                    Toast.makeText(getContext(), "Your Old Password not correct. ", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getContext(), "Password has been changed, Successfully!", Toast.LENGTH_LONG).show();
                    session.createUserLoginSession(name, email, "", user_NewPassword, userId);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
                    while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
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

                try {
                    if (result.equals("")) {
                        Toast.makeText(getContext(), "Check For Data Connection..", Toast.LENGTH_LONG).show();
                    } else {
                        //   Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String res = jsonObject.getString("user");
                            // String message = jsonObject.getString("User");
                            //  Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                            if (res.equals("")) {
                                Toast.makeText(getContext(), "User does not exists..", Toast.LENGTH_LONG).show();
                            } else {

                                JSONArray jsonArrayChanged = jsonObject.getJSONArray("user");
                                for (int i = 0, count = jsonArrayChanged.length(); i < count; i++) {
                                    try {
                                        //JSONObject jObject = jsonArrayChanged.getJSONObject(i);
                                        userId = jsonArrayChanged.getJSONObject(i).get("id").toString();
                                        email = jsonArrayChanged.getJSONObject(i).get("email_id").toString();
                                        //  voucherClass.setStore_name(jsonArrayChanged.getJSONObject(i).get("contact").toString());
                                        name = jsonArrayChanged.getJSONObject(i).get("username").toString();
                                        //  voucherClass.setOffer_title(jsonArrayChanged.getJSONObject(i).get("password").toString());
                                        byte[] image = jsonArrayChanged.getJSONObject(i).get("image").toString().getBytes();
                                        // Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);

                                        byte[] decodedString = Base64.decode(jsonArrayChanged.getJSONObject(i).get("image").toString(), Base64.DEFAULT);
                                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                                        profile_image.setImageBitmap(decodedByte);
                                        // byte[] byteArray =  Base64.decode(jsonArrayChanged.getJSONObject(i).get("image").toString().getBytes(), Base64.DEFAULT) ;
                                        //  Bitmap bmp1 = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                                        // Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                                        //  ivImage.setImageBitmap(bmp1);
                                        etBday.setText(jsonArrayChanged.getJSONObject(i).get("dob").toString());
                                        String gen = jsonArrayChanged.getJSONObject(i).get("gender").toString();
                                        // voucherClass.setMessage(jsonArrayChanged.getJSONObject(i).get("type").toString());
                                        if (gen.equalsIgnoreCase("Male")) {
                                            spinner.setSelection(1);
                                        } else if (gen.equalsIgnoreCase("Female")) {
                                            spinner.setSelection(2);
                                        }

                                        //  txtUserID.setText(userID);
                                        etName.setText(jsonArrayChanged.getJSONObject(i).get("username").toString());
                                        etEmail.setText(email);
                                        txtEmail.setText(email);
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
                } catch (Exception e) {
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

                try {
                    if (result.equals("")) {
                        count = 0;
                    } else {
                        //   Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String res = jsonObject.getString("redeem");
                            // String message = jsonObject.getString("User");
                            //  Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                            if (res == null) {
                                count = 0;
                            } else {
                                JSONArray jsonArrayChanged = jsonObject.getJSONArray("redeem");
                                if (jsonArrayChanged.length() == 0) {
                                    count = 0;
                                } else {
                                    count = jsonArrayChanged.length();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    //voucher.setText(String.valueOf(count));
                }catch (Exception e){}
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
                //Offer.setText(String.valueOf(OfferCount));
            }
        }

        // Initialize the AsyncTask class
        HttpGetAsyncTask httpGetAsyncTask = new HttpGetAsyncTask();
        // Parameter we pass in the execute() method is relate to the first generic type of the AsyncTask
        // We are passing the connectWithHttpGet() method arguments to that
        httpGetAsyncTask.execute();

    }
    private class Upload extends AsyncTask<Void,Void,String> {
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
            detail.put("id", userId);
            detail.put("dob", date2);
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
                Log.e("","ERROR  "+e);
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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
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
}
