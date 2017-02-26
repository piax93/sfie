package com.ifalot.sfie.app;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.ifalot.sfie.R;
import com.ifalot.sfie.model.Fridge;
import com.ifalot.sfie.model.Meal;
import com.ifalot.sfie.model.MealCalendar;
import com.ifalot.sfie.util.Generic;

import java.util.Comparator;
import java.util.Date;

public class MealList extends Fragment implements Comparator<Meal> {

    private final static int NEW_MEAL_REQCODE = 11;
    private MealCalendar calendar;
    private ArrayAdapter<Meal> mealArrayAdapter;

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_meal_list, container, false);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.button_add_meal);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MealList.this.getContext(), NewMeal.class);
                startActivityForResult(i, NEW_MEAL_REQCODE);
            }
        });

        mealArrayAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1);
        update();
        ListView lv = (ListView) rootView.findViewById(R.id.meal_list);
        lv.setAdapter(mealArrayAdapter);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MealList.this.getContext());
                builder.setTitle("Delete Meal");
                builder.setMessage("Are you sure you want to delete '" +
                        mealArrayAdapter.getItem(i).toString() + "' ?");
                builder.setNegativeButton("No", null);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int j) {
                        Fridge.getInstance().vomit(mealArrayAdapter.getItem(i));
                        mealArrayAdapter.getItem(i).deleteFromDatabase();
                        mealArrayAdapter.remove(mealArrayAdapter.getItem(i));
                    }
                });
                builder.show();
                return true;
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Meal m = mealArrayAdapter.getItem(i);
                AlertDialog.Builder builder = new AlertDialog.Builder(MealList.this.getContext());
                builder.setTitle(m.toString());
                builder.setMessage(m.getFoodDetailsToString());
                builder.setNeutralButton("Close", null);
                builder.show();
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NEW_MEAL_REQCODE && resultCode == Activity.RESULT_OK){
            Meal m = data.getParcelableExtra(NewMeal.extraNameString);
            calendar.addMeal(m);
            mealArrayAdapter.add(m);
            mealArrayAdapter.sort(this);
            Fridge.getInstance().consume(m);
        }
    }

    public void update(){
        calendar = MealCalendar.getMealCalendar(getContext().getSharedPreferences("global", Context.MODE_PRIVATE)
                .getBoolean("showAll", false) ? new Date(0) : Generic.getMidnight());
        mealArrayAdapter.clear();
        mealArrayAdapter.addAll(calendar.getMeals());
    }

    @Override
    public int compare(Meal e1, Meal e2) {
        return (int)(e1.getDateLong() - e2.getDateLong());
    }

}
