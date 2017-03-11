package com.ifalot.sfie.app;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.ifalot.sfie.R;
import com.ifalot.sfie.util.Database;
import com.ifalot.sfie.util.FragmentResultListener;

import java.util.Calendar;
import java.util.List;

public class MainWrapper extends AppCompatActivity implements FragmentResultListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wrapper);
        Database.initDatabase(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_view, new TabsFragment(), TabsFragment.FRAG_TAG)
                .commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager sfm = getSupportFragmentManager();
        if (sfm.getBackStackEntryCount() > 0) {
            sfm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onFragmentResult(Fragment caller, Object result) {
        if(caller instanceof NewFoodFragment){
            ((NewMealFragment)getSupportFragmentManager()
                    .findFragmentByTag(NewMealFragment.FRAG_TAG))
                    .onFragmentResult(caller, result);
        } else if(caller instanceof NewMealFragment){
            List<Fragment> lf = getSupportFragmentManager().getFragments();
            for (Fragment f : lf) {
                if (f instanceof MealListFragment) {
                    ((MealListFragment) f).onFragmentResult(caller, result);
                    break;
                }
            }
        } else {
            Log.d("onFragmentResult", "Unknown calling fragment");
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    protected Dialog onCreateDialog(int id) {
        if (id == NewMealFragment.DIALOG_ID) {
            EditText et = (EditText) findViewById(R.id.meal_date);
            int day, month, year;
            if (et.getText().length() == 0) {
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
            } else {
                String[] tmp = et.getText().toString().split("-");
                year = Integer.parseInt(tmp[0]);
                month = Integer.parseInt(tmp[1]) - 1;
                day = Integer.parseInt(tmp[2]);
            }
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    (NewMealFragment)getSupportFragmentManager().findFragmentByTag(NewMealFragment.FRAG_TAG),
                        year, month, day);
            dpd.setVibrate(false);
            dpd.show(getSupportFragmentManager(), et.getHint().toString().split(" ")[0] + " Date");
        }
        return super.onCreateDialog(id);
    }

}
