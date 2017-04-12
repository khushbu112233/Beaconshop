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
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
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
import android.widget.Toast;
import com.amplearch.beaconshop.Adapter.CustomAdapter;
import com.amplearch.beaconshop.Adapter.LayoutPager;
import com.amplearch.beaconshop.Fragment.AboutUsFragment;
import com.amplearch.beaconshop.Fragment.BadgesFragment;
import com.amplearch.beaconshop.Fragment.FavoriteFragment;
import com.amplearch.beaconshop.Fragment.HomeFragment;
import com.amplearch.beaconshop.Fragment.MyProfileFragment;
import com.amplearch.beaconshop.Fragment.ProfileFragment;
import com.amplearch.beaconshop.Fragment.SettingsFragment;
import com.amplearch.beaconshop.Fragment.HelpFragment;
import com.amplearch.beaconshop.Fragment.VoucherFragment;
import com.amplearch.beaconshop.Model.ItemObject;
import com.amplearch.beaconshop.MyService;
import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.Utils.Const;
import com.amplearch.beaconshop.Utils.LocationUpdateService;
import com.amplearch.beaconshop.Utils.NotificationHandler;
import com.amplearch.beaconshop.Utils.TrojanText;

import android.support.design.widget.TabLayout;
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

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    RelativeLayout rlButtons ;
    FrameLayout flBackImage ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rlButtons = (RelativeLayout)findViewById(R.id.rlButtons);
//        flBackImage = (FrameLayout)findViewById(R.id.flBackImage);

        if (!mIsServiceStarted) {
            mIsServiceStarted = true;
           // setButtonsEnabledState();
            OnGoingLocationNotification(this);
            startService(new Intent(this, LocationUpdateService.class));
        }

        mTitle = mDrawerTitle = getTitle();
        titles = getResources().getStringArray(R.array.navigation_drawer_items_array);

        toolbarTitle =(TrojanText)findViewById(R.id.toolbarTitle);

        topToolBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);
       // topToolBar.setLogo(R.mipmap.ic_launcher);
        topToolBar.setLogo(R.mipmap.ic_launcher);
//        topToolBar.setLogoDescription("BeaconShop");
//        topToolBar.setTitleTextColor(getResources().getColor(R.color.icons));
//        topToolBar.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
//       mDrawerToggle.setDrawerIndicatorEnabled(false);

        //Initializing the tablayout
      /*  tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("All Offer List"));
        tabLayout.addTab(tabLayout.newTab().setText("Nearby"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
      */

        //Initializing viewPager
     /*   viewPager = (ViewPager) findViewById(R.id.pager);

        //Creating our pager adapter
        LayoutPager adapter = new LayoutPager(getSupportFragmentManager());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);*/

        //Adding onTabSelectedListener to swipe views
/*
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
*/

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        LayoutInflater inflater = getLayoutInflater();
        View listHeaderView = inflater.inflate(R.layout.header_list,null, false);

        mDrawerList.addHeaderView(listHeaderView);

        List<ItemObject> listViewItems = new ArrayList<ItemObject>();
        listViewItems.add(new ItemObject("Home", R.drawable.ic_home_black_24dp));
        listViewItems.add(new ItemObject("Favorites", R.drawable.ic_favorite_black_24dp));
        listViewItems.add(new ItemObject("My Vouchers", R.drawable.ic_email_black_24dp));
        listViewItems.add(new ItemObject("Badges", R.drawable.ic_help_outline_black_24dp));
        listViewItems.add(new ItemObject("My Account", R.drawable.ic_person_black_24dp));
        listViewItems.add(new ItemObject("Settings", R.drawable.ic_settings_black_24dp));
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
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // make Toast when click
                Toast.makeText(getApplicationContext(), "Position " + position, Toast.LENGTH_LONG).show();
                selectItemFragment(position);
            }
        });
    }


    private void selectItemFragment(int position){

        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch(position)
        {
            default:
            /*case 0:
                fragment = new HomeFragment();
                break;*/
            case 1:
                fragment = new HomeFragment();
                toolbarTitle.setText("Beacon Shop");
                break;
            case 2:
                fragment = new FavoriteFragment();
                rlButtons.setVisibility(View.INVISIBLE);
                toolbarTitle.setText("Favorites");
                break;
            case 3:
                fragment = new VoucherFragment();
                rlButtons.setVisibility(View.INVISIBLE);
                toolbarTitle.setText("My Vouchers");
                break;
            case 4:
                fragment = new BadgesFragment();
                rlButtons.setVisibility(View.INVISIBLE);
                toolbarTitle.setText("Badges");
                break;
            case 5:
                fragment = new MyProfileFragment();
                rlButtons.setVisibility(View.INVISIBLE);
                toolbarTitle.setText("My Account");
                break;
            case 6:
                fragment = new SettingsFragment();
                rlButtons.setVisibility(View.INVISIBLE);
                toolbarTitle.setText("Settings");
                break;
            case 7:
                fragment = new HelpFragment();
                rlButtons.setVisibility(View.INVISIBLE);
                toolbarTitle.setText("Help");
                break;
            case 8:
                fragment = new AboutUsFragment();
                rlButtons.setVisibility(View.INVISIBLE);
                toolbarTitle.setText("About Us");
                break;
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
