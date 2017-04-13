package com.amplearch.beaconshop.Utils;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplearch.beaconshop.Activity.MainActivity;
import com.amplearch.beaconshop.ApplicationUtils.MyApplication;
import com.amplearch.beaconshop.Model.StoreLocation;
import com.amplearch.beaconshop.Model.Voucher;
import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.helper.DatabaseHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.utils.UrlBeaconUrlCompressor;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Grishma on 16/5/16.
 */
public class LocationUpdateService extends Service implements
          GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, BeaconConsumer {
    protected static final String TAG = "LocationUpdateService";
    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 600000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // Keys for storing activity state in the Bundle.
    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    protected final static String LOCATION_KEY = "location-key";
    protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";
    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    public static Boolean mRequestingLocationUpdates;
    /**
     * Time when the location was updated represented as a String.
     */
    protected String mLastUpdateTime;
    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;

    /**
     * Represents a geographical location.
     */
    protected Location mCurrentLocation;
    public static boolean isEnded = false;
    private ArrayList<LocationVo> mLocationData;

    double fix_Latitude, fix_Longitude;
    double cur_Latitude, cur_Longitude;
    double dist, theta;
    int dist_int;
    float dist_float;
    double dist_double;


    private static final String PREFERENCE_SCANINTERVAL = "scanInterval";
    private static final String PREFERENCE_TIMESTAMP = "timestamp";
    private static final String PREFERENCE_POWER = "power";
    private static final String PREFERENCE_PROXIMITY = "proximity";
    private static final String PREFERENCE_RSSI = "rssi";
    private static final String PREFERENCE_MAJORMINOR = "majorMinor";
    private static final String PREFERENCE_UUID = "uuid";
    private static final String PREFERENCE_INDEX = "index";
    private static final String PREFERENCE_LOCATION = "location";
    private static final String PREFERENCE_REALTIME = "realTimeLog";
    private static final String MODE_SCANNING = "Stop Scanning";
    private static final String MODE_STOPPED = "Start Scanning";

    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    private final static int
            CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private FileHelper fileHelper;
    private BeaconManager beaconManager;
    private Region region;
    private int eventNum = 1;

    // This StringBuffer will hold the scan data for any given scan.
    private StringBuffer logString;

    // Preferences - will actually have a boolean value when loaded.
    private Boolean index;
    private Boolean location;
    private Boolean uuid;
    private Boolean majorMinor;
    private Boolean rssi;
    private Boolean proximity;
    private Boolean power;
    private Boolean timestamp;
    private String scanInterval;
    // Added following a feature request from D.Schmid.
    private Boolean realTimeLog;

    private EditText editText;
    DatabaseHelper db;

    @Override
    public void onCreate() {
        super.onCreate();
      // verifyBluetooth();
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        MyApplication app = (MyApplication) this.getApplication();
        beaconManager = app.getBeaconManager();
        //beaconManager.setForegroundScanPeriod(10);
        region = app.getRegion();
        beaconManager.bind(this);
        // locationClient = new LocationClient(this, this, this);
        fileHelper = app.getFileHelper();

        editText = new EditText(getApplicationContext());
        // Initialise scan button.
        getScanButton().setText(MODE_STOPPED);
        // toggleScanState();
        // Kick off the process of building a GoogleApiClient and requesting the LocationServices
        // API.
    }

    private void verifyBluetooth() {

        try {
            if (!BeaconManager.getInstanceForApplication(this).checkAvailability()) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Bluetooth not enabled");
                builder.setMessage("Please enable bluetooth in settings and restart this application.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        //System.exit(0);
                    }
                });
                builder.show();
            }
        } catch (RuntimeException e) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Bluetooth LE not available");
            builder.setMessage("Sorry, this device does not support Bluetooth LE.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    //  finish();
                    //  System.exit(0);
                }

            });
            builder.show();

        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.
        db = new DatabaseHelper(getApplicationContext());
        Log.d("LOC", "Service init...");
        isEnded = false;
        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";
        buildGoogleApiClient();
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }

        return Service.START_REDELIVER_INTENT;
    }

    Button btn;

    private Button getScanButton() {
        return btn = new Button(getApplicationContext());
    }


    private void startScanning(Button scanButton) {

        // Set UI elements to the correct state.
        scanButton.setText(MODE_SCANNING);
        //((EditText)findViewById(R.id.scanText)).setText("");

        // Reset event counter
        eventNum = 1;
        // Get current values for logging preferences
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        HashMap<String, Object> prefs = new HashMap<String, Object>();
        prefs.putAll(sharedPrefs.getAll());

        index = (Boolean) prefs.get(PREFERENCE_INDEX);
        location = (Boolean) prefs.get(PREFERENCE_LOCATION);
        uuid = (Boolean) prefs.get(PREFERENCE_UUID);
        majorMinor = (Boolean) prefs.get(PREFERENCE_MAJORMINOR);
        rssi = (Boolean) prefs.get(PREFERENCE_RSSI);
        proximity = (Boolean) prefs.get(PREFERENCE_PROXIMITY);
        power = (Boolean) prefs.get(PREFERENCE_POWER);
        timestamp = (Boolean) prefs.get(PREFERENCE_TIMESTAMP);
        scanInterval = (String) prefs.get(PREFERENCE_SCANINTERVAL);
        realTimeLog = (Boolean) prefs.get(PREFERENCE_REALTIME);

        // Get current background scan interval (if specified)
        if (prefs.get(PREFERENCE_SCANINTERVAL) != null) {
            beaconManager.setBackgroundBetweenScanPeriod(Long.parseLong(scanInterval));
        }

        logToDisplay("Scanning...");

        // Initialise scan log
        logString = new StringBuffer();

        //Start scanning again.
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    Iterator<Beacon> beaconIterator = beacons.iterator();
                    while (beaconIterator.hasNext()) {
                        Beacon beacon = beaconIterator.next();
                        // Debug - logging a beacon - checking background logging is working.
                        System.out.println("Logging another beacon.");

                        List<Voucher> allTags = db.getAllBeaconVouchers();
                        for (Voucher tag : allTags) {
                            Log.d("Voucher Details", tag.getUuid());
                            int mId = Integer.parseInt(String.valueOf(tag.getId()));
                            if (beacon.getId1().toString().equalsIgnoreCase(tag.getUuid())){
                                NotificationCompat.Builder mBuilder =
                                        new NotificationCompat.Builder(getApplicationContext())
                                                .setSmallIcon(R.drawable.ic_audiotrack)
                                                .setContentTitle(tag.getMessage() + " at " + tag.getStore_name())
                                                .setContentText(tag.getOffer_desc());
// Creates an explicit intent for an Activity in your app
                                NotificationManager mNotificationManager =
                                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
                                mNotificationManager.notify(mId, mBuilder.build());
                            }
                        }

                        logBeaconData(beacon);
                    }
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            // TODO - OK, what now then?
        }

    }

    private void logBeaconData(Beacon beacon) {

        StringBuilder scanString = new StringBuilder();

        if (index) {
            scanString.append(eventNum++);
        }

        if (beacon.getServiceUuid() == 0xfeaa) {

            if (beacon.getBeaconTypeCode() == 0x00) {

                scanString.append(" Eddystone-UID -> ");
                scanString.append(" Namespace : ").append(beacon.getId1());
                scanString.append(" Identifier : ").append(beacon.getId2());

                logEddystoneTelemetry(scanString, beacon);

            } else if (beacon.getBeaconTypeCode() == 0x10) {

                String url = UrlBeaconUrlCompressor.uncompress(beacon.getId1().toByteArray());
                scanString.append(" Eddystone-URL -> " + url);

            } else if (beacon.getBeaconTypeCode() == 0x20) {

                scanString.append(" Eddystone-TLM -> ");
                logEddystoneTelemetry(scanString, beacon);

            }

        } else {

            // Just an old fashioned iBeacon or AltBeacon...
            logGenericBeacon(scanString, beacon);

        }

        logToDisplay(scanString.toString());
        Toast.makeText(getApplicationContext(), scanString.toString() + "\n", Toast.LENGTH_LONG).show();

        scanString.append("\n");

        // Code added following a feature request by D.Schmid - writes a single entry to a file
        // every time a beacon is detected, the file will only ever have one entry as it will be
        // recreated on each call to this method.
        // Get current background scan interval (if specified)
        if (realTimeLog) {
            // We're in realtime logging mode, create a new log file containing only this entry.
            fileHelper.createFile(scanString.toString(), "realtimelog.txt");
        }

        logString.append(scanString.toString());

    }

    private void logGenericBeacon(StringBuilder scanString, Beacon beacon) {
        if (location) {
            scanString.append(" Location: ").append(mCurrentLocation.getLatitude()).append(" ");
        }

        if (uuid) {
            scanString.append(" UUID: ").append(beacon.getId1());
        }

        if (majorMinor) {
            scanString.append(" Maj. Mnr.: ");
            if (beacon.getId2() != null) {
                scanString.append(beacon.getId2());
            }
            scanString.append("-");
            if (beacon.getId3() != null) {
                scanString.append(beacon.getId3());
            }
        }

        if (rssi) {
            scanString.append(" RSSI: ").append(beacon.getRssi());
        }

        if (proximity) {
            scanString.append(" Proximity: ").append(BeaconHelper.getProximityString(beacon.getDistance()));
        }

        if (power) {
            scanString.append(" Power: ").append(beacon.getTxPower());
        }

        if (timestamp) {
            scanString.append(" Timestamp: ").append(BeaconHelper.getCurrentTimeStamp());
        }
    }

    private void logEddystoneTelemetry(StringBuilder scanString, Beacon beacon) {
        // Do we have telemetry data?
        if (beacon.getExtraDataFields().size() > 0) {
            long telemetryVersion = beacon.getExtraDataFields().get(0);
            long batteryMilliVolts = beacon.getExtraDataFields().get(1);
            long pduCount = beacon.getExtraDataFields().get(3);
            long uptime = beacon.getExtraDataFields().get(4);

            scanString.append(" Telemetry version : " + telemetryVersion);
            scanString.append(" Uptime (sec) : " + uptime);
            scanString.append(" Battery level (mv) " + batteryMilliVolts);
            scanString.append(" Tx count: " + pduCount);
        }
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection beacons, Region region) {
                if (beacons.size() > 0) {

                    Iterator<Beacon> beaconIterator = beacons.iterator();
                    while (beaconIterator.hasNext()) {
                        Beacon beacon = beaconIterator.next();
                        // Debug - logging a beacon - checking background logging is working.
                        System.out.println("Logging another beacon.");
                        //   logBeaconData(beacon);
                        Log.i(TAG, "The first beacon I see is about " + beacon.getDistance() + " meters away.");

                        Toast.makeText(getApplicationContext(), "The first beacon I see is about " + beacon.getDistance() + " meters away.", Toast.LENGTH_LONG).show();

                        List<Voucher> allTags = db.getAllBeaconVouchers();
                        for (Voucher tag : allTags) {
                            Log.d("Voucher Details", tag.getUuid());
                            int mId = Integer.parseInt(String.valueOf(tag.getId()));
                            if (beacon.getId1().toString().equalsIgnoreCase(tag.getUuid())){
                                NotificationCompat.Builder mBuilder =
                                        new NotificationCompat.Builder(getApplicationContext())
                                                .setSmallIcon(R.drawable.ic_audiotrack)
                                                .setContentTitle(tag.getMessage() + " at " + tag.getStore_name())
                                                .setContentText(tag.getOffer_desc());
// Creates an explicit intent for an Activity in your app
                                NotificationManager mNotificationManager =
                                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
                                mNotificationManager.notify(mId, mBuilder.build());
                            }
                        }
                    }

                    Log.i(TAG, "The first beacon I see is about " + beacons.toString() + " meters away.");
                    // logBeaconData(beacons);
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void logToDisplay(final String line) {
        new MainActivity().runOnUiThread(new Runnable() {
            public void run() {

                Toast.makeText(getApplicationContext(), line + "\n", Toast.LENGTH_LONG).show();
                //  editText.append(line + "\n");

                // Temp code - don't really want to do this for every line logged, will look for a
                // workaround.
                //Linkify.addLinks(editText, Linkify.WEB_URLS);

                //  scroller.fullScroll(View.FOCUS_DOWN);

            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended==");
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
        Toast.makeText(this, getResources().getString(R.string.location_updated_message),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient===");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        createLocationRequest();
    }

    public void setLocationData() {

        mLocationData = new ArrayList<>();
        LocationVo mLocVo = new LocationVo();
        mLocVo.setmLongitude(mCurrentLocation.getLongitude());
        mLocVo.setmLatitude(mCurrentLocation.getLatitude());
        Log.e("service : ", mCurrentLocation.getLongitude() + "  " + mCurrentLocation.getLatitude());
        mLocVo.setmLocAddress(Const.getCompleteAddressString(this, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
        mLocationData.add(mLocVo);
        cal_Distance();

    }

    /**
     * Updates the latitude, the longitude, and the last location time in the UI.
     */
    private void updateUI() {
        setLocationData();
        Toast.makeText(this, "Latitude: =" + mCurrentLocation.getLatitude() + " Longitude:=" + mCurrentLocation
                .getLongitude(), Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Latitude:==" + mCurrentLocation.getLatitude() + "\n Longitude:==" + mCurrentLocation.getLongitude
                ());

     //   LocationDBHelper.getInstance(this).insertLocationDetails(mLocationData);
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    protected void createLocationRequest() {
        mGoogleApiClient.connect();
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;

            // The final argument to {@code requestLocationUpdates()} is a LocationListener
            // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
            Log.i(TAG, " startLocationUpdates===");
            isEnded = true;

        }
    }

    //String URL = "content://com.amplearch.beaconshop.StoreLocations";
    void cal_Distance() {
        cur_Latitude = mCurrentLocation.getLatitude();
        cur_Longitude = mCurrentLocation.getLongitude();


        String res = null;
        //String[] proj = { MediaStore.Images.Media.DATA };
        // Cursor cursor = getContentResolver().query(Uri.parse(URL), null, null, null, null);
        List<StoreLocation> allTags = db.getAllLocations();
        for (StoreLocation tag : allTags) {
            Log.d("StoreLocation Name", tag.getStore_name());
          //  Toast.makeText(getApplicationContext(), tag.getStore_name() + " " + tag.getOffer_title(), Toast.LENGTH_LONG).show();

                /*Toast.makeText(this,
                        cursor.getString(cursor.getColumnIndex(StoreLocations._ID)) +
                                ", " + cursor.getString(cursor.getColumnIndex(StoreLocations.FIELD_OFFER_TITLE)) +
                                ", " + cursor.getString(cursor.getColumnIndex(StoreLocations.FIELD_STORE_NAME)) +
                                ", " + cursor.getString(cursor.getColumnIndex(StoreLocations.FIELD_START_DATE)),
                        Toast.LENGTH_SHORT).show();*/


            fix_Latitude = Double.parseDouble(tag.getLat());
            fix_Longitude = Double.parseDouble(tag.getLng());

            theta = fix_Longitude - cur_Longitude;
            dist = (Math.sin(deg2rad(fix_Latitude)) * Math.sin(deg2rad(cur_Latitude)))
                    + (Math.cos(deg2rad(fix_Latitude)) * Math.cos(deg2rad(cur_Latitude)))
                    * Math.cos(deg2rad(theta));
            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.753; //1.1515

            //Number Format code
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            numberFormat.setMinimumFractionDigits(2);
            numberFormat.setMaximumFractionDigits(3);
            String distt_km = numberFormat.format(dist);
            float disttt_km = Float.parseFloat(numberFormat.format(dist));

            //distance units
            dist_double = dist; // double
            dist_float = disttt_km * 1000;  //
            dist_int = (int) dist_float;

            String dist_D = String.valueOf(dist_double);
            String dist_F = String.valueOf(disttt_km);
            String dist_I = String.valueOf(dist_int);
            // tvDouble_Dist.setText(dist_D+" Km");
            //tvFloat_Dist.setText(dist_F+" Km");
            // tvInteger_Dist.setText(dist_I+" M");

            if (dist_int > 0 && dist_int < 500) {
                int mId = Integer.parseInt(String.valueOf(tag.getId()));
                Toast.makeText(getApplicationContext(), "Please! Enable Bluetooth " + dist_int, Toast.LENGTH_LONG).show();
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (!mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.enable();

                }
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.ic_bluetooth_black_24dp)
                                .setContentTitle(tag.getOffer_title() + " at " + tag.getStore_name())
                                .setContentText(tag.getOffer_desc());
// Creates an explicit intent for an Activity in your app
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
                mNotificationManager.notify(mId, mBuilder.build());


                //     tvStatus.setText("Status: "+"Please! Enable Bluetooth Mode.");
            } else if (dist_int > 500 && dist_int < 1000) {
                Toast.makeText(getApplicationContext(), "You are nearest to Mall within" + dist_int + " Meter", Toast.LENGTH_LONG).show();
                //       tvStatus.setText("Status: "+"You are nearest to Mall within "+dist_int+" meters.");
            } else if (dist_int > 1000 && dist_int < 2000) {
                Toast.makeText(getApplicationContext(), "Your distance from Mall is: " + dist_int + " Meters.", Toast.LENGTH_LONG).show();
                //       tvStatus.setText("Status: "+"Your distance from Mall is "+distt_km+" km.");
            } else if (dist_int > 2000 && dist_int < 3000) {
                Toast.makeText(getApplicationContext(), "Distance from mall: " + distt_km + " Km", Toast.LENGTH_LONG).show();
                //      tvStatus.setText("Status: "+"Find Alpha-One Mall in range of "+distt_km+" kms distance.");
            } else if (dist_int > 3000) {
                Toast.makeText(getApplicationContext(), "Away from.." + distt_km + " Km", Toast.LENGTH_LONG).show();
                //   tvStatus.setText("You are away from Alpha-One Mall at "+distt_km+" Km.");
            } else {
                //    tvStatus.setText("Ta-Ta Bye.. Bye.. "+distt_km+" Km.");
            }

            int dist_cm = dist_int * 100;
            String cm = String.valueOf(dist_cm);

        }
    }
    //     cursor.close();

        /*if (AccountActivity.c.moveToFirst()) {
            do{
                fix_Latitude = Double.parseDouble(AccountActivity.c.getString(AccountActivity.c.getColumnIndex( StoreLocations.FIELD_LAT)));
                fix_Longitude = Double.parseDouble(AccountActivity.c.getString(AccountActivity.c.getColumnIndex( StoreLocations.FIELD_LNG)));

                theta = fix_Longitude - cur_Longitude ;
                dist = ( Math.sin(deg2rad(fix_Latitude)) * Math.sin(deg2rad(cur_Latitude)) )
                        + ( Math.cos(deg2rad(fix_Latitude)) * Math.cos(deg2rad(cur_Latitude)) )
                        * Math.cos(deg2rad(theta));
                dist = Math.acos(dist);
                dist = rad2deg(dist);
                dist = dist * 60 * 1.753 ; //1.1515

                //Number Format code
                NumberFormat numberFormat = NumberFormat.getNumberInstance();
                numberFormat.setMinimumFractionDigits(2);
                numberFormat.setMaximumFractionDigits(3);
                String distt_km = numberFormat.format(dist);
                float disttt_km = Float.parseFloat(numberFormat.format(dist));

                //distance units
                dist_double = dist; // double
                dist_float = disttt_km * 1000 ;  //
                dist_int  = (int) dist_float ;

                String dist_D = String.valueOf(dist_double);
                String dist_F = String.valueOf(disttt_km);
                String dist_I = String.valueOf(dist_int);
                // tvDouble_Dist.setText(dist_D+" Km");
                //tvFloat_Dist.setText(dist_F+" Km");
                // tvInteger_Dist.setText(dist_I+" M");

                if(dist_int > 0 && dist_int < 500)
                {
                    int mId = 007;
                    Toast.makeText(getApplicationContext(),"Please! Enable Bluetooth " + dist_int ,Toast.LENGTH_LONG).show();
                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if(!mBluetoothAdapter.isEnabled()) {

                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(this)
                                        .setSmallIcon(R.drawable.ic_bluetooth_black_24dp)
                                        .setContentTitle(AccountActivity.c.getString(AccountActivity.c.getColumnIndex( StoreLocations.FIELD_OFFER_TITLE)) + " at " + AccountActivity.c.getString(AccountActivity.c.getColumnIndex( StoreLocations.FIELD_STORE_NAME)))
                                        .setContentText(AccountActivity.c.getString(AccountActivity.c.getColumnIndex( StoreLocations.FIELD_OFFER_DESC)));
// Creates an explicit intent for an Activity in your app
                        NotificationManager mNotificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        mBluetoothAdapter.enable();
// mId allows you to update the notification later on.
                        mNotificationManager.notify(mId, mBuilder.build());
                    }



                    //     tvStatus.setText("Status: "+"Please! Enable Bluetooth Mode.");
                }
                else if(dist_int > 500 &&  dist_int < 1000)
                {
                    Toast.makeText(getApplicationContext(),"You are nearest to Mall within"+dist_int+" Meter",Toast.LENGTH_LONG).show();
                    //       tvStatus.setText("Status: "+"You are nearest to Mall within "+dist_int+" meters.");
                }
                else if(dist_int > 1000 && dist_int < 2000)
                {
                    Toast.makeText(getApplicationContext(),"Your distance from Mall is: "+dist_int+" Meters.",Toast.LENGTH_LONG).show();
                    //       tvStatus.setText("Status: "+"Your distance from Mall is "+distt_km+" km.");
                }
                else if(dist_int > 2000 && dist_int < 3000)
                {
                    Toast.makeText(getApplicationContext(),"Distance from mall: "+distt_km+" Km",Toast.LENGTH_LONG).show();
                    //      tvStatus.setText("Status: "+"Find Alpha-One Mall in range of "+distt_km+" kms distance.");
                }
                else if (dist_int > 3000)
                {
                    Toast.makeText(getApplicationContext(),"Away from.."+distt_km+" Km",Toast.LENGTH_LONG).show();
                    //   tvStatus.setText("You are away from Alpha-One Mall at "+distt_km+" Km.");
                }
                else {
                    //    tvStatus.setText("Ta-Ta Bye.. Bye.. "+distt_km+" Km.");
                }

                int dist_cm = dist_int * 100;
                String cm = String.valueOf(dist_cm);

                *//*Toast.makeText(this,
                        AccountActivity.c.getString(AccountActivity.c.getColumnIndex(StoreLocations._ID)) +
                                ", " +  AccountActivity.c.getString(AccountActivity.c.getColumnIndex( StoreLocations.FIELD_OFFER_TITLE)) +
                                ", " + AccountActivity.c.getString(AccountActivity.c.getColumnIndex( StoreLocations.FIELD_STORE_NAME)) +
                                ", " + AccountActivity.c.getString(AccountActivity.c.getColumnIndex( StoreLocations.FIELD_START_DATE)),
                        Toast.LENGTH_SHORT).show();*//*
            } while (AccountActivity.c.moveToNext());
        }*/

    //for Alfa-One Mall
    //  fix_Latitude = 23.012102 ;
    // fix_Longitude = 72.522634 ;

    //for current location


    // dist calculate in Km


    //  tvKm_Distance.setText(distt_km+" Km");
    //   tvM_Distance.setText(dist_int+" Meter");
    //   tvCm_Distance.setText(cm+" Cm");

//        get_Distance();


    private double deg2rad(double deg)
    {
        return ( (deg * Math.PI )/ 180.0 );
    }

    private double rad2deg(double rad )
    {
        return ( (rad * 180.0)/ (Math.PI) );
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {
        if (mRequestingLocationUpdates) {
            mRequestingLocationUpdates = false;
            // It is a good practice to remove location requests when the activity is in a paused or
            // stopped state. Doing so helps battery performance and is especially
            // recommended in applications that request frequent location updates.

            Log.d(TAG, "stopLocationUpdates();==");
            // The final argument to {@code requestLocationUpdates()} is a LocationListener
            // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }

}
