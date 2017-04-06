package com.amplearch.beaconshop.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.amplearch.beaconshop.Adapter.OffersAdapter;
import com.amplearch.beaconshop.R;

public class OffersListActivity extends AppCompatActivity {

    ListView listOffer;
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
        setContentView(R.layout.activity_offers_list);

        topToolBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);
        // topToolBar.setLogo(R.mipmap.ic_launcher);
        topToolBar.setLogoDescription("Offers");
        getSupportActionBar().setTitle("Offers");

        web = new String[imageId.length];
        listOffer =(ListView) findViewById(R.id.offers_list);
        OffersAdapter adapterViewAndroid = new OffersAdapter(OffersListActivity.this, web, imageId);
        listOffer.setAdapter(adapterViewAndroid);
        listOffer.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int i, long id) {
                //  Toast.makeText(getContext(), "GridView Item: " , Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), OfferDetailActivity.class);
                startActivity(intent);
            }
        });
    }
}
