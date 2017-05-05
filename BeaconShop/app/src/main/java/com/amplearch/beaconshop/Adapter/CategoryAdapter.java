package com.amplearch.beaconshop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.amplearch.beaconshop.Model.PbWithCircle;
import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.task.CircleImageLazyLoading;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 03/30/2017.
 */

public class CategoryAdapter extends BaseAdapter
{
    private static ArrayList name, notice, count;
    private Context mContext;
    private ProgressBar pb;
    private CircleImageView circleImageView ;
    private TextView txtCategory , badgeCategory ;

    public CategoryAdapter(Context context, ArrayList name, ArrayList image, ArrayList count)
    {
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
             circleImageView = (CircleImageView) gridViewAndroid.findViewById(R.id.imgCategory);
             txtCategory = (TextView) gridViewAndroid.findViewById(R.id.txtCategory);
             badgeCategory = (TextView) gridViewAndroid.findViewById(R.id.badgeCategory);

            pb = (ProgressBar)gridViewAndroid.findViewById(R.id.progressBar1);

//            txtCategory.setText(name.get(i).toString());
            txtCategory.setText(name.get(i).toString());
            badgeCategory.setText(count.get(i).toString());

            if (count.get(i).toString().equals("0")){
                badgeCategory.setVisibility(View.INVISIBLE);
            }
            else {
                badgeCategory.setVisibility(View.VISIBLE);
            }


           /* if (notice.get(i).toString()!=null)
            {
           // imageViewAndroid.setImageResource(gridViewImageId[i]);
                Picasso.with(mContext).load(notice.get(i).toString()).into(imageViewAndroid);
            }*/

            circleImageView.setTag(notice.get(i).toString());

            PbWithCircle pwc = new PbWithCircle();
            pwc.setCircleImageView(circleImageView);
            pwc.setProgressBar(pb);
            new CircleImageLazyLoading().execute(pwc);

        }
        else {
            gridViewAndroid = (View) convertView;
        }

        return gridViewAndroid;
    }
}
