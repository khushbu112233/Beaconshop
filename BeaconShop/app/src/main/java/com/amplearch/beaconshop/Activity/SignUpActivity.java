package com.amplearch.beaconshop.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amplearch.beaconshop.R;

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

import static com.amplearch.beaconshop.Utils.Utility.validate;

public class SignUpActivity extends AppCompatActivity
{
    public static EditText etUserName, etEmailAddress, etContactNo, etPassword, etRePassword;
    TextView tvSignup ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etEmailAddress = (EditText) findViewById(R.id.etEmailAddress);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etRePassword = (EditText) findViewById(R.id.etRePassword);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etContactNo = (EditText)findViewById(R.id.etContactNo);

        tvSignup = (TextView) findViewById(R.id.tvSignUp);

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String UserName = etUserName.getText().toString();
                String EmailAddress = etEmailAddress.getText().toString();
                String ContactNo = etContactNo.getText().toString();
                String Password = etPassword.getText().toString();
                String RePassword = etRePassword.getText().toString();
                String Type = "simple";

                if (!validate(UserName,EmailAddress,ContactNo,Password,RePassword))
                {
                    Toast.makeText(getApplicationContext(), "Form Fill Invalid!", Toast.LENGTH_SHORT).show();


                }
                else {
                    connectWithHttpPost(UserName, EmailAddress, ContactNo, Password, Type);
                }
            }
        });
    }

    private void connectWithHttpPost(String name, String email, String contact, String pass, String type)
    {
       class HttpGetAsyncTask extends AsyncTask<String, Void, String>
       {

            @Override
            protected String doInBackground(String... params) {

                // As you can see, doInBackground has taken an Array of Strings as the argument
                //We need to specifically get the givenUsername and givenPassword
                String paramUsername = params[0];
                String paramEmail = params[1];
                String paramContact = params[2];
                String paramPassword = params[3];
                String paramType = params[4];
                System.out.println("paramUsername is :" + paramUsername + " paramPassword is :" + paramPassword);

                // Create an intermediate to connect with the Internet
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://beacon.ample-arch.com/BeaconWebService.asmx/RegisterUser");
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

                //Post Data
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(5);
                nameValuePair.add(new BasicNameValuePair("name", paramUsername));
                nameValuePair.add(new BasicNameValuePair("email", paramEmail));
                nameValuePair.add(new BasicNameValuePair("contact", paramContact));
                nameValuePair.add(new BasicNameValuePair("pass", paramPassword));
                nameValuePair.add(new BasicNameValuePair("type", paramType));


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
                    Toast.makeText(getApplicationContext(), "Register attempt Failed..", Toast.LENGTH_LONG).show();
                }else {
                    // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String res = jsonObject.getString("message");
                        // String message = jsonObject.getString("User");
                        //  Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                        if (res.equals("not exists")){
                            Toast.makeText(getApplicationContext(), "Successfully Registered: Login to your account.", Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(SignUpActivity.this,SignInActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {

//                            JSONArray jsonArrayChanged = jsonObject.getJSONArray("User");
//
//                            String email_id = jsonArrayChanged.getJSONObject(0).get("email_id").toString();
//                            String user_id = jsonArrayChanged.getJSONObject(0).get("id").toString();
//                            String username = jsonArrayChanged.getJSONObject(0).get("username").toString();
//                            String image = jsonArrayChanged.getJSONObject(0).get("image").toString();
//                            String dob = jsonArrayChanged.getJSONObject(0).get("dob").toString();
//                            String gender = jsonArrayChanged.getJSONObject(0).get("gender").toString();
//                            String password = jsonArrayChanged.getJSONObject(0).get("password").toString();

//                            Toast.makeText(getApplicationContext(), email_id, Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(), "Not registered!", Toast.LENGTH_LONG).show();
//                            session.createUserLoginSession(username, email_id, image, password, user_id);
                            etEmailAddress.setError("Email Address! Already Used.");


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
        httpGetAsyncTask.execute(name, email, contact, pass, type);

    }

}
