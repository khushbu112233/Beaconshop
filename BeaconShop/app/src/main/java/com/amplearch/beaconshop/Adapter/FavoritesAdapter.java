package com.amplearch.beaconshop.Adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amplearch.beaconshop.R;
import com.github.siyamed.shapeimageview.RoundedImageView;

import java.util.ArrayList;

/**
 * Created by ample-arch on 4/14/2017.
 */

public class FavoritesAdapter extends BaseAdapter
{
    Context context;
    ArrayList<String> storeName;
    ArrayList<String> offerTitle;
    ArrayList<String> offerDesc;
    ArrayList<String> startDate;
    ArrayList<String> endDate;

    public FavoritesAdapter(FragmentActivity activity, ArrayList<String> storeName, ArrayList<String> offerTitle, ArrayList<String> offerDesc,
                            ArrayList<String> startDate, ArrayList<String> endDate)
    {
        this.context = activity;
        this.storeName = storeName;
        this.offerTitle = offerTitle;
        this.offerDesc = offerDesc;
        this.startDate = startDate;
        this.endDate = endDate;
    }


    @Override
    public int getCount() {
        return storeName.size();
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
        View rowView= inflater.inflate(R.layout.grid_favorites, null, true);

        TextView StoreName = (TextView)rowView.findViewById(R.id.tvStoreName);
        TextView OfferTitle = (TextView)rowView.findViewById(R.id.tvOfferTitle);
        TextView OfferDesc = (TextView)rowView.findViewById(R.id.tvOfferDescription);
        TextView StartDate = (TextView)rowView.findViewById(R.id.tvStartDate);
        TextView EndDate = (TextView)rowView.findViewById(R.id.tvEndDate);

        StoreName.setText(storeName.get(position));
        OfferTitle.setText(offerTitle.get(position));
        OfferDesc.setText(offerDesc.get(position));
        StartDate.setText(startDate.get(position));
        EndDate.setText(endDate.get(position));

        return rowView ;
    }
}
