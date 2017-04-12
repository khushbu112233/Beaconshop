package com.amplearch.beaconshop.Utils;

import com.amplearch.beaconshop.Activity.SignInActivity;
import com.amplearch.beaconshop.Activity.SignUpActivity;

import java.lang.ref.SoftReference;

/**
 * Created by admin on 04/04/2017.
 */

public class Caller extends Thread
{
    public CallSoap cs;
    public String uname,pwd;

    public void run(){
        try{
            cs=new CallSoap();
           // String resp=cs.Call(uname, pwd);
           // SignInActivity.rslt=resp;
        }catch(Exception ex)
        {
            SignInActivity.rslt=ex.toString();}
    }
}