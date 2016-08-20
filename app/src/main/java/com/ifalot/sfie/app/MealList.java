package com.ifalot.sfie.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.ifalot.sfie.R;
import com.ifalot.sfie.model.Meal;
import com.ifalot.sfie.model.MealCalendar;
import com.ifalot.sfie.util.Database;
import com.ifalot.sfie.util.Generic;
import org.parceler.Parcels;

public class MealList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final static int NEW_MEAL_REQCODE = 11;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private int lastItemChecked = 0;
    private MealCalendar calendar;
    private ArrayAdapter<Meal> mealArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_list);
        Database.initDatabase(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.meal_list_toolbar);
        setSupportActionBar(toolbar);
        try { getSupportActionBar().setElevation(18.0f); }
        catch (NullPointerException e) { Log.d("MealList", "Cant set toolbar elevation:" + e.getMessage()); }

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.meal_list_drawer);

        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.openDrawer, R.string.closeDrawer){
            @Override
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                if(lastItemChecked != 0)
                    navigationView.getMenu().findItem(lastItemChecked).setChecked(false);
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.button_add_meal);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MealList.this, NewMeal.class);
                startActivityForResult(i, NEW_MEAL_REQCODE);
            }
        });

        calendar = MealCalendar.getMealCalendar(Generic.getMidnight());

        ListView lv = (ListView) findViewById(R.id.meal_list);
        mealArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, calendar.getMeals());
        lv.setAdapter(mealArrayAdapter);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MealList.this);
                builder.setTitle("Delete Meal");
                builder.setMessage("Are you sure you want to delete '" + mealArrayAdapter.getItem(i).toString() + "' ?");
                builder.setNegativeButton("No", null);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int j) {
                        mealArrayAdapter.getItem(i).deleteFromDatabase();
                        mealArrayAdapter.remove(mealArrayAdapter.getItem(i));
                    }
                });
                builder.show();
                return false;
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        item.setChecked(true);
        lastItemChecked = item.getItemId();
        int id = item.getItemId();
        drawerLayout.closeDrawers();
        switch (id) {
            case R.id.test_item:
                Toast.makeText(this, "Ciaooooo", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NEW_MEAL_REQCODE && resultCode == RESULT_OK){
            Meal m = Parcels.unwrap(data.getParcelableExtra(NewMeal.extraNameString));
            calendar.addMeal(m);
            mealArrayAdapter.add(m);
            Toast.makeText(MealList.this, "Woooooaaaa " + m.getDate(), Toast.LENGTH_SHORT).show();
        }
    }
}
