package com.ifalot.sfie.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import com.ifalot.sfie.R;
import com.ifalot.sfie.model.Meal;
import org.parceler.Parcels;

public class MealDetail extends AppCompatActivity {

    public static final String mealData = "mealData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_detail);

        Meal meal = Parcels.unwrap(getIntent().getParcelableExtra(mealData));
        setTitle(meal.toString());

        TextView tv = (TextView) findViewById(R.id.meal_detail_textview);
        tv.setText(meal.getFoodDetails());

    }
}
