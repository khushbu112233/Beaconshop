package com.amplearch.beaconshop.Activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplearch.beaconshop.R;
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

public class ChangePasswordActivity extends AppCompatActivity
{
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        session = new UserSessionManager(getApplicationContext());
//        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isUserLoggedIn(), Toast.LENGTH_LONG).show();

        // get user data from session
        final HashMap<String, String> user1 = session.getUserDetails();
        final String login_email = user1.get(UserSessionManager.KEY_EMAIL);
//        Toast.makeText(getApplicationContext(), "Login Email " + login_email, Toast.LENGTH_LONG).show();

        final EditText etChEmail = (EditText)findViewById(R.id.etChEmail);
        final EditText etChOldPassword = (EditText)findViewById(R.id.etChOldPassword);
        final EditText etChNewPassword = (EditText)findViewById(R.id.etChNewPassword);
        Button btnChange = (Button) findViewById(R.id.btnChange);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String email_id = etChEmail.getText().toString();
                String oldPassword = etChOldPassword.getText().toString();
                String newPassword = etChNewPassword.getText().toString();

                if(!email_id.equals(login_email))
                {
                    etChEmail.setError("You using wrong EMAIL");
                }
                else {
                    etChEmail.setError(null);
                    changePassword(email_id,oldPassword,newPassword);
                }
            }
        });
    }

    private void changePassword(String email_id, String oldPassword , String newPassword)
    {
        class HttpGetAsyncTask extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground(String... params)
            {
                String paramEmail = params[0];
                String paramOldPassword = params[1];
                String paramNewPassword = params[2];
                System.out.println("paramEmail is :" + paramEmail);
                System.out.println("paramOldPassword is :" + paramOldPassword);
                System.out.println("paramNewPassword is :" + paramNewPassword);
                // Create an intermediate to connect with the Internet
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://beacon.ample-arch.com/BeaconWebService.asmx/ChangePassword");
                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                //Post Data
                List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
                nameValuePair.add(new BasicNameValuePair("email_id", paramEmail));
                nameValuePair.add(new BasicNameValuePair("oldPassword", paramOldPassword));
                nameValuePair.add(new BasicNameValuePair("newPassword", paramNewPassword));

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
                if (result.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Email Address..?", Toast.LENGTH_LONG).show();
                }
                else
                {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String res = jsonObject.getString("User");
//                        Toast.makeText(getApplicationContext(), "res: "+res, Toast.LENGTH_LONG).show();
                        if (res.equals("Invalid Credential"))
                        {
                            Toast.makeText(getApplicationContext(), "Your Old Password not correct. ", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Password has been changed, Successfully!", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        // Initialize the AsyncTask class
        HttpGetAsyncTask httpGetAsyncTask = new HttpGetAsyncTask();
        httpGetAsyncTask.execute(email_id,oldPassword,newPassword);
    }

}
