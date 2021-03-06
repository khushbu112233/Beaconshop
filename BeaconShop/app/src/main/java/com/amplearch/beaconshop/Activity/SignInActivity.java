package com.amplearch.beaconshop.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amplearch.beaconshop.ConnectivityReceiver;
import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.Utils.GillSansButton;
import com.amplearch.beaconshop.Utils.GillSansEditText;
import com.amplearch.beaconshop.Utils.GillSansTextView;
import com.amplearch.beaconshop.Utils.UserSessionManager;
import com.amplearch.beaconshop.WebCall.AsyncRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity implements AsyncRequest.OnAsyncRequestComplete
{
    GillSansTextView tvForgotPassword;
    GillSansEditText etUsername;
    EditText etEmailAdd, etPass;
    GillSansButton tvSignIn ;
    LinearLayout lnrSignup;
    public static String rslt="";
    UserSessionManager session;
    final Context context = this ;

    String loginURL = "http://beacon.ample-arch.com/BeaconWebService.asmx/Login" ;
    String forgotURL = "http://beacon.ample-arch.com/BeaconWebService.asmx/ForgotPassword";
    ArrayList<NameValuePair> params ;
    String login_Email, login_Password, login_Username, forgot_Email;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        checkConnection();
        final  AlertDialog ad=new AlertDialog.Builder(this).create();
        etEmailAdd = (EditText)findViewById(R.id.etEmailAdd);
        etPass = (EditText)findViewById(R.id.etPass);
        etUsername = (GillSansEditText) findViewById(R.id.etUserName);
        tvForgotPassword = (GillSansTextView) findViewById(R.id.tvForgotPassword);
        lnrSignup = (LinearLayout) findViewById(R.id.lnrSignup);
        session = new UserSessionManager(getApplicationContext());

       tvSignIn = (GillSansButton) findViewById(R.id.tvSignIn);


        lnrSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                login_Email = etEmailAdd.getText().toString();
                login_Password = etPass.getText().toString();
                login_Username = etUsername.getText().toString();

                //CallSoap cs=new CallSoap();

              if(!isValidUserName(login_Username)){
                    etUsername.setError("at least 3 characters.");
                }

                else if (!isValidEmail( login_Email)) {
                    etEmailAdd.setError("Email id is not in valid format.");
                }

                else if (!isValidPassword( login_Password)) {
                    etPass.setError("Minimum 4 characters.");
                }

                else {

                  if (checkConnection())
                  {
                      params = getParams();
                      AsyncRequest getPosts = new AsyncRequest(SignInActivity.this, "GET", params);
                      getPosts.execute(loginURL);
                  }
              }
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder inputEmail = new AlertDialog.Builder(context);
                inputEmail.setTitle("Forgot Password?");
                inputEmail.setMessage("Please! Enter your email id.");
                // for email edittext
                final EditText etInputEmail = new EditText(context);
                etInputEmail.setHint("ample-arch@gmail.com");
                inputEmail.setView(etInputEmail);
                //for positive response
                inputEmail.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        forgot_Email = etInputEmail.getEditableText().toString();
                        if (checkConnection()==true)
                        {
                            params = getForgotParams();
                            AsyncRequest getPosts = new AsyncRequest(SignInActivity.this, "GET", params);
                            getPosts.execute(forgotURL);
                        }
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
        if (pass != null && pass.length() > 3) {
            return true;
        }
        return false;
    }

    private ArrayList<NameValuePair> getForgotParams() {
        // define and ArrayList whose elements are of type NameValuePair
        ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        nameValuePair.add(new BasicNameValuePair("email_id", forgot_Email));
        return nameValuePair;
    }
    private ArrayList<NameValuePair> getParams() {
        // define and ArrayList whose elements are of type NameValuePair
        ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        nameValuePair.add(new BasicNameValuePair("uname", login_Email));
        nameValuePair.add(new BasicNameValuePair("type", "simple"));
        nameValuePair.add(new BasicNameValuePair("pwd", login_Password));
        return nameValuePair;
    }

    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
        return isConnected;
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

    @Override
    public void asyncResponse(String response)
    {
//      Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
      Log.i("SignIn response: ", response);

        if (response.equals(""))
        {
            Toast.makeText(getApplicationContext(), "Attempt failed..", Toast.LENGTH_LONG).show();
        }
        else
        {
         try
         {
                JSONObject jsonObject = new JSONObject(response);
                String res = jsonObject.getString("User");
                 Log.i("SignIn response: ", res);

//             Toast.makeText(getApplicationContext(),"Res_login: "+res,Toast.LENGTH_LONG ).show();
                if (res.equals("Invalid Credential")){

                    Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                }
                else
                {
                    JSONArray jsonArrayChanged = jsonObject.getJSONArray("User");

                    String email_id = jsonArrayChanged.getJSONObject(0).get("email_id").toString();
                    String user_id = jsonArrayChanged.getJSONObject(0).get("id").toString();
                    String username = jsonArrayChanged.getJSONObject(0).get("username").toString();
                    String image = jsonArrayChanged.getJSONObject(0).get("image").toString();
                    String dob = jsonArrayChanged.getJSONObject(0).get("dob").toString();
                    String gender = jsonArrayChanged.getJSONObject(0).get("gender").toString();
                    String mob = jsonArrayChanged.getJSONObject(0).get("contact").toString();

                    String password = jsonArrayChanged.getJSONObject(0).get("password").toString();

                    Toast.makeText(getApplicationContext(),"Logged In: "+ email_id + " Successfully..", Toast.LENGTH_LONG).show();
                    session.createUserLoginSession(username, email_id, image, password, user_id,mob);

                    Intent intent=new Intent(SignInActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // for forgot password
            try
            {
                JSONObject jsonObject = new JSONObject(response);
                String res = jsonObject.getString("message");
                Log.e("SignIn response: ", res);
//                Toast.makeText(getApplicationContext(), "res: "+res, Toast.LENGTH_LONG).show();

                if (res.equals("This email address does not match our records."))
                {
                    Toast.makeText(getApplicationContext(), "This email address does not exists..", Toast.LENGTH_LONG).show();
                }
                else if (res.equalsIgnoreCase("Password has been sent to your email address.")){
                    Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
