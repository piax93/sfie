package com.ifalot.sfie.app;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.ifalot.sfie.R;
import com.ifalot.sfie.model.MealCalendar;
import com.ifalot.sfie.util.Database;

import java.util.Date;

public class MealList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private int lastItemChecked = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_list);
        Database.initDatabase(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.meal_list_toolbar);
        setSupportActionBar(toolbar);
        try { getSupportActionBar().setElevation(18.0f); }
        catch (NullPointerException e) { Log.d("MealList", "Cant set toolbar elevation"); }

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

            }
        });

        // MealCalendar.getMeals(Generic.getMidnight());
        MealCalendar calendar = MealCalendar.getMeals(new Date(0));

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

}
