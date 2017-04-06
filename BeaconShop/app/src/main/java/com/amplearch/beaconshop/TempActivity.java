package com.amplearch.beaconshop;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TempActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        String URL = "content://com.amplearch.beaconshop.StoreLocations";

        Uri students = Uri.parse(URL);
        Cursor c = managedQuery(students, null, null, null, null);

        if (c.moveToFirst()) {
            do{
                Toast.makeText(this,
                        c.getString(c.getColumnIndex(StoreLocations._ID)) +
                                ", " +  c.getString(c.getColumnIndex( StoreLocations.FIELD_OFFER_TITLE)) +
                                ", " + c.getString(c.getColumnIndex( StoreLocations.FIELD_STORE_NAME)) +
                                ", " + c.getString(c.getColumnIndex( StoreLocations.FIELD_START_DATE)),
                        Toast.LENGTH_SHORT).show();
            } while (c.moveToNext());
        }
        //addToFavoritesBtn.setText("Remove From Favourites");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String to_date = sdf.format(new Date());

        Toast.makeText(getApplicationContext(), to_date, Toast.LENGTH_LONG).show();

        String query = "select *" +
                "  from Locations" +
                "  where start_date >= Datetime('05/04/2017')" +
                "  and end_date <= Datetime('05/04/2017')";
        Log.e("query ", query );

        Cursor crs = StoreLocations.db.rawQuery(
                "SELECT * " +
                        "FROM " +
                        StoreLocations.LOCATIONS_TABLE_NAME +
                        " WHERE " +
                        "start_date <= '"+to_date+"' AND end_date >= '"+to_date+"'", null);

        if (crs.moveToFirst()) {
            do{
                Toast.makeText(this,
                        crs.getString(crs.getColumnIndex(StoreLocations._ID)) +
                                ", " +  crs.getString(crs.getColumnIndex( StoreLocations.FIELD_OFFER_TITLE)) +
                                ", " + crs.getString(crs.getColumnIndex( StoreLocations.FIELD_STORE_NAME)) +
                                ", " + crs.getString(crs.getColumnIndex( StoreLocations.FIELD_START_DATE)),
                        Toast.LENGTH_SHORT).show();
            } while (crs.moveToNext());
        }
       // Cursor dateCursor = StoreLocations.dateCompare(Date.valueOf(StoreLocations.FIELD_START_DATE), Date.valueOf(StoreLocations.FIELD_END_DATE));
    }

}
