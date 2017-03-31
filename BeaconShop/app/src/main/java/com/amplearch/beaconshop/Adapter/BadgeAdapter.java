package com.amplearch.beaconshop.Adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.amplearch.beaconshop.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ample-arch on 3/30/2017.
 */

public class BadgeAdapter extends BaseAdapter
{
    Context context;
    ArrayList<Integer> Bimages;

    public BadgeAdapter(FragmentActivity activity, ArrayList<Integer> badgeImages)
    {
        this.context = activity;
        this.Bimages = badgeImages;
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

        CircleImageView circleBadges = (CircleImageView)rowView.findViewById(R.id.circleBadges);
        circleBadges.setImageResource(Bimages.get(position));

        return rowView;
    }
}
