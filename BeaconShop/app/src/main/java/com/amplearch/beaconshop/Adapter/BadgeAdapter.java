package com.amplearch.beaconshop.Adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.Utils.TrojanText;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ample-arch on 3/30/2017.
 */

public class BadgeAdapter extends BaseAdapter
{
    Context context;
    ArrayList<Integer> Bimages;
    ArrayList<String> Btexts;

    public BadgeAdapter(FragmentActivity activity, ArrayList<Integer> badgeImages, ArrayList<String> badgeText)
    {
        this.context = activity;
        this.Bimages = badgeImages;
        this.Btexts = badgeText ;
    }

    @Override
    public int getCount() {
        return Bimages.size();
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
        View rowView= inflater.inflate(R.layout.grid_badges, null, true);

        ImageView circleBadges = (ImageView) rowView.findViewById(R.id.circleBadges);
        TrojanText tvAwardname = (TrojanText)rowView.findViewById(R.id.tvAwardName);

        circleBadges.setImageResource(Bimages.get(position));
        tvAwardname.setText(Btexts.get(position));

        return rowView;
    }
}
