package com.amplearch.beaconshop.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.amplearch.beaconshop.Adapter.CategoryAdapter;
import com.amplearch.beaconshop.R;
import com.amplearch.beaconshop.Activity.StoreListActivity;

/**
 * Created by anupamchugh on 10/12/15.
 */
public class HomeFragment extends Fragment {

    GridView listCategory;
    String[] web;
    int[] imageId = {
            R.drawable.ic_sale,
            R.drawable.ic_sale,
            R.drawable.ic_sale,
            R.drawable.ic_sale,
            R.drawable.ic_sale,
            R.drawable.ic_sale,
            R.drawable.ic_sale

    };

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);


        web = new String[imageId.length];
        listCategory =(GridView) rootView.findViewById(R.id.category_list);
        CategoryAdapter adapterViewAndroid = new CategoryAdapter(getContext(), web, imageId);
        listCategory.setAdapter(adapterViewAndroid);
        listCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int i, long id) {
              //  Toast.makeText(getContext(), "GridView Item: " , Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), StoreListActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

}
