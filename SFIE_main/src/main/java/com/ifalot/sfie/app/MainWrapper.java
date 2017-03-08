package com.ifalot.sfie.app;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.ifalot.sfie.R;
import com.ifalot.sfie.util.Database;

public class MainWrapper extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wrapper);
        Database.initDatabase(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_view, new TabsFragment())
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

}
