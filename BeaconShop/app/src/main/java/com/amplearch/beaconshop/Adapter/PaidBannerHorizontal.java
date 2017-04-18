package com.amplearch.beaconshop.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.amplearch.beaconshop.Model.Voucher;
import com.amplearch.beaconshop.Model.VoucherClass;
import com.amplearch.beaconshop.R;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by admin on 04/17/2017.
 */

public class PaidBannerHorizontal extends RecyclerView.Adapter<PaidBannerHorizontal.MyViewHolder>  {

    private Activity activity;
    private List<VoucherClass> movieItems;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public RoundedImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            imageView = (RoundedImageView) view.findViewById(R.id.custom_sticker_image);
            view.setClickable(true);

        }
    }
    public PaidBannerHorizontal(Activity activity, List<VoucherClass> movieItems) {
        this.activity = activity;
        this.movieItems = movieItems;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_paidbanner, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        //holder.imageView.setBackgroundResource(imageId[position]);

        if(movieItems.get(position).getStore_image() != null) {
            Picasso.with(activity).load(movieItems.get(position).getStore_image())
                    .into(holder.imageView);
        }

        holder.imageView.setSelected(movieItems.contains(position));
        //holder.getp.setSelected(mSelectedRows.contains(i));
       /* holder.txtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,holder.txtView.getText().toString(),Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return movieItems.size();
    }
}
