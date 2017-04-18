package com.amplearch.beaconshop.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amplearch.beaconshop.ConnectivityReceiver;
import com.amplearch.beaconshop.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

public class OfferDetailActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imgFavourite, imgShare, imgFBShare, imgOffer;
    TextView tvClaimOffer;

    CallbackManager callbackManager;
    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        facebookSDKInitialize();
        setContentView(R.layout.activity_offer_detail);
        checkConnection();
        shareDialog = new ShareDialog(this);
        imgFavourite = (ImageView) findViewById(R.id.imgFavourites);
        imgShare = (ImageView) findViewById(R.id.imgShare);
        imgOffer = (ImageView) findViewById(R.id.imgOffer);

        tvClaimOffer = (TextView) findViewById(R.id.imgClaimOffer);
        imgFBShare = (ImageView) findViewById(R.id.imgFBShare);

        imgFavourite.setOnClickListener(this);
        imgShare.setOnClickListener(this);
        imgFBShare.setOnClickListener(this);
        tvClaimOffer.setOnClickListener(this);

        imgOffer.buildDrawingCache();
        Bitmap bmap = imgOffer.getDrawingCache();
      //  ShareDialog(bmap);
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
        if (v == imgFBShare){
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
