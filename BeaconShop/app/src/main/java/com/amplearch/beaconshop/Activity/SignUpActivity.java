package com.amplearch.beaconshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amplearch.beaconshop.ConnectivityReceiver;
import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.WebCall.AsyncRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.amplearch.beaconshop.Utils.Utility.validate;

public class SignUpActivity extends AppCompatActivity implements AsyncRequest.OnAsyncRequestComplete
{
    public static EditText etUserName, etEmailAddress, etContactNo, etPassword, etRePassword;
    TextView tvSignup ;
    String apiURL = "http://beacon.ample-arch.com/BeaconWebService.asmx/RegisterUser" ;
    ArrayList<NameValuePair> params ;
    String UserName, EmailAddress, ContactNo, Password, RePassword, tType;
    LinearLayout lnrSignIn;
    Spinner spnCountry, spnState;
    LinearLayout llStateBox;
    String final_country, final_state;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        checkConnection();
        spnCountry = (Spinner) findViewById(R.id.spnCountry);
        spnState = (Spinner) findViewById(R.id.spnState);
        etEmailAddress = (EditText) findViewById(R.id.etEmailAddress);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etRePassword = (EditText) findViewById(R.id.etRePassword);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etContactNo = (EditText)findViewById(R.id.etContactNo);
        tvSignup = (TextView) findViewById(R.id.tvSignUp);
        lnrSignIn = (LinearLayout) findViewById(R.id.lnrSignIn);
        llStateBox = (LinearLayout) findViewById(R.id.llStateBox);

        ArrayAdapter<CharSequence> adapterCountry = ArrayAdapter.createFromResource(this,
                R.array.country_array, R.layout.spinner_item);
// Specify the layout to use when the list of choices appears
        adapterCountry.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spnCountry.setAdapter(adapterCountry);


        ArrayAdapter<CharSequence> adapterState = ArrayAdapter.createFromResource(SignUpActivity.this,
                R.array.canada_state_array, R.layout.spinner_item);
// Specify the layout to use when the list of choices appears
        adapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spnState.setAdapter(adapterState);

        spnCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String country = parent.getItemAtPosition(position).toString();
                if (country.equalsIgnoreCase("Canada")){

                    llStateBox.setVisibility(View.VISIBLE);
                }
                else if (country.equalsIgnoreCase("Singapore")){

                    llStateBox.setVisibility(View.GONE);
                }
                else if (country.equalsIgnoreCase("Select Country")){
                    llStateBox.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

                final_country = spnCountry.getSelectedItem().toString();
                final_state = spnState.getSelectedItem().toString();
                if (!validate(UserName,EmailAddress,ContactNo,Password,RePassword))
                {
                    Toast.makeText(getApplicationContext(), "Form fill invalid!", Toast.LENGTH_SHORT).show();
                } else if (final_country.equalsIgnoreCase("Select Country")){
                    Toast.makeText(getApplicationContext(), "Please select country..", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (checkConnection() == true)
                    {

                        if (final_country.equalsIgnoreCase("Canada")) {
                            if (final_state.equalsIgnoreCase("Select Province")) {
                                Toast.makeText(getApplicationContext(), "Please select province for canada..", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        if (final_country.equalsIgnoreCase("Singapore")){
                            final_state = "Singapore";
                        }
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
        nameValuePair.add(new BasicNameValuePair("country", final_country));
        nameValuePair.add(new BasicNameValuePair("state", final_state));
        return nameValuePair;
    }

    @Override
    public void asyncResponse(String response)
    {
       // Toast.makeText(getApplicationContext(), "Response "+response, Toast.LENGTH_LONG).show();
        Log.i("SignUp Response ", response);

        if (response.equals(""))
        {
            Toast.makeText(getApplicationContext(), "SignUp attempt failed..", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getApplicationContext(), "Successfully registered!", Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(SignUpActivity.this,SignInActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Not registered!", Toast.LENGTH_LONG).show();
//                            session.createUserLoginSession(username, email_id, image, password, user_id);
                    etEmailAddress.setError("Email id, Already exist!");
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