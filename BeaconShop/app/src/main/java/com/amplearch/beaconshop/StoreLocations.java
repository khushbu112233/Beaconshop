package com.amplearch.beaconshop;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by admin on 04/05/2017.
 */

public class  StoreLocations extends ContentProvider {

    static final String PROVIDER_NAME = "com.amplearch.beaconshop.StoreLocations";
    static final String URL = "content://" + PROVIDER_NAME + "/locations";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    public static final String _ID = "_id";

    public static final String FIELD_STORE_NAME = "store_name";

    /** Field 2 of the table locations, stores the latitude */
    public static final String FIELD_LAT = "lat";

    /** Field 3 of the table locations, stores the longitude*/
    public static final String FIELD_LNG = "lng";

    public static final String FIELD_OFFER_TITLE = "offer_title";

    public static final String FIELD_OFFER_DESC = "offer_desc";

    public static final String FIELD_START_DATE = "start_date";

    public static final String FIELD_END_DATE = "end_date";

    private static HashMap<String, String> LOCATIONS_PROJECTION_MAP;

    static final int LOCATIONS = 1;
    static final int LOCATIONS_ID = 2;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "locations", LOCATIONS);
        uriMatcher.addURI(PROVIDER_NAME, "locations/#", LOCATIONS_ID);
    }

    /**
     * Database specific constant declarations
     */

    public static SQLiteDatabase db;
    static final String DATABASE_NAME = "StoreLocations";
    static final String LOCATIONS_TABLE_NAME = "Locations";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + LOCATIONS_TABLE_NAME +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " store_name TEXT NOT NULL, " +
                    " lat TEXT NOT NULL, " +
                    " lng TEXT NOT NULL, " +
                    " offer_title TEXT NOT NULL, " +
                    " offer_desc TEXT NOT NULL, " +
                    " start_date DATETIME NOT NULL, " +
                    " end_date DATETIME NOT NULL);";

    static final String COMPARE_DATE = "SELECT * FROM " + LOCATIONS_TABLE_NAME + " WHERE Date() >= StartDate AND Date() =< EndDate AND DateField = Date()";
    /**
     * Helper class that actually creates and manages
     * the provider's underlying data repository.
     */

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_TABLE);
            ContentValues insertValues = new ContentValues();
            insertValues.put("store_name", "Ghatlodia Police Station");
            insertValues.put("lat", "23.057506");
            insertValues.put("lng", "72.543392");
            insertValues.put("offer_title", "Cashbak");
            insertValues.put("offer_desc", "70% Cashback, Hurry up. Offer till 6th April, 2017 only. Men's wear discount 50%, Women's Wear discount 75%.");
            insertValues.put("start_date", "08/03/2017");
            insertValues.put("end_date", "06/04/2017");
            db.insert(LOCATIONS_TABLE_NAME, null, insertValues);

            insertValues.put("store_name", "Vikram Appts");
            insertValues.put("lat", "23.012102");
            insertValues.put("lng", "72.522634");
            insertValues.put("offer_title", "Redeem Code");
            insertValues.put("offer_desc", "Hurry up. Offer till 6th April, 2017 only. Men's wear discount 50%, Women's Wear discount 75%");
            insertValues.put("start_date", "08/02/2015");
            insertValues.put("end_date", "14/02/2016");
            db.insert(LOCATIONS_TABLE_NAME, null, insertValues);

            insertValues.put("store_name", "Titanium City Center");
            insertValues.put("lat", "23.012102");
            insertValues.put("lng", "72.522634");
            insertValues.put("offer_title", "Whole Sale");
            insertValues.put("offer_desc", "70% Cashback");
            insertValues.put("start_date", "08/02/2015");
            insertValues.put("end_date", "14/02/2016");
            db.insert(LOCATIONS_TABLE_NAME, null, insertValues);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " +  LOCATIONS_TABLE_NAME);
            onCreate(db);
        }
    }


    public static Cursor dateCompare (Date startdate, Date enddate){
        Cursor c = null;
        try {

            String query = "SELECT * FROM " + LOCATIONS_TABLE_NAME + " WHERE Date() >= " + startdate + " AND Date() =< " + enddate;
            c = db.rawQuery(query, null);
            if (c.moveToFirst()) {
                //count = c.getInt(0);
            }
            return c;
        }
        finally {
            if (c != null) {
                c.close();
            }
        }
    }

    public static boolean verification(String _username) throws SQLException {
        int count = -1;
        Cursor c = null;
        try {
            String query = "SELECT COUNT(*) FROM "
                    + LOCATIONS_TABLE_NAME + " WHERE " + _ID + " = ?";
            c = db.rawQuery(query, new String[] {_username});
            if (c.moveToFirst()) {
                count = c.getInt(0);
            }
            return count > 0;
        }
        finally {
            if (c != null) {
                c.close();
            }
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        /**
         * Create a write able database which will trigger its
         * creation if it doesn't already exist.
         */

        db = dbHelper.getWritableDatabase();
        return (db == null)? false:true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        /**
         * Add a new student record
         */
        long rowID = db.insert(	LOCATIONS_TABLE_NAME, "", values);

        /**
         * If record is added successfully
         */
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection,
                        String selection,String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(LOCATIONS_TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case LOCATIONS:
                qb.setProjectionMap(LOCATIONS_PROJECTION_MAP);
                break;

            case LOCATIONS_ID:
                qb.appendWhere( _ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
        }

        if (sortOrder == null || sortOrder == ""){
            /**
             * By default sort on student names
             */
            sortOrder = FIELD_STORE_NAME;
        }

        Cursor c = qb.query(db,	projection,	selection,
                selectionArgs,null, null, sortOrder);
        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    public static boolean deleteTitle(String name)
    {
        return db.delete(LOCATIONS_TABLE_NAME, _ID + "=" + name, null) > 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case LOCATIONS:
                count = db.delete(LOCATIONS_TABLE_NAME, selection, selectionArgs);
                break;

            case LOCATIONS_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete( LOCATIONS_TABLE_NAME, _ID +  " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values,
                      String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case LOCATIONS:
                count = db.update(LOCATIONS_TABLE_NAME, values, selection, selectionArgs);
                break;

            case LOCATIONS_ID:
                count = db.update(LOCATIONS_TABLE_NAME, values,
                        _ID + " = " + uri.getPathSegments().get(1) +
                                (!TextUtils.isEmpty(selection) ? " AND (" +selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri );
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            /**
             * Get all student records
             */
            case LOCATIONS:
                return "vnd.android.cursor.dir/vnd.example.students";
            /**
             * Get a particular student
             */
            case LOCATIONS_ID:
                return "vnd.android.cursor.item/vnd.example.students";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }
}
