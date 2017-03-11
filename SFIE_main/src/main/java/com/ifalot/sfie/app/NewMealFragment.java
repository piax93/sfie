package com.ifalot.sfie.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.ifalot.sfie.R;
import com.ifalot.sfie.model.Food;
import com.ifalot.sfie.model.Meal;
import com.ifalot.sfie.util.FragmentResultListener;
import com.ifalot.sfie.util.Generic;

import java.util.ArrayList;
import java.util.Calendar;

public class NewMealFragment extends Fragment implements DatePickerDialog.OnDateSetListener, View.OnClickListener,
        FragmentResultListener {

    final static String FRAG_TAG = "NewMealFragment";
    final static int DIALOG_ID = 23;

    private long date;
    private String dateString;
    private ArrayList<Food> foods;
    private ArrayList<Boolean> valid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_meal, container, false);

        foods = new ArrayList<>();
        valid = new ArrayList<>();

        final Spinner typeSpinner = (Spinner) view.findViewById(R.id.type_spinner);
        typeSpinner.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, Meal.meal_type));

        EditText mealDate = (EditText) view.findViewById(R.id.meal_date);
        mealDate.setInputType(InputType.TYPE_NULL);
        mealDate.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                getActivity().showDialog(DIALOG_ID);
            }
        });
        mealDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) v.performClick();
            }
        });

        Button addFood = (Button) view.findViewById(R.id.button_add_food);
        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.slide_out_right,
                            android.R.anim.fade_in, android.R.anim.slide_out_right)
                        .add(R.id.main_view, new NewFoodFragment(), NewFoodFragment.FRAG_TAG)
                        .addToBackStack(null)
                        .commit();
            }
        });

        Button saveMeal = (Button) view.findViewById(R.id.button_save_meal);
        saveMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Meal meal = new Meal(date, Meal.meal_type[typeSpinner.getSelectedItemPosition()]);
                for (int i = 0; i < foods.size(); i++) if (valid.get(i)) meal.addFood(foods.get(i));
                if (dateString != null && dateString.length() > 0) {
                    if(meal.getFoodCount() > 0) {
                        ((FragmentResultListener) getActivity()).onFragmentResult(NewMealFragment.this, meal);
                        getActivity().onBackPressed();
                    } else {
                        Generic.fastErrorDialog(NewMealFragment.this.getContext(), "You gotta eat something man!");
                    }
                } else {
                    Generic.fastErrorDialog(NewMealFragment.this.getContext(), "Some fields are emtpy");
                }
            }
        });

        return view;
    }

    @Override
    public void onFragmentResult(Fragment caller, Object result) {
        Food food = (Food) result;
        LinearLayout foodWrap = (LinearLayout) getActivity().findViewById(R.id.foods_wrapper);
        TextView newFoodTV = new TextView(getContext());
        int pad = (int) (12 * getResources().getDisplayMetrics().density + 0.5f);
        newFoodTV.setPadding(pad, pad, pad, pad);
        newFoodTV.setText("\u274C\t" + food.getName());
        newFoodTV.setTextSize(19);
        newFoodTV.setOnClickListener(this);
        newFoodTV.setTag(foods.size());
        foods.add(food);
        valid.add(true);
        foodWrap.addView(newFoodTV);
    }

    @Override
    public void onClick(View view) {
        Integer i = (Integer) view.getTag();
        valid.set(i, false);
        view.setVisibility(View.GONE);
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        if (compareDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
                year, monthOfYear, dayOfMonth)) {
            EditText et = (EditText) getActivity().findViewById(R.id.meal_date);
            dateString = String.valueOf(year) + '-' + (monthOfYear + 1) + '-' + dayOfMonth;
            date = Generic.date2Timestamp(year, monthOfYear, dayOfMonth);
            et.setText(dateString);
        } else {
            Generic.fastErrorDialog(getContext(), "You can't change the past, sorry");
        }
    }

    private boolean compareDate(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) {
        return endYear > startYear
                || (endYear == startYear && endMonth > startMonth)
                || (endYear == startYear && endMonth == startMonth && endDay >= startDay);
    }

}
