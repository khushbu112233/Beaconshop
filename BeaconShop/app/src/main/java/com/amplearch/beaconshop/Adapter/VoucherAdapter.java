
package com.amplearch.beaconshop.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amplearch.beaconshop.Model.UserRedeem;
import com.amplearch.beaconshop.R;
import com.github.siyamed.shapeimageview.CircularImageView;

import java.util.List;

/**
 * Created by ample-arch on 3/31/2017.
 */

public class VoucherAdapter extends BaseAdapter
{
    Context context;
    private List<UserRedeem> voucherItems;
    private CircularImageView roundedImage ;
    private TextView tvVouchText;
    private TextView tvVouchText1;

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
        roundedImage = (CircularImageView)rView.findViewById(R.id.imgVoucher);
        tvVouchText = (TextView)rView.findViewById(R.id.tvVouchText);
        tvVouchText1 = (TextView)rView.findViewById(R.id.tvVouchText1);

//        ivVouchImage.setImageResource(vouchImage.get(position));
      //  roundedImage.setImageResource(vouchImage.get(position));


        if(voucherItems.get(position).getOffer_image() != null)
        {
//            Picasso.with(context).load(voucherItems.get(position).getOffer_image()).into(roundedImage);

            byte[] decodedString = new byte[0];
            try {
                decodedString = Base64.decode(voucherItems.get(position).getOffer_image(), Base64.DEFAULT);
            }catch (Exception e){}
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            roundedImage.setImageBitmap(decodedByte);
        }
        tvVouchText.setText(voucherItems.get(position).getOffer_title());
        tvVouchText1.setText(voucherItems.get(position).getOffer_desc());
//        roundedImage.setTag(voucherItems.get(position).getOffer_image());
       /* tvVouchText.setText(voucherItems.get(position).getOffer_title());

        for(int i = 0; i < voucherItems.get(position).getOffer_image().length(); i ++ )
        {
            roundedImage.setTag(voucherItems.get(position).getOffer_image());
        }

        PbWithRoundImage pwri = new PbWithRoundImage();
        pwri.setRoundedImageView(roundedImage);
        pwri.setProgressBar(progressBar);
        new RoundImageLazyLoading().execute(pwri);*/

        return rView;
    }
}
