package com.ifalot.sfie.app;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.ifalot.sfie.R;
import com.ifalot.sfie.model.Fridge;
import com.ifalot.sfie.util.FridgeListAdapter;

public class FridgeListingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fridge_listing, container, false);

        ListView lv = (ListView) rootView.findViewById(R.id.fridge_list);
        FridgeListAdapter fridgeListAdapter = new FridgeListAdapter(getContext(), (TextView) rootView.findViewById(R.id.enddate_alert));
        lv.setAdapter(fridgeListAdapter);
        Fridge.getInstance().setViewUpdateListener(fridgeListAdapter);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.button_add_supplies);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Fridge.getInstance().getSupplies().size() == 0) {
                    new AlertDialog.Builder(FridgeListingFragment.this.getContext())
                            .setTitle("Error")
                            .setMessage("No meals have been defined yet, so no ingredients are known")
                            .setNeutralButton("Close", null).show();
                } else {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.slide_out_right,
                                    android.R.anim.fade_in, android.R.anim.slide_out_right)
                            .add(R.id.main_view, new ShoppingFragment())
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        fridgeListAdapter.update();
        return rootView;
    }

    @Override
    public void onPause() {
        Fridge.getInstance().save();
        super.onPause();
    }

}
