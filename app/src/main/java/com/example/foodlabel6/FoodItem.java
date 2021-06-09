package com.example.foodlabel6;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class FoodItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item);

        String upc = getIntent().getStringExtra("upc");
//        System.out.println(upc);
    }
}