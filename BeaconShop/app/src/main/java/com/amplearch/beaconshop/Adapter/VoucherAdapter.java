package com.amplearch.beaconshop.Adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplearch.beaconshop.Model.UserRedeem;
import com.amplearch.beaconshop.Model.VoucherClass;
import com.amplearch.beaconshop.R;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ample-arch on 3/31/2017.
 */

public class VoucherAdapter extends BaseAdapter
{
    Context context;
    private List<UserRedeem> voucherItems;

    public VoucherAdapter(FragmentActivity activity, List<UserRedeem> voucherItems)
    {
        this.context = activity;
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
        View rView = inflater.inflate(R.layout.grid_voucher,null,true);

//        ImageView ivVouchImage = (ImageView)rView.findViewById(R.id.ivVouchImage);
       RoundedImageView roundedImage = (RoundedImageView)rView.findViewById(R.id.imgVoucher);
       TextView tvVouchText = (TextView)rView.findViewById(R.id.tvVouchText);

//        ivVouchImage.setImageResource(vouchImage.get(position));
      //  roundedImage.setImageResource(vouchImage.get(position));
        tvVouchText.setText(voucherItems.get(position).getOffer_title());

        if(voucherItems.get(position).getOffer_image() != null) {
            Picasso.with(context).load(voucherItems.get(position).getOffer_image())
                    .into(roundedImage);
        }

        return rView;
    }
}
