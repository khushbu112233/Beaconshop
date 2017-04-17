package com.amplearch.beaconshop.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amplearch.beaconshop.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 03/30/2017.
 */

public class CategoryAdapter extends BaseAdapter {
    private static ArrayList name, notice, count;
    private Context mContext;
 //   private final String[] gridViewString;
   // private final int[] gridViewImageId;

    public CategoryAdapter(Context context, ArrayList name, ArrayList image, ArrayList count) {
        mContext = context;
        notice = image;
        this.name = name;
        this.count = count;
    }

    @Override
    public int getCount() {
        return name.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View gridViewAndroid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
        {
            gridViewAndroid = new View(mContext);
            gridViewAndroid = inflater.inflate(R.layout.category_row, null);
           // TextView textViewAndroid = (TextView) gridViewAndroid.findViewById(R.id.android_gridview_text);
            CircleImageView imageViewAndroid = (CircleImageView) gridViewAndroid.findViewById(R.id.imgCategory);
            TextView txtCategory = (TextView) gridViewAndroid.findViewById(R.id.txtCategory);
            TextView badgeCategory = (TextView) gridViewAndroid.findViewById(R.id.badgeCategory);

//            txtCategory.setText(name.get(i).toString());
            txtCategory.setText(name.get(i).toString());

            badgeCategory.setText(count.get(i).toString());
            if (count.get(i).toString().equals("0")){
                badgeCategory.setVisibility(View.INVISIBLE);
            }
            else {
                badgeCategory.setVisibility(View.VISIBLE);
            }


            if (notice.get(i).toString()!=null)
            {
           // imageViewAndroid.setImageResource(gridViewImageId[i]);
                Picasso.with(mContext).load(notice.get(i).toString()).into(imageViewAndroid);
            }
        }
        else {
            gridViewAndroid = (View) convertView;
        }

        return gridViewAndroid;
    }
}
