package com.orbital.lead.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;

/**
 * Created by joseph on 17/6/2015.
 */
public class CustomFragmentStatePagerAdapter extends CacheFragmentStatePagerAdapter{
    //private static final String[] TITLES = new String[]{"Journal", "Experience", "Cupcake", "Donut", "Eclair", "Froyo", "Gingerbread", "Honeycomb", "Ice Cream Sandwich", "Jelly Bean", "KitKat", "Lollipop"};
    private static final String[] TITLES = new String[]{"Journal", "Test"};

    private int mScrollY;

    public CustomFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setScrollY(int scrollY) {
        mScrollY = scrollY;
    }

    @Override
    protected Fragment createItem(int position) {
        // Initialize fragments.
        // Please be sure to pass scroll position to each fragments using setArguments.
        Fragment f = null;

        switch (position){
            case 0:
                f = FragmentMainUserJournalList.newInstance("","");
                break;
            case 1:
                f = FragmentDetail.newInstance("","");
                break;
        }


        return f;

        /*
        final int pattern = position % 4;
        switch (pattern) {
            case 0:

                if (0 <= mScrollY) {
                    Bundle args = new Bundle();
                    args.putInt(ViewPagerTabScrollViewFragment.ARG_SCROLL_Y, mScrollY);
                    f.setArguments(args);
                }


                break;

            case 1:

                f = new ViewPagerTabListViewFragment();
                if (0 < mScrollY) {
                    Bundle args = new Bundle();
                    args.putInt(ViewPagerTabListViewFragment.ARG_INITIAL_POSITION, 1);
                    f.setArguments(args);
                }

                break;
        }

        return f;
        */
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

}
