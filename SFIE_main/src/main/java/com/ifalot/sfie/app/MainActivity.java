package com.ifalot.sfie.app;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.view.MenuItem;
import com.astuetz.PagerSlidingTabStrip;
import com.ifalot.sfie.R;
import com.ifalot.sfie.util.Database;
import com.ifalot.sfie.util.SFIEPageAdapter;

public class MainActivity extends FragmentActivity implements Toolbar.OnMenuItemClickListener {

    private String[] tabs = { "MEAL LIST", "FRIDGE" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Database.initDatabase(this);
        getSharedPreferences("global", MODE_PRIVATE).edit().putBoolean("showAll", false).apply();

        SFIEPageAdapter pageAdapter = new SFIEPageAdapter(getSupportFragmentManager(), tabs);
        ViewPager viewPager = (ViewPager) findViewById(R.id.main_pager);
        viewPager.setAdapter(pageAdapter);
        PagerSlidingTabStrip slidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.pager_title_strip);
        slidingTabStrip.setViewPager(viewPager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle("SFIE");
        toolbar.inflateMenu(R.menu.main);
        toolbar.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.info:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Info");
                SpannableString s = new SpannableString("App by Matteo Piano (ifalot93@gmail.com)");
                Linkify.addLinks(s, Linkify.EMAIL_ADDRESSES);
                builder.setMessage(s);
                builder.show();
                break;
            case R.id.showall:
                boolean check = !item.isChecked();
                getSharedPreferences("global", MODE_PRIVATE).edit().putBoolean("showAll", check).apply();
                item.setChecked(check);
                ((MealList) getSupportFragmentManager().getFragments().get(0)).update();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
