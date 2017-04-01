package com.amplearch.beaconshop.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amplearch.beaconshop.R;

import static com.amplearch.beaconshop.Utils.Utility.validate;

public class SignUpActivity extends AppCompatActivity {

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

                if (!validate(UserName,EmailAddress,ContactNo,Password,RePassword))
                {

                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(getApplicationContext(), "you can go ", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}
