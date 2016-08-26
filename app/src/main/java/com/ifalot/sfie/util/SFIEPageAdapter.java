package com.ifalot.sfie.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.ifalot.sfie.app.FridgeListing;
import com.ifalot.sfie.app.MealList;

public class SFIEPageAdapter extends FragmentPagerAdapter {

    private String[] tabs;

    public SFIEPageAdapter(FragmentManager fm, String[] tabs) {
        super(fm);
        this.tabs = tabs;
    }

    @Override
    public Fragment getItem(int index) {
        Fragment f;
        if(index == 0) f = new MealList();
        else f = new FridgeListing();
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
