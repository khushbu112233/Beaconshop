package com.amplearch.beaconshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.Utils.TrojanButton;
import com.amplearch.beaconshop.Utils.TrojanCheckBox;
import com.amplearch.beaconshop.Utils.TrojanText;

/**
 * Created by ample-arch on 4/13/2017.
 */

public class ElectClaimOfferAcivity extends AppCompatActivity implements View.OnClickListener
{
    TrojanText tvItemOffers, tvItemDetailOffer ;
    TrojanCheckBox tvCheckAgree ;
    TrojanButton btnItemClaimOffer ;
    ImageView ivFacebook, ivFavorite, ivShare ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.claim_offers);

        tvItemOffers = (TrojanText)findViewById(R.id.tvItemOffer);
        ivFavorite = (ImageView)findViewById(R.id.ivFavorite);
        ivShare = (ImageView)findViewById(R.id.ivShare);
        ivFacebook = (ImageView)findViewById(R.id.ivFacebook);
        btnItemClaimOffer = (TrojanButton)findViewById(R.id.btnItemClaimOffer);

        ivFacebook.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        ivFavorite.setOnClickListener(this);
        btnItemClaimOffer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if (v == ivFacebook)
        {
            Toast.makeText(getApplicationContext(),"Try after some TIME.",Toast.LENGTH_LONG).show();
        }
        if (v == ivShare)
        {
            String shareBody = "Here is the share content body";
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Reddem Code");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share Via"));
        }
        if (v == ivFavorite)
        {
            Toast.makeText(getApplicationContext(),"Added to Favorite.",Toast.LENGTH_LONG).show();
        }
        if (v == btnItemClaimOffer)
        {
            Toast.makeText(getApplicationContext(),"You are not ELIGIBLE to claim this offer.",Toast.LENGTH_LONG).show();
        }
    }
}
