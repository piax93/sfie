package com.ifalot.sfie.util;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.ifalot.sfie.R;
import com.ifalot.sfie.model.Fridge;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FridgeListAdapter extends ArrayAdapter<String> {

    private TextView theendtv;

    public FridgeListAdapter(Context context, TextView theendtv) {
        super(context, android.R.layout.simple_list_item_1);
        this.theendtv = theendtv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        if (convertView == null) view = inflater.inflate(R.layout.fridge_list_item, parent, false);
        else view = convertView;

        String ing = this.getItem(position);
        Float quant = Fridge.getInstance().getQuantity(ing);
        ((TextView) view.findViewById(R.id.ingr_tv)).setText(ing);
        ((TextView) view.findViewById(R.id.quant_tv)).setText(quant.toString());
        if (quant < 0) view.setBackgroundColor(Color.RED);
        else view.setBackgroundColor(Color.WHITE);

        return view;
    }

    public void update() {
        super.clear();
        super.addAll(new ArrayList<>(Fridge.getInstance().getSupplies().keySet()));
        Date theend = Fridge.getInstance().getTheEnd();
        if (theend != null) {
            theendtv.setBackgroundColor(Color.YELLOW);
            theendtv.setText("You must go shopping before " + DateFormat.getDateInstance().format(theend));
        } else {
            theendtv.setBackgroundColor(Color.WHITE);
            theendtv.setText("You will be ok for a while");
        }
    }

}
