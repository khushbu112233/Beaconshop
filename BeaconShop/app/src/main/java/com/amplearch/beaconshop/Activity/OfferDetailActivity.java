package com.amplearch.beaconshop.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amplearch.beaconshop.R;

public class OfferDetailActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imgFavourite, imgShare;
    TextView tvClaimOffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_detail);

        imgFavourite = (ImageView) findViewById(R.id.imgFavourites);
        imgShare = (ImageView) findViewById(R.id.imgShare);

        tvClaimOffer = (TextView) findViewById(R.id.imgClaimOffer);

        imgFavourite.setOnClickListener(this);
        imgShare.setOnClickListener(this);
        tvClaimOffer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == imgFavourite){
            Toast.makeText(getApplicationContext(), "Added to Favourites..", Toast.LENGTH_LONG).show();
        }
        if (v == imgShare){

            String shareBody = "Here is the share content body";
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Reddem Code");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share Via"));

        }
        if (v == tvClaimOffer){
            Toast.makeText(getApplicationContext(), "You have Claimed Offer Successfully..", Toast.LENGTH_LONG).show();
        }

    }
}
