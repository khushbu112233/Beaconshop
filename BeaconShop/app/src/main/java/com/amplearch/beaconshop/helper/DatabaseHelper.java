package com.amplearch.beaconshop.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.amplearch.beaconshop.Model.Favourites;
import com.amplearch.beaconshop.Model.Images;
import com.amplearch.beaconshop.Model.StoreLocation;
import com.amplearch.beaconshop.Model.Voucher;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

public class DatabaseHelper extends SQLiteOpenHelper
{
	// Logcat tag
	private static final String LOG = "DatabaseHelper";
	// Database Version
	private static final int DATABASE_VERSION = 1;
	// Database Name
	private static final String DATABASE_NAME = "BeaconShopDatabase";
	// Table Names
	private static final String TABLE_STORELOCATION = "store_location";
	private static final String TABLE_FAVOURITES = "favourites";
	//	private static final String TABLE_TODO_TAG = "todo_tags";
	private static final String TABLE_VOUCHER = "voucher";
	// Common column names
	private static final String KEY_ID = "id";
	//private static final String KEY_CREATED_AT = "store_name";
	// STORELOCATION & VOUCHER Table - Common column nmaes
	private static final String KEY_STORENAME = "store_name";
	private static final String KEY_LAT = "lat";
	private static final String KEY_LNG = "lng";
	private static final String KEY_OFFERTITLE = "offer_title";
	private static final String KEY_OFFERDESC = "offer_desc";
	private static final String KEY_STARTDATE = "start_date";
	private static final String KEY_ENDDATE = "end_date";
	// FAVOURITES Table - column names
	private static final String KEY_PRODUCTID = "product_id";
	private static final String KEY_USERID = "user_id";
	// VOCHERS Table - column names
	private static final String KEY_CATEGORYID = "category_id";
    private static final String KEY_STORE_IMAGE = "store_image";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_PAID_BANNER = "paid_banner";
    private static final String KEY_PAID_START_DATE = "paid_start_date";
    private static final String KEY_PAID_END_DATE = "paid_end_date";

	private static final String KEY_UUID = "uuid";
	private static final String KEY_MAJOR = "major";
	private static final String KEY_MINOR = "minor";
    private static final String KEY_BEACON_MESSAGE = "message";

	// Home fragment table name
	private static final String TABLE_HOMEFRAG = "homefragment";
	// Home fragment table & columns
	private static final String KEY_NAME = "image_name";
	private static final String KEY_IMAGE = "image_data";
/*
	// NOTE_TAGS Table - column names
	private static final String KEY_TODO_ID = "todo_id";
	private static final String KEY_TAG_ID = "tag_id";*/
	// Table Create Statements
	// Store Locations table create statement
	private static final String CREATE_TABLE_STORELOCATION = "CREATE TABLE "
			+ TABLE_STORELOCATION
			+ "("
			+ KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_PRODUCTID + " TEXT,"
			+ KEY_STORENAME + " TEXT,"
		    + KEY_STORE_IMAGE + " BLOB,"
			+ KEY_LAT + " TEXT,"
			+ KEY_LNG + " TEXT,"
            + KEY_QUANTITY + " TEXT,"
			+ KEY_OFFERTITLE + " TEXT,"
			+ KEY_OFFERDESC + " TEXT,"
			+ KEY_STARTDATE + " DATETIME,"
			+ KEY_ENDDATE
			+ " DATETIME"
			+ ")";

	private static final String CREATE_TABLE_VOUCHER = "CREATE TABLE "
			+ TABLE_VOUCHER
			+ "("
			+ KEY_ID + " INTEGER PRIMARY KEY,"
			+ KEY_PRODUCTID + " TEXT,"
            + KEY_CATEGORYID + " TEXT,"
			+ KEY_STORENAME + " TEXT,"
            + KEY_STORE_IMAGE + " BLOB,"
			+ KEY_LAT + " TEXT,"
			+ KEY_LNG + " TEXT,"
			+ KEY_OFFERTITLE + " TEXT,"
			+ KEY_OFFERDESC + " TEXT,"
			+ KEY_STARTDATE + " DATETIME,"
			+ KEY_ENDDATE + " DATETIME, "
            + KEY_QUANTITY + " TEXT,"
            + KEY_PAID_BANNER + " TEXT,"
            + KEY_PAID_START_DATE + " DATETIME,"
            + KEY_PAID_END_DATE + " DATETIME,"
			+ KEY_BEACON_MESSAGE + " TEXT,"
			+ KEY_UUID + " TEXT,"
			+ KEY_MAJOR + " TEXT,"
			+ KEY_MINOR + " TEXT "
			+ ")";

	// Favourites table create statement
	private static final String CREATE_TABLE_FAVOURITES = "CREATE TABLE "
			+ TABLE_FAVOURITES
			+ "("
			+ KEY_ID + " INTEGER PRIMARY KEY,"
			+ KEY_PRODUCTID + " TEXT,"
			+ KEY_USERID + " TEXT"
			+ ")";

	private static final String CREATE_TABLE_HOMEFRAG = "CREATE TABLE "
			+ TABLE_HOMEFRAG
			+ "("
			+ KEY_ID + "INTEGER PRIMARY KEY,"
			+ KEY_NAME + " TEXT,"
			+ KEY_IMAGE + " BLOB"
			+ ")";

	// todo_tag table create statement
	/*private static final String CREATE_TABLE_TODO_TAG = "CREATE TABLE "
			+ TABLE_TODO_TAG + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
			+ KEY_TODO_ID + " INTEGER," + KEY_TAG_ID + " INTEGER,"
			+ KEY_CREATED_AT + " DATETIME" + ")";
*/
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

    public boolean deleteFavourites(String productt_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_FAVOURITES, KEY_PRODUCTID + "=" + productt_id, null) > 0;
    }

	public boolean verification(String product_id) throws SQLException
	{
		int count = -1;
		Cursor c = null;
		try {
			String query = "SELECT COUNT(*) FROM " + TABLE_FAVOURITES + " WHERE " + KEY_PRODUCTID + " = ?";
			SQLiteDatabase db = this.getWritableDatabase();
			c = db.rawQuery(query, new String[] {product_id});
			if (c.moveToFirst())
			{
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

	public void deleteStoreLocaion(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STORELOCATION, null, null);
    }

    public void deleteVoucher(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_VOUCHER, null, null);
    }

	@Override
	public void onCreate(SQLiteDatabase db)
	{

		// creating required tables
		db.execSQL(CREATE_TABLE_FAVOURITES);
		db.execSQL(CREATE_TABLE_STORELOCATION);
		db.execSQL(CREATE_TABLE_VOUCHER);
		db.execSQL(CREATE_TABLE_HOMEFRAG);

		/*ContentValues insertFavValues = new ContentValues();
		insertFavValues.put("product_id", "5");
		insertFavValues.put("user_id", "11");
		db.insert(TABLE_FAVOURITES, null, insertFavValues);
*/
		/*insertFavValues.put("product_id", "6");
		insertFavValues.put("user_id", "22");
		db.insert(TABLE_FAVOURITES, null, insertFavValues);

		insertFavValues.put("product_id", "7");
		insertFavValues.put("user_id", "55");
		db.insert(TABLE_FAVOURITES, null, insertFavValues);

		insertFavValues.put("product_id", "10");
		insertFavValues.put("user_id", "45");
		db.insert(TABLE_FAVOURITES, null, insertFavValues);

		ContentValues insertVoucherValues = new ContentValues();
		insertVoucherValues.put("product_id", "7");
		insertVoucherValues.put("store_name", "Life Style");
		insertVoucherValues.put("lat", "23.057506");
		insertVoucherValues.put("lng", "72.543392");
		insertVoucherValues.put("offer_title", "50% Off");
		insertVoucherValues.put("offer_desc", "Mega Sale. 50% Off on Summer Collection. ");
		insertVoucherValues.put("start_date", "05/04/2017");
		insertVoucherValues.put("end_date", "05/05/2017");
		insertVoucherValues.put("message", "Mega Sale 50% Off on Life Style Clothing");
		insertVoucherValues.put("uuid", "e71c1a56-58e5-7b1f-abbe-9bf13e06f36a");
		insertVoucherValues.put("major", "0");
		insertVoucherValues.put("minor", "0");
		db.insert(TABLE_VOUCHER, null, insertVoucherValues);*/

		/*insertVoucherValues.put("product_id", "5");
		insertVoucherValues.put("store_name", "Levie");
		insertVoucherValues.put("lat", "23.057506");
		insertVoucherValues.put("lng", "72.543392");
		insertVoucherValues.put("offer_title", "50% Off");
		insertVoucherValues.put("offer_desc", "75% Off on HandBags ");
		insertVoucherValues.put("start_date", "05/04/2017");
		insertVoucherValues.put("end_date", "05/05/2017");
		insertVoucherValues.put("message", "Offer For limited peroid");
		insertVoucherValues.put("uuid", "f18aa677-3b40-48c5-a937-9e2c9e9f8");
		insertVoucherValues.put("major", "0");
		insertVoucherValues.put("minor", "0");
		db.insert(TABLE_VOUCHER, null, insertVoucherValues);*/


		/*ContentValues insertValues = new ContentValues();
		insertValues.put("store_name", "Ghatlodia Police Station");
		insertValues.put("lat", "23.057506");
		insertValues.put("lng", "72.543392");
		insertValues.put("offer_title", "Cashbak");
		insertValues.put("offer_desc", "70% Cashback, Hurry up. Offer till 6th April, 2017 only. Men's wear discount 50%, Women's Wear discount 75%.");
		insertValues.put("start_date", "08/03/2017");
		insertValues.put("end_date", "06/04/2017");
		db.insert(TABLE_STORELOCATION, null, insertValues);

		insertValues.put("store_name", "Vikram Appts");
		insertValues.put("lat", "23.012102");
		insertValues.put("lng", "72.522634");
		insertValues.put("offer_title", "Redeem Code");
		insertValues.put("offer_desc", "Hurry up. Offer till 6th April, 2017 only. Men's wear discount 50%, Women's Wear discount 75%");
		insertValues.put("start_date", "08/02/2015");
		insertValues.put("end_date", "14/02/2016");
		db.insert(TABLE_STORELOCATION, null, insertValues);

		insertValues.put("store_name", "Titanium City Center");
		insertValues.put("lat", "23.012102");
		insertValues.put("lng", "72.522634");
		insertValues.put("offer_title", "Whole Sale");
		insertValues.put("offer_desc", "70% Cashback");
		insertValues.put("start_date", "08/02/2015");
		insertValues.put("end_date", "14/02/2016");
		db.insert(TABLE_STORELOCATION, null, insertValues);*/
		//db.execSQL(CREATE_TABLE_TODO_TAG);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORELOCATION);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_VOUCHER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOMEFRAG);
		//db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO_TAG);

		// create new tables
		onCreate(db);
	}

	public void addImageData(String name, byte[] image)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(KEY_NAME, name);
		cv.put(KEY_NAME, image);
		db.insert(TABLE_HOMEFRAG, null , cv);
		db.close();
	}

	public List<Images> getImageData()
    {
        List<Images> todos = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String selecQuery  =  "SELECT * FROM " + TABLE_HOMEFRAG ;
        Cursor c = db.rawQuery(selecQuery, null);

        if(c.moveToFirst())
        {
            do
            {
				try
				{
					Images td = new Images();
					td.setId(c.getInt(0));
					td.setName(c.getString(1));
					td.setImage(c.getBlob(2));

				}
				catch (Exception e) {
					e.printStackTrace();
				}
            }
            while (c.moveToNext());
        }
        db.close();

        return todos;
    }

	// ------------------------ "todos" table methods ----------------//

	/*
	 * Creating a todo
	 */
	public long createFavourites(Favourites favourites, long[] tag_ids) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, favourites.getId());
		values.put(KEY_PRODUCTID, favourites.getProduct_id());
		values.put(KEY_USERID, favourites.getUser_id());

		// insert row
		long todo_id = db.insert(TABLE_FAVOURITES, null, values);

		// insert tag_ids
		/*for (long tag_id : tag_ids) {
			createTodoTag(todo_id, tag_id);
		}*/

		return todo_id;
	}

	public List<Favourites> getFavoritesData()
	{
		List<Favourites> todos = new ArrayList<Favourites>();

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor c = null ;

		int value = 3 ;

		try{
			String selectQuery = TABLE_FAVOURITES + " * " + KEY_ID + " IN " + value ;

			Log.d(LOG, selectQuery);

			String Table = TABLE_FAVOURITES ;
			String[] table_Columns = new String[] { " * " };
			String[] args_Data = new String[] { "1,3,5,7" };
			String selctioonArgs = Arrays.toString(args_Data);

			selctioonArgs = selctioonArgs.replace("[","(");
			selctioonArgs = selctioonArgs.replace("]",")");

			String whereClause = KEY_ID + " IN " + selctioonArgs ;

			c = db.query(Table,table_Columns,whereClause,null,null,null,null);
		}catch (Exception e){
			e.printStackTrace();
		}
		if(c.moveToFirst())
		{
		do
			{
				Favourites fvt = new Favourites();
				fvt.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				fvt.setProduct_id((c.getString(c.getColumnIndex(KEY_PRODUCTID))));
				fvt.setUser_id(c.getString(c.getColumnIndex(KEY_USERID)));
				todos.add(fvt);
			}
			while (c.moveToNext());
		}
		return todos;
	}
	/*
	 * get single todo
	 */
	public Favourites getFavourites(long todo_id) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_FAVOURITES + " WHERE " + KEY_ID + " = " + todo_id;

		Log.e(LOG, selectQuery);

		Cursor c = db.rawQuery(selectQuery, null);

		if (c != null)
			c.moveToFirst();

		Favourites td = new Favourites();
		td.setId(c.getInt(c.getColumnIndex(KEY_ID)));
		td.setProduct_id((c.getString(c.getColumnIndex(KEY_PRODUCTID))));
		td.setUser_id(c.getString(c.getColumnIndex(KEY_USERID)));

		return td;
	}

//	public Voucher getVoucherByProductId(long todo_id) {
//		SQLiteDatabase db = this.getReadableDatabase();
//
//		String selectQuery = "SELECT  * FROM " + TABLE_VOUCHER + " WHERE " + KEY_PRODUCTID + " = " + todo_id;
//
//		Log.e(LOG, selectQuery);
//
//		Cursor c = db.rawQuery(selectQuery, null);
//
//		if (c != null)
//			c.moveToFirst();
//
//		Voucher td = new Voucher();
//		td.setId(c.getInt(c.getColumnIndex(KEY_ID)));
//		td.setProduct_id((c.getString(c.getColumnIndex(KEY_PRODUCTID))));
//		td.setStore_name(c.getString(c.getColumnIndex(KEY_STORENAME)));
//
//		return td;
//	}


/*		public List<Voucher> getVoucherByProductId()
	{
		List<Voucher> todos = new ArrayList<Voucher>();

		String fav_id = String.valueOf(5);
//		String selectQuery = "SELECT  * FROM " + TABLE_VOUCHER + " WHERE product_id = 5" ;
		String selectQuery = "SELECT * FROM "+ TABLE_VOUCHER + " WHERE "+ KEY_PRODUCTID + "=" +fav_id;

//		String selectQuery = "SELECT * FROM " + TABLE_VOUCHER + ",favourites WHERE voucher.product_id = favourites.product_id" ;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (c.moveToFirst())
		{
			do {
				Voucher td = new Voucher();
				td.setProduct_id(c.getString((c.getColumnIndex(KEY_PRODUCTID))));
				td.setMessage((c.getString(c.getColumnIndex(KEY_BEACON_MESSAGE))));
				td.setOffer_title(c.getString(c.getColumnIndex(KEY_OFFERTITLE)));
				td.setStore_name(c.getString(c.getColumnIndex(KEY_STORENAME)));
				td.setUuid(c.getString(c.getColumnIndex(KEY_UUID)));
				td.setEnd_date(c.getString(c.getColumnIndex(KEY_ENDDATE)));
				td.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				td.setLat(c.getString(c.getColumnIndex(KEY_LAT)));
				td.setLng(c.getString(c.getColumnIndex(KEY_LNG)));
				td.setMajor(c.getString(c.getColumnIndex(KEY_MAJOR)));
				td.setMinor(c.getString(c.getColumnIndex(KEY_MINOR)));
				td.setOffer_desc(c.getString(c.getColumnIndex(KEY_OFFERDESC)));
				td.setStart_date(c.getString(c.getColumnIndex(KEY_STARTDATE)));
				// adding to todo list
				todos.add(td);
			}
			while (c.moveToNext());
		}

		return todos;
	}*/

	public List<Voucher> getVoucherbyID()
	{
		List<Voucher> todos = new ArrayList<Voucher>();
		SQLiteDatabase db = this.getReadableDatabase();

		String [] settingsProjection = new String[] { "*" };
		String [] favData = getAllFavourites();

		if (favData == null || favData.length == 0)
			return null;

		String whereArgs = Arrays.toString(favData);

		//at this point inClause will look like "[23,343,33,55,43]"
		//replace the brackets with parentheses
		whereArgs = whereArgs.replace("[","(");
		whereArgs = whereArgs.replace("]",")");
		//now inClause will look like  "(23,343,33,55,43)" so use it to construct your SELECT
		///"id IN (1,2,3,4)"
		String whereClause = KEY_PRODUCTID + " IN " + whereArgs;
		Cursor c = null;
		try
		{
            c = db.query( TABLE_VOUCHER, settingsProjection, whereClause, null, null, null, null);
			Log.d(LOG, String.valueOf(c));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		// looping through all rows and adding to list
		if (c.moveToFirst())
		{
			do {
				Voucher td = new Voucher();

                td.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                td.setProduct_id(c.getString(c.getColumnIndex(KEY_PRODUCTID)));
                td.setCategory_id(c.getString(c.getColumnIndex(KEY_CATEGORYID)));
                td.setStore_name(c.getString(c.getColumnIndex(KEY_STORENAME)));
                td.setStore_image(c.getBlob(c.getColumnIndex(KEY_STORE_IMAGE)));
                td.setLat(c.getString(c.getColumnIndex(KEY_LAT)));
                td.setLng(c.getString(c.getColumnIndex(KEY_LNG)));
                td.setOffer_title(c.getString(c.getColumnIndex(KEY_OFFERTITLE)));
                td.setOffer_desc(c.getString(c.getColumnIndex(KEY_OFFERDESC)));
                td.setStart_date(c.getString(c.getColumnIndex(KEY_STARTDATE)));
                td.setEnd_date(c.getString(c.getColumnIndex(KEY_ENDDATE)));
                td.setQuantity(c.getString(c.getColumnIndex(KEY_QUANTITY)));
                td.setPaid_banner(c.getString(c.getColumnIndex(KEY_PAID_BANNER)));
                td.setPaid_start_date(c.getString(c.getColumnIndex(KEY_PAID_START_DATE)));
                td.setPaid_end_date(c.getString(c.getColumnIndex(KEY_PAID_END_DATE)));
                td.setMessage(c.getString(c.getColumnIndex(KEY_BEACON_MESSAGE)));
                td.setUuid(c.getString(c.getColumnIndex(KEY_UUID)));
                td.setMajor(c.getString(c.getColumnIndex(KEY_MAJOR)));
                td.setMinor(c.getString(c.getColumnIndex(KEY_MINOR)));

				// adding to todo list
				todos.add(td);
			} while (c.moveToNext());
		}

		return todos ;
	}

	public String makePlaceholders(int len) {
		StringBuilder sb = new StringBuilder(len * 2 - 1);
		sb.append("?");
		for (int i = 1; i < len; i++)
			sb.append(",?");
		return sb.toString();
	}

	/**
	 * getting all todos
	 * */
	public String[] getAllFavourites() {
		List<String> todos = new ArrayList<String>();
		String selectQuery = "SELECT  * FROM " + TABLE_FAVOURITES;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				// adding to todo list
				todos.add((c.getString(c.getColumnIndex(KEY_PRODUCTID))));
			} while (c.moveToNext());
		}

		String[] FavId = new String[todos.size()];
		FavId = todos.toArray(FavId);//now strings is the resulting array

		return FavId;
	}


    public void addRecord(String product_id,String category_id, String store_name, byte[] store_image,
                          String lat, String lng, String offer_title, String offer_desc,String start_date,
                          String end_date, String quantity, String paid_banner, String paid_start_date, String paid_end_date,
                          String message, String uuid, String major, String minor){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();

        /*initialValues.put(KEY_DESCRIPTION , description);
        initialValues.put(KEY_WHY, why);
        initialValues.put(KEY_ACCOUNTABILITY, accontability);
        initialValues.put(KEY_ALARM_TYPE, alarm_type);

        initialValues.put(KEY_ALARM_VOLUME, alarm_vol);
        initialValues.put(KEY_BG_IMAGE, bg_image);
        initialValues.put(KEY_CATEGORY, category);
        initialValues.put(KEY_DATE_TIME, goalDateTime);
        initialValues.put(KEY_STATUS, status);*/


        initialValues.put(KEY_PRODUCTID, product_id);
        initialValues.put(KEY_CATEGORYID, category_id);
        initialValues.put(KEY_STORENAME, store_name);
        initialValues.put(KEY_STORE_IMAGE, store_image);
        initialValues.put(KEY_LAT, lat);
        initialValues.put(KEY_LNG, lng);
        initialValues.put(KEY_OFFERTITLE, offer_title);
        initialValues.put(KEY_OFFERDESC, offer_desc);
        initialValues.put(KEY_STARTDATE, start_date);
        initialValues.put(KEY_ENDDATE, end_date);
        initialValues.put(KEY_QUANTITY, quantity);
        initialValues.put(KEY_PAID_BANNER, paid_banner);
        initialValues.put(KEY_PAID_START_DATE, paid_start_date);
        initialValues.put(KEY_PAID_END_DATE, paid_end_date);
        initialValues.put(KEY_BEACON_MESSAGE, message);
        initialValues.put(KEY_UUID, uuid);
        initialValues.put(KEY_MAJOR, major);
        initialValues.put(KEY_MINOR, minor);



        long rowId = db.insert(TABLE_VOUCHER , null, initialValues);


    }

    public void addFavourites(String product_id,String user_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();

        /*initialValues.put(KEY_DESCRIPTION , description);
        initialValues.put(KEY_WHY, why);
        initialValues.put(KEY_ACCOUNTABILITY, accontability);
        initialValues.put(KEY_ALARM_TYPE, alarm_type);

        initialValues.put(KEY_ALARM_VOLUME, alarm_vol);
        initialValues.put(KEY_BG_IMAGE, bg_image);
        initialValues.put(KEY_CATEGORY, category);
        initialValues.put(KEY_DATE_TIME, goalDateTime);
        initialValues.put(KEY_STATUS, status);*/


        initialValues.put(KEY_PRODUCTID, product_id);
        initialValues.put(KEY_USERID, user_id);

        long rowId = db.insert(TABLE_FAVOURITES , null, initialValues);


    }


    public List<Voucher> getAllVouchers()
	{
		List<Voucher> voucher = new ArrayList<Voucher>();
//		String selectQuery = "SELECT * FROM " + TABLE_VOUCHER + " WHERE " + KEY_PRODUCTID + " = " + 5 ;
		String selectQuery ="SELECT * FROM " + TABLE_VOUCHER ;
//		String selectQuery ="SELECT * FROM " + TABLE_VOUCHER + " WHERE " + KEY_STORENAME + " = " +"Levie";

		Log.e(LOG,selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);


		if(c.moveToFirst()){
			do {
				Voucher vc = new Voucher();
				vc.setId(c.getInt(c.getColumnIndex(KEY_ID)));
				vc.setProduct_id(c.getString(c.getColumnIndex(KEY_PRODUCTID)));
				vc.setStore_name(c.getString(c.getColumnIndex(KEY_STORENAME)));
				vc.setOffer_title(c.getString(c.getColumnIndex(KEY_OFFERTITLE)));
				vc.setOffer_desc(c.getString(c.getColumnIndex(KEY_OFFERDESC)));
				vc.setStart_date(c.getString(c.getColumnIndex(KEY_STARTDATE)));
				vc.setEnd_date(c.getString(c.getColumnIndex(KEY_ENDDATE)));
				voucher.add(vc);
			}
			while (c.moveToNext());
		}

		return voucher;
	}
    public List<Voucher> getVoucherbyID(String tag_name) {
        List<Voucher> todos = new ArrayList<Voucher>();

        String selectQuery = "SELECT  * FROM " + TABLE_VOUCHER + " WHERE "
                + KEY_PRODUCTID + " = " + tag_name;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Voucher td = new Voucher();
                td.setProduct_id(c.getString((c.getColumnIndex(KEY_PRODUCTID))));
                td.setMessage((c.getString(c.getColumnIndex(KEY_BEACON_MESSAGE))));
                td.setOffer_title(c.getString(c.getColumnIndex(KEY_OFFERTITLE)));
                td.setStore_name(c.getString(c.getColumnIndex(KEY_STORENAME)));
                td.setUuid(c.getString(c.getColumnIndex(KEY_UUID)));
                td.setEnd_date(c.getString(c.getColumnIndex(KEY_ENDDATE)));
                td.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                td.setLat(c.getString(c.getColumnIndex(KEY_LAT)));
                td.setLng(c.getString(c.getColumnIndex(KEY_LNG)));
                td.setMajor(c.getString(c.getColumnIndex(KEY_MAJOR)));
                td.setMinor(c.getString(c.getColumnIndex(KEY_MINOR)));
                td.setOffer_desc(c.getString(c.getColumnIndex(KEY_OFFERDESC)));
                td.setStart_date(c.getString(c.getColumnIndex(KEY_STARTDATE)));

                // adding to todo list
                todos.add(td);
            } while (c.moveToNext());
        }

        return todos;
    }

	/**
	 * getting all todos under single tag
	 * */
	/*public List<Favourites> getAllToDosByTag(String tag_name) {
		List<Favourites> todos = new ArrayList<Favourites>();

		String selectQuery = "SELECT  * FROM " + TABLE_FAVOURITES + " td, "
				+ TABLE_TAG + " tg, " + TABLE_TODO_TAG + " tt WHERE tg."
				+ KEY_TAG_NAME + " = '" + tag_name + "'" + " AND tg." + KEY_ID
				+ " = " + "tt." + KEY_TAG_ID + " AND td." + KEY_ID + " = "
				+ "tt." + KEY_TODO_ID;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Favourites td = new Favourites();
				td.setId(c.getInt((c.getColumnIndex(KEY_ID))));
				td.setNote((c.getString(c.getColumnIndex(KEY_TODO))));
				td.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

				// adding to todo list
				todos.add(td);
			} while (c.moveToNext());
		}

		return todos;
	}
*/
	/*
	 * getting todo count
	 */
	public int getFavouritesCount() {
		String countQuery = "SELECT  * FROM " + TABLE_FAVOURITES;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		int count = cursor.getCount();
		cursor.close();

		// return count
		return count;
	}

	/*
	 * Updating a todo
	 */
	public int updateFavourites(Favourites todo) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_PRODUCTID, todo.getProduct_id());
		values.put(KEY_USERID, todo.getUser_id());

		// updating row
		return db.update(TABLE_FAVOURITES, values, KEY_ID + " = ?",
				new String[] { String.valueOf(todo.getId()) });
	}

	/*
	 * Deleting a todo
	 */
	public void deleteFavorites(long tado_id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_FAVOURITES, KEY_ID + " = ?",
				new String[] { String.valueOf(tado_id) });
	}

	// ------------------------ "tags" table methods ----------------//

	/*
	 * Creating tag
	 */
	public long createStoreLocation(StoreLocation tag) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
        values.put(KEY_PRODUCTID, tag.getProduct_id());
		values.put(KEY_STORENAME, tag.getStore_name());
        values.put(KEY_STORE_IMAGE, tag.getStore_image() );
		values.put(KEY_LAT, tag.getLat());
		values.put(KEY_LNG, tag.getLng());
        values.put(KEY_QUANTITY, tag.getQuantity());
		values.put(KEY_OFFERTITLE, tag.getOffer_title());
		values.put(KEY_OFFERDESC, tag.getOffer_desc());
		values.put(KEY_STARTDATE, tag.getStart_date());
		values.put(KEY_ENDDATE, tag.getEnd_date());

		// insert row
		long tag_id = db.insert(TABLE_STORELOCATION, null, values);

		return tag_id;
	}

	public long createFavorites(Favourites tag) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_PRODUCTID, tag.getProduct_id());
		values.put(KEY_USERID, tag.getUser_id());

		// insert row
		long tag_id = db.insert(TABLE_FAVOURITES, null, values);

		return tag_id;
	}

    public long createVoucher(Voucher tag) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
		values.put(KEY_PRODUCTID, tag.getProduct_id());
		values.put(KEY_CATEGORYID, tag.getCategory_id());
        values.put(KEY_STORENAME, tag.getStore_name());
		values.put(KEY_STORE_IMAGE, tag.getStore_image());
        values.put(KEY_LAT, tag.getLat());
        values.put(KEY_LNG, tag.getLng());
        values.put(KEY_OFFERTITLE, tag.getOffer_title());
        values.put(KEY_OFFERDESC, tag.getOffer_desc());
        values.put(KEY_STARTDATE, tag.getStart_date());
        values.put(KEY_ENDDATE, tag.getEnd_date());
		values.put(KEY_QUANTITY, tag.getQuantity());
		values.put(KEY_PAID_BANNER, tag.getPaid_banner());
		values.put(KEY_PAID_START_DATE, tag.getPaid_start_date());
		values.put(KEY_PAID_END_DATE, tag.getPaid_end_date());
        values.put(KEY_BEACON_MESSAGE, tag.getMessage());
        values.put(KEY_UUID, tag.getUuid());
        values.put(KEY_MAJOR, tag.getMajor());
        values.put(KEY_MINOR, tag.getMinor());


        // insert row
        long tag_id = db.insert(TABLE_VOUCHER, null, values);

        return tag_id;
    }

	/**
	 * getting all tags
	 * */
	public List<StoreLocation> getAllLocations() {
		List<StoreLocation> tags = new ArrayList<StoreLocation>();
		String selectQuery = "SELECT  * FROM " + TABLE_STORELOCATION;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				StoreLocation t = new StoreLocation();
				t.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                t.setProduct_id(c.getString(c.getColumnIndex(KEY_PRODUCTID)));
				t.setStore_name(c.getString(c.getColumnIndex(KEY_STORENAME)));
                t.setStore_image(c.getBlob(c.getColumnIndex(KEY_STORE_IMAGE)));
				t.setLat(c.getString(c.getColumnIndex(KEY_LAT)));
				t.setLng(c.getString(c.getColumnIndex(KEY_LNG)));
                t.setQuantity(c.getString(c.getColumnIndex(KEY_QUANTITY)));
				t.setOffer_title(c.getString(c.getColumnIndex(KEY_OFFERTITLE)));
				t.setOffer_desc(c.getString(c.getColumnIndex(KEY_OFFERDESC)));
				t.setStart_date(c.getString(c.getColumnIndex(KEY_STARTDATE)));
				t.setEnd_date(c.getString(c.getColumnIndex(KEY_ENDDATE)));

				// adding to tags list
				tags.add(t);
			} while (c.moveToNext());
		}
		return tags;
	}


    public List<Favourites> getAllFavouritesRecords() {
        List<Favourites> tags = new ArrayList<Favourites>();
        String selectQuery = "SELECT  * FROM " + TABLE_FAVOURITES;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Favourites t = new Favourites();
                t.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                t.setProduct_id(c.getString(c.getColumnIndex(KEY_PRODUCTID)));
                t.setUser_id(c.getString(c.getColumnIndex(KEY_USERID)));
                // adding to tags list
                tags.add(t);
            } while (c.moveToNext());
        }
        return tags;
    }

	public List<Voucher> getAllBeaconVouchers() {
		List<Voucher> tags = new ArrayList<Voucher>();
		String selectQuery = "SELECT  * FROM " + TABLE_VOUCHER;

		Log.e(LOG, selectQuery);

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Voucher t = new Voucher();
				t.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                t.setProduct_id(c.getString(c.getColumnIndex(KEY_PRODUCTID)));
                t.setCategory_id(c.getString(c.getColumnIndex(KEY_CATEGORYID)));
				t.setStore_name(c.getString(c.getColumnIndex(KEY_STORENAME)));
                t.setStore_image(c.getBlob(c.getColumnIndex(KEY_STORE_IMAGE)));
				t.setLat(c.getString(c.getColumnIndex(KEY_LAT)));
				t.setLng(c.getString(c.getColumnIndex(KEY_LNG)));
				t.setOffer_title(c.getString(c.getColumnIndex(KEY_OFFERTITLE)));
				t.setOffer_desc(c.getString(c.getColumnIndex(KEY_OFFERDESC)));
				t.setStart_date(c.getString(c.getColumnIndex(KEY_STARTDATE)));
				t.setEnd_date(c.getString(c.getColumnIndex(KEY_ENDDATE)));
                t.setQuantity(c.getString(c.getColumnIndex(KEY_QUANTITY)));
                t.setPaid_banner(c.getString(c.getColumnIndex(KEY_PAID_BANNER)));
                t.setPaid_start_date(c.getString(c.getColumnIndex(KEY_PAID_START_DATE)));
                t.setPaid_end_date(c.getString(c.getColumnIndex(KEY_PAID_END_DATE)));
				t.setMessage(c.getString(c.getColumnIndex(KEY_BEACON_MESSAGE)));
				t.setUuid(c.getString(c.getColumnIndex(KEY_UUID)));
				t.setMajor(c.getString(c.getColumnIndex(KEY_MAJOR)));
				t.setMinor(c.getString(c.getColumnIndex(KEY_MINOR)));


                // adding to tags list
				tags.add(t);
			} while (c.moveToNext());
		}
		return tags;
	}

	/*
	 * Updating a tag
	 */
	public int updateTag(StoreLocation tag) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_STORENAME, tag.getStore_name());

		// updating row
		return db.update(TABLE_STORELOCATION, values, KEY_ID + " = ?",
				new String[] { String.valueOf(tag.getId()) });
	}

	/*
	 * Deleting a tag
	 */
	public void deleteTag(StoreLocation tag, boolean should_delete_all_tag_todos) {
		SQLiteDatabase db = this.getWritableDatabase();

		// before deleting tag
		// check if todos under this tag should also be deleted

		// now delete the tag
		db.delete(TABLE_STORELOCATION, KEY_ID + " = ?",
				new String[] { String.valueOf(tag.getId()) });
	}

	// ------------------------ "todo_tags" table methods ----------------//

	/*
	 * Creating todo_tag
	 */
	/*public long createTodoTag(long todo_id, long tag_id) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TODO_ID, todo_id);
		values.put(KEY_TAG_ID, tag_id);
		values.put(KEY_CREATED_AT, getDateTime());

		long id = db.insert(TABLE_TODO_TAG, null, values);

		return id;
	}*/

	/*
	 * Updating a todo tag
	 */
	/*public int updateNoteTag(long id, long tag_id) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TAG_ID, tag_id);

		// updating row
		return db.update(TABLE_TODO, values, KEY_ID + " = ?",
				new String[] { String.valueOf(id) });
	}*/

	/*
	 * Deleting a todo tag
	 */
/*	public void deleteToDoTag(long id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_TODO, KEY_ID + " = ?",
				new String[] { String.valueOf(id) });
	}*/

	// closing database
	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}

	/**
	 * get datetime
	 * */
	private String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		Date date = new Date();
		return dateFormat.format(date);
	}
}
