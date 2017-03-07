package com.ifalot.sfie.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.ifalot.sfie.app.FridgeListingFragment;
import com.ifalot.sfie.app.MealListFragment;

public class TabPageAdapter extends FragmentPagerAdapter {

    private String[] tabs;

    public TabPageAdapter(FragmentManager fm, String[] tabs) {
        super(fm);
        this.tabs = tabs;
    }

    @Override
    public Fragment getItem(int index) {
        Fragment f;
        if (index == 0) f = new MealListFragment();
        else f = new FridgeListingFragment();
        return f;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

}
