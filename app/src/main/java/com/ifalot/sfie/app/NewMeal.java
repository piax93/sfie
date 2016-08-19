package com.ifalot.sfie.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.ifalot.sfie.R;
import com.ifalot.sfie.model.Food;
import com.ifalot.sfie.model.Meal;
import com.ifalot.sfie.util.Generic;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;

public class NewMeal extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

    public final static String extraNameString = "newMeal";
    private final static int NEW_FOOD_CODE = 123;
    private final static int DIALOG_ID = 23;
    private final static String[] meal_type = { "Lunch", "Dinner" };

    private long date;
    private String dateString;
    private ArrayList<Food> foods;
    private ArrayList<Boolean> valid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meal);
        setTitle("New Meal");
        foods = new ArrayList<>();
        valid = new ArrayList<>();

        final Spinner typeSpinner = (Spinner) findViewById(R.id.type_spinner);
        typeSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, meal_type));

        EditText mealDate = (EditText) findViewById(R.id.meal_date);
        mealDate.setInputType(InputType.TYPE_NULL);
        mealDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                showDialog(DIALOG_ID);
            }
        });

        Button addFood = (Button) findViewById(R.id.button_add_food);
        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            startActivityForResult(new Intent(NewMeal.this, NewFood.class), NEW_FOOD_CODE);
            }
        });

        Button saveMeal = (Button) findViewById(R.id.button_save_meal);
        saveMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Meal meal = new Meal(date, meal_type[typeSpinner.getSelectedItemPosition()]);
                for(int i = 0; i < foods.size(); i++) if(valid.get(i)) meal.addFood(foods.get(i));
                if(meal.getFoodCount() > 0 && dateString != null && dateString.length() > 0) {
                    Intent i = getIntent();
                    i.putExtra(extraNameString, Parcels.wrap(meal));
                    setResult(Activity.RESULT_OK, i);
                    finish();
                }else{
                    Generic.fastErrorDialog(NewMeal.this, "Some fields are emtpy");
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NEW_FOOD_CODE && resultCode == RESULT_OK) {
            Food food = Parcels.unwrap(data.getParcelableExtra(NewFood.newFoodExtraString));
            LinearLayout foodWrap = (LinearLayout) findViewById(R.id.foods_wrapper);
            TextView newFoodTV = new TextView(this);
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
    }

    @Override
    public void onClick(View view) {
        Integer i = (Integer) view.getTag();
        valid.set(i, false);
        view.setVisibility(View.GONE);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == DIALOG_ID){
            EditText et = (EditText) findViewById(R.id.meal_date);
            int day, month, year;
            if(et.getText().length() == 0){
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
            }else{
                String[] tmp = et.getText().toString().split("-");
                year = Integer.parseInt(tmp[0]);
                month = Integer.parseInt(tmp[1]) - 1;
                day = Integer.parseInt(tmp[2]);
            }
            DatePickerDialog dpd = DatePickerDialog.newInstance(this, year, month, day);
            dpd.setVibrate(false);
            dpd.show(this.getSupportFragmentManager(), et.getHint().toString().split(" ")[0] + " Date");
        }
        return super.onCreateDialog(id);
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        if(compareDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
                year, monthOfYear, dayOfMonth)) {
            EditText et = (EditText) findViewById(R.id.meal_date);
            dateString = String.valueOf(year) + '-' + (monthOfYear + 1) + '-' + dayOfMonth;
            date = Generic.date2Timestamp(year, monthOfYear, dayOfMonth);
            et.setText(dateString);
        }
    }

    private boolean compareDate(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay){
        return endYear > startYear
                || (endYear == startYear && endMonth > startMonth)
                || (endYear == startYear && endMonth == startMonth && endDay >= startDay);
    }
}
