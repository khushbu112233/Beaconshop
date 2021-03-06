package com.amplearch.beaconshop.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amplearch.beaconshop.Model.PbWithImage;
import com.amplearch.beaconshop.Model.VoucherClass;
import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.task.ImageViewLazyLoading;

import java.util.List;

/**
 * Created by admin on 04/17/2017.
 */

public class PaidBannerHorizontal extends RecyclerView.Adapter<PaidBannerHorizontal.MyViewHolder>
{
    private Activity activity;
    private List<VoucherClass> movieItems;
    private ImageView imageView ;
    private TextView bannerTitle, bannerDesc ;
    private ProgressBar pb;


    public class MyViewHolder extends RecyclerView.ViewHolder
    {
//        public RoundedImageView imageView;

        public MyViewHolder(View view)
        {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.custom_sticker_image);
            bannerTitle = (TextView)view.findViewById(R.id.tvCustomTitle);
            pb = (ProgressBar)view.findViewById(R.id.progressBar3);
//            bannerDesc = (TextView)view.findViewById(R.id.tvCustomDesc);

            view.setClickable(true);
        }
    }
    public PaidBannerHorizontal(Activity activity, List<VoucherClass> movieItems)
    {
        this.activity = activity;
        this.movieItems = movieItems;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_paidbanner, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        //holder.imageView.setBackgroundResource(imageId[position]);

       /* if(movieItems.get(position).getStore_image() != null)
        {
            //Picasso.with(activity).load(movieItems.get(position).getStore_image()).into(holder.imageView);
            byte[] decodedString = Base64.decode(movieItems.get(position).getStore_image(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

//            holder.imageView.setImageBitmap(decodedByte);

//            holder.imageView.setTag(movieItems.get(position).getStore_image().toString());
        }*/
        //        holder.imageView.setSelected(movieItems.contains(position));


        imageView.setTag(movieItems.get(position).getStore_image().toString());
        bannerTitle.setText(movieItems.get(position).getOffer_title());

//        holder.bannerDesc.setText(movieItems.get(position).getOffer_desc());

        //holder.getp.setSelected(mSelectedRows.contains(i));
       /* holder.txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,holder.txtView.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });*/

        PbWithImage pwi = new PbWithImage();
        pwi.setImageView(imageView);
        pwi.setProgressBar(pb);
        new ImageViewLazyLoading().execute(pwi);

    }

    @Override
    public int getItemCount() {
        return movieItems.size();
    }
}
