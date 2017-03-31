package com.amplearch.beaconshop.Adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplearch.beaconshop.R;
import com.github.siyamed.shapeimageview.RoundedImageView;

import java.util.ArrayList;

/**
 * Created by ample-arch on 3/31/2017.
 */

public class VoucherAdapter extends BaseAdapter
{
    Context context;
    ArrayList<Integer> vouchImage;
    ArrayList<String> vouchText ;

    public VoucherAdapter(FragmentActivity activity, ArrayList<Integer> vouchImage, ArrayList<String> vouchText)
    {
        this.context = activity;
        this.vouchImage = vouchImage ;
        this.vouchText = vouchText;
    }

    @Override
    public int getCount() {
        return vouchImage.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
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
        roundedImage.setImageResource(vouchImage.get(position));
        tvVouchText.setText(vouchText.get(position));

        return rView;
    }
}
