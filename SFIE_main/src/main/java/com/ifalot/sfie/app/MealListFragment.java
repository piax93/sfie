package com.ifalot.sfie.app;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.view.*;
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

public class MealListFragment extends Fragment implements Comparator<Meal>, Toolbar.OnMenuItemClickListener {

    private final static int NEW_MEAL_REQCODE = 11;
    private boolean showAll;
    private MealCalendar calendar;
    private ArrayAdapter<Meal> mealArrayAdapter;

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_meal_list, container, false);

        setHasOptionsMenu(true);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.button_add_meal);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MealListFragment.this.getContext(), NewMeal.class);
                startActivityForResult(i, NEW_MEAL_REQCODE);
            }
        });

        mealArrayAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1);
        ListView lv = (ListView) rootView.findViewById(R.id.meal_list);
        reload();
        lv.setAdapter(mealArrayAdapter);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MealListFragment.this.getContext());
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
                AlertDialog.Builder builder = new AlertDialog.Builder(MealListFragment.this.getContext());
                builder.setTitle(m.toString());
                builder.setMessage(m.getFoodDetailsToString());
                builder.setNeutralButton("Close", null);
                builder.show();
            }
        });

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showAll = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_MEAL_REQCODE && resultCode == Activity.RESULT_OK) {
            Meal m = data.getParcelableExtra(NewMeal.extraNameString);
            calendar.addMeal(m);
            mealArrayAdapter.add(m);
            mealArrayAdapter.sort(this);
            Fridge.getInstance().consume(m);
        }
    }

    private void reload() {
        calendar = MealCalendar.getMealCalendar(showAll ? new Date(0) : Generic.getMidnight());
        mealArrayAdapter.clear();
        mealArrayAdapter.addAll(calendar.getMeals());
    }

    @Override
    public int compare(Meal e1, Meal e2) {
        return (int) (e1.getDateLong() - e2.getDateLong());
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Info");
                SpannableString s = new SpannableString("App by Matteo Piano (ifalot93@gmail.com)");
                Linkify.addLinks(s, Linkify.EMAIL_ADDRESSES);
                builder.setMessage(s);
                builder.show();
                break;
            case R.id.showall:
                showAll = !item.isChecked();
                item.setChecked(showAll);
                reload();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

}
