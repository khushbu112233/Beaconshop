package com.amplearch.beaconshop.Activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.amplearch.beaconshop.Adapter.CustomAdapter;
import com.amplearch.beaconshop.ApplicationUtils.MyApplication;
import com.amplearch.beaconshop.Fragment.AboutUsFragment;
import com.amplearch.beaconshop.Fragment.BadgesFragment;
import com.amplearch.beaconshop.Fragment.FavoriteFragment;
import com.amplearch.beaconshop.Fragment.HomeFragment;
import com.amplearch.beaconshop.Fragment.ProfileFragment;
import com.amplearch.beaconshop.Fragment.SettingsFragment;
import com.amplearch.beaconshop.Fragment.HelpFragment;
import com.amplearch.beaconshop.Fragment.VoucherFragment;
import com.amplearch.beaconshop.Model.ItemObject;
import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.Utils.BeaconHelper;
import com.amplearch.beaconshop.Utils.Const;
import com.amplearch.beaconshop.Utils.FileHelper;
import com.amplearch.beaconshop.Utils.LocationUpdateService;
import com.amplearch.beaconshop.Utils.NotificationHandler;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.utils.UrlBeaconUrlCompressor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends ActionBarActivity implements BeaconConsumer{

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    String[] titles ;
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar topToolBar;
    private static final int RC_SIGN_IN = 007;

    private boolean mIsServiceStarted = false;
    public static final String EXTRA_NOTIFICATION_ID = "notification_id";
    public static final String ACTION_STOP = "STOP_ACTION";
    public static final String ACTION_FROM_NOTIFICATION = "isFromNotification";
    private String action;
    private int notifID;

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
    protected static final String TAG = "ScanActivity";

    private final static int
            CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private FileHelper fileHelper;
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

   // private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
    // LocationClient for Google Play Location Services

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!mIsServiceStarted) {
            mIsServiceStarted = true;
           // setButtonsEnabledState();
            OnGoingLocationNotification(this);
            startService(new Intent(this, LocationUpdateService.class));
        }

        verifyBluetooth();
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        MyApplication app = (MyApplication) this.getApplication();
     //   beaconManager.bind(this);
        //beaconManager.setForegroundScanPeriod(10);

        fileHelper = app.getFileHelper();
        startScanning();
        mTitle = mDrawerTitle = getTitle();
        titles = getResources().getStringArray(R.array.navigation_drawer_items_array);
        topToolBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);
        topToolBar.setLogo(R.mipmap.ic_launcher);
        topToolBar.setLogoDescription("BeaconShop");
        topToolBar.setTitleTextColor(getResources().getColor(R.color.icons));
//        mDrawerToggle.setDrawerIndicatorEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        LayoutInflater inflater = getLayoutInflater();
        View listHeaderView = inflater.inflate(R.layout.header_list,null, false);

        mDrawerList.addHeaderView(listHeaderView);

        List<ItemObject> listViewItems = new ArrayList<ItemObject>();
        listViewItems.add(new ItemObject("Home", R.drawable.ic_home_black_24dp));
        listViewItems.add(new ItemObject("Favourites", R.drawable.ic_favorite_black_24dp));
        listViewItems.add(new ItemObject("My Vouchers", R.drawable.ic_email_black_24dp));
        listViewItems.add(new ItemObject("Badges", R.drawable.ic_help_outline_black_24dp));
        listViewItems.add(new ItemObject("My Account", R.drawable.ic_person_black_24dp));
        listViewItems.add(new ItemObject("Settings", R.drawable.ic_settings_black_24dp));
        listViewItems.add(new ItemObject("Help", R.drawable.ic_help_outline_black_24dp));
        listViewItems.add(new ItemObject("About Us", R.drawable.ic_info_outline_black_24dp));

        mDrawerList.setAdapter(new CustomAdapter(this, listViewItems));

        mDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close)
        {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_logo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.content_frame, new HomeFragment());
        tx.commit();

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // make Toast when click
                Toast.makeText(getApplicationContext(), "Position " + position, Toast.LENGTH_LONG).show();
                selectItemFragment(position);
            }
        });
    }

    @Override
    public void onBeaconServiceConnect() {
        /*beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection beacons, Region region) {
                if (beacons.size() > 0) {
                    Log.i(TAG, "The first beacon I see is about "+beacons.toString()+" meters away.");
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
            e.printStackTrace();
        }*/
    }

    private void selectItemFragment(int position){

        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch(position) {
            default:
            /*case 0:
                fragment = new HomeFragment();
                break;*/
            case 1:
                fragment = new HomeFragment();
                break;
            case 2:
                fragment = new FavoriteFragment();
                break;
            case 3:
                fragment = new VoucherFragment();
                break;
            case 4:
                fragment = new BadgesFragment();
                break;
            case 5:
                fragment = new ProfileFragment();
                break;
            case 6:
                fragment = new SettingsFragment();
                break;
            case 7:
                fragment = new HelpFragment();
                break;
            case 8:
                fragment = new AboutUsFragment();
                break;
        }
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        mDrawerList.setItemChecked(position, true);
        setTitle(titles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    public void exportDatabaseToSdCard(View view) {
        Const.ExportDatabase(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
      //  beaconManager.bind(this);
        if (getIntent().getAction() != null) {
            action = getIntent().getAction();
            notifID = getIntent().getIntExtra(EXTRA_NOTIFICATION_ID, 0);
            if (action.equalsIgnoreCase(ACTION_FROM_NOTIFICATION)) {
                mIsServiceStarted = true;
                //setButtonsEnabledState();

            }
        }
    }


    private void startScanning() {

        // Set UI elements to the correct state.
      //  scanButton.setText(MODE_SCANNING);
       // ((EditText)findViewById(R.id.scanText)).setText("");

        // Reset event counter
        eventNum = 1;
        // Get current values for logging preferences
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        HashMap<String, Object> prefs = new HashMap<String, Object>();
        prefs.putAll(sharedPrefs.getAll());

        index = (Boolean)prefs.get(PREFERENCE_INDEX);
        location = (Boolean)prefs.get(PREFERENCE_LOCATION);
        uuid = (Boolean)prefs.get(PREFERENCE_UUID);
        majorMinor = (Boolean)prefs.get(PREFERENCE_MAJORMINOR);
        rssi = (Boolean)prefs.get(PREFERENCE_RSSI);
        proximity = (Boolean)prefs.get(PREFERENCE_PROXIMITY);
        power = (Boolean)prefs.get(PREFERENCE_POWER);
        timestamp = (Boolean)prefs.get(PREFERENCE_TIMESTAMP);
        scanInterval = (String)prefs.get(PREFERENCE_SCANINTERVAL);
        realTimeLog = (Boolean)prefs.get(PREFERENCE_REALTIME);

        // Get current background scan interval (if specified)
        if (prefs.get(PREFERENCE_SCANINTERVAL) != null) {
            //beaconManager.setBackgroundBetweenScanPeriod(Long.parseLong(scanInterval));
        }

      //  logToDisplay("Scanning...");

        // Initialise scan log
        logString = new StringBuffer();

        //Start scanning again.
        /*beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    Iterator<Beacon> beaconIterator = beacons.iterator();
                    while (beaconIterator.hasNext()) {
                        Beacon beacon = beaconIterator.next();
                        // Debug - logging a beacon - checking background logging is working.
                        System.out.println("Logging another beacon.");
                        logBeaconData(beacon);
                        Toast.makeText(getApplicationContext(), "Enter", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            // TODO - OK, what now then?
        }*/

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
        Log.e("beacon : ", scanString + "\n");
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
            Log.e("beacon : ", scanString + "\n");
        }
    }

    private void logToDisplay(final String line) {
        runOnUiThread(new Runnable() {
            public void run() {

                Log.e("beacon : ", line + "\n");
               // editText.append(line + "\n");

                // Temp code - don't really want to do this for every line logged, will look for a
                // workaround.
               // Linkify.addLinks(editText, Linkify.WEB_URLS);

               // scroller.fullScroll(View.FOCUS_DOWN);

            }
        });
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
                        finish();
                        System.exit(0);
                    }
                });
                builder.show();
            }
        }
        catch (RuntimeException e) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Bluetooth LE not available");
            builder.setMessage("Sorry, this device does not support Bluetooth LE.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                    System.exit(0);
                }

            });
            builder.show();

        }

    }


    /**
     * Method to generate OnGoingLocationNotification
     *
     * @param mcontext
     */
    public static void OnGoingLocationNotification(Context mcontext) {
        int mNotificationId;

        mNotificationId = (int) System.currentTimeMillis();

        //Broadcast receiver to handle the stop action
        Intent mstopReceive = new Intent(mcontext, NotificationHandler.class);
        mstopReceive.putExtra(EXTRA_NOTIFICATION_ID, mNotificationId);
        mstopReceive.setAction(ACTION_STOP);
        PendingIntent pendingIntentStopService = PendingIntent.getBroadcast(mcontext, (int) System.currentTimeMillis(), mstopReceive, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mcontext)
                        .setSound(alarmSound)
                        .setSmallIcon(R.drawable.ic_cast_off_light)
                        .setContentTitle("Location Service")
                        .addAction(R.drawable.ic_cancel, "Stop Service", pendingIntentStopService)
                        .setOngoing(true).setContentText("Running...");
        mBuilder.setAutoCancel(false);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(mcontext, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        resultIntent.setAction(ACTION_FROM_NOTIFICATION);
        resultIntent.putExtra(EXTRA_NOTIFICATION_ID, mNotificationId);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(mcontext, (int) System.currentTimeMillis(), resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) mcontext.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.cancel(mNotificationId);

        Notification mNotification = mBuilder.build();
        mNotification.defaults |= Notification.DEFAULT_VIBRATE;
        mNotification.flags |= Notification.FLAG_ONGOING_EVENT;

        mNotificationManager.notify(mNotificationId, mNotification);

    }

    private void cancelNotification(Context mContext, int mnotinotifId) {
        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(mnotinotifId);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application?");
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
