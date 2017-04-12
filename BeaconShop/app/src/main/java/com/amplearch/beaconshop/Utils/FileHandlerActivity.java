package com.amplearch.beaconshop.Utils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.amplearch.beaconshop.ApplicationUtils.MyApplication;
import com.amplearch.beaconshop.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Handles the listing of files and allows the user to select files for deletion and/or e-mailing.
 * @author justin
 *
 */
public class FileHandlerActivity extends Activity {
	
	private FileHelper fileHelper;
	private ArrayList<String> list;
	private static String selectedItem;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
	    super.onCreate(savedInstanceState);
	    
	    // Get the file helper and list files.
		MyApplication app = (MyApplication) this.getApplication();
		fileHelper = app.getFileHelper();
	    
	   // setContentView(R.layout.activity_filelist);
	    getActionBar().setDisplayHomeAsUpEnabled(true);
	    
	  //  refreshFileList();

	}

	
	// Handle the user selecting items from the action bar.

	/**
	 * Refresh the list of files so the current state is always displayed. 
	 * We call this when intialising the activity, and also after deleting a file.  
	 */
	
}
