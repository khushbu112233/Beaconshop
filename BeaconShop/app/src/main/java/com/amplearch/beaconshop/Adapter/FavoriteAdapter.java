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
 * Created by ample-arch on 3/31/2017.
 */

public class FavoriteAdapter extends BaseAdapter
{
    Context context;
    ArrayList<Integer> favImage;
    ArrayList<String> favText;

    public FavoriteAdapter(FragmentActivity activity, ArrayList<Integer> favImage, ArrayList<String> favText)
    {
        this.context = activity ;
        this.favImage = favImage ;
        this.favText = favText ;
    }

    @Override
    public int getCount() {
        return favImage.size();
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
        View rowView= inflater.inflate(R.layout.grid_favorite, null, true);

        RoundedImageView roundedImageView = (RoundedImageView)rowView.findViewById(R.id.imgOffer);
        TextView tvFavText = (TextView)rowView.findViewById(R.id.tvFavText);

        roundedImageView.setImageResource(favImage.get(position));
        tvFavText.setText(favText.get(position));

        return rowView ;
    }
}
