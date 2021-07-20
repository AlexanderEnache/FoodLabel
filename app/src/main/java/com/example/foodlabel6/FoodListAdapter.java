package com.example.foodlabel6;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.zip.Inflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FoodListAdapter extends ArrayAdapter<String> {
    private int layout;
    DBHelper DB;

    public FoodListAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        layout = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String[] itemAndId = getItem(position).split(";");
        System.out.println(itemAndId[0] + " ID " + itemAndId[1]);
//        System.out.println(getItem(position));

        if(convertView == null){
            LayoutInflater li = LayoutInflater.from(getContext());
            convertView = li.inflate(layout, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.id = itemAndId[1];
            viewHolder.title = (TextView)convertView.findViewById(R.id.name);
            viewHolder.row = (LinearLayout) convertView.findViewById(R.id.row);
            viewHolder.upc = itemAndId[2];
            viewHolder.button = (Button)convertView.findViewById(R.id.delete);
            viewHolder.isDeleted = false;

//            viewHolder.title.setText(getItem(position));

            viewHolder.title.setText(itemAndId[0]);
//            viewHolder.id.setText(itemAndId[1]);

            viewHolder.button.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View v) {
                    if(viewHolder.isDeleted){
                        viewHolder.row.setBackgroundColor(Color.WHITE);
                        viewHolder.title.setTextColor(Color.BLACK);
                        viewHolder.isDeleted = false;
                        viewHolder.button.setText("Delete");

                        getApiData(viewHolder.upc, (String) viewHolder.id);

                    }else{
                        viewHolder.row.setBackgroundColor(Color.GRAY);
                        viewHolder.title.setTextColor(Color.WHITE);
//                        Toast.makeText(getContext(), viewHolder.title.getText(), Toast.LENGTH_SHORT).show();
                        viewHolder.isDeleted = true;
                        viewHolder.button.setText("Undo");

                        DB = new DBHelper(getContext());
                        DB.deleteItem(itemAndId[1]);
                    }
                }
            });
            convertView.setTag(viewHolder);
        }else{
            ViewHolder mainViewHolder = (ViewHolder) convertView.getTag();
            mainViewHolder.title.setText(getItem(position));

            mainViewHolder.title.setText(itemAndId[0]);
//            mainViewHolder.id.setText(itemAndId[1]);
        }
        return convertView;
    }
    public class ViewHolder{
//        int id;
        TextView title;
        LinearLayout row;
        String id;
        String upc;
        boolean isDeleted;
        Button button;
    }

    public void getApiData(String upc, String id){
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
                        DB.insertItemWithID(id, upc, name, currentFoodCalories, currentFoodSugar, currentFoodSodium, currentFoodProtein);

//                        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
//                        startActivity(myIntent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
//                else
            }
        });

    }
}
