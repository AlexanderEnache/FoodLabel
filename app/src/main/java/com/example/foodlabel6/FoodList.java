package com.example.foodlabel6;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class FoodList extends AppCompatActivity {

    DBHelper DB = new DBHelper(this);
    Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        delete = (Button) findViewById(R.id.delete);

        Cursor cur = DB.getFoodList();

        if(cur.getCount() == 0){
            System.out.println("Nothing to print");
        }else{
            ListView lv = findViewById(R.id.list);
            ArrayList<String> foodList = new ArrayList<>();

            while(cur.moveToNext()) {
                foodList.add((cur.getString(cur.getColumnIndex("name")) + ";" + cur.getString(cur.getColumnIndex("id"))));
            }

            lv.setAdapter(new FoodListAdapter(this, R.layout.list_item_custom, foodList));
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Delete");
                DB.delete();
            }
        });

    }
}