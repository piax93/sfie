package com.ifalot.sfie.app;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.ifalot.sfie.R;
import com.ifalot.sfie.model.Fridge;
import com.ifalot.sfie.util.FridgeListAdapter;

public class FridgeListing extends Fragment {

    private Fridge fridge;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fridge_listing, container, false);
        fridge = Fridge.load();

        ListView lv = (ListView) rootView.findViewById(R.id.fridge_list);
        lv.setAdapter(new FridgeListAdapter(getContext(), fridge));

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.button_add_supplies);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return rootView;
    }

}
