package com.amplearch.beaconshop.Fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplearch.beaconshop.Activity.SignInActivity;
import com.amplearch.beaconshop.ConnectivityReceiver;
import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.Utils.UserSessionManager;
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
 * Created by ample-arch on 4/15/2017.
 */

public class ChangePasswordFragment extends Fragment implements AsyncRequest.OnAsyncRequestComplete
{
    public ChangePasswordFragment() { }

    EditText etChEmail, etChOldPassword, etChNewPassword;
    Button btnChange;

    UserSessionManager session;
    String changePasswordURL = "http://beacon.ample-arch.com/BeaconWebService.asmx/ChangePassword" ;
    ArrayList<NameValuePair> params ;
    String user_Email, user_OldPassword, user_NewPassword ;
    String email, password, userId, name;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_changepassword, container, false);
        checkConnection();
        session = new UserSessionManager(getContext());
        // get user data from session
        final HashMap<String, String> user1 = session.getUserDetails();
        final String login_email = user1.get(UserSessionManager.KEY_EMAIL);

        email = user1.get(UserSessionManager.KEY_EMAIL);
        name = user1.get(UserSessionManager.KEY_NAME);
        password = user1.get(UserSessionManager.KEY_PASSWORD);
        userId = user1.get(UserSessionManager.KEY_USER_ID);

         etChEmail = (EditText)rootView.findViewById(R.id.etChEmail);
         etChOldPassword = (EditText)rootView.findViewById(R.id.etChOldPassword);
         etChNewPassword = (EditText)rootView.findViewById(R.id.etChNewPassword);
         btnChange = (Button)rootView.findViewById(R.id.btnChange);



        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                 user_Email = etChEmail.getText().toString();
                 user_OldPassword = etChOldPassword.getText().toString();
                 user_NewPassword = etChNewPassword.getText().toString();

                if(!user_Email.equals(login_email))
                {
                    etChEmail.setError("You using wrong EMAIL");
                }
                else {
                    etChEmail.setError(null);
                    if (checkConnection() == true)
                    {
                        params = getParams();
                        AsyncRequest getPosts = new AsyncRequest(ChangePasswordFragment.this,getActivity(), "GET", params, "");
                        getPosts.execute(changePasswordURL);
                    }
                }
            }
        });
        return rootView;


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
}
