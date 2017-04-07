package com.amplearch.beaconshop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.Utils.TrojanText;
import com.github.siyamed.shapeimageview.RoundedImageView;

import java.util.ArrayList;

/**
 * Created by ample-arch on 4/7/2017.
 */

public class ElectOfferAdapter extends BaseAdapter
{
    Context context;
    ArrayList<Integer> elect_Image ;
    ArrayList<String> elect_Text ;

    public ElectOfferAdapter(Context applicationContext, ArrayList<Integer> elect_image, ArrayList<String> elect_text)
    {
        this.context = applicationContext ;
        this.elect_Image = elect_image ;
        this.elect_Text = elect_text ;
    }

    @Override
    public int getCount() {
        return elect_Image.size();
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
        View rowView= inflater.inflate(R.layout.electronics_offers, null, true);

        ImageView imageView = (ImageView)rowView.findViewById(R.id.ivElectImage);
        TrojanText trojanText = (TrojanText)rowView.findViewById(R.id.tvElectOfferDetails);

        imageView.setImageResource(elect_Image.get(position));
        trojanText.setError(elect_Text.get(position));

        return rowView ;
    }
}
