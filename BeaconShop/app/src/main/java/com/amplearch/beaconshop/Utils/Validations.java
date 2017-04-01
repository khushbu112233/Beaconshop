package com.amplearch.beaconshop.Utils;

import com.amplearch.beaconshop.Activity.SignUpActivity;

/**
 * Created by Admin on 4/1/2017.
 */

public class Validations
{
    public Validations() {
    }

    public static boolean validate(String userName, String emailAddress, String contactNo, String password, String rePassword)
    {
        boolean valid = true ;


        if(userName.isEmpty() || userName.length() < 3 )
        {
            SignUpActivity.etUserName.setError("Minimum 3 Characters.");
            valid = false ;
        }
        else
        {
            SignUpActivity.etUserName.setError(null);
        }

        if (emailAddress.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches())
        {
            SignUpActivity. etEmailAddress.setError("Not a valid Email Address");
            valid = false;
        }
        else {
            SignUpActivity. etEmailAddress.setError(null);
        }

        if(contactNo.isEmpty() || contactNo.length() != 10  )
        {
            SignUpActivity.  etContactNo.setError("10 Characters Required");
            valid = false ;
        }
        else if(contactNo.length() > 10){
            SignUpActivity. etContactNo.setError("10 Characters Required");
        }
        else {
            SignUpActivity.  etContactNo.setError(null);
        }

        if (password.isEmpty() || password.length() < 4)
        {
            SignUpActivity.  etPassword.setError("Minimum 4 Characters");
            valid = false;
        }
        else {
            SignUpActivity. etPassword.setError(null);
        }

      /*  if (rePassword.isEmpty() || rePassword.length() < 4)
        {
            SignUpActivity.etRePassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            SignUpActivity.etRePassword.setError(null);
        }*/

        if (rePassword.equals(password))
        {

            SignUpActivity.etRePassword.setError(null);
        }
        else {
            SignUpActivity.etRePassword.setError("Password does not match");
            valid = false;
        }

        return valid;
    }
}
