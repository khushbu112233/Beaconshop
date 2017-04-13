package com.amplearch.beaconshop;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.amplearch.beaconshop.Model.Favourites;
import com.amplearch.beaconshop.Model.StoreLocation;
import com.amplearch.beaconshop.helper.DatabaseHelper;

import java.util.List;
import java.util.StringTokenizer;

public class TempActivity extends AppCompatActivity {

    public static Cursor c;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        db = new DatabaseHelper(getApplicationContext());

        // Creating tags
       /* StoreLocation tag1 = new StoreLocation("Ghatlodia", "23.057506", "72.543392", "Cashbak", "70% Cashback, Hurry up. Offer till 6th April, 2017 only. Men's wear discount 50%, Women's Wear discount 75%.", "08/03/2017", "06/04/2017");
        StoreLocation tag2 = new StoreLocation("Ghatlodia", "23.057506", "72.543392", "Cashbak", "70% Cashback, Hurry up. Offer till 6th April, 2017 only. Men's wear discount 50%, Women's Wear discount 75%.", "08/03/2017", "06/04/2017");
        StoreLocation tag3 = new StoreLocation("Vikram Appts", "23.01210", "72.522634", "Redeem Code", "Hurry up. Offer till 6th April, 2017 only. Men's wear discount 50%, Women's Wear discount 75%", "08/02/2015", "14/02/2016");
        StoreLocation tag4 = new StoreLocation("Titanium City Center", "23.012102", "72.522634", "Whole Sale", "70% Cashback", "08/02/2015", "14/02/2016");

        // Inserting tags in db
        long tag1_id = db.createStoreLocation(tag1);
        long tag2_id = db.createStoreLocation(tag2);
        long tag3_id = db.createStoreLocation(tag3);
        long tag4_id = db.createStoreLocation(tag4);
*/
        Log.d("StoreLocation Count", "StoreLocation Count: " + db.getAllLocations().size());


        // Creating ToDos
        Favourites todo1 = new Favourites("1", "22");
        Favourites todo2 = new Favourites("2", "25");
        Favourites todo3 = new Favourites("3", "26");

        Favourites todo4 = new Favourites("4", "44");
        Favourites todo5 = new Favourites("5", "50");

        // Inserting todos in db
        // Inserting todos under "Shopping" StoreLocation
      /*  long todo1_id = db.createFavourites(todo1, new long[] { tag1_id });
        long todo2_id = db.createFavourites(todo2, new long[] { tag1_id });
        long todo3_id = db.createFavourites(todo3, new long[] { tag1_id });

        // Inserting todos under "Watchlist" StoreLocation
        long todo4_id = db.createFavourites(todo4, new long[] { tag3_id });
        long todo5_id = db.createFavourites(todo5, new long[] { tag3_id });
*/
        Log.e("Favourites Count", "Favourites count: " + db.getFavouritesCount());

        // "Post new Article" - assigning this under "Important" StoreLocation
        // Now this will have - "Androidhive" and "Important" Tags
        //   db.createTodoTag(todo10_id, tag2_id);

        // Getting all tag names
        Log.d("Get Tags", "Getting All Tags");

        List<StoreLocation> allTags = db.getAllLocations();
        for (StoreLocation tag : allTags) {
            Log.d("StoreLocation Name", tag.getStore_name());
            Toast.makeText(getApplicationContext(), tag.getStore_name() + " " + tag.getOffer_title(), Toast.LENGTH_LONG).show();

        }

        // Getting all Todos
        Log.d("Get Todos", "Getting All ToDos");

        List<Favourites> allToDos = db.getAllFavourites();
        for (Favourites todo : allToDos) {
            Log.d("ToDo", todo.getProduct_id());
        }

        // Getting todos under "Watchlist" tag name
        Log.d("ToDo", "Get todos under single StoreLocation name");
/*

        List<Favourites> tagsWatchList = db.getAllToDosByTag(tag3.getTagName());
        for (Favourites todo : tagsWatchList) {
            Log.d("ToDo Watchlist", todo.getNote());
        }
*/

        // Deleting a ToDo
        Log.d("Delete Favourites", "Deleting a Favourites");
        Log.d("Favourites Count", "Favourites Count Before Deleting: " + db.getFavouritesCount());

        // db.deleteFavorites(todo5_id);

        Log.d("Favourites Count", "Favourites Count After Deleting: " + db.getFavouritesCount());

        //    db.deleteTag(tag1, true);
/*

        Log.d("StoreLocation Count",
                "StoreLocation Count After Deleting 'Shopping' Todos: "
                        + db.getToDoCount());
*/

        // Updating tag name
     /*   tag3.setOffer_title("Movies to watch");
        db.updateTag(tag3);
*/
        // Don't forget to close database connection
        db.closeDB();


        //addToFavoritesBtn.setText("Remove From Favourites");

        /*SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
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
        }*/
        // Cursor dateCursor = StoreLocations.dateCompare(Date.valueOf(StoreLocations.FIELD_START_DATE), Date.valueOf(StoreLocations.FIELD_END_DATE));
    }

}
