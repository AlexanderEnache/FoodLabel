package com.example.foodlabel6;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class FoodItem extends AppCompatActivity {

    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item);

        Button submit = findViewById(R.id.submit);
        EditText servings = findViewById(R.id.servings);

        DB = new DBHelper(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(servings.getText());
                getData(Integer.parseInt(servings.getText().toString()));
            }
        });

    }

    public void getData(int serving){

        String upc = getIntent().getStringExtra("upc");

        OkHttpClient client = new OkHttpClient();
        String url = "https://trackapi.nutritionix.com/v2/search/item?x-app-id=28dbe9b2&x-app-key=fabfe2466869329f5f532fcee0fb54fc&upc="+upc;

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    try {
                        JSONObject wholeObject = new JSONObject(response.body().string());
                        JSONArray foods = wholeObject.getJSONArray("foods");
                        JSONObject foodsObj = foods.getJSONObject(0);
                        String name = foodsObj.getString("food_name");
                        int currentFoodCalories = Integer.parseInt(foodsObj.getString("nf_calories"));
                        int currentFoodSugar = Integer.parseInt(foodsObj.getString("nf_sugars"));
                        int currentFoodSodium = Integer.parseInt(foodsObj.getString("nf_sodium"));
                        int currentFoodProtein = Integer.parseInt(foodsObj.getString("nf_protein"));
                        saveData(upc, name, currentFoodCalories, currentFoodSugar, currentFoodSodium, currentFoodProtein);

                        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(myIntent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
//                else
            }
        });

    }
    private void saveData(String upc, String name, int cal, int sugar, int sodium, int protein){
        DB.insertFoodData(upc, name, cal, sugar, sodium, protein);

        SharedPreferences sharedPreferences = getSharedPreferences("FoodLabelCalories", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("Calories", cal);
        editor.apply();
    }

    private int loadCalorieData(){
        SharedPreferences sharedPreferences = getSharedPreferences("FoodLabelCalories", MODE_PRIVATE);
        int cals = sharedPreferences.getInt("Calories", 0);
//        System.out.println(cals);

        return cals;
    }

}