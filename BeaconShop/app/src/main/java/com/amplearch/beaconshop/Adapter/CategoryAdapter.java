package com.amplearch.beaconshop.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import com.amplearch.beaconshop.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 03/30/2017.
 */

public class CategoryAdapter extends BaseAdapter {

    private Context mContext;
    private final String[] gridViewString;
    private final int[] gridViewImageId;

    public CategoryAdapter(Context context, String[] gridViewString, int[] gridViewImageId) {
        mContext = context;
        this.gridViewImageId = gridViewImageId;
        this.gridViewString = gridViewString;
    }

    @Override
    public int getCount() {
        return gridViewString.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View gridViewAndroid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            gridViewAndroid = new View(mContext);
            gridViewAndroid = inflater.inflate(R.layout.category_row, null);
           // TextView textViewAndroid = (TextView) gridViewAndroid.findViewById(R.id.android_gridview_text);
            CircleImageView imageViewAndroid = (CircleImageView) gridViewAndroid.findViewById(R.id.imgCategory);
         //   textViewAndroid.setText(gridViewString[i]);
            imageViewAndroid.setImageResource(gridViewImageId[i]);
        } else {
            gridViewAndroid = (View) convertView;
        }

        return gridViewAndroid;
    }
}
