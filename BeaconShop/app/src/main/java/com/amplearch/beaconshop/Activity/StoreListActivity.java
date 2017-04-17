package com.amplearch.beaconshop.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.amplearch.beaconshop.Adapter.StoreListAdapter;
import com.amplearch.beaconshop.ConnectivityReceiver;
import com.amplearch.beaconshop.R;

public class StoreListActivity extends AppCompatActivity {

    GridView storeGrid;
    String[] web;
    int[] imageId = {
            R.drawable.ic_sale,
            R.drawable.ic_sale,
            R.drawable.ic_sale,
            R.drawable.ic_sale,
            R.drawable.ic_sale,
            R.drawable.ic_sale,
            R.drawable.ic_sale

    };
    private Toolbar topToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);
        checkConnection();
        topToolBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);
        // topToolBar.setLogo(R.mipmap.ic_launcher);
        topToolBar.setLogoDescription("Stores");
        getSupportActionBar().setTitle("Stores");

        web = new String[imageId.length];
        storeGrid =(GridView) findViewById(R.id.store_list);
        StoreListAdapter adapterViewAndroid = new StoreListAdapter(StoreListActivity.this, web, imageId);
        storeGrid.setAdapter(adapterViewAndroid);
        storeGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int i, long id) {
                //  Toast.makeText(getContext(), "GridView Item: " , Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), OffersListActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
        return isConnected ;
    }

    private void showSnack(boolean isConnected) {
        String message = "Sorry! No Internet connection.";
        if (isConnected) {
//            message = "Good! Connected to Internet";
        } else {
//            message = "Sorry! Not connected to internet";
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}
