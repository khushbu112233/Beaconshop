package com.amplearch.beaconshop.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amplearch.beaconshop.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity
{
    TextView tvSignIn;
    EditText etEmailAdd, etPass;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        etEmailAdd = (EditText)findViewById(R.id.etEmailAdd);
        etPass = (EditText)findViewById(R.id.etPass);

        tvSignIn = (TextView)findViewById(R.id.tvSignIn);
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String email = etEmailAdd.getText().toString();
                String pass = etPass.getText().toString();

                if (!isValidEmail(email)) {
                    etEmailAdd.setError("Email Id is not in Valid Format.");
                }

                else if (!isValidPassword(pass)) {
                    etPass.setError("Minimum 5 characters.");
                }

                else
                {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
                }
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

    // validating password with retype password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 4) {
            return true;
        }
        return false;
    }
}
