package com.amplearch.beaconshop.Utils;

import android.app.Activity;
import android.os.Bundle;

import com.amplearch.beaconshop.ApplicationUtils.MyApplication;

import java.util.ArrayList;

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
