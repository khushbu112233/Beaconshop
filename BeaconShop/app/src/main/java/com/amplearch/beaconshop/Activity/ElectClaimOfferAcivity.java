package com.amplearch.beaconshop.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.Utils.TrojanButton;
import com.amplearch.beaconshop.Utils.TrojanCheckBox;
import com.amplearch.beaconshop.Utils.TrojanText;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

/**
 * Created by ample-arch on 4/13/2017.
 */

public class ElectClaimOfferAcivity extends AppCompatActivity implements View.OnClickListener
{
    TrojanText tvItemOffers, tvItemOfferDetails ;
    TrojanCheckBox tvCheckAgree ;
    TrojanButton btnItemClaimOffer ;
    ImageView ivFacebook, ivFavorite, ivShare ;
    String offer_title, offer_desc;
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        facebookSDKInitialize();
        setContentView(R.layout.claim_offers);
        shareDialog = new ShareDialog(this);
        Intent intent = getIntent();
        offer_title = intent.getStringExtra("offer_title");
        offer_desc = intent.getStringExtra("offer_desc");

        tvItemOffers = (TrojanText)findViewById(R.id.tvItemOffer);
        tvItemOfferDetails = (TrojanText) findViewById(R.id.tvItemOfferDetails);
        ivFavorite = (ImageView)findViewById(R.id.ivFavorite);
        ivShare = (ImageView)findViewById(R.id.ivShare);
        ivFacebook = (ImageView)findViewById(R.id.ivFacebook);
        btnItemClaimOffer = (TrojanButton)findViewById(R.id.btnItemClaimOffer);

        tvItemOffers.setText(offer_title);
        tvItemOfferDetails.setText(offer_desc);
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
            if (ShareDialog.canShow(ShareLinkContent.class)) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentTitle("How to integrate Linkedin from your app")
                        .setImageUrl(Uri.parse("https://www.numetriclabz.com/wp-content/uploads/2015/11/114.png"))
                        .setContentDescription(
                                "simple LinkedIn integration")
                        .setContentUrl(Uri.parse("https://www.numetriclabz.com/android-linkedin-integration-login-tutorial/"))
                        .build();

                shareDialog.show(linkContent);  // Show facebook ShareDialog
            }
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
            Toast.makeText(getApplicationContext(),"Wait under process to add favorite.",Toast.LENGTH_LONG).show();
        }
        if (v == btnItemClaimOffer)
        {
            Toast.makeText(getApplicationContext(),"You are not ELIGIBLE to claim this offer.",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        /*if (resultCode == Activity.RESULT_OK) {

            if (requestCode == SELECT_FILE)

                onSelectFromGalleryResult(data);

            else if (requestCode == REQUEST_CAMERA)

                onCaptureImageResult(data);
        }*/
    }

    public void ShareDialog(Bitmap imagePath){

        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(imagePath)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        shareDialog.show(content);

    }


    // Initialize the facebook sdk and then callback manager will handle the login responses.

    protected void facebookSDKInitialize() {

        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }
}
