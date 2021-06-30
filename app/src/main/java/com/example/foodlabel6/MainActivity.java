package com.example.foodlabel6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

//    Button startScan = (Button)findViewById(R.id.startScan);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startScan = (Button)findViewById(R.id.startScan);
        Calories = findViewById(R.id.calories);
        Sugar = findViewById(R.id.sugar);
        Sodium = findViewById(R.id.sodium);
        Protein = findViewById(R.id.protein);

        try {
            loadCalorieData();
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        try {
//            Calories.setText("Calories : " + Integer.toString(loadCalorieData()));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        startScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), Scan.class);
                startActivity(myIntent);
            }
        });

    }


    private int loadCalorieData() throws JSONException {

        DB = new DBHelper(this);
        JSONObject res = DB.getData();

        Calories.setText(Integer.toString(res.getInt("calories")));
        Sugar.setText(Integer.toString(res.getInt("sugar")));
        Sodium.setText(Integer.toString(res.getInt("sodium")));
        Protein.setText(Integer.toString(res.getInt("protein")));

//        System.out.println(Integer.toString(res.getInt("calories")));
//        System.out.println(res.get("sugar"));
//        System.out.println(res.get("sodium"));
//        System.out.println(res.get("protein"));

        SharedPreferences sharedPreferences = getSharedPreferences("FoodLabelCalories", MODE_PRIVATE);
        int cals = sharedPreferences.getInt("Calories", 0);
//        System.out.println(cals);

        return cals;
    }
}