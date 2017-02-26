package com.ifalot.sfie.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.ifalot.sfie.R;
import com.ifalot.sfie.model.Fridge;

import java.util.ArrayList;
import java.util.Comparator;

public class Shopping extends AppCompatActivity {

    private int itemCount = 0;
    private ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        setTitle("Shopping");

        final Fridge fridge = Fridge.getInstance();
        final LinearLayout shoppingWrapper = (LinearLayout) findViewById(R.id.shopping_wrapper);
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, android.R.id.text1,
                new ArrayList<>(fridge.getSupplies().keySet()));
        spinnerAdapter.sort(new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        });
        Button addItem = (Button) findViewById(R.id.button_add_material);
        Button doneShopping = (Button) findViewById(R.id.button_save_shopping);

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup v = (ViewGroup) ((ViewGroup) getLayoutInflater().inflate(R.layout.shopitem_wrapper, shoppingWrapper)).getChildAt(itemCount);
                Spinner s = (Spinner) v.getChildAt(0);
                s.setAdapter(spinnerAdapter);
                itemCount++;
            }
        });

        doneShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean something = false;
                for(int i = 0; i < itemCount; i++){
                    LinearLayout ll = (LinearLayout) shoppingWrapper.getChildAt(i);
                    Spinner name = (Spinner) ll.getChildAt(0);
                    EditText quant = (EditText) ll.getChildAt(1);
                    if(quant.length() > 0) {
                        something = true;
                        fridge.addSupply((String) name.getSelectedItem(), Float.parseFloat(quant.getText().toString()));
                    }
                }
                if(something) fridge.updateTheEnd();
                Shopping.this.finish();
            }
        });

        addItem.performClick();
    }

}
