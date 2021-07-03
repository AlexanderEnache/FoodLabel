package com.example.foodlabel6;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class FoodList extends AppCompatActivity {

    DBHelper DB = new DBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        Cursor cur = DB.getFoodList();

        if(cur.getCount() == 0){
            System.out.println("Nothing to print");
        }else{
            ListView lv = findViewById(R.id.list);
            ArrayList<String> foodList = new ArrayList<>();

            while(cur.moveToNext()) {
//                foodList.add(cur.getString(0));
                foodList.add(cur.getString(cur.getColumnIndex("name")));
//                System.out.println(cur.getString(cur.getColumnIndex("upc")));
            }

            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, foodList);
            lv.setAdapter(arrayAdapter);
        }
    }
}