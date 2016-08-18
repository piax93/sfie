package com.ifalot.sfie.app;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.ifalot.sfie.R;
import com.ifalot.sfie.model.Food;
import com.ifalot.sfie.util.Generic;
import org.parceler.Parcels;

public class NewFood extends AppCompatActivity {

    public final static String newFoodExtraString = "newFood";
    private int ingCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_food);

        Button addIngredient = (Button) findViewById(R.id.button_add_ingredient);
        Button saveFood = (Button) findViewById(R.id.button_save_food);
        final LinearLayout ingWrapper = (LinearLayout) findViewById(R.id.ingredients_wrapper);

        addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLayoutInflater().inflate(R.layout.ingredient_wrapper, ingWrapper);
                ingCount++;
            }
        });

        saveFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText foodName = (EditText) findViewById(R.id.food_name);
                if(foodName.length() == 0){
                    Generic.fastErrorDialog(NewFood.this, "Food name can't be empty");
                    return;
                }
                int filledIngr = 0;
                Food food = new Food(-1, foodName.getText().toString());
                for (int i = 0; i < ingCount; i++){
                    LinearLayout ll = (LinearLayout) ingWrapper.getChildAt(i);
                    EditText ingName = (EditText) ll.getChildAt(0);
                    EditText ingQty = (EditText) ll.getChildAt(1);
                    if(ingName.length() > 0 && ingQty.length() > 0){
                        filledIngr++;
                        food.addIngredient(ingName.getText().toString(), Float.parseFloat(ingQty.getText().toString()));
                    }
                }
                if(filledIngr > 0) {
                    Intent i = getIntent();
                    i.putExtra(newFoodExtraString, Parcels.wrap(food));
                    setResult(RESULT_OK, i);
                    finish();
                }else{
                    Generic.fastErrorDialog(NewFood.this, "Food must contain at least one ingredient");
                }
            }
        });

        addIngredient.performClick();

    }
}
