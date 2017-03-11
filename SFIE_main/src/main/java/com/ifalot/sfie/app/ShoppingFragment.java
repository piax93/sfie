package com.ifalot.sfie.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.ifalot.sfie.R;
import com.ifalot.sfie.model.Fridge;

import java.util.ArrayList;
import java.util.Comparator;

public class ShoppingFragment extends Fragment {

    final static String FRAG_TAG = "ShoppingFragment";
    private int itemCount = 0;
    private ArrayAdapter<String> spinnerAdapter;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping, container, false);

        final Fridge fridge = Fridge.getInstance();
        final LinearLayout shoppingWrapper = (LinearLayout) view.findViewById(R.id.shopping_wrapper);
        spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item,
                android.R.id.text1, new ArrayList<>(fridge.getSupplies().keySet()));
        spinnerAdapter.sort(new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        });
        Button addItem = (Button) view.findViewById(R.id.button_add_material);
        Button doneShopping = (Button) view.findViewById(R.id.button_save_shopping);

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup v = (ViewGroup) ((ViewGroup) inflater.inflate(R.layout.shopitem_wrapper, shoppingWrapper)).getChildAt(itemCount);
                Spinner s = (Spinner) v.getChildAt(0);
                s.setAdapter(spinnerAdapter);
                itemCount++;
            }
        });

        doneShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity().getCurrentFocus() != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                boolean something = false;
                for (int i = 0; i < itemCount; i++) {
                    LinearLayout ll = (LinearLayout) shoppingWrapper.getChildAt(i);
                    Spinner name = (Spinner) ll.getChildAt(0);
                    EditText quant = (EditText) ll.getChildAt(1);
                    if (quant.length() > 0) {
                        something = true;
                        fridge.addSupply((String) name.getSelectedItem(), Float.parseFloat(quant.getText().toString()));
                    }
                }
                if (something) fridge.updateTheEnd();
                getActivity().onBackPressed();
            }
        });

        addItem.performClick();

        return view;
    }

}
