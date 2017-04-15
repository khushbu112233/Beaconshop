package com.amplearch.beaconshop.Fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplearch.beaconshop.Activity.ChangePasswordActivity;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUsFragment extends Fragment
{
    Context context;
    public AboutUsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_about_us, container, false);

        Button btnChangePassword = (Button)rootView.findViewById(R.id.btnChangePassword);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(i);
                //for alert box
//                AlertDialog.Builder input = new AlertDialog.Builder(rootView.getContext());
//                input.setTitle("Change Password?");
////                input.setMessage("Please! Enter your Emial Id.");
//                // for edittext
//                final EditText etEmail = new EditText(rootView.getContext());
//                etEmail.setHint("ample-arch@gmail.com");
//                final EditText etOldPassword = new EditText(rootView.getContext());
//                etOldPassword.setHint("Old Password");
//                final EditText etNewPassword = new EditText(rootView.getContext());
//                etNewPassword.setHint("New Password");
//                //for positive button
//                input.setPositiveButton("OK", new DialogInterface.OnClickListener(){
//                    @Override
//                    public void onClick(DialogInterface dialog, int which)
//                    {
//                        String email_id = etEmail.getEditableText().toString();
//                        String oldPassword = etOldPassword.getEditableText().toString();
//                        String  newPassword= etNewPassword.getEditableText().toString();
//
//                    }
//                });
//                input.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//                AlertDialog alertDialog = input.create();
//                alertDialog.show();
            }
        });

        return rootView;
    }


}
