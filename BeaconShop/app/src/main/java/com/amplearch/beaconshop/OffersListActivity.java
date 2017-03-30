package com.amplearch.beaconshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers_list);

        web = new String[imageId.length];
        listOffer =(ListView) findViewById(R.id.offers_list);
        OffersAdapter adapterViewAndroid = new OffersAdapter(OffersListActivity.this, web, imageId);
        listOffer.setAdapter(adapterViewAndroid);
        listOffer.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int i, long id) {
                //  Toast.makeText(getContext(), "GridView Item: " , Toast.LENGTH_LONG).show();
               /* Intent intent = new Intent(getApplicationContext(), StoreListActivity.class);
                startActivity(intent);*/
            }
        });
    }
}
