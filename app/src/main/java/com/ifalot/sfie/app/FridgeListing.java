package com.ifalot.sfie.app;


import android.app.AlertDialog;
import android.content.Intent;
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

public class FridgeListing extends Fragment {

    private FridgeListAdapter fridgeListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fridge_listing, container, false);

        ListView lv = (ListView) rootView.findViewById(R.id.fridge_list);
        fridgeListAdapter = new FridgeListAdapter(getContext(), (TextView) rootView.findViewById(R.id.enddate_alert));
        lv.setAdapter(fridgeListAdapter);
        Fridge.getInstance().setViewUpdateListener(fridgeListAdapter);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.button_add_supplies);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Fridge.getInstance().getSupplies().size() == 0){
                    new AlertDialog.Builder(FridgeListing.this.getContext())
                        .setTitle("Error")
                        .setMessage("Not meals have been defined yet, so no ingredients are known")
                        .setNeutralButton("Close", null).show();
                }else {
                    startActivityForResult(new Intent(FridgeListing.this.getContext(), Shopping.class), 0);
                }
            }
        });

        fridgeListAdapter.update();
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fridgeListAdapter.update();
    }

    @Override
    public void onPause() {
        Fridge.getInstance().save();
        super.onPause();
    }

}
