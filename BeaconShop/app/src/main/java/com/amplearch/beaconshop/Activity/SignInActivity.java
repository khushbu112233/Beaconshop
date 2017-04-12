package com.amplearch.beaconshop.Activity;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.Utils.CallSoap;
import com.amplearch.beaconshop.Utils.Caller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity
{
    TextView tvSignIn;
    EditText etEmailAdd, etPass, etUsername;
    public static String rslt="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        final  AlertDialog ad=new AlertDialog.Builder(this).create();
        etEmailAdd = (EditText)findViewById(R.id.etEmailAdd);
        etPass = (EditText)findViewById(R.id.etPass);
        etUsername = (EditText)findViewById(R.id.etUserName);

        tvSignIn = (TextView)findViewById(R.id.tvSignIn);
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String email = etEmailAdd.getText().toString();
                String pass = etPass.getText().toString();
                String name = etUsername.getText().toString();

                //CallSoap cs=new CallSoap();

//                if(!isValidUserName(name)){
//                    etUsername.setError("at least 3 characters.");
//                }
//
//                else if (!isValidEmail(email)) {
//                    etEmailAdd.setError("Email Id is not in Valid Format.");
//                }
//
//                else if (!isValidPassword(pass)) {
//                    etPass.setError("Minimum 5 characters.");
//                }
//
//                else
//                {
//                    try
//                    {
//
//                        rslt="START";
//                        Caller c=new Caller(); c.uname=email;
//                        c.pwd=pass;
//                        c.join(); c.start();
//                        while(rslt=="START") {
//                            try {
//                                Thread.sleep(10);
//                            }catch(Exception ex) {
//                            }
//                        }
//                        ad.setTitle("RESULT OF ");
//                        ad.setMessage(rslt);
//                    }catch(Exception ex) {
//                        ad.setTitle("Error!"); ad.setMessage(ex.toString());
//                    }
//                    ad.show();

                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();

//                }
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
}
