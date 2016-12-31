package com.ifalot.sfie.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.ifalot.sfie.R;

import java.util.ArrayList;

public class Shopping extends AppCompatActivity {

    private int itemCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                new ArrayList<>(FridgeListing.fridge.getSupplies().keySet()));
        final LinearLayout shoppingWrapper = (LinearLayout) findViewById(R.id.shopping_wrapper);
        Button addItem = (Button) findViewById(R.id.button_add_material);
        Button doneShopping = (Button) findViewById(R.id.button_save_shopping);

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemCount++;
                ViewGroup v = (ViewGroup) ((ViewGroup) getLayoutInflater().inflate(R.layout.shopitem_wrapper, shoppingWrapper)).getChildAt(0);
                Spinner s = (Spinner) v.getChildAt(0);
                s.setAdapter(spinnerAdapter);
            }
        });

        doneShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < itemCount; i++){
                    LinearLayout ll = (LinearLayout) shoppingWrapper.getChildAt(i);
                    Spinner name = (Spinner) ll.getChildAt(0);
                    EditText quant = (EditText) ll.getChildAt(1);
                    FridgeListing.fridge.addSupply((String) name.getSelectedItem(),
                            Float.parseFloat(quant.getText().toString()));
                }
                Shopping.this.finish();
            }
        });

        addItem.performClick();
    }

}
