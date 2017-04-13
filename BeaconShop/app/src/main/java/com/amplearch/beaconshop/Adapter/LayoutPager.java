package com.amplearch.beaconshop.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.amplearch.beaconshop.Fragment.HomeFragment;
import com.amplearch.beaconshop.Fragment.Nearbyfragment;

/**
 * Created by ample-arch on 4/6/2017.
 */

public class LayoutPager extends FragmentPagerAdapter
{
    //integer to count number of tabs
    int tabCount;

    public LayoutPager(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    public LayoutPager(FragmentManager supportFragmentManager)
    {
        super(supportFragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                HomeFragment tab1 = new HomeFragment();
                return tab1;
            case 1:
                Nearbyfragment tab2 = new Nearbyfragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
