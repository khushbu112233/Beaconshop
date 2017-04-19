package com.amplearch.beaconshop.ApplicationUtils;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.amplearch.beaconshop.ConnectivityReceiver;
import com.amplearch.beaconshop.Utils.FileHelper;
import com.facebook.FacebookSdk;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MyApplication extends Application {

    private FileHelper fileHelper;
    private BackgroundPowerSaver backgroundPowerSaver;
    private BeaconManager beaconManager;
    private Region region;

    private static MyApplication mInstance ;

    @Override
    public void onCreate()
    {
        super.onCreate();

        mInstance = this ;

        FacebookSdk.sdkInitialize(getApplicationContext());
        try
        {
            PackageInfo info = getPackageManager().getPackageInfo("com.amplearch.beaconshop", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e) { }
        catch ( NoSuchAlgorithmException e ) {  }

        fileHelper = new FileHelper(getExternalFilesDir(null));
        // Allow scanning to continue in the background.
        backgroundPowerSaver = new BackgroundPowerSaver(this);
        beaconManager = BeaconManager.getInstanceForApplication(this);


        MyApplication app = this;//(BeaconScannerApp)this.getApplication();
        //beaconManager = app.getBeaconManager();
        //beaconManager.setForegroundScanPeriod(10);

        // logToDisplay("BeaconParsers size is:" + beaconManager.getBeaconParsers().size());

        // Add parser for iBeacons;
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        // Detect the Eddystone main identifier (UID) frame:
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("s:0-1=feaa,m:2-2=00,p:3-3:-41,i:4-13,i:14-19"));
        // Detect the Eddystone telemetry (TLM) frame:
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("x,s:0-1=feaa,m:2-2=20,d:3-3,d:4-5,d:6-7,d:8-11,d:12-15"));
        // Detect the Eddystone URL frame:
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("s:0-1=feaa,m:2-2=10,p:3-3:-41,i:4-20v"));

        // Get the details for all the beacons we encounter.
        region = new Region("justGiveMeEverything", null, null, null);
    }

    public FileHelper getFileHelper() { return fileHelper; }
    public BeaconManager getBeaconManager() {
        return beaconManager;
    }
    public Region getRegion() {return region; }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener)
    {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

}
