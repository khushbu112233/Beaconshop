package com.amplearch.beaconshop.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.Utils.CallSoap;
import com.amplearch.beaconshop.Utils.Caller;
import com.amplearch.beaconshop.Utils.TrojanButton;
import com.amplearch.beaconshop.Utils.TrojanEditText;
import com.amplearch.beaconshop.Utils.TrojanText;
import com.amplearch.beaconshop.Utils.UserSessionManager;

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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity
{
    TrojanText tvForgotPassword;
    TrojanEditText etEmailAdd, etPass, etUsername;
    TrojanButton tvSignIn ;
    public static String rslt="";
    UserSessionManager session;

    final Context context = this ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        final  AlertDialog ad=new AlertDialog.Builder(this).create();
        etEmailAdd = (TrojanEditText)findViewById(R.id.etEmailAdd);
        etPass = (TrojanEditText)findViewById(R.id.etPass);
        etUsername = (TrojanEditText)findViewById(R.id.etUserName);
        tvForgotPassword = (TrojanText)findViewById(R.id.tvForgotPassword);
        session = new UserSessionManager(getApplicationContext());

        tvSignIn = (TrojanButton) findViewById(R.id.tvSignIn);
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String email = etEmailAdd.getText().toString();
                String pass = etPass.getText().toString();
                String name = etUsername.getText().toString();

                //CallSoap cs=new CallSoap();

              if(!isValidUserName(name)){
                    etUsername.setError("at least 3 characters.");
                }

                else if (!isValidEmail(email)) {
                    etEmailAdd.setError("Email Id is not in Valid Format.");
                }

                else if (!isValidPassword(pass)) {
                    etPass.setError("Minimum 5 characters.");
                }

                else {
                  connectWithHttpPost(email, pass);
              }
//                startActivity(new Intent(getApplicationContext(),MainActivity.class));
//                finish();
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder inputEmail = new AlertDialog.Builder(context);
                inputEmail.setTitle("Forgot Password?");
                inputEmail.setMessage("Please! Enter your Emial Id.");
                // for email edittext
                final EditText etInputEmail = new EditText(context);
                etInputEmail.setHint("ample-arch@gmail.com");
                inputEmail.setView(etInputEmail);
                //for positive response
                inputEmail.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        String email_id = etInputEmail.getEditableText().toString();
                            forgotpassword(email_id);
                    }
                });
                inputEmail.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                    }
                });
                AlertDialog alertDialog = inputEmail.create();
                alertDialog.show();
            }
        });


    }
    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidUserName(String name){
        if(name != null && name.length() > 2)
        {
            return true ;
        }
        return false ;
    }

    // validating password with retype password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 4) {
            return true;
        }
        return false;
    }

    private void forgotpassword(String email_id)
    {
        class HttpGetAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                String paramEmail = params[0];
                System.out.println("paramEmail is :" + paramEmail);
                // Create an intermediate to connect with the Internet
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://beacon.ample-arch.com/BeaconWebService.asmx/ForgotPassword");
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                //Post Data
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
                nameValuePair.add(new BasicNameValuePair("email_id", paramEmail));

                //Encoding POST data
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                } catch (UnsupportedEncodingException e) {
                    // log exception
                    e.printStackTrace();
                }
                try
                {
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    System.out.println("httpResponse");
                    InputStream inputStream = httpResponse.getEntity().getContent();
                    // We have a byte stream. Next step is to convert it to a Character stream
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    // Then we have to wraps the existing reader (InputStreamReader) and buffer the input
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    // InputStreamReader contains a buffer of bytes read from the source stream and converts these into characters as needed.
                    StringBuilder stringBuilder = new StringBuilder();
                    String bufferedStrChunk = null;

                    while((bufferedStrChunk = bufferedReader.readLine()) != null){
                        stringBuilder.append(bufferedStrChunk);
                    }
                    System.out.println("Returning value of doInBackground :" + stringBuilder.toString());

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

            @Override
            protected void onPostExecute(String result)
            {
                super.onPostExecute(result);
//                Toast.makeText(getApplicationContext(),"Result: "+result,Toast.LENGTH_LONG).show();
                if (result.equals(" "))
                {
                    Toast.makeText(getApplicationContext(), "Email Address..?", Toast.LENGTH_LONG).show();
                }
                else {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String res = jsonObject.getString("message");
//                        Toast.makeText(getApplicationContext(), "res: "+res, Toast.LENGTH_LONG).show();
                        if (res.equals("This email address does not match our records."))
                        {
                            Toast.makeText(getApplicationContext(), "This email address does not EXISTS..", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Password Has been sent to email id.", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        // Initialize the AsyncTask class
        HttpGetAsyncTask httpGetAsyncTask = new HttpGetAsyncTask();
        httpGetAsyncTask.execute(email_id);
    }

    private void connectWithHttpPost(String givenUsername, String givenPassword)
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
                String paramUsername = params[0];
                String paramPassword = params[1];
                System.out.println("paramUsername is :" + paramUsername + " paramPassword is :" + paramPassword);

                // Create an intermediate to connect with the Internet
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://beacon.ample-arch.com/BeaconWebService.asmx/Login");
                httpPost.setHeader(HTTP.CONTENT_TYPE,
                        "application/x-www-form-urlencoded;charset=UTF-8");

                //Post Data
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(3);
                nameValuePair.add(new BasicNameValuePair("uname", paramUsername));
                nameValuePair.add(new BasicNameValuePair("type", "simple"));
                nameValuePair.add(new BasicNameValuePair("pwd", paramPassword));


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
                    Toast.makeText(getApplicationContext(), "Login attempt Failed..", Toast.LENGTH_LONG).show();
                }else {
                    // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String res = jsonObject.getString("User");
                        // String message = jsonObject.getString("User");
                        //  Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                        if (res.equals("Invalid Credential")){

                            Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                        }
                        else {

                            JSONArray jsonArrayChanged = jsonObject.getJSONArray("User");

                            String email_id = jsonArrayChanged.getJSONObject(0).get("email_id").toString();
                            String user_id = jsonArrayChanged.getJSONObject(0).get("id").toString();
                            String username = jsonArrayChanged.getJSONObject(0).get("username").toString();
                            String image = jsonArrayChanged.getJSONObject(0).get("image").toString();
                            String dob = jsonArrayChanged.getJSONObject(0).get("dob").toString();
                            String gender = jsonArrayChanged.getJSONObject(0).get("gender").toString();
                            String password = jsonArrayChanged.getJSONObject(0).get("password").toString();

                            Toast.makeText(getApplicationContext(), email_id, Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(), "LoggedIn Successfully..", Toast.LENGTH_LONG).show();
                            session.createUserLoginSession(username, email_id, image, password, user_id);

                            Intent intent=new Intent(SignInActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
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
        httpGetAsyncTask.execute(givenUsername, givenPassword);

    }

    private void makePostRequest(String email, String pass) {


        HttpClient httpClient = new DefaultHttpClient();
        // replace with your url
        HttpPost httpPost = new HttpPost("http://beacon.ample-arch.com/BeaconWebService.asmx/Login");
        httpPost.setHeader(HTTP.CONTENT_TYPE,
                "application/x-www-form-urlencoded;charset=UTF-8");

        //Post Data
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(3);
        nameValuePair.add(new BasicNameValuePair("uname", email));
        nameValuePair.add(new BasicNameValuePair("type", "simple"));
        nameValuePair.add(new BasicNameValuePair("pwd", pass));


        //Encoding POST data
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            // log exception
            e.printStackTrace();
        }

        //making POST request.
        try {
            HttpResponse response = httpClient.execute(httpPost);
            // write response to log
            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
            Log.d("Http Post Response:", response.toString());
        } catch (ClientProtocolException e) {
            // Log exception
            e.printStackTrace();
        } catch (IOException e) {
            // Log exception
            e.printStackTrace();
        }

    }

}
