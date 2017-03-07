package com.ifalot.sfie.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ifalot.sfie.R;
import com.ifalot.sfie.util.TabPageAdapter;

public class TabsFragment extends Fragment {

    private final String[] tabs = {"MEAL LIST", "FRIDGE"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tabs, container);
        TabPageAdapter pageAdapter = new TabPageAdapter(getActivity().getSupportFragmentManager(), tabs);
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.main_pager);
        viewPager.setAdapter(pageAdapter);

        final TabLayout tabLayout = (TabLayout) view.findViewById(R.id.pager_title_strip);
        tabLayout.addTab(tabLayout.newTab().setText(tabs[0]));
        tabLayout.addTab(tabLayout.newTab().setText(tabs[1]));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

}
