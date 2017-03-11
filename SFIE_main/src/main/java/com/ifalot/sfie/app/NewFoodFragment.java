package com.ifalot.sfie.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.ifalot.sfie.R;
import com.ifalot.sfie.model.Food;
import com.ifalot.sfie.util.FragmentResultListener;
import com.ifalot.sfie.util.Generic;

import java.util.ArrayList;

public class NewFoodFragment extends Fragment implements InputFilter {

    final static String FRAG_TAG = "NewFoodFragment";
    private int ingCount = 0;
    private int nextFoodId;
    private boolean isNewFood = false;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_new_food, container, false);

        Button addIngredient = (Button) view.findViewById(R.id.button_add_ingredient);
        Button saveFood = (Button) view.findViewById(R.id.button_save_food);
        final Spinner preloadedFoods = (Spinner) view.findViewById(R.id.preloaded_foods);
        final TextView ingrDisplay = (TextView) view.findViewById(R.id.ingredients_list);
        final RelativeLayout newFoodWrap = (RelativeLayout) view.findViewById(R.id.new_food_wrap);
        final LinearLayout ingWrapper = (LinearLayout) view.findViewById(R.id.ingredients_wrapper);
        final ArrayList<Food> foods = Food.getFoodsInDB();
        nextFoodId = foods.size();
        foods.add(new Food(-1, "New Food"));

        preloadedFoods.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, foods));

        preloadedFoods.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != foods.size() - 1) {
                    isNewFood = false;
                    ingrDisplay.setText(foods.get(i).getIngredientsInNiceListing());
                    newFoodWrap.setVisibility(View.GONE);
                    ingrDisplay.setVisibility(View.VISIBLE);
                } else {
                    isNewFood = true;
                    ingrDisplay.setVisibility(View.GONE);
                    newFoodWrap.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup v = (ViewGroup) ((ViewGroup) inflater.inflate(R.layout.ingredient_wrapper, ingWrapper)).getChildAt(ingCount);
                EditText et = (EditText) v.getChildAt(0);
                et.setFilters(new InputFilter[]{NewFoodFragment.this});
                ingCount++;
            }
        });

        saveFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Food food;
                if (isNewFood) {
                    EditText foodName = (EditText) view.findViewById(R.id.food_name);
                    if (foodName.length() == 0) {
                        Generic.fastErrorDialog(NewFoodFragment.this.getContext(), "Food name can't be empty");
                        return;
                    }
                    int filledIngr = 0;
                    food = new Food(nextFoodId, foodName.getText().toString());
                    for (int i = 0; i < ingCount; i++) {
                        LinearLayout ll = (LinearLayout) ingWrapper.getChildAt(i);
                        EditText ingName = (EditText) ll.getChildAt(0);
                        EditText ingQty = (EditText) ll.getChildAt(1);
                        if (ingName.length() > 0 && ingQty.length() > 0) {
                            filledIngr++;
                            food.addIngredient(ingName.getText().toString(), Float.parseFloat(ingQty.getText().toString()));
                        }
                    }
                    if (filledIngr <= 0) {
                        Generic.fastErrorDialog(NewFoodFragment.this.getContext(), "Food must contain at least one ingredient");
                        return;
                    }
                    food.insertIntoDatabase();
                } else {
                    food = foods.get(preloadedFoods.getSelectedItemPosition());
                }
                ((FragmentResultListener)getActivity()).onFragmentResult(NewFoodFragment.this, food);
                getActivity().onBackPressed();
            }
        });

        addIngredient.performClick();

        return view;
    }

    @Override
    public CharSequence filter(CharSequence charSequence, int start, int end, Spanned spanned, int i2, int i3) {
        try {
            char c = charSequence.charAt(end - 1);
            if (c == Food.INGR_EQUAL || c == Food.INGR_SEPARATOR) return "";
        } catch (StringIndexOutOfBoundsException ignored) {
        }
        return null;
    }

}
