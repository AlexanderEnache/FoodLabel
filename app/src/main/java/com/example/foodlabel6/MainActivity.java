package com.example.foodlabel6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    DBHelper DB;
    TextView Calories;
    TextView Sugar;
    TextView Sodium;
    TextView Protein;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startScan = (Button)findViewById(R.id.startScan);
        Button foodList = (Button)findViewById(R.id.foodList);

        Calories = findViewById(R.id.calories);
        Sugar = findViewById(R.id.sugar);
        Sodium = findViewById(R.id.sodium);
        Protein = findViewById(R.id.protein);

        try {
            loadCalorieData();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        startScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), Scan.class);
                startActivity(myIntent);
            }
        });

        foodList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), FoodList.class);
                startActivity(myIntent);
            }
        });

    }

    private void loadCalorieData() throws JSONException {
        DB = new DBHelper(this);
        JSONObject res = DB.getSum();

        Calories.setText("Calories : " + Integer.toString(res.getInt("calories")));
        Sugar.setText("Sugar : " + Integer.toString(res.getInt("sugar")) + "g");
        Sodium.setText("Sodium : " + Integer.toString(res.getInt("sodium")) + "g");
        Protein.setText("Protein : " + Integer.toString(res.getInt("protein")) + "g");
    }
}