package com.amplearch.beaconshop.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amplearch.beaconshop.Model.InfoWindowData;
import com.amplearch.beaconshop.R;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomInfoWindowGoogleMap(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.map_custom_infowindow, null);
        view.setLayoutParams(new RelativeLayout.LayoutParams(700, RelativeLayout.LayoutParams.WRAP_CONTENT));

        TextView name_tv = (TextView) view.findViewById(R.id.tvElectOfferDetails);
        TextView details_tv = (TextView)view.findViewById(R.id.tvElectOfferDetails1);
        TextView hotel_tv = (TextView)view.findViewById(R.id.tvElectOfferDetails2);
        CircularImageView img = (CircularImageView) view.findViewById(R.id.ivElectImage);

        // TextView hotel_tv = (TextView)view.findViewById(R.id.hotels);
        // TextView food_tv = (TextView)view.findViewById(R.id.food);
        // TextView transport_tv = (TextView)view.findViewById(R.id.transport);


        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();
        byte[] decodedString = Base64.decode(infoWindowData.getImage(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        img.setImageBitmap(decodedByte);
        name_tv.setText(infoWindowData.getTitle());
        details_tv.setText(infoWindowData.getDesc());
        hotel_tv.setText(infoWindowData.getStorename());
        //food_tv.setText(infoWindowData.getFood());
        //transport_tv.setText(infoWindowData.getTransport());

        return view;
    }
}
