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
import android.widget.ProgressBar;

import com.amplearch.beaconshop.Model.ImageVoucherPb;
import com.amplearch.beaconshop.Model.VoucherClass;
import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.Utils.TrojanText;
import com.amplearch.beaconshop.task.DownloadImageTask2;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ample-arch on 4/7/2017.
 */

public class ElectOfferAdapter extends BaseAdapter
{
    Context context;
    private List<VoucherClass> voucherItems;
    private ProgressBar pb ;
    private ImageView imageView ;
    private TrojanText trojanText, trojanTextdetail ;

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

         imageView = (ImageView)rowView.findViewById(R.id.ivElectImage);
         trojanText = (TrojanText)rowView.findViewById(R.id.tvElectOfferDetails);
         trojanTextdetail = (TrojanText)rowView.findViewById(R.id.tvElectOfferDetails1);
//         pb = (ProgressBar)rowView.findViewById(R.id.progressBar2);

        if(voucherItems.get(position).getStore_image() != null)
        {
//            Picasso.with(context).load(voucherItems.get(position).getStore_image()).into(imageView);
            byte[] decodedString = Base64.decode(voucherItems.get(position).getStore_image(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            imageView.setImageBitmap(decodedByte);
        }

        trojanText.setText(voucherItems.get(position).getMessage().toString());
        trojanTextdetail.setText(voucherItems.get(position).getOffer_title().toString());

       /* imageView.setTag(voucherItems.get(position).getStore_image().toString());

        ImageVoucherPb ivp = new ImageVoucherPb();
        ivp.setImage(imageView);
        ivp.setProgressBar(pb);
        new DownloadImageTask2().execute(ivp);*/

        return rowView ;
    }
}
