package com.amplearch.beaconshop.Activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.amplearch.beaconshop.Adapter.CustomAdapter;
import com.amplearch.beaconshop.Adapter.DrawerAdapter;
import com.amplearch.beaconshop.Fragment.AboutUsFragment;
import com.amplearch.beaconshop.Fragment.BadgesFragment;
import com.amplearch.beaconshop.Fragment.ChangePasswordFragment;
import com.amplearch.beaconshop.Fragment.FavoriteFragment;
import com.amplearch.beaconshop.Fragment.HomeFragment;
import com.amplearch.beaconshop.Fragment.ProfileFragment;
import com.amplearch.beaconshop.Fragment.SettingsFragment;
import com.amplearch.beaconshop.Fragment.HelpFragment;
import com.amplearch.beaconshop.Fragment.VoucherFragment;
import com.amplearch.beaconshop.Model.ItemObject;
import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.Utils.Const;
import com.amplearch.beaconshop.Utils.LocationUpdateService;
import com.amplearch.beaconshop.Utils.NotificationHandler;
import com.amplearch.beaconshop.Utils.TrojanText;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    String[] titles ;
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar topToolBar;
    private TrojanText toolbarTitle ;
    private static final int RC_SIGN_IN = 007;

    private boolean mIsServiceStarted = false;
    public static final String EXTRA_NOTIFICATION_ID = "notification_id";
    public static final String ACTION_STOP = "STOP_ACTION";
    public static final String ACTION_FROM_NOTIFICATION = "isFromNotification";
    private String action;
    private int notifID;

    ArrayList<Integer> drawerImage = new ArrayList<Integer>();
    ArrayList<String> drawerText = new ArrayList<String>();
    DrawerAdapter drawerAdapter ;

    RelativeLayout rlButtons ;
    FrameLayout flBackImage ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rlButtons = (RelativeLayout)findViewById(R.id.rlButtons);

        if (!mIsServiceStarted) {
            mIsServiceStarted = true;
           // setButtonsEnabledState();
            OnGoingLocationNotification(this);
            startService(new Intent(this, LocationUpdateService.class));
        }

//        mTitle = mDrawerTitle = getTitle();
//        titles = getResources().getStringArray(R.array.navigation_drawer_items_array);

        toolbarTitle =(TrojanText)findViewById(R.id.toolbarTitle);

        topToolBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);
        topToolBar.setLogo(R.mipmap.ic_launcher);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        LayoutInflater inflater = getLayoutInflater();

        View listHeaderView = inflater.inflate(R.layout.header_list,null, false);
        mDrawerList.addHeaderView(listHeaderView);

        // adding values in left drawer
//        drawerImage.add(R.drawable.ic_home_black_24dp);         // for home
//        drawerText.add("Home");
//        drawerImage.add(R.drawable.ic_favorite_black_24dp);     // for home
//        drawerText.add("Favourites");
//        drawerImage.add(R.drawable.ic_email_black_24dp);       // for home
//        drawerText.add("My Vouchers");
//        drawerImage.add(R.drawable.ic_help_outline_black_24dp);     // for home
//        drawerText.add("Badges");
//        drawerImage.add(R.drawable.ic_person_black_24dp);     // for home
//        drawerText.add("My Account");
//        drawerImage.add(R.drawable.ic_settings_black_24dp);     // for home
//        drawerText.add("Settings");
//        drawerImage.add(R.drawable.ic_help_outline_black_24dp);     // for home
//        drawerText.add("Help");
//        drawerImage.add(R.drawable.ic_info_outline_black_24dp);     // for home
//        drawerText.add("About Us");
//
//        drawerAdapter = new DrawerAdapter(getApplicationContext(),drawerImage,drawerText);
//
//        mDrawerList.setAdapter(drawerAdapter);

        List<ItemObject> listViewItems = new ArrayList<ItemObject>();
        listViewItems.add(new ItemObject("Home", R.drawable.ic_home_black_24dp));
        listViewItems.add(new ItemObject("Favourites", R.drawable.ic_favorite_black_24dp));
        listViewItems.add(new ItemObject("My Vouchers", R.drawable.ic_email_black_24dp));
        listViewItems.add(new ItemObject("Badges", R.drawable.ic_help_outline_black_24dp));
        listViewItems.add(new ItemObject("My Account", R.drawable.ic_person_black_24dp));
        listViewItems.add(new ItemObject("Settings", R.drawable.ic_settings_black_24dp));
        listViewItems.add(new ItemObject("Change Password", R.drawable.ic_swap_horiz_black_24dp));
        listViewItems.add(new ItemObject("Help", R.drawable.ic_help_outline_black_24dp));
        listViewItems.add(new ItemObject("About Us", R.drawable.ic_info_outline_black_24dp));

        mDrawerList.setAdapter(new CustomAdapter(this, listViewItems));

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close)
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.content_frame, new HomeFragment());
        tx.commit();

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
//                Toast.makeText(getApplicationContext(), "Position " + position, Toast.LENGTH_LONG).show();
                selectItemFragment(position);
            }
        });


    }

    public Fragment getItem(int position)
    {
        List<Fragment> fragList = new ArrayList<Fragment>();
        fragList.add(new HomeFragment());
        fragList.add(new FavoriteFragment());
        fragList.add(new VoucherFragment());
        fragList.add(new BadgesFragment());
        fragList.add(new ProfileFragment());
        fragList.add(new SettingsFragment());
        fragList.add(new ChangePasswordFragment());
        fragList.add(new HelpFragment());
        fragList.add(new AboutUsFragment());

        Fragment fragment = fragList.get(position);
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    private void selectItemFragment(int position){

        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch(position)
        {
            /*default:
            case 0:
                fragment = new HomeFragment();
                break;
            */
            case 1:
                fragment = new HomeFragment();
                rlButtons.setVisibility(View.VISIBLE);
                toolbarTitle.setText("Beacon Shop");
                break;
            case 2:
                fragment = new FavoriteFragment();
                rlButtons.setVisibility(View.GONE);
                toolbarTitle.setText("Favorites");
                break;
            case 3:
                fragment = new VoucherFragment();
                rlButtons.setVisibility(View.GONE);
                toolbarTitle.setText("Vouchers");
                break;
            case 4:
                fragment = new BadgesFragment();
                rlButtons.setVisibility(View.GONE);
                toolbarTitle.setText("Badges");
                break;
            case 5:
                fragment = new ProfileFragment();
                rlButtons.setVisibility(View.GONE);
                toolbarTitle.setText("Profile");
                break;
            case 6:
                fragment = new SettingsFragment();
                rlButtons.setVisibility(View.GONE);
                toolbarTitle.setText("Settings");
                break;
            case 7:
                fragment = new ChangePasswordFragment();
                rlButtons.setVisibility(View.GONE);
                toolbarTitle.setText("Change Password");
                break;
            case 8:
                fragment = new HelpFragment();
                rlButtons.setVisibility(View.GONE);
                toolbarTitle.setText("Help");
                break;
            case 9:
                fragment = new AboutUsFragment();
                rlButtons.setVisibility(View.GONE);
                toolbarTitle.setText("About Us");
        }
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        mDrawerList.setItemChecked(position, true);
//        setTitle(titles[position]);
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
        if (getIntent().getAction() != null) {
            action = getIntent().getAction();
            notifID = getIntent().getIntExtra(EXTRA_NOTIFICATION_ID, 0);
            if (action.equalsIgnoreCase(ACTION_FROM_NOTIFICATION)) {
                mIsServiceStarted = true;
                //setButtonsEnabledState();

            }
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
    public void onBackPressed()
    {
       /* Fragment frag = new Fragment();
        Fragment frag0 = new HomeFragment();
        Fragment frag1 = new FavoriteFragment();
        Fragment frag2 = new VoucherFragment();
        Fragment frag3 = new BadgesFragment();
        Fragment frag4 = new ProfileFragment();
        Fragment frag5 = new SettingsFragment();
        Fragment frag6 = new ChangePasswordFragment();
        Fragment frag7 = new HelpFragment();
        Fragment frag8 = new AboutUsFragment();
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        fragmentList.add(0,frag0);
        fragmentList.add(1,frag1);
        fragmentList.add(2,frag2);
        fragmentList.add(3,frag3);
        fragmentList.add(4,frag4);
        fragmentList.add(5,frag5);
        fragmentList.add(6,frag6);
        fragmentList.add(7,frag7);
        fragmentList.add(8,frag8);
        if(fragmentList.indexOf(0) == 0)
        {

        }
*/
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Exit Application?");
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id)
                            {
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
