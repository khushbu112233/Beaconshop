package com.amplearch.beaconshop;

import android.bluetooth.BluetoothAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.amplearch.beaconshop.Model.StoreLocation;
import com.amplearch.beaconshop.helper.DatabaseHelper;

import java.text.NumberFormat;
import java.util.List;

public class NearbyOffers extends AppCompatActivity {

    DatabaseHelper db;
    double fix_Latitude, fix_Longitude;
    double cur_Latitude, cur_Longitude;
    double dist, theta;
    int dist_int;
    float dist_float;
    double dist_double;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_offers);

        db = new DatabaseHelper(getApplicationContext());

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
                // Toast.makeText(getApplicationContext(), "Please! Enable Bluetooth " + dist_int, Toast.LENGTH_LONG).show();
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (!mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.enable();

                }
               /* NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.ic_bluetooth_black_24dp)
                                .setContentTitle(tag.getOffer_title() + " at " + tag.getStore_name())
                                .setContentText(tag.getOffer_desc());
// Creates an explicit intent for an Activity in your app
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
                mNotificationManager.notify(mId, mBuilder.build());
*/

                //     tvStatus.setText("Status: "+"Please! Enable Bluetooth Mode.");
            } else if (dist_int > 500 && dist_int < 1000) {
                // Toast.makeText(getApplicationContext(), "You are nearest to Mall within" + dist_int + " Meter", Toast.LENGTH_LONG).show();
                //       tvStatus.setText("Status: "+"You are nearest to Mall within "+dist_int+" meters.");
            } else if (dist_int > 1000 && dist_int < 2000) {
                // Toast.makeText(getApplicationContext(), "Your distance from Mall is: " + dist_int + " Meters.", Toast.LENGTH_LONG).show();
                //       tvStatus.setText("Status: "+"Your distance from Mall is "+distt_km+" km.");
            } else if (dist_int > 2000 && dist_int < 3000) {
                // Toast.makeText(getApplicationContext(), "Distance from mall: " + distt_km + " Km", Toast.LENGTH_LONG).show();
                //      tvStatus.setText("Status: "+"Find Alpha-One Mall in range of "+distt_km+" kms distance.");
            } else if (dist_int > 3000) {
                // Toast.makeText(getApplicationContext(), "Away from.." + distt_km + " Km", Toast.LENGTH_LONG).show();
                //   tvStatus.setText("You are away from Alpha-One Mall at "+distt_km+" Km.");
            } else {
                //    tvStatus.setText("Ta-Ta Bye.. Bye.. "+distt_km+" Km.");
            }

            int dist_cm = dist_int * 100;
            String cm = String.valueOf(dist_cm);

        }
    }
    private double deg2rad(double deg)
    {
        return ( (deg * Math.PI )/ 180.0 );
    }

    private double rad2deg(double rad )
    {
        return ( (rad * 180.0)/ (Math.PI) );
    }

}
