package com.amplearch.beaconshop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.Utils.TrojanText;

import java.util.ArrayList;

/**
 * Created by ample-arch on 4/12/2017.
 */

public class DrawerAdapter extends BaseAdapter
{
    ArrayList<Integer> drawerImage ;
    ArrayList<String> drawerText ;
    Context context ;

    public DrawerAdapter(Context applicationContext, ArrayList<Integer> drawerImage, ArrayList<String> drawerText) {
        this.context = applicationContext ;
        this.drawerImage = drawerImage;
        this.drawerText = drawerText ;
    }

    @Override
    public int getCount() {
        return drawerImage.size();
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
        View rowView= inflater.inflate(R.layout.listview_with_text_image, null, true);

        ImageView imageView = (ImageView)rowView.findViewById(R.id.imageView);
        TrojanText trojanText = (TrojanText)rowView.findViewById(R.id.textView);

        imageView.setImageResource(drawerImage.get(position));
        trojanText.setText(drawerText.get(position));

        return rowView;
    }
}
