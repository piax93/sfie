package com.ifalot.sfie.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.ifalot.sfie.R;
import com.ifalot.sfie.model.Fridge;

import java.util.ArrayList;

public class FridgeListAdapter extends ArrayAdapter<String> {

    private Fridge fridge;

    public FridgeListAdapter(Context context, Fridge fridge) {
        super(context, android.R.layout.simple_list_item_1, new ArrayList<>(fridge.getSupplies().keySet()));
        this.fridge = fridge;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        if(convertView == null) view = inflater.inflate(R.layout.fridge_list_item, parent, false);
        else view = convertView;

        String ing = this.getItem(position);
        ((TextView)view.findViewById(R.id.ingr_tv)).setText(ing);
        ((TextView)view.findViewById(R.id.quant_tv)).setText(fridge.getQuantity(ing).toString());

        return view;
    }

    public void update(){
        super.clear();
        super.addAll(new ArrayList<>(fridge.getSupplies().keySet()));
    }

}
