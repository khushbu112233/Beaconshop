package com.amplearch.beaconshop.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplearch.beaconshop.Model.VoucherClass;
import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.Utils.TrojanText;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ample-arch on 4/7/2017.
 */

public class ElectOfferAdapter extends BaseAdapter
{
    Context context;
    private List<VoucherClass> voucherItems;

    public ElectOfferAdapter(Context applicationContext, List<VoucherClass> voucherItems)
    {
        this.context = applicationContext ;
        this.voucherItems = voucherItems ;
    }

    @Override
    public int getCount() {
        return voucherItems.size();
    }

    @Override
    public Object getItem(int position) {
        return voucherItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView= inflater.inflate(R.layout.electronics_offers, null, true);

        ImageView imageView = (ImageView)rowView.findViewById(R.id.ivElectImage);
        TrojanText trojanText = (TrojanText)rowView.findViewById(R.id.tvElectOfferDetails);

        if(voucherItems.get(position).getStore_image() != null) {
           /* Picasso.with(context).load(voucherItems.get(position).getStore_image())
                    .into(imageView);*/
            byte[] decodedString = Base64.decode(voucherItems.get(position).getStore_image(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            imageView.setImageBitmap(decodedByte);
        }
        trojanText.setText(voucherItems.get(position).getOffer_title().toString());

        return rowView ;
    }
}
