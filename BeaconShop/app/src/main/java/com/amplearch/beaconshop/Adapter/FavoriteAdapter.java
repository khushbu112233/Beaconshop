package com.amplearch.beaconshop.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.Utils.GillSansTextView;
import com.github.siyamed.shapeimageview.CircularImageView;

import java.util.ArrayList;

/**
 * Created by ample-arch on 3/31/2017.
 */

public class FavoriteAdapter extends BaseAdapter
{
    Context context;
    ArrayList<Bitmap> favImage;
    ArrayList<String> favText;
    ArrayList<String> favText1;

    public FavoriteAdapter(FragmentActivity activity, ArrayList<Bitmap> favImage, ArrayList<String> favText,ArrayList<String> favText1)
    {
        this.context = activity ;
        this.favImage = favImage ;
        this.favText = favText ;
        this.favText1 = favText1;
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

        CircularImageView roundedImageView = (CircularImageView) rowView.findViewById(R.id.imgOffer);
        GillSansTextView tvFavText = (GillSansTextView)rowView.findViewById(R.id.tvFavText);
        GillSansTextView tvFavText1 = (GillSansTextView)rowView.findViewById(R.id.tvFavText1);

        roundedImageView.setImageBitmap(favImage.get(position));
        tvFavText.setText(favText.get(position));
        tvFavText1.setText(favText1.get(position));

        return rowView ;
    }
}
