package com.amplearch.beaconshop.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amplearch.beaconshop.ConnectivityReceiver;
import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.WebCall.AsyncRequest;

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

public class SignUpActivity extends AppCompatActivity implements AsyncRequest.OnAsyncRequestComplete
{
    public static EditText etUserName, etEmailAddress, etContactNo, etPassword, etRePassword;
    TextView tvSignup ;
    String apiURL = "http://beacon.ample-arch.com/BeaconWebService.asmx/RegisterUser" ;
    ArrayList<NameValuePair> params ;
    String UserName, EmailAddress, ContactNo, Password, RePassword, tType;
    LinearLayout lnrSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        checkConnection();
        etEmailAddress = (EditText) findViewById(R.id.etEmailAddress);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etRePassword = (EditText) findViewById(R.id.etRePassword);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etContactNo = (EditText)findViewById(R.id.etContactNo);
        tvSignup = (TextView) findViewById(R.id.tvSignUp);
        lnrSignIn = (LinearLayout) findViewById(R.id.lnrSignIn);

        lnrSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
            }
        });

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                UserName = etUserName.getText().toString();
                EmailAddress = etEmailAddress.getText().toString();
                ContactNo = etContactNo.getText().toString();
                Password = etPassword.getText().toString();
                RePassword = etRePassword.getText().toString();
                tType = "simple";

                if (!validate(UserName,EmailAddress,ContactNo,Password,RePassword))
                {
                    Toast.makeText(getApplicationContext(), "Form Fill Invalid!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (checkConnection() == true)
                    {
                        params = getParams();
                        AsyncRequest getPosts = new AsyncRequest(SignUpActivity.this, "GET", params);
                        getPosts.execute(apiURL);
                    }

                }
            }
        });
    }

    private ArrayList<NameValuePair> getParams() {
        // define and ArrayList whose elements are of type NameValuePair
        ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        nameValuePair.add(new BasicNameValuePair("name", UserName));
        nameValuePair.add(new BasicNameValuePair("email", EmailAddress));
        nameValuePair.add(new BasicNameValuePair("contact", ContactNo));
        nameValuePair.add(new BasicNameValuePair("pass", Password));
        nameValuePair.add(new BasicNameValuePair("type", "simple"));
        return nameValuePair;
    }

    @Override
    public void asyncResponse(String response)
    {
        Toast.makeText(getApplicationContext(), "Response "+response, Toast.LENGTH_LONG).show();
        Log.i("SignUp Response ", response);

        if (response.equals(""))
        {
            Toast.makeText(getApplicationContext(), "SignUp attempt Failed..", Toast.LENGTH_LONG).show();
        }
        else
        {
            // Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try
            {
                JSONObject jsonObject = new JSONObject(response);
                String res = jsonObject.getString("message");
                // String message = jsonObject.getString("User");
                //  Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                if (res.equals("Success"))
                {
                    Toast.makeText(getApplicationContext(), "Successfully Registered!", Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(SignUpActivity.this,SignInActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Not registered!", Toast.LENGTH_LONG).show();
//                            session.createUserLoginSession(username, email_id, image, password, user_id);
                    etEmailAddress.setError("Email Address, Already Exist!");
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
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

}
