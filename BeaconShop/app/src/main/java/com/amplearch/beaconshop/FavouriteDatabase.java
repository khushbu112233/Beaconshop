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
 * Created by admin on 04/07/2017.
 */

public class FavouriteDatabase extends ContentProvider
{

    static final String PROVIDER_NAME = "com.amplearch.beaconshop.FavouriteDatabase";
    static final String URL = "content://" + PROVIDER_NAME + "/favourites";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    public static final String _ID = "_id";

    public static final String FIELD_PRODUCT_ID = "product_id";

    /** Field 2 of the table locations, stores the latitude */
    public static final String FIELD_USER_ID = "user_id";

    /** Field 3 of the table locations, stores the longitude*/
    private static HashMap<String, String> FAVOURITES_PROJECTION_MAP;

    static final int FAVOURITES = 1;
    static final int FAVOURITES_ID = 2;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "favourites", FAVOURITES);
        uriMatcher.addURI(PROVIDER_NAME, "favourites/#", FAVOURITES_ID);
    }

    /**
     * Database specific constant declarations
     */

    public static SQLiteDatabase db;
    static final String DATABASE_NAME = "FavouritesDatabase";
    static final String FAVOURITES_TABLE_NAME = "Favourites";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + FAVOURITES_TABLE_NAME +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " product_id TEXT NOT NULL, " +
                    " user_id DATETIME NOT NULL);";

  //  static final String COMPARE_DATE = "SELECT * FROM " + LOCATIONS_TABLE_NAME + " WHERE Date() >= StartDate AND Date() =< EndDate AND DateField = Date()";
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
            insertValues.put("product_id", "1");
            insertValues.put("user_id", "23");
            db.insert(FAVOURITES_TABLE_NAME, null, insertValues);

            insertValues.put("product_id", "2");
            insertValues.put("user_id", "25");
            db.insert(FAVOURITES_TABLE_NAME, null, insertValues);

            insertValues.put("product_id", "3");
            insertValues.put("user_id", "44");
            db.insert(FAVOURITES_TABLE_NAME, null, insertValues);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " +  FAVOURITES_TABLE_NAME);
            onCreate(db);
        }
    }


    /*public static Cursor dateCompare (Date startdate, Date enddate){
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
    }*/

    public static boolean verification(String _username) throws SQLException {
        int count = -1;
        Cursor c = null;
        try {
            String query = "SELECT COUNT(*) FROM "
                    + FAVOURITES_TABLE_NAME + " WHERE " + _ID + " = ?";
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
        long rowID = db.insert(	FAVOURITES_TABLE_NAME, "", values);

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
        qb.setTables(FAVOURITES_TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case FAVOURITES:
                qb.setProjectionMap(FAVOURITES_PROJECTION_MAP);
                break;

            case FAVOURITES_ID:
                qb.appendWhere( _ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
        }

        if (sortOrder == null || sortOrder == ""){
            /**
             * By default sort on student names
             */
            sortOrder = FIELD_PRODUCT_ID;
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
        return db.delete(FAVOURITES_TABLE_NAME, _ID + "=" + name, null) > 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case FAVOURITES:
                count = db.delete(FAVOURITES_TABLE_NAME, selection, selectionArgs);
                break;

            case FAVOURITES_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete( FAVOURITES_TABLE_NAME, _ID +  " = " + id +
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
            case FAVOURITES:
                count = db.update(FAVOURITES_TABLE_NAME, values, selection, selectionArgs);
                break;

            case FAVOURITES_ID:
                count = db.update(FAVOURITES_TABLE_NAME, values,
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
            case FAVOURITES:
                return "vnd.android.cursor.dir/vnd.example.students";
            /**
             * Get a particular student
             */
            case FAVOURITES_ID:
                return "vnd.android.cursor.item/vnd.example.students";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    public Cursor getData()
    {

        return null;
    }
}
